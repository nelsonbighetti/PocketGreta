package com.source.backend.endpoint;

import com.source.backend.Dto.AccountCodeRequest;
import com.source.backend.Dto.BonusesDto;
import com.source.backend.Dto.RegisterRequest;
import com.source.backend.service.BonusesService;
import com.sun.mail.iap.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/bonuses")
@AllArgsConstructor
public class BonusesEndpoint {
    private final BonusesService bonusesService;

    @PostMapping("/code")
    @ResponseBody
    public ResponseEntity<String> bonuses(@RequestBody BonusesDto bonusesDto) {
        try {
                return new ResponseEntity(bonusesService.setBonusesForAccount(bonusesDto.getCode()), HttpStatus.OK);
        }
        catch (Exception e)
        {
                return new ResponseEntity("try new code", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/history")
    @ResponseBody
    public ResponseEntity getBonusesHistory() {
        try {
            return new ResponseEntity(bonusesService.bonusesHistory(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/addcode")
    @ResponseBody
    public ResponseEntity addCode(@Valid @RequestBody AccountCodeRequest accountCodeRequest) {
        try {
            bonusesService.addCode(accountCodeRequest.getCode(), accountCodeRequest.getBonuses());
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("can't add code");
        }
    }
}
