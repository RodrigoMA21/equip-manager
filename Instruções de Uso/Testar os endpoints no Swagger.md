1. Testar os endpoints no Swagger
Acesse:

bash
Copiar
Editar
http://localhost:8080/swagger-ui.html
E siga esta ordem para testar:

🔹 POST /auth/register
Cadastre um novo usuário:

json
Copiar
Editar
{
  "email": "usuario@teste.com",
  "senha": "123456"
}
Se já estiver cadastrado, ele dará erro (como esperado).

🔹 POST /auth/login
Use o mesmo e-mail/senha e envie:

json
Copiar
Editar
{
  "email": "usuario@teste.com",
  "senha": "123456"
}
📥 A resposta será algo como:

json
Copiar
Editar
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}
Copie esse token — você usará para acessar endpoints protegidos.

✅ 2. Ativar autenticação no Swagger (botão Authorize)
No topo da Swagger UI, clique no botão Authorize (cadeado)

Cole seu token assim:

nginx
Copiar
Editar
Bearer SEU_TOKEN_JWT
Inclua a palavra Bearer com espaço
3. Clique em Authorize

Agora todos os endpoints protegidos poderão ser acessados ✅

✅ 3. Vai precisar de token/senha depois para acessar o projeto?
🔒 Sim, para acessar a API (endpoints protegidos)
Depois que o projeto estiver com o filtro JWT ativado, qualquer rota fora de /auth/** e do Swagger só pode ser acessada com token válido.

Exemplo:

http
Copiar
Editar
GET /api/equipamentos
Authorization: Bearer eyJhbGciOi...
❌ Não precisa de senha/token para:
Swagger (/swagger-ui.html)

Login e cadastro (/auth/**)

Recuperação de senha

✅ Conclusão
Ação	Requer token?
/auth/register	❌ Não
/auth/login	❌ Não
/auth/recuperar-senha	❌ Não
/api/... (protegido)	✅ Sim
Swagger UI	❌ Não
Swagger + endpoints protegidos	✅ Sim (se clicar em Authorize)

