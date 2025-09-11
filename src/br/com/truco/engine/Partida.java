package br.com.truco.engine;

import java.util.ArrayList;
import java.util.Arrays;
import br.com.truco.model.Dupla;
import br.com.truco.model.Jogador;

public class Partida {
    private final Dupla dupla1, dupla2;
    private final InterfaceUsuario iu;
    private int vitoriasD1 = 0, vitoriasD2 = 0;

    public Partida(Dupla d1, Dupla d2, InterfaceUsuario iu) {
        this.dupla1 = d1;
        this.dupla2 = d2;
        this.iu = iu;
        d1.registrarParticipacaoQueda();
        d2.registrarParticipacaoQueda();
    }

    public void jogar() {
        ArrayList<Jogador> jogadores = new ArrayList<>(Arrays.asList(dupla1.getJogador1(), dupla2.getJogador1(), dupla1.getJogador2(), dupla2.getJogador2()));

        while (vitoriasD1 < 2 && vitoriasD2 < 2) {
            Jogo jogo = new Jogo(dupla1, dupla2, jogadores, iu);
            Dupla vencedoraJogo = jogo.jogar();
            if (vencedoraJogo.equals(dupla1)) vitoriasD1++;
            else vitoriasD2++;
            iu.mostrarPlacar("QUEDA", dupla1, vitoriasD1, dupla2, vitoriasD2);
        }

        if (vitoriasD1 > vitoriasD2) {
            dupla1.registrarVitoriaQueda();
            iu.anunciarVencedor("QUEDA", dupla1);
        } else {
            dupla2.registrarVitoriaQueda();
            iu.anunciarVencedor("QUEDA", dupla2);
        }
    }
}