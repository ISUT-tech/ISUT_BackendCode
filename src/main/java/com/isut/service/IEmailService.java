package com.isut.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface IEmailService {

	void sendEmail(String toEmail, String subject, String body, String label, List<String> attachmentUrlList,
			List<String> ccEmailList);

}
