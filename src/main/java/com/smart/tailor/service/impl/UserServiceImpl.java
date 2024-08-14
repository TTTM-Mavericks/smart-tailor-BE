package com.smart.tailor.service.impl;


import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Roles;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.UserMapper;
import com.smart.tailor.repository.UserRepository;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.service.TokenService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.service.VerificationTokenService;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VerificationTokenService verificationTokenService;
    private final TokenService tokenService;

    @Override
    public Optional<User> getUserDetailByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User registerNewUsers(UserRequest userRequest) {
        User savedUser = null;
        Optional<Roles> role = roleService.findRoleByRoleName(userRequest.getRoleName().trim().toUpperCase());
        if (!role.isPresent()) {
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ROLE + " " + userRequest.getRoleName());
        }

        if (!userRequest.getEmail().isEmpty() && !userRequest.getEmail().isBlank() && !userRequest.getPassword().isEmpty() && !userRequest.getPassword().isBlank()) {
            savedUser = userRepository.save(
                    User
                            .builder()
                            .email(userRequest.getEmail())
                            .password(userRequest.getPassword())
                            .language(userRequest.getLanguage())
                            .provider(userRequest.getProvider())
                            .userStatus(userRequest.getProvider().equals(Provider.LOCAL) ? UserStatus.INACTIVE : UserStatus.ACTIVE)
                            .fullName(userRequest.getFullName())
                            .phoneNumber(userRequest.getPhoneNumber())
                            .roles(role.get())
                            .imageUrl(userRequest.getImageUrl())
                            .build()
            );
        }
        return savedUser;
    }

    @Override
    public List<UserResponse> getAllUserResponse() {
        return userRepository.findAll().stream().map(this::convertToUserResponse).toList();
    }

    public UserResponse convertToUserResponse(User user) {
        return userMapper.mapperToUserResponse(user);
    }

    @Override
    public Boolean updateStatusAccount(String email, UserStatus userStatus) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setUserStatus(userStatus);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public User updateUserProfile(UserRequest userRequest) {
        User userExisted = userRepository.getByEmail(userRequest.getEmail());
        if (userExisted != null) {
            userExisted.setFullName(userRequest.getFullName());
            userExisted.setImageUrl(userRequest.getImageUrl());
            userExisted.setPhoneNumber(userRequest.getPhoneNumber());
            return userRepository.save(userExisted);
        }
        return null;
    }

    @Override
    public Optional<User> getUserByUserID(String String) {
        return userRepository.findById(String);
    }

    @Override
    public List<UserResponse> findAllUserByRoleName(RoleType roleType) {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getRoles().getRoleName().equals(roleType.name()))
                .sorted(
                        Comparator
                                .comparing(User::getLastModifiedDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .reversed()
                                .thenComparing(User::getCreateDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .reversed()
                )
                .map(userMapper::mapperToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllUnverifiedUser() {
        return userRepository
                .getAllUserWithEmailUnverified(false, TypeOfVerification.VERIFY_ACCOUNT.name())
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUnverifiedUser(String userID) {
        verificationTokenService.deleteVerificationTokenByUserID(userID);
        tokenService.deleteTokenByUserID(userID);
        userRepository.deleteUserByUserID(userID);
    }

    @Override
    public Float calculateNewCustomerGrowthPercentageForCurrentAndPreviousWeek() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentWeek = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        LocalDateTime endOfCurrentWeek = now;

        LocalDateTime startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1);
        LocalDateTime endOfPreviousWeek = startOfCurrentWeek.minusSeconds(1);

        var totalCustomer = userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().getRoleName().equals(RoleType.CUSTOMER.name()))
                .toList();

        var currentWeekCustomerCount = totalCustomer
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfCurrentWeek) && !createDate.isAfter(endOfCurrentWeek);
                })
                .count();

        var previousWeekCustomerCount = totalCustomer
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfPreviousWeek) && !createDate.isAfter(endOfPreviousWeek);
                })
                .count();

        if (previousWeekCustomerCount == 0) {
            return currentWeekCustomerCount > 0 ? 100.0f : 0.0f;
        }

        float growthPercentage = ((float) (currentWeekCustomerCount - previousWeekCustomerCount) / previousWeekCustomerCount) * 100.0f;

        return BigDecimal
                .valueOf(growthPercentage)
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();
    }

    @Override
    public Float calculateUserGrowthPercentageForCurrentAndPreviousMonth() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        YearMonth currentMonth = YearMonth.from(now);
        LocalDateTime startOfCurrentMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfCurrentMonth = now;

        YearMonth previousMonth = currentMonth.minusMonths(1);
        LocalDateTime startOfPreviousMonth = previousMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfPreviousMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        var totalUser = userRepository.findAll();

        var currentMonthUserCount = totalUser
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfCurrentMonth) && !createDate.isAfter(endOfCurrentMonth);
                })
                .count();

        var previousMonthUserCount = totalUser
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfPreviousMonth) && !createDate.isAfter(endOfPreviousMonth);
                })
                .count();

        if (previousMonthUserCount == 0) {
            return currentMonthUserCount > 0 ? 100.0f : 0.0f;
        }

        float growthPercentage = ((float) (currentMonthUserCount - previousMonthUserCount) / previousMonthUserCount) * 100.0f;

        return BigDecimal
                .valueOf(growthPercentage)
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();
    }
}
