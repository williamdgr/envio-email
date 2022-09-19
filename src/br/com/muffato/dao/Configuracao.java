/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.muffato.dao;

/**
 *
 * @author gomes_cb
 */
public class Configuracao {
    
    public static final String HOST = "HOST";
    public static final String PORTA = "PORTA";
    public static final String EMAIL = "EMAIL";
    public static final String SENHA = "SENHA";
    
    public static final String HOST_PROXY = "HOST_PROXY";
    public static final String PORTA_PROXY = "PORTA_PROXY";
    public static final String LOGIN_PROXY = "LOGIN_PROXY";
    public static final String SENHA_PROXY = "SENHA_PROXY";
    
    public static final String ASSINATURA = "ASSINATURA";
    public static final String NOME_EMPRESA = "NOME_EMPRESA";
    public static final String TEXTO_EMAIL = "TEXTO_EMAIL";

    private String chave;
    private String valor;

    public Configuracao() {
    }

    public Configuracao(String chave, String valor) {
        this.chave = chave;
        this.valor = valor;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    
}
