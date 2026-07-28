# Diagrama Entidade-Relacionamento (DER) - Equipamentos Raros

Este documento compila as informações sobre o Diagrama Entidade-Relacionamento (DER) para o sistema de controle de inventário "Equipamentos Raros", incluindo o mapeamento inicial de entidades e uma explicação sobre o padrão Peter Chen com um exemplo textual.

## 1. Mapeamento Inicial de Entidades

Com base na descrição do projeto "Equipamentos Raros - Controle de inventário da Raro", as principais entidades que podem ser mapeadas para um sistema de controle de inventário são:

**Entidades Principais:**

*   **Usuário:** Representa os usuários do sistema que terão acesso à plataforma.
    *   Atributos: `email`, `senha`, `status_confirmacao_email`.
*   **Colaborador:** Representa os funcionários da Raro que recebem e devolvem equipamentos.
    *   Atributos: `nome`, `dados_pessoais`, `endereco` (com integração ViaCEP), `data_inicio_contrato`, `data_rescisao_contrato`, `especificacoes_equipamentos_necessarios`.
*   **Equipamento:** Representa os itens físicos que fazem parte do inventário.
    *   Atributos: `especificacoes`, `numero_serie`, `marca`, `modelo`, `data_aquisicao`, `tempo_uso`, `tipo_equipamento`.
*   **Equipamento_Colaborador:** Representa a relação entre um equipamento e um colaborador, registrando quando um equipamento foi entregue e devolvido.
    *   Atributos: `id_equipamento`, `id_colaborador`, `data_entrega`, `data_devolucao`.
*   **Parâmetros do Sistema:** Entidade para armazenar configurações e parâmetros que norteiam as decisões de compra e alertas.
    *   Atributos: `tempo_medio_reposicao`, `tempo_medio_consumo_estoque`, `tempo_medio_envio`, `taxa_media_equipamentos_defeituosos`, `estoque_minimo_seguranca` (por tipo de equipamento).
*   **Alerta:** Representa os alertas disparados pelo sistema.
    *   Atributos: `id_alerta`, `tipo_alerta`, `data_hora_geracao`, `status`, `descricao`.

**Entidades Acessórias/Relacionamentos:**

*   **Tipo_Equipamento:** Uma entidade para categorizar os equipamentos (ex: Notebook, Monitor, Celular, Mouse, Teclado). Isso pode ser um atributo na entidade `Equipamento` ou uma entidade separada com um relacionamento.
*   **Endereço:** Embora o `Colaborador` tenha um endereço, a integração com o ViaCEP sugere que os dados de endereço podem ser armazenados de forma estruturada, talvez como um objeto embutido ou uma entidade separada relacionada ao `Colaborador`.

**Relacionamentos:**

*   Um `Colaborador` deve possuir um `Endereço`.
*   Um  `Endereço` deve ter um `Colaborador`. 
*   Um `Colaborador` deve ter vários `Equipamentos`.
*   Um `Equipamento` pode estar envolvido com um `Colaborador`.
*   Um `Tipo Equipamento` categoriza um `Equipamento`.
*   Um `Equipamento` é categorizado por um `Tipo Equipamento`.
*   Um `Tipo Equipamento` pode estar armazenado em um `Estoque`. 
*   Um `Estoque` pode armazenar vários `Tipos Equipamentos`.
*   Um `Parâmetro do Sistema` é administrado pelo `Estoque`.
*   Um `Parâmetro do Sistema` pode influencia a geração de `Alertas`.
*   Um `Alertas` é baseado em `Parâmetros do Sistema`.


#### Entidades:

---

**1. Entidade: USUÁRIO**

*   **Atributos:**
    *   `id_usuario` (Chave Primária)
    *   `email` 
    *   `senha`
    *   `status_de_confirmação`
    *   `token_de_confirmação`
    *   `token_recuperação`
    *   `token_recuperação_validade`

---

**2. Entidade: COLABORADOR**

*   **Atributos:**
    *   `id_colaborador` (Chave Primária)
    *   `nome`
    *   `email`
    *   `cpf`
    *   `cep`
    *   `data_nascimento`
    *   `data_inicio_contrato`
    *   `data_rescisao_contrato`
    *   `especificacoes_equipamentos_necessarios`

---

**3. Entidade: EQUIPAMENTO**

*   **Atributos:**
    *   `id_equipamento` (Chave Primária)
    *   `especificacoes`
    *   `numero_serie`
    *   `marca`
    *   `modelo`
    *   `data_aquisicao`
    *   `tempo_uso`
    *   `disponibilidade`
    *   `tipo_equipamento`
    *   `tipo_equipamento_id_fk` (Chave Estrangeira de `TIPO_EQUIPAMENTO`)
---

**4. Entidade: TIPO_EQUIPAMENTO**

*   **Atributos:**
    *   `id_tipo_equipamento` (Chave Primária)
    *   `nome_tipo`
    *   `estoque_id_fk` (Chave Estrangeira de `ESTOQUE`)

---

**5. Entidade: EQUIPAMENTO_TO_COLABORADOR**

