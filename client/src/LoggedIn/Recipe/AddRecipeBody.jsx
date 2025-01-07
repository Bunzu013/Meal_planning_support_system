import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import '../Styles/AddRecipe.css'
import Axios from 'axios';
import Form from './Form';
import toast from 'react-hot-toast';

function AddRecipeBody() {
    const [formData, setFormData] = useState({
        recipeName: '',
        notes: null,
        ingredients: [],
        calories: 0,
        categories: [],
        filters: [],
        imageFile: null,
    });
    const navigate = useNavigate();
    const [windowWidth, setWindowWidth] = useState(window.innerWidth);

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;
        if (type === 'checkbox') {
            const updatedValues = formData[name].includes(value)
                ? formData[name].filter((item) => item !== value)
                : [...formData[name], value];
            setFormData({ ...formData, [name]: updatedValues });
        } else {
            setFormData({ ...formData, [name]: type === 'number' ? parseFloat(value) : value });
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        Axios.post('http://localhost:8080/user/addRecipe', formData)
            .then((response) => {
          
                if (formData.imageFile) {
                    const imageFormData = new FormData();
                    imageFormData.append('file', formData.imageFile);
    
                    Axios.post(`http://localhost:8080/user/recipes/image?recipeId=${response.data}`, imageFormData, {
                        headers: {
                            'Content-Type': 'multipart/form-data',
                        },
                    })
                        .then((imageResponse) => {
                        
                            navigate(`/recipe/${response.data}`);
                            toast.success('Recipe was successfully added');
                        })
                        .catch((imageError) => {
                            console.error('Error uploading image', imageError);
                        });
                } else {
                    navigate(`/recipe/${response.data}`);
                    toast.success('Recipe was successfully added');
                }
            })
            .catch((error) => {
                console.error('Error adding recipe', error);
                console.log(formData);
                toast.error('Error adding recipe');
            });
    };

    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-xl-6 col-sm-10 col-12">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/recipes">recipes</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/myrecipes">my recipes</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/addrecipe">add new recipe</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon />
                        <h1 className='main-title'>ADD NEW RECIPE</h1>
                    </div>
                    <Form
                        formData={formData}
                        setFormData={setFormData}
                        handleInputChange={handleInputChange}
                        handleSubmit={handleSubmit}
                        edit={false}
                    />
                </div>
            </div>
        </div>
    );
}

export default AddRecipeBody;
