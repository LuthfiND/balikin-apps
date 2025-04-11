package org.balikin.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.balikin.dto.TransactionDto;
import org.balikin.service.transaction.TransactionService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Path("transaction")
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @Inject
    private TransactionService transactionService;

    @GET
    @Path("filter")
    public Response filterTransaction(
        @QueryParam("type") String type,
        @QueryParam("sort") @DefaultValue("asc") String sort,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate){

        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

            List<TransactionDto> transactions = transactionService.filterTransactions(type, sort, start, end);
            return Response.ok(transactions).build();
        } catch (Exception e) {
            log.error("Error filtering transactions: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid request parameters")
                    .build();
        }
    }
}
