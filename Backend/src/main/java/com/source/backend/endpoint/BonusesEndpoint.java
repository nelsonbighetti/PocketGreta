package com.source.backend.endpoint;

import com.source.backend.Dto.BonusesDto;
import com.source.backend.service.BonusesService;
import com.sun.mail.iap.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/bonuses")
@AllArgsConstructor
public class BonusesEndpoint {
    private final BonusesService bonusesService;

    @PostMapping("/code")
    @ResponseBody
    private ResponseEntity<String> bonuses(@RequestBody BonusesDto bonusesDto){
        try {
                return new ResponseEntity(bonusesService.setBonusesForAccount(bonusesDto.getCode()), HttpStatus.OK);
        }
        catch (Exception e)
        {
                return new ResponseEntity("try new code", HttpStatus.FORBIDDEN);
        }
    }
}
