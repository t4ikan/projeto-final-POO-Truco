public class Carta {
    private final String valor;
    private final Naipe naipe;

    public Carta(String valor, Naipe naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    // Função que "traduz" o valor da carta para um número de força
    public int getValorDeForca() {
        return switch (this.valor) {
            case "4" -> 1;
            case "5" -> 2;
            case "6" -> 3;
            case "7" -> 4;
            case "Q" -> 5;
            case "J" -> 6;
            case "K" -> 7;
            case "A" -> 8;
            case "2" -> 9;
            case "3" -> 10;
            default -> 0;
        };
    }

    //Função para exibir a carta de forma legível (ex: "A de OUROS")
    @Override
    public String toString() {
        return valor + " de " + naipe;
    }
}