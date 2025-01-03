import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import Axios from 'axios';
import toast from 'react-hot-toast';
import AddToPlan from './AddToPlan'
import FavoriteButton from '../../icons/FavoriteButton'
import { jwtDecode } from "jwt-decode";

function RecipeBody() {
    const { recipeId } = useParams();
    const [recipeData, setRecipeData] = useState({});
    const recipeCategories = recipeData.categories || [];
    const recipeFilters = recipeData.filters || []
    const recipeIngredients = recipeData.ingredients || [];
    const [favoriteRecipesData, setFavoriteRecipesData] = useState([]);
    const [userRoles, setUserRoles] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);

                const roles = decoded.roles || [];

                setUserRoles(roles);
            } catch (error) {
                console.error('Error decoding JWT:', error);
            }
        }
    }, []);
    const isAdmin = userRoles.includes('ROLE_ADMIN');

    useEffect(() => {
        Axios.get(`http://localhost:8080/user/getFavouriteRecipes`)
            .then((response) => {
                setFavoriteRecipesData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching favorite recipes', error);
            });
    }, []);
    useEffect(() => {
        Axios.get(`http://localhost:8080/getRecipeData/${recipeId}`)
            .then((response) => {
                setRecipeData(response.data);
                console.log(response.data);
            })
            .catch((error) => {
                console.error('Error fetching recipe data', error);
            });
    }, [recipeId]);
    const [showOverlay, setShowOverlay] = useState(false);
    const toggleOverlay = () => {
        setShowOverlay(!showOverlay);
    };
    const isRecipeInFavorites = (recipeId) => {
        return favoriteRecipesData.some((recipe) => recipe.recipeId === recipeId);
    };
    const handleAddToFavorites = (recipeId) => {
        if (isRecipeInFavorites(recipeId)) {
            Axios.post(`http://localhost:8080/user/deleteFromFavourites?recipeId=${recipeId}`)
                .then((response) => {
                    if (response.status === 200) {
                        console.log("Recipe removed from favorites");
                        toast.success(`Recipe removed from favorites`, {
                            icon: "🗑️"
                        });
                        setFavoriteRecipesData(favoriteRecipesData.filter((recipe) => recipe.recipeId !== recipeId));
                    } else {
                        console.error("Error removing recipe from favorites");
                    }
                })
                .catch((error) => {
                    console.error('Error removing recipe from favorites', error);
                });
        } else {
            Axios.post(`http://localhost:8080/user/addRecipeToFavourites?recipeId=${recipeId}`)
                .then((response) => {
                    if (response.status === 200) {
                        console.log("Recipe added to favorites");
                        toast.success(`Recipe added to favorites`);
                        setFavoriteRecipesData([...favoriteRecipesData, { recipeId }]);
                    } else {
                        console.error("Error adding recipe to favorites");
                    }
                })
                .catch((error) => {
                    console.error('Error adding recipe to favorites', error);
                });
        }
    };
    const [showAddToPlanOverlay, setShowAddToPlanOverlay] = useState(false);
    const toggleAddToPlanOverlay = () => {
        setShowAddToPlanOverlay(!showAddToPlanOverlay);
    };
    const handleDeleteRecipe = () => {
        const isConfirmed = window.confirm("Are you sure you want to delete this recipe?");
        if (isConfirmed) {
            Axios.post(`http://localhost:8080/admin/deleteRecipe?recipeId=${recipeId}`)
                .then((response) => {
                    if (response.status === 200) {
                        console.log("Recipe deleted successfully");
                        toast.success(`Recipe deleted successfully`);
                        navigate('/recipes');
                    } else {
                        console.error("Error deleting recipe");
                    }
                })
                .catch((error) => {
                    console.error('Error deleting recipe', error);
                });
        }
    };

    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className='d-flex flex-lg-row flex-column mt-3'>
                        <div className="col-lg-4 col-12 image-block">
                            <img
                                className='recipe-img'
                                style={{ height: '100%' }}
                                src={`data:image/jpeg;base64, ${recipeData.imageData}`}
                                alt={recipeData.imageName}
                            />
                            <FavoriteButton
                                isRecipeInFavorites={isRecipeInFavorites}
                                recipe={recipeData}
                                handleAddToFavorites={handleAddToFavorites}
                            />
                        </div>
                        <div className="col-lg-8">
                            <h1 class="main-title">{recipeData.recipeName}</h1>
                            {recipeData?.notes?.split("\n").map((s) => <p className="text" style={{ 'margin-bottom': 0 }}>{s}</p>)}
                        </div>
                    </div>
                    <div className='d-flex flex-lg-row flex-column mt-3'>
                        <div className="col-lg-4 col-6">
                            <div className="d-flex flex-row">
                                <p class="main-text" style={{ marginRight: '20px' }}>CATEGORY: </p>
                                <p className="brown-title">{recipeCategories.join(', ')}</p>
                            </div>
                            <div className="d-flex flex-row">
                                <p class="main-text" style={{ marginRight: '20px' }}>TYPE: </p>
                                <p className="brown-title">{recipeFilters.join(', ')}</p>
                            </div>
                            <div className="d-flex flex-row">
                                <p class="main-text" style={{ marginRight: '20px' }}>CALORIES: </p>
                                <p className="brown-title">{recipeData.calories}</p>
                            </div>
                        </div>
                        <div className="col-lg-8">
                            <p class="main-text mb-0">INGREDIENTS:</p>
                            <ul>
                                {recipeIngredients.map((ingredient, index) => (
                                    <li key={index}>{ingredient.ingredientName} - {ingredient.quantity} {ingredient.unit}</li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div className="d-flex flex-row justify-content-center">
                        <button type="button" className="button mx-2" onClick={toggleOverlay}>
                            ADD TO PLAN
                        </button>

                        {isAdmin && (
                            <Link to={`/editrecipe/${recipeId}`} className="button mx-2">EDIT</Link>

                        )}
                        {isAdmin && (
                            <button className="button mx-2" onClick={handleDeleteRecipe}>DELETE</button>
                        )}
                    </div>
                    {showOverlay && (
                        <AddToPlan
                            recipeId={recipeId}
                            toggleOverlay={toggleOverlay}
                            toggleAddToPlanOverlay={toggleAddToPlanOverlay}
                        />
                    )}
                </div>
            </div>
        </div>
    )
}
export default RecipeBody;