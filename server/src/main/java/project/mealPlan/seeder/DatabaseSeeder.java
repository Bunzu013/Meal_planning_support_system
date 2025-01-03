package project.mealPlan.seeder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.apache.commons.fileupload.disk.DiskFileItem;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import project.mealPlan.entity.*;
import project.mealPlan.repository.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import project.mealPlan.service.RecipeService;
import project.mealPlan.service.UserService;
import javax.transaction.Transactional;
import java.io.IOException;

@Component
public class DatabaseSeeder implements ApplicationRunner {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    WeekDayRepository weekDayRepository;
    @Autowired
    InitializationStatusRepository initializationStatusRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    UserRepository userRepository;
@Autowired
   RoleRepository roleRepository;
    @Override
    public void run(ApplicationArguments args) {
        InitializationStatus status = initializationStatusRepository.findById(1L).orElse(new InitializationStatus());
        initializationStatusRepository.save(status);
        if (!status.isOperationsExecuted()) {
            try {
                addRole();
                addUserFile();
                addWeekDaysFile();
                addUnitFile();
                addIngredientFile();
                addImagesFile();
                addRecipeFile();
                userService.setAdmin();
                status.setOperationsExecuted(true);
                initializationStatusRepository.save(status);
                System.out.println("Data added successfully");
            } catch (IOException e) {
                System.err.println("Error adding data");
            }
        }
    }


    @Transactional
    public ResponseEntity<?> addUserFile() throws IOException {
        try {
            Resource resourceUsers = resourceLoader.getResource("classpath:seeds/userSeed.json");
            List<User> users = null;
            if (resourceUsers.exists()) {
                File fileUsers = resourceUsers.getFile();
                users = objectMapper.readValue(
                        fileUsers, new TypeReference<List<User>>() {
                        }
                );
            }
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    userService.addUser(user);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Users added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding data");
        }
    }

    @Transactional
    public ResponseEntity<?> addRole() {
        try {
            UserRole defaultRole = roleRepository.findByAuthority("ROLE_ADMIN");
            if (defaultRole == null) {
                defaultRole = new UserRole("ROLE_ADMIN");
                roleRepository.save(defaultRole);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Role added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user roles");
        }
    }

    @Transactional
    public ResponseEntity<?> addRecipeFile() throws IOException {
        try {
            Resource resource = resourceLoader.getResource("classpath:seeds/recipeSeed.json");
            List<Map<String, Object>> recipeInputs = null;
            if (resource.exists()) {
                File file = resource.getFile();
                recipeInputs = objectMapper.readValue(
                        file, new TypeReference<List<Map<String, Object>>>() {
                        }
                );
            }
            if (recipeInputs != null && !recipeInputs.isEmpty()) {
               for (Map<String, Object> recipeInput : recipeInputs) {
                    recipeService.addRecipeAdmin(recipeInput);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Recipes added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding data");
        }
    }

    @Transactional
    public ResponseEntity<?> addWeekDaysFile() throws IOException {
        try {
            Resource resource = resourceLoader.getResource("classpath:seeds/weekDaySeed.json");
            List<WeekDay> weekDays = null;
            if (resource.exists()) {
                File weekDayFile = resource.getFile();
                weekDays = objectMapper.readValue(
                        weekDayFile, new TypeReference<List<WeekDay>>() {
                        }
                );
            }
            if (weekDays != null && !weekDays.isEmpty()) {
                for (WeekDay weekDay : weekDays) {
                    weekDayRepository.save(weekDay);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Week days added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding data");
        }
    }

    @Transactional
    public ResponseEntity<?> addUnitFile() throws IOException {
        try {
            Resource resource = resourceLoader.getResource("classpath:seeds/unitSeed.json");
            List<Unit> units = null;
            if (resource.exists()) {
                File unitFile = resource.getFile();
                units = objectMapper.readValue(
                        unitFile, new TypeReference<List<Unit>>() {
                        }
                );
            }
            if (units != null && !units.isEmpty()) {
                for (Unit unit : units) {
                    unitRepository.save(unit);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Week days added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding data");
        }
    }
    @Transactional
    public ResponseEntity<?> addImagesFile() {
        try {
            File imgFolder = ResourceUtils.getFile("classpath:images");
            File[] imageFilesArray = imgFolder.listFiles();
            if (imageFilesArray != null && imageFilesArray.length > 0) {
                Arrays.sort(imageFilesArray, Comparator.comparing(File::getName));
                List<File> imageFiles = new ArrayList<>(List.of(imageFilesArray));
                for (File imageFile : imageFiles) {
                    String fileName = imageFile.getName();
                    String recipeNumber = fileName.replaceAll("[^\\d]", "");
                    Integer recipeId = Integer.parseInt(recipeNumber);
                    byte[] imageData = Files.readAllBytes(imageFile.toPath());
                    MultipartFile multipartFile = new MockMultipartFile("file", fileName, "image/jpg", imageData);
                    recipeService.addRecipeImage(recipeId, multipartFile);
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("Images added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No images found in the 'img' directory.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding images: " + e.getMessage());
        }
    }


    @Transactional
    public ResponseEntity<?> addIngredientFile() throws IOException {
        try {
            Resource resource = resourceLoader.getResource("classpath:seeds/ingredientSeed.json");
            List<Ingredient> ingredients = null;
            if (resource.exists()) {
                File ingredientFile = resource.getFile();
                ingredients = objectMapper.readValue(
                        ingredientFile, new TypeReference<List<Ingredient>>() {
                        }
                );
            }
            if (ingredients != null && !ingredients.isEmpty()) {
                for (Ingredient ingredient : ingredients) {
                    ingredientRepository.save(ingredient);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Week days added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding data");
        }
    }

}


