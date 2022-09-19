package br.com.muffato.email;

import br.com.muffato.dao.Configuracao;
import br.com.muffato.dao.SQLite;
import static br.com.muffato.form.EmailForm.getPathLog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class JavaMailSender implements Runnable {
    private String host;
    private String port;

    private boolean debug;

    private Properties props;
    
    //Para envio em lote
    private String emailTo;
    private String assunto;
    private String texto;
    
    public JavaMailSender(String emailTo,String assunto,String texto) {
        this.emailTo = emailTo;
        this.assunto = assunto;
        this.texto = texto;
    }
            

    public JavaMailSender(String host, String port) {
	this.host = host;
	this.port = port;

	/** Get a Properties object */
	props = System.getProperties();
    }
    
    
    

    public Properties addProxy(String host, String port, String user, String password) {
        if ((host != null && !host.isEmpty()) && (port != null && !port.isEmpty())) {
            props.setProperty("proxySet", "true");
            
            if (host != null && !host.isEmpty()) {
                props.setProperty("http.proxyHost", host);
            }
            if (port != null && !port.isEmpty()) {
                props.setProperty("http.proxyPort", port);
            }
            if (user != null && !user.isEmpty()) {
                props.setProperty("http.proxyUser", user);
            }
            if (password != null && !password.isEmpty()) {
                props.setProperty("http.proxyPassword", password);
            }
        }
       
	return props;
    }

    public boolean send(String from, String password, String to, String cc, String title, String text, String pathAnexo) throws Exception {
	boolean result = false;
	try {

	    props.setProperty("mail.smtp.host", this.host);
	    props.setProperty("mail.smtp.port", this.port);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.store.protocol", "pop3");
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", this.host);

	    Session session = Session.getDefaultInstance(props, new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(from, password);
		}
	    });

	    session.setDebug(debug);

	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));

	    InternetAddress[] addressTO = { new InternetAddress(to) };
	    msg.setRecipients(Message.RecipientType.TO, addressTO);

            if (cc != null && !cc.isEmpty()) {
                InternetAddress[] addressCC = { new InternetAddress(cc) };
                msg.setRecipients(Message.RecipientType.CC, addressCC);
            }

	    InternetAddress addressFROM = new InternetAddress(from);
	    msg.setFrom(addressFROM);

	    msg.setSentDate(new Date());

	    msg.setSubject(title);
            MimeBodyPart messageBodyPartText = new MimeBodyPart();
            messageBodyPartText.setText(text);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPartText);
            
            if (pathAnexo != null && !pathAnexo.isEmpty()) {
                //Anexo
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                String file = pathAnexo;
                File f = new File(pathAnexo);
                String fileName = f.getName();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);
            }
            msg.setContent(multipart);
            Transport.send(msg);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

	return result;
    }

    public boolean isDebug() {
	return debug;
    }

    public void setDebug(boolean debug) {
	this.debug = debug;
    }

    public static void main(String[] args) {

	System.out.println("Send emails example using JavaMail");
	
	//String host = "smtp.gmail.com";
        String host = "smtp.muffato.com.br";
	String port = "587";

	JavaMailSender sender = new JavaMailSender(host, port);

	/** Activate this line for proxy authentication and change the settings with your details */
	sender.addProxy("proxy.muffato.com.br", "3128", "gomes_cb", "private1");
        //sender.addProxy("proxy.muffato.com.br", "3128", null, null);

	/** Activate this line if you need to see more details */
        //sender.setDebug(true);

	//String from = "william.dg88@gmail.com";
	//String password = "william1337";

	String from = "desenvolvimento10@muffato.com.br";
	String password = "private3";

	String to = "william.dg88@gmail.com";
	String cc = "william.dg88@gmail.com";

	String subject = "test";
	String msg = "Message sent using JavaMail.";

	System.out.println();
	System.out.println();
        try {
            boolean b = sender.send(from, password, to, cc, subject, msg, "C:\\Users\\gomes_cb\\Documents\\Garcia.pdf");
            if (b) {
                System.out.println("Message sent successfully.");
            } else {
                System.out.println("Message failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void enviarEmail(String emailTo, String assunto, String texto, String pathAnexo) throws Exception {
        try {
            String host = SQLite.obterConfiguracao(Configuracao.HOST);
            String port = SQLite.obterConfiguracao(Configuracao.PORTA);
            String proxyHost = SQLite.obterConfiguracao(Configuracao.HOST_PROXY);
            String proxyPorta = SQLite.obterConfiguracao(Configuracao.PORTA_PROXY);
            String proxyUser = SQLite.obterConfiguracao(Configuracao.LOGIN_PROXY);
            String proxySenha = SQLite.obterConfiguracao(Configuracao.SENHA_PROXY);
            String from = SQLite.obterConfiguracao(Configuracao.EMAIL);
            String password = SQLite.obterConfiguracao(Configuracao.SENHA);

            JavaMailSender sender = new JavaMailSender(host, port);
            sender.addProxy(proxyHost, proxyPorta, proxyUser, proxySenha);

            boolean b = sender.send(from, password, emailTo, null, assunto, texto, pathAnexo);
            if (b) {
                System.out.println("Message sent successfully.");
            } else {
                System.out.println("Fail to send email.");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void run() {
        try {
            enviarEmail(emailTo, assunto, texto, null);
        } catch (Exception ex) {
            Logger.getLogger(JavaMailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}