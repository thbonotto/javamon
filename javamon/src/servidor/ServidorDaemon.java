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
package servidor;

//<editor-fold defaultstate="collapsed" desc="Imports">
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import poo.GUIMain;
import poo.Mensagem;
import poo.Personagens.Bulbasaur;
import poo.Personagens.Caterpie;
import poo.Personagens.Geodude;
import poo.Personagens.Goldeen;
import poo.Personagens.Hoothoot;
import poo.Personagens.Mankey;
import poo.Personagens.Personagem;
import poo.Personagens.Pikachu;
import poo.Personagens.Ponyta;
import poo.Personagens.Sandshrew;
import poo.Personagens.Shellder;
//</editor-fold>

/**
 * 2013-11-26
 *
 * Esta classe é responsável por criar um Thread de Servidor para cada cliente
 * que se conectar.
 *
 * @author Emerson Ribeiro de Mello
 * 
 * Adicionada a lógica do jogo com o tratamento das mensagens recebidas por cada cliente.
 * 
 * @author Thiago Henrique Bonotto da Silva
 * @author Gustavo Constante
 *
 */
public class ServidorDaemon extends Thread {

    private int c; //contador de clientes
    private int porta; // porta onde ficará ouvindo
    private ServerSocket servidor; // socket TCP
    // lista com os fluxos de saida de todos os clientes conectados. 
    // Isso é compartilhado por todos os objetos da classe ServidorThread
    private ArrayList<ObjectOutputStream> saidaDosParticipantes;

    private boolean sair;
    private String NomePlayer1;
    private String NomePlayer2;
    int estrategia1;
    int estrategia2;
    private Personagem PersonagemAtual1;
    private Personagem PersonagemAtual2;
    private Personagem[] Personagens1 = new Personagem[10];
    private Personagem[] Personagens2 = new Personagem[10];
    private Personagem[] Personagens1Selecionados = new Personagem[6];
    private Personagem[] Personagens2Selecionados = new Personagem[6];
    double a;
    private GUIMain parent;

    /**
     * Para criar um servidorDaemon é necessário informar a porta onde este
     * ficará ouvindo (esperando por conexões).
     * No momento da criação iniciamos os Personagens dos Jogadores.
     *
     * @param parent Gui principal, pai do ServidorDaemon
     * @param po porta
     */
    public ServidorDaemon(GUIMain parent, int po) {
        this.parent = parent;
        this.porta = po;
        this.saidaDosParticipantes = new ArrayList<ObjectOutputStream>();
        this.sair = false;
        Personagens1[0] = new Ponyta();
        Personagens1[1] = new Shellder();
        Personagens1[2] = new Bulbasaur();
        Personagens1[3] = new Goldeen();
        Personagens1[4] = new Geodude();
        Personagens1[5] = new Hoothoot();
        Personagens1[6] = new Caterpie();
        Personagens1[7] = new Sandshrew();
        Personagens1[8] = new Pikachu();
        Personagens1[9] = new Mankey();
        Personagens2[0] = new Ponyta();
        Personagens2[1] = new Shellder();
        Personagens2[2] = new Bulbasaur();
        Personagens2[3] = new Goldeen();
        Personagens2[4] = new Geodude();
        Personagens2[5] = new Hoothoot();
        Personagens2[6] = new Caterpie();
        Personagens2[7] = new Sandshrew();
        Personagens2[8] = new Pikachu();
        Personagens2[9] = new Mankey();

    }

    /**
     * Método que será executado quando alguém invocar o método start() desta
     * classe.
     */
    @Override
    public void run() {
        try {
            // Criando socket TCP para ouvir na porta
            this.servidor = new ServerSocket(this.porta, 10);

            // Ficará neste laço até que o usuário diga para não mais ouvir
            while (!sair) {

                // fica esperando um cliente conectar.
                Socket conexao = servidor.accept();
                // As linhas abaixo só serão executadas se um cliente se conectar.
                // Ou seja, a execução fica travada na linha acima até o cliente conectar

                // cliente conectou, crie um thread só para ele
                //Máximo dois clientes para evitar ataques.
                if (c < 2) {
                    System.out.println("O seguinte cliente conectou: " + conexao.getInetAddress().getHostAddress());
                    Thread t = new ServidorThread(this, conexao, this.saidaDosParticipantes);
                    t.start();
                    c++;
                } else {
                    conexao.close();
                    System.out.println("Conexão recusada para o seguinte cliente: " + conexao.getInetAddress().getHostAddress());
                }
            }
        } catch (SocketException e) {
            System.out.println("Servidor foi desligado");

        } catch (Exception e) {
            System.err.println("Erro: " + e.toString());
        }
    }

