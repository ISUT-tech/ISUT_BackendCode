package com.isut.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.exception.FileStorageException;
import com.isut.exception.MyFileNotFoundException;
import com.isut.property.FileStorageProperties;
import com.isut.service.IFileService;

@Service
public class FileServiceImpl implements IFileService {

	private Path fileStorageLocation;

	@Autowired
	private Environment environment;

	@Autowired
	public FileServiceImpl(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException(
					Constants.COULD_NOT_CREATE_THE_DIRECTORY_WHERE_THE_UPLOAD_FILES_WILL_BE_STORED, ex);
		}
	}

	@Override
	public void uploadFile(MultipartFile file, String fileName, String type,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		String fileUrl = storeFile(file, fileName, type);
		apiResponseDtoBuilder.withMessage(Constants.FILE_UPLOAD_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(fileUrl);
	}

	@Override
	public void uploadMultipleFile(MultipartFile[] files, String type, String fileName,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<String> imageList = new ArrayList<String>();
		int count = 1;
		for (MultipartFile file : files) {
			imageList.add(storeFile(file, new Date().getTime() + "_" + count, type));
			count++;
		}
		apiResponseDtoBuilder.withMessage(Constants.FILE_UPLOAD_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(imageList);
	}

	public String storeFile(MultipartFile file, String fileName, String imageType) {
		String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		File fileDirectory = new File(environment.getProperty("file.upload-dir") + File.separator + imageType);
		if (!fileDirectory.isDirectory()) {
			fileDirectory.mkdir();
		}
		fileStorageLocation = Paths.get(environment.getProperty("file.upload-dir") + File.separator + imageType)
				.toAbsolutePath().normalize();

		originalFileName = fileName;
		try {

			if (originalFileName.contains("..")) {
				throw new FileStorageException(Constants.FILENAME_CONTAINS_INVALID_PATH_SEQUENCE + originalFileName);
			}

			Path targetLocation = this.fileStorageLocation.resolve(originalFileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return imageType + File.separator + originalFileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName, String type, String directory) {
		try {
			Path filePath = Paths.get(directory + File.separator + type).toAbsolutePath().normalize().resolve(fileName)
					.normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException(Constants.FILE_NOT_FOUND + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException(Constants.FILE_NOT_FOUND + fileName, ex);
		}
	}

}
