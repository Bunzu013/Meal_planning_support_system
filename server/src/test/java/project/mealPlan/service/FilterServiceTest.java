package project.mealPlan.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.mealPlan.entity.Filter;
import project.mealPlan.repository.FilterRepository;
@ExtendWith(SpringExtension.class)
public class FilterServiceTest {

    @Mock
    private FilterRepository filterRepository;

    @InjectMocks
    private FilterService filterService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void addNewFilter_conflict() {
        Map<String, Object> filterInput = new HashMap<>();
        filterInput.put("filterName", "ExistingFilter");
        when(filterRepository.findByRecipeFilterName("ExistingFilter")).thenReturn(new Filter("ExistingFilter"));
        ResponseEntity<?> responseEntity = filterService.addNewFilter(filterInput);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Filter already exists", responseEntity.getBody());
        verify(filterRepository, never()).save(any(Filter.class));
    }

}
