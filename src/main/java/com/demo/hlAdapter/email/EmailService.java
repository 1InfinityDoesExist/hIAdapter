package com.demo.hlAdapter.email;

import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import com.amazonaws.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailService {

    @Autowired
    private GmailProvider gmailProvider;

    @Value("${reset.password.email.host}")
    private String host;
    @Value("${reset.password.email.port}")
    private int port;
    @Value("${reset.password.email.id}")
    private String emailId;
    @Value("${reset.password.email.pwd}")
    private String password;
    @Value("${reset.password.email.provider}")
    private String provider;
    @Value("${reset.password.email.subject}")
    private String subject;

    public ModelMap sendEmail(ModelMap emailDetails) {
        ModelMap modelMap = new ModelMap();
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(host);
        javaMailSenderImpl.setPort(port);
        javaMailSenderImpl.setUsername(emailId);
        javaMailSenderImpl.setPassword(password);
        javaMailSenderImpl.setJavaMailProperties(getJavaMailProperties(provider));
        MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject((String) emailDetails.get("subject"));
            helper.setText((String) emailDetails.get("mailBody"));
            if (emailDetails.get("to") instanceof List) {
                helper.setTo(((String) emailDetails.get("to")).split(","));
            } else {
                helper.setTo((String) emailDetails.get("to"));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSenderImpl.send(mimeMessage);
        modelMap.addAttribute("msg", "Successfully mail sent.");
        return modelMap;
    }

    private Properties getJavaMailProperties(String provider) {
        Properties properties = new Properties();
        if (!StringUtils.isNullOrEmpty(provider)) {
            if (provider.equalsIgnoreCase("GMAIL")) {
                getJavaGmailProperties(properties);
            }
        }
        return properties;
    }

    private void getJavaGmailProperties(Properties properties) {
        gmailProvider.getGmailProperties().forEach((key, value) -> {
            properties.setProperty(key, value);
        });
    }
}
