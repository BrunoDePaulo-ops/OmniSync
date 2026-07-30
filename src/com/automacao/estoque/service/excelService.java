package com.automacao.estoque.service;
import com.automacao.estoque.model.Produto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class excelService {
    public List<Produto> lerProduto(String caminho) throws IOException{
        List<Produto> produtos = new ArrayList<>();

        try(FileInputStream fis = new FileInputStream(caminho); Workbook workbook = new XSSFWorkbook(fis)){
            Sheet planilha = workbook.getSheetAt(0);
            System.out.println("📊 Lendo planilha: " + planilha.getPhysicalNumberOfRows() + " linhas");

            for (int i = 1; i <= planilha.getLastRowNum(); i++){
                Row linha = planilha.getRow(i);
                if(linha == null){
                    continue;
                }

                try{
                    Produto p = new Produto();
                    p.setCodigo(getStringValue(linha.getCell(0)));
                    p.setNome(getStringValue(linha.getCell(1)));
                    p.setDescricao(getStringValue(linha.getCell(2)));
                    p.setCategoria(getStringValue(linha.getCell(3)));
                    p.setPreco(getBigDecimalValue(linha.getCell(4)));
                    p.setQuantidadeEstoque(getIntValue(linha.getCell(5)));
                    p.setEstoqueMinimo(getIntValue(linha.getCell(6)));

                    produtos.add(p);
                    System.out.println("  ✅ Lido: " + p.getCodigo() + " - " + p.getNome());

                }catch(Exception e){
                    System.err.println("  ⚠️ Erro na linha " + i + ": " + e.getMessage());
                }
            }
        }
        System.out.println("📦 Total de produtos lidos: " + produtos.size());
        return produtos;

    }
    private String getStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return "";
    }

    private BigDecimal getBigDecimalValue(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        if (cell.getCellType() == CellType.NUMERIC) return BigDecimal.valueOf(cell.getNumericCellValue());
        if (cell.getCellType() == CellType.STRING) {
            try {
                return new BigDecimal(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    private Integer getIntValue(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) return (int) cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
}
