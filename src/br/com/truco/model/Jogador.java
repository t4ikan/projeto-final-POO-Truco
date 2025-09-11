package br.com.truco.model;

import br.com.truco.model.Dupla;
import br.com.truco.engine.InterfaceUsuario;
import br.com.truco.model.Jogador;
import br.com.truco.model.Mao;

import java.util.ArrayList;

public class Jogador {
    // 1. Atributos agora são privados para proteger os dados
    private String nome;
    private ArrayList<Carta> mao = new ArrayList<>();

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public ArrayList<Carta> getMao() {
        return this.mao;
    }


    public void setMao(ArrayList<Carta> mao) {
        this.mao = mao;
    }

    public Carta jogarCarta(int indice) {
        if (indice >= 0 && indice < mao.size()){
            return mao.remove(indice);
        }
        return null;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}