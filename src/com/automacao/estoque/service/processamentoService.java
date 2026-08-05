package com.automacao.estoque.service;

import com.automacao.estoque.dao.produtoDAO;
import com.automacao.estoque.dao.movimentacaoDAO;
import com.automacao.estoque.dao.databaseInitializer;
import com.automacao.estoque.model.Produto;
import com.automacao.estoque.model.Movimentacao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class processamentoService {
    // Aqui, estabeleci atributos para a classe de processamento. Ela é a responsável por orquestrar (chamar) as demais classes do serviço.
    private excelService excelService;
    private produtoDAO produtoDAO;
    private PDFService pdfService;
    private movimentacaoDAO movimentacaoDAO;
    

    public processamentoService() throws SQLException {
        // Objetos para poder usar os métodos das classes. 
        this.excelService = new excelService();
        this.produtoDAO = new produtoDAO();
        this.pdfService = new PDFService(); 
        this.movimentacaoDAO = new movimentacaoDAO();
        
        
    }

    /**
     * Processa o arquivo Excel e salva os dados no banco
     * @param caminhoArquivo Caminho do arquivo Excel
     * @return Quantidade de produtos processados
     */
    
    public static void inicializarSistema() throws SQLException{
        databaseInitializer.inicializarBanco();
    }

    public int processarEstoque(String caminhoArquivo) {
        System.out.println("\n🔄 INICIANDO PROCESSAMENTO...");
        System.out.println("📁 Arquivo: " + caminhoArquivo);

        try {
            // 1. Ler o Excel
            List<Produto> produtos = excelService.lerProduto(caminhoArquivo);

            if (produtos.isEmpty()) {
                System.out.println("⚠️ Nenhum produto encontrado na planilha!");
                return 0;
            }

            System.out.println("📦 " + produtos.size() + " produtos lidos do Excel.");

            // 2. Salvar no banco (usando seu CRUD)
            produtoDAO.salvarEmLote(produtos);

            // 3. Verificar estoque baixo (regra de negócio)
            verificarEstoqueBaixo(produtos);

            listarMovimentacoes(produtos);

            /* 
            String nomePDF = "relatorio_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            String caminho = "reports/" + nomePDF;

            
            pdfService.gerarRelatorio(produtos, caminho);
            */
            System.out.println("✅ PROCESSAMENTO CONCLUÍDO!");
            return produtos.size();

        } catch (Exception e) {
            System.err.println("❌ ERRO no processamento: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Verifica produtos com estoque abaixo do mínimo
     */
    private void verificarEstoqueBaixo(List<Produto> produtos) {
        System.out.println("\n🔍 VERIFICANDO ESTOQUE...");
        boolean temAlerta = false;

        for (Produto p : produtos) {
            // Verifica se o estoque mínimo foi definido
            if (p.isEstoqueBaixo()) {
                System.out.println("  ⚠️ ALERTA: " + p.getNome() +
                        " | Estoque: " + p.getQuantidadeEstoque() +
                        " | Mínimo: " + p.getEstoqueMinimo());
                temAlerta = true;
            }
        }

        if (!temAlerta) {
            System.out.println("  ✅ Todos os produtos com estoque OK.");
        }
    }

    private void listarMovimentacoes(List<Produto> produtos) throws SQLException{
        List<Movimentacao> movimentacoes = new ArrayList<>();
        String codigo = null;

        for (Produto p : produtos){
            codigo = p.getCodigo();
        
            movimentacoes = movimentacaoDAO.listarMovimentacoesPorProduto(codigo);

            for (Movimentacao m : movimentacoes){
                System.out.println("Listando movimentações do produto: " + m.getProdutoId() + 
                    " | ID: " + m.getId() + 
                    " | Tipo: " + m.getTipo() + 
                    " | Qtd: " + m.getQuantidade() + 
                    " | Antes: " + m.getQuantidadeAnterior() + 
                    " | Depois: " + m.getQuantidadeNova() + 
                    " | Origem: " + m.getOrigem());
                }
            }
        }

    }

