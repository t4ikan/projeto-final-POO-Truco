package br.com.truco.engine;

import br.com.truco.model.Carta;
import br.com.truco.model.Dupla;
import br.com.truco.model.Jogador;

import java.util.List;
import java.util.Scanner;


public class InterfaceUsuario {
    private final Scanner scanner;

    public InterfaceUsuario() {
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuPrincipal() {
        System.out.println("\n--- JOGO DE TRUCO ---");
        System.out.println("1. Cadastrar jogador");
        System.out.println("2. Cadastrar dupla");
        System.out.println("3. Iniciar jogo");
        System.out.println("4. Ver estatísticas");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    public int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Por favor, digite um número.");
            return -1;
        }
    }

    public String lerNome(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

    public void listarJogadores(List<Jogador> jogadores) {
        System.out.println("Jogadores disponíveis:");
        for (int i = 0; i < jogadores.size(); i++) {
            System.out.println((i + 1) + ". " + jogadores.get(i).getNome());
        }
    }

    public void listarDuplas(List<Dupla> duplas) {
        System.out.println("Duplas disponíveis:");
        for (int i = 0; i < duplas.size(); i++) {
            System.out.println((i + 1) + ". " + duplas.get(i));
        }
    }

    public void anunciarInicioQueda(Dupla d1, Dupla d2) {
        System.out.println("\n--- INICIANDO NOVA QUEDA ---");
        System.out.println(d1 + " vs " + d2);
    }

    public void anunciarInicioJogo() {
        System.out.println("\n--- Iniciando novo jogo. Melhor de 12 pontos. ---");
    }

    public void mostrarPlacarJogo(Dupla d1, int p1, Dupla d2, int p2) {
        System.out.println("\n--- PLACAR DO JOGO ---");
        System.out.println(d1 + ": " + p1 + " pontos");
        System.out.println(d2 + ": " + p2 + " pontos");
        System.out.println("----------------------");
    }

    public void mostrarPlacarQueda(Dupla d1, int j1, Dupla d2, int j2) {
        System.out.println("\n--- PLACAR DA QUEDA ---");
        System.out.println(d1 + ": " + j1 + " jogos ganhos");
        System.out.println(d2 + ": " + j2 + " jogos ganhos");
        System.out.println("----------------------");
    }

    public void anunciarVencedorQueda(Dupla vencedora) {
        System.out.println("\n*** " + vencedora + " VENCEU A QUEDA! ***\n");
    }

    public Carta pedirCarta(Jogador jogador) {
        System.out.println("\n> Vez de " + jogador.getNome() + ". Sua mão:");
        List<Carta> mao = jogador.getMao();
        for (int i = 0; i < mao.size(); i++) {
            System.out.println((i + 1) + ": " + mao.get(i));
        }

        if (mao.size() == 1) {
            return jogador.jogarCarta(0);
        }

        int escolha = -1;
        while (escolha < 1 || escolha > mao.size()) {
            System.out.print("Escolha a carta para jogar (1-" + mao.size() + "): ");
            escolha = lerOpcao();
        }
        return jogador.jogarCarta(escolha - 1);
    }

    public int pedirAcao(Jogador jogador) {
        System.out.println("\n> Vez de " + jogador.getNome() + ". Sua mão: " + jogador.getMao());
        System.out.println("Ações: 1-Jogar Carta, 2-TRUCO");
        int escolha = -1;
        while(escolha != 1 && escolha != 2) {
            System.out.print("Escolha: ");
            escolha = lerOpcao();
        }
        return escolha;
    }

    public boolean responderTruco(Dupla duplaRespondendo) {
        System.out.println("\n" + duplaRespondendo + ", vocês aceitam o truco?");
        System.out.println("1- ACEITAR | 2- CORRER");
        int escolha = -1;
        while(escolha != 1 && escolha != 2) {
            System.out.print("Escolha: ");
            escolha = lerOpcao();
        }
        if (escolha == 1) System.out.println("O TRUCO FOI ACEITO!");
        else System.out.println(duplaRespondendo + " correu!");
        return escolha == 1;
    }

    public void mostrarJogada(Jogador j, Carta c) {
        System.out.println(j.getNome() + " jogou: " + c);
    }

    public void mostrarResultadoVaza(int numVaza, Jogador vencedor) {
        if (vencedor == null) {
            System.out.println(">> A vaza " + numVaza + " empatou!");
        } else {
            System.out.println(">> " + vencedor.getNome() + " venceu a vaza " + numVaza + "!");
        }
    }

    public void mostrarEstatisticas(List<Dupla> duplas) {
        System.out.println("\n--- ESTATÍSTICAS DAS DUPLAS ---");
        if (duplas.isEmpty()) {
            mostrarMensagem("Nenhuma dupla cadastrada.");
            return;
        }
        for (Dupla dupla : duplas) {
            System.out.println(dupla + " -> Quedas Jogadas: " + dupla.getQuedasJogadas() +
                    " | Quedas Ganhas: " + dupla.getQuedasGanhas());
        }
    }

    public void esperarEnter() {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }
}
