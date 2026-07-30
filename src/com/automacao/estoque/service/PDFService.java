package com.automacao.estoque.service;

import com.automacao.estoque.model.Produto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.Color;

public class PDFService {
        public void gerarRelatorio(List<Produto> produtos, String caminhoArquivo) throws Exception {
        // 1. Cria o documento
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
        document.open();

        // 2. Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph("📊 RELATÓRIO DE ESTOQUE", titleFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // 3. Data e hora
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Paragraph data = new Paragraph("Gerado em: " + dataHora);
        data.setAlignment(Element.ALIGN_CENTER);
        document.add(data);
        document.add(new Paragraph(" ")); // Espaço

        // 4. Estatísticas
        long totalProdutos = produtos.size();
        long produtosComEstoqueBaixo = produtos.stream()
                .filter(Produto::isEstoqueBaixo)
                .count();
        long produtosComEstoqueOk = totalProdutos - produtosComEstoqueBaixo;

        document.add(new Paragraph("📦 Total de produtos: " + totalProdutos));
        document.add(new Paragraph("✅ Produtos com estoque OK: " + produtosComEstoqueOk));
        document.add(new Paragraph("⚠️ Produtos com estoque baixo: " + produtosComEstoqueBaixo));
        document.add(new Paragraph(" ")); // Espaço

        // 5. Tabela de produtos
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Cabeçalho
        adicionarCabecalho(table, "Código");
        adicionarCabecalho(table, "Nome");
        adicionarCabecalho(table, "Categoria");
        adicionarCabecalho(table, "Preço");
        adicionarCabecalho(table, "Quantidade");
        adicionarCabecalho(table, "Status");


        // Corpo da tabela
        for (Produto p : produtos) {
            table.addCell(p.getCodigo());
            table.addCell(p.getNome());
            table.addCell(p.getcategoria() != null ? p.getcategoria() : "");
            table.addCell("R$ " + p.getPreco());
            table.addCell(String.valueOf(p.getQuantidadeEstoque()));

            // Status com cor
            PdfPCell statusCell;
            if (p.isEstoqueBaixo()) {
                statusCell = new PdfPCell(new Phrase("⚠️ BAIXO"));
                statusCell.setBackgroundColor(Color.RED);
                statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
                statusCell = new PdfPCell(new Phrase("✅ OK"));
                statusCell.setBackgroundColor(Color.GREEN);
                statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
            table.addCell(statusCell);
        }

        document.add(table);

        // 6. Alerta de estoque baixo (se houver)
        if (produtosComEstoqueBaixo > 0) {

            Font fonteVermelha = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.RED);
            document.add(new Paragraph(" ")); // Espaço
            Paragraph alerta = new Paragraph("⚠️ PRODUTOS COM ESTOQUE BAIXO:", fonteVermelha);
            alerta.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            document.add(alerta);

            for (Produto p : produtos) {
                if (p.isEstoqueBaixo()) {
                    document.add(new Paragraph("   • " + p.getNome() +
                            " | Estoque: " + p.getQuantidadeEstoque() +
                            " | Mínimo: " + p.getEstoqueMinimo()));
                }
            }
        }
        document.close();

        System.out.println("📄 Relatório PDF gerado com sucesso: " + caminhoArquivo);


    }
    private void adicionarCabecalho(PdfPTable table, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
