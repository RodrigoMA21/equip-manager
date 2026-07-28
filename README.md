# 📦 Projeto Final - API de Gestão de Equipamentos Raros

Esta API REST foi desenvolvida como parte do projeto final da disciplina, com o objetivo de gerenciar equipamentos, seus estoques e o empréstimo desses itens para colaboradores.

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5
- Spring Security + JWT
- PostgreSQL
- Hibernate (JPA)
- Lombok
- Swagger/OpenAPI
- Jakarta Validation
- Flyway (controle de versões do banco de dados)

---

## 📌 Funcionalidades da API

### 🔐 Autenticação e Autorizações

- Login com JWT e geração de token
- Confirmação de e-mail
- Recuperação e redefinição de senha
- Refresh de token
(OBS: A api atua com dois tipos de token, o token do email para confirmação da conta e recuperação de senha e o Acess Token para acessar os endpoints no Swagger.)

#### Endpoints

| Método | Rota                      | Descrição                                   |
|--------|---------------------------|---------------------------------------------|
| POST   | /auth/login               | Login e geração de token                    |
| POST   | /auth/refresh-token       | Renovar access token com refresh token      |
| POST   | /auth/registrar           | Registro de novo usuário                    |
| POST   | /auth/confirmar-email     | Confirmação de e-mail                       |
| POST   | /auth/esqueci-senha       | Solicita redefinição de senha               |
| POST   | /auth/resetar-senha       | Redefine senha com token enviado por e-mail |
| POST   | /auth/alterar-senha       | Cadastra nova senha                         |

---

### 🛠️ Equipamentos

CRUD completo com controle de disponibilidade.

| Método | Rota                 | Descrição                   |
|--------|----------------------|-----------------------------|
| GET    | /equipamentos        | Listar todos os equipamentos|
| GET    | /equipamentos/{id}   | Buscar por ID               |
| POST   | /equipamentos        | Criar novo equipamento      |
| PUT    | /equipamentos/{id}   | Atualizar equipamento       |
| DELETE | /equipamentos/{id}   | Deletar equipamento         |

---

### 👤 Equipamento-Colaborador (Empréstimos)

| Método | Rota                                 | Descrição                           |
|--------|--------------------------------------|-------------------------------------|
| GET    | /emprestimos                     | Listar todos os envios              |
| GET    | /emprestimos/{id}                | Buscar envio por ID                 |
| POST   | /emprestimos                     | Cadastrar novo envio                |
| PUT    | /emprestimos/{id}                | Atualizar envio existente           |
| DELETE | /emprestimos/{id}                | Deletar envio por ID                |
| PUT    | /emprestimos/{id}/devolver       | Registrar devolução do equipamento  |

---

### 📦 Parâmetros do Sistema

Define as regras de estoque mínimo, reposição, consumo, etc.

| Método | Rota                                             | Descrição                                 |
|--------|--------------------------------------------------|-------------------------------------------|
| GET    | /parametros-sistema                              | Lista todos os parâmetros                 |
| GET    | /parametros-sistema/{id}                         | Buscar por ID                             |
| POST   | /parametros-sistema                              | Criar novo parâmetro                      |
| PUT    | /parametros-sistema/{id}                         | Atualizar parâmetro                       |
| DELETE | /parametros-sistema/{id}                         | Deletar parâmetro                         |
| GET    | /parametros-sistema/relatorio-previsao-falta     | Gera relatório de previsão de falta       |

---

### 🧾 Tipos de Equipamento

Categoria de equipamentos (ex: notebook, celular).

| Método | Rota                         | Descrição                             |
|--------|------------------------------|---------------------------------------|
| GET    | /tipos-equipamento           | Listar todos os tipos                 |
| GET    | /tipos-equipamento/{id}      | Buscar por ID                         |
| POST   | /tipos-equipamento           | Criar novo tipo                       |
| PUT    | /tipos-equipamento/{id}      | Atualizar tipo                        |
| DELETE | /tipos-equipamento/{id}      | Deletar tipo                          |

---

### 👥 Usuário

| Método | Rota            | Descrição                  |
|--------|-----------------|----------------------------|
| GET    | /usuarios       | Listar todos os usuários   |
| GET    | /usuarios/{id}  | Buscar usuário por ID      |
| POST   | /usuarios       | Criar novo usuário         |
| PUT    | /usuarios/{id}  | Atualizar usuário          |
| DELETE | /usuarios/{id}  | Deletar usuário            |

---


---

### 🧑‍🔧 Colaborador

| Método | Rota                                 | Descrição                     |
|--------|--------------------------------------|-------------------------------|
| POST   | /colaborador/                        | Cadastrar novo colaborador    |
| PATCH  | /colaborador/                        | Editar colaborador existente  |
| GET    | /colaborador/                        | Imprimir lista de colaboradores |
| DELETE | /colaborador/deleta-colaborador/{id} | Deletar colaborador           |

---

### 🚨 Alertas de Estoque

| Método | Rota                          | Descrição                        |
|--------|-------------------------------|----------------------------------|
| POST   | /estoque-alerta/verificar | Verifica alerta de estoque baixo|

---

🧪 Como Rodar o Projeto

1. Pré-requisitos

Java 21

PostgreSQL

Maven

2. Configuração do Banco

spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update

2.1. Migrações com Flyway

O projeto utiliza o Flyway para controle de versão do banco de dados. As migrações SQL ficam localizadas em:

src/main/resources/db/migration

Ao iniciar a aplicação, o Flyway executa automaticamente os scripts que ainda não foram aplicados.

💡 Você pode utilizar os scripts do Flyway para popular tabelas com dados de teste durante o desenvolvimento.

📊 Importante: Se estiver utilizando o Flyway, configure o application.properties da seguinte forma:

spring.jpa.hibernate.ddl-auto=none

3. Rodar a aplicação

./mvnw spring-boot:run

Acesse em:

http://localhost:8080

---

## 📂 Diagramas do Banco de Dados

- [MER - Modelo Entidade-Relacionamento](./diagramas/mer.md)
- [DER - Diagrama Entidade-Relacionamento](./diagramas/der.png)

---

📘 Documentação Swagger

http://localhost:8080/swagger-ui/index.html

🔐 Autenticação JWT

Login via POST /auth/login

Copie o token JWT e clique em Authorize no Swagger

Formato:

Bearer seu_token_jwt_aqui

👨‍💻 Desenvolvedores

JRLL TECH – Grupo de Desenvolvimento

João Paulo Fernandes Conceição

Rodrigo Mayer Alves

Luis Felipe Assunção Pereira

Lara Freire de Oliveira

📜 Licença

Este projeto é de uso educacional.

