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

import java.io.Serializable;

/**
 * Classe reponsavel por encaminhar as mensagens de comunicação pela rede
 * 
 * @author Gustavo Constante   
 * @author Thiago Henrique Bonotto da Silva
 * 
 */

public class Mensagem implements Serializable{
   public int codigo;
   public String corpo;
   public Mensagem(int cod, String co){
      this.codigo = cod;
      this.corpo = co;
   }
}

