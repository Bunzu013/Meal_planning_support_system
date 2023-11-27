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
public class UserControllerIntegrationTesting {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void testGetMealPlanDetails() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/getMealPlanDetails")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //test zwracajacy konflikt i przekazanie danych typu map
    @Test
    public void testUpdatePassword() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        Map<String, Object> data = new HashMap<>();
        data.put("oldPassword", "123456789");
        data.put("newPassword", "123456789");
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);
       mockMvc.perform(MockMvcRequestBuilders.post("/user/updatePassword")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
    @Test
    public void testAddNewMeal() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        int weekDayId = 1;
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/addNewMeal")
                        .param("weekDayId", String.valueOf(weekDayId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //błąd bo user nie jest wlascicielem przepisu
    @Test
    public void testDeleteUserRecipe() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        int recipeId = 1;
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/deleteUserRecipe")
                        .param("recipeId", String.valueOf(recipeId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}


