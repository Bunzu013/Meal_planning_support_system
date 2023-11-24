package project.mealPlan.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.mealPlan.entity.Unit;
import project.mealPlan.repository.UnitRepository;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitService unitService;
    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void getAllUnits_success() {
        List<Unit> units = new ArrayList<>();
        units.add(new Unit("Unit1"));
        units.add(new Unit("Unit2"));
        when(unitRepository.findAll()).thenReturn(units);
        ResponseEntity<?> responseEntity = unitService.getAllUnits();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateUnit_success() {
        Integer unitId = 1;
        String unitName = "UpdatedUnit";
        Unit existingUnit = new Unit("ExistingUnit");
        when(unitRepository.findByUnitId(unitId)).thenReturn(existingUnit);
        when(unitRepository.findByUnitName(unitName)).thenReturn(null);
        java.util.Map<String, Object> unitInput = new java.util.HashMap<>();
        unitInput.put("unitId", unitId);
        unitInput.put("unitName", unitName);
        ResponseEntity<?> responseEntity = unitService.updateUnit(unitInput);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
}
