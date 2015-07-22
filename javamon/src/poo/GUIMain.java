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
package poo;

//<editor-fold defaultstate="collapsed" desc="Import's">
import cliente.ClienteThread;
import javax.swing.JPanel;
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
import servidor.ServidorDaemon;
//</editor-fold>

/**
 * Classe principal responsável por gerenciar interface grafica, iniciar cliente
 * e/ou servidor
 *
 * @author Thiago Henque Bonotto da Silva
 * @author Gustavo Constante
 *
 */
public class GUIMain extends javax.swing.JFrame {

    String pickedPersonagens = "";
    boolean recebeuPick = false;
    private ServidorDaemon hospedeiro;
    public String nickName;
    public ChatMenu chatMenu;
    public PickMenuDown pickMenuDown;
    public PickMenu pickMenu;
    Personagem[] personagensSelecionados = new Personagem[6];
    Personagem personagemAtual;
    Personagem personagemAdversarioAtual;
    BattleMenu battleMenu;
    BattleMenuDown battleMenuDown;
    ClienteThread ct;
    FirstMenuEsq fme = new FirstMenuEsq();
    FistMenuDown fmd = new FistMenuDown(this);
    Personagem[] personagens = new Personagem[10];
    Personagem[] personagensAdversario = new Personagem[10];
    int numeroJogador = 0;
    int PersonagensRestantes = 6;
    int PersonagensRestantesAdversario = 6;
    public String nomeAdversario;

     /**
     * Método para alterar imagem de uma tela GUIMain
     *
     * @param JPSource Recebe o novo JPanel
     * @param JPDest Indica qual o JPanel sera atualizado pelo novo
     */
//<editor-fold defaultstate="collapsed" desc="Método para alterar tela">
    public void alteraPanel(JPanel JPSource, JPanel JPDest) {
        JPDest.removeAll();
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(JPDest);
        JPDest.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JPSource, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JPSource, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Construtor">
    /**
     * Método construtor: define os primeiros paineis a serem exibidos na interface gráfica
     * e inicializa os personagens disponiveis para o jogador e adversario.
     */
    public GUIMain() {
        
        initComponents();
        this.setLocationRelativeTo(null);
        alteraPanel(this.fme, jPMenuEsq);
        alteraPanel(this.fmd, jPMenuDown);
        personagens[0] = new Ponyta();
        personagens[1] = new Shellder();
        personagens[2] = new Bulbasaur();
        personagens[3] = new Goldeen();
        personagens[4] = new Geodude();
        personagens[5] = new Hoothoot();
        personagens[6] = new Caterpie();
        personagens[7] = new Sandshrew();
        personagens[8] = new Pikachu();
        personagens[9] = new Mankey();
        personagensAdversario[0] = new Ponyta();
        personagensAdversario[1] = new Shellder();
        personagensAdversario[2] = new Bulbasaur();
        personagensAdversario[3] = new Goldeen();
        personagensAdversario[4] = new Geodude();
        personagensAdversario[5] = new Hoothoot();
        personagensAdversario[6] = new Caterpie();
        personagensAdversario[7] = new Sandshrew();
        personagensAdversario[8] = new Pikachu();
        personagensAdversario[9] = new Mankey();
        
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Altera paineis de escolha de Personagens">
    /**
     * Método que carrega os paineis de escolha dos personagens
     */
    public void carregaPickMenu() {
        this.pickMenu = new PickMenu(this);
        pickMenu.enableAll(false);
        alteraPanel(pickMenu, jPMenuEsq);
        this.pickMenuDown = new PickMenuDown(this);
        alteraPanel(pickMenuDown, jPMenuDown);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Método ao se conectar no">
    /**
     * Método que ao conectar ao servidor pega o nickname na label, cria o chat,
     * cria e inicia um clienteThread para realizar a comunicação com o
     * servidor e chama os métodos para atualizar as telas.
     */
    public void conectouServidor() {
        this.nickName = fme.getNickName();
        this.chatMenu = new ChatMenu(this.nickName);
        this.ct = new ClienteThread(this, this.nickName, fmd.getClienteIp(), fmd.getClientePorta());
        ct.start();
        chatMenu.setCT(ct);
        alteraPanel(chatMenu, jPMenuDir);
        carregaPickMenu();
        pickMenu.enableAll(true);
    }
//</editor-fold>

    /**
     * Método responsavel por criar e iniciar o servidor
     * 
     * @throws InterruptedException
     */
    public void criarServidor() throws InterruptedException {
        this.nickName = fme.getNickName();
        this.hospedeiro = new ServidorDaemon(this, fmd.getServidorPorta());
        this.hospedeiro.start();
        this.chatMenu = new ChatMenu(this.nickName);
        this.ct = new ClienteThread(this, this.nickName, "127.0.0.1", fmd.getServidorPorta());
        ct.start();
        chatMenu.setCT(ct);
        alteraPanel(chatMenu, jPMenuDir);
        carregaPickMenu();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMenuEsq = new javax.swing.JPanel();
        jPMenuDir = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTAMensagens = new javax.swing.JTextArea();
        jTFChat = new javax.swing.JTextField();
        jBEnviarMensagem = new javax.swing.JButton();
        jPMenuDown = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(650, 420));
        setMinimumSize(new java.awt.Dimension(650, 420));
        setPreferredSize(new java.awt.Dimension(650, 420));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPMenuEsq.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPMenuEsqLayout = new javax.swing.GroupLayout(jPMenuEsq);
        jPMenuEsq.setLayout(jPMenuEsqLayout);
        jPMenuEsqLayout.setHorizontalGroup(
            jPMenuEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 454, Short.MAX_VALUE)
        );
        jPMenuEsqLayout.setVerticalGroup(
            jPMenuEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 244, Short.MAX_VALUE)
        );

        getContentPane().add(jPMenuEsq, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 250));

