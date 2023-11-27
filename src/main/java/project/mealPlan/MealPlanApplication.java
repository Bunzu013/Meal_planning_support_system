package project.mealPlan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"project.mealPlan.controller", "project.mealPlan.configuration", "project.mealPlan.service", "project.mealPlan.repository"})

public class MealPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealPlanApplication.class, args);
	}

}
