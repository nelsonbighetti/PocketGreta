package com.source.backend.service;


import com.source.backend.Dto.EcoUnitDto;
import com.source.backend.model.EcoUnit;
import com.source.backend.model.Type;
import com.source.backend.repository.EcoUnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class MapService {
    private final EcoUnitRepository ecoUnitRepository;

    public Set<EcoUnit> getCoordinates(String type) throws Exception{
        if (type.equals(Type.TRASHBIN.getType())) {
            return getTrashcans();
        }
        if (type.equals(Type.SHOP.getType())) {
            return getShops();
        }
        if (type.equals(Type.EVENT.getType())){
            return getEvents();
        }
        throw new Exception("Type is incorrect");
    }
    public Set<EcoUnit> getTrashcans(){
        return ecoUnitRepository.findByType(Type.TRASHBIN);
    }

    public Set<EcoUnit> getShops(){
        return ecoUnitRepository.findByType(Type.SHOP);
    }

    public Set<EcoUnit> getEvents(){
        return ecoUnitRepository.findByType(Type.EVENT);
    }

    public List<EcoUnit> getAll(){
        return ecoUnitRepository.findAll();
    }

    public void addEcoUnit(EcoUnitDto ecoUnitDto) throws Exception{
        EcoUnit ecoUnit = new EcoUnit();
        ecoUnit.setType(ecoUnitDto.getType());
        ecoUnit.setDescriptions(ecoUnitDto.getDescriptions());
        ecoUnit.setDetails(ecoUnitDto.getDetails());
        ecoUnit.setLongitude(ecoUnitDto.getLongitude());
        ecoUnit.setLatitude(ecoUnitDto.getLatitude());
        ecoUnit.setSubtype(ecoUnitDto.getSubtype());
        ecoUnitRepository.save(ecoUnit);
    }
    public void deleteEcoUnit(EcoUnitDto ecoUnitDto) throws Exception{
        EcoUnit ecoUnit = ecoUnitRepository.findById(ecoUnitDto.getId()).orElseThrow(Exception::new);
        ecoUnitRepository.delete(ecoUnit);
    }
}
