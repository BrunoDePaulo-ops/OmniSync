package com.automacao.estoque.dao;

import com.automacao.estoque.model.Movimentacao;
import com.automacao.estoque.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class produtoDAO {
    private movimentacaoDAO movimentacaoDAO;

    public produtoDAO(){
        this.movimentacaoDAO = new movimentacaoDAO();
        
    }

    //Recebe a referência para uma lista de objetos do tipo Produto. 
    public void salvarEmLote(List<Produto> produtos) throws SQLException{
        String sqlCheck = "SELECT id, quantidade_estoque FROM produtos WHERE codigo = ?";
        String sqlInsert = "INSERT INTO produtos (codigo, nome, descricao, categoria, preco, quantidade_estoque, estoque_minimo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE produtos SET nome=?, descricao=?, categoria=?, preco=?, quantidade_estoque=?, estoque_minimo=?, ultima_atualizacao=CURRENT_TIMESTAMP WHERE codigo=?";

        // Essa forma evita o Memory-Leak pois o try fecha os pstmt automaticamente.
        try(Connection conexao = conexaoDAO.abrirConexao();PreparedStatement pstmt1 = conexao.prepareStatement(sqlCheck); PreparedStatement pstmt2 = conexao.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS); PreparedStatement pstmt3 = conexao.prepareStatement(sqlUpdate)){
            
            int totalInseridos = 0;
            int totalAtualizados = 0;
            List<Movimentacao> listaMov = new ArrayList<>();

            // Para cada objeto p do tipo Produto dentro da referência recebida produtos
            for(Produto p: produtos){
                System.out.println("🔍 Código: " + p.getCodigo() + " (Tipo: " + p.getCodigo().getClass().getName() + ")");

                //Verifica se o código do produto já existe no banco de dados. 
                pstmt1.setString(1, p.getCodigo());
                try(ResultSet rs = pstmt1.executeQuery()){ // O executeQuery aqui é usado porque eu quero que o banco me retorne o produto se ele já estiver armazenado.
                   
                    // Se a lista retornar resultados, ele já existe. Agora é criar um objeto movimentações para ele no banco.
                    if (rs.next()){
                        Long idProd = rs.getLong("id");// Pega o ID do objeto no banco
                        int quantidade_antiga = rs.getInt("quantidade_estoque");// Pega a quantidade dele em estoque no banco. 
                        int quantidade_nova = p.getQuantidadeEstoque();// Pega a quantidade dele no objeto/planilha (que será a quantidade nova).
                        
                        String tipo = null;

                        if(quantidade_antiga !=  quantidade_nova){
                            if (quantidade_antiga < quantidade_nova){
                                tipo = "Entrada";
                            }else if(quantidade_antiga > quantidade_nova){
                                tipo = "Saída";
                            }
                        }else{
                            tipo = "Ajuste";
                            System.out.println("Quantidade inalterada.");
                        }

                        int diferenca = Math.abs(quantidade_nova - quantidade_antiga);

                        // Cria o objeto movimentação ANTES de atualizar. O objeto tem que refletir as mudanças. Se ele fosse criado depois da atualização ele nunca captaria as mudanças do produto.
                        Movimentacao mov = new Movimentacao(
                            idProd,
                            tipo,
                            diferenca,
                            quantidade_antiga,
                            quantidade_nova,
                            "Excel"
                        );

                        listaMov.add(mov);
                        System.out.println("   ✅ Movimentação criada: " + tipo + " de " + diferenca);
                        
                        pstmt3.setString(1, p.getNome());
                        pstmt3.setString(2, p.getDescricao());
                        pstmt3.setString(3, p.getcategoria());
                        pstmt3.setBigDecimal(4, p.getPreco());
                        pstmt3.setInt(5, p.getQuantidadeEstoque());
                        pstmt3.setInt(6, p.getEstoqueMinimo());
                        pstmt3.setString(7, p.getCodigo());
                        pstmt3.executeUpdate(); // Eu uso o executeUpdate pois quero apenas modificar dados no banco. Se fosse para retornar dados, usaria  executeQuery.

                        totalAtualizados++;
                        
                    }else{
                        pstmt2.setString(1, p.getCodigo());
                        pstmt2.setString(2, p.getNome());
                        pstmt2.setString(3, p.getDescricao());
                        pstmt2.setString(4, p.getcategoria());
                        pstmt2.setBigDecimal(5, p.getPreco());
                        pstmt2.setInt(6, p.getQuantidadeEstoque());
                        pstmt2.setInt(7, p.getEstoqueMinimo());
                        pstmt2.executeUpdate();

                        totalInseridos++;

                        // O banco vai gerar um id para o produto cadastrado. Eu tenho que pegar esse ID gerado pelo banco e setar no objeto Java para ficar sincronizado.
                        try(ResultSet idGerado = pstmt2.getGeneratedKeys()){// Pegue a chave gerada pelo banco e armazene em idGerado.
                            if(idGerado.next()){

                                // Pegue o id gerado, converta no tipo Long e armazene em id.
                                Long id = idGerado.getLong(1);
                                p.setId(id);// Use o método setId do objeto produto para setar o id vindo do banco.

                                Movimentacao mov = new Movimentacao(
                                    p.getId(),
                                    "Entrada",
                                    p.getQuantidadeEstoque(),
                                    0,
                                    p.getQuantidadeEstoque(),
                                    "Excel"

                                );
                                listaMov.add(mov);
                                System.out.println("   ✅ Produto novo: ENTRADA de " + p.getQuantidadeEstoque());

                            }
                        }

                            

                            
                    }

                        
                }
                    
                
            }
            System.out.println("📊 BD: " + totalInseridos + " inseridos, " + totalAtualizados + " atualizados.");
            // ===== SALVA AS MOVIMENTAÇÕES (DEPOIS DE TUDO) =====
            
            for (Movimentacao mov : listaMov) {
                movimentacaoDAO.salvarMovimentacao(mov);
            }
            
            
        }
        

    }

    public List<Produto> listarProdutos() throws SQLException{
        List<Produto> produtos = new ArrayList<>();

        String sqlListar = "SELECT * FROM produtos ORDER BY id";
        
        
        try(Connection conexao = conexaoDAO.abrirConexao(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sqlListar)){

            while(rs.next()){
                Produto p = new Produto();
                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNome(rs.getString("nome"));
                p.setDescricao(rs.getString("descricao"));
                p.setCategoria(rs.getString("categoria"));
                p.setPreco(rs.getBigDecimal("preco"));
                p.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                p.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produtos.add(p);
            }
        
            return produtos;    
        }

    }
}
