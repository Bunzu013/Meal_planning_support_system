import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import SearchIcon from '../../icons/SearchIcon';
import '../Styles/AddIngredients.css';
import axios from 'axios';
import toast from 'react-hot-toast';

function PreferredIngredients() {
    const [allIngredients, setAllIngredients] = useState([]);
    const [preferredIngredients, setPreferredIngredients] = useState([]);
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
        const getPreferredIngredients = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/user/getUserPreferredIngredients`);
                setPreferredIngredients(response.data);
            } catch (error) {
                console.error("Error fetching user's preferred ingredients", error);
            }
        };
        getPreferredIngredients();
    }, []);

    const addIngredient = async (ingredientId) => {
        try {
            await axios.post(`http://localhost:8080/user/addToPreferred?ingredientId=${ingredientId}`);
            const updatedUserPreferredIngredients = await axios.get(`http://localhost:8080/user/getUserPreferredIngredients`);
            setPreferredIngredients(updatedUserPreferredIngredients.data);
        } catch (error) {
            toast.error('Error adding ingredient');
            console.error('Error adding ingredient:', error);
        }
    };

    const removeIngredient = async (ingredientId) => {
        try {
            await axios.post(`http://localhost:8080/user/deleteFromPreferredIngredients?ingredientId=${ingredientId}`);
            const updatedPreferredIngredients = await axios.get(`http://localhost:8080/user/getUserPreferredIngredients`);
            setPreferredIngredients(updatedPreferredIngredients.data);
        } catch (error) {
            toast.error('Error removing ingredient');
            console.error('Error removing ingredient:', error);
        }
    };

    const toggleIngredient = (ingredientId) => {
        if (preferredIngredients.find(e => e.ingredientId === ingredientId)) {
            removeIngredient(ingredientId);
            document.getElementById(ingredientId).classList.remove('selected');
            toast.success(`Ingredient removed from preferred ingredients`, {
                icon: "🗑️"
            });
        } else {
            addIngredient(ingredientId);
            document.getElementById(ingredientId).classList.add('selected');
            toast.success(`Ingredient added to preferred ingredients`, {
                icon: "✅"
            });
        }
    };

    const isIngredientSelected = (ingredientId) => preferredIngredients && preferredIngredients.find(e => e.ingredientId === ingredientId);

    const filteredIngredients = allIngredients.filter((ingredient) => ingredient.ingredientName.toLowerCase().includes(searchQuery.toLowerCase()));


    return (
        <div className="Page">
            <div className="body>">
                <div className="container body-content col-10">
                    <div className="little-nav col-xxl-4 col-xl-4 col-l-4 col-md-6 col-12">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/profile">profile</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/preferredingredients">preferred ingredients</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon to="/profile" />
                        <h1 className='main-title col-xl-6 col-lg-8 col-10'>PREFERRED INGREDIENTS</h1>
                    </div>
                    <div className="row justify-content-center mb-4">
                        <div className="col-xl-4 col-lg-6 col-8">
                            <form className="form-inline">
                                <div className="input-group mx-2">
                                    <input type="text"
                                        className="form-control"
                                        placeholder="Search"
                                        value={searchQuery}
                                        onChange={(e) => setSearchQuery(e.target.value)} />
                                    <div className="input-group-append">
                                        <button className="btn search-button" type="submit">
                                            <SearchIcon />
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div className="content-border">
                        <div className="flex-row flex-wrap d-flex justify-content-center">
                            {filteredIngredients.map((ingredient) => (
                                <button key={ingredient.ingredientId}
                                    id={ingredient.ingredientId}
                                    value={ingredient.ingredientName}
                                    onClick={() => toggleIngredient(ingredient.ingredientId)}
                                    className={`ingredient-button ${isIngredientSelected(ingredient.ingredientId) ? 'selected' : ''}`}>
                                    {ingredient.ingredientName}
                                </button>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default PreferredIngredients;