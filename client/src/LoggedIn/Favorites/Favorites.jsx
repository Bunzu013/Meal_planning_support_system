import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import axios from 'axios';
import toast from 'react-hot-toast';
import "../Styles/Recipes.css";
import { Trash } from 'react-bootstrap-icons';
import "../../MainStyle/MainStyle.css";

function Favorites(){
    const [userFavorites, setUserFavorites] = useState([]);
    const [noUserFavorites, setNoUserFavorites] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [recipesPerPage, setRecipesPerPage] = useState(9);

    useEffect(() => {
        const getUserFavorites = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/user/getFavouriteRecipes`);
                setUserFavorites(response.data);
                console.log(response.data);
                if (response.data.length === 0) {
                    setNoUserFavorites(true);
                }
            } catch (error) {
                console.error("Error fetching favorites list", error);
            }
        }
        getUserFavorites();
    }, []);

    const indexOfLastRecipe = currentPage * recipesPerPage;
    const indexOfFirstRecipe = indexOfLastRecipe - recipesPerPage;
    const currentRecipes = userFavorites.slice(indexOfFirstRecipe, indexOfLastRecipe);

    const currPage = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleRemoveFromFavorites = (recipeId) => {
        const isConfirmed = window.confirm("Are you sure you want to delete this item?");
        if(isConfirmed){
            axios.post(`http://localhost:8080/user/deleteFromFavourites?recipeId=${recipeId}`)
                .then((response) => {
                    if (response.status === 200) {
                        console.log("Recipe removed from favorites");
                        toast.success(`Recipe removed from favorites`, {
                            icon: "🗑️"
                        });
                        setUserFavorites(userFavorites.filter((recipe) => recipe.recipeId !== recipeId));
                    } else {
                        console.error("Error removing recipe from favorites");
                    }
                })
                .catch((error) => {
                    console.error('Error removing recipe from favorites', error);
                });
        }
    };

    return(
        <div className="Page">
            <div className="body>">
                <div className="container body-content col-10">
                    <div className="little-nav col-xxl-3 col-xl-3 col-l-2 col-md-5 col-9">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/">recipes</Link> 
                        <span><code>&gt;</code></span>
                        <Link to="/favorites">favorites</Link>
                    </div>
                    <div className="title-row">
                            <BackIcon to="/recipes" />
                        <h1 className='main-title col-xl-6 col-lg-6 col-10'>MY FAVORITES</h1>
                    </div>

                        <div className="col-md-12 col-12 d-flex flex-column justify-content-center">
                            
                            {noUserFavorites ? (
                                <p className='item-center text'>No recipes found</p>
                            ) : (
                                <div className="d-flex flex-row flex-wrap justify-content-center">
                                    {currentRecipes.map((recipe, index) => (
                                        <div key={index} className='col-xl-3 col-md-4 col-l-3 col-12 recipe d-flex flex-column'>
                                            <div>
                                                <img className='recipe-img img-fluid' style={{'height': '185px'}} src={`data:image/jpeg;base64, ${recipe.imageData}`} alt={recipe.imageName} />
                                                <button className="heart-icon" type="button" onClick={() => handleRemoveFromFavorites(recipe.recipeId)}>
                                                    <Trash size={30} color="#ef6767" />
                                                </button>
                                            </div>

                                            <Link className='recipe-title' to={`/recipe/${recipe.recipeId}`}>{recipe.recipeName}</Link>
                                        </div>
                                    ))}
                                </div>
                            )}
                            
                            <nav>
                                <ul className="pagination justify-content-center">
                                    <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                                        <Link className="page-link" style={{ background: 'transparent', color: '#ef6767' }} onClick={() => currPage(currentPage - 1)}>&lt;</Link>
                                    </li>
                                    {Array.from({ length: Math.ceil(userFavorites.length / recipesPerPage) }, (_, i) => (
                                        <li key={i} className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}>
                                            <Link className="page-link" style={{ boxShadow: 0 }} onClick={() => currPage(i + 1)}>{i + 1}</Link>
                                        </li>
                                    ))}
                                    <li className={`page-item ${currentPage === Math.ceil(userFavorites.length / recipesPerPage) ? 'disabled' : ''}`}>
                                        <Link className="page-link" style={{ background: 'transparent', color: '#ef6767' }} onClick={() => currPage(currentPage + 1)}>&gt;</Link>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                </div>
            </div>
        </div>
    )
}

export default Favorites;