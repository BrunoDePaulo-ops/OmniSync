package com.automacao.estoque.tests;

import com.automacao.estoque.model.*;
import com.automacao.estoque.dao.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.SQLException;



public class TesteMovimentacoesDAO {

    public static void main(String[] args) {

        movimentacaoDAO dao = new movimentacaoDAO();

        try {

            /* 
            List<Movimentacao> lista = new ArrayList<>();
                    lista = dao.listarMovimentacoesPorProduto("PROD001");

            if (lista == null || lista.isEmpty()) {
                System.out.println("Nenhuma movimentação encontrada.");
            } else {

                for (Movimentacao m : lista) {

                    System.out.println("----------------");

                    System.out.println("Produto: " + m.getProdutoId());
                    System.out.println("Tipo: " + m.getTipo());
                    System.out.println("Quantidade: " + m.getQuantidade());
                    System.out.println("Anterior: " + m.getQuantidadeAnterior());
                    System.out.println("Nova: " + m.getQuantidadeNova());
                    System.out.println("Origem: " + m.getOrigem());
                }

            }
            */
            List<Movimentacao> mov = new ArrayList<>();
            mov = dao.listarMovimentacoesPorNomeDoProduto("MONITOR LG");

            if(mov == null || mov.isEmpty()){
                System.out.println("Nenhuma movimentação encontrada.");
            }else{
                for (Movimentacao m : mov){
        
                    System.out.printf(" %10s | %-7s | %10s | %19s | %15s | %-6s%n",
                    Objects.toString(m.getProdutoId(), "null"),
                    Objects.toString(m.getTipo(), ""),
                    Objects.toString(m.getQuantidade(), "0"),
                    Objects.toString(m.getQuantidadeAnterior(), "0"),
                    Objects.toString(m.getQuantidadeNova(), "0"),
                    Objects.toString(m.getOrigem(), "")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
