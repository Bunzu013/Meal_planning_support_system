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
 User has the opportunity to add recipes to their own meal plan and to generate a grocery shopping list. This repository contains server side of the project, client side and database (with seeder). 
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
### Server
Java, JPA, Maven, REST API, Spring Boot, MySQL
### Client 
HTML, CSS, JavaScript, React.js

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
#### Front-end mostly made by co-autors
### Home page for guest user
<p align="center">
<img src="![1](https://github.com/user-attachments/assets/58db74ea-98bd-4c60-b882-9507ebc6128a)"/>
 </p>
 
### Sign up page 
![2](https://github.com/user-attachments/assets/92d844ec-4706-4a2e-98ad-fa2e1093af5b)

### Sign in page 
![3](https://github.com/user-attachments/assets/ab559f29-cf3f-4401-a26e-2a0053ba3821)

### Home page for logged user 
![4](https://github.com/user-attachments/assets/5d2ecccc-19e0-40b1-bfa4-9783f34de2da)

### Recipes search page
##### Recipes with ingredients that are marked as user allergens are not on disply without user consent
![5](https://github.com/user-attachments/assets/f3af3ac1-25f5-4b57-a2f4-c57388a86ca0)

### Recipes search with choosen categiories and filters
![8](https://github.com/user-attachments/assets/68b78add-6646-4ca5-bf51-b005509e3f8a)

### Recipe details page
![6](https://github.com/user-attachments/assets/b43c4a24-ffc3-4971-90cd-dd737ed05f99)

### Recipe details if user is the owner of the recipe 
![7](https://github.com/user-attachments/assets/69599f7b-aac5-4e38-9014-6690bb524996)

### Add recipe to meal plan window
##### User can choose day and meal to which they want to add recipe
![9](https://github.com/user-attachments/assets/10f19fa3-cc82-47b6-a636-33d2e3755b04)
![10](https://github.com/user-attachments/assets/bed8cf6f-ebd0-4f43-aee9-6d30eeaa664a)


### Meal plan page
![12](https://github.com/user-attachments/assets/6c1df007-9ca0-4036-ba9d-7aa75d560788)

### Generate shopping list page 
#### Shopping list is based on the meal plan and sums up the amounts of ingredients
![11](https://github.com/user-attachments/assets/375a35b9-71d5-4b24-b096-54269e83a42e)
![13](https://github.com/user-attachments/assets/81945f8d-f139-4514-879a-a5a6f42c23c5)

### User profile page
![16](https://github.com/user-attachments/assets/35ad436a-fb72-4ebb-9451-09da761711b5)

### Edit user's data page
![17](https://github.com/user-attachments/assets/c27146cf-bf7b-42bd-b821-3ff589d1ab74)

### User allergens page
![24](https://github.com/user-attachments/assets/05f6db9f-f699-4330-a8c8-b5f23909857f)

### User prefered ingedients page
![19](https://github.com/user-attachments/assets/2d3ed971-81c8-41cd-8e6e-e20dbb60fb72)

### User's "My Recipes" page
![20](https://github.com/user-attachments/assets/69305dd2-9937-4be9-941d-d4ba55015a10)
![21](https://github.com/user-attachments/assets/a2cc884a-a2af-4811-96a3-2912f61b18eb)

### Add new recipe page
![22](https://github.com/user-attachments/assets/937da2bc-0128-4db4-a386-a77da1b19b1b)

### Edit recipe page
![23](https://github.com/user-attachments/assets/b51d5809-1551-41c7-837e-d894c04fda4e)

### Admin's vs user's searching panel
![25](https://github.com/user-attachments/assets/3c6cf8ce-02da-45cc-88ac-c409b3886219)
![26](https://github.com/user-attachments/assets/9508d217-d398-472e-9b02-724d9d33f17f)

### Rest of admin's side funcionalities are avilable only on back-end (no front-end was created)

# Setup
#### Technical requirements
##### Frontend (client)
- React.js 18.2.0
##### Backend (server)
- Java 17
- Spring Boot 2.7.14
- MySQL
- Maven

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
#### 2. Client side
```
$ npm install
$ npm run
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
