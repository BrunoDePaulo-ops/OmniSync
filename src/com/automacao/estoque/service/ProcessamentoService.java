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
import java.util.Objects;




public class ProcessamentoService {
    
    private excelService excelService;
    private PDFService PDFService;
    private produtoDAO produtoDAO;
    private logExecucaoDAO logExecucaoDAO;
    private movimentacaoDAO movimentacaoDAO;
    
    public ProcessamentoService(){
        this.excelService = new excelService();
        this.PDFService = new PDFService();
        this.produtoDAO = new produtoDAO();
        this.logExecucaoDAO = new logExecucaoDAO();
        this.movimentacaoDAO = new movimentacaoDAO();
        
        
    }
    
    

    public List<Produto> lerPlanilha(){
        List <Produto> produtos = new ArrayList<>();

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
        
            
        }catch(FileNotFoundException e){
            System.err.println("Erro: Arquivo 'produtos.xlsx' não foi encontrado na pasta 'uploads'.");
            System.err.println("Verifique se o caminho na raiz do projeto está correto.");

        }catch(IOException e){
            System.err.println("Erro de entrada/saída ao ler a planilha: " + e.getMessage());
            
        }
        
        return produtos;
    }

    public void lerProdutosNoBanco(){
        List <Produto> produtos = new ArrayList<>();

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
            
        }
    }

    public void sincronizar(){
        List <Produto> produtos = new ArrayList<>();

        produtos = lerPlanilha();

        try{
            produtoDAO.salvarEmLote(produtos);
            System.out.println("Dados sincronizados com sucesso.");

            logExecucao log = new logExecucao(
                "Sincronização", 
                "Sincronização realizada com sucesso", 
                "Sincronização entre planilha e banco", 
                true
            );

            try{
                logExecucaoDAO.gravarLogs(log);
            }catch(SQLException e){
                System.out.println("Erro ao realizar sincronização: " + e.getMessage());
                
                logExecucao logErro = new logExecucao(
                    "Sincronização", 
                    "Falha na sincronização", 
                    "Erro ao gravar log de sincronização", 
                    false
                );

                try{
                    logExecucaoDAO.gravarLogs(logErro);
                }catch(SQLException ex){
                    System.err.println("Erro crítico: Não foi possível gravar o log de erro: " + ex.getMessage());
                }

            }
        }catch(SQLException e){
            System.out.println("Erro ao tentar sincronização: " + e.getMessage());
        }

    }

    public void verificarEstoqueBaixo(){
        boolean baixo = false;
        List <Produto> produtos = new ArrayList<>();

        try{
            produtos = excelService.lerProduto("uploads/produtos.xlsx");

            for (Produto p : produtos){
                if(p.isEstoqueBaixo()){
                    baixo = true;
                    System.out.println("ALERTA: " + p.getNome() + 
                        " | Estoque: " + p.getQuantidadeEstoque() +
                        " | Mínimo: " + p.getEstoqueMinimo()
                    );

                }
            }

            if(!baixo){
                System.out.println("O estoque está OK, pode dormir tranquilo.");
            }
        }catch(IOException e){
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

    }

    public void emitirRelatorio(){
        List <Produto> produtos = new ArrayList<>();
        try{
            String nomePDF = "relatorio_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            String caminho = "reports/" + nomePDF;
            
            produtos = excelService.lerProduto("uploads/produtos.xlsx");
            PDFService.gerarRelatorio(produtos, caminho);
            System.out.println("Relatório gerado com sucesso na pasta reports.");

        }catch(IOException e){
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
            
        }catch(Exception e){
            System.out.println("Erro ao desenvolver relatório: " + e.getMessage());
        }
    }

    public void lerLogs(){
        List <logExecucao> listaLogs = new ArrayList<>(); 
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

    public void verMovimentacoesPorNomeDoProduto(String nome){
        List<Movimentacao> movimentacoes = new ArrayList<>();
        

        try{
            movimentacoes = movimentacaoDAO.listarMovimentacoesPorNomeDoProduto(nome); 
            if(movimentacoes == null || movimentacoes.isEmpty()){
                System.out.println("Nenhuma movimentação encontrada.");
                
            }else{
                System.out.println("Listando movimentações encontradas para o produto: " + nome);

                for (Movimentacao m : movimentacoes){
                    System.out.printf(" %10s | %-7s | %10s | %19s | %15s | %-6s%n",
                    Objects.toString(m.getProdutoId(), "null"),
                    Objects.toString(m.getTipo(), ""),
                    Objects.toString(m.getQuantidade(), "0"),
                    Objects.toString(m.getQuantidadeAnterior(), "0"),
                    Objects.toString(m.getQuantidadeNova(), "0"),
                    Objects.toString(m.getOrigem(), "")
                    );
   
                }
                System.out.println("O total de movimentações encontrada para o produto " + nome + ": " + movimentacoes.size());
            }
        }catch(SQLException e){
            System.out.println("Erro ao buscar movimentações para o produto: " + nome );
        }

    }
}
