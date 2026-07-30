package com.automacao.estoque.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimentacao {
    private Long id;
    private Long produtoId;
    private String tipo; // "ENTRADA", "SAIDA", "AJUSTE"
    private Integer quantidade;
    private Integer quantidadeAnterior;
    private Integer quantidadeNova;
    private String origem; // "EXCEL", "MANUAL", "SISTEMA"
    private LocalDateTime dataMovimentacao;

    public Movimentacao(){}

    public Movimentacao(Long produtoId, String tipo, Integer quantidade, 
        Integer quantidadeAnterior, Integer quantidadeNova, String origem){
            this.produtoId = produtoId;
            this.tipo = tipo;
            this.quantidade = quantidade;
            this.quantidadeAnterior = quantidadeAnterior;
            this.quantidadeNova = quantidadeNova;
            this.origem = origem;
            this.dataMovimentacao = LocalDateTime.now();
    }
    // Getters e Setters
    public Long getId(){
        return id;
    }
    // Define o id de movimentação. 
    public void setId(Long id){
        this.id = id;
    }
    // Define o id do produto.
    public Long getProdutoId(){
        return produtoId;
    }
    public void setProdutoId(Long produtoId){
        this.produtoId = produtoId;
    }
    public String getTipo(){
        return tipo;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    public Integer getQuantidade(){
        return quantidade;
    }
    public void setQuantidade(Integer quantidade){
        this.quantidade = quantidade;
    }
    public Integer getQuantidadeAnterior(){
        return quantidadeAnterior;
    }
    public void setQuantidadeAnterior(Integer quantidadeAnterior){
        this.quantidadeAnterior = quantidadeAnterior;
    }
    public Integer getQuantidadeNova() { return quantidadeNova; }
    
    public void setQuantidadeNova(Integer quantidadeNova) { this.quantidadeNova = quantidadeNova; }

    public String getOrigem() { return origem; }
    
    public void setOrigem(String origem) { this.origem = origem; }
    
    public LocalDateTime getDataMovimentacao() { return dataMovimentacao; }
    
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }
    
    @Override
    public String toString() {
        return String.format("Movimentacao{produtoId=%d, tipo='%s', qtd=%d, origem='%s'}", 
                            produtoId, tipo, quantidade, origem);
    }
}
