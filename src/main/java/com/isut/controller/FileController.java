package com.isut.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.service.IFileService;

@CrossOrigin(origins = "*", maxAge = 36000000)
@RestController
@RequestMapping(Constants.API_BASE_URL)
public class FileController {

	@Autowired
	private IFileService fileService;

	@Autowired
	private Environment environment;

	@RequestMapping(value = "/file/upload", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto uploadFile(@RequestParam(required = true) MultipartFile file, @RequestParam String fileName,
			@RequestParam String type) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		fileService.uploadFile(file, fileName, type, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/files/upload", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto uploadMultipleFile(@RequestParam(required = true) MultipartFile[] files,
			@RequestParam String type, @RequestParam String fileName) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		fileService.uploadMultipleFile(files, type, fileName, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/file/{type}/{file}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFile(@PathVariable("type") String type, @PathVariable("file") String file) {
		byte[] image = null;
		try {
			image = Files.readAllBytes(Paths
					.get(environment.getProperty("file.upload-dir") + File.separator + type + File.separator + file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}

	@GetMapping("/file/download/{type}/{fileName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String type, @PathVariable String fileName,
			HttpServletRequest request) {

		Resource resource = null;
		resource = fileService.loadFileAsResource(fileName, type, environment.getProperty("file.upload-dir"));

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
