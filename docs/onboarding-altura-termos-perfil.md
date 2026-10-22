# Onboarding, termos, altura e perfil

Branch baseada em `dev` (`42b98a2`), adaptada da entrega `b9762b3` de JpMuniz21/Academia_UNIFOR.

## Executar

Abra a raiz deste projeto no Android Studio, sincronize o Gradle e execute o módulo `app` em um dispositivo API 26 ou superior. Nesta branch, a tela inicial é `onboarding.IntroActivity`.

Fluxo de apresentação: introdução → termos → seleção visual de altura → perfil. O seletor conserva a animação e o estado durante a rotação. Os dados são demonstrativos, sem cadastro, autenticação ou gravação de perfil.

## Compatibilidade com a equipe

- Mantidos integralmente SDK 37, minSdk 26, AGP 9.4.1, Gradle 9.6.0, JDK 25 e dependências da `dev`.
- Activities Kotlin, XML, `findViewById` e insets como as telas existentes.
- Fonte `sans-serif`, botões de 54dp e paleta do questionário da branch `feat/questionario-2`.
- `RegistrationBackgroundView` reproduz as mesmas coordenadas, traços e cor `#A4BFFF` do questionário dos colegas; o banner do perfil conserva suas formas próprias.
- Pacote `onboarding`, recursos `ob_` e tema local evitam substituir estilos e ícones compartilhados. Seta de retorno reutiliza `ic_voltar` da `dev`.
- Todas as telas originais de treinos, chat e configurações continuam registradas. O launcher só aponta para esta entrega enquanto ela é revisada nesta branch.

## Validação

`gradlew.bat :app:assembleDebug :app:lintDebug --offline --console=plain --max-workers=2 -Pkotlin.compiler.execution.strategy=in-process` passou com JDK 25 e as dependências originais. Lint sem erros; permanecem avisos de apresentação nas telas do projeto.

As únicas correções na base compartilhada são o fechamento do `startActivity` em `ExercicioSeletActivity`, os IDs `btnPerfil` ausentes em configurações/objetivos e as constraints do botão de retorno em meus treinos. Elas permitem compilar e navegar para as telas existentes sem crashes, preservando seu desenho.
