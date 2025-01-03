import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import { Pencil } from 'react-bootstrap-icons';
import "../Styles/Recipes.css"
import axios from 'axios';
import toast from 'react-hot-toast';

function MyRecipes() {
    const [userRecipes, setUserRecipes] = useState([]);
    const [noUserRecipes, setNoUserRecipes] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [recipesPerPage, setRecipesPerPage] = useState(9);

    useEffect(() => {
        const getUserRecipes = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/user/getUserRecipes`);
                setUserRecipes(response.data);
                console.log(response.data);
                if (response.data.length === 0) {
                    setNoUserRecipes(true);
                }
            } catch (error) {
                toast.error('Error getting recipes list');
                console.error("Error fetching user's recipes list", error);
            }
        }
        getUserRecipes();

    }, []);

    const removeRecipe = async (recipeId) => {
        const isConfirmed = window.confirm("Are you sure you want to delete this item?");
        if (isConfirmed) {
            await axios.post(`http://localhost:8080/user/deleteUserRecipe?recipeId=${recipeId}`)
                .then((response) => {
                    if (response.status === 200) {
                        setUserRecipes(userRecipes.filter((recipe) => recipe.recipeId !== recipeId));
                        console.log("Recipe removed successfully");
                        toast.success('Recipe removed successfuly', {
                            icon: "🗑️"
                        });
                    } else {
                        console.error("Error removing recipe");
                    }
                })
                .catch((error) => {
                    toast.error('Error removing recipe');
                    console.error('Error removing recipe:', error);
                });
        }
    };

    const removeButton = (recipeId) => {
        removeRecipe(recipeId);
    }

    const indexOfLastRecipe = currentPage * recipesPerPage;
    const indexOfFirstRecipe = indexOfLastRecipe - recipesPerPage;
    const currentRecipes = userRecipes.slice(indexOfFirstRecipe, indexOfLastRecipe);

    const currPage = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    return (
        <div className="Page">
            <div className="body>">
                <div className="container body-content col-10">
                    <div className="little-nav col-xxl-3 col-xl-3 col-l-2 col-md-5 col-9">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/">recipes</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/myrecipes">my recipes</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon to="/recipes" />
                        <h1 className='main-title col'>MY RECIPES</h1>
                        <div className="item-right col">
                            <Link to="/addrecipe">
                                <button className="btn button-main">ADD NEW RECIPE</button>
                            </Link>
                        </div>
                    </div>
                    <div className="col-md-12 col-12 d-flex flex-column justify-content-center">
                        {noUserRecipes ? (
                            <p className='item-center text'>No recipes found</p>
                        ) : (
                            <div className="d-flex flex-row flex-wrap justify-content-center">
                                {currentRecipes.map((recipe, index) => (
                                    <div key={index} className='col-xl-3 col-md-4 col-l-3 col-12 recipe d-flex flex-column'>
                                        <div>
                                            <img className='recipe-img img-fluid' style={{ 'height': '185px' }} src={`data:image/jpeg;base64, ${recipe.imageData}`} alt={recipe.imageName} />
                                            <Link to={`/editrecipe/${recipe.recipeId}`}>
                                                <button className="heart-icon" type="button">
                                                    <Pencil size={30} color="#ef6767" />
                                                </button>
                                            </Link>
                                        </div>
                                        <Link className='recipe-title' to={`/recipe/${recipe.recipeId}`}>{recipe.recipeName}</Link>
                                        <div className="item-center">
                                            <button type="button" className="btn button-add" onClick={() => removeButton(recipe.recipeId)}>
                                                DELETE RECIPE
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
                <nav>
                    <ul className="pagination justify-content-center">
                        <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                            <Link className="page-link" style={{ background: 'transparent', color: '#ef6767' }} onClick={() => currPage(currentPage - 1)}>&lt;</Link>
                        </li>
                        {Array.from({ length: Math.ceil(userRecipes.length / recipesPerPage) }, (_, i) => (
                            <li key={i} className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}>
                                <Link className="page-link" style={{ boxShadow: 0 }} onClick={() => currPage(i + 1)}>{i + 1}</Link>
                            </li>
                        ))}
                        <li className={`page-item ${currentPage === Math.ceil(userRecipes.length / recipesPerPage) ? 'disabled' : ''}`}>
                            <Link className="page-link" style={{ background: 'transparent', color: '#ef6767' }} onClick={() => currPage(currentPage + 1)}>&gt;</Link>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    )
}

export default MyRecipes;