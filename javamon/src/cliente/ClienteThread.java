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

package cliente;

//<editor-fold defaultstate="collapsed" desc="Import's">
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import poo.Mensagem;
import poo.GUIMain;
//</editor-fold>

/**
 * Classe ClienteThread que gerencia tudo que o cliente recebe e envia de mensagens de comunicação com o servidor
 * 
 * @author Thiago Henrique Bonotto da Silva
 * @author Gustavo Constante
 * @author Emerson Ribeiro de Mello
 */

public class ClienteThread extends Thread {
    
//Atributos
//<editor-fold defaultstate="collapsed" desc="Atributos da Classe ClienteThread">
    
    private GUIMain pai;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private String ip;
    private int porta;
    private String nome;
    boolean conectado = true;
//</editor-fold>

//Métodos
//<editor-fold defaultstate="collapsed" desc="Método construtor do clientThread">

    /**
     * Método contrutor do ClienteThread
     * 
     * @param pai
     * @param n
     * @param IP
     * @param Porta
     */
        public ClienteThread(GUIMain pai, String n, String IP, int Porta) {
        this.pai = pai;
        this.nome = n;
        this.ip = IP;
        this.porta = Porta;
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Método de saida das Mensagens">

    /**
     * Método de Saída do ClienteThread
     * Ele que envia todas as mensagens pela rede.
     * @param msg
     */
        public void enviarMensagem(Mensagem msg) {
        try {
            saida.writeObject(msg);
        } catch (IOException ex) {
            System.err.println("Erro ao enviar msg: " + ex.toString());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Método de comunicação cliente <-> servidor">
    @Override
    public void run() {
        Socket conexao;
        
        try {
            conexao = new Socket(ip, porta);
            //obtendo os fluxos de entrada e de saida
            saida = new ObjectOutputStream(conexao.getOutputStream());
            entrada = new ObjectInputStream(conexao.getInputStream());
            conectado = true;
            enviarMensagem(new Mensagem(100, nome));
            while (conectado) {
                Mensagem Msg = (Mensagem) entrada.readObject();
                String mensagem = Msg.corpo;
                switch (Msg.codigo) {
                    case 102:
                        if(pai.getNumeroJogador()==0){
                            pai.setNumeroJogador(1);
                        }
                        pai.chatMenu.adicionarLinha(Msg.corpo);
                        break;
                    case 500:
                        pai.atualizarDados(new Mensagem(Msg.codigo, Msg.corpo));
                        break;
                    case 600:
                        pai.inicioTurno(new Mensagem(Msg.codigo, Msg.corpo));
                        break;
                    case 100:
                        pai.pickMenu.enableAll(true);
                        pai.setNomeAdversario(Msg.corpo);
                        break;
                    case 110:
                        if (pai.getNomeAdversario() == null) {
                            pai.chatMenu.adicionarLinha(mensagem);
                        } else {
                            pai.chatMenu.adicionarLinha("" + pai.getNomeAdversario() + ": " + mensagem);
                        }
                        break;
                    case 120:
                        conectado = false;
                        pai.chatMenu.adicionarLinha("Jogador desconectou-se");
                        break;
                        
                }
                
            }
            
            // fechando os fluxos e a conexao
            saida.close();
            entrada.close();
            conexao.close();
        } catch (Exception ex) {
            System.err.println("Erro sockets: " + ex.toString());
        }
        
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Método que envia para saida a mensagem de encerrar conexão">

    /**
     * Envia uma mensagem para o servidor para encerrar a conexão
     */
        public void closeConnection() {
        enviarMensagem(new Mensagem(120, "Fim"));
        conectado = false;
        
    }
//</editor-fold>
}
