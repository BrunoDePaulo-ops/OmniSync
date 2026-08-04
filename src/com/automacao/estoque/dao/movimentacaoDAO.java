package com.automacao.estoque.dao;

import com.automacao.estoque.model.Movimentacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class movimentacaoDAO {
    
    // Método criarTabelas privado. Só quem pode usar é um objeto do tipo da classe movimentacaoDAO.
    private void criarTabelas() throws SQLException{
        
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
        try(Connection conexao = conexaoDAO.abrirConexao(); Statement stmt = conexao.createStatement()){
            stmt.execute(sqlMovimentacoes);
            System.out.println("Tabela movimentações criada com sucesso.");
        }
    }
    
    public movimentacaoDAO(){
        try{
            criarTabelas();
        }catch(SQLException e){
            System.err.println("Erro crítico: Não foi possível criar a tabela de movimentações.");
            e.printStackTrace();
        }
    }

    public void salvarMovimentacao(Movimentacao movimentacao) throws SQLException{
        String sqlSalvar = "INSERT INTO movimentacoes (produto_id, tipo, quantidade, quantidade_anterior, quantidade_nova, origem) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conexao = conexaoDAO.abrirConexao(); PreparedStatement pstmt = conexao.prepareStatement(sqlSalvar, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setLong(1, movimentacao.getProdutoId());
            pstmt.setString(2, movimentacao.getTipo());
            pstmt.setInt(3, movimentacao.getQuantidade());
            pstmt.setInt(4, movimentacao.getQuantidadeAnterior());
            pstmt.setInt(5, movimentacao.getQuantidadeNova());
            pstmt.setString(6, movimentacao.getOrigem());
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()){
                if (rs.next()){
                    movimentacao.setId(rs.getLong(1));
                }
            }
        }
    }

    private Long buscarID(String codigo) throws SQLException{
        Long produtoId = -1L;

        String sqlBuscar = "SELECT id FROM produtos WHERE LOWER(TRIM(codigo)) = LOWER(TRIM(?))";

        try(Connection conexao = conexaoDAO.abrirConexao(); PreparedStatement pstmt = conexao.prepareStatement(sqlBuscar)){
            pstmt.setString(1, codigo);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    produtoId = rs.getLong("id");
                    return produtoId;
                }
            }

        }
        return produtoId;
    }

    public List<Movimentacao> listarMovimentacoesPorProduto(String codigo) throws SQLException{
        List<Movimentacao> movimentacoes = new ArrayList<>();

        Long produtoId= buscarID(codigo);

        if (produtoId == -1L){
            System.out.println("Produto não encontrado no Banco de Dados.");
            return movimentacoes;
        }else{
            String sqlBuscar = "SELECT id, produto_id, tipo, quantidade, quantidade_anterior, quantidade_nova, origem FROM movimentacoes WHERE produto_id = ?";

            try(Connection conexao = conexaoDAO.abrirConexao(); PreparedStatement pstmt = conexao.prepareStatement(sqlBuscar)){
                pstmt.setLong(1, produtoId);

                try(ResultSet rs = pstmt.executeQuery()){
                    while(rs.next()){
                        Movimentacao mov = new Movimentacao( 
                            rs.getLong("produto_id"),
                            rs.getString("tipo"),
                            rs.getInt("quantidade"),
                            rs.getInt("quantidade_anterior"),
                            rs.getInt("quantidade_nova"),
                            rs.getString("origem")
                            
                        );
                        movimentacoes.add(mov);
                    }
                    return movimentacoes;
                }
            }
        }
    }

}

