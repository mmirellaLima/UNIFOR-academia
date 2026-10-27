# Integração local das telas e navegação

Branch de trabalho: `feat/conexoes-fluxos`. Base preservada: `dev@42b98a2`.
Esta implementação não deve receber push até nova autorização explícita do usuário.
Nenhum merge na dev faz parte desta entrega.

## Abrir no Android Studio

Abra a pasta raiz deste worktree, que contém `settings.gradle.kts`, `gradlew.bat` e `app/`.
Não abra somente a pasta app nem a cópia do repositório antigo.

1. Aguarde o Gradle Sync. O projeto mantém AGP 9.4.1, Gradle 9.6.0, JDK 25,
   compileSdk/targetSdk 37 e minSdk 26 da dev.
2. Use o JBR 25 do Android Studio como Gradle JDK e o SDK Android local.
   `local.properties` é específico de cada computador e fica fora do Git.
3. Selecione o módulo app e um emulador/dispositivo. A única launcher Activity é
   `com.example.myapplication.MainActivity`.
4. Execute app. Não é necessário iniciar Activities isoladas para acessar as telas.

O primeiro Sync precisa de rede para baixar as dependências Compose das telas dos
colegas. O compilador Compose 2.2.10 corresponde ao Kotlin embutido no AGP da base.
As dependências adicionadas estão fixadas no catálogo de versões.

## Cenários visuais disponíveis

- **Aluno já configurado:** Boas-vindas → Login. Preencha matrícula e senha com
  valores fictícios não vazios, toque Entrar e escolha Aluno já configurado no
  diálogo identificado como demonstração. Abre Início, com acesso a Progresso,
  MoemIA Fit, Treinos, Perfil e Configurações.
- **Administrador:** no mesmo diálogo, escolha Administrador. Abre o feed
  administrativo entregue, seus painéis existentes e o Chat. Agenda, Treinos e
  Perfil administrativos permanecem reservados.
- **Novo cadastro:** Boas-vindas → Cadastre-se → idade → peso → altura →
  introdução → 10 perguntas de saúde → declaração → Parte 2 → conclusão →
  Objetivos reservado. Não se considera que o aluno concluiu a configuração.
- **Retomada:** após interromper o cadastro, abrir o app retoma a etapa pendente.
  Depois de sair, o diálogo de demonstração também oferece Retomar meu cadastro
  quando existir rascunho. O histórico de predecessores é reconstruído para
  permitir Voltar após reiniciar o processo.
- **Refazer saúde:** Perfil → Configurações → Refazer questionário de saúde →
  10 perguntas → declaração → Configurações. Não repete dados iniciais nem Parte 2.
- **Saída:** Configurações → Sair da conta, ou Voltar na raiz de um feed →
  confirmação. A saída limpa o histórico de telas internas e abre Login.

Não há autenticação real, envio de e-mail, API, Toolfit nem gravação de senha.
Nome, medidas, respostas e escolhas são estado local de apresentação. O aluno
configurado e a administradora usam identidades de demonstração separadas.

## Destinos reservados

`PendingDestination` identifica cada entrega em um único `PendingActivity`.
O conteúdo é vazio, com apenas o mecanismo comum de Voltar. Os parâmetros
`destination` e `selection` identificam o destino e o treino/exercício escolhido
para a futura entrega do colega.

Reservados por orientação do usuário: recuperação de senha, objetivos, detalhes
de treino e exercício, tamanho da fonte, edição de e-mail/alteração de senha e
áreas administrativas ainda em produção.

Dados pessoais e Notificações também não têm uma entrega pronta nas referências
consultadas: seus acessos em Configurações abrem destinos vazios. As rotas de
editar e-mail/alterar senha estão reservadas para serem conectadas quando a tela
de Dados pessoais for entregue; não foram inventados formulários ou um menu novo.

Os antigos fontes de TreinoAActivity, ExercicioSeletActivity e ObjetivosActivity
continuam no repositório, mas não são destinos registrados da integração atual.
Suas versões incompletas não são abertas pelos atalhos integrados.

## Organização e preservação das entregas

