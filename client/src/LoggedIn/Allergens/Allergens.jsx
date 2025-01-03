import { React, useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import "../Styles/AddIngredients.css"
import BackIcon from '../../icons/BackIcon';
import SearchIcon from '../../icons/SearchIcon';
import axios from 'axios';
import "../Styles/Allergens.css";
import toast from 'react-hot-toast';

function Allergens() {
    const [userAllergens, setUserAllergens] = useState([]);
    const [allIngredients, setAllIngredients] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        const getAllIngredients = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/getAllIngredients`);
                setAllIngredients(response.data);
            } catch (error) {
                console.error("Error fetching allergens list", error);
            }
        };
        getAllIngredients();

    }, []);

    useEffect(() => {
        const getUserAllergens = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/user/getUserAllergenIngredients`);
                setUserAllergens(response.data);
            } catch (error) {
                console.error("Error fetching user's allergens", error);
            }
        };

        getUserAllergens();
    }, []);

    const addIngredient = async (ingredientId) => {
        try {
            await axios.post(`http://localhost:8080/user/addToAllergenIngredients?ingredientId=${ingredientId}`);
            console.log("user allergens before: ", userAllergens);
            const updatedUserAllergens = await axios.get('http://localhost:8080/user/getUserAllergenIngredients');
            setUserAllergens(updatedUserAllergens.data);
            console.log("user allergens after adding:", userAllergens);
        } catch (error) {
            toast.error('Error adding allergen');
            console.error('Error adding allergen:', error);
        }
    };

    const removeIngredient = async (ingredientId) => {
        try {
            await axios.post(`http://localhost:8080/user/deleteFromAllergenIngredients?ingredientId=${ingredientId}`);
            const updatedUserAllergens = await axios.get('http://localhost:8080/user/getUserAllergenIngredients');
            setUserAllergens(updatedUserAllergens.data);
        } catch (error) {
            toast.error('Error removing allergen');
            console.error('Error removing allergen:', error);
        }
    };

    const toggleIngredient = (ingredientId) => {
        if (userAllergens.find(e => e.ingredientId === ingredientId)) {
            removeIngredient(ingredientId);
            document.getElementById(ingredientId).classList.remove('selected');
            toast.success(`Ingredient removed from allergens`, {
                icon: "🗑️"
            });
        } else {
            addIngredient(ingredientId);
            document.getElementById(ingredientId).classList.add('selected');
            toast.success(`Ingredient added to allergens`, {
                icon: "✅"
            });
        }
    };

    const isAllergenSelected = (ingredientId) => userAllergens && userAllergens.find(e => e.ingredientId === ingredientId);

    const filteredIngredients = allIngredients.filter((ingredient) => ingredient.ingredientName.toLowerCase().includes(searchQuery.toLowerCase()));

    return (
        <div className='Page'>
            <div className='body'>
                <div class="container body-content col-10">
                    <div class="little-nav col-xxl-4 col-xl-4 col-l-4 col-md-6 col-12">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/profile">profile</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/allergens">allergens</Link>
                    </div>
                    <div class="title-row">
                        <BackIcon to="/profile" />
                        <h1 class='main-title col-xl-6 col-lg-6 col-10'>ALLERGENS</h1>
                    </div>
                    <div class="row justify-content-center mb-4">
                        <div class="col-xl-4 col-lg-6 col-8">
                            <form class="form-inline">
                                <div class="input-group mx-2">
                                    <input type="text"
                                        className="form-control"
                                        placeholder="Search"
                                        value={searchQuery}
                                        onChange={(e) => setSearchQuery(e.target.value)} />
                                    <div class="input-group-append">
                                        <button class="btn search-button" type="submit">
                                            <SearchIcon />
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="content-border">
                        <div class="flex-row flex-wrap d-flex justify-content-center">
                            {filteredIngredients.map((allergen) => (
                                <button key={allergen.ingredientId}
                                    id={allergen.ingredientId}
                                    value={allergen.ingredientName}
                                    onClick={() => toggleIngredient(allergen.ingredientId)}
                                    className={`ingredient-button ${isAllergenSelected(allergen.ingredientId) ? 'selected' : ''}`}>
                                    {allergen.ingredientName}
                                </button>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Allergens;