package org.balikin.service.impl;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.balikin.dto.*;
import org.balikin.entity.Auth;
import org.balikin.repository.AuthRepository;
import org.balikin.service.AuthService;
import org.balikin.util.AESUtil;
import org.balikin.util.JwtUtil;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ApplicationScoped
public class AuthServiceImpl implements AuthService {
    @Inject
    AuthRepository authRepository;
    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    AESUtil aesUtil;

    @Inject
    JwtUtil jwtUtil;
    public void register (RegisterDto registerDto) throws Exception {
        if (authRepository.isEmailExist(registerDto.email)) {
            throw new BadRequestException("Email already exist");
        }
        if (registerDto.username.isEmpty() || registerDto.password.isEmpty() || registerDto.email.isEmpty()) {
            throw new BadRequestException("Username or Email Or Password is invalid");
        }
        if (!registerDto.email.contains("@") || !registerDto.email.contains(".")) {
            throw new NotAcceptableException("Invalid email format");
        }
        String regex = "^(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(registerDto.password);
        if (!matcher.matches()) {
            throw new BadRequestException("Password is invalid");
        }
        Auth user = new Auth();
        String hashPassword = aesUtil.encrypt(registerDto.password);
        user.setEmail(registerDto.email);
        user.setPassword(hashPassword);
        user.setUsername(registerDto.username);
        authRepository.register(user);
    }
    public LoginResponseDto login (LoginDto loginDto) throws Exception {
        Optional<Auth> userOptional = authRepository.FindByEmail(loginDto.getEmail());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        String hashPassword = aesUtil.decrypt(userOptional.get().getPassword());

        if (!hashPassword.equals(loginDto.getPassword())) {
            throw new BadRequestException("Username Or Password Wrong!");
        }
        String token = jwtUtil.generateJwt(userOptional.get().getEmail(), userOptional.get().getUsername(),userOptional.get().getId());
        return new LoginResponseDto(token, userOptional.get().getUsername(), "Login Success");
    }

    @Override
    public void updateProfile(UploadFileDto urlImage) throws Exception {
        String id =  securityIdentity.getPrincipal().getName();
        Auth user = Optional.ofNullable(authRepository.findById(id))
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setImageUrl(urlImage.getUrlImage());
        authRepository.persist(user);

    }

    @Override
    public ProfileDto getProfile() throws Exception {
        String id =  securityIdentity.getPrincipal().getName();
        log.info("ID {}", id);
        Auth user = authRepository.findById(id);
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(user.getUsername());
        profileDto.setEmail(user.getEmail());
        profileDto.setImageUrl(user.getImageUrl());
        return profileDto;
    }
}
