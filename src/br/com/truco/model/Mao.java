package br.com.truco.model;

import java.util.ArrayList;
import br.com.truco.engine.InterfaceUsuario;

public class Mao {
    private final Dupla dupla1, dupla2;
    private final ArrayList<Jogador> jogadores;
    private final int pontosJogoD1, pontosJogoD2;
    private final InterfaceUsuario iu;
    private int valorMao = 1;

    public Mao(ArrayList<Jogador> jogadores, Dupla d1, Dupla d2, int p1, int p2, InterfaceUsuario iu) {
        this.jogadores = jogadores;
        this.dupla1 = d1;
        this.dupla2 = d2;
        this.pontosJogoD1 = p1;
        this.pontosJogoD2 = p2;
        this.iu = iu;
    }

    public int[] jogar() {
        distribuirCartas();
        int[] vitoriasVaza = {0, 0};
        int jogadorInicialVaza = 0;
        Dupla duplaComTurnoAposta = null;

        for (int i = 0; i < 3; i++) {
            ArrayList<Carta> cartasVaza = new ArrayList<>();
            ArrayList<Jogador> jogadoresVaza = new ArrayList<>();

            for (int j = 0; j < 4; j++) {
                int idxJogadorAtual = (jogadorInicialVaza + j) % 4;
                Jogador jogadorAtual = jogadores.get(idxJogadorAtual);
                Carta cartaJogada = obterJogada(jogadorAtual, duplaComTurnoAposta);

                if (cartaJogada == null) {
                    Dupla duplaCorreu = dupla1.contemJogador(jogadorAtual) ? dupla1 : dupla2;
                    return duplaCorreu.equals(dupla1) ? new int[]{0, valorMao} : new int[]{valorMao, 0};
                }
                cartasVaza.add(cartaJogada);
                jogadoresVaza.add(jogadorAtual);
                iu.mostrarJogada(jogadorAtual, cartaJogada);
            }

            Jogador vencedorVaza = determinarVencedorVaza(cartasVaza, jogadoresVaza);
            iu.mostrarVencedorVaza(vencedorVaza);

            if (vencedorVaza == null) {
                vitoriasVaza[0]++;
                vitoriasVaza[1]++;
            } else {
                if (dupla1.contemJogador(vencedorVaza)) vitoriasVaza[0]++;
                else vitoriasVaza[1]++;
                jogadorInicialVaza = jogadores.indexOf(vencedorVaza);
            }

            Dupla vencedoraMao = checarVencedorMao(vitoriasVaza);
            if (vencedoraMao != null) {
                if (dupla1.equals(vencedoraMao)) {
                    return new int[]{valorMao, 0};
                } else {
                    return new int[]{0, valorMao};
                }
            }
        }
        return new int[]{0, 0};
    }

    private void distribuirCartas() {
        Baralho baralho = new Baralho();
        baralho.embaralhar();
        for (Jogador j : jogadores) {

            j.getMao().clear();

            j.getMao().add(baralho.distribuir());
            j.getMao().add(baralho.distribuir());
            j.getMao().add(baralho.distribuir());
        }
    }

    private Jogador determinarVencedorVaza(ArrayList<Carta> cartas, ArrayList<Jogador> jogadores) {
        Carta maiorCarta = cartas.get(0);
        Jogador vencedor = jogadores.get(0);
        boolean empate = false;
        for (int i = 1; i < cartas.size(); i++) {
            if (cartas.get(i).peso > maiorCarta.peso) {
                maiorCarta = cartas.get(i);
                vencedor = jogadores.get(i);
                empate = false;
            } else if (cartas.get(i).peso == maiorCarta.peso) {
                empate = true;
            }
        }
        return empate ? null : vencedor;
    }

    private Dupla checarVencedorMao(int[] vitorias) {
        if (vitorias[0] >= 2 && vitorias[0] > vitorias[1]) return dupla1;
        if (vitorias[1] >= 2 && vitorias[1] > vitorias[0]) return dupla2;
        return null;
    }

    private Carta obterJogada(Jogador jogador, Dupla duplaPodeAumentar) {
        while (true) {
            iu.mostrarMao(jogador);
            String promptAcoes = "(1-" + jogador.getMao().size() + ") Jogar Carta";
            Dupla duplaJogador = dupla1.contemJogador(jogador) ? dupla1 : dupla2;

            boolean emMaoDeDez = (dupla1.equals(duplaJogador) && pontosJogoD1 == 10) || (dupla2.equals(duplaJogador) && pontosJogoD2 == 10);
            boolean podeApostar = (duplaPodeAumentar == null || duplaPodeAumentar.equals(duplaJogador)) && valorMao < 12;

            if (emMaoDeDez) {
                iu.mostrarMensagem("!! MÃO DE DEZ: Não é permitido trucar!");
                podeApostar = false;
            }

            if (podeApostar) {
                promptAcoes += " | (99) " + getNomeAposta();
            }

            int acao = iu.lerOpcao(promptAcoes + ": ");
            if (acao >= 1 && acao <= jogador.getMao().size()) {
                return jogador.jogarCarta(acao - 1);
            } else if (acao == 99 && podeApostar) {
                iu.mostrarMensagem("\n" + jogador.getNome() + " pediu " + getNomeAposta() + "!");
                int resposta = iu.lerOpcao(getDuplaOponente(duplaJogador) + ", aceitam? (1-Aceitar, 2-Correr, 3-Aumentar): ");
                if (resposta == 1) {
                    valorMao = getValorProximaAposta();
                    duplaPodeAumentar = null;
                } else if (resposta == 2) {
                    return null; // Retorna null para indicar que o jogador correu
                } else if (resposta == 3) {
                    valorMao = getValorProximaAposta();
                    duplaPodeAumentar = getDuplaOponente(duplaJogador);
                }
            } else {
                iu.mostrarMensagem("Ação inválida.");
            }
        }
    }

    private String getNomeAposta() {
        return switch (valorMao) {
            case 1 -> "TRUCO";
            case 3 -> "SEIS";
            case 6 -> "NOVE";
            case 9 -> "DOZE";
            default -> "";
        };
    }

    private int getValorProximaAposta() {
        return switch (valorMao) {
            case 1 -> 3;
            case 3 -> 6;
            case 6 -> 9;
            case 9 -> 12;
            default -> valorMao;
        };
    }

    private Dupla getDuplaOponente(Dupla dupla) {
        return dupla.equals(dupla1) ? dupla2 : dupla1;
    }
}