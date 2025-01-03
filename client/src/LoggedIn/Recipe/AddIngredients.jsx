import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import SearchIcon from '../../icons/SearchIcon';
import '../Styles/AddIngredients.css'
import Axios from 'axios';

function AddIngredient(props) {
    const {
        selectedIngredients,
        ingredientQuantities,
        toggleIngredientsOverlay,
        setFormData,
        formData,
        setShowIngredientsOverlay,
        setSelectedIngredients,
        selectedUnits
    } = props;
    const [ingredientsData, setIngredientsData] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const chunkArray = (arr, chunkSize) => {
        const result = [];
        for (let i = 0; i < arr.length; i += chunkSize) {
            result.push(arr.slice(i, i + chunkSize));
        }
        return result;
    };
    useEffect(() => {
        Axios.get('http://localhost:8080/getAllIngredients')
            .then((response) => {
                setIngredientsData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching ingredients data', error);
            });
    }, []);
    const handleIngredientChange = (ingredientName) => {
        if (selectedIngredients.includes(ingredientName)) {
            setSelectedIngredients(selectedIngredients.filter((item) => item !== ingredientName));
        } else {
            setSelectedIngredients([...selectedIngredients, ingredientName]);
        }
    };
    const addSelectedIngredients = () => {
        const ingredientsWithQuantities = selectedIngredients.map((ingredientName) => ({
            ingredientName,
            quantity: ingredientQuantities[ingredientName],
            unit: selectedUnits[ingredientName]
        }));
        setFormData({
            ...formData,
            ingredients: ingredientsWithQuantities,

        });
        setShowIngredientsOverlay(false);
    };
    const ingredientsChunks = chunkArray(ingredientsData, Math.ceil(ingredientsData.length / 3));

    const filteredIngredients = ingredientsData.filter((ingredient) => ingredient.ingredientName.toLowerCase().includes(searchQuery.toLowerCase()));

    return (
        <div className="overlay">
            <div className="container body-content col-10 overlay-content">
                <div className="title-row">
                    <Link onClick={toggleIngredientsOverlay}>
                        <BackIcon />
                    </Link>
                    <h1 className='main-title col-xl-6 col-lg-6 col-10'>ADD INGREDIENTS</h1>
                </div>
                <div className="row justify-content-center mb-4">
                    <div className="col-xl-4 col-lg-6 col-8">
                        <form className="form-inline">
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
                <div className="content-border" style={{ 'max-height': '50vh', "overflow-y": 'auto' }}>
                    <div className="row">
                        
                                <ul className="text_category d-flex flex-wrap">
                                    {filteredIngredients.map((ingredient) => (
                                        <li key={ingredient.ingredientName} className='d-flex flex-row col-lg-4 col-sm-6 col-12'>
                                            <input
                                                className="form-check-input"
                                                type="checkbox"
                                                id={ingredient.ingredientName}
                                                value={ingredient.ingredientName}
                                                checked={selectedIngredients.includes(ingredient.ingredientName)}
                                                onChange={() => handleIngredientChange(ingredient.ingredientName)}
                                            />
                                            <label htmlFor={ingredient.ingredientName}>
                                                {ingredient.ingredientName}
                                            </label>
                                        </li>
                                    ))}
                                </ul>
                    </div>
                </div>
                <br />
                <div className="item-center d-flex justify-content-center">
                    <button type="button" className="button" onClick={addSelectedIngredients}>
                        ADD SELECTED INGREDIENTS
                    </button>
                    <button type="button" className="button" onClick={toggleIngredientsOverlay}>
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
}
export default AddIngredient;