## Table of contents
* [General info](#general-info)
* [Functional Requirements](#functional-requirements)
* [Technologies](#technologies)
* [Project structure](#project-structure)
* [Database preview](#database-preview)
* [Aplication preview](#aplication-preview)
* [Setup](#setup)

# General info
<div>
<p align="justify"> 
 A system designed to help users maintain a healthy and balanced diet based on the user's preferred ingredients and allergens. 
 User has the opportunity to add recipes to their own meal plan and to generate a grocery shopping list. This repository contains only server side of the project and database (with seeder). 
</p>
</div>

# Functional Requirements
### User Features
- **Logging in**
- **Registration**
- **Password recovery**
- **Editing personal information**
- **Selecting preferred ingredients**
- **Editing preferred ingredients**
- **Selecting allergens**
- **Editing selected allergens**
- **Viewing the weekly meal plan**
- **Adding meals to the weekly meal plan**
- **Editing meals in the weekly meal plan**
- **Deleting meals from the weekly meal plan**
- **Browsing recipes**
- **Browsing recommended recipes**
- **Filtering recipes**
- **Hiding recipe caloric information**
- **Adding recipes to the favorites list**
- **Removing recipes from the favorites list**
- **Adding personal recipes**
- **Editing personal recipes**
- **Deleting personal recipes**
- **Adding recipes to meals in the meal plan**
- **Removing recipes from meals in the meal plan**
- **Generating a shopping list**
- **Changing the status of the shopping list**

### Admin Features
- **All user functionalities**
- **Managing categories**
- **Managing filters**
- **Managing units**
- **Managing recipes**
- **Editing the list of ingredients**
- **Managing users (deleting, adding, blocking, unblocking user accounts)**

# Technologies
Java, JPA, Maven, REST API, Spring Boot, MySQL, InteliJ

# Project structure
<p align="center">    
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/e50f03f2-81a9-459e-ad04-8d5f222eb200" alt="proj 1"/>
</p>

# Database preview
<p align="center">
   <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/740e23d6-9b43-4a34-a127-1c0729577691" alt="database 1" />
 </p>
<p align="center">
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/086184d6-cac2-41a4-b798-9b3e4064cae2" alt="database 1" width="250"/>
    <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/5fd7637d-2914-4fea-b385-49ee354cd8d7" alt="database 2" width="250"/>
    <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/aed0a2ad-4ab2-4a52-8f6d-6a74b649e570" alt="database 3" width="400"/>
</p>


# Aplication preview 
#### Front-end made by co-autors
### Home page for guest user
![2](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/d3431434-273e-4267-ab57-7a8e00fa2cc8)
 
### Sign up page 
![30](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/ee72276d-31cd-49b6-a8d2-b5e20f6f96a0)

### Sign in page 
<p align="center">    
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/83552d33-400c-46a9-9d44-95f4ad2096b8" alt="sigIn 1"/>
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/73f3b5e2-194b-4d3e-b1e1-2f9cee4c7aa4" alt="sigIn 2" width="400"/>

</p>

### Home page for logged user 
![21](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/90aa272b-2a16-47cf-98f5-52b89c4f8c3d)

### Recipes search page
##### Recipes with ingredients that are marked as user allergens are not on disply without user consent
![5](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/fec5aec2-b3a8-458b-a628-9a1bae127c07)

### Recipe details page
![18](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/d8293437-d428-4f5d-aa9b-4ba5d1b5f8a0)
### Recipe details if user is the owner of the recipe 
![23](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/0a9205d8-fdef-4fdd-b9dc-3258169c0fc0)

### Recipes search with choosen categiories and filters
![6](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/bbf448a8-3786-422a-8432-e52ef776c111)

### Add recipe to meal plan window
##### User can choose day and meal to which they want to add recipe
<p align="center">    
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/8517aeb7-52cd-444d-bfac-f9921dff5ecf" alt="window 1" width="400"/>
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/70eab40e-6091-4ddb-8aac-b00bc594d1bc" alt="window 2" width="400"/>
</p>

### Meal plan page
![7](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/08e14993-c0d8-450b-99d4-6522ff9e9f36)

### Added new meal
![8](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/b26b8d62-c64a-47f7-b591-d808e7dcb836)

### Added recipes to meal
![9](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/151c2388-5d72-4c53-a676-6f712970c7af)

### Generate shopping list page 
#### Shopping list is based on the meal plan and sums up the amounts of ingredients
<p align="center">    
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/2eeaa59e-5154-4461-ae05-ce48e4de5578" alt="shopp 1"/>
</p>

![20](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/9ec79d68-1f09-446e-a8ca-078605264318)

### User profile page
![10](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/f52de181-37a8-4c5a-81c3-620d743ba1b9)

### Edit user's data page
![14](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/e0b039ef-4813-44e7-bdeb-f90203ff6881)

### User allergens page
![11](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/83eae3f9-3b2e-41ca-9e0b-bc52e1e55149)

### User prefered ingedients page
![12](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/e8133cc4-6b42-47e6-a6d6-e9e338b0e984)

### User's "My Recipes" page
![26](https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/203c42e3-a35b-4525-b830-6fb071baf5ee)

### Add new recipe page
<p align="center">    
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/4e1575a8-cb86-4745-8d9f-59618535b3c0" alt="edit 2"/>
</p>

### Edit recipe page
<p align="center">    
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/b7172c52-af5b-489c-8679-57ca452e6975" alt="edit 2"/>
</p>

### Admin's vs user's searching panel
<p align="center">    
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/0fe6a366-4a99-4ed1-b9ab-52b30c3d9c71" alt="panel 1" width="400"/>
  <img src="https://github.com/Bunzu013/Meal_planning_support_system/assets/83347605/e98f29a9-731d-4a43-923c-62b49a02fd5b" alt="panel 2" width="400"/>
</p>

### Rest of admin's side funcionalities are avilable only on back-end (no front-end was created)

# Setup
#### Used ports:
```
front-end:3000
back-end: 8080
database: 3306
```

#### 1. Create database with name: mealplan
###### If there is a problem with connection, you probably need to add spring.datasource.password to serwer\src\main\resources\application.properties 
#### 2. Server side
```
$ mvn clean install
$ mvn spring-boot:run
```
#### Test user login data:
```
email: test@gmail.com
password: 123456789
```
#### Admin login data:
```
email: admin@gmail.com
password: 123456780
```
