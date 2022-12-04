package com.isut.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isut.constants.Constants;
import com.isut.dto.AdminRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.PaginationDto;
import com.isut.dto.UserFilterWithPaginationDto;
import com.isut.dto.UserRequestDto;
import com.isut.dto.UserWithCabDetails;
import com.isut.mapper.CustomMapper;
import com.isut.model.Admin;
import com.isut.model.Cab;
import com.isut.model.Customer;
import com.isut.model.Driver;
import com.isut.model.License;
import com.isut.model.User;
import com.isut.repository.AdminRepository;
import com.isut.repository.CabRepository;
import com.isut.repository.CustomerRepository;
import com.isut.repository.DriverRepository;
import com.isut.repository.LicenseRepository;
import com.isut.repository.UserRepository;
import com.isut.repository.custom.UserRepositoryCustom;
import com.isut.service.IEmailService;
import com.isut.service.IUserService;
import com.isut.service.IVerificationTokenService;
import com.isut.utility.Utility;

@Service("userService")
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private CabRepository cabRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepositoryCustom userRepositoryCustom;

	@Autowired
	private CustomMapper customMapper;

	@Autowired
	private IVerificationTokenService verificationTokenService;

	@Autowired
	private Environment environment;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private IEmailService emailService;

	@Autowired
	private LicenseRepository licenseRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByMobileNumberOrEmail(username, username);
		if (user == null) {
			throw new UsernameNotFoundException(Constants.INVALID_USERNAME_OR_PASSWORD);
		}
		return new org.springframework.security.core.userdetails.User(user.getMobileNumber(), user.getPassword(),
				getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	@Override
	public void addAdmin(AdminRequestDto adminRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder,
			HttpServletRequest request) {
		if (userRepository.existsByMobileNumber(adminRequestDto.getMobileNumber())) {
			apiResponseDtoBuilder.withMessage(Constants.MOBILE_NUMBER_ALREADY_EXISTS)
					.withStatus(HttpStatus.ALREADY_REPORTED);
			return;
		}
		if (userRepository.existsByEmail(adminRequestDto.getEmail())) {
			apiResponseDtoBuilder.withMessage(Constants.EMAIL_ALREADY_EXISTS).withStatus(HttpStatus.ALREADY_REPORTED);
			return;
		}
		Admin user = customMapper.adminRequestDtoToAdmin(adminRequestDto);
		String password = Utility.generateRandomPassword(8);
		String newPasswordEncodedString = bCryptPasswordEncoder.encode(password);
		user.setPassword(newPasswordEncodedString);
		user.setCreatedAt(new Date());
		user.setRole(1);
		saveAdmin(user);
		apiResponseDtoBuilder.withMessage(Constants.USER_ADD_SUCCESS).withStatus(HttpStatus.OK).withData(user);
		verificationTokenService.sendVerificationToken(user);
	}

	@Override
	public void addUser(UserRequestDto userRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder,
			HttpServletRequest request) {
		if (userRepository.existsByMobileNumber(userRequestDto.getMobileNumber())) {
			apiResponseDtoBuilder.withMessage(Constants.MOBILE_NUMBER_ALREADY_EXISTS)
					.withStatus(HttpStatus.ALREADY_REPORTED);
			return;
		}
		if (userRepository.existsByEmail(userRequestDto.getEmail())) {
			apiResponseDtoBuilder.withMessage(Constants.EMAIL_ALREADY_EXISTS).withStatus(HttpStatus.ALREADY_REPORTED);
			return;
		}
		Customer user = customMapper.userRequestDtoToCustomer(userRequestDto);
		String newPasswordEncodedString = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(newPasswordEncodedString);
		user.setCreatedAt(new Date());
		user.setRole(2);
		saveCustomer(user);
		apiResponseDtoBuilder.withMessage(Constants.USER_ADD_SUCCESS).withStatus(HttpStatus.OK).withData(user);
		verificationTokenService.sendVerificationToken(user);
	}

	@Override
	public void addDriver(UserRequestDto userRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder,
			HttpServletRequest request) {
		if (userRepository.existsByMobileNumber(userRequestDto.getMobileNumber())) {
			apiResponseDtoBuilder.withMessage(Constants.MOBILE_NUMBER_ALREADY_EXISTS)
					.withStatus(HttpStatus.ALREADY_REPORTED);
			return;
		}
		if (userRepository.existsByEmail(userRequestDto.getEmail())) {
			apiResponseDtoBuilder.withMessage(Constants.EMAIL_ALREADY_EXISTS).withStatus(HttpStatus.ALREADY_REPORTED);
			return;
		}
		Driver user = customMapper.userRequestDtoToDriver(userRequestDto);
		String newPasswordEncodedString = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(newPasswordEncodedString);
		user.setCreatedAt(new Date());
		user.setRole(3);
		saveDriver(user);
		Driver driver = driverRepository.findByMobileNumber(userRequestDto.getMobileNumber());
		if (driver != null) {
			License license = new License();
			license.setDriverId(user.getId());
			license.setLicenseExpire(userRequestDto.getLicenseExpired());
			license.setCreatedAt(new Date());
			saveLicense(license);
		}
		apiResponseDtoBuilder.withMessage(Constants.USER_ADD_SUCCESS).withStatus(HttpStatus.OK).withData(user);
		verificationTokenService.sendVerificationToken(user);
	}

	@Override
	public void updateDriver(@Valid Driver user, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		if (!driverRepository.existsByMobileNumber(user.getMobileNumber())) {
			apiResponseDtoBuilder.withMessage(Constants.NO_USER_EXISTS).withStatus(HttpStatus.OK).withData(user);
			return;
		}
		String newPasswordEncodedString = bCryptPasswordEncoder.encode(user.getMobileNumber());
		user.setPassword(newPasswordEncodedString);
		user.setUpdatedAt(new Date());
		saveDriver(user);
		apiResponseDtoBuilder.withMessage(Constants.USER_UPDATED_SUCCESSFULLY).withStatus(HttpStatus.OK).withData(user);
	}

	@Override
	public void getUserByRole(int role, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<UserWithCabDetails> userWithCabDetailsList = new ArrayList<UserWithCabDetails>();
		List<User> userList = userRepository.findByRole(role);
		for (User user : userList) {
			UserWithCabDetails userWithCabDetails = new UserWithCabDetails();
			userWithCabDetails.setUser(user);
			List<Cab> cabList = cabRepository.findByUserId(user.getId());
			userWithCabDetails.setCabs(cabList);
			userWithCabDetailsList.add(userWithCabDetails);
			if (user.getRole() == 3) {
				License license = licenseRepository.findByDriverId(user.getId());
				userWithCabDetails.setLicenseNumber(license.getLicenseNumber());
			}
		}
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(userWithCabDetailsList);
	}

	@Override
	public void getUserListByFilterWithPagination(UserFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		PaginationDto pagination = userRepositoryCustom.getUserListByFilterWithPagination(filterWithPagination);
		apiResponseDtoBuilder.withMessage(Constants.DATA_LIST).withStatus(HttpStatus.OK).withData(pagination);
	}

	@Override
	public void getUserDetailsById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			UserWithCabDetails userWithCabDetails = new UserWithCabDetails();
			userWithCabDetails.setUser(user.get());
			List<Cab> cabList = cabRepository.findByUserId(user.get().getId());
			userWithCabDetails.setCabs(cabList);
			apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(userWithCabDetails);
		} else {
			apiResponseDtoBuilder.withMessage("fail").withStatus(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void isActiveUser(long id, boolean active, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			user.get().setActive(active);
			save(user.get());
			apiResponseDtoBuilder.withMessage(active ? Constants.USER_ACTIVE_SUCCESS : Constants.USER_DEACTIVE_SUCCESS)
					.withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.USER_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public User findByMobileNumber(String username) {
		return userRepository.findByMobileNumber(username);
	}

	@Override
	public User findByMobileNumberOrEmail(String mobileNumber, String email) {
		return userRepository.findByMobileNumberOrEmail(mobileNumber, email);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	private void saveAdmin(Admin admin) {
		adminRepository.save(admin);
	}

	private void saveLicense(License license) {
		licenseRepository.save(license);
	}

	@Override
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.isPresent() ? user.get() : null;
	}

	@Override
	public void sendTemporaryPassword(String username, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		User user = findByEmailOrMobileNumber(username, username);
		if (user != null) {
			String password = Utility.generateRandomPassword(8);
			String newPasswordEncodedString = bCryptPasswordEncoder.encode(password);
			user.setPassword(newPasswordEncodedString);
			save(user);
			new Thread(() -> {
				String subject = "";
				String body = environment.getProperty("email.reset.body").replace("%newLine%", "\n");
				body = body.replace("%password%", password);
				body = body + "\n\n Kind Regards\n Team ISUT";
				emailService.sendEmail(user.getEmail(), subject, body, "", null, null);
			}).start();
			apiResponseDtoBuilder.withMessage(Constants.SEND_DETAILS_TO_YOUR_EMAIL).withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.USER_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

	private User findByEmailOrMobileNumber(String username, String username2) {
		return userRepository.findByEmailOrMobileNumber(username, username);
	}

	@Override
	@Transactional
	public void deleteUserById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			userRepository.deleteById(id);
			if (user.get().getRole() == 3) {
				cabRepository.deleteByUserId(user.get().getId());
			}
			apiResponseDtoBuilder.withMessage(Constants.USER_DELETED_SUCCESSFULLY).withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage("fail").withStatus(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public User getSessionUser() {
		return Utility.getSessionUser(userRepository);
	}

	@Override
	public void referFriendByEmail(String email, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		User user = getSessionUser();
		new Thread(() -> {
			String subject = "Refer Friend";
			String body = "Hi " + email.split("@")[0] + " your friend " + user.getFullName()
					+ " has invited to check our new ISUT cab application. Please install and try it out.\n\nKind Regards\nTeam ISUT";
			emailService.sendEmail(email, subject, body, "", null, null);
		}).start();
		apiResponseDtoBuilder.withMessage("Successfully refered!!").withStatus(HttpStatus.OK);
	}

	@Override
	public void sendRewardToDriver(long rewardPoint, long drivierId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Driver> user = driverRepository.findById(drivierId);
		if (user.isPresent()) {
			if (user.get().getRole() != 3) {
				apiResponseDtoBuilder.withMessage(Constants.DRIVER_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
				return;
			}
			Driver driverUser = user.get();
			if (driverUser.getRewardPoints() == null) {
				driverUser.setRewardPoints(rewardPoint);
			} else {
				driverUser.setRewardPoints(driverUser.getRewardPoints() + rewardPoint);
			}
			driverRepository.save(driverUser);
			apiResponseDtoBuilder.withMessage(Constants.REWARD_SENT_SUCCESSFULLY).withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.USER_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void isActiveDriver(long id, boolean active, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<User> user = userRepository.findByIdAndRole(id, 3);
		if (user.isPresent()) {
			user.get().setActive(active);
			save(user.get());
			apiResponseDtoBuilder
					.withMessage(active ? Constants.DRIVER_ACTIVE_SUCCESS : Constants.DRIVER_DEACTIVE_SUCCESS)
					.withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.DRIVER_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ApiResponseDtoBuilder isAccountValid(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		User user = getSessionUser();
		Optional<Driver> driver = driverRepository.findById(user.getId());
		if (driver.isPresent()) {
			Driver driverUser = driver.get();
			if (!driverUser.getActive()) {
				return apiResponseDtoBuilder.withMessage("Your account has been suspended")
						.withStatus(HttpStatus.EXPECTATION_FAILED);
			}
			if (driverUser.getRole() == 3) {
				License license = licenseRepository.findByDriverId(driverUser.getId());
				if (license.getLicenseExpire() == null) {
					return apiResponseDtoBuilder.withMessage("Please add licence expired details !")
							.withStatus(HttpStatus.EXPECTATION_FAILED);
				} else if (license.getLicenseExpire().before(new Date())) {
					return apiResponseDtoBuilder.withMessage("Licence has been expired !")
							.withStatus(HttpStatus.EXPECTATION_FAILED);
				}
				List<Cab> cabs = cabRepository.findByUserId(driverUser.getId());
				if (cabs.size() > 0 && cabs.get(0).getStatus() == 0) {
					return apiResponseDtoBuilder.withMessage("Cab not Approved yet !")
							.withStatus(HttpStatus.EXPECTATION_FAILED);
				} else if (cabs.size() > 0 && cabs.get(0).getStatus() == 2) {
					return apiResponseDtoBuilder.withMessage("Cab has been rejected !")
							.withStatus(HttpStatus.EXPECTATION_FAILED);
				}

			}
		}
		return apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK);
	}

	@Override
	public void updateCustomer(@Valid Customer user, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		if (!customerRepository.existsByMobileNumber(user.getMobileNumber())) {
			apiResponseDtoBuilder.withMessage(Constants.NO_USER_EXISTS).withStatus(HttpStatus.OK).withData(user);
			return;
		}
		String newPasswordEncodedString = bCryptPasswordEncoder.encode(user.getMobileNumber());
		user.setPassword(newPasswordEncodedString);
		user.setUpdatedAt(new Date());
		saveCustomer(user);
		apiResponseDtoBuilder.withMessage(Constants.USER_UPDATED_SUCCESSFULLY).withStatus(HttpStatus.OK).withData(user);
	}

	@Override
	public Customer findCustomerById(Long id) {
		Optional<Customer> customer = customerRepository.findById(id);
		return customer.isPresent() ? customer.get() : null;
	}

	@Override
	public void saveCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public Driver findDriverById(Long id) {
		Optional<Driver> driver = driverRepository.findById(id);
		return driver.isPresent() ? driver.get() : null;
	}

	@Override
	public void saveDriver(Driver driver) {
		driverRepository.save(driver);
	}

	@Override
	public Admin findAdminByMobileNumberOrEmail(String username, String username2) {
		return adminRepository.findByMobileNumberOrEmail(username, username2);
	}

	@Override
	public Admin findAdminByEmail(String username) {
		return adminRepository.findByEmail(username);
	}

	@Override
	public User findByMobileNumberOrEmailAndPassword(String username, String username2, String password) {
		return userRepository.findByMobileNumberOrEmailAndPassword(username, username2, password);
	}

	@Override
	public void forgotPassword(ApiResponseDtoBuilder apiResponseDtoBuilder, String mobile, String password) {
		User dbuser = userRepository.findByMobileNumber(mobile);
		if (dbuser != null) {
			if (dbuser.getRole() == 2) {
				Optional<Customer> customer = customerRepository.findById(dbuser.getId());
				customer.ifPresent(cstmr -> {
					cstmr.setPassword(bCryptPasswordEncoder.encode(password));
					saveCustomer(cstmr);
				});
			} else if (dbuser.getRole() == 3) {
				Optional<Driver> driver = driverRepository.findById(dbuser.getId());
				driver.ifPresent(drvr -> {
					drvr.setPassword(bCryptPasswordEncoder.encode(password));
					saveDriver(drvr);
				});
			}
			apiResponseDtoBuilder.withMessage("Password changed successfully").withStatus(HttpStatus.OK);
		} else {
			apiResponseDtoBuilder.withMessage("No User Found").withStatus(HttpStatus.NO_CONTENT);
		}

	}

}