    /**
     * Para fazer o servidor parar de ouvir na porta. Finalizando o processo
     *
     */
    public void parar() {
        try {
            this.sair = true;

            this.servidor.close();
        } catch (Exception ex) {
            System.err.println("Erro no parar: " + ex.toString());
        }
    }

    /**
     *Obtem o nome do Jogador 1
     * @return
     */
    public String getNomePlayer1() {
        return this.NomePlayer1;
    }

    /**
     * Define o nome do Jogador 1
     * @param NomePlayer1
     */
    public void setNomePlayer1(String NomePlayer1) {
        this.NomePlayer1 = NomePlayer1;
    }

    /**
     * Obtem o nome do Jogador 2
     * @return
     */
    public String getNomePlayer2() {
        return this.NomePlayer2;
    }

    /**
     * Define o nome do Jogador 2
     * @param NomePlayer2
     */
    public void setNomePlayer2(String NomePlayer2) {
        this.NomePlayer2 = NomePlayer2;
    }

    /**
     * calculaEstragegia é chamada pelo segundo cliente que enviou a estratagia
     * para controlar isso temos duas flags, estrategia1 e estrategia 2 nesse Pai
     * Os servidores Threads criados a partir desse, tem acesso e quando recebem a estrategia de seus clientes a setam aqui.
     * A estrategia escolhida por cada cliente é tratada no case, respeitando as especificações na wiki
     * Tratando caso algum Pokemon morra, esperando sua troca, utilização de itens, e buff e debuffs.
     * @throws IOException
     */
    public void calculaEstrategia() throws IOException {
        int hp1;
        int hp2;
        int atck1 = 0;
        int atck2 = 0;
        double mult1;
        double mult2;
        String saida;

        if (PersonagemAtual1.getAttackSpeed() > PersonagemAtual2.getAttackSpeed()) {
            saida = "(0):";
        } else if (PersonagemAtual1.getAttackSpeed() < PersonagemAtual2.getAttackSpeed()) {
            saida = "(1):";
        } else {
            Random r = new Random();
            saida = "(" + r.nextInt(1) + "):";
        }
        mult1 = advantageChecker(PersonagemAtual1, PersonagemAtual2);
        mult2 = advantageChecker(PersonagemAtual2, PersonagemAtual1);
        switch (this.estrategia2) {
            case 301:
                a = Math.random();
                if (a < 0.4) {
                    atck1 = (int) ((30 + PersonagemAtual2.getAtaque() - PersonagemAtual1.getDefesa()) * mult1);
                }
                break;
            case 311:
                a = Math.random();
                if (a < 0.4) {
                    atck1 = (int) ((30 + PersonagemAtual2.getAtaque() - PersonagemAtual1.getDefesa()) * mult1);
                }
                break;
            case 312:
                if (PersonagemAtual1.getAttackSpeed() > 0.2 * personagem1ById(PersonagemAtual1.getId()).getAttackSpeed()) {
                    PersonagemAtual1.setAttackSpeed((int) 0.8 * PersonagemAtual1.getAttackSpeed());
                }
                break;
            case 313:
                a = Math.random();
                if (a < 0.8) {
                    atck1 = (int) ((10 + PersonagemAtual2.getAtaque() - PersonagemAtual1.getDefesa()) * mult1);
                }
                break;
            case 320:
                if (PersonagemAtual2.isItem1()) {
                    PersonagemAtual2.setItem1(false);
                    PersonagemAtual2.setHp(100);
                }
                break;
            case 321:
                if (PersonagemAtual2.isItem2()) {
                    PersonagemAtual2.setItem2(false);
                    PersonagemAtual2.setAttackSpeed(personagem1ById(PersonagemAtual1.getId()).getAttackSpeed());
                }
                break;
            case 330:
                setPersonagemAtual2(0);
                break;
            case 331:
                setPersonagemAtual2(1);
                break;
            case 332:
                setPersonagemAtual2(2);
                break;
            case 333:
                setPersonagemAtual2(3);
                break;
            case 334:
                setPersonagemAtual2(4);
                break;
            case 335:
                setPersonagemAtual2(5);
                break;
            case 336:
                setPersonagemAtual2(6);
                break;
            case 337:
                setPersonagemAtual2(7);
                break;
            case 338:
                setPersonagemAtual2(8);
                break;
            case 339:
                setPersonagemAtual2(9);
                break;

        }
        switch (this.estrategia1) {
            case 301:
                a = Math.random();
                if (a < 0.4) {
                    atck2 = (int) ((30 + PersonagemAtual1.getAtaque() - PersonagemAtual2.getDefesa()) * mult2);
                }
                break;
            case 311:
                a = Math.random();
                if (a < 0.4) {
                    atck2 = (int) ((30 + PersonagemAtual1.getAtaque() - PersonagemAtual2.getDefesa()) * mult2);
                }
                break;
            case 312:
                if (PersonagemAtual2.getAttackSpeed() > 0.2 * personagem1ById(PersonagemAtual2.getId()).getAttackSpeed()) {
                    PersonagemAtual2.setAttackSpeed((int) 0.8 * PersonagemAtual2.getAttackSpeed());
                }
                break;
            case 313:
                a = Math.random();
                if (a < 0.8) {
                    atck2 = (int) ((10 + PersonagemAtual1.getAtaque() - PersonagemAtual2.getDefesa()) * mult2);
                }
                break;
            case 320:
                if (PersonagemAtual1.isItem1()) {
                    PersonagemAtual1.setItem1(false);
                    PersonagemAtual1.setHp(100);
                }
                break;
            case 321:
                if (PersonagemAtual1.isItem2()) {
                    PersonagemAtual1.setItem2(false);
                    PersonagemAtual1.setAttackSpeed(personagem1ById(PersonagemAtual1.getId()).getAttackSpeed());
                }
                break;
            case 330:
                setPersonagemAtual1(0);
                break;
            case 331:
                setPersonagemAtual1(1);
                break;
            case 332:
                setPersonagemAtual1(2);
                break;
            case 333:
                setPersonagemAtual1(3);
                break;
            case 334:
                setPersonagemAtual1(4);
                break;
            case 335:
                setPersonagemAtual1(5);
                break;
            case 336:
                setPersonagemAtual1(6);
                break;
            case 337:
                setPersonagemAtual1(7);
                break;
            case 338:
                setPersonagemAtual1(8);
                break;
            case 339:
                setPersonagemAtual1(9);
                break;

        }
        this.estrategia2 = 0;
        this.estrategia1 = 0;
        if ((PersonagemAtual1.getHp() - atck1) < 0) {
            atck2 = 0;
        }

        if ((PersonagemAtual2.getHp() - atck2) < 0) {

            atck1 = 0;
        }
        if (atck1 > 0) {
            hp1 = (PersonagemAtual1.getHp() - atck1);

            if (hp1 > 0) {
                PersonagemAtual1.setHp(hp1);

            } else {
                PersonagemAtual1.setHp(0);
                this.estrategia2 = 1;

            }
        }
        if (atck2 > 0) {
            hp2 = (PersonagemAtual2.getHp() - atck2);

            if (hp2 > 0) {

                PersonagemAtual2.setHp(hp2);

            } else {

                PersonagemAtual2.setHp(0);

                this.estrategia1 = 1;
            }
        }

        saida = (saida + PersonagemAtual1.getId() + "=" + PersonagemAtual1.getHp() + ":" + PersonagemAtual2.getId() + "=" + PersonagemAtual2.getHp());

        // percorrendo todos os fluxos de saida dos clientes conectados
        saidaDosParticipantes.get(0).writeObject(new Mensagem(500, saida));
        saidaDosParticipantes.get(1).writeObject(new Mensagem(500, saida));

    }

