package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.Filter;
import project.mealPlan.entity.Recipe;
import project.mealPlan.repository.FilterRepository;
import project.mealPlan.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterService {


@Autowired
    FilterRepository filterRepository;
@Autowired
    RecipeRepository recipeRepository;
    public ResponseEntity<?> getAllFilters()
    {
        try {
            List<Map<String,Object>> allFiltersList = new ArrayList<>();
            List<Filter> filters= filterRepository.findAll();
            for(Filter filter : filters)
            {
                Map<String,Object> filterInfo = new HashMap<>();
                filterInfo.put("filterId", filter.getRecipeFilterId());
                filterInfo.put("filterName", filter.getRecipeFilterName());
                allFiltersList.add(filterInfo);
            }
            return new ResponseEntity<>(allFiltersList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?>  addNewFilter(Map<String, Object> filterInput)
    {
        try {
            String filter = (String) filterInput.get("filterName");
            if(filterRepository.findByRecipeFilterName(filter) == null){
                Filter newFilter = new Filter(filter);
                filterRepository.save(newFilter);
                return new ResponseEntity<>("New filter added", HttpStatus.OK);
            }
            return new ResponseEntity<>("Filter already exists", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> updateFilter(Map<String, Object> filterInput)
    {
        try {
            Integer filterId = (Integer) filterInput.get("filterId");
            String filterName = (String) filterInput.get("filterName");
            if(filterRepository.findByRecipeFilterName(filterName) == null) {
                if (filterId != null) {
                    Filter filterUpdate = filterRepository.findByRecipeFilterId(filterId);
                    if (filterUpdate != null) {
                        filterUpdate.setRecipeFilterName(filterName);
                        filterRepository.save(filterUpdate);
                        return new ResponseEntity<>("Filter updated", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("No filter found", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }else{
                return new ResponseEntity<>("Filter with this name already exists", HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>("Error updating filter", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteFilter(Map<String, Object> filterInput)
    {
        try {
            Integer filterId = (Integer) filterInput.get("filterId");
            if (filterId != null) {
                Filter filter = filterRepository.findByRecipeFilterId(filterId);
                if (filter != null) {
                    List<Recipe> recipes = recipeRepository.findByRecipeFilters(filter);
                    for (Recipe recipe : recipes) {
                        recipe.getRecipeCategories().remove(filter);
                    }
                    filterRepository.delete(filter);
                    return new ResponseEntity<>("Filter deleted", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No filter found", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>("Error deleting filter", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