        jPMenuDir.setBorder(javax.swing.BorderFactory.createTitledBorder("Chat"));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setEnabled(false);

        jTAMensagens.setEditable(false);
        jTAMensagens.setColumns(20);
        jTAMensagens.setRows(5);
        jScrollPane1.setViewportView(jTAMensagens);

        jTFChat.setEnabled(false);

        jBEnviarMensagem.setText("Enviar");
        jBEnviarMensagem.setEnabled(false);
        jBEnviarMensagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEnviarMensagemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPMenuDirLayout = new javax.swing.GroupLayout(jPMenuDir);
        jPMenuDir.setLayout(jPMenuDirLayout);
        jPMenuDirLayout.setHorizontalGroup(
            jPMenuDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPMenuDirLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jBEnviarMensagem)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPMenuDirLayout.createSequentialGroup()
                .addGroup(jPMenuDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jTFChat, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPMenuDirLayout.setVerticalGroup(
            jPMenuDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPMenuDirLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBEnviarMensagem))
        );

        getContentPane().add(jPMenuDir, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 0, 180, 386));

        jPMenuDown.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPMenuDownLayout = new javax.swing.GroupLayout(jPMenuDown);
        jPMenuDown.setLayout(jPMenuDownLayout);
        jPMenuDownLayout.setHorizontalGroup(
            jPMenuDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 454, Short.MAX_VALUE)
        );
        jPMenuDownLayout.setVerticalGroup(
            jPMenuDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 124, Short.MAX_VALUE)
        );

        getContentPane().add(jPMenuDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 256, 460, 130));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBEnviarMensagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEnviarMensagemActionPerformed
    }//GEN-LAST:event_jBEnviarMensagemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        ct.closeConnection();
    }//GEN-LAST:event_formWindowClosed

    /**
     * Método Main do programa inicia e chama o método GUIMain() que inicia a
     * tela princial
     *
     * @param args the command line arguments
     */
//<editor-fold defaultstate="collapsed" desc="Método Main">
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                new GUIMain().setVisible(true);

            }
        });
    }
