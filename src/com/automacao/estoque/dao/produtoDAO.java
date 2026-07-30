package com.automacao.estoque.dao;
import com.automacao.estoque.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class produtoDAO {
    private Connection conexao;

    public produtoDAO() throws SQLException{

    // Tenta a conexão com o banco.
        conexao = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/autostock",
        "postgres",
        "brunox123"
        );
        criarTabelas();
        System.out.println("Conexão bem sucedida.");
        
    }
    
    private void criarTabelas(){
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

        String sqlMovimentacoes = """
            CREATE TABLE IF NOT EXISTS movimentacoes(
            id SERIAL PRIMARY KEY,
            produto_id INTEGER REFERENCES produtos(id),
            tipo VARCHAR(20) NOT NULL, -- ENTRADA, SAIDA, AJUSTE
            quantidade INTEGER NOT NULL,
            quantidade_anterior INTEGER,
            quantidade_nova INTEGER,
            origem VARCHAR(50), -- EXCEL, MANUAL, SISTEMA
            data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
                    
        """;

        String sqlLogs = """
            CREATE TABLE IF NOT EXISTS registros(
            id SERIAL PRIMARY KEY,
            tipo VARCHAR(50) NOT NULL,
            mensagem TEXT,
            detalhes JSONB,
            data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            sucesso BOOLEAN DEFAULT TRUE
            )
        """;

        try(Statement stmt = conexao.createStatement()){
            stmt.execute(sqlProdutos);
            stmt.execute(sqlMovimentacoes);
            stmt.execute(sqlLogs);

            System.out.println("✅ Tabelas criadas/verificads com sucesso!");
        }catch(SQLException e){
            System.out.println("❌ Erro ao criar tabelas: " + e.getMessage());
        }
        
        
    }
    //Recebe a referência para uma lista de objetos do tipo Produto.
    public void salvarEmLote(List<Produto> produtos) throws SQLException{
        String sqlCheck = "SELECT id FROM produtos WHERE codigo = ?";
        String sqlInsert = "INSERT INTO produtos (codigo, nome, descricao, categoria, preco, quantidade_estoque, estoque_minimo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE produtos SET nome=?, descricao=?, categoria=?, preco=?, quantidade_estoque=?, estoque_minimo=?, ultima_atualizacao=CURRENT_TIMESTAMP WHERE codigo=?";

        
        PreparedStatement pstmt1 = conexao.prepareStatement(sqlCheck);
        PreparedStatement pstmt2 = conexao.prepareStatement(sqlInsert);
        PreparedStatement pstmt3 = conexao.prepareStatement(sqlUpdate);
            
        int totalInseridos = 0;
        int totalAtualizados = 0;

        // Para cada objeto p do tipo Produto dentro da referência recebida produtos
        for(Produto p: produtos){
            System.out.println("🔍 Código: " + p.getCodigo() + " (Tipo: " + p.getCodigo().getClass().getName() + ")");

            //Verifica se o código do produto já existe nessa lista. 
            pstmt1.setString(1, p.getCodigo());
            ResultSet rs = pstmt1.executeQuery(); // O executeQuery aqui é usado porque eu quero que o banco me retorne o produto se ele já estiver armazenado.

                
                
            //Se a lista retornar resultados, ele já existe. Só atualiza.
            if (rs.next()){
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
                ResultSet idGerado = pstmt2.getGeneratedKeys();// Pegue a chave gerada pelo banco e armazene em idGerado.
                if(idGerado.next()){

                    // Pegue o id gerado, converta no tipo Long e armazene em id.
                    Long id = idGerado.getLong(1);
                    p.setId(id);// Use o método setId do objeto produto para setar o id vindo do banco.
                }
                
            }
            rs.close();

        }
        System.out.println("📊 BD: " + totalInseridos + " inseridos, " + totalAtualizados + " atualizados.");
        

    }

    public List<Produto> listarProdutos() throws SQLException{
        List<Produto> produtos = new ArrayList<>();

        String sqlListar = "SELECT * FROM produtos ORDER BY id";
        
        Statement pstmt = conexao.createStatement();
        ResultSet rs = pstmt.executeQuery(sqlListar);

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
