package org.balikin.resource;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.balikin.dto.*;
import org.balikin.model.ApiResponse;
import org.balikin.service.AuthService;
import org.balikin.service.impl.AuthServiceImpl;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.Map;


@Slf4j
@Path("auth")
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    private AuthServiceImpl authServiceImpl;
    @POST
    @Transactional
    @Path("register")
    @Operation(summary = "Register Service For Balikin App", description = "Service ini akan Menggunakan Service Register")
    @APIResponse(responseCode = "201",description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class)))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegisterDto registerDto) throws Exception {
        authServiceImpl.register(registerDto);
        return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "Successfully Registered"))
                .build();
    }
    @POST
    @Transactional
    @Path("login")
    @Operation(summary = "Login Service For Balikin App", description = "Service ini akan Menggunakan Service Login")
    @APIResponse(responseCode = "200",description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LoginResponseDto.class)))
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<LoginResponseDto> login(LoginDto loginDto) throws Exception {
        log.info("Masuk sini {}", loginDto.getEmail());
        LoginResponseDto response = authServiceImpl.login(loginDto);
        return new ApiResponse<LoginResponseDto>("Login Success",response , 200);
    }
    @POST
    @Transactional
    @Path("update-profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<String> updateProfile (UploadFileDto urlImage) throws Exception {
        authServiceImpl.updateProfile(urlImage);
        return new ApiResponse<String>("Success Update Profile", null , 200);

    }
    @GET
    @Path("profile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<ProfileDto> getProfile() throws Exception {
        ProfileDto user = authServiceImpl.getProfile();
        return new ApiResponse<ProfileDto>("Success Get Profile", user , 200);
    }


}
