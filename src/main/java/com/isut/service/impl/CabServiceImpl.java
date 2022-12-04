package com.isut.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.CabDetailsWithDriverRewardDto;
import com.isut.dto.CabFilterWithPaginationDto;
import com.isut.dto.CabRequestDto;
import com.isut.dto.UserWithCabDetails;
import com.isut.mapper.CustomMapper;
import com.isut.model.Cab;
import com.isut.model.Driver;
import com.isut.model.License;
import com.isut.model.User;
import com.isut.repository.CabRepository;
import com.isut.repository.DriverRepository;
import com.isut.repository.LicenseRepository;
import com.isut.repository.UserRepository;
import com.isut.repostory.custom.CabRepositoryCustom;
import com.isut.service.ICabService;
import com.isut.service.IUserService;

@Service
public class CabServiceImpl implements ICabService {
	@Autowired
	private CabRepository cabRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CabRepositoryCustom cabRepositoryCustom;

	@Autowired
	private CustomMapper customMapper;

	@Autowired
	private IUserService userService;

	@Autowired
	private DriverRepository driverRepository;
	@Autowired
	LicenseRepository licenseRepository;

	@Override
	public void addCabDetails(@Valid CabRequestDto cabRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		User sessionUser = userService.getSessionUser();
		if (sessionUser.getRole() != 3) {
			apiResponseDtoBuilder.withMessage(Constants.UNAUTHORIZED).withStatus(HttpStatus.UNAUTHORIZED);
			return;
		}
		if (cabRepository.existsByCarNumber(cabRequestDto.getCarNumber())) {
			apiResponseDtoBuilder.withMessage(Constants.CAR_ALLREADY_EXISTS).withStatus(HttpStatus.OK);
			return;
		}
		Cab cab = customMapper.cabRequestDtoToCab(cabRequestDto);
		cab.setCreatedAt(new Date());
		cabRepository.save(cab);
		License license = licenseRepository.findByDriverId(sessionUser.getId());
		license.setLicenseNumber(cabRequestDto.getLicenseNumber());
		licenseRepository.save(license);
		apiResponseDtoBuilder.withMessage(Constants.CAR_DETAILS_ADDED_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(cab);
	}

	@Override
	public void updateCabDetails(@Valid Cab cab, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Cab> cabCheck = cabRepository.findById(cab.getId());
		if (!cabCheck.isPresent()) {
			apiResponseDtoBuilder.withMessage(Constants.NO_CAR_EXISTS).withStatus(HttpStatus.OK);
			return;
		}
		cab.setUpdatedAt(new Date());
		cabRepository.save(cab);
		apiResponseDtoBuilder.withMessage(Constants.CAR_DETAILS_UPDATED_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(cab);
	}

	@Override
	public void getCabDetailsByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Cab> cabCheck = cabRepository.findByUserId(userId);
		Optional<Driver> user = driverRepository.findById(userId);
		if (user.isPresent()) {
			if (user.get().getRole() == 3) {
				CabDetailsWithDriverRewardDto dataDto = new CabDetailsWithDriverRewardDto();
				dataDto.setCabs(cabCheck);
				dataDto.setDriverId(user.get().getId());
				dataDto.setRewardPoints(user.get().getRewardPoints());
				apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(dataDto);
			} else {
				apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(cabCheck);
			}
		} else {
			apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(cabCheck);

		}
	}

	@Override
	public void getCabDetailsById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Cab> cab = cabRepository.findById(id);
		if (cab.isPresent()) {
			apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(cab);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.CAB_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void getAllCabDetails(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Cab> cabList = cabRepository.findAll();
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(cabList);
	}

	@Override
	public void getCabListByFilterWithPagination(CabFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withMessage(Constants.DATA_LIST).withStatus(HttpStatus.OK)
				.withData(cabRepositoryCustom.getCabListByFilterWithPagination(filterWithPagination));
	}

	@Override
	public void isActiveCab(long id, boolean active, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Cab> cab = cabRepository.findById(id);
		if (cab.isPresent()) {
			cab.get().setActive(active);
			save(cab.get());
			apiResponseDtoBuilder.withMessage(active ? Constants.CAB_ACTIVE_SUCCESS : Constants.CAB_DEACTIVE_SUCCESS)
					.withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.CAB_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

	private void save(Cab cab) {
		cabRepository.save(cab);
	}

	@Override
	public void changeStatus(long id, int status, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Cab> cab = cabRepository.findById(id);
		if (cab.isPresent()) {
			cab.get().setStatus(status);
			save(cab.get());
			apiResponseDtoBuilder
					.withMessage(status == 1 ? Constants.CAB_APPROVE_SUCCESS : Constants.CAB_REJECT_SUCCESS)
					.withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.CAB_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void selectCarByName(String carName, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Cab> cabs = cabRepository.findByCarNameContaining(carName);
		if (cabs.size() > 0) {
			apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(cabs);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.CAB_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void selectCarByType(Integer carType, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Cab> cabs = cabRepository.findByCarType(carType);
		if (cabs.size() > 0) {
			List<UserWithCabDetails> listOfData = new ArrayList<>();
			for (Cab cab : cabs) {
				Boolean flag = true;
				Optional<User> user = userRepository.findById(cab.getUserId());
				for (UserWithCabDetails userWithCabDetails : listOfData) {
					if (userWithCabDetails.getUser().getId().equals(user.get().getId())) {
						userWithCabDetails.getCabs().add(cab);
						flag = false;
					}
				}
				if (flag) {
					UserWithCabDetails dataDto = new UserWithCabDetails();
					dataDto.setCabs(new ArrayList<Cab>());
					dataDto.setUser(user.get());
					dataDto.getCabs().add(cab);
					listOfData.add(dataDto);
				}

			}
			apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(listOfData);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.CAB_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

}
