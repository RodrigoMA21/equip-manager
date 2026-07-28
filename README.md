# EquipManager

Sistema completo de gestão de equipamentos com API REST (Java/Spring Boot) e frontend web (React/TypeScript).

## 🚀 Tecnologias

### Backend
- Java 21 + Spring Boot 3.5
- Spring Security + JWT
- PostgreSQL + Flyway
- Hibernate (JPA) + Lombok
- Swagger/OpenAPI
- Feign (ViaCEP)
- Mailtrap (email sandbox)

### Frontend
- React 19 + TypeScript
- Vite 8
- Tailwind CSS v4
- React Router v7
- Axios + TanStack React Query
- React Hook Form + Zod

## 📌 Funcionalidades

- Autenticação JWT com refresh token e confirmação de email
- CRUD completo de Colaboradores (com integração ViaCEP)
- CRUD completo de Equipamentos (com controle de estoque automático)
- Tipos de Equipamento
- Empréstimos com previsão de entrega por região e registro de devolução
- Parâmetros do sistema e relatório de previsão de falta
- Alertas automáticos de estoque baixo
- Dashboard com indicadores

## 🧪 Como Rodar

### Backend

1. **Pré-requisitos:** Java 21, PostgreSQL, Maven
2. **Banco de dados:**
   ```
   createdb InventarioRaro
   ```
3. Configure `src/main/resources/application.properties` com suas credenciais do PostgreSQL
4. Execute:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Swagger em: http://localhost:8080/swagger-ui/index.html

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Acesse em: http://localhost:5173

> O Vite está configurado com proxy para redirecionar chamadas `/api` para `localhost:8080`.

## 📂 Estrutura

```
equip-manager/
├── src/                    # Backend (Spring Boot)
├── frontend/               # Frontend (React + Vite)
│   └── src/
│       ├── components/     # Layout, ProtectedRoute
│       ├── contexts/       # AuthContext
│       ├── pages/          # Login, Dashboard, CRUDs
│       ├── services/       # Axios + API services
│       └── types/          # TypeScript interfaces
├── pom.xml
└── README.md
```

## 📜 Licença

Distribuído sob licença MIT. Veja [LICENSE](./LICENSE) para mais informações.

## 👨‍💻 Desenvolvedores

JRLL TECH – Grupo de Desenvolvimento

- João Paulo Fernandes Conceição
- Rodrigo Mayer Alves
- Luis Felipe Assunção Pereira
- Lara Freire de Oliveira
