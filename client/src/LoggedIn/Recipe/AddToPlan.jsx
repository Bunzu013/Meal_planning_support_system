import React, { useState, useEffect } from 'react';
import Axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import toast from 'react-hot-toast';

function AddToPlan(props) {
    const {
        toggleAddToPlanOverlay,
        recipeId
    } = props;
    const navigate = useNavigate();
    const [weekDays, setWeekDays] = useState([]);
    const [selectedDay, setSelectedDay] = useState(null);
    const [mealsForSelectedDay, setMealsForSelectedDay] = useState([]);
    const [selectedMeal, setSelectedMeal] = useState(null);
    const [recipesInSelectedMeal, setRecipesInSelectedMeal] = useState([]);
    useEffect(() => {
        Axios.get('http://localhost:8080/getAllWeekDays')
            .then((response) => {
                setWeekDays(response.data);
            })
            .catch((error) => {
                console.error('Error fetching week days', error);
            });
    }, []);
    const handleDayChange = (event) => {
        const selectedDayId = event.target.value;
        setSelectedDay(selectedDayId);
        Axios.post('http://localhost:8080/user/getMealPlanDetails', { weekDayName: selectedDayId })
            .then((response) => {
                setMealsForSelectedDay(response.data[selectedDayId] || []);
            })
            .catch((error) => {
                console.error('Error fetching meals for the selected day', error);
            });
    };
    const handleMealChange = (event) => {
        const selectedMealId = event.target.value;
        setSelectedMeal(selectedMealId);
        const selectedMealDetails = mealsForSelectedDay.find((meal) => meal.mealId === parseInt(selectedMealId, 10));
        setRecipesInSelectedMeal(selectedMealDetails?.recipes || []);
    };
    const handleSave = () => {
        if (selectedDay && selectedMeal) {
            const selectedDayObject = weekDays.find(day => day.weekDayName === selectedDay);
            if (selectedDayObject) {
                if (selectedMeal === "0") {
                    const mealData = {
                        recipeId: parseInt(recipeId),
                        mealId: 0,
                        weekDayId: selectedDayObject.weekDayId
                    };
                    Axios.post('http://localhost:8080/user/addRecipeToMealAndMealPlan', mealData)
                        .then((response) => {
                            console.log(response.data);
                            console.log(mealData);
                            toast.success('Recipe was successfully added to mealplan');
                            toggleAddToPlanOverlay();
                        })
                        .catch((error) => {
                            console.error('Error adding recipe to meal and meal plan', error);
                            console.log(mealData);
                            toast.error('Failed to add a recipe because it had been added previously');
                            toggleAddToPlanOverlay();
                        });
                } else {
                    const mealData = {
                        recipeId: parseInt(recipeId),
                        mealId: parseInt(selectedMeal),
                    };
                    Axios.post('http://localhost:8080/user/addRecipeToMealAndMealPlan', mealData)
                        .then((response) => {
                            console.log(response.data);
                            console.log(mealData);
                            toast.success('Recipe was successfully added to mealplan');
                            toggleAddToPlanOverlay();
                        })
                        .catch((error) => {
                            console.error('Error adding recipe to meal and meal plan', error);
                            console.log(mealData);
                            toast.error('Failed to add a recipe because it had been added previously');
                            toggleAddToPlanOverlay();
                        });
                }
            } else {
                console.error('Invalid selected day');
            }
        } else {
            console.error('Please select both day and meal before saving.');
        }
    };

    return (
        <div className="overlay">
            <div className="container body-content col-5 overlay-content">
                <div className="title-row">
                    <Link onClick={toggleAddToPlanOverlay}>
                        <BackIcon />
                    </Link>
                    <h1 className='main-title col-xl-6 col-lg-6 col-10'>ADD TO PLAN</h1>
                </div>
                <div className="row mt-1">
                    <div className="mb-3 d-flex justify-content-between">
                        <span className='main-text'>Select day of the week: </span>
                        <select className='units col-7' onChange={handleDayChange}>
                            <option value="" disabled selected>
                                Select Day
                            </option>
                            {weekDays.map((day) => (
                                <option key={day.weekDayName} value={day.weekDayName}>
                                    {day.weekDayName}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="d-flex justify-content-between">
                        <span className='main-text'>Select meal: </span>
                        <select className='units col-7' onChange={handleMealChange}>
                            <option value="" disabled selected>
                                Select Meal
                            </option>
                            {mealsForSelectedDay.map((meal, index) => (
                                <option key={meal.mealId} value={meal.mealId}>
                                    <span>Meal {index + 1}</span>
                                </option>
                            ))}
                            <option value="0">
                                <span>New meal</span>
                            </option>
                        </select>
                    </div>
                    <p className="brown-title mt-2 mb-0" style={{ paddingLeft: '2%' }}>Recipes in meal: {recipesInSelectedMeal.map((recipe, index) => recipe.recipeName).join(', ')}</p>
                </div>
                <br />
                <div className="d-flex justify-content-center">
                    <button type="submit" className="button" onClick={handleSave}>
                        ADD TO PLAN
                    </button>
                </div>
            </div>
        </div>
    );
}
export default AddToPlan;