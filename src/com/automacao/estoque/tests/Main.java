package com.automacao.estoque.tests;

import com.automacao.estoque.service.*;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        ProcessamentoService proc = new ProcessamentoService();
        digitar(proc);
        
    }

    private static void digitar( ProcessamentoService proc){
        
    
        Scanner leitor = new Scanner(System.in);
        int numero = -1;
        boolean entradaValida = false;

        while(!entradaValida){
            System.out.println(" ========== OMNISYNC - Sistema de Gerenciamento de Estoques ========== \n");
            System.out.println(" ========== Menu ========== \n");
            System.out.println("Digite um número para uma ação...\n");
            System.out.println("1. Ler dados direto da planilha.");
            System.out.println("2. Ler dados direto do banco.");
            System.out.println("3. Sincronizar planilha, movimentações e banco.");
            System.out.println("4. Verificar se existem e quais são os produtos com estoque baixo. (Sincronize o sistema antes)");
            System.out.println("5. Emitir relatório em PDF. (Sincronize o sistema antes)");
            System.out.println("6. Verificar os logs gravados no sistema.");
            System.out.println("7. Verificar as movimentações de um produto específico.");
            System.out.println("0. Sair\n");

            if(leitor.hasNextInt()){
                numero = leitor.nextInt();
                if (numero >= 0 && numero <= 7) {
                    entradaValida = true; // Sai do laço while principal
                } else {
                System.out.println("\n[ERRO] O valor digitado está fora do intervalo (0 a 2). Tente novamente.");
                }
            }else {
                System.out.println("\n[ERRO] O valor digitado não é um número inteiro. Tente novamente.");
                leitor.next(); // IMPORTANTE: Limpa a letra/texto incorreto do Scanner para não travar o terminal
            }
        }
        switch (numero) {
            case 1 -> proc.lerPlanilha();
            case 2 -> proc.lerProdutosNoBanco();
            case 3 -> proc.sincronizar();
            case 4 -> proc.verificarEstoqueBaixo();
            case 5 -> proc.emitirRelatorio();
            case 6 -> proc.lerLogs();
            case 7 -> {
                leitor.nextLine(); 
                System.out.print("\nDigite o nome do produto: ");
                String nomeProduto = leitor.nextLine(); 
                proc.verMovimentacoesPorNomeDoProduto(nomeProduto); 
            }
            case 0 -> System.out.println("Saindo da aplicação...\n");

        }

        // Feche o leitor apenas no final de tudo
        leitor.close();
    
    
    }
    

}

