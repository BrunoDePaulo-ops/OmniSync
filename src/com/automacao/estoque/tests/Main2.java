package com.automacao.estoque.tests;

import com.automacao.estoque.dao.produtoDAO;
import com.automacao.estoque.model.Produto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        System.out.println("🧪 MODO DE TESTE - CRUD DE PRODUTOS\n");
        
        try {
            // 1. Criar uma lista de produtos de exemplo
            List<Produto> produtos = criarProdutosExemplo();
            
            // 2. Salvar no banco
            System.out.println("📝 SALVANDO PRODUTOS NO BANCO...");
            produtoDAO dao = new produtoDAO();
            dao.salvarEmLote(produtos);
            
            // 3. Listar os produtos do banco
            System.out.println("\n📋 LISTANDO PRODUTOS DO BANCO...");
            List<Produto> produtosDoBanco = dao.listarProdutos();
            
            // 4. Exibir os produtos
            System.out.println("\n✅ PRODUTOS CADASTRADOS:");
            System.out.println("=" .repeat(60));
            for (Produto p : produtosDoBanco) {
                System.out.printf("ID: %d | Código: %s | Nome: %s | Quantidade: %d | Preço: R$ %.2f\n",
                    p.getId(),
                    p.getCodigo(),
                    p.getNome(),
                    p.getQuantidadeEstoque(),
                    p.getPreco()
                );
            }
            System.out.println("=" .repeat(60));
            System.out.println("📊 Total de produtos: " + produtosDoBanco.size());
            
        } catch (SQLException e) {
            System.err.println("❌ Erro de banco de dados: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Cria uma lista de produtos de exemplo para teste
     */
    private static List<Produto> criarProdutosExemplo() {
        List<Produto> produtos = new ArrayList<>();
        
        // Produto 1
        Produto p1 = new Produto();
        p1.setCodigo("PROD001");
        p1.setNome("Notebook Dell");
        p1.setDescricao("i7 16GB 512SSD");
        p1.setCategoria("Eletrônicos");
        p1.setPreco(new BigDecimal("4500.00"));
        p1.setQuantidadeEstoque(10);
        p1.setEstoqueMinimo(3);
        produtos.add(p1);
        
        // Produto 2
        Produto p2 = new Produto();
        p2.setCodigo("PROD002");
        p2.setNome("Mouse Logitech");
        p2.setDescricao("Sem fio, ergonômico");
        p2.setCategoria("Periféricos");
        p2.setPreco(new BigDecimal("150.00"));
        p2.setQuantidadeEstoque(25);
        p2.setEstoqueMinimo(5);
        produtos.add(p2);
        
        // Produto 3
        Produto p3 = new Produto();
        p3.setCodigo("PROD003");
        p3.setNome("Teclado Mecânico");
        p3.setDescricao("RGB, switch blue");
        p3.setCategoria("Periféricos");
        p3.setPreco(new BigDecimal("350.00"));
        p3.setQuantidadeEstoque(2);
        p3.setEstoqueMinimo(5);
        produtos.add(p3);
        
        // Produto 4
        Produto p4 = new Produto();
        p4.setCodigo("PROD004");
        p4.setNome("Monitor LG");
        p4.setDescricao("24 polegadas, Full HD");
        p4.setCategoria("Eletrônicos");
        p4.setPreco(new BigDecimal("1200.00"));
        p4.setQuantidadeEstoque(8);
        p4.setEstoqueMinimo(3);
        produtos.add(p4);
        
        return produtos;
    }
}