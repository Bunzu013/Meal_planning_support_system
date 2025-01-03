import React, { useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import Axios from 'axios';
import toast from 'react-hot-toast';
import FavoriteButton from '../../icons/FavoriteButton'
import AddToPlanButton from '../../icons/AddToPlanButton'

function RecipesContent({ noRecipesFound, setCurrentPage, isRecipeInFavorites, handleAddToFavorites, recipesData, currentPage, toggleAddToPlanOverlay, setRecipeAdd }) {
    
    const [searchParams] = useSearchParams();

    const recipesPerPage =  9;
    const indexOfLastRecipe = currentPage * recipesPerPage;
    const indexOfFirstRecipe = indexOfLastRecipe - recipesPerPage;
    const currentRecipes = recipesData.slice(indexOfFirstRecipe, indexOfLastRecipe);

    const mealId = Number(searchParams.get("mealId"));
    const [selectedRecipe, setSelectedRecipe] = useState(null);

    const handleAddToMeal = (recipeId) => {
        setSelectedRecipe(recipeId);
        if (mealId) {
            const mealData = {
                recipeId: recipeId,
                mealId: mealId
            };
            Axios.post('http://localhost:8080/user/addRecipeToMealAndMealPlan', mealData)
                .then((response) => {
                    console.log(response.data);
                    console.log(mealData);
                    toast.success('Recipe was successfully added to mealplan');
                })
                .catch((error) => {
                    console.error('Error adding recipe to meal and meal plan', error);
                    console.log(mealData);
                    toast.error('Failed to add a recipe because it had been added previously');
                });
        } else {
            console.error('Please select both day and meal before saving.');
        };
    };
    const paginate = (pageNumber) => {
        setCurrentPage(pageNumber);
    };
    return (
        <div className="col-lg-9 col-12 d-flex flex-column justify-content-between">
            {noRecipesFound ? (
                <p className='item-center text'>No recipes found</p>
            ) : (
                <div className="d-flex flex-row flex-wrap justify-content-xl-start justify-content-center">
                    {currentRecipes.map((recipe, index) => (
                        <div key={index} className='col-xl-3 col-md-4 col-8 recipe d-flex flex-column'>
                            {searchParams.get("addToPlan") === "true" ?
                                <div>
                                    <img className='recipe-img' style={{ 'height': '185px' }} src={`data:image/jpeg;base64, ${recipe.imageData}`} alt={recipe.imageName} />
                                    <AddToPlanButton
                                        recipe={recipe}
                                        handleAddToMeal={handleAddToMeal}
                                    />
                                </div>
                                :
                                <div>
                                    <img className='recipe-img' style={{ 'height': '185px' }} src={`data:image/jpeg;base64, ${recipe.imageData}`} alt={recipe.imageName} />
                                    <FavoriteButton
                                        isRecipeInFavorites={isRecipeInFavorites}
                                        recipe={recipe}
                                        handleAddToFavorites={handleAddToFavorites}
                                    />
                                </div>}
                            <Link className='recipe-title' to={`/recipe/${recipe.recipeId}`}>{recipe.recipeName}</Link>
                            {searchParams.get("addToPlan") === "true" ?
                                <div></div>
                                :
                                <div className="item-center">
                                    <button type="button" className="btn button-add" onClick={() => { setRecipeAdd(recipe.recipeId); toggleAddToPlanOverlay() }}>
                                        ADD TO PLAN
                                    </button>
                                </div>
                            }
                        </div>
                    ))}
                </div>
            )}
            <nav>
                <ul className="pagination justify-content-center">
                    <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                        <span className="page-link" style={{ background: 'transparent', color: '#ef6767' }} onClick={() => paginate(currentPage - 1)}>&lt;</span>
                    </li>
                    {Array.from({ length: Math.ceil(recipesData.length / recipesPerPage) }, (_, i) => (
                        <li key={i} className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}>
                            <span className="page-link" style={{ boxShadow: 0 }} onClick={() => paginate(i + 1)}>{i + 1}</span>
                        </li>
                    ))}
                    <li className={`page-item ${currentPage === Math.ceil(recipesData.length / recipesPerPage) ? 'disabled' : ''}`}>
                        <span className="page-link" style={{ background: 'transparent', color: '#ef6767' }} onClick={() => paginate(currentPage + 1)}>&gt;</span>
                    </li>
                </ul>
            </nav>
        </div>
    )
}
export default RecipesContent;