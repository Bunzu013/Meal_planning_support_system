import { React, useEffect, useState }  from 'react';
import '../Styles/MainPage.css'
import axios from 'axios';
import { Link } from 'react-router-dom';
import toast from 'react-hot-toast';

function LoggedInMainPage(){
    const [favoriteRecipes, setFavoriteRecipes] = useState([]);
    const [recommendedRecipes, setRecommendedRecipes] = useState([]);

    useEffect(() => {
    const fetchData = async () => {
      try {
        const recipesResponse = await axios.get(`http://localhost:8080/getAllRecipes`);
        const favoritesResponse = await axios.get(`http://localhost:8080/user/getFavouriteRecipes`);
        setFavoriteRecipes(favoritesResponse.data);

        if (recommendedRecipes.length === 0) {
          const randomRecs = randomRecipes(recipesResponse.data);
          setRecommendedRecipes(randomRecs);
        }
      } catch (error) {
        console.error("Error fetching recipes", error);
      }
    };

    fetchData();
   
  }, [recommendedRecipes]);

  const randomRecipes = (recipes) => {
    const shuffledRecipes = recipes.sort(() => Math.random() - 0.5);
    return shuffledRecipes.slice(0, 3);
  };

  const isRecipeInFavorites = (recipeId) => {
    return favoriteRecipes.some((recipe) => recipe.recipeId === recipeId);
  };

  const handleAddToFavorites = (recipeId) => {
    if (isRecipeInFavorites(recipeId)) {
      axios.post(`http://localhost:8080/user/deleteFromFavourites?recipeId=${recipeId}`)
        .then((response) => {
          if (response.status === 200) {
            console.log("Recipe removed from favorites");
            toast.success(`Recipe removed from favorites`, {
              icon: "🗑️",
            });
            setFavoriteRecipes(favoriteRecipes.filter((recipe) => recipe.recipeId !== recipeId));
          } else {
            console.error("Error removing recipe from favorites");
          }
        })
        .catch((error) => {
          console.error('Error removing recipe from favorites', error);
        });
    } else {
      axios.post(`http://localhost:8080/user/addRecipeToFavourites?recipeId=${recipeId}`)
        .then((response) => {
          if (response.status === 200) {
            console.log("Recipe added to favorites");
            toast.success(`Recipe added to favorites`);
            setFavoriteRecipes([...favoriteRecipes, { recipeId }]);
          } else {
            console.error("Error adding recipe to favorites");
          }
        })
        .catch((error) => {
          console.error('Error adding recipe to favorites', error);
        });
    }
}
    
    return(
        <div className="Page">
            <div className='body'>
                <div className="container body-content col-10">
                    <h1 className='main-title'>MEAL IDEAS</h1>
                    <p className='text'>Our recipe recommendations just for you.</p>  
                    <div className="container">
                        <div className="flex-row d-flex flex-wrap justify-content-center">
                            {recommendedRecipes.map((recipe, index) => (
                                <div key={index} className="col-12 col-md-12 col-lg-3 col-sm-12 recipe">
                                    <div className="text-center">
                                        <img src={`data:image/jpeg;base64, ${recipe.imageData}`} style={{'height': '185px'}} alt="recipe.imageName" className="img-fluid recipe-img"></img>
                                        <button type="button" className={`heart-icon ${isRecipeInFavorites(recipe.recipeId) ? 'favorite' : ''}`} onClick={() => handleAddToFavorites(recipe.recipeId)}>
                                            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill={isRecipeInFavorites(recipe.recipeId) ? '#ef6767' : '#fff'} stroke="#ef6767" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
                                            </svg>
                                        </button>
                                        <h3 className='recipe-title'>{recipe.recipeName}</h3>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="item-right">
                        <Link to="/recipes">
                            <button className="btn button-main">
                                MORE
                            </button>  
                        </Link>
                    </div>
                </div>      
            </div>     
        </div>
    )
}

export default LoggedInMainPage;