package project.mealPlan.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import project.mealPlan.entity.InitializationStatus;
import project.mealPlan.entity.Unit;
import project.mealPlan.entity.User;
import project.mealPlan.entity.WeekDay;
import project.mealPlan.repository.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;
import project.mealPlan.service.RecipeService;
import project.mealPlan.service.UnitService;
import project.mealPlan.service.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;

@Component
public class DatabaseSeeder implements ApplicationRunner {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private WeekDayRepository weekDayRepository;
    @Autowired
    private InitializationStatusRepository initializationStatusRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InitializationStatus status = initializationStatusRepository.findById(1L).orElse(new InitializationStatus());

        if (!status.isOperationsExecuted()) {
            try {
                addUserFile();
                addRecipeFile();
                addWeekDaysFile();
                addUnitFile();
                addImagesFile();
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
/*
    @Transactional
    public ResponseEntity<?> addImagesFile() {
        try {
            File imgFolder = ResourceUtils.getFile("classpath:images");
            File[] imageFilesArray = imgFolder.listFiles();
            if (imageFilesArray != null && imageFilesArray.length > 0) {
                Arrays.sort(imageFilesArray, Comparator.comparing(File::getName));
                List<File> imageFiles = new ArrayList<>(List.of(imageFilesArray));
                for (int i = 0; i < imageFiles.size(); i++) {
                    Integer recipeId = i + 1; // Numeracja od 1
                    String fileName = String.valueOf(i + 1);
                    File imageFile = imageFiles.get(i);
                    byte[] imageData = Files.readAllBytes(imageFile.toPath());
                    MultipartFile multipartFile = new MockMultipartFile("file", fileName, "image/jpg", imageData);

                    System.out.println("Sprawdzanie " + fileName + "    " + recipeId);
                    System.out.println("Path: " + imageFile.getAbsolutePath());

                    ResponseEntity<?> response = recipeService.addRecipeImage(recipeId, multipartFile);
                    System.out.println("Response: " + response);
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("Images added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No images found in the 'img' directory.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding images: " + e.getMessage());
        }
    }*/

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
                    // Usuń rozszerzenie pliku (np. ".jpg") i pozostaw tylko cyfry
                    String recipeNumber = fileName.replaceAll("[^\\d]", "");
                    Integer recipeId = Integer.parseInt(recipeNumber);

                    byte[] imageData = Files.readAllBytes(imageFile.toPath());
                    MultipartFile multipartFile = new MockMultipartFile("file", fileName, "image/jpg", imageData);

                    System.out.println("Sprawdzanie " + fileName + "    " + recipeId);
                    System.out.println("Path: " + imageFile.getAbsolutePath());

                    ResponseEntity<?> response = recipeService.addRecipeImage(recipeId, multipartFile);
                    System.out.println("Response: " + response);
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



}