- XML/Activities dos colegas permanecem em onboarding, questionario2, feed e admin.
- Compose mantém os componentes e telas de apresentação/cadastro/idade/peso,
  saúde e progresso. Não foram importados launchers ou gerenciadores duplicados.
- `AppNavigation` centraliza transições entre as duas estruturas, seleção de área,
  destinos reservados, revisão de saúde e limpeza da pilha na saída.
- `DemoSession` guarda somente o estado de demonstração compartilhado.
- As abas reutilizam a Activity existente com REORDER_TO_FRONT; rolagem,
  período de resultados e texto do Chat são mantidos durante as trocas.
- A barra nativa entregue é compartilhada com Perfil e Chat. Configurações fica
  na engrenagem de Perfil. O ícone central usa o vetor recortado logo_chat.
- O fundo padronizado bg_shapes_completo da entrega 0c86e96 é preservado e
  compartilhado entre dados iniciais e questionários.
- Foram corrigidos o parêntese ausente de ExercicioSeletActivity, IDs de Perfil
  ausentes e as constraints de Voltar de Meus Treinos. Os cartões de Treinos
  ganharam rolagem para não sobrepor a barra em telas baixas.

As referências exatas de cada entrega estão no mapa `fluxo-completo-app.md`.
Nenhuma branch de colega é alterada por esta integração.

## Verificações locais

Com JDK 25 e SDK 37 instalados:

```powershell
.\gradlew.bat :app:assembleDebug :app:lintDebug :app:testDebugUnitTest --console=plain
```

`FlowStateTest` cobre ausência de acesso, aluno configurado/incompleto e separação
do administrador. O lint da integração não usa baseline nem supressão geral de
erros. Avisos de recursos/fontes antigos das entregas ficam visíveis no relatório.

`tools/navigation_smoke.py` verifica percursos reais por adb, usando somente a
biblioteca padrão do Python. Execute em emulador de teste, com o APK debug instalado,
partindo de Boas-vindas/Login. As fases student e admin saem da conta ao terminar;
registration para em Objetivos reservado.

```powershell
$env:ANDROID_SERIAL = 'emulator-5556'
python tools/navigation_smoke.py --phase student
python tools/navigation_smoke.py --phase admin
python tools/navigation_smoke.py --phase resilience
python tools/navigation_smoke.py --phase registration
```

Capturas e relatórios JSON ficam em `app/build/reports/navigation/`, fora do Git.
Não usar o emulador simultaneamente com a automação; tutoriais do teclado do
Android devem ser fechados antes de verificar digitação.

## Resultado da validação em 27/10/2026

- assembleDebug, lintDebug e testDebugUnitTest concluídos com sucesso.
- Quatro testes de FlowState aprovados, cobrindo entrada e separação das áreas.
- Lint com zero erros e 320 avisos, mantidos visíveis; não há baseline para ocultá-los.
- Aluno: abas, destinos reservados, Chat, Perfil, Configurações, revisão somente
  de saúde e limpeza do histórico na saída verificados no emulador.
- Administrador: feed e painel existente, abas reservadas, Chat com Agenda e
  atalho de fonte para destino vazio verificados.
- Progresso conserva o período após recriar a Activity em rotação. Em 320 dp,
  Treino C continua acessível por rolagem; as trocas de aba mantêm uma única
  Activity de Progresso e uma de Treinos no histórico.
- A saída permanece efetiva após reiniciar o processo.
- Cadastro: formulário preservado ao voltar de Idade, nome compartilhado em
  Altura, dez perguntas de saúde, termos e todas as etapas da Parte 2 percorridos.
- Objetivos permanece vazio. Reabrir o app retoma essa etapa; Voltar retorna à
  conclusão. Ao reconstruir o histórico, os termos aceitos e a seleção da Parte 2
  são preservados. Tocar novamente no cartão dos termos permite continuar.
- Nenhuma falha do processo do app foi registrada no percurso final de cadastro.

Ambiente: emuladores Android API 37, orientação retrato/paisagem e largura de
320 dp. Não foram executados testes em aparelho físico ou outras versões do Android.
As telas pendentes continuam impedindo a conclusão real do primeiro acesso,
conforme a orientação de não desenvolvê-las nesta branch.
