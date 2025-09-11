package br.com.truco.model;

import java.util.ArrayList;
import java.util.Collections;

public class Baralho {
    ArrayList<Carta> baralho = new ArrayList<>();

    public Baralho() {
        String[] naipes = {"O", "E", "C", "P"};
        String[] valores = {"A", "2", "3", "Q", "J", "K", "7", "6", "5", "4"};
        for (String naipe : naipes) {
            for (String valor : valores) {
                baralho.add(new Carta(valor, naipe));
            }
        }
    }

    public void embaralhar() {
        Collections.shuffle(baralho);
    }

    public Carta distribuir() {
        if (!baralho.isEmpty()) {
            return baralho.remove(0);
        }
        return null;
    }
}