package com.source.backend.endpoint;

import com.source.backend.Dto.EcoUnitDto;
import com.source.backend.service.MapService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestController
@RequestMapping("rest/map")
@AllArgsConstructor
public class MapEndpoint {

    private final MapService mapService;

    @GetMapping("/{trashType}")
    @ResponseBody
    public ResponseEntity trashcanRequest(@PathVariable String trashType){

        try{
            return ResponseEntity.ok(mapService.getCoordinates(trashType));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity allEcoUnit(){
        try{
            return ResponseEntity.ok(mapService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Admin')")
    @ResponseBody
    public ResponseEntity addEcoUnit(@RequestBody EcoUnitDto ecoUnitDto){
        try{
            mapService.addEcoUnit(ecoUnitDto);
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('Admin')")
    @ResponseBody
    public ResponseEntity deleEcoUnit(@RequestBody EcoUnitDto ecoUnitDto){
        try{
            mapService.deleteEcoUnit(ecoUnitDto);
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
