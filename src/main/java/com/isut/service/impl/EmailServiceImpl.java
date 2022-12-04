package com.isut.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.isut.constants.Constants;
import com.isut.service.IEmailService;

@Service
public class EmailServiceImpl implements IEmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private Environment environment;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(String toEmail, String subject, String body, String label, List<String> attachmentUrlList,
			List<String> ccEmailList) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(environment.getProperty(Constants.SUPPORT_EMAIL));// add mail address
			helper.setTo(toEmail);
			helper.setSubject(subject);
			mimeMessage.setFrom(new InternetAddress(environment.getProperty(Constants.SUPPORT_EMAIL), label));
			mimeMessage.setContent(body, "text/html");

			if (ccEmailList != null && !ccEmailList.isEmpty()) {
				for (String ccEmail : ccEmailList) {
					mimeMessage.addRecipients(Message.RecipientType.CC, ccEmail);
				}
			}
			mimeMessage.setFrom(new InternetAddress(environment.getProperty(Constants.SUPPORT_EMAIL), label));

			if (attachmentUrlList != null && !attachmentUrlList.isEmpty()) {
				for (String attachmentUrl : attachmentUrlList) {
					FileSystemResource file = new FileSystemResource(
							environment.getProperty(Constants.FILE_UPLOAD_DIR) + File.separator + attachmentUrl);
					helper.addAttachment(file.getFilename(), file);
				}
			}
			javaMailSender.send(mimeMessage);
			logger.info("Email Success!! Email sent to " + toEmail);
		} catch (MessagingException e) {
			logger.info("Email Failed!! MessagingException!! " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.info("Email Failed!! UnsupportedEncodingException!! " + e.getMessage());
		}
	}
}
