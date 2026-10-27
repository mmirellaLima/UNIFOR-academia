"""Local UI checks. Install the debug APK on a test emulator before running.

Uses only adb and the Python standard library; screenshots/reports go to app/build.
Run: python tools/navigation_smoke.py --phase student|registration|admin
The registration phase uses fictional data and starts from the welcome screen.
"""
import argparse
import json
import os
from pathlib import Path
import re
import subprocess
import time
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "app/build/reports/navigation"
OUT.mkdir(parents=True, exist_ok=True)
SDK = Path(os.environ.get("ANDROID_HOME", Path(os.environ["LOCALAPPDATA"]) / "Android/Sdk"))
ADB = SDK / "platform-tools/adb.exe"
PACKAGE = "com.example.myapplication"
SERIAL = os.environ.get("ANDROID_SERIAL", "emulator-5554")
CHECKS = []


def adb(*args, check=True):
    result = subprocess.run([str(ADB), "-s", SERIAL, *args], capture_output=True, timeout=60)
    if check and result.returncode:
        raise AssertionError(result.stdout.decode(errors="replace") + result.stderr.decode(errors="replace"))
    return result.stdout


def ui():
    for attempt in range(4):
        try:
            result = adb("shell", "uiautomator", "dump", "--compressed", "/sdcard/flow-test.xml")
            if b"UI hierchary dumped" in result:
                return ET.fromstring(adb("shell", "cat", "/sdcard/flow-test.xml"))
        except (AssertionError, subprocess.TimeoutExpired, ET.ParseError):
            if attempt == 3:
                raise
        time.sleep(.5)
    raise AssertionError("Unable to read current UI")


