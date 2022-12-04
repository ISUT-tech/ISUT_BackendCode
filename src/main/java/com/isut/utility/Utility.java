package com.isut.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gson.Gson;
import com.isut.dto.NotificationDto;
import com.isut.dto.PaginationDataDto;
import com.isut.dto.PaginationDto;
import com.isut.model.User;
import com.isut.repository.UserRepository;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utility {

	public static String generateRandomPassword(int len) {
		String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String Small_chars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String symbols = "!@#$%^&*_=+-/.?<>)";

		String values = Capital_chars + Small_chars + numbers + symbols;

		Random rndm_method = new Random();

		String password = "";

		for (int i = 0; i < len; i++) {

			char ch = values.charAt(rndm_method.nextInt(values.length()));
			password += ch;

		}
		return password;
	}

	public static int generateOTP(int length) {
		String numbers = "1234567890";
		Random random = new Random();
		String otp = new String();

		for (int i = 0; i < length; i++) {
			otp += numbers.charAt(random.nextInt(numbers.length()));
		}

		return Integer.parseInt(otp);
	}

	public static <T, U> List<U> convertStringListToIntegerList(List<T> listOfString, Function<T, U> function) {
		return listOfString.stream().map(function).collect(Collectors.toList());
	}

	public static PaginationDataDto getPaginationData(long totalCounts, PaginationDto paginationDto) {
		PaginationDataDto paginationDataDto = new PaginationDataDto();
		int totalPages = (int) Math.ceil((double) totalCounts / (double) paginationDto.getPerPage());
		int from = paginationDto.getCurrentPage() - 1;
		int to = paginationDto.getPerPage();
		paginationDataDto.setTotalPages(totalPages);
		paginationDataDto.setFrom(from);
		paginationDataDto.setTo(to);
		return paginationDataDto;
	}

	public static String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	public static void sendNotification(NotificationDto notificationDto) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().followRedirects(true)
					.readTimeout(30, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, new Gson().toJson(notificationDto));
			Request request = new Request.Builder().url("https://fcm.googleapis.com/fcm/send").method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization",
							"key=AAAAjC-MCf4:APA91bFY44FW6WsnC1vRqPKxVzRvn9X_tUJqf4yA1jHYsBi__2N_nKi6JI3XHHjOdsLhl6zkO_NaiQ8-uxWoAKAtaiilHMbowO8kpQRfMTd66BIcjhjot8rWYByVOzcB_jBOrznezLM1")
					.build();
			Response response = client.newCall(request).execute();
			String responseString = response.body().string();
			System.out.println(responseString);
			okhttpConnectionResponseClose(response);
			okhttpConnectionClose(client);
		} catch (Exception e) {
			System.err.println("notification failed");
		}
	}

	public static void okhttpConnectionClose(OkHttpClient client) {
		try {
			client.dispatcher().executorService().shutdown();
		} catch (Exception e) {
		}
		try {
			client.connectionPool().evictAll();
		} catch (Exception e) {
		}
		try {
			client.cache().close();
		} catch (Exception e) {
		}

	}

	public static void okhttpConnectionResponseClose(Response response) {
		try {
			response.close();
		} catch (Exception e) {
		}
		try {
			response.body().close();
		} catch (Exception e) {
		}
	}

	public static String addANDOrOR(boolean flag) {
		String condition = "";
		if (flag) {
			condition = " AND";
		}
		return condition;
	}

	public static String addWhere(boolean flag) {
		String condition = "";
		if (flag) {
			condition = " where";
		}
		return condition;
	}

	public static User getSessionUser(UserRepository userRepository) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		return userRepository.findByEmailOrMobileNumber(currentUsername, currentUsername);
	}

	public static Date getFormatedDateFromDate(Date date, int hourOfDay, int minute) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.AM_PM, 0);
			calendar.set(Calendar.HOUR, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, 0);
			return calendar.getTime();
		} catch (Exception e) {
			return null;
		}
	}

}
