<p align="center">
  <img src="assets/logo.png" width="220">
</p>

<h1 align="center">OmniSync</h1>

<p align="center">
  Intelligent Inventory Automation Platform
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange">
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue">
  <img src="https://img.shields.io/badge/Apache%20POI-Excel-success">
  <img src="https://img.shields.io/badge/OpenPDF-PDF-red">
</p>

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

--- 
## 🔧 Configuração do VS Code

### 1. Caso use o VS Code, copie o arquivo de exemplo:

```bash
cp .vscode/settings.json.example .vscode/settings.json
```
### 2. Recarregue o VSCode: 

- **Pressione**: CRTL + Shift + P
- **Digite**: Developer: Reload Window
- **Pressione**: Enter


⚠️ **Extensões recomendadas:** Instale as seguintes extensões para uma melhor experiência.

```bash
- Extension Pack For Java (Microsoft)
- Language Suport For Java (Red Hat)
```
### 3. Estrutura do .vscode:

```
.vscode/
├── settings.json.example   ← Exemplo de configuração (versionado)
└── settings.json           ← Configuração local (NÃO versionado)
```

⚠️ **Importante:** O arquivo settings.json não é versionado no Git. Cada dev deve criar o seu a partir do exemplo que foi fornecido.

### 4. Conteúdo do arquivo de exemplo:

```
{
    "java.project.sourcePaths": [
        "src"
    ],
    "java.project.referencedLibraries": [
        "lib/*.jar"
    ]
}
```
- "src" → O VS Code vai procurar os arquivos .java na pasta src/
- "lib/*.jar" → O VS Code vai adicionar todos os JARs da pasta lib/ ao classpath automaticamente

___

# 🛠️ Tecnologias Utilizadas

Abaixo estão as principais tecnologias, frameworks e bibliotecas utilizadas no projeto:

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| **☕ Java** | 17+ | Linguagem de programação principal |
| **🐘 PostgreSQL** | 15+ | Banco de dados relacional |
| **📊 Apache POI** | 4.1.2 | Leitura e escrita de arquivos Excel (.xlsx) |
| **📄 OpenPDF** | 1.3.40 | Geração de relatórios em PDF |
| **🐙 Git** | - | Controle de versão |
| **📦 GitHub** | - | Hospedagem do repositório |
| **🖥️ VS Code** | - | Ambiente de desenvolvimento (opcional) |

---
## 📊 Fluxo do Sistema

![Fluxo do OmniSync](assets/fluxo-omnisync.png)

*Diagrama do fluxo principal do sistema OmniSync.*

---
### Como testar

- Compilar o projeto
```bash
javac -d bin -cp "lib/*" src/com/automacao/estoque/**/*.java
```
- Executar o sistema
```bash
java -cp "lib/*:bin" com.automacao.estoque.tests.Main
```
- Após isso, você verá:

```bash
========== OMNISYNC - Sistema de Gerenciamento de Estoques ==========
========== Menu ==========
Digite um número para uma ação...

1. Ler dados direto da planilha.
2. Ler dados direto do banco.
3. Sincronizar planilha, movimentações e banco.
4. Verificar se existem e quais são os produtos com estoque baixo. (Sincronize o sistema antes)
5. Emitir relatório em PDF (Sincronize o sistema antes).
6. Verificar logs gravados no sistema.
7. Verificar as movimentações de um produto específico.
0. Sair.
```