    /**
     * Indica o personagem e quanto de vida ele esta no momento que vai para a
     * arena, método do jogodar 1.
     *
     * @param i
     */
    public void setPersonagemAtual1(int i) {
        Personagem p = personagem1SelecionadoById(i);
        if (p.getHp() > 0) {
            this.PersonagemAtual1 = p;
        }

    }

    /**
     * Indica o personagem e quanto de vida ele esta no momento que vai para a
     * arena, método do jogodar 2.
     *
     * @param i
     */
    public void setPersonagemAtual2(int i) {
        Personagem p = personagem2SelecionadoById(i);

        if (p.getHp() > 0) {
            this.PersonagemAtual2 = p;

        }
    }

    /**
     * Retorna o personagem atual do jogador 1
     *
     * @return
     */
    public Personagem getPersonagemAtual1() {
        return PersonagemAtual1;
    }

    /**
     * Retorna o personagem atual do jogador 2
     *
     * @return
     */
    public Personagem getPersonagemAtual2() {
        return PersonagemAtual2;
    }

    /**
     * Retorna o personagem que vai possuir vantagem na batalha e quanto de
     * vantagem ele possui.
     *
     * @return
     */
    private double advantageChecker(Personagem P1, Personagem P2) {
        String tipo1;
        String tipo2;
        tipo1 = P1.getTipo();
        tipo2 = P2.getTipo();
        if (tipo1.equals("Fogo")) {
            if (tipo2.equals("Gelo") || tipo2.equals("Inseto")) {
                return 1.5;
            }
            if (tipo2.equals("Água") || tipo2.equals("Terrestre")) {
                return 0.5;
            }
        }
        if (tipo1.equals("Gelo")) {
            if (tipo2.equals("Planta") || tipo2.equals("Voador")) {
                return 1.5;
            }
            if (tipo2.equals("Fogo") || tipo2.equals("Pedra")) {
                return 0.5;
            }
        }
        if (tipo1.equals("Planta")) {
            if (tipo2.equals("Água") || tipo2.equals("Terrestre")) {
                return 1.5;
            }
            if (tipo2.equals("Gelo") || tipo2.equals("Inseto")) {
                return 0.5;
            }

        }
        if (tipo1.equals("Água")) {
            if (tipo2.equals("Fogo") || tipo2.equals("Pedra")) {
                return 1.5;
            }
            if (tipo2.equals("Planta") || tipo2.equals("Elétrico")) {
                return 0.5;
            }

        }
        if (tipo1.equals("Pedra")) {
            if (tipo2.equals("Gelo") || tipo2.equals("Elétrico")) {
                return 1.5;
            }
            if (tipo2.equals("Água") || tipo2.equals("Lutador")) {
                return 0.5;
            }
        }
        if (tipo1.equals("Voador")) {
            if (tipo2.equals("Planta") || tipo2.equals("Lutador")) {
                return 1.5;
            }
            if (tipo2.equals("Gelo") || tipo2.equals("Elétrico")) {
                return 0.5;
            }
        }
        if (tipo1.equals("Inseto")) {
            if (tipo2.equals("Terrestre") || tipo2.equals("Planta")) {
                return 1.5;
            }
            if (tipo2.equals("Fogo") || tipo2.equals("Lutador")) {
                return 0.5;
            }
        }
        if (tipo1.equals("Terrestre")) {
            if (tipo2.equals("Fogo") || tipo2.equals("Elétrico")) {
                return 1.5;
            }
            if (tipo2.equals("Planta") || tipo2.equals("Inseto")) {
                return 0.5;
            }
        }
        if (tipo1.equals("Elétrico")) {
            if (tipo2.equals("Água") || tipo2.equals("Voador")) {
                return 1.5;
            }
            if (tipo2.equals("Terrestre") || tipo2.equals("Pedra")) {
                return 0.5;
            }
        }
        if (tipo1.equals("Lutador")) {
            if (tipo2.equals("Pedra") || tipo2.equals("Inseto")) {
                return 1.5;
            }
            if (tipo2.equals("Voador")) {
                return 0.5;
            }
        }
        return 1;
    }

