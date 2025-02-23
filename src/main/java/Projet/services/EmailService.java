package Projet.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {
    private String username;
    private String password;
    private String smtpHost;
    private String smtpPort;
    private boolean smtpAuth;
    private boolean smtpStarttls;

    public EmailService() {
        loadEmailConfiguration();
    }

    // Load email configuration from properties file
    public void loadEmailConfiguration() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("Gestion User/email.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find email.properties");
                return;
            }
            Properties prop = new Properties();
            prop.load(input);
            smtpHost = prop.getProperty("mail.smtp.host");
            smtpPort = prop.getProperty("mail.smtp.port");
            smtpAuth = Boolean.parseBoolean(prop.getProperty("mail.smtp.auth"));
            smtpStarttls = Boolean.parseBoolean(prop.getProperty("mail.smtp.starttls.enable"));
            username = prop.getProperty("mail.smtp.username");
            password = prop.getProperty("mail.smtp.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Send the password reset email with instructions
    public void sendResetEmail(String recipientEmail, String token) {

        String subject = "Réinitialisation du mot de passe";
        String body = "Vous avez demandé à réinitialiser votre mot de passe.\n\n"
                + "Veuillez ouvrir l'application et cliquer sur 'Réinitialiser manuellement le mot de passe'.\n"
                + "Copiez ensuite le jeton ci-dessous et collez-le dans le champ prévu dans la fenêtre de réinitialisation, "
                + "saisissez votre nouveau mot de passe, puis validez.\n\n"
                + "Jeton: " + token + "\n\n"
                + "Si vous n'avez pas demandé cette réinitialisation, ignorez cet email.";

        Properties props = new Properties();
        props.put("mail.smtp.auth", String.valueOf(smtpAuth));
        props.put("mail.smtp.starttls.enable", String.valueOf(smtpStarttls));
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur d'envoi de l'email.");
        }
    }
}
