package com.automacao.estoque.tests;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;

public class CriarPlanilha {
    public static void main(String[] args) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Produtos");
        
        // Cabeçalho
        String[] headers = {"Codigo", "Nome", "Descricao", "Categoria", "Preco", "Quantidade", "EstoqueMinimo"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        
        // Dados
        Object[][] dados = {
            {"PROD001", "Notebook Dell", "i7 16GB 512SSD", "Eletrônicos", 4500.00, 10, 3},
            {"PROD002", "Mouse Logitech", "Sem fio, ergonômico", "Periféricos", 150.00, 25, 5},
            {"PROD003", "Teclado Mecânico", "RGB, switch blue", "Periféricos", 350.00, 2, 5},
            {"PROD004", "Monitor LG", "24 polegadas, Full HD", "Eletrônicos", 1200.00, 8, 3}
        };
        
        int rowNum = 1;
        for (Object[] linha : dados) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < linha.length; i++) {
                Object valor = linha[i];
                if (valor instanceof String) {
                    row.createCell(i).setCellValue((String) valor);
                } else if (valor instanceof Double) {
                    row.createCell(i).setCellValue((Double) valor);
                } else if (valor instanceof Integer) {
                    row.createCell(i).setCellValue((Integer) valor);
                }
            }
        }
        
        // Ajusta largura das colunas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Salva
        try (FileOutputStream out = new FileOutputStream("uploads/produtos.xlsx")) {
            workbook.write(out);
        }
        workbook.close();
        
        System.out.println("✅ Planilha criada em: uploads/produtos.xlsx");
    }
}