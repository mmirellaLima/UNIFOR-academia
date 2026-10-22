# Questionário — parte 2

As telas ficam no pacote `com.example.myapplication.questionario2`.
Os recursos usam o prefixo `q2_` para evitar conflitos com outras partes do aplicativo.

## Como acessar

Abra o aplicativo e siga: Meus Treinos → Perfil → Questionário.
A entrada desta etapa é `Questionario2Activity`. A primeira parte do cadastro pode abrir essa Activity usando um Intent.

## Sequência

Como conheceu → Motivo da escolha → Frequência → Tempo sem exercícios → Experiência → Lesão → Dor → Cigarro e álcool → Cirurgia → Conclusão.
Na conclusão, Iniciar abre a `ObjetivosActivity` existente no projeto.

## Comportamento

- As setas retornam à tela anterior.
- As perguntas precisam de uma resposta antes de avançar.
- Frequência aceita uma opção; origem e motivo aceitam várias.
- Lesão, dor e cirurgia pedem detalhes somente quando a resposta é Sim.
- Cigarro e álcool têm respostas independentes.
- Os campos usam o estado das telas; não há persistência em banco de dados.

## Integração

A feature parte da branch dev e mantém as configurações de Android e Gradle do grupo.
O aplicativo continua abrindo em Meus Treinos. Nenhuma Activity do questionário adiciona outro ícone de inicialização.
