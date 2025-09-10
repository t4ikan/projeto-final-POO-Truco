package br.com.truco.model;

public class Carta {
    private final String valor;
    private final Naipe naipe;
    private final int forca;

    public Carta(String valor, Naipe naipe) {
        this.valor = valor;
        this.naipe = naipe;
        this.forca = this.calcularForca();
    }

    public int getForca() {
        return forca;
    }

    private int calcularForca() {
        // Manilhas
        if (this.naipe == Naipe.PAUS && this.valor.equals("4")) return 14; // Zape
        if (this.naipe == Naipe.COPAS && this.valor.equals("7")) return 13; // 7 de Copas
        if (this.naipe == Naipe.ESPADAS && this.valor.equals("A")) return 12; // Espadilha
        if (this.naipe == Naipe.OUROS && this.valor.equals("7")) return 11; // 7 de Ouros

        // Cartas Comuns
        return switch (this.valor) {
            case "3" -> 10;
            case "2" -> 9;
            case "A" -> 8;
            case "K" -> 7;
            case "J" -> 6;
            case "Q" -> 5;
            case "7" -> 4;
            case "6" -> 3;
            case "5" -> 2;
            case "4" -> 1;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return valor + " de " + naipe;
    }
}