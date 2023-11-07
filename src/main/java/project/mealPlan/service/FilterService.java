package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.Filter;
import project.mealPlan.repository.FilterRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterService {


@Autowired
    FilterRepository filterRepository;
    public ResponseEntity<?> getAllFilters()
    {
        try {
            List<Map<String,Object>> allFiltersList = new ArrayList<>();
            List<Filter> filters= filterRepository.findAll();
            for(Filter filter : filters)
            {
                Map<String,Object> filterInfo = new HashMap<>();
                filterInfo.put("filterName", filter.getRecipeFilterName());
                allFiltersList.add(filterInfo);
            }
            return new ResponseEntity<>(allFiltersList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
