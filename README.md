# 📦 OmniSync

**OmniSync** é um softare de automação voltado para o gerenciamento de estoques. Ele lê planilhas Excel, sincroniza, atualiza e armazena em um banco de dados PostgreSQL os dados vindos do software da Microsoft, gera relatórios PDF e alerta sobre produtos com estoque baixo.

---

## 🚀 **Funcionalidades**

- 📊 **Leitura de Excel** - Lê planilhas `.xlsx` usando Apache POI
- 💾 **CRUD no PostgreSQL** - Persiste dados usando JDBC
- 📄 **Relatórios PDF** - Gera relatórios com OpenPDF
- ⚠️ **Alertas de estoque** - Detecta produtos com estoque baixo automaticamente
- 🧪 **Testes manuais** - Testes para validar movimentações

---

## 📋 **Pré-requisitos**

Antes de executar o projeto, você precisa ter instalado:

### 1. Java 17+
```bash
# Verifique a versão do Java
java -version

```
### 2. Biblioteca Apache POI

Baixe os seguintes JAR's e coloque na pasta lib: 
```bash
# Ler e editar dados em planilhas do Excel
poi-4.1.2.jar
poi-ooxml-4.1.2.jar
xmlbeans-3.1.0.jar
ooxml-schemas-1.4.jar
commons-collections4-4.4.jar
commons-compress-1.19.jar
```

### 3. Biblioteca OpenPDF 1.3.40

Baixe a biblioteca e coloque na pasta lib:
```bash
openpdf-1.3.40.jar
```

### 4. Driver do JDBC PostgreSQL

Baixe o driver do banco de dados e o adicione na pasta lib:
```bash
postgresql-42.7.13.jar
```

## 📁 **Estrutura do Projeto**

Abaixo está a organização das pastas e principais arquivos do sistema:

```
auto_stock_manager/
│
├── .vscode/                         # Configurações do VS Code (não versionado)
│   └── settings.json.example        # Exemplo de configuração para o VS Code
│
├── src/                             # Código fonte do projeto
│   └── com/automacao/estoque/
│       ├── dao/                     # Acesso a dados (CRUD)
│       │   ├── ConexaoBD.java
│       │   ├── ProdutoDAO.java
│       │   └── MovimentacaoDAO.java
│       │
│       ├── model/                   # Entidades do sistema
│       │   ├── Produto.java
│       │   └── Movimentacao.java
│       │
│       ├── service/                 # Regras de negócio
│       │   ├── ExcelService.java
│       │   ├── ProcessamentoService.java
│       │   └── PDFService.java
│       │
│       ├── util/                    # Utilitários gerais
│       │
│       ├── tests/                   # Testes manuais
│       │   ├── Main.java
│       │   ├── Main2.java
│       │   └── TesteMovimentacoes.java
│       │
│       └── scheduler/               # (Reservado para agendamento futuro)
│
├── lib/                             # Dependências (JARs) - não versionado
│   └── *.jar
│
├── uploads/                         # Planilhas de entrada
│   └── produtos.xlsx
│
├── reports/                         # PDFs gerados pelo sistema
│   └── relatorio_*.pdf
│
├── logs/                            # Logs do sistema
│   └── *.log
│
├── bin/                             # Arquivos compilados (.class) - ignorado
│
├── .gitignore
├── README.md
└── LICENSE
```

## 🔧 Configuração do VS Code

Caso use o VS Code, configure o projeto com:

```bash
cp .vscode/settings.json.example .vscode/settings.json