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
package poo.Personagens;

/**
 * Classe responsável por guardar todos os atributos do personagem especifico Goldeen
 * 
 * @author Thiago Henque Bonotto da Silva
 * @author Gustavo Constante
 */
public class Goldeen extends Personagem implements Agua {

    public Goldeen() {
        this.id = 3;
        this.nome = "Goldeen";
        this.Ataque = 35;
        this.Defesa = 31;
        this.AttackSpeed = 34; 
        this.icon_escolha = new javax.swing.ImageIcon(getClass().getResource("/imagens/goldeen_escolha.png"));
        this.icon_frente = new javax.swing.ImageIcon(getClass().getResource("/imagens/goldeen_frente.png"));
        this.icon_costas = new javax.swing.ImageIcon(getClass().getResource("/imagens/goldeen_costas.png"));
    }

    @Override
    public String getTipo() {
        return Goldeen.tipo;
    }

}
