package com.automacao.estoque.tests;

import com.automacao.estoque.service.processamentoService;


public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 AutoStock Manager iniciado!");
        System.out.println("📌 Processando planilha: uploads/produtos.xlsx\n");
        
        try {
            
            // 1. Criar o serviço de processamento
            processamentoService service = new processamentoService();
            
            // 2. Processar o arquivo Excel (isso vai ler, salvar e verificar estoque)
            String caminhoExcel = "uploads/produtos.xlsx";
            int total = service.processarEstoque(caminhoExcel);
            
            // 3. Exibir resultado
            System.out.println("\n" + "=".repeat(50));
            if (total > 0) {
                System.out.println("✅ Automação concluída com sucesso!");
                System.out.println("📊 Total de produtos processados: " + total);
            } else {
                System.out.println("⚠️ Nenhum produto foi processado.");
                System.out.println("💡 Verifique se a planilha existe em: " + caminhoExcel);
            }
            System.out.println("=".repeat(50));
            

            
        } catch (Exception e) {
            System.err.println("\n❌ ERRO FATAL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}