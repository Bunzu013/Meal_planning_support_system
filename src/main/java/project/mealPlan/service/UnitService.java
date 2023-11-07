package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.Ingredient;
import project.mealPlan.entity.Unit;
import project.mealPlan.repository.UnitRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UnitService {
    @Autowired
    UnitRepository unitRepository;

    public ResponseEntity<?> getAllUnits()
    {
        try {
            List<Map<String,Object>> unitInfoList = new ArrayList<>();
            List<Unit> units = unitRepository.findAll();
            for(Unit unit : units)
            {
                Map<String,Object> unitInfo = new HashMap<>();
                unitInfo.put("unitName", unit.getUnitName());
                unitInfoList.add(unitInfo);
            }
            return new ResponseEntity<>(unitInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
