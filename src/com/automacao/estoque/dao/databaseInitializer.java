package com.automacao.estoque.dao;

import java.sql.*;

public class databaseInitializer {
    public static void inicializarBanco()throws SQLException{
        try (Connection conexao = conexaoDAO.abrirConexao(); Statement stmt = conexao.createStatement()){
            criarTabelaProdutos(stmt);
            criarTabelaMovimentacoes(stmt);
            criarTabelaLogs(stmt);
        }
    }

    private databaseInitializer(){}
    
    private static void criarTabelaProdutos(Statement stmt) throws SQLException{
        String sqlProdutos = """
            CREATE TABLE IF NOT EXISTS produtos(
            id SERIAL PRIMARY KEY,
            codigo VARCHAR(50) UNIQUE NOT NULL,
            nome VARCHAR(200) NOT NULL,
            descricao TEXT,
            categoria VARCHAR(100),
            preco DECIMAL(10,2),
            quantidade_estoque INTEGER DEFAULT 0,
            estoque_minimo INTEGER DEFAULT 10,
            data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            ativo BOOLEAN DEFAULT TRUE
            )
        """;

        stmt.execute(sqlProdutos);
        System.out.println("Tabela de produtos criada com sucesso.");
    }
    

    private static void criarTabelaMovimentacoes(Statement stmt) throws SQLException{
        String sqlMovimentacoes = """
            CREATE TABLE IF NOT EXISTS movimentacoes(
            id SERIAL PRIMARY KEY,
            produto_id INTEGER REFERENCES produtos(id),
            tipo VARCHAR(20) NOT NULL,
            quantidade INTEGER NOT NULL,
            quantidade_anterior INTEGER,
            quantidade_nova INTEGER,
            origem VARCHAR(50), -- EXCEL, MANUAL, SISTEMA
            data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
                    
        """;
        stmt.execute(sqlMovimentacoes);
        System.out.println("Tabela movimentações criada com sucesso.");
    }
    

    private static void criarTabelaLogs(Statement stmt) throws SQLException{
        String sqlLogs = """
            CREATE TABLE IF NOT EXISTS registros(
            id BIGSERIAL PRIMARY KEY,
            tipo VARCHAR(50) NOT NULL,
            mensagem TEXT,
            detalhes JSONB,
            data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            sucesso BOOLEAN DEFAULT TRUE
            )
        """;

        
        stmt.execute(sqlLogs);
        System.out.println("Tabela de logs criada com sucesso.");
    }
    
    
}
