package com.automacao.estoque.tests;


import java.time.LocalDateTime;

import com.automacao.estoque.model.Movimentacao;


public class TesteMovimentacoes {

    public static void main(String[] args){
        System.out.println("Teste para movimentações.");
        /* 
        Movimentacao mov = new Movimentacao();
        mov.setId(1L);
        mov.setProdutoId(100L);
        mov.setTipo("Entrada");
        mov.setQuantidade(300);
        mov.setQuantidadeAnterior(197);
        mov.setQuantidadeNova(452);
        mov.setOrigem("Sistema");
        mov.setDataMovimentacao(null);
        */
        
        Movimentacao mov = new Movimentacao(100L, "Entrada", 300, 197, 452, "Excel");

        System.out.println("Movimentação 1 criada com sucesso!");
        System.out.println("Listando dados...");

        System.out.println(mov);



    }




}
