package br.com.truco.engine;

import java.util.ArrayList;
import java.util.Collections;
import br.com.truco.model.Dupla;
import br.com.truco.engine.InterfaceUsuario;
import br.com.truco.model.Jogador;
import br.com.truco.model.Mao;

public class Jogo {
    private final Dupla dupla1, dupla2;
    private final ArrayList<Jogador> jogadores;
    private final InterfaceUsuario iu;
    private int pontosD1 = 0, pontosD2 = 0;

    public Jogo(Dupla d1, Dupla d2, ArrayList<Jogador> jogadores, InterfaceUsuario iu) {
        this.dupla1 = d1;
        this.dupla2 = d2;
        this.jogadores = jogadores;
        this.iu = iu;
    }

    public Dupla jogar() {
        while (pontosD1 < 12 && pontosD2 < 12) {
            iu.mostrarPlacar("JOGO", dupla1, pontosD1, dupla2, pontosD2);
            Mao mao = new Mao(new ArrayList<>(jogadores), dupla1, dupla2, pontosD1, pontosD2, iu);
            int[] resultadoMao = mao.jogar();
            pontosD1 += resultadoMao[0];
            pontosD2 += resultadoMao[1];
            Collections.rotate(jogadores, 1);
        }

        Dupla vencedora = pontosD1 >= 12 ? dupla1 : dupla2;
        iu.anunciarVencedor("JOGO", vencedora);
        return vencedora;
    }
}