//</editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton jBEnviarMensagem;
    public javax.swing.JPanel jPMenuDir;
    public javax.swing.JPanel jPMenuDown;
    public javax.swing.JPanel jPMenuEsq;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTAMensagens;
    private static javax.swing.JTextField jTFChat;
    // End of variables declaration//GEN-END:variables

    public void adicionaPokemon(Personagem p) {
        pickMenuDown.addMenuPokemon(p);
        if ("".equals(pickedPersonagens)) {
            pickedPersonagens = ("" + p.getId());
            setPersonagemAtual(p);
        } else {
            pickedPersonagens = (pickedPersonagens + ":" + p.getId());
        }
        if (personagensSelecionados[0] == null) {
            personagensSelecionados[0] = p;
        } else if (personagensSelecionados[1] == null) {
            personagensSelecionados[1] = p;
        } else if (personagensSelecionados[2] == null) {
            personagensSelecionados[2] = p;
        } else if (personagensSelecionados[3] == null) {
            personagensSelecionados[3] = p;
        } else if (personagensSelecionados[4] == null) {
            personagensSelecionados[4] = p;
        } else if (personagensSelecionados[5] == null) {
            personagensSelecionados[5] = p;
        }

    }

//<editor-fold defaultstate="collapsed" desc="Enviar Personagens Selecionados">
    public void enviaPick() {

        ct.enviarMensagem(new Mensagem(200, pickedPersonagens));

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GET e SET Nomes dos jogadores">
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNomeAdversario() {
        return this.nomeAdversario;
    }

    public void setNomeAdversario(String nomeAdversario) {
        this.nomeAdversario = nomeAdversario;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Limpa lista de Personagens Escolhidos">
    public void resetaPick() {
        pickedPersonagens = "";
        pickMenu.resetaPick();
        for (int n = 0; n < personagensSelecionados.length; n++) {
            personagensSelecionados[n] = null;
        }
        personagemAtual = null;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Habilita a tela para escolha de Personagens">
    public void enablePickMenu() {
        pickMenu.setEnabled(true);
        pickMenuDown.setEnabled(true);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Inicia tela de batalha">
    public void carregaBattleMenu() {
        this.battleMenu = new BattleMenu(this);
        alteraPanel(battleMenu, jPMenuEsq);
        this.battleMenuDown = new BattleMenuDown(this);
        alteraPanel(battleMenuDown, jPMenuDown);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Atualiza dados de cada Round da batalha">
    @SuppressWarnings("empty-statement")
    public void atualizarDados(Mensagem Msg) throws InterruptedException {

        String[] s = Msg.corpo.split(":");
        String[] jg1 = s[1].split("=");
        String[] jg2 = s[2].split("=");
        if (numeroJogador == 1) {
            personagemAtual = personagemSelecionadosById(Integer.parseInt(jg1[0]));
            personagemAdversarioAtual = personagemAdversarioById(Integer.parseInt(jg2[0]));
            if ("(0)".equals(s[0])) {
                battleMenu.setJg2Pokemon(personagemAdversarioAtual);
                personagemAdversarioAtual.setHp(Integer.parseInt(jg2[1]));
                battleMenu.setJg2Hp(jg2[1]);
                Thread.sleep(1000L);
                battleMenu.setJg1Pokemon(personagemAtual);
                personagemAtual.setHp(Integer.parseInt(jg1[1]));
                battleMenu.setJg1Hp(jg1[1]);

            } else {
                battleMenu.setJg1Pokemon(personagemAtual);
                personagemAtual.setHp(Integer.parseInt(jg1[1]));
                battleMenu.setJg1Hp(jg1[1]);
                Thread.sleep(1000L);
                battleMenu.setJg2Pokemon(personagemAdversarioAtual);
                personagemAdversarioAtual.setHp(Integer.parseInt(jg2[1]));
                battleMenu.setJg2Hp(jg2[1]);
            }
            if (Integer.parseInt(jg1[1]) <= 0) {
                PersonagensRestantes--;
                if (PersonagensRestantes == 0) {
                    DefeatMenu derrota = new DefeatMenu();
                    alteraPanel(derrota, jPMenuEsq);
                    FinalDown finaldown = new FinalDown(this);
                    alteraPanel(finaldown, jPMenuDown);
                } else {
                    ChangeMenu changeMenu = new ChangeMenu(this, personagensSelecionados);
                    changeMenu.habilitaVoltar(false);
                    alteraPanel(changeMenu, jPMenuEsq);
                    chatMenu.adicionarLinha("Teu Pokemon morreu");

                }
            } else if (Integer.parseInt(jg2[1]) <= 0) {
                chatMenu.adicionarLinha("Pokemon do adversário morreu");
                PersonagensRestantesAdversario--;
                if (PersonagensRestantesAdversario == 0) {
                    VitoriaMenu victory = new VitoriaMenu();
                    alteraPanel(victory, jPMenuEsq);
                    FinalDown finaldown = new FinalDown(this);
                    alteraPanel(finaldown, jPMenuDown);
                }
            } else {
                battleMenuDown.enableAll(true);
            }
        } else {
            personagemAtual = personagemSelecionadosById(Integer.parseInt(jg2[0]));
            personagemAdversarioAtual = personagemAdversarioById(Integer.parseInt(jg1[0]));
            if ("(0)".equals(s[0])) {
                battleMenu.setJg1Pokemon(personagemAtual);
                personagemAtual.setHp(Integer.parseInt(jg2[1]));
                battleMenu.setJg1Hp(jg2[1]);
                Thread.sleep(1000L);
                battleMenu.setJg2Pokemon(personagemAdversarioAtual);
                personagemAdversarioAtual.setHp(Integer.parseInt(jg1[1]));
                battleMenu.setJg2Hp(jg1[1]);
            } else {
                battleMenu.setJg2Pokemon(personagemAdversarioAtual);
                personagemAdversarioAtual.setHp(Integer.parseInt(jg1[1]));
                battleMenu.setJg2Hp(jg1[1]);
                Thread.sleep(1000L);
                battleMenu.setJg1Pokemon(personagemAtual);
                personagemAtual.setHp(Integer.parseInt(jg2[1]));
                battleMenu.setJg1Hp(jg2[1]);

            }
            if (Integer.parseInt(jg2[1]) <= 0) {
                PersonagensRestantes--;
                if (PersonagensRestantes == 0) {
                    DefeatMenu derrota = new DefeatMenu();
                    alteraPanel(derrota, jPMenuEsq);
                    FinalDown finaldown = new FinalDown(this);
                    alteraPanel(finaldown, jPMenuDown);
                } else {
                    ChangeMenu changeMenu = new ChangeMenu(this, personagensSelecionados);
                    changeMenu.habilitaVoltar(false);
                    alteraPanel(changeMenu, jPMenuEsq);
                    chatMenu.adicionarLinha("Teu Pokemon morreu");

                }
            } else if (Integer.parseInt(jg1[1]) <= 0) {
                chatMenu.adicionarLinha("Pokemon do adversário morreu");
                PersonagensRestantesAdversario--;
                if (PersonagensRestantesAdversario == 0) {
                    VitoriaMenu victory = new VitoriaMenu();
                    alteraPanel(victory, jPMenuEsq);
                    FinalDown finaldown = new FinalDown(this);
                    alteraPanel(finaldown, jPMenuDown);
                }
            } else {
                battleMenuDown.enableAll(true);
            }

        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Rotina para inicio de cada turno">
    public void inicioTurno(Mensagem Msg) {
        carregaBattleMenu();
        String[] s = Msg.corpo.split(":");
        String[] jg1 = s[0].split("=");
        String[] jg2 = s[6].split("=");

        if (numeroJogador == 1) {
            personagemAtual = personagemSelecionadosById(Integer.parseInt(jg1[0]));
            personagemAdversarioAtual = personagemAdversarioById(Integer.parseInt(jg2[0]));
            battleMenu.setJg1Pokemon(personagemAtual);
            personagemAtual.setHp(Integer.parseInt(jg1[1]));
            battleMenu.setJg1Hp(jg1[1]);
            battleMenu.setJg2Pokemon(personagemAdversarioAtual);
            personagemAdversarioAtual.setHp(Integer.parseInt(jg2[1]));
            battleMenu.setJg2Hp(jg2[1]);

        } else {
            personagemAtual = personagemSelecionadosById(Integer.parseInt(jg2[0]));
            personagemAdversarioAtual = personagemAdversarioById(Integer.parseInt(jg1[0]));
            battleMenu.setJg1Pokemon(personagemAtual);
            personagemAtual.setHp(Integer.parseInt(jg2[1]));
            battleMenu.setJg1Hp(jg2[1]);
            battleMenu.setJg2Pokemon(personagemAdversarioAtual);
            personagemAdversarioAtual.setHp(Integer.parseInt(jg1[1]));
            battleMenu.setJg2Hp(jg1[1]);

        }
        battleMenuDown.enableAll(true);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Rotina para troca de pokemon em batalha">
    public void trocarPokemon() {
        ChangeMenu changeMenu = new ChangeMenu(this, this.personagensSelecionados);
        alteraPanel(changeMenu, jPMenuEsq);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GET e SET do personagem em batalha">
    public Personagem getPersonagemAtual() {
        return personagemAtual;
    }

    public void setPersonagemAtual(Personagem p) {
        this.personagemAtual = p;
    }
//</editor-fold>

    @SuppressWarnings("empty-statement")
    public void setBattlePersonagem(String s) {
        Personagem p = personagemSelecionadoByName(s);
        switch (p.getId()) {
            case 0:
                ct.enviarMensagem(new Mensagem(330, s));
                break;
            case 1:
                ct.enviarMensagem(new Mensagem(331, s));
                break;
            case 2:
                ct.enviarMensagem(new Mensagem(332, s));
                break;
            case 3:
                ct.enviarMensagem(new Mensagem(333, s));
                break;
            case 4:
                ct.enviarMensagem(new Mensagem(334, s));
                break;
            case 5:
                ct.enviarMensagem(new Mensagem(335, s));
                break;
            case 6:
                ct.enviarMensagem(new Mensagem(336, s));
                break;
            case 7:
                ct.enviarMensagem(new Mensagem(337, s));
                break;
            case 8:
                ct.enviarMensagem(new Mensagem(338, s));
                break;
            case 9:
                ct.enviarMensagem(new Mensagem(339, s));
                break;
        }

    }

    @SuppressWarnings("empty-statement")
    public Personagem personagemSelecionadoByName(String s) {
        int n;
        if (personagensSelecionados[0].getNome().equals(s)) {
            n = 0;
        } else {
            for (n = 0; !personagensSelecionados[n].getNome().equals(s); n++);
        }
        return personagensSelecionados[n];
    }

    public Personagem personagemSelecionadosById(int id) {
        int n;
        if (personagensSelecionados[0].getId() == id) {
            n = 0;
        } else {
            for (n = 0; personagensSelecionados[n].getId() != id; n++);

        }
        return personagensSelecionados[n];

    }

    public Personagem personagemAdversarioById(int id) {
        int n;
        if (personagensAdversario[0].getId() == id) {
            n = 0;
        } else {
            for (n = 0; personagensAdversario[n].getId() != id; n++);

        }
        return personagensAdversario[n];

    }

//<editor-fold defaultstate="collapsed" desc="GET e SET do número do jogador">
    public void setNumeroJogador(int numeroJogador) {
        this.numeroJogador = numeroJogador;
    }

    public int getNumeroJogador() {
        return numeroJogador;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Termina o jogo e desconecta o cliente">
    void closeAll() {
        ct.enviarMensagem(new Mensagem(120, nickName));
        ct.closeConnection();
        if (hospedeiro != null) {
            this.hospedeiro.parar();
        }
        this.setVisible(false);
    }
//</editor-fold>

}
