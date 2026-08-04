package com.automacao.estoque.model;

import java.time.LocalDateTime;

public class logExecucao {

    private Long id;
    private String tipo;
    private String mensagem;
    private String detalhes;
    private LocalDateTime data;
    private boolean sucesso;

    public logExecucao(Long id, String tipo, String mensagem, String detalhes, LocalDateTime data, boolean sucesso){
        this.id = id;
        this.tipo = tipo;
        this.mensagem = mensagem;
        this.detalhes = detalhes;
        this.data = data;
        this.sucesso = sucesso;

    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getTipo(){
        return tipo;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getMensagem(){
        return mensagem;
    }
    public void setMensagem(String mensagem){
        this.mensagem = mensagem;
    }

    public String getDetalhes(){
        return detalhes;
    }
    public void setDetalhes(String detalhes){
        this.detalhes = detalhes;
    }

    public LocalDateTime getDataExecucao(){
        return data;
    }
    public void setDataExecucao(LocalDateTime data){
        this.data = data;
    }

    public boolean getSucesso(){
        return sucesso;
    }
    public void setSucesso(boolean sucesso){
        this.sucesso = sucesso;
    }

    @Override
    public String toString(){
        return String.format("Log de execução{id=d%, tipo='%s', mensagem='%s', detalhes='s%'}", id, tipo, mensagem, detalhes);
    }
    
}
