# Feed do administrador

Branch baseada em `dev` (`42b98a2`), adaptada da entrega `2ff9c3d` de JpMuniz21/Academia_UNIFOR.

## Executar

Abra a raiz deste projeto no Android Studio, sincronize o Gradle e execute o módulo `app` em um dispositivo API 26 ou superior. Nesta branch, o launcher abre `admin.FeedAdministradorActivity`.

Apresentação: próxima atividade → detalhes → seletores de professor e status; aulas de hoje → detalhes; adicionar atividade → seletores de horário, vagas, professor e calendário. Os botões só navegam e fecham as prévias: não cadastram nem cancelam aulas.

## Compatibilidade com a equipe

- SDK 37, minSdk 26, AGP 9.4.1, Gradle 9.6.0, JDK 25 e todas as dependências permanecem iguais à `dev`.
- Activities Kotlin, layouts XML, `findViewById` e insets seguem a estrutura existente.
- Fonte `sans-serif`, azul `#3E46F4` e fundo `#191A1E` seguem `feat/feed`.
- Reutilizados ícones `ic_voltar`, `ic_inicio`, `ic_treinos`, `ic_perfil`, `ic_fonte`, `logo_chat` e cores compartilhadas da `dev`.
- Navegação e botão usam dimensões da equipe; cartões, painéis compactos e calendário conservam o desenho da entrega administrativa.
- Pacote `admin`, IDs e recursos `admin_` não substituem os componentes de `feat/feed` nem de `feat/questionario-2`. O tema fica restrito a esta entrega.
- As telas de treinos, chat e configurações da equipe permanecem disponíveis pela navegação inferior; o launcher desta branch é a prévia administrativa.

## Validação

`gradlew.bat :app:assembleDebug :app:lintDebug --offline --console=plain --max-workers=2 -Pkotlin.compiler.execution.strategy=in-process` passou com JDK 25 e as dependências originais. Lint sem erros; permanecem avisos de apresentação nas telas do projeto.

As únicas correções na base compartilhada são o fechamento do `startActivity` em `ExercicioSeletActivity`, os IDs `btnPerfil` ausentes em configurações/objetivos e as constraints do botão de retorno em meus treinos. Elas permitem compilar e navegar para as telas existentes sem crashes, preservando seu desenho.
