package br.com.truco.model;

public class Carta {
    String valor;
    String naipe;
    int peso;

    public Carta(String valor, String naipe) {
        this.valor = valor;
        this.naipe = naipe;

        if (valor.equals("4") && naipe.equals("P")) this.peso = 10;
        else if (valor.equals("7") && naipe.equals("C")) this.peso = 9;
        else if (valor.equals("A") && naipe.equals("E")) this.peso = 8;
        else if (valor.equals("7") && naipe.equals("O")) this.peso = 7;
        else if (valor.equals("3")) this.peso = 6;
        else if (valor.equals("2")) this.peso = 5;
        else if (valor.equals("A")) this.peso = 4;
        else if (valor.equals("K")) this.peso = 3;
        else if (valor.equals("J")) this.peso = 2;
        else if (valor.equals("Q")) this.peso = 1;
        else this.peso = 0;
    }

    @Override
    public String toString() {
        String valorPorExtenso;
        String naipePorExtenso;

        switch (this.valor) {
            case "A":
                valorPorExtenso = "ÁS";
                break;
            case "K":
                valorPorExtenso = "REI";
                break;
            case "J":
                valorPorExtenso = "VALETE";
                break;
            case "Q":
                valorPorExtenso = "DAMA";
                break;
            default:
                valorPorExtenso = this.valor;
                break;
        }

        switch (this.naipe) {
            case "P":
                naipePorExtenso = "PAUS";
                break;
            case "C":
                naipePorExtenso = "COPAS";
                break;
            case "E":
                naipePorExtenso = "ESPADAS";
                break;
            case "O":
                naipePorExtenso = "OUROS";
                break;
            default:
                naipePorExtenso = "";
                break;
        }

        return valorPorExtenso + " de " + naipePorExtenso;
    }
}