import React, { useState, useEffect } from 'react';
import Axios from 'axios';
import AddIngredient from '../Recipe/AddIngredients';
import '../../MainStyle/Form.css'

const Form = ({
    formData,
    setFormData,
    handleInputChange,
    handleSubmit,
    edit,
}) => {
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const [categoriesData, setCategoriesData] = useState([]);
    const [filtersData, setFiltersData] = useState([]);
    const [unitsData, setUnitsData] = useState([]);
    const [ingredientQuantities, setIngredientQuantities] = useState({});
    const [selectedUnits, setSelectedUnits] = useState({});

    useEffect(() => {
        Axios.get('http://localhost:8080/getAllCategories')
            .then((response) => {
                setCategoriesData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching categories data', error);
            });
        Axios.get('http://localhost:8080/getAllFilters')
            .then((response) => {
                setFiltersData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching filters data', error);
            });
        Axios.get('http://localhost:8080/getAllUnits')
            .then((response) => {
                setUnitsData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching units data', error);
            });
    }, []);


    useEffect(() => {
        if (edit === true) {
            const initiallySelectedIngredients = formData.ingredients.map((ingredient) => ingredient.ingredientName);
            setSelectedIngredients(initiallySelectedIngredients);

            const initialIngredientQuantities = {};
            formData.ingredients.forEach((ingredient) => {
                initialIngredientQuantities[ingredient.ingredientName] = ingredient.quantity;
            });
            setIngredientQuantities(initialIngredientQuantities);

            const initialSelectedUnits = {};
            formData.ingredients.forEach((ingredient) => {
                initialSelectedUnits[ingredient.ingredientName] = ingredient.unit;
            });
            setSelectedUnits(initialSelectedUnits);
        }
    }, [edit, formData.ingredients]);


    const [showIngredientsOverlay, setShowIngredientsOverlay] = useState(false);
    const toggleIngredientsOverlay = () => {
        setShowIngredientsOverlay(!showIngredientsOverlay);
    };

    const handleIngredientChange = (ingredientName, quantity, unitName) => {
        console.log(quantity);
        console.log(parseFloat(quantity));
        const updatedQuantities = {
            ...ingredientQuantities,
            [ingredientName]: parseFloat(quantity),
        };
        const updatedUnits = {
            ...selectedUnits,
            [ingredientName]: unitName || '',
        };
        setIngredientQuantities(updatedQuantities);
        setSelectedUnits(updatedUnits);
        console.log(updatedQuantities);
        const updatedIngredients = selectedIngredients.map((ingredientName) => ({
            ingredientName,
            quantity: parseFloat(updatedQuantities[ingredientName]),
            unit: updatedUnits[ingredientName],
        }));
        console.log(updatedIngredients);
        setFormData({
            ...formData,
            ingredients: updatedIngredients,
        });

    };
    return (
        <form className="d-flex flex-lg-row flex-column" onSubmit={handleSubmit} encType="multipart/form-data">
            <div className="col-lg-4 col-12">
                {formData.imageFile && (
                    <img className='mb-1 recipe-img' src={URL.createObjectURL(formData.imageFile)} alt="Recipe" />
                )}
                <label htmlFor="recipeImage" className="form-control-file">Choose file</label>
                <input
                    type="file"
                    className="form-control-file"
                    id="recipeImage"
                    accept="image/*"
                    onChange={(e) => setFormData({ ...formData, imageFile: e.target.files[0] })}
                />
            </div>
            <div className="formularz_dane col-lg-8 col-md-12">
                <div className="form-group">
                    <label className="col-lg-4" htmlFor="inputRecipename">RECIPE NAME</label>
                    <input
                        type="text"
                        className="form-control"
                        style={{ 'marginRight': 0 }}
                        id="inputRecipename"
                        name='recipeName'
                        value={formData.recipeName}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label className="col-lg-4" htmlFor="recipeDescription">DESCRIPTION</label>
                    <textarea
                        className="form-control"
                        id="recipeDescription"
                        rows="3"
                        name='notes'
                        value={formData.notes}
                        onChange={handleInputChange}
                    ></textarea>
                </div>
                <div className="form-group">
                    <label className="col-lg-4" htmlFor="recipeIngredint">INGREDIENTS</label>
                    <div className="form-check">
                        <ul className="brown-title mt-2">
                            {formData.ingredients.map((ingredient, index) => {
                                return (
                                    <li className='col-12 mb-1' key={index}>
                                        <label className='col-xl-5 col-12'>{ingredient.ingredientName}</label>
                                        <input
                                            type="number"
                                            className='col-xl-3 col-5 quantity'
                                            name={`quantity${index}`}
                                            style={{ 'margin-right': '10px' }}
                                            placeholder="quantity"
                                            value={
                                                edit ? ingredientQuantities[ingredient.ingredientName] || '' : ingredientQuantities[ingredient.ingredientName]
                                            }
                                            onChange={(e) => {
                                                handleIngredientChange(
                                                    ingredient.ingredientName,
                                                    e.target.value,
                                                    selectedUnits[ingredient.ingredientName]
                                                );
                                            }}
                                        />
                                        <select
                                            className="col-xl-3 col-5 units"
                                            name={`unit${index}`}
                                            value={edit ? selectedUnits[ingredient.ingredientName] || '' :  selectedUnits[ingredient.ingredientName] }
                                            onChange={(e) => {
                                                handleIngredientChange(
                                                    ingredient.ingredientName,
                                                    ingredientQuantities[ingredient.ingredientName],
                                                    e.target.value
                                                );
                                            }}
                                        >
                                            <option value="">Choose unit</option>
                                            {unitsData.map((unit) => (
                                                <option key={unit.id} value={unit.unitName}>
                                                    {unit.unitName}
                                                </option>
                                            ))}
                                        </select>
                                    </li>
                                )
                            })}
                        </ul>
                        <div className="item-center mb-3">
                            <button
                                type='button'
                                className="button-add btn item-center"
                                onClick={toggleIngredientsOverlay}
                            >
                                ADD
                            </button>
                        </div>
                    </div>
                </div>
                {showIngredientsOverlay && (
                    <AddIngredient
                        formData={formData}
                        selectedIngredients={selectedIngredients}
                        ingredientQuantities={ingredientQuantities}
                        toggleIngredientsOverlay={toggleIngredientsOverlay}
                        setShowIngredientsOverlay={setShowIngredientsOverlay}
                        setFormData={setFormData}
                        setSelectedIngredients={setSelectedIngredients}
                        selectedUnits={selectedUnits}
                    />
                )}
                <div className="form-group">
                    <label className="col-lg-4" htmlFor="recipeCategory">CATEGORY</label>
                    <div className="form-check">
                        {categoriesData.map((category, index) => (
                            <div className="option" key={index}>
                                <input
                                    className="form-check-input"
                                    type="checkbox"
                                    style={{ 'marginRight': '10px' }}
                                    name="categories"
                                    value={category.categoryName}
                                    checked={formData.categories.includes(category.categoryName)}
                                    onChange={handleInputChange}
                                />
                                <label className="form-check-label" htmlFor={`categoryCheck${index}`}>
                                    {category.categoryName}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="form-group">
                    <label className="col-lg-4" htmlFor="recipeFilters">FILTERS</label>
                    <div className="form-check">
                        {filtersData.map((filter, index) => (
                            <div className="option" key={index}>
                                <input
                                    className="form-check-input"
                                    type="checkbox"
                                    style={{ 'marginRight': '10px' }}
                                    name="filters"
                                    value={filter.filterName}
                                    checked={formData.filters.includes(filter.filterName)}
                                    onChange={handleInputChange}
                                />
                                <label className="form-check-label" htmlFor={`filterCheck${index}`}>
                                    {filter.filterName}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="form-group">
                    <label className="col-lg-4" htmlFor="recipeCalories">CALORIES</label>
                    <div className="col-lg-8 d-flex flex-column">
                        <input
                            type="number"
                            className="form-control"
                            style={{ 'marginRight': 0 }}
                            name="calories"
                            value={formData.calories}
                            onChange={handleInputChange}
                        />
                        <div className='item-center mt-2'>
                            <button type="submit" className="button-add btn mt-2">SAVE</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
};
export default Form;