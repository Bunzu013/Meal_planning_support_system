import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Toaster } from 'react-hot-toast';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Axios from 'axios';
import Header from './Header';
import Footer from './Footer';
//NotLogged
import NotLoggedInMainPage from './NotLoggedIn/MainPage/NotLoggedInMainPage';
import SignUpBody from './NotLoggedIn/Sign/SignUpBody';
import SignInBody from './NotLoggedIn/Sign/SignInBody';
import ForgotPage from './NotLoggedIn/Sign/ForgotBody';
//LoggedIn
import Contact from './LoggedIn/Contact/Contact';
import LoggedInMainPage from './LoggedIn/MainPage/LoggedInMainPage';
import Profile from './LoggedIn/Profile/Profile';
import EditData from './LoggedIn/EditData/EditData';
import ChangePassword from './LoggedIn/ChangePassword/ChangePassword';
import PreferredIngredients from './LoggedIn/PreferredIngredients/PreferredIngredients';
import Allergens from './LoggedIn/Allergens/Allergens';
import RecipesPage from './LoggedIn/Recipes/RecipesBody';
import RecipeBody from './LoggedIn/Recipe/RecipeBody';
import AddRecipeBody from './LoggedIn/Recipe/AddRecipeBody';
import EditRecipeBody from './LoggedIn/Recipe/EditRecipeBody';
import Favorites from './LoggedIn/Favorites/Favorites';
import MyRecipes from './LoggedIn/MyRecipes/MyRecipes';
import Mealplan from './LoggedIn/Mealplan/Mealplan';
import ShoppingList from './LoggedIn/ShoppingList/ShoppingList';
//Styles
import './MainStyle/MainStyle.css'
import './MainStyle/Form.css'
import './MainStyle/Header.css'
import './MainStyle/Footer.css'


const accessToken = localStorage.getItem('token');
Axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

function App() {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);

  useEffect(() => {
    const handleResize = () => {
      setWindowWidth(window.innerWidth);
    };
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return (
    <BrowserRouter>
      
      <div className="App">
      <Header />
        <Routes>
          <Route path="/notloggedin" element={<NotLoggedInMainPage />} />
          <Route path="/signup" element={<SignUpBody />} />
          <Route path="/signin" element={<SignInBody />} />
          <Route path="/forgotpassword" element={<ForgotPage />} />

          <Route path="/" element={<LoggedInMainPage />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/editdata" element={<EditData />} />
          <Route path="/changepassword" element={<ChangePassword />} />
          <Route path="/preferredingredients" element={<PreferredIngredients />} />
          <Route path="/allergens" element={<Allergens />} />
          <Route path="/contact" element={<Contact />} />

          <Route path="/recipes" element={<RecipesPage />} />
          <Route path="/recipe/:recipeId" element={<RecipeBody />} />
          <Route path="/addrecipe" element={<AddRecipeBody />} />
          <Route path="/editrecipe/:recipeId" element={<EditRecipeBody />} />
          <Route path="/favorites" element={<Favorites />} />
          <Route path="/myrecipes" element={<MyRecipes />} />

          <Route path="/shoppinglist" element={<ShoppingList />} />
          <Route path="/mealplan" element={<Mealplan />} />

        </Routes>
        {windowWidth >= 992 && <Footer />}

      </div>
      <Toaster position="top-center" />
    </BrowserRouter>
  );
}
export default App;
