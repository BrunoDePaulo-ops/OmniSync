package com.automacao.estoque.dao;

import com.automacao.estoque.model.Movimentacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class movimentacaoDAO {
    // Cria o objeto movimentacaoDAO. Através desse objeto eu terei acesso aos métodos public da classe movimentacaoDAO.
    public movimentacaoDAO(){}

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
                        mov.setId(rs.getLong("id"));
                        movimentacoes.add(mov);
                    }
                    return movimentacoes;
                }
            }
        }
    }

    public List<Movimentacao> listarMovimentacoesPorNomeDoProduto(String nome) throws SQLException{
        List<Movimentacao> movimentacoes = new ArrayList<>();
        Long idBanco = null;
        Long id = null;
        

        String sqlBusca = "SELECT id FROM produtos WHERE LOWER(TRIM(nome))= LOWER(TRIM(?))";
        String sqlMov = "SELECT * FROM movimentacoes WHERE produto_id = ?";
        
        try(Connection conexao = conexaoDAO.abrirConexao(); PreparedStatement pstmt = conexao.prepareStatement(sqlBusca); PreparedStatement pstmt1 = conexao.prepareStatement(sqlMov) ){
            pstmt.setString(1, nome);

            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
                
                idBanco = rs.getLong("id");
                if(!rs.wasNull()){
                    id = idBanco;                    
                }
                System.out.println("O id encontrado foi: " + id);
                
                pstmt1.setLong(1, id);
                
                ResultSet rs2 = pstmt1.executeQuery();
                System.out.println("Busca executada para o id: " + id);

                while(rs2.next()){
                    
                    Movimentacao movimentacao = new Movimentacao(
                        rs2.getLong("produto_id"),
                        rs2.getString("tipo"),
                        rs2.getInt("quantidade"),
                        rs2.getInt("quantidade_anterior"),
                        rs2.getInt("quantidade_nova"),
                        rs2.getString("origem")
                        
                    );

                    movimentacao.setId(rs2.getLong("id"));
                    movimentacoes.add(movimentacao);
                    

                }
            }else{
                System.out.println("O produto não está no banco de dados!");
                return movimentacoes;
            }
        }
        return movimentacoes;
    }

}