    /**
     * Recebe a lista de personagens do jogador 1 e seta o personagem atual e
     * envia uma mensagem de aguardo para o jogador 2
     *
     * @param s
     * @throws IOException
     */
    public void setPersonagens1(String s) throws IOException {
        int n;
        int i = 0;
        String[] cc = s.split(":");
        do {
            n = Integer.parseInt(cc[i]);
            this.Personagens1Selecionados[i] = personagem1ById(n);
            i++;
        } while (i < 6);
        this.PersonagemAtual1 = this.Personagens1Selecionados[0];
        if (PersonagemAtual2 != null) {
            for (ObjectOutputStream outraSaida : saidaDosParticipantes) {// percorrendo todos os fluxos de saida dos clientes conectados
                outraSaida.writeObject(new Mensagem(600, Personagens1Selecionados[0].getId() + "=" + Personagens1Selecionados[0].getHp() + ":" + Personagens1Selecionados[1].getId() + "=" + Personagens1Selecionados[1].getHp() + ":" + Personagens1Selecionados[2].getId() + "=" + Personagens1Selecionados[2].getHp() + ":" + Personagens1Selecionados[3].getId() + "=" + Personagens1Selecionados[3].getHp() + ":" + Personagens1Selecionados[4].getId() + "=" + Personagens1Selecionados[4].getHp() + ":" + Personagens1Selecionados[5].getId() + "=" + Personagens1Selecionados[5].getHp() + ":" + PersonagemAtual2.getId() + "=" + PersonagemAtual2.getHp()));
            }
        } else {
            saidaDosParticipantes.get(0).writeObject(new Mensagem(102, "Aguardando seleção de jogardor 2."));
        }

    }

