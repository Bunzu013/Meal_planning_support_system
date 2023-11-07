package project.mealPlan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MealPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealPlanApplication.class, args);
	}

}
