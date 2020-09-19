package com.source.backend.endpoint;

import com.source.backend.service.MapService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestController
@RequestMapping("rest/map")
@AllArgsConstructor
public class MapEndpoint {
    private final MapService mapService;

    @GetMapping("/{trashType}")
    @ResponseBody
    private ResponseEntity trashcanRequest(@PathVariable String trashType){

        try{
            return ResponseEntity.ok(mapService.getCoordinates(trashType));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/all")
    @ResponseBody
    private ResponseEntity allEcoUnit(){
        try{
            return ResponseEntity.ok(mapService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
