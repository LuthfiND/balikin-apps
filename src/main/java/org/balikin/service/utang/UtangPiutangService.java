package org.balikin.service.utang;

import jakarta.inject.Inject;
import org.balikin.dto.TotalUtangPiutangDto;
import org.balikin.dto.UtangDto;
import org.balikin.dto.UtangRequestDto;
import org.balikin.entity.Utang;
import org.balikin.repository.AuthRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface UtangPiutangService {
     Optional<Utang> saveDataUtang (UtangRequestDto utang , String Id) throws Exception;
     Optional<List<UtangDto>> getAllUtang () throws Exception;
     String balikinUtang (String email, Double paymentAmount, Long Id) throws Exception;
     UtangDto getUtangById (String id) throws Exception;
     TotalUtangPiutangDto getTotalUtangPiutang () throws Exception;
     void sendToInbox() throws Exception;


}
