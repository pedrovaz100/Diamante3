# 🛒 Lista de Compras API

API REST desenvolvida com Spring Boot para gerenciamento de listas de compras, mercados e itens.

---

## 🎯 Objetivo do Projeto

O objetivo desta API é permitir o controle simples e eficiente de listas de compras, possibilitando:

- Cadastro de mercados
- Criação de listas de compras
- Adição de itens em listas
- Consulta, atualização e remoção de dados

---

## ⚙️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database (em memória)
- Maven
- Lombok

---

## 🧱 Entidades

### 🏪 Mercado

Representa o local onde as compras serão realizadas.

- `id`
- `nome`
- `endereco`

---

### 📋 ListaCompra

Representa uma lista de compras vinculada a um mercado.

- `id`
- `nome`
- `dataCriacao`
- `mercado`

---

### 🛍️ ItemCompra

Representa um item dentro de uma lista de compras.

- `id`
- `nome`
- `quantidade`
- `preco`
- `listaCompra`

---

## 🔄 Funcionamento

1. O usuário cadastra um mercado  
2. Cria uma lista vinculada a esse mercado  
3. Adiciona itens à lista  

---

## 🔌 Endpoints

### 🔍 Health Check

```http
GET /health
```

---

## 🏪 Mercados

```http
GET /mercados
GET /mercados/{id}
POST /mercados
PUT /mercados/{id}
DELETE /mercados/{id}
```

### Exemplo POST:

```json
{
  "nome": "Mercado Central",
  "endereco": "Rua A, 123"
}
```

---

## 📋 Listas de Compras

```http
GET /listas
GET /listas/{id}
POST /listas
PUT /listas/{id}
DELETE /listas/{id}
```

### Exemplo POST:

```json
{
  "nome": "Compras do mês",
  "dataCriacao": "2026-04-28",
  "mercadoId": 1
}
```

---

## 🛍️ Itens de Compra

```http
GET /itens
GET /itens/{id}
POST /itens
PUT /itens/{id}
DELETE /itens/{id}
```

### Exemplo POST:

```json
{
  "nome": "Arroz",
  "quantidade": 2,
  "preco": 25.90,
  "listaCompraId": 1
}
```
---
## 🌐 Frontend para API

Acesse a aplicação:

Acesso direto: https://gerenciamento-listas-compras-front.vercel.app



Repositorio do front: https://github.com/pedrovaz100/Gerenciamento-listas-compras-front.git

---

## 👨‍💻 Autor

- Pedro Vaz - RM 566551
- João Victor luiz oliveira resende - RM 565139
