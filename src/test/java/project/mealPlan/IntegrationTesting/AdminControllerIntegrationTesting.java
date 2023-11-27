package project.mealPlan.IntegrationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.mealPlan.configuration.JwtTokenUtil;
import project.mealPlan.entity.User;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTesting {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //status OK ale tylko za pierwszym razem
    @Test
    public void testAddFilter() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        Map<String, Object> data = new HashMap<>();
        data.put("filterName", "newName");
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addNewFilter")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //status conflict
    @Test
    public void testUpdateIngredient() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        Map<String, Object> data = new HashMap<>();
        data.put("ingredientId", 1);
        data.put("ingredientName", "ground beef");
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/updateIngredient")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
//nie znaleziono kategorii
    @Test
    public void testDeleteCategory() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        Map<String, Object> data = new HashMap<>();
        data.put("categoryId", 1000);
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/deleteCategory")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
