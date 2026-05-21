# Lista de Compras API

API REST para gerenciamento de listas de compras, mercados e itens, desenvolvida com Spring Boot.

---

## Objetivo

Fornecer uma API REST completa para criar e gerenciar mercados, associar listas de compras a esses mercados e adicionar itens a cada lista. O projeto aplica conceitos avançados de Spring Boot como HATEOAS, Cache, Actuator, Swagger/OpenAPI, paginação, ordenação e projections.

---

## Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.0 |
| Spring Data JPA | - |
| Spring HATEOAS | - |
| Spring Cache | - |
| Spring Actuator | - |
| SpringDoc OpenAPI (Swagger) | 2.5.0 |
| H2 Database | - |
| Lombok | - |
| Maven | - |

---

## Funcionalidades

- CRUD completo de **Mercados**, **Listas de Compras** e **Itens de Compra**
- Paginação e ordenação em todos os endpoints de listagem
- Busca por nome, mercado, faixa de preço e quantidade mínima
- Projection (resumo de listas com apenas id e nome)
- Links HATEOAS nas respostas (self, coleção)
- Cache em operações de leitura com eviction automático em mutações
- Documentação automática via Swagger UI
- Monitoramento via Spring Actuator
- Tratamento global de erros com `@RestControllerAdvice`
- Validações com Bean Validation e validação customizada (`@NotFutureDate`)

---

## Estrutura das Entidades

```
Mercado (1) ──────── (*) ListaCompra (1) ──────── (*) ItemCompra
  - id                     - id                         - id
  - nome                   - nome                       - nome
  - endereco               - dataCriacao                - quantidade
                           - mercado (FK)               - preco
                           - itens                      - listaCompra (FK)
```

### Regras de Negócio

- A `dataCriacao` não pode ser uma data futura
- O preço de um item não pode ultrapassar **R$ 10.000,00**
- A quantidade de um item não pode ultrapassar **1.000 unidades**

---

## Como Rodar o Projeto

### Pré-requisitos

- Java 17+
- Maven 3.6+

### Executar

```bash
# Clone o repositório
git clone <url-do-repositorio>

# Acesse a pasta do projeto
cd lista-compras-api

# Execute com Maven
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## Endpoints Principais

### Mercados — `/mercados`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/mercados` | Listar todos (paginado) |
| GET | `/mercados/{id}` | Buscar por ID |
| POST | `/mercados` | Criar novo |
| PUT | `/mercados/{id}` | Atualizar |
| DELETE | `/mercados/{id}` | Deletar |

### Listas de Compras — `/listas`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/listas` | Listar todas (paginado) |
| GET | `/listas/{id}` | Buscar por ID |
| POST | `/listas` | Criar nova |
| PUT | `/listas/{id}` | Atualizar |
| DELETE | `/listas/{id}` | Deletar |
| GET | `/listas/resumo` | Listar resumo (Projection) |
| GET | `/listas/busca/nome?nome=X` | Buscar por nome |
| GET | `/listas/busca/mercado/{id}` | Buscar por mercado |

### Itens de Compra — `/itens`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/itens` | Listar todos (paginado) |
| GET | `/itens/{id}` | Buscar por ID |
| POST | `/itens` | Criar novo |
| PUT | `/itens/{id}` | Atualizar |
| DELETE | `/itens/{id}` | Deletar |
| GET | `/itens/busca/nome?nome=X` | Buscar por nome |
| GET | `/itens/busca/preco?precoMin=X&precoMax=Y` | Buscar por faixa de preço |
| GET | `/itens/busca/quantidade-minima?quantidade=X` | Buscar por quantidade mínima |

### Paginação e Ordenação

Todos os endpoints de listagem aceitam os parâmetros:

```
?page=0&size=10&sort=nome,asc
?page=1&size=5&sort=id,desc
```

---

## Exemplos de Requisições

### Criar Mercado

```http
POST /mercados
Content-Type: application/json

{
  "nome": "Mercado Central",
  "endereco": "Rua das Compras, 100"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "Mercado Central",
  "endereco": "Rua das Compras, 100",
  "_links": {
    "self": { "href": "http://localhost:8080/mercados/1" },
    "mercados": { "href": "http://localhost:8080/mercados" }
  }
}
```

### Criar Lista de Compras

```http
POST /listas
Content-Type: application/json

{
  "nome": "Lista Semanal",
  "dataCriacao": "2026-05-20",
  "mercadoId": 1
}
```

### Criar Item de Compra

```http
POST /itens
Content-Type: application/json

{
  "nome": "Arroz",
  "quantidade": 2,
  "preco": 25.90,
  "listaCompraId": 1
}
```

### Listar com Paginação e Ordenação

```http
GET /mercados?page=0&size=5&sort=nome,asc
GET /listas?page=0&size=10&sort=dataCriacao,desc
GET /itens?page=0&size=20&sort=preco,asc
```

---

## Swagger / OpenAPI

Após iniciar a aplicação, acesse a documentação interativa:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

---

## Spring Actuator

Endpoints de monitoramento disponíveis:

| Endpoint | Descrição |
|----------|-----------|
| `/actuator` | Lista todos os endpoints disponíveis |
| `/actuator/health` | Status de saúde da aplicação |
| `/actuator/info` | Informações da aplicação |
| `/actuator/metrics` | Métricas da aplicação |
| `/actuator/env` | Variáveis de ambiente |
| `/actuator/beans` | Beans do contexto Spring |

---

## Banco H2

Console web do banco de dados em memória:

- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:listacomprasdb`
- **Usuário:** `sa`
- **Senha:** *(deixar em branco)*

---

## Spring Cache

O projeto utiliza cache em memória (`ConcurrentHashMap`) para otimizar operações de leitura:

- `@Cacheable` aplicado nos métodos `listar` e `buscarPorId` de todos os services
- `@CacheEvict` aplicado nas operações de criação, atualização e exclusão
- Caches utilizados: `mercados`, `mercado`, `listas`, `lista`, `itens`, `item`, `listas-resumo`, `listas-por-nome`, `listas-por-mercado`, `itens-por-nome`

---

## Autores

- **Pedro Vaz** — RM 566551
- **João Victor Luiz Oliveira Resende** — RM 565139

---

## Estrutura de Pacotes

```
com.listacompras.api/
├── config/          SwaggerConfig, CorsConfig
├── controller/      MercadoController, ListaCompraController, ItemCompraController
├── dto/             DTOs de Request e Response
├── entity/          Entidades JPA
├── exception/       GlobalExceptionHandler
├── service/         MercadoService, ListaCompraService, ItemCompraService
├── repository/      Repositórios JPA e Projections
└── validation/      NotFutureDate, NotFutureDateValidator
```
