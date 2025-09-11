package br.com.truco.model;

import br.com.truco.engine.InterfaceUsuario;

import java.util.ArrayList;
import java.util.List;

public class Mao {
    private final List<Jogador> ordemJogadores;
    private final Dupla dupla1, dupla2;
    private final InterfaceUsuario iu;

    private int valorMao;
    private final int pontosD1, pontosD2; // Pontos correntes no jogo

    public Mao(List<Jogador> ordemJogadores, Dupla d1, Dupla d2, int pontosD1, int pontosD2) {
        this.ordemJogadores = ordemJogadores;
        this.dupla1 = d1;
        this.dupla2 = d2;
        this.pontosD1 = pontosD1;
        this.pontosD2 = pontosD2;
        this.iu = new InterfaceUsuario();
        this.valorMao = 1; // A mão sempre começa valendo 1 ponto. O valor muda depois.
    }

    public int[] jogar() {
        distribuirCartas();
        // Verifica se alguma dupla está na mão de dez.
        boolean isMaoDeDez = ehMaoDeDez();
        if (isMaoDeDez) {
            iu.mostrarMensagem("\n!!! ATENÇÃO: MÃO DE DEZ !!!");
        }

        int[] vitoriasVaza = new int[2]; // Posição 0: Dupla 1, Posição 1: Dupla 2
        int jogadorInicialVaza = 0;
        Dupla duplaPediuTruco = null;

        for (int i = 0; i < 3; i++) {
            Vaza vaza = new Vaza(ordemJogadores, jogadorInicialVaza);

            while (!vaza.estaFechada()) {
                Jogador jogadorDaVez = vaza.getProximoJogador();

                // Lógica de Ações (Jogar, Trucar, etc
                if (duplaPediuTruco == null) { // Só permite pedir truco se ainda não foi pedido.

                    // Define se o jogador atual pode ou não pedir truco.
                    boolean podeTrucar = !isMaoDeDez || // Pode trucar se NÃO for mão de dez
                            (dupla1.contemJogador(jogadorDaVez) && pontosD1 < 10) || // Ou se for da dupla 1 e ela tiver menos de 10 pontos
                            (dupla2.contemJogador(jogadorDaVez) && pontosD2 < 10);   // Ou se for da dupla 2 e ela tiver menos de 10 pontos

                    int acao = iu.pedirAcao(jogadorDaVez, podeTrucar); // Passa a permissão para a interface

                    if (acao == 2) { // TRUCO
                        // Verifica a penalidade: se quem trucou já tinha 10 pontos.
                        if ((dupla1.contemJogador(jogadorDaVez) && pontosD1 >= 10) ||
                                (dupla2.contemJogador(jogadorDaVez) && pontosD2 >= 10)) {

                            iu.mostrarMensagem("!!! PENALIDADE !!! A dupla com 10 ou mais pontos não pode trucar!");
                            // Retorna 3 pontos para a dupla adversária.
                            return dupla1.contemJogador(jogadorDaVez) ? new int[]{0, 3} : new int[]{3, 0};
                        }

                        // Lógica normal do truco
                        Dupla duplaOponente = dupla1.contemJogador(jogadorDaVez) ? dupla2 : dupla1;
                        boolean aceitou = iu.responderTruco(duplaOponente);
                        if (aceitou) {
                            valorMao = 3;
                            duplaPediuTruco = dupla1.contemJogador(jogadorDaVez) ? dupla1 : dupla2;
                        } else {
                            // Dupla correu, a que pediu truco ganha os pontos da mão antes do pedido.
                            return dupla1.contemJogador(jogadorDaVez) ? new int[]{1, 0} : new int[]{0, 1};
                        }
                    }
                }

                Carta cartaJogada = iu.pedirCarta(jogadorDaVez);
                vaza.adicionarJogada(jogadorDaVez, cartaJogada);
                iu.mostrarJogada(jogadorDaVez, cartaJogada);
            }

            // Lógica para determinar o vencedor da vaza
            Jogador vencedorVaza = vaza.getVencedor(dupla1);
            if (vencedorVaza == null) {
                iu.mostrarResultadoVaza(i + 1, null);
                vitoriasVaza[0]++;
                vitoriasVaza[1]++;
            } else {
                iu.mostrarResultadoVaza(i + 1, vencedorVaza);
                if (dupla1.contemJogador(vencedorVaza)) vitoriasVaza[0]++;
                else vitoriasVaza[1]++;
                jogadorInicialVaza = ordemJogadores.indexOf(vencedorVaza);
            }

            // Checa se a mão já tem um vencedor
            Dupla vencedoraMao = determinarVencedorMao(vitoriasVaza);
            if (vencedoraMao != null) {
                // Calcula os pontos corretos para a Mão de Dez
                if (isMaoDeDez) {
                    // Se a dupla com menos de 10 pontos ganhou, a mão vale 3.
                    if (vencedoraMao.equals(dupla1) && pontosD1 < 10) {
                        valorMao = 3;
                    } else if (vencedoraMao.equals(dupla2) && pontosD2 < 10) {
                        valorMao = 3;
                    } else {
                        // Se a dupla com 10 pontos ganhou, a mão vale 1.
                        valorMao = 1;
                    }
                }
                return vencedoraMao.equals(dupla1) ? new int[]{valorMao, 0} : new int[]{0, valorMao};
            }
        }

        return new int[]{0, 0};
    }


    private boolean ehMaoDeDez() {
        // A verificação é >= 10 para cobrir o caso raro de uma dupla pular de 9 para 11, por exemplo.
        return pontosD1 >= 10 || pontosD2 >= 10;
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
    private final int jogadorInicial;

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
                    empate = true;
                }
            }
        }
        return empate ? null : vencedor;
    }
}