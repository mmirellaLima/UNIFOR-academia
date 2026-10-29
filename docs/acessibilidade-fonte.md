# Acessibilidade de fonte

Todos os atalhos de fonte nas telas XML usam o painel `feed_font_popup`, sem abrir uma Activity de pendência.
O tamanho é salvo em `AcessibilidadePrefs`, respeitando também o tamanho de fonte do sistema.
`FontAccessibility` acompanha o ciclo da tela, reaplica o valor ao retornar e fecha o painel ao sair.
As telas que herdam `BaseActivity` recebem o ajuste automaticamente; as demais registram o mesmo controlador.

## Telas verificadas

- Feed do aluno
- Configurações
- Objetivos
- Dados pessoais
- Editar e-mail
- Alterar senha
- Notificações
- Feed do administrador
- Agenda do administrador
- Treinos do administrador

## Teste de interface

`FontAccessibilityTest` abre as dez telas em um emulador, aciona o botão de fonte, verifica o painel visível,
altera a barra, confere a preferência e a descrição acessível da porcentagem, recria a tela e reabre o painel.
A preferência original do aparelho é restaurada ao terminar o teste.

Executar: `gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.myapplication.navigation.FontAccessibilityTest`

A barra inferior e as demais conexões da dev não são substituídas por esta correção.