def find(id=None, text=None, desc=None, scroll=False):
    for attempt in range(7 if scroll else 1):
        for node in ui().iter("node"):
            if ((id and node.get("resource-id", "").endswith(":id/" + id))
                    or (text and node.get("text") == text) or (desc and node.get("content-desc") == desc)):
                bounds = list(map(int, re.findall(r"\d+", node.get("bounds", ""))))
                if len(bounds) == 4 and bounds[2] > bounds[0] and bounds[3] > bounds[1]:
                    return node
        if scroll:
            width, height = size()
            adb("shell", "input", "swipe", str(width // 2), str(height * 3 // 5),
                str(width // 2), str(height // 4), "400")
            time.sleep(.4)
    raise AssertionError("Missing " + str(id or text or desc))


def size():
    values = re.findall(r"(\d+)x(\d+)", adb("shell", "wm", "size").decode())
    return tuple(map(int, values[-1]))


def tap(id=None, text=None, desc=None, scroll=False):
    node = find(id, text, desc, scroll)
    left, top, right, bottom = map(int, re.findall(r"\d+", node.get("bounds")))
    adb("shell", "input", "tap", str((left + right) // 2), str((top + bottom) // 2))
    time.sleep(.7)


def back():
    adb("shell", "input", "keyevent", "KEYCODE_BACK")
    time.sleep(.7)


def shot(name):
    (OUT / (name + ".png")).write_bytes(adb("exec-out", "screencap", "-p"))


def record(name):
    CHECKS.append(name)
    print("PASS " + name, flush=True)


def selected_period(label):
    tree = ui()
    parents = {child: parent for parent in tree.iter() for child in parent}
    for node in tree.iter("node"):
        if node.get("text") == label:
            current = node
            while current is not None:
                if current.get("selected") == "true" or current.get("checked") == "true":
                    return
                current = parents.get(current)
    raise AssertionError("Period is not selected: " + label)


def blank():
    tree = ui()
    assert any(n.get("resource-id", "").endswith(":id/flow_back") for n in tree.iter("node"))
    assert not any(n.get("text", "").strip() for n in tree.iter("node")), "Pending content must be empty"


def type_field(index, value):
    fields = [n for n in ui().iter("node") if n.get("class") == "android.widget.EditText"]
    node = fields[index]
    left, top, right, bottom = map(int, re.findall(r"\d+", node.get("bounds")))
    adb("shell", "input", "tap", str((left + right) // 2), str((top + bottom) // 2))
    time.sleep(.8)  # Let the input connection and keyboard settle before typing.
    adb("shell", "input", "keycombination", "113", "29")  # Ctrl+A, replace an existing draft.
    adb("shell", "input", "text", value.replace(" ", "%s"))
    time.sleep(.4)


def login(role):
    for _ in range(4):
        tree = ui()
        if any(n.get("text") == "Login" for n in tree.iter("node")):
            tap(text="Login")
            break
        if any(n.get("text") == "Olá, acesse sua conta" for n in tree.iter("node")):
            break
        time.sleep(.5)
    else:
        raise AssertionError("Expected welcome or login")
    tap(text="Entrar")
    find(text="Preencha este campo.")
    type_field(0, "2512000")
    back()
    type_field(1, "teste123")
    back()  # keyboard
    tap(text="Esqueceu sua senha?")
    blank()
    tap(id="flow_back")
    find(text="2512000")
    tap(text="Entrar")
    tap(text=role)


def student():
    login("Aluno já configurado")
    find(id="feed_start")
    tap(id="feed_start")
    blank()
    back()
    find(id="feed_start")
    tap(id="feed_font")
    blank()
    back()
    tap(id="feed_nav_progress")
    tap(text="1 mês")
    tap(desc="MoemIA Fit")
    find(id="etMensagem")
    type_field(0, "Treino de hoje")
    back()
    tap(id="btnEnviar")
    find(text="Treino de hoje")
    tap(desc="Progresso")
    find(text="1 mês")
    find(text="12")
    tap(desc="Treinos")
    find(id="btnTreinoA")
    for letter in "ABC":
        tap(id="btnTreino" + letter)
        blank()
        back()
    tap(desc="Perfil")
    find(text="Lucas Vasconcelos")
    tap(id="ob_button_add_objetivo")
    blank()
    back()
    tap(id="ob_button_add_resultado", scroll=True)
    find(text="Meus Resultados")
    back()
    width, height = size()
    for _ in range(3):
        adb("shell", "input", "swipe", str(width // 2), str(height // 3), str(width // 2), str(height * 4 // 5), "300")
    tap(id="ob_button_settings")
    find(id="btnQuestionario")
    for id in ("btnDadosPessoais", "btnNotificacoes", "btnObjetivos", "btnFonte"):
        tap(id=id)
        blank()
        back()
    tap(id="btnQuestionario")
    for _ in range(10):
        tap(text="Não")
    find(id="ob_checkbox_termos")
    tap(id="ob_checkbox_termos")
    find(id="btnQuestionario")
    shot("student-settings")
    record("Student tabs, pending destinations and health-only review")
    tap(id="btnSair")
    tap(text="Cancelar")
    find(id="btnSair")
    tap(id="btnSair")
    tap(text="Sair")
    find(text="Olá, acesse sua conta")
    back()
    find(text="Login")
    record("Logout clears the internal back stack")


def admin():
    if not any(n.get("resource-id", "").endswith(":id/admin_nav_home") for n in ui().iter("node")):
        login("Administrador")
    find(id="admin_view_activity")
    tap(id="admin_view_activity")
    find(id="admin_teacher_selector")
    back()
    for id in ("admin_nav_agenda", "admin_nav_training", "admin_nav_profile", "admin_accessibility_badge"):
        tap(id=id, scroll=(id == "admin_accessibility_badge"))
        blank()
        back()
    tap(id="admin_nav_chat")
    find(id="etMensagem")
    find(text="Agenda")
    tap(desc="Início")
    find(id="admin_add_activity")
    shot("admin-home")
    record("Administrator feed and chat remain separate from student data")
    back()
    tap(text="Sair")
    back()
    find(text="Login")


def resilience():
    if not any(n.get("text") == "Meus Resultados" for n in ui().iter("node")):
        login("Aluno já configurado")
        tap(id="feed_nav_progress")
    tap(text="1 mês")
    rotation = adb("shell", "settings", "get", "system", "user_rotation").decode().strip()
    accelerometer = adb("shell", "settings", "get", "system", "accelerometer_rotation").decode().strip()
    try:
        adb("shell", "settings", "put", "system", "accelerometer_rotation", "0")
        adb("shell", "settings", "put", "system", "user_rotation", "1")
        time.sleep(2)
        selected_period("1 mês")
        shot("progress-landscape")
    finally:
        adb("shell", "settings", "put", "system", "user_rotation", rotation)
        adb("shell", "settings", "put", "system", "accelerometer_rotation", accelerometer)
    time.sleep(1)
    selected_period("1 mês")
    record("Progress period survives Activity recreation on rotation")
    tap(desc="Treinos")
    old_size = adb("shell", "wm", "size").decode()
    old_density = adb("shell", "wm", "density").decode()
    try:
        adb("shell", "wm", "size", "720x1280")
        adb("shell", "wm", "density", "360")
        time.sleep(2)
        tap(id="btnTreinoC", scroll=True)
        blank()
        back()
        shot("workouts-320dp")
        tap(desc="Progresso")
        find(text="12")
        tap(desc="Treinos")
        tap(desc="Progresso")
        stack = adb("shell", "dumpsys", "activity", "activities").decode()
        records = [line for line in stack.splitlines() if "* Hist" in line and PACKAGE in line]
        assert sum("ProgressActivity" in line for line in records) == 1, records
        assert sum("MeusTreinosActivity" in line for line in records) == 1, records
        record("320dp scrolling, pending workout and tab reuse without duplicate Activities")
    finally:
        sizes = re.findall(r"Override size: (\d+x\d+)", old_size)
        densities = re.findall(r"Override density: (\d+)", old_density)
        adb("shell", "wm", "size", sizes[-1] if sizes else "reset")
        adb("shell", "wm", "density", densities[-1] if densities else "reset")
    time.sleep(1)
    tap(desc="Início")
    back()
    tap(text="Sair")
    adb("shell", "am", "force-stop", PACKAGE)
    adb("shell", "am", "start", "-W", "-n", PACKAGE + "/.MainActivity")
    time.sleep(1)
    assert not any(n.get("resource-id", "").endswith(":id/feed_start") for n in ui().iter("node"))
    record("Logout remains effective after process restart")
    if any(n.get("text") == "Olá, acesse sua conta" for n in ui().iter("node")):
        back()


def registration():
    tap(text="Cadastre-se")
    for index, value in enumerate(("Maria Teste", "maria@exemplo.com", "1234567", "teste123")):
        type_field(index, value)
        if index < 3:
            back()
    back()
    tap(text="Entrar", scroll=True)
    back()
    find(text="maria@exemplo.com")
    find(text="1234567")
    tap(text="Entrar", scroll=True)
    record("Registration form is preserved when returning from Age")
    tap(text="Avançar")
    tap(text="Avançar")
    find(id="ob_height_picker_container")
    assert "Maria" in find(id="ob_altura_title").get("text", "")
    tap(id="ob_button_avancar")
    find(id="ob_intro_title_view")
    tap(id="ob_button_avancar")
    tap(text="Sim")
    back()
    find(text="Sim")
    tap(text="Não")
    for _ in range(9):
        tap(text="Não")
    find(id="ob_checkbox_termos")
    tap(id="ob_checkbox_termos")
    tap(id="q2_option_instagram")
    tap(id="q2_next_button")
    back()
    assert find(id="q2_option_instagram").get("checked") == "true"
    tap(id="q2_next_button")
    tap(id="q2_option_professional")
    tap(id="q2_next_button")
    tap(id="q2_option_frequency_3")
    tap(id="q2_next_button")
    type_field(0, "3 meses")
    back()
    tap(id="q2_next_button")
    tap(id="q2_experience_no")
    tap(id="q2_next_button")
    tap(id="q2_injury_no")
    tap(id="q2_next_button")
    tap(id="q2_pain_no")
    tap(id="q2_next_button")
    tap(id="q2_smoking_no")
    tap(id="q2_alcohol_no")
    tap(id="q2_next_button")
    tap(id="q2_surgery_no")
    tap(id="q2_next_button")
    shot("registration-completion")
    tap(id="q2_next_button")
    blank()
    shot("registration-goals-reserved")
    record("Registration reaches reserved Goals without bypassing the unfinished screen")
    adb("shell", "am", "force-stop", PACKAGE)
    adb("shell", "monkey", "-p", PACKAGE, "-c", "android.intent.category.LAUNCHER", "1")
    time.sleep(2)
    blank()
    back()
    find(id="q2_next_button")
    record("Cold start resumes the exact pending step with a working Back stack")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--phase", choices=("student", "registration", "admin", "resilience"), required=True)
    phase = parser.parse_args().phase
    try:
        globals()[phase]()
    except Exception:
        shot(phase + "-failure")
        (OUT / (phase + "-failure.xml")).write_bytes(adb("shell", "cat", "/sdcard/flow-test.xml"))
        raise
    finally:
        (OUT / (phase + ".json")).write_text(json.dumps(CHECKS, ensure_ascii=False, indent=2), encoding="utf-8")
