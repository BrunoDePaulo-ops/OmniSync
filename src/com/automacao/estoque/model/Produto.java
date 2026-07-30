package com.automacao.estoque.model;

import java.math.BigDecimal;

public class Produto {
    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal preco;
    private int quantidadeEstoque;
    private int estoqueMinimo;

    public Produto(){

    }

    public Produto(String codigo, String nome, BigDecimal preco, int quantidadeEstoque){
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;

        
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getCodigo(){
        return codigo;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getcategoria(){
        return categoria;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public BigDecimal getPreco(){
        return preco;
    }

    public void setPreco(BigDecimal preco){
        this.preco = preco;
    }

    public int getQuantidadeEstoque(){
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque){
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getEstoqueMinimo(){
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo){
        this.estoqueMinimo = estoqueMinimo;
    }

    public boolean isEstoqueBaixo() {
        return quantidadeEstoque < estoqueMinimo;
    }

    @Override
    public String toString() {
        return String.format("Produto{codigo='%s', nome='%s', quantidade=%d}", 
                           codigo, nome, quantidadeEstoque);
                           
    }


}
