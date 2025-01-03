package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.Recipe_Ingredient;
import project.mealPlan.entity.Unit;
import project.mealPlan.repository.Recipe_IngredientRepository;
import project.mealPlan.repository.UnitRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UnitService {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    Recipe_IngredientRepository recipe_IngredientRepository;

    public ResponseEntity<?> getAllUnits()
    {
        try {
            List<Map<String,Object>> unitInfoList = new ArrayList<>();
            List<Unit> units = unitRepository.findAll();
            for(Unit unit : units)
            {
                Map<String,Object> unitInfo = new HashMap<>();
                unitInfo.put("unitId", unit.getUnitId());
                unitInfo.put("unitName", unit.getUnitName());
                unitInfoList.add(unitInfo);
            }
            return new ResponseEntity<>(unitInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addNewUnit(Map<String, Object> unitInput)
    {
        try {
            String unitName = (String) unitInput.get("unitName");
            if(unitRepository.findByUnitName(unitName) == null){
                Unit newUnit = new Unit(unitName);
                unitRepository.save(newUnit);
                return new ResponseEntity<>("New unit added", HttpStatus.OK);
            }
            return new ResponseEntity<>("Unit already exists", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> updateUnit(Map<String, Object> unitInput)
    {
        try {
            Integer unitId = (Integer) unitInput.get("unitId");
            String unitName = (String) unitInput.get("unitName");
            if(unitRepository.findByUnitName(unitName) == null) {
                if (unitId != null) {
                    Unit unitUpdate = unitRepository.findByUnitId(unitId);
                    if (unitUpdate != null) {
                        unitUpdate.setUnitName(unitName);
                        unitRepository.save(unitUpdate);
                        return new ResponseEntity<>("Unit updated", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("No unit found", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }else{
                return new ResponseEntity<>("Unit with this name already exists", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Error updating unit", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteUnit(Map<String, Object> unitInput)
    {
        try {
            Integer unitId = (Integer) unitInput.get("unitId");
            if (unitId != null) {
                Unit unit = unitRepository.findByUnitId(unitId);
                if (unit != null) {
                    List<Recipe_Ingredient> recipes = recipe_IngredientRepository.findByUnit(unit) ;
                    Unit unitTemp = unitRepository.findByUnitName("notKnow");
                    for (Recipe_Ingredient recipe : recipes) {
                        recipe.setUnit(unitTemp);
                    }
                    unitRepository.delete(unit);
                    return new ResponseEntity<>("unit deleted", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No unit found", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>("Error deleting unit", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
