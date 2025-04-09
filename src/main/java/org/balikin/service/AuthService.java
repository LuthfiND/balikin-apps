package org.balikin.service;
import org.balikin.dto.*;

public interface AuthService {
    public void register(RegisterDto registerDto) throws Exception;
    public LoginResponseDto login(LoginDto loginDto) throws Exception;
    public void updateProfile(UploadFileDto urlImage) throws Exception;
    public ProfileDto getProfile() throws Exception;

}
