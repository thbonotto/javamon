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

/**
 * Classe responsável por guardar todos os atributos do personagem especifico Ponyta
 * 
 * @author Thiago Henque Bonotto da Silva
 * @author Gustavo Constante
 */
public class Ponyta extends Personagem implements Fogo {

    public Ponyta() {
        this.id = 0;
        this.nome = "Ponyta";
        this.Ataque = 37;
        this.Defesa = 24;
        this.AttackSpeed = 39;        
        this.icon_escolha = new javax.swing.ImageIcon(getClass().getResource("/imagens/ponyta_escolha.png"));
        this.icon_frente = new javax.swing.ImageIcon(getClass().getResource("/imagens/ponyta_frente.png"));
        this.icon_costas = new javax.swing.ImageIcon(getClass().getResource("/imagens/ponyta_costas.png"));
    }
    public String getTipo(){
        return Ponyta.tipo;
    }

}
