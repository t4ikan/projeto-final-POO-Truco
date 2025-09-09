import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baralho {
    private List<Carta> cartas;

    public Baralho() {
        this.cartas = new ArrayList<>();
        String[] valores = {"4", "5", "6", "7", "Q", "J", "K", "A", "2", "3"};
        Naipe[] naipes = Naipe.values(); // Pega todos os naipes do Enum

        for (Naipe naipe : naipes) {
            for (String valor : valores) {
                this.cartas.add(new Carta(valor, naipe));
            }
        }
        embaralhar();
    }

    // Função que usa a ferramenta "shuffle" para embaralhar a list
    private void embaralhar() {
        Collections.shuffle(this.cartas);
    }

    public Carta retirarCarta() {
        if (this.cartas.isEmpty()) {
            return null;
        }
        // Remove a carta do topo e a entrega
        return this.cartas.removeFirst();
    }
}