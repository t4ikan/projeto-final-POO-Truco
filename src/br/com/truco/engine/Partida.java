package br.com.truco.engine;

import br.com.truco.model.Dupla;
import br.com.truco.model.Jogador;
import br.com.truco.model.Mao;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Partida {
    private static final int PONTOS_PARA_VENCER_JOGO = 12;
    private static final int JOGOS_PARA_VENCER_QUEDA = 2;

    private final Dupla dupla1;
    private final Dupla dupla2;
    private final InterfaceUsuario iu = new InterfaceUsuario();

    public Partida(Dupla d1, Dupla d2) {
        this.dupla1 = d1;
        this.dupla2 = d2;
    }



    public void jogarQueda() {
        iu.anunciarInicioQueda(dupla1, dupla2);
        dupla1.registrarParticipacaoQueda();
        dupla2.registrarParticipacaoQueda();

        int jogosGanhosD1 = 0;
        int jogosGanhosD2 = 0;

        // Lista de jogadores para definir a ordem (pé da rodada)
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(dupla1.getJogador1());
        jogadores.add(dupla2.getJogador1());
        jogadores.add(dupla1.getJogador2());
        jogadores.add(dupla2.getJogador2());

        int indicePe = 0; // O "pé" é o último a jogar

        while (jogosGanhosD1 < JOGOS_PARA_VENCER_QUEDA && jogosGanhosD2 < JOGOS_PARA_VENCER_QUEDA) {

            // A ordem de jogar é rotacionada a cada jogo
            Collections.rotate(jogadores, 1);

            Dupla vencedoraJogo = jogarJogo(new ArrayList<>(jogadores));

            if (vencedoraJogo.equals(dupla1)) jogosGanhosD1++;
            else jogosGanhosD2++;

            iu.mostrarPlacarQueda(dupla1, jogosGanhosD1, dupla2, jogosGanhosD2);
        }

        if (jogosGanhosD1 > jogosGanhosD2) {
            dupla1.registrarVitoriaQueda();
            iu.anunciarVencedorQueda(dupla1);
        } else {
            dupla2.registrarVitoriaQueda();
            iu.anunciarVencedorQueda(dupla2);
        }
    }

    private Dupla jogarJogo(List<Jogador> ordemJogadores) {
        int pontosD1 = 0;
        int pontosD2 = 0;
        iu.anunciarInicioJogo();

        while (pontosD1 < PONTOS_PARA_VENCER_JOGO && pontosD2 < PONTOS_PARA_VENCER_JOGO) {
            iu.mostrarPlacarJogo(dupla1, pontosD1, dupla2, pontosD2);

            Collections.rotate(ordemJogadores, 1); // Rotaciona o "mão"

            Mao mao = new Mao(new ArrayList<>(ordemJogadores), dupla1, dupla2, pontosD1, pontosD2);
            int[] resultadoMao = mao.jogar();
            pontosD1 += resultadoMao[0];
            pontosD2 += resultadoMao[1];
        }

        return pontosD1 >= PONTOS_PARA_VENCER_JOGO ? dupla1 : dupla2;
    }
}

