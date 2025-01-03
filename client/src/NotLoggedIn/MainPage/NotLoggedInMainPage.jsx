import React, { useEffect, useState } from 'react';
import '../Styles/MainPage.css'
import axios from 'axios';
import { Link } from 'react-router-dom';

function NotLoggedInMainPage(){
    const [recommendedRecipes, setRecommendedRecipes] = useState([]);

    useEffect(() => {
    const fetchData = async () => {
      try {
        const recipesResponse = await axios.get(`http://localhost:8080/getAllRecipes`);
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

    return(
        <div className='Page'>
            <div className='body'>
                <div className="container body-content col-10">
                    <h1 className='main-title'>BUILD YOUR OWN MEALPLAN</h1>
                    <p className='text'>Choose from a variety of recipes.</p>  
                    <div className="container">
                        <div className="flex-row d-flex flex-wrap justify-content-center">
                            {recommendedRecipes.map((recipe, index) => (
                                <div key={index} className="col-12 col-md-12 col-lg-3 col-sm-12 recipe">
                                    <div className="text-center">
                                        <img src={`data:image/jpeg;base64, ${recipe.imageData}`} style={{'height': '185px'}} alt="recipe.imageName" className="img-fluid recipe-img"></img>
                                        <h3 className='recipe-title'>{recipe.recipeName}</h3>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="item-right">
                        <Link to="/signin">
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

export default NotLoggedInMainPage;