# Wishlist Service

Este projeto é um microserviço para gerenciar a **Wishlist** de um cliente em um e-commerce. O serviço foi desenvolvido usando **Spring Boot** e utiliza **MongoDB** como banco de dados NoSQL.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **MongoDB**
- **Docker**
- **Docker Compose**
- **JaCoCo**
- **JUnit**
- **Mockito**
- **BDD**

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

## Rodando os Testes
Para rodar os testes unitários e verificar a cobertura de código, utilize o seguinte comando Maven:
mvn clean test

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

## Documentação da API
A documentação da API está disponível através do Swagger UI. Após iniciar a aplicação, acesse:
http://localhost:8080/swagger-ui/index.html

## Estrutura do Projeto

- src/main/java/br/com/wishlist/adapters/in/controller: Contém os controladores REST.
- src/main/java/br/com/wishlist/adapters/out/repository: Contém os repositórios que interagem com o MongoDB.
- src/main/java/br/com/wishlist/application/core/domain: Contém as entidades de domínio.
- src/main/java/br/com/wishlist/application/core/usecase: Contém a lógica de negócio (Use Cases).
- src/main/java/br/com/wishlist/exception: Contém as classes de tratamento de exceções.

## Configurações Adicionais

- SPRING_DATA_MONGODB_URI: URI de conexão com o MongoDB. No Docker Compose, já está configurada para mongodb://mongo:27017/wishlist.