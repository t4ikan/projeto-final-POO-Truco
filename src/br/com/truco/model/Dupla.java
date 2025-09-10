package br.com.truco.model;

public class Dupla {
    private final Jogador jogador1;
    private final Jogador jogador2;
    private int quedasJogadas = 0;
    private int quedasGanhas = 0;

    public Dupla(Jogador j1, Jogador j2) {
        this.jogador1 = j1;
        this.jogador2 = j2;
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }

    public int getQuedasGanhas() {
        return quedasGanhas;
    }

    public int getQuedasJogadas() {
        return quedasJogadas;
    }

    public void registrarVitoriaQueda() {
        this.quedasGanhas++;
    }

    public void registrarParticipacaoQueda() {
        this.quedasJogadas++;
    }

    public boolean contemJogador(Jogador jogador) {
        return jogador1.equals(jogador) || jogador2.equals(jogador);
    }

    @Override
    public String toString() {
        return "Dupla (" + jogador1.getNome() + " e " + jogador2.getNome() + ")";
    }
}
