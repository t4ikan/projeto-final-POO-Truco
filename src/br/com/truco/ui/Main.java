package br.com.truco.ui;

import java.util.ArrayList;
import java.util.Arrays;
import br.com.truco.model.Dupla;
import br.com.truco.model.Jogador;
import br.com.truco.engine.InterfaceUsuario;
import br.com.truco.engine.Partida;

public class Main {
    private final ArrayList<Jogador> jogadores = new ArrayList<>();
    private final ArrayList<Dupla> duplas = new ArrayList<>();
    private final InterfaceUsuario iu = new InterfaceUsuario();

    public Main() {
        jogadores.add(new Jogador("J1"));
        jogadores.add(new Jogador("J2"));
        jogadores.add(new Jogador("J3"));
        jogadores.add(new Jogador("J4"));
    }

    public static void main(String[] args) {
        new Main().executar();
    }

    public void executar() {
        int opcao;
        do {
            iu.mostrarMenuPrincipal();
            opcao = iu.lerOpcao("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> cadastrarJogador();
                case 2 -> cadastrarDupla();
                case 3 -> iniciarJogo();
                case 4 -> verEstatisticas();
                case 5 -> iu.mostrarMensagem("Obrigado por jogar!");
                default -> iu.mostrarMensagem("Opção inválida.");
            }
            if (opcao != 5) iu.esperarEnter();
        } while (opcao != 5);
    }

    private void cadastrarJogador() {
        String nome = iu.lerTexto("Digite o nome do jogador: ");
        jogadores.add(new Jogador(nome));
        iu.mostrarMensagem(nome + " cadastrado com sucesso!");
    }

    private void cadastrarDupla() {
        if (jogadores.size() < 2) {
            iu.mostrarMensagem("São necessários pelo menos 2 jogadores cadastrados.");
            return;
        }
        for (int i = 0; i < jogadores.size(); i++) {
            iu.mostrarMensagem((i + 1) + ". " + jogadores.get(i).getNome());
        }
        int idx1 = iu.lerOpcao("Escolha o primeiro jogador: ") - 1;
        int idx2 = iu.lerOpcao("Escolha o segundo jogador: ") - 1;

        if (idx1 >= 0 && idx1 < jogadores.size() && idx2 >= 0 && idx2 < jogadores.size() && idx1 != idx2) {
            duplas.add(new Dupla(jogadores.get(idx1), jogadores.get(idx2)));
            iu.mostrarMensagem("Dupla cadastrada com sucesso!");
        } else {
            iu.mostrarMensagem("Seleção inválida.");
        }
    }

    private void iniciarJogo() {
        if (duplas.size() < 2) {
            iu.mostrarMensagem("São necessárias pelo menos 2 duplas cadastradas.");
            return;
        }
        iu.mostrarMensagem("Selecione as duplas para o jogo:");
        for (int i = 0; i < duplas.size(); i++) {
            iu.mostrarMensagem((i + 1) + ". " + duplas.get(i));
        }
        int idx1 = iu.lerOpcao("Escolha a primeira dupla: ") - 1;
        int idx2 = iu.lerOpcao("Escolha a segunda dupla: ") - 1;

        if (idx1 >= 0 && idx1 < duplas.size() && idx2 >= 0 && idx2 < duplas.size() && idx1 != idx2) {
            Partida partida = new Partida(duplas.get(idx1), duplas.get(idx2), iu);
            partida.jogar();
        } else {
            iu.mostrarMensagem("Seleção inválida.");
        }
    }

    private void verEstatisticas() {
        iu.mostrarEstatisticas(duplas);
    }
}