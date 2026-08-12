package com.automacao.estoque.service;

import com.automacao.estoque.dao.*;
import com.automacao.estoque.model.*;
import com.automacao.estoque.tests.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;




public class ProcessamentoService {
    
    private excelService excelService;
    private PDFService PDFService;
    private produtoDAO produtoDAO;
    private logExecucaoDAO logExecucaoDAO;
    
    public ProcessamentoService(){
        this.excelService = new excelService();
        this.PDFService = new PDFService();
        this.produtoDAO = new produtoDAO();
        this.logExecucaoDAO = new logExecucaoDAO();
        
        
    }
    
    private List<Produto> produtos = new ArrayList<>();
    private logExecucao log;
    private List<logExecucao> listaLogs = new ArrayList<>();
    

    public List<Produto> lerPlanilha(){
        
        try{
            
            produtos = excelService.lerProduto("uploads/produtos.xlsx");
            
            for (Produto p : produtos){
                System.out.println( 
                " | Codigo: " + p.getCodigo() + 
                " | Nome: " + p.getNome() + 
                " | Descrição: " + p.getDescricao() + 
                " | Categoria: " + p.getcategoria() + 
                " | Preço: " + p.getPreco() +
                " | Quantidade em estoque: " + p.getQuantidadeEstoque() +
                " | Estoque mínimo: " + p.getEstoqueMinimo() +
                " | Estoque : " + p.isEstoqueBaixo()
                ); 

            }
            // Criei outro construtor pois alguns atributos da classe virão do banco e do Java.
            log = new logExecucao(
                "Processamento",
                "Leitura da planilha ",
                "Leitura da planilha sem sincronização",
                true

            );

            logExecucaoDAO.gravarLogs(log);
            
        }catch(FileNotFoundException e){
            System.err.println("Erro: Arquivo 'produtos.xlsx' não foi encontrado na pasta 'uploads'.");
            System.err.println("Verifique se o caminho na raiz do projeto está correto.");

        }catch(IOException e){
            System.err.println("Erro de entrada/saída ao ler a planilha: " + e.getMessage());
            e.printStackTrace();
        }catch(SQLException e){
            System.out.println("Erro ao gravar log no disco: " + e.getMessage());
            try{
                log = new logExecucao("Processamento", "Falha no processamento", "Erro ao tentar ler dados do banco", false);
                logExecucaoDAO.gravarLogs(log);
            }catch(SQLException ex){
                 System.err.println("Erro ao salvar log de erro: " + ex.getMessage());
            }
        }
        return produtos;
    }

    public void lerProdutosNoBanco(){

        try{
            produtos = produtoDAO.listarProdutos();
            
            for(Produto p: produtos){
                System.out.println(
                " | ID: " + p.getId() + 
                " | Codigo: " + p.getCodigo() + 
                " | Nome: " + p.getNome() + 
                " | Descrição: " + p.getDescricao() + 
                " | Categoria: " + p.getcategoria() + 
                " | Preço: " + p.getPreco() +
                " | Quantidade em estoque: " + p.getQuantidadeEstoque() +
                " | Estoque mínimo: " + p.getEstoqueMinimo() +
                " | Estoque : " + p.isEstoqueBaixo()
                ); 
            }
        }catch(SQLException e){
            System.out.println("Erro ao buscar produtos no banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sincronizar(){

        produtos = lerPlanilha();

        try{
            produtoDAO.salvarEmLote(produtos);
            System.out.println("Dados sincronizados com sucesso.");
        }catch(SQLException e){
            System.out.println("Erro ao tentar sincronização: " + e.getMessage());
        }



    
    }

    public void verificarEstoqueBaixo(){
        boolean baixo = false;

        try{
            produtos = excelService.lerProduto("uploads/produtos.xlsx");

            for (Produto p : produtos){
                if(p.isEstoqueBaixo()){
                    baixo = true;
                    System.out.println(" ⚠️ ALERTA: " + p.getNome() + 
                        " | Estoque: " + p.getQuantidadeEstoque() +
                        " | Mínimo: " + p.getEstoqueMinimo()
                    );

                }
            }

            if(!baixo){
                System.out.println("  ✅ O estoque está OK, pode dormir tranquilo.");
            }
        }catch(IOException e){
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void emitirRelatorio(){

        try{
            String nomePDF = "relatorio_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            String caminho = "reports/" + nomePDF;
            
            produtos = excelService.lerProduto("uploads/produtos.xlsx");
            PDFService.gerarRelatorio(produtos, caminho);
            System.out.println("  ✅ Relatório gerado com sucesso na pasta reports.");

        }catch(IOException e){
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
            e.printStackTrace();
        }catch(Exception e){
            System.out.println("Erro ao desenvolver relatório: " + e.getMessage());
        }
    }

    public void lerLogs(){
        try{
            listaLogs = logExecucaoDAO.listarTodos();
            
            System.out.println("Listando todos os logs no disco...\n");
            for(logExecucao l : listaLogs){
                System.out.println(" | ID: " + l.getId() + 
                " | Tipo: " + l.getTipo() + 
                " | Mensagem: " + l.getMensagem() + 
                " | Detalhes: " + l.getDetalhes() + 
                " | Data: " + l.getDataExecucao() + 
                " | Sucesso: " + l.getSucesso() 
                );
            }
        }catch(SQLException e){
            System.out.println("Erro ao listar logs : " + e.getMessage() );
        }
    }
}
