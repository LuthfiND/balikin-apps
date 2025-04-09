package org.balikin.resource;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.balikin.dto.BalikinUtangRequest;
import org.balikin.dto.TotalUtangPiutangDto;
import org.balikin.dto.UtangDto;
import org.balikin.dto.UtangRequestDto;
import org.balikin.entity.Utang;
import org.balikin.model.ApiResponse;
import org.balikin.repository.AuthRepository;
import org.balikin.service.impl.UtangPiutangServiceImpl;
import org.balikin.util.FormatRupiahUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;
import java.util.Optional;

@Slf4j
@Path("transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)


@RolesAllowed({"USER", "ADMIN"})
public class UtangPiutangResource {
    @Inject
    UtangPiutangServiceImpl utangPiutangServiceImpl;
    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    AuthRepository authRepository;
    @Inject
    FormatRupiahUtil formatRupiahUtil;
    @POST
    @Path("/save")
    @Transactional
    @Operation(summary = "Post Data utang Service For Balikin App", description = "Service ini akan Menggunakan Service Register")
    @APIResponse(responseCode = "200",description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public ApiResponse<UtangRequestDto> save(UtangRequestDto utangRequestDto, @Context SecurityContext securityContext) {
        try{
            String id = securityIdentity.getPrincipal().getName();
            Optional<Utang> savedUtang = utangPiutangServiceImpl.saveDataUtang(utangRequestDto, id);
            if (savedUtang.isEmpty()) {
                throw new RuntimeException("Gagal menyimpan data utang");
            }
            Utang utang = savedUtang.get();
            UtangRequestDto responseDto = new UtangRequestDto();
            responseDto.setNameUtang(utang.getName_utang());
            responseDto.setAmount(utang.getAmount());
            responseDto.setTransactionType(Utang.TransactionType.valueOf(String.valueOf(utang.getTransactionType())));
            responseDto.setDescription(utang.getDescription());
            responseDto.setReceivedEmail(utang.getReceiver().getEmail());
            responseDto.setTransactionDate(utang.getTransactionDate());
            return new ApiResponse<>("Success Menyimpan Data", responseDto, 200);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GET
    @Path("/utang")
    @Transactional
    @APIResponse(responseCode = "200",description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @Operation(summary = "get Data utang Service For Balikin App", description = "Service ini akan Menggunakan Get Data Utang")

    public ApiResponse<List<UtangDto>> getAllUtang () {
        Optional<List<UtangDto>> allUtang = utangPiutangServiceImpl.getAllUtang();
        if (allUtang.isEmpty()) {
            return new ApiResponse<>("Data Kosong" , null , 200);
        } else {
            return new ApiResponse<List<UtangDto>>("success", allUtang.get(), 200);
        }

    }
    @POST
    @Path("/balikin")
    @Operation(summary = "get Data utang Service For Balikin App", description = "Service ini akan Menggunakan Balikin Data Utang")
    @APIResponse(responseCode = "200",description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public ApiResponse<String> balikinUtang (@RequestBody BalikinUtangRequest balikinUtangRequest) throws Exception {
        String result = utangPiutangServiceImpl.balikinUtang(balikinUtangRequest.emailReceived,balikinUtangRequest.paymentAmount, balikinUtangRequest.Id);
        return new ApiResponse<String>(result , null , 200);
    }
    @GET
    @Path("utang/{id}")
    public ApiResponse<UtangDto> getUtangById (@PathParam("id")  String id) throws Exception {
        UtangDto result = utangPiutangServiceImpl.getUtangById(id);
        return new ApiResponse<UtangDto>("success", result, 200);
    };

    @GET
    @Path("/total-utang-piutang")
    public ApiResponse<TotalUtangPiutangDto> getTotalUtangPiutang () throws Exception {
        TotalUtangPiutangDto totalUtang =  utangPiutangServiceImpl.getTotalUtangPiutang();
        return new ApiResponse<TotalUtangPiutangDto>("success", totalUtang, 200);
    }

}
