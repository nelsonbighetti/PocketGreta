package com.source.backend.repository;

import com.source.backend.model.EcoUnit;
import com.source.backend.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface EcoUnitRepository extends JpaRepository<EcoUnit, Long> {

    public Set<EcoUnit> findByType(Type type);
    public List<EcoUnit> findAll();
}
