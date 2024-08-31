# Wishlist Service

Este projeto é um microserviço para gerenciar a **Wishlist** de um cliente em um e-commerce. O serviço foi desenvolvido usando **Spring Boot** e utiliza **MongoDB** como banco de dados NoSQL.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **MongoDB**
- **Docker**
- **Docker Compose**

## Funcionalidades

- Adicionar um produto na Wishlist do cliente.
- Remover um produto da Wishlist do cliente.
- Consultar todos os produtos da Wishlist do cliente.
- Consultar se um determinado produto está na Wishlist do cliente.

## Como Executar o Projeto

### Pré-requisitos

- **Docker** e **Docker Compose** instalados na sua máquina.

### Passos para Iniciar a Aplicação

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/wcosme/wishlist-api.git

2. **Build da aplicação e iniciar os containers:**

No diretório raiz do projeto, execute o seguinte comando para construir a imagem da aplicação e iniciar os containers.

- docker-compose up --build

3. **Acessar a aplicação:**

Após os containers serem iniciados, a aplicação estará disponível em http://localhost:8080.

## Endpoints Disponíveis

- POST /api/wishlist/{clientId}/products -> Adicionar um produto na Wishlist

Exemplo de request body:

```json
{
  "productId": "12345",
  "name": "Produto Exemplo"
}
```

- DELETE /api/wishlist/{clientId}/products/{productId} -> Remover um produto da Wishlist
- GET /api/wishlist/{clientId}/products -> Consultar todos os produtos da Wishlist
- GET /api/wishlist/{clientId}/products/{productId} -> Verificar se um produto está na Wishlist

## Configurações Adicionais

- SPRING_DATA_MONGODB_URI: URI de conexão com o MongoDB. No Docker Compose, já está configurada para mongodb://mongo:27017/wishlist.