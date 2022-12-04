package com.isut.service.impl;

import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.isut.constants.Constants;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {
	@InjectMocks
	EmailServiceImpl emailServiceImpl;
	@Mock
	JavaMailSender javaMailSender;
	@Mock
	Environment environment;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void sendEmail() throws MessagingException, UnsupportedEncodingException {
		String toEmail = "test123@g.com";
		String subject = "test";
		String body = "hii test";
		String label = "test";
		Properties properties = new Properties();
		Session session = Session.getInstance(properties, null);
		MimeMessage mimeMessage = new MimeMessage(session);

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("test123@gmail.com");
		helper.setTo(toEmail);
		helper.setSubject(subject);
		mimeMessage.setFrom(new InternetAddress("test", label));
		mimeMessage.setContent(body, "text/html");
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		String fromEmail = "test@gmail.com";
		when(environment.getProperty(Constants.SUPPORT_EMAIL)).thenReturn(fromEmail);
		emailServiceImpl.sendEmail(toEmail, subject, body, label, null, null);

	}
}
