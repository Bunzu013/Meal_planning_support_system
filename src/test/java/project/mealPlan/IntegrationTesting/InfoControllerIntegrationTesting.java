package project.mealPlan.IntegrationTesting;
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
@SpringBootTest
@AutoConfigureMockMvc
public class InfoControllerIntegrationTesting {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //punkt koncowy z autoryzacja
    @Test
    public void testGetAllCategories() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/getAllCategories")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    //punkt koncowy bez autoryzacji
    @Test
    public void testGetAllRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/getAllRecipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //punkt koncowy z autoryzacja i parametrem
    @Test
    public void testGetRecipeData() throws Exception {
        User testUser = JwtTestUtils.createSampleUser();
        String validJwtToken = jwtTokenUtil.generateJwtToken(testUser);
        int recipeId = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/getRecipeData/{recipeId}", recipeId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
