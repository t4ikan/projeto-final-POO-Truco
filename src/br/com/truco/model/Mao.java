package br.com.truco.model;

import br.com.truco.engine.InterfaceUsuario;

import java.util.ArrayList;
import java.util.List;

public class Mao {
    private final List<Jogador> ordemJogadores;
    private final Dupla dupla1, dupla2;
    private final InterfaceUsuario iu;

    private int valorMao;
    private int pontosD1, pontosD2; // Pontos correntes no jogo (para regra da Mão de Onze)

    public Mao(List<Jogador> ordemJogadores, Dupla d1, Dupla d2, int pontosD1, int pontosD2) {
        this.ordemJogadores = ordemJogadores;
        this.dupla1 = d1;
        this.dupla2 = d2;
        this.pontosD1 = pontosD1;
        this.pontosD2 = pontosD2;
        this.iu = new InterfaceUsuario();
        this.valorMao = ehMaoDeOnze() ? 3 : 1;
    }

    public int[] jogar() {
        distribuirCartas();

        int[] vitoriasVaza = new int[2]; // Posição 0: Dupla 1, Posição 1: Dupla 2
        int jogadorInicialVaza = 0;
        Dupla duplaPediuTruco = null;

        for (int i = 0; i < 3; i++) {
            Vaza vaza = new Vaza(ordemJogadores, jogadorInicialVaza);

            // Loop de jogadas dentro de uma vaza
            while (!vaza.estaFechada()) {
                Jogador jogadorDaVez = vaza.getProximoJogador();

                // Lógica de Ações (Jogar, Trucar, etc.)
                if (duplaPediuTruco == null && !ehMaoDeOnze()) {
                    int acao = iu.pedirAcao(jogadorDaVez);
                    if (acao == 2) { // TRUCO
                        Dupla duplaOponente = dupla1.contemJogador(jogadorDaVez) ? dupla2 : dupla1;
                        boolean aceitou = iu.responderTruco(duplaOponente);
                        if (aceitou) {
                            valorMao = 3;
                            duplaPediuTruco = dupla1.contemJogador(jogadorDaVez) ? dupla1 : dupla2;
                        } else {
                            // Dupla correu
                            if (dupla1.contemJogador(jogadorDaVez)) return new int[]{1, 0};
                            else return new int[]{0, 1};
                        }
                    }
                }

                Carta cartaJogada = iu.pedirCarta(jogadorDaVez);
                vaza.adicionarJogada(jogadorDaVez, cartaJogada);
                iu.mostrarJogada(jogadorDaVez, cartaJogada);
            }

            Jogador vencedorVaza = vaza.getVencedor(dupla1);
            if (vencedorVaza == null) { // Empate
                iu.mostrarResultadoVaza(i + 1, null);
                vitoriasVaza[0]++;
                vitoriasVaza[1]++;
            } else {
                iu.mostrarResultadoVaza(i + 1, vencedorVaza);
                if (dupla1.contemJogador(vencedorVaza)) vitoriasVaza[0]++;
                else vitoriasVaza[1]++;
                jogadorInicialVaza = ordemJogadores.indexOf(vencedorVaza);
            }

            // Checar se a mão já acabou
            Dupla vencedoraMao = determinarVencedorMao(vitoriasVaza);
            if (vencedoraMao != null) {
                if (vencedoraMao.equals(dupla1)) return new int[]{valorMao, 0};
                else return new int[]{0, valorMao};
            }
        }

        // Se chegar aqui, todas as 3 vazas foram jogadas e empatadas.
        // Regra: Ninguém marca ponto.
        return new int[]{0, 0};
    }

    private boolean ehMaoDeOnze() {
        return pontosD1 == 11 || pontosD2 == 11;
    }

    private void distribuirCartas() {
        Baralho baralho = new Baralho();
        for (Jogador jogador : ordemJogadores) {
            List<Carta> mao = new ArrayList<>();
            mao.add(baralho.retirarCarta());
            mao.add(baralho.retirarCarta());
            mao.add(baralho.retirarCarta());
            jogador.receberCartas(mao);
        }
    }

    private Dupla determinarVencedorMao(int[] vitoriasVaza) {
        // Alguém fez 2 vitórias
        if (vitoriasVaza[0] == 2 && vitoriasVaza[1] < 2) return dupla1;
        if (vitoriasVaza[1] == 2 && vitoriasVaza[0] < 2) return dupla2;

        // Empatou a primeira e alguém ganhou a segunda
        if (vitoriasVaza[0] == 2 && vitoriasVaza[1] == 1) return dupla1;
        if (vitoriasVaza[1] == 2 && vitoriasVaza[0] == 1) return dupla2;

        return null;
    }
}

class Vaza {
    private final List<Jogador> ordemJogadores;
    private final Jogador[] jogadoresDaVaza = new Jogador[4];
    private final Carta[] cartasJogadas = new Carta[4];
    private int jogadasFeitas = 0;
    private int jogadorInicial;

    public Vaza(List<Jogador> ordemGeral, int jogadorInicial) {
        this.ordemJogadores = ordemGeral;
        this.jogadorInicial = jogadorInicial;
    }

    public Jogador getProximoJogador() {
        return ordemJogadores.get((jogadorInicial + jogadasFeitas) % 4);
    }

    public void adicionarJogada(Jogador jogador, Carta carta) {
        jogadoresDaVaza[jogadasFeitas] = jogador;
        cartasJogadas[jogadasFeitas] = carta;
        jogadasFeitas++;
    }

    public boolean estaFechada() {
        return jogadasFeitas == 4;
    }

    public Jogador getVencedor(Dupla dupla1) {
        Carta maiorCarta = null;
        Jogador vencedor = null;
        boolean empate = false;

        for (int i = 0; i < 4; i++) {
            if (maiorCarta == null) {
                maiorCarta = cartasJogadas[i];
                vencedor = jogadoresDaVaza[i];
            } else {
                if (cartasJogadas[i].getForca() > maiorCarta.getForca()) {
                    maiorCarta = cartasJogadas[i];
                    vencedor = jogadoresDaVaza[i];
                    empate = false;
                } else if (cartasJogadas[i].getForca() == maiorCarta.getForca()) {
                    // Se a carta atual empata com a maior, verifica se são de duplas diferentes.
                    if (!dupla1.contemJogador(vencedor) == dupla1.contemJogador(jogadoresDaVaza[i])) {
                        empate = true;
                    }
                }
            }
        }
        return empate ? null : vencedor;
    }
}