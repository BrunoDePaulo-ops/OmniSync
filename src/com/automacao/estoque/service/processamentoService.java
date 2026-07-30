package com.automacao.estoque.service;

import com.automacao.estoque.dao.produtoDAO;
import com.automacao.estoque.model.Produto;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class processamentoService {

    private excelService excelService;
    private produtoDAO produtoDAO;
    private PDFService pdfService;

    public processamentoService() throws SQLException {
        this.excelService = new excelService();
        this.produtoDAO = new produtoDAO();
        this.pdfService = new PDFService(); 
        
    }

    /**
     * Processa o arquivo Excel e salva os dados no banco
     * @param caminhoArquivo Caminho do arquivo Excel
     * @return Quantidade de produtos processados
     */
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

            String nomePDF = "relatorio_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            String caminho = "reports/" + nomePDF;

            pdfService.gerarRelatorio(produtos, caminho);

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
}
