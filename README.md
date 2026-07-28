# EquipManager

Sistema completo de gestão de equipamentos com API REST e frontend web moderno.

<div align="center">
  
[![Portfolio](https://img.shields.io/badge/Portfolio-rodrigomayer-ff69b4?style=for-the-badge&logo=vercel&logoColor=white)](https://rodrigomayer.vercel.app/)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Rodrigo_Mayer_Alves-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rodrigo-mayer-alves-a9255675)

</div>

## 🚀 Tecnologias

### Backend
![Java 21](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot 3.5](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-FF2D20?style=for-the-badge&logo=lombok&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Feign](https://img.shields.io/badge/Feign-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Mailtrap](https://img.shields.io/badge/Mailtrap-22D172?style=for-the-badge&logo=mailtrap&logoColor=white)

### Frontend
![React 19](https://img.shields.io/badge/React_19-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Vite 8](https://img.shields.io/badge/Vite_8-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![Tailwind CSS v4](https://img.shields.io/badge/Tailwind_CSS_v4-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)
![React Router v7](https://img.shields.io/badge/React_Router_v7-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white)
![React Query](https://img.shields.io/badge/React_Query-FF4154?style=for-the-badge&logo=reactquery&logoColor=white)
![React Hook Form](https://img.shields.io/badge/React_Hook_Form-EC5990?style=for-the-badge&logo=reacthookform&logoColor=white)
![Zod](https://img.shields.io/badge/Zod-3E67B1?style=for-the-badge&logo=zod&logoColor=white)

## 📌 Funcionalidades

- **Autenticação JWT** com refresh token e confirmação de email
- **Colaboradores** — CRUD completo com integração ViaCEP
- **Equipamentos** — CRUD completo com controle de estoque automático
- **Tipos de Equipamento** — categorização de ativos
- **Empréstimos** — com previsão de entrega por região e registro de devolução
- **Parâmetros do Sistema** — configuração de regras de negócio
- **Relatório de Previsão de Falta** — análise de risco de estoque
- **Alertas Automáticos** — notificação de estoque baixo
- **Dashboard** — indicadores e visão geral

## 🧪 Como Rodar

### Backend

1. **Pré-requisitos:** Java 21, PostgreSQL, Maven
2. Crie o banco de dados:
   ```sql
   CREATE DATABASE "InventarioRaro";
   ```
3. Configure `src/main/resources/application.properties` com suas credenciais
4. Execute:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Swagger: http://localhost:8080/swagger-ui/index.html

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Acesse: http://localhost:5173

> O Vite está configurado com proxy para redirecionar `/api/*` para `localhost:8080`.

## 📂 Estrutura

```
equip-manager/
├── src/                    # Backend (Spring Boot)
│   ├── main/java/.../
│   │   ├── controller/    # REST controllers
│   │   ├── service/       # Lógica de negócio
│   │   ├── model/         # Entidades JPA
│   │   ├── repository/    # Repositórios
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── security/      # JWT + Spring Security
│   │   └── config/        # Configurações
│   └── resources/
│       ├── db/migration/  # Flyway migrations
│       └── application.properties
├── frontend/               # Frontend (React + Vite)
│   └── src/
│       ├── components/     # Layout, ProtectedRoute
│       ├── contexts/       # AuthContext
│       ├── pages/          # Login, Dashboard, CRUDs
│       ├── services/       # Axios + API services
│       └── types/          # TypeScript interfaces
└── README.md
```

## 📜 Licença

Distribuído sob licença MIT. Veja [LICENSE](./LICENSE) para mais informações.

## 👨‍💻 Desenvolvedor

[![Portfolio](https://img.shields.io/badge/Rodrigo_Mayer_Alves-ff69b4?style=for-the-badge&logo=vercel&logoColor=white)](https://rodrigomayer.vercel.app/)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rodrigo-mayer-alves-a9255675)
