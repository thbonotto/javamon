/*
 * Copyright (C) 2013 vendetta.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package poo.Personagens;

import javax.swing.Icon;

/**
 * Classe responsável por guardar todos os atributos basicos que todos os personagens implementam
 * 
 * @author Thiago Henque Bonotto da Silva 
 * @author Gustavo Constante
 */
public abstract class Personagem {
    Icon icon_escolha;
    Icon icon_costas;
    Icon icon_frente;
    String nome;
    int id;
    int hp = 100;
    int AttackSpeed;
    int Defesa;
    int Ataque;
    boolean Item2=true;
    boolean Item1=true;

    /**
     *
     * @return
     */
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Icon getIcon_escolha() {
        return icon_escolha;
    }

    public Icon getIcon_costas() {
        return icon_costas;
    }

    public Icon getIcon_frente() {
        return icon_frente;
    }

    

    public String getNome() {
        return this.nome;
    }

    public int getId() {
       return this.id;
    }

    public int getAttackSpeed() {
       return this.AttackSpeed;
    }

    
    public int getAtaque() {
        return this.Ataque;
    }

 
    public int getDefesa() {
       return this.Defesa;
    }
    public abstract String getTipo();

    public void setAttackSpeed(int attackSpeed) {
        this.AttackSpeed=attackSpeed;
    }

    public boolean isItem2() {
        return Item2;
    }

    public void setItem2(boolean Item2) {
        this.Item2 = Item2;
    }

    public boolean isItem1() {
        return Item1;
    }

    public void setItem1(boolean Item1) {
        this.Item1 = Item1;
    }
    
}
