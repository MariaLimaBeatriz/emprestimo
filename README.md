# Sistema de Empréstimo de Equipamentos - API REST

API REST desenvolvida em **Java com Spring Boot** para gerenciamento e controle de empréstimos de equipamentos  (notebooks, monitores, headsets) para colaboradores de uma empresa.

---

## -- Link da API em Produção (Render)
> **URL da API no Render:** [https://sistema-emprestimo-api.onrender.com](https://sistema-emprestimo-api.onrender.com)  
*(Exemplo de teste: `https://sistema-emprestimo-api.onrender.com/equipamentos`)*

---

## -- Tecnologias Utilizadas
* **Java 17**
* **Spring Boot**
* **H2 Database** (Banco de dados relacional em arquivo/memória)
* **JUnit 5** (Testes unitários das entidades de domínio)
* **Docker** ( deploy no Render)

---

## -- Mapa do Projeto

```text
emprestimo/
├── pom.xml
├── Dockerfile
├── README.md
└── src/
    ├── main/
    │   ├── java/com/example/emprestimo/
    │   │   ├── EmprestimoApplication.java
    │   │   ├── model/
    │   │   │   ├── Equipamento.java
    │   │   │   ├── TipoEquipamento.java
    │   │   │   ├── Colaborador.java
    │   │   │   └── Emprestimo.java
    │   │   ├── repository/
    │   │   │   ├── EquipamentoRepository.java
    │   │   │   ├── ColaboradorRepository.java
    │   │   │   └── EmprestimoRepository.java
    │   │   ├── service/
    │   │   │   ├── EquipamentoService.java
    │   │   │   ├── ColaboradorService.java
    │   │   │   └── EmprestimoService.java
    │   │   └── controller/
    │   │       ├── EquipamentoController.java
    │   │       ├── ColaboradorController.java
    │   │       └── EmprestimoController.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/example/emprestimo/
            ├── EmprestimoApplicationTests.java
            └── model/
                ├── EquipamentoTest.java
                └── EmprestimoTest.java
```

---

## -- Como Rodar o Projeto Localmente

### Pré-requisitos
* Java 17 (JDK) instalado
* Git instalado

### Passo a passo
1. **Clone o repositório:**
   ```bash
   git clone <LINK_DO_SEU_REPOSITORIO_GITHUB>
   cd emprestimo
   ```

2. **Execute a aplicação:**
   * Pelo terminal (Linux/Mac):
     ```bash
     ./mvnw spring-boot:run
     ```
   * Pelo terminal (Windows PowerShell):
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```
   * Ou simplesmente abra o projeto no **IntelliJ IDEA** e execute a classe `EmprestimoApplication.java`.

3. A aplicação estará disponível em: `http://localhost:8080`
4. Console do banco H2: `http://localhost:8080/h2-console`
   * **JDBC URL:** `jdbc:h2:file:./dados/emprestimos`

---

## -- Como Rodar os Testes Unitários

Os testes das regras de domínio das entidades (`EquipamentoTest` e `EmprestimoTest`) usam **JUnit 5 puro** e executam em milissegundos sem depender de banco de dados ou do contexto Spring:

```bash
./mvnw test
```
Ou no IntelliJ: clique com o botão direito na pasta `src/test/java` e selecione **Run 'All Tests'**.

---

## -- Lista de Rotas da API

###  Equipamentos (`/equipamentos`)

| Método | Rota | Descrição | Status Sucesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/equipamentos` | Cadastra um novo equipamento | `201 Created` |
| `GET` | `/equipamentos` | Lista todos os equipamentos (filtros opcionais: `?tipo=NOTEBOOK&disponivel=true`) | `200 OK` |
| `GET` | `/equipamentos/{id}` | Busca um equipamento por ID | `200 OK` |
| `PUT` | `/equipamentos/{id}` | Atualiza nome e tipo do equipamento | `200 OK` |
| `DELETE` | `/equipamentos/{id}` | Remove um equipamento (se não tiver histórico) | `204 No Content` |

**Exemplo de corpo no cadastro (POST):**
```json
{
  "patrimonio": "NB-001",
  "nome": "Notebook Dell",
  "tipo": "NOTEBOOK"
}
```

---

### -- Colaboradores (`/colaboradores`)

| Método | Rota | Descrição | Status Sucesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/colaboradores` | Cadastra um colaborador | `201 Created` |
| `GET` | `/colaboradores` | Lista todos os colaboradores | `200 OK` |
| `GET` | `/colaboradores/{id}` | Busca colaborador por ID | `200 OK` |
| `GET` | `/colaboradores/{id}/emprestimos` | Consulta o histórico de empréstimos do colaborador | `200 OK` |

**Exemplo de corpo no cadastro (POST):**
```json
{
  "nome": "Ana Souza",
  "email": "ana@empresa.com",
  "cargo": "Analista"
}
```

---

### -- Empréstimos (`/emprestimos`)

| Método | Rota | Descrição | Status Sucesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/emprestimos?equipamentoId=1&colaboradorId=1` | Registra novo empréstimo (prevê devolução em 14 dias) | `201 Created` |
| `GET` | `/emprestimos` | Lista empréstimos (filtro opcional: `?atrasados=true`) | `200 OK` |
| `GET` | `/emprestimos/{id}` | Busca um empréstimo por ID | `200 OK` |
| `PUT` | `/emprestimos/{id}/devolucao` | Registra a devolução do equipamento | `200 OK` |

---

## -- Regras de Negócio Implementadas (R1 a R10)

* **R1:** Não podem existir dois equipamentos com o mesmo patrimônio  - (*EquipamentoService*).
* **R2:** Todo equipamento novo nasce com status disponível, ignorando valor falso enviado no JSON (*EquipamentoService*).
* **R3:** Não podem existir dois colaboradores com o mesmo e-mail (*ColaboradorService*).
* **R4:** Só é permitido emprestar equipamento disponível (*Entidade Equipamento*).
* **R5:** Ao emprestar: o equipamento fica indisponível, a data do empréstimo é hoje e o prazo previsto para devolução é de 14 dias (*Entidade Emprestimo* e *EmprestimoService*).
* **R6:** Um empréstimo já devolvido não pode ser devolvido novamente (*Entidade Emprestimo*).
* **R7:** Ao devolver: data de devolução é registrada como hoje e equipamento volta a ficar disponível (*EmprestimoService*).
* **R8:** Empréstimo é considerado atrasado se ainda não foi devolvido e a data prevista já expirou (*Entidade Emprestimo* e *EmprestimoRepository*).
* **R9:** Equipamento com histórico de empréstimo não pode ser excluído (*EquipamentoService*).
* **R10:** Colaborador que possua empréstimo atrasado não pode pegar novos equipamentos (*EmprestimoService*).

---

## -- Bônus Implementados

### 📄 Swagger / OpenAPI (Nível 1)
Documentação interativa gerada automaticamente com tela para testar todas as rotas diretamente pelo navegador sem precisar do Postman:
* **Localmente:** `http://localhost:8080/swagger-ui/index.html`
* **No Render:** `https://sistema-emprestimo-api.onrender.com/swagger-ui/index.html`
