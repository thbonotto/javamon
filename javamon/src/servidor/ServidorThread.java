/*
 * Copyright (C) 2013 Thiago Henque Bonotto da Silva & Gustavo Constante
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Classe responsável por tratar intermediações entre o cliente e o servidor
 * com acesso ao pai, para setar e obter informações necessárias ao cliente
 * @author Emerson Ribeiro de Mello
 * @author Thiago Henrique Bonotto da Silva
 * @author Gustavo Constante
 *
 */
package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import poo.Mensagem;

public class ServidorThread extends Thread {

    ServidorDaemon pai;
    private Socket conexao;
    private ArrayList<ObjectOutputStream> outrosClientes;
    int n;
    private int PlayerNumber;

    public ServidorThread(ServidorDaemon p, Socket c, ArrayList<ObjectOutputStream> saidaDosClientes) {
        this.pai = p;
        this.conexao = c;
        this.outrosClientes = saidaDosClientes;

    }

    @Override
    public void run() {
        ObjectOutputStream saida;
        ObjectInputStream entrada;

        try {
            //obtendo os fluxos de entrada e de saida
            saida = new ObjectOutputStream(conexao.getOutputStream());
            entrada = new ObjectInputStream(conexao.getInputStream());

            // adicionando o fluxo de saida deste cliente no ArrayList compartilhado
            // por todas as threads
            this.outrosClientes.add(saida);

            boolean conectado = true;

            // enviando o objeto Pessoa para o cliente
            Mensagem Msg = (Mensagem) entrada.readObject();
            String nomeCliente = Msg.corpo;
            if (outrosClientes.size() == 2) {
                saida.writeObject(new Mensagem(110, "bem vindo ao servidor \n Você é o segundo jogador."));
                saida.writeObject(new Mensagem(100, pai.getNomePlayer1()));
                pai.setNomePlayer2(Msg.corpo);
                ObjectOutputStream outraSaida = outrosClientes.get(0);
                outraSaida.writeObject(new Mensagem(100, pai.getNomePlayer2()));
                outraSaida.writeObject(new Mensagem(102, "Adversário " + pai.getNomePlayer2() + " conectou-se."));
                PlayerNumber = 2;

            } else {
                         saida.writeObject(new Mensagem(102, "bem vindo ao servidor \n Você é o primeiro jogador."));
                pai.setNomePlayer1(Msg.corpo);
                PlayerNumber = 1;

            }
            System.out.println("Cliente conectou no servidor...");

            while (conectado) {
                Mensagem MsgLinha = (Mensagem) entrada.readObject();
                String linha = MsgLinha.corpo;
                System.out.println(nomeCliente + "> \n Código: " + MsgLinha.codigo + "\nCorpo: " + linha);
                switch (MsgLinha.codigo) {
                    case 110:
                        for (ObjectOutputStream outraSaida : outrosClientes) {// percorrendo todos os fluxos de saida dos clientes conectados
                            if (saida != outraSaida) {
                                outraSaida.writeObject(new Mensagem(MsgLinha.codigo, linha));
                            }
                        }
                        break;
                    case 120:
                        saida.writeObject(new Mensagem(120, "Desconetado"));
                        outrosClientes.remove(saida); // removendo o fluxo de saida do ArrayList
                        conectado = false;
                        break;
                    case 200:
                        if (PlayerNumber == 1) {
                            pai.setPersonagens1(MsgLinha.corpo);
                        } else {
                            pai.setPersonagens2(MsgLinha.corpo);
                        }

                        break;
                   
                    default:
                        if (PlayerNumber == 1) {
                            pai.estrategia1 = MsgLinha.codigo;
                        } else {
                            pai.estrategia2 = MsgLinha.codigo;
                        }
                        if (pai.estrategia1 != 0 & pai.estrategia2 != 0) {
                            pai.calculaEstrategia();
                        }
                        break;

                }

            }

            saida.close();
            entrada.close();
            conexao.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

            // fechando os fluxos e a conexao
}
