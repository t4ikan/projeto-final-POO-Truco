package br.com.truco.engine;

import java.util.ArrayList;
import java.util.Scanner;

import br.com.truco.model.Dupla;
import br.com.truco.model.Jogador;
import br.com.truco.model.Carta;
import br.com.truco.model.Mao;


public class InterfaceUsuario {
    private final Scanner scanner = new Scanner(System.in);

    public int lerOpcao(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void mostrarMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Cadastrar Jogador");
        System.out.println("2. Cadastrar Dupla");
        System.out.println("3. Iniciar Jogo");
        System.out.println("4. Ver Estatísticas");
        System.out.println("5. Sair");
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

    public void esperarEnter() {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    public void mostrarMao(Jogador jogador) {
        System.out.println("\n> Vez de " + jogador.getNome() + ". Sua mão: " + jogador.getMao());
    }

    public void mostrarJogada(Jogador j, Carta c) {
        System.out.println("  " + j.getNome() + " jogou: " + c);
    }

    public void mostrarVencedorVaza(Jogador vencedor) {
        if (vencedor != null) {
            System.out.println(">> " + vencedor.getNome() + " venceu a vaza!");
        } else {
            System.out.println(">> Vaza empatada (cangou)!");
        }
    }

    public void mostrarPlacar(String tipo, Object entidade1, int placar1, Object entidade2, int placar2) {
        System.out.println("\n--- PLACAR " + tipo.toUpperCase() + " ---");
        System.out.println(entidade1 + ": " + placar1);
        System.out.println(entidade2 + ": " + placar2);
        System.out.println("--------------------");
    }

    public void anunciarVencedor(String tipo, Object vencedor) {
        System.out.println("\n*** " + vencedor + " VENCEU O " + tipo.toUpperCase() + "! ***");
    }

    public void mostrarEstatisticas(ArrayList<Dupla> duplas) {
        System.out.println("\n--- ESTATÍSTICAS ---");
        if (duplas.isEmpty()) {
            System.out.println("Nenhuma dupla cadastrada.");
            return;
        }
        for (Dupla d : duplas) {
            System.out.println(d + " | Quedas Jogadas: " + d.getQuedasJogadas() + " | Vitórias: " + d.getQuedasGanhas());
        }
    }
}