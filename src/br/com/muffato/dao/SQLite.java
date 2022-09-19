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
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLite {
    private Connection conn;
    private Statement stm;
    
    public SQLite(String arquivo) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + arquivo);
        this.stm = this.conn.createStatement();
    }
    
    public static SQLite getConn() {
        SQLite dbCon = null;
        try {
            dbCon = new SQLite("base.db");
            dbCon.initDB();
        } catch (SQLException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbCon;
    }

    public void initDB() {	  
        try {
                // Remove e cria a tabela a cada execução. Mero exemplo
                //this.stm.executeUpdate("DROP TABLE IF EXISTS pessoas");

                this.stm.executeUpdate("CREATE TABLE IF NOT EXISTS configuracao ("
                        + "chave varchar(255) PRIMARY KEY NOT NULL,"
                        + "valor varchar(255));");
        } 
        catch (SQLException e) {
                e.printStackTrace();  
        }  
    }

    public void insert(Configuracao conf) {		  
        try {		  
                this.stm = this.conn.createStatement();

                this.stm.executeUpdate("INSERT INTO configuracao (chave, valor) VALUES ('"
                        + conf.getChave() + "','"
                        + conf.getValor() + "')");
        }
        catch (SQLException e) {
                e.printStackTrace();
        }
    }
    
     public void update(String chave, String valor) {		  
        try {		  
                this.stm = this.conn.createStatement();

                this.stm.executeUpdate("UPDATE configuracao SET valor = '" + valor + "' WHERE chave = '" + chave + "'"); 
        }
        catch (SQLException e) {
                e.printStackTrace();
        }
    }

    public void remove(String chave) {		  
        try {		  
                this.stm = this.conn.createStatement();

                this.stm.executeUpdate("DELETE FROM configuracao WHERE chave = '" + chave + "'");
        }
        catch (SQLException e) {
                e.printStackTrace();  
        }  
    }

    public Vector getAll() {
        Vector<Configuracao> lista = new Vector();	  
        ResultSet rs;

        try {
            rs = this.stm.executeQuery("SELECT * FROM configuracao ORDER BY chave");

            while (rs.next()) {
                    lista.add(new Configuracao(rs.getString("chave"), rs.getString("valor")));
            }

            rs.close();

        }
        catch (SQLException e) {
                e.printStackTrace();
        }

        return lista;
    }
    
    public static void iniciarConfiguracoes() {
        try {
            SQLite dbCon = new SQLite("base.db");
            dbCon.initDB();

            if (dbCon.getAll().size() == 0 ) {
                dbCon.insert(new Configuracao(Configuracao.HOST, ""));
                dbCon.insert(new Configuracao(Configuracao.PORTA,""));
                dbCon.insert(new Configuracao(Configuracao.EMAIL, ""));
                dbCon.insert(new Configuracao(Configuracao.SENHA, ""));
                
                dbCon.insert(new Configuracao(Configuracao.HOST_PROXY, ""));
                dbCon.insert(new Configuracao(Configuracao.PORTA_PROXY, ""));
                dbCon.insert(new Configuracao(Configuracao.LOGIN_PROXY, ""));
                dbCon.insert(new Configuracao(Configuracao.SENHA_PROXY, ""));
                
                dbCon.insert(new Configuracao(Configuracao.ASSINATURA, ""));
                dbCon.insert(new Configuracao(Configuracao.NOME_EMPRESA, ""));
                dbCon.insert(new Configuracao(Configuracao.TEXTO_EMAIL, 
                "Bom dia, 	   \n" +
                "	   \n" +
                "Prezado cliente,	   \n" +
                "	   \n" +
                "Estamos entrando em contato para lembra-lo de que sua empresa tem um boleto em aberto conosco	   \n" +
                "que foi enviado junto a DANFE no ato da entrega da mercadoria, conforme informações descriminadas abaixo 	   \n" +
                "Caso tenha alguma dúvida ou divergencia sobre o documento,  por favor entrar em contato 	   \n" +
                "	   \n" +
                "Razão Social 	_RAZAO_   \n" +
                "CnpJ	_CNPJ_   \n" +
                "Nota Fiscal	_NOTA_FISCAL_   \n" +
                "Emissão	_EMISSAO   \n" +
                "Venc	_VCTO_   \n" +
                "Valor	R$ _VALOR_   \n" +
                "	   \n" +
                "Caso o pagamento já tenha sido efetuado, por favor desconsiderar a mensagem 	   \n" +
                "	\n" +
                "Cordialmente"));
            }
        }
        catch (Exception e) {
                e.printStackTrace();
        }  
    }
    
    public static String obterConfiguracao(String chave) {
        try {
            SQLite dbCon = new SQLite("base.db");
            dbCon.initDB();
            Vector<Configuracao> lista = dbCon.getAll();
            if (lista.size() > 0) {
                for (Configuracao c : lista) {
                    if (c.getChave().equals(chave)) {
                        return c.getValor();
                    }
                }
            }
                
        }
        catch (Exception e) {
                e.printStackTrace();
        }  
        return "";
    }
    
}
