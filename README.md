# L’Oyapock Food — versão Android para Google Play

Aplicativo Android nativo em Kotlin + Jetpack Compose.

## Objetivo
Base preparada para evoluir até publicação na Google Play:
- targetSdk 36 (Android 16)
- minSdk 24
- interface em francês
- Saint-Georges-de-l’Oyapock
- catálogo, carrinho, checkout, pedidos e conta
- taxa de entrega: 5 €
- estimativa: 20–40 min

## Importante
Esta primeira versão é uma base funcional local. O pagamento online, banco de dados, autenticação real, notificações e painel administrativo ainda precisam ser ligados a serviços reais antes da publicação comercial.

## Build sem Android Studio
O repositório inclui GitHub Actions. O workflow `Build Android` gera o AAB de release no servidor do GitHub.

Para publicação real, será necessário configurar uma chave de assinatura de upload e os dados da Play Console.
