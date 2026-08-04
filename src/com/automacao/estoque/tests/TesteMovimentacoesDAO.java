package com.automacao.estoque.tests;

import com.automacao.estoque.model.*;
import com.automacao.estoque.dao.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;



public class TesteMovimentacoesDAO {

    public static void main(String[] args) {

        movimentacaoDAO dao = new movimentacaoDAO();

        try {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