    /**
     * Recebe a lista de personagens do jogador 2 e seta o personagem atual e
     * envia uma mensagem de aguardo para o jogador 1
     *
     * @param s
     * @throws IOException
     */
    public void setPersonagens2(String s) throws IOException {
        int n;
        int i = 0;
        String[] cc = s.split(":");
        do {
            n = Integer.parseInt(cc[i]);
            this.Personagens2Selecionados[i] = personagem2ById(n);
            i++;
        } while (i < 6);
        this.PersonagemAtual2 = this.Personagens2Selecionados[0];
        if (PersonagemAtual1 != null) {
            for (ObjectOutputStream outraSaida : saidaDosParticipantes) {// percorrendo todos os fluxos de saida dos clientes conectados
                outraSaida.writeObject(new Mensagem(600, Personagens2Selecionados[0].getId() + "=" + Personagens2Selecionados[0].getHp() + ":" + Personagens2Selecionados[1].getId() + "=" + Personagens2Selecionados[1].getHp() + ":" + Personagens2Selecionados[2].getId() + "=" + Personagens2Selecionados[2].getHp() + ":" + Personagens2Selecionados[3].getId() + "=" + Personagens2Selecionados[3].getHp() + ":" + Personagens2Selecionados[4].getId() + "=" + Personagens2Selecionados[4].getHp() + ":" + Personagens2Selecionados[5].getId() + "=" + Personagens2Selecionados[5].getHp() + ":" + PersonagemAtual1.getId() + "=" + PersonagemAtual1.getHp()));
            }
        } else {
            saidaDosParticipantes.get(1).writeObject(new Mensagem(102, "Aguardando seleção de jogardor 1."));
        }

    }

    /**
     * Retorna o personagem do Jogador 1 recebendo o id do personagem como parametro
     * @param id
     * @return
     */
    public Personagem personagem1ById(int id) {
        int n;
        if (Personagens1[0].getId() == id) {
            n = 0;
        } else {
            for (n = 0; Personagens1[n].getId() != id; n++);

        }
        return Personagens1[n];

    }

    public Personagem personagem1SelecionadoById(int id) {
        int n;
        if (Personagens1Selecionados[0].getId() == id) {
            n = 0;
        } else {
            for (n = 0; Personagens1Selecionados[n].getId() != id; n++);

        }
        return Personagens1Selecionados[n];

    }

    public Personagem personagem2ById(int id) {
        int n;
        if (Personagens2[0].getId() == id) {
            n = 0;
        } else {
            for (n = 0; Personagens2[n].getId() != id; n++);

        }
        return Personagens2[n];

    }

    public Personagem personagem2SelecionadoById(int id) {
        int n;
        if (Personagens2Selecionados[0].getId() == id) {
            n = 0;
        } else {
            for (n = 0; Personagens2Selecionados[n].getId() != id; n++);

        }
        return Personagens2Selecionados[n];

    }
}
