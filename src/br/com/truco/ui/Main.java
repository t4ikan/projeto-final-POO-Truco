package br.com.truco.ui;

import br.com.truco.engine.InterfaceUsuario;

public class Main {
    public static void main(String[] args) {
        InterfaceUsuario iu = new InterfaceUsuario();
        GerenciadorJogo gerenciador = new GerenciadorJogo(iu);

        int opcao = -1;
        while (opcao != 5) {
            iu.exibirMenuPrincipal();
            opcao = iu.lerOpcao();

            switch (opcao) {
                case 1:
                    gerenciador.cadastrarJogador();
                    break;
                case 2:
                    gerenciador.cadastrarDupla();
                    break;
                case 3:
                    gerenciador.iniciarPartida();
                    break;
                case 4:
                    gerenciador.mostrarEstatisticas();
                    break;
                case 5:
                    iu.mostrarMensagem("Obrigado por jogar!");
                    break;
                default:
                    if (opcao != -1) iu.mostrarMensagem("Opção inválida.");
            }
            if (opcao != 5) {
                iu.esperarEnter();
            }
        }
    }
}