/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.muffato.dao;

import java.util.Iterator;

/**
 *
 * @author gomes_cb
 */
public class Exemplo {
    public static void main(String[] args) {
        try {
                SQLite dbCon = new SQLite("base.db");
                dbCon.initDB();

                dbCon.insert(new Configuracao("Chave1", "valor1"));
                dbCon.insert(new Configuracao("Chave2", "valor2"));
                dbCon.insert(new Configuracao("Chave3", "valor3"));

                Exemplo.listaTodos(dbCon);

                System.out.println("Removemos a pessoa com o nome Fulano e listamos novamenten");
                dbCon.remove("Chave3");

                Exemplo.listaTodos(dbCon);
        }
        catch (Exception e) {
                e.printStackTrace();
        }  
    }

    public static void listaTodos(SQLite dbCon) {
        Iterator it = dbCon.getAll().iterator();
        Configuracao hs;

        while (it.hasNext()) {
                hs = (Configuracao) it.next();

                System.out.println("Chave:" + hs.getChave());
                System.out.println("Valor:" + hs.getValor());
        }
    }
}
