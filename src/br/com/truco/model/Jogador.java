package br.com.truco.model;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private final String nome;
    private List<Carta> mao;

    public Jogador(String nome) {
        this.nome = nome;
        this.mao = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public List<Carta> getMao() {
        // Retorna uma cópia para proteger a lista original de modificações externas
        return new ArrayList<>(mao);
    }

    public void receberCartas(List<Carta> cartas) {
        this.mao = new ArrayList<>(cartas);
    }

    public Carta jogarCarta(int indice) {
        if (indice >= 0 && indice < mao.size()) {
            return this.mao.remove(indice);
        }
        return null; // Retorna nulo se o índice for inválido
    }

    public void limparMao() {
        this.mao.clear();
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
