package com.source.backend.endpoint;


import com.source.backend.Dto.AccountInfoDto;
import com.source.backend.model.Account;
import com.source.backend.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/acc")
@AllArgsConstructor
public class AccountEndpoint {

    private final AccountService accountService;

    @ResponseBody
    @RequestMapping("/{username}")
    private ResponseEntity<AccountInfoDto> getAccountInfo(@PathVariable String username){
        String usernameCheck = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (usernameCheck.equals(username)){
            return new ResponseEntity(accountService.getInfo(username), HttpStatus.OK);
        }
        else {
            return new ResponseEntity("Now allowed", HttpStatus.FORBIDDEN);
        }
    }
    @ResponseBody
    @GetMapping("/role")
    private ResponseEntity getAccountRoles(){
        try
        {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.getRole());
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
