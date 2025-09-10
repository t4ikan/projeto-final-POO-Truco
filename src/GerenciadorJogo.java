import br.com.truco.engine.InterfaceUsuario;
import br.com.truco.engine.Partida;
import br.com.truco.model.Dupla;
import br.com.truco.model.Jogador;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorJogo {
    private final List<Jogador> jogadores;
    private final List<Dupla> duplas;
    private final InterfaceUsuario iu;

    public GerenciadorJogo(InterfaceUsuario iu) {
        this.jogadores = new ArrayList<>();
        this.duplas = new ArrayList<>();
        this.iu = iu;
        // Adiciona jogadores padrão para facilitar testes
        jogadores.add(new Jogador("J1"));
        jogadores.add(new Jogador("J2"));
        jogadores.add(new Jogador("J3"));
        jogadores.add(new Jogador("J4"));
    }

    public void cadastrarJogador() {
        String nome = iu.lerNome("Digite o nome do novo jogador: ");
        jogadores.add(new Jogador(nome));
        iu.mostrarMensagem("Jogador " + nome + " cadastrado com sucesso!");
    }

    public void cadastrarDupla() {
        if (jogadores.size() < 2) {
            iu.mostrarMensagem("É preciso ter pelo menos 2 jogadores cadastrados.");
            return;
        }
        iu.listarJogadores(jogadores);

        iu.mostrarMensagem("Escolha o primeiro jogador (pelo número):");
        int idx1 = iu.lerOpcao() - 1;
        iu.mostrarMensagem("Escolha o segundo jogador (pelo número):");
        int idx2 = iu.lerOpcao() - 1;

        if (idx1 >= 0 && idx1 < jogadores.size() && idx2 >= 0 && idx2 < jogadores.size() && idx1 != idx2) {
            Jogador j1 = jogadores.get(idx1);
            Jogador j2 = jogadores.get(idx2);
            duplas.add(new Dupla(j1, j2));
            iu.mostrarMensagem("Dupla (" + j1.getNome() + " e " + j2.getNome() + ") cadastrada!");
        } else {
            iu.mostrarMensagem("Seleção inválida.");
        }
    }

    public void iniciarPartida() {
        if (duplas.size() < 2) {
            iu.mostrarMensagem("É preciso ter pelo menos 2 duplas cadastradas para jogar.");
            return;
        }
        iu.listarDuplas(duplas);

        iu.mostrarMensagem("Escolha a primeira dupla (pelo número):");
        int idx1 = iu.lerOpcao() - 1;
        iu.mostrarMensagem("Escolha a segunda dupla (pelo número):");
        int idx2 = iu.lerOpcao() - 1;

        if (idx1 >= 0 && idx1 < duplas.size() && idx2 >= 0 && idx2 < duplas.size() && idx1 != idx2) {
            Dupla d1 = duplas.get(idx1);
            Dupla d2 = duplas.get(idx2);
            Partida partida = new Partida(d1, d2);
            partida.jogarQueda();
        } else {
            iu.mostrarMensagem("Seleção inválida.");
        }
    }

    public void mostrarEstatisticas() {
        iu.mostrarEstatisticas(duplas);
    }
}
