✅ 1. Cadastro do usuário
Endpoint:
bash
Copiar
Editar
POST /auth/registrar
Exemplo de JSON:
json
Copiar
Editar
{
  "email": "usuario@teste.com",
  "senha": "123456"
}
Resultado esperado:
Retorno 200 OK (ou 201 se você quiser ajustar).

Um e-mail (via Mailtrap) é enviado com um link como:

bash
Copiar
Editar
http://localhost:8080/usuarios/confirmar?token=abcdefg123
🧪 2. (Opcional) Verificar no banco
Se quiser garantir que o token foi salvo, execute no PostgreSQL:

sql
Copiar
Editar
SELECT email, email_confirmado, token_recuperacao, token_recuperacao_validade
FROM usuario
WHERE email = 'usuario@teste.com';
✅ 3. Confirmar o e-mail
Endpoint:
bash
Copiar
Editar
GET /usuarios/confirmar?token=abcdefg123
Use o token que veio no e-mail (ou do banco).

Resultado esperado:
Mensagem: "E-mail confirmado com sucesso!"

No banco, o campo email_confirmado agora está true.

❌ 4. Tentar login sem confirmar e-mail (teste negativo)
(Esse teste deve ser feito antes do passo 3, se quiser garantir que a proteção funciona.)

Endpoint:
bash
Copiar
Editar
POST /auth/login
json
Copiar
Editar
{
  "email": "usuario@teste.com",
  "senha": "123456"
}
Resultado esperado:
Erro (provavelmente 403 ou 401).

Porque o isEnabled() está retornando false.

✅ 5. Tentar login após confirmação (teste positivo)
Depois de confirmar, repita o login:

json
Copiar
Editar
{
  "email": "usuario@teste.com",
  "senha": "123456"
}
Resultado esperado:
Retorno 200 OK

Um token JWT no corpo da resposta, como:

json
Copiar
Editar
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
🧼 6. (Opcional) Reset de senha
Você já tem:

/auth/solicitar-recuperacao

/auth/resetar-senha?token=...

Esse fluxo vai continuar funcionando normalmente, pois usa o mesmo campo de token (tokenRecuperacao), que agora serve tanto para reset quanto para confirmação (até ser apagado após uso).