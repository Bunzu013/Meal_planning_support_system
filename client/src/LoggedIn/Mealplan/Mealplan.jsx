import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import '../Styles/Mealplan.css'
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import "../Styles/Recipes.css"
import PlusIcon from "../../icons/PlusIcon"
import TrashIcon from '../../icons/TrashIcon';
import axios from 'axios';


function Mealplan() {
    const [weekDays, setWeekDays] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [mealPlanData, setMealPlanData] = useState([]);
    const navigate = useNavigate();


    useEffect(() => {

        const fetchWeekDays = async () => {
            try {
                const response = await axios.get('http://localhost:8080/getAllWeekDays');
                setWeekDays(response.data);
            } catch (error) {
                console.error('Error fetching week days:', error);
            }
        };

        const fetchRecipes = async () => {
            try {
                const response = await axios.get('http://localhost:8080/getAllRecipes');
                const data = response.data;
                setRecipes(data);
            } catch (error) {
                console.error('Error fetching recipes:', error);
            }
        };
        const fetchMealPlan = async () => {
            try {
                const response = await axios.post('http://localhost:8080/user/getMealPlanDetails', {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                const data = response.data;
                setMealPlanData(data);
            } catch (error) {
                console.error('Error fetching meal plan:', error);
            }
        };
        fetchWeekDays();
        fetchRecipes();
        fetchMealPlan();

    }, []);

    const handleDeleteMeal = async (mealId) => {
        console.log(mealId);
        const isConfirmed = window.confirm("Are you sure you want to delete this meal?");
        if (isConfirmed) {
            axios.post(`http://localhost:8080/user/deleteMeal?mealId=${mealId}`)
                .then((response) => {
                    if (response.status === 200) {
                        toast.success(`Meal removed from mealplan`, {
                            icon: "🗑️"
                        });
                        setTimeout(() => {
                            window.location.reload();
                        }, 500);
                        console.log('Meal deleted successfully');
                    } else {
                        console.error('Error deleting meal:', response.statusText);
                        console.log(mealId)
                    }
                })
                .catch((error) => {
                    console.error('Error deleting meal:', error.message);
                    console.log(mealId)
                });
        }
    };

    const handleAddMeal = async (weekDayId) => {
        try {
            const response = await axios.post(`http://localhost:8080/user/addNewMeal?weekDayId=${weekDayId}`, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 200) {
                toast.success(`New meal added to meal plan`, {
                    icon: "🍲"
                });
                setTimeout(() => {
                    window.location.reload();
                }, 500);
            } else {
                console.error('Error adding new meal:', response.statusText);
            }
        } catch (error) {
            console.error('Error adding new meal:', error.message);
        }
    };

    const handleDeleteRecipeFromMeal = async (mealId, recipeId) => {
        const isConfirmed = window.confirm("Are you sure you want to delete this recipe?");
        if (isConfirmed) {
            axios.post(`http://localhost:8080/user/deleteRecipeFromMealAndMealPlan?mealId=${mealId}&recipeId=${recipeId}`)
                .then((response) => {
                    if (response.ok) {
                        toast.success(`Recipe removed from meal`, {
                            icon: "🗑️"
                        });
                        setTimeout(() => {
                            window.location.reload();
                        }, 500);
                        console.log('Recipe deleted successfully');
                    } else {
                        console.error('Error deleting recipe:', response.statusText);
                        console.log(mealId);
                        console.log(recipeId);
                    }
                })
                .catch((error) => {
                    console.error('Error deleting recipe:', error);
                    console.log(mealId);
                    console.log(recipeId);
                })
        }
    };
    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-xxl-3 col-xl-4 col-l-3 col-md-5 col-sm-8 col-10">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/mealplan">mealplan</Link>
                    </div>
                    <div className="title-row">
                        <h1 className='main-title'>MEALPLAN</h1>
                    </div>
                    <div>
                        {weekDays.map((weekDay) => (
                            <Accordion key={weekDay.weekDayName} className='days mb-3'>
                                <AccordionSummary
                                    expandIcon={<ExpandMoreIcon />}
                                    aria-controls={`panel-${weekDay.weekDayName}-content`}
                                    id={`panel-${weekDay.weekDayName}-header`}
                                >
                                    <Typography className='week'>{weekDay.weekDayName}</Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                    <div className='meals'>
                                        {Array.isArray(mealPlanData[weekDay.weekDayName]) ? (
                                            mealPlanData[weekDay.weekDayName].map((mealPlanItem, index) => (
                                                <div key={mealPlanItem.mealId} className='meal'>
                                                    <div className="meal-header">
                                                        <span>Meal {index + 1}</span>
                                                        <div className='d-flex'>
                                                            <PlusIcon
                                                                to={`/recipes?addToPlan=true&mealId=${mealPlanItem.mealId}`}
                                                            />
                                                            <div className='trash' onClick={() => handleDeleteMeal(mealPlanItem.mealId)} >
                                                                <TrashIcon />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <ul style={{ 'list-style-type': 'none' }}>
                                                        {mealPlanItem.recipes.map((recipe) => (
                                                            <li key={recipe.recipeId}>
                                                                <Link className="brown-title" style={{ paddingLeft: 0 }} to={`/recipe/${recipe.recipeId}`}>{recipe.recipeName}</Link>
                                                                <button
                                                                    className="btn"
                                                                    style={{ paddingTop: 0, fontWeight: '600' }}
                                                                    onClick={() => handleDeleteRecipeFromMeal(mealPlanItem.mealId, recipe.recipeId)}
                                                                >
                                                                    x
                                                                </button>
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            ))
                                        ) : (
                                            <p className='brown-title mt-2'>No meals have been added here yet..</p>
                                        )}
                                        <div className="item-center" onClick={() => handleAddMeal(weekDay.weekDayId)}>
                                            <button className="button">ADD MEAL</button>
                                        </div>
                                    </div>
                                </AccordionDetails>
                            </Accordion>
                        ))}
                    </div>
                    <div className="item-center">
                        <p className="main-text mt-3">Generate your shopping list</p>
                        <Link to='/shoppinglist' className="btn button mt-0">SHOPPING LIST
                            <svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" fill='#fff' height='20px' width='20px' style={{ marginLeft: '10px', marginBottom: '5px' }} viewBox="0 0 97.25 122.88">
                                <path d="M9.28,8.23h8.55V2.85a2.86,2.86,0,0,1,5.71,0V8.23H36.09V2.85a2.85,2.85,0,0,1,5.7,0V8.23H54.34V2.85a2.86,2.86,0,0,1,5.71,0V8.23H72.6V2.85a2.85,2.85,0,1,1,5.7,0V8.23H88a9.29,9.29,0,0,1,9.27,9.28V113.6A9.32,9.32,0,0,1,88,122.88H9.28A9.3,9.3,0,0,1,0,113.6V17.51A9.29,9.29,0,0,1,9.28,8.23Zm12,90.31a2.77,2.77,0,0,1,0-5.54H76a2.77,2.77,0,0,1,0,5.54Zm0-18.26a2.77,2.77,0,0,1,0-5.53H76a2.77,2.77,0,0,1,0,5.53Zm0-18.25a2.77,2.77,0,0,1,0-5.54H76A2.77,2.77,0,0,1,76,62Zm0-18.26a2.77,2.77,0,0,1,0-5.53H76a2.77,2.77,0,0,1,0,5.53Zm57.06-30v5.38a2.85,2.85,0,1,1-5.7,0V13.76H60.05v5.38a2.86,2.86,0,0,1-5.71,0V13.76H41.79v5.38a2.85,2.85,0,0,1-5.7,0V13.76H23.54v5.38a2.86,2.86,0,0,1-5.71,0V13.76H9.28a3.7,3.7,0,0,0-2.64,1.1,3.75,3.75,0,0,0-1.11,2.65V113.6a3.79,3.79,0,0,0,3.75,3.75H88a3.76,3.76,0,0,0,3.74-3.75V17.51A3.76,3.76,0,0,0,88,13.76Z" />
                            </svg>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default Mealplan;