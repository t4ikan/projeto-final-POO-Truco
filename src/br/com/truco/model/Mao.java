package br.com.truco.model;

import br.com.truco.engine.InterfaceUsuario;

import java.util.ArrayList;
import java.util.List;

public class Mao {
    private final List<Jogador> ordemJogadores;
    private final Dupla dupla1, dupla2;
    private final InterfaceUsuario iu;

    private int valorMao;
    private final int pontosD1, pontosD2;

    public Mao(List<Jogador> ordemJogadores, Dupla d1, Dupla d2, int pontosD1, int pontosD2) {
        this.ordemJogadores = ordemJogadores;
        this.dupla1 = d1;
        this.dupla2 = d2;
        this.pontosD1 = pontosD1;
        this.pontosD2 = pontosD2;
        this.iu = new InterfaceUsuario();

        // A mão já começa valendo 3 se for Mão de Dez, ou 1 caso contrário.
        this.valorMao = ehMaoDeDez() ? 3 : 1;
    }

    public int[] jogar() {
        distribuirCartas();

        boolean isMaoDeDez = ehMaoDeDez();

        if (isMaoDeDez) {
            iu.mostrarMensagem("\n!!! ATENÇÃO: MÃO DE DEZ - A MÃO COMEÇA VALENDO " + this.valorMao + " PONTOS !!!");

            Dupla duplaComDez = pontosD1 >= 10 ? dupla1 : dupla2;
            Dupla duplaAdversaria = duplaComDez.equals(dupla1) ? dupla2 : dupla1;

            // Só oferece a opção de correr se apenas uma dupla tiver 10+ pontos.
            // Se ambas tiverem, o jogo é obrigatório.
            if (! (pontosD1 >= 10 && pontosD2 >= 10)) {
                boolean aceitouJogar = iu.querJogarMaoDeDez(duplaAdversaria);

                if (!aceitouJogar) { // A dupla adversária correu
                    this.valorMao = 1; // O valor da mão cai para 1
                    iu.mostrarMensagem(duplaAdversaria + " correu! " + duplaComDez + " vence e ganha " + this.valorMao + " ponto.");
                    return duplaComDez.equals(dupla1) ? new int[]{this.valorMao, 0} : new int[]{0, this.valorMao};
                }
            }
        }

        int[] vitoriasVaza = new int[2];
        int jogadorInicialVaza = 0;

        for (int i = 0; i < 3; i++) {
            Vaza vaza = new Vaza(ordemJogadores, jogadorInicialVaza);

            while (!vaza.estaFechada()) {
                Jogador jogadorDaVez = vaza.getProximoJogador();

                // ALTERADO: Simplificado para não permitir NENHUM truco na Mão de Dez.
                // Na mão normal, ainda permite trucar.
                if (!isMaoDeDez) {
                    int acao = iu.pedirAcao(jogadorDaVez, true); // Permite trucar
                    if (acao == 2) { // TRUCO
                        Dupla duplaOponente = dupla1.contemJogador(jogadorDaVez) ? dupla2 : dupla1;
                        boolean aceitou = iu.responderTruco(duplaOponente);
                        if (aceitou) {
                            valorMao = 3;
                        } else {
                            return dupla1.contemJogador(jogadorDaVez) ? new int[]{1, 0} : new int[]{0, 1};
                        }
                    }
                }

                // Se for Mão de Dez, o jogador é forçado a apenas jogar a carta.
                Carta cartaJogada = iu.pedirCarta(jogadorDaVez);
                vaza.adicionarJogada(jogadorDaVez, cartaJogada);
                iu.mostrarJogada(jogadorDaVez, cartaJogada);
            }

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

            Dupla vencedoraMao = determinarVencedorMao(vitoriasVaza);
            if (vencedoraMao != null) {
                // O valor da mão já foi definido no início, então apenas retornamos o vencedor.
                return vencedoraMao.equals(dupla1) ? new int[]{valorMao, 0} : new int[]{0, valorMao};
            }
        }

        return new int[]{0, 0};
    }

    private boolean ehMaoDeDez() {
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
        if (vitoriasVaza[0] == 2 && vitoriasVaza[1] < 2) return dupla1;
        if (vitoriasVaza[1] == 2 && vitoriasVaza[0] < 2) return dupla2;
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