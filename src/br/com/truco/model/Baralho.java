package br.com.truco.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Baralho {
    private final List<Carta> cartas;

    public Baralho() {
        this.cartas = new ArrayList<>();
        this.criarBaralho();
        this.embaralhar();
    }

    private void criarBaralho() {
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "Q", "J", "K"};
        for (Naipe naipe : Naipe.values()) {
            for (String valor : valores) {
                this.cartas.add(new Carta(valor, naipe));
            }
        }
    }

    private void embaralhar() {
        Collections.shuffle(this.cartas);
    }

    public Carta retirarCarta() {
        if (this.cartas.isEmpty()) {
            return null;
        }
        return this.cartas.remove(0);
    }
}