*   **Atributos:**
    *   `colaborador_id_fk` (Chave Estrangeira de `COLABORADOR`)
    *   `equipamento_id_fk` (Chave Estrangeira de `EQUIPAMENTO`)
    *   `data_entrega`
    *   `data_devolucao`
        (As chaves estrangeiras `colaborador_id_fk` e `equipamento_id_fk` compõe um chave primária composta de `EQUIPAMENTO_TO_COLABORADOR`)
        
---

**6. Entidade: PARÂMETRO_SISTEMA**

*   **Atributos:**
    *   `id_parametro_sistema` (Chave Primária)
    *   `tempo_medio_reposicao`
    *   `tempo_medio_consumo_estoque`
    *   `tempo_medio_envio`
    *   `taxa_media_equipamentos_defeituosos`
    *   `estoque_minimo_seguranca`
    *   `estoque_id_fk` (Chave Estrangeira de `ESTOQUE`)

---

**7. Entidade: ALERTA**

*   **Atributos:**
    *   `id_alerta` (Chave Primária)
    *   `tipo_alerta` (ex: \'Estoque Baixo\', \'Equipamento Defeituoso\', \'Devolução Atrasada\')
    *   `data_hora_geracao`
    *   `status` (ex: \'Ativo\', \'Resolvido\', \'Ignorado\')
    *   `descricao`
    *   `parametro_sistema_id_fk` (Chave Estrangeira de `PARÂMETRO_SISTEMA`)

---
**8. Entidade: ESTOQUE**

*   **Atributos:**
    *   `id_estoque` (Chave Primária)
    *   `quantidade_disponivel` 
    *   `quantidade_em_uso`
    *   `quantidade_defeituosa`
  

---
**9. Entidade: ENDEREÇO**

*   **Atributos:**
    *   `colaborador_id_fk` (Chave Estrangeira de `COLABORADOR`)
    *   `cep`
    *   `logradouro`
    *   `complemento`
    *   `unidade`
    *   `bairro`
    *   `localidade`
    *   `uf`
    *   `estado`
    *   `regiao`
    *   `ibge`
    *   `gia`
    *   `ddd`
    *   `siafi`
    *   `numero`
    (A chave estrangeira `colaborador_id_fk` também será Chave Primária de `ENDEREÇO`)
---



#### Relacionamentos (com Cardinalidades no padrão Peter Chen - (min, max)):

1. **COLABORADOR -- (possui) -- EQUIPAMENTO**
    *   **Descrição:** Um `COLABORADOR` *possui* um ou muitos `EQUIPAMENTO`. Cada `EQUIPAMENTO` pode ser *possuido* por somente um `COLABORADOR`.
    *   **Notação Peter Chen:**
        *   `COLABORADOR` -- (0,1) -- `POSSUI` -- (1,N) -- `EQUIPAMENTO`

2. **COLABORADOR -- (possuir) -- ENDEREÇO**
    *   **Descrição:** Um `COLABORADOR` deve *possuir* somente um `ENDEREÇO`. Cada `ENDEREÇO` deve ser *possuido* por somente um `COLABORADOR`.
    *   **Notação Peter Chen:**
        *   `COLABORADOR` -- (1,1) -- `POSSUI` -- (1,1) -- `ENDEREÇO`

3. **TIPO_EQUIPAMENTO -- (categorizar) -- EQUIPAMENTO**
    *   **Descrição:** Um `TIPO_EQUIPAMENTO` deve *categorizar* um ou mais `EQUIPAMENTO`. Cada `EQUIPAMENTO` é *categorizado* por somente um `TIPO_EQUIPAMENTO`.
    *   **Notação Peter Chen:**
        *   `TIPO_EQUIPAMENTO` -- (1,1) -- `CATEGORIZA` -- (1,N) -- `EQUIPAMENTO`

4. **ESTOQUE -- (armazena) -- TIPO_EQUIPAMENTO**
        *   **Descrição:** Um `ESTOQUE` pode *armazenar* a um ou mais `TIPO_EQUIPAMENTO` (por tipo, localização, ou status).Um `TIPO_EQUIPAMENTO` pode ser *armazenado* à somente um `ESTOQUE`.
    *   **Notação Peter Chen:**
        *   `ESTOQUE` (0,1) — `ARMAZENA` — (0,N) `TIPO_EQUIPAMENTO`

5. **PARÂMETROS_SISTEMA — (administra) — ESTOQUE**
    *   **Descrição:** Um `PARÂMETRO_SISTEMA` deve *administrar* somente um `ESTOQUE`. Cada `ESTOQUE` é *administrado* por somente um `PARÂMETRO_SISTEMA`.
    *   **Notação Peter Chen:**
        *   `PARÂMETROS_SISTEMA` (1,1) — `define_niveis` — (1,1) `ESTOQUE`

6. **PARÂMETRO_SISTEMA -- (influencia) -- ALERTA**
    *   **Descrição:** Um `PARÂMETRO_SISTEMA` deve *influenciar* um ou vários `ALERTAS`. Cada `ALERTA` deve ser *influenciado* por somente um `PARÂMETRO_SISTEMA`.
    *   **Notação Peter Chen:**
        *   `PARÂMETRO_SISTEMA` -- (1,1) -- `INFLUENCIA` -- (1,N) -- `ALERTA`
---
