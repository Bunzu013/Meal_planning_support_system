import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Axios from 'axios';
import Menu from './Menu';
import toast from 'react-hot-toast';
import "../Styles/Recipes.css"
import RecipesContent from './RecipesContent';
import AddToPlan from '../Recipe/AddToPlan';
import AddNewFilters from './AddNewFilters';
import EditFilters from './EditFilters';

function RecipesBody() {
    const [recipesData, setRecipesData] = useState([]);
    const [recipeAdd, setRecipeAdd] = useState(null);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [selectedFilters, setSelectedFilters] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [noRecipesFound, setNoRecipesFound] = useState(false);
    const [favoriteRecipesData, setFavoriteRecipesData] = useState([]);
    const [showAllergens, setShowAllergens] = useState(false);
    const [selectedType, setSelectedType] = useState('');
    const [categoryId, setCategoryId] = useState(null);
    const [categoriesData, setCategoriesData] = useState([]);
    const [filtersData, setFiltersData] = useState([]);
    const [filterId, setFilterId] = useState(null);
    const [formData, setFormData] = useState({
        categoryId: '',
        categoryName: '',
        filterId: '',
        filterName: '',
    })


    useEffect(() => {
        Axios.get(`http://localhost:8080/getAllRecipes`)
            .then((response) => {
                setRecipesData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching recipes data', error);
            });

        Axios.get(`http://localhost:8080/user/getFavouriteRecipes`)
            .then((response) => {
                setFavoriteRecipesData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching favorite recipes', error);
            });
    }, []);
    useEffect(() => {
        const filterRecipes = () => {
            const categoriesParam = selectedCategories.join(',');
            const filtersParam = selectedFilters.join(',');
            const allergensParam = showAllergens ? 'hideAllergens=false' : '';
            const typeParam = selectedType ? `type=${selectedType}` : '';

            Axios.get(`http://localhost:8080/getRecipesByCategoriesAndFilters?categoriesN=${categoriesParam}&filtersN=${filtersParam}&${allergensParam}&${typeParam}`)
                .then((response) => {
                    const filteredRecipes = response.data;
                    setRecipesData(filteredRecipes);
                    setCurrentPage(1);
                    setNoRecipesFound(filteredRecipes.length === 0);
                })
                .catch((error) => {
                    console.error('Błąd podczas filtrowania przepisów', error);
                });
        };

        filterRecipes();
    }, [selectedCategories, selectedFilters, showAllergens, selectedType]);

    const fetchFilters = () => {
        Axios.get(`http://localhost:8080/getAllCategories`)
            .then((response) => {
                setCategoriesData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching categories data', error);
            });
        Axios.get(`http://localhost:8080/getAllFilters`)
            .then((response) => {
                setFiltersData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching filters data', error);
            });
    }

    const handleCategoryChange = (categoryName) => {
        if (selectedCategories.includes(categoryName)) {
            setSelectedCategories(selectedCategories.filter((category) => category !== categoryName));
        } else {
            setSelectedCategories([...selectedCategories, categoryName]);
        }
    };
    const handleFilterChange = (filterName) => {
        if (selectedFilters.includes(filterName)) {
            setSelectedFilters(selectedFilters.filter((filter) => filter !== filterName));
        } else {
            setSelectedFilters([...selectedFilters, filterName]);
        }
    };
    const handleShowAllergensChange = () => {
        setShowAllergens(!showAllergens);
    };

    const handleRadioChange = (type) => {
        setSelectedType(type);
    };
    const handleClear = () => {
        setSelectedType('');
        setSelectedCategories([]);
        setSelectedFilters([]);
        setShowAllergens(false);
    };
    const [showAddToPlanOverlay, setShowAddToPlanOverlay] = useState(false);
    const toggleAddToPlanOverlay = () => {
        setShowAddToPlanOverlay(!showAddToPlanOverlay);
    };

    const [showAddNewCategoryOverlay, setShowAddNewCategoryOverlay] = useState(false);
    const toggleAddNewCategoryOverlay = () => {
        setShowAddNewCategoryOverlay(!showAddNewCategoryOverlay);
    };

    const [showAddNewFilterOverlay, setShowAddNewFilterOverlay] = useState(false);
    const toggleAddNewFilterOverlay = () => {
        setShowAddNewFilterOverlay(!showAddNewFilterOverlay);
    };

    const [showEditCategoryOverlay, setEditCategoryOverlay] = useState(false);
    const toggleEditCategoryOverlay = () => {
        setEditCategoryOverlay(!showEditCategoryOverlay);
    };

    const [showEditFilterOverlay, setEditFilterOverlay] = useState(false);
    const toggleEditFilterOverlay = () => {
        setEditFilterOverlay(!showEditFilterOverlay);
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

    const handleCategorySubmit = () => {
        Axios.post('http://localhost:8080/admin/addNewCategory', formData)
            .then((response) => {
                console.log('Category added successfully:', response.data);
                console.log(formData);
                toast.success('Category was successfully added');
                toggleAddNewCategoryOverlay();
                fetchFilters();
            })
            .catch((error) => {
                console.error('Error adding category', error);
                console.log(formData);
                toast.error('Error adding category');
            });
    };

    const handleFilterSubmit = () => {
        Axios.post('http://localhost:8080/admin/addNewFilter', formData)
            .then((response) => {
                console.log('Filter added successfully:', response.data);
                console.log(formData);
                toast.success('Filter was successfully added');
                toggleAddNewFilterOverlay();
                fetchFilters();
            })
            .catch((error) => {
                console.error('Error adding filter', error);
                console.log(formData);
                toast.error('Error adding filter');
            });
    };

    const handleEditCategorySubmit = () => {
        formData.categoryId = categoryId;
        Axios.post('http://localhost:8080/admin/updateCategory', formData)
            .then((response) => {
                console.log('Category edited successfully:', response.data);
                console.log(formData);
                toast.success('Category was successfully edited');
                toggleEditCategoryOverlay();
                fetchFilters();
            })
            .catch((error) => {
                console.error('Error editing category', error);
                console.log(formData);
                toast.error('Error editing category');
            });
    };

    const handleEditFilterSubmit = () => {
        formData.filterId = filterId;
        Axios.post('http://localhost:8080/admin/updateFilter', formData)
            .then((response) => {
                console.log('Category edited successfully:', response.data);
                console.log(formData);
                toast.success('Category was successfully edited');
                toggleEditFilterOverlay();
                fetchFilters();
            })
            .catch((error) => {
                console.error('Error editing category', error);
                console.log(formData);
                toast.error('Error editing category');
            });
    };

    const handleDeleteCategory = (categoryId) => {
        formData.categoryId = categoryId;
        console.log(categoryId);
        Axios.post('http://localhost:8080/admin/deleteCategory', formData)
            .then((response) => {
                console.log('Category deleted successfully:', response.data);
                console.log(formData);
                toast.success('Category was successfully deleted');
                fetchFilters();
            })
            .catch((error) => {
                console.error('Error delete category', error);
                console.log(formData);
            });
    };

    const handleDeleteFilter = (filterId) => {
        formData.filterId = filterId;
        console.log(filterId);
        Axios.post('http://localhost:8080/admin/deleteFilter', formData)
            .then((response) => {
                console.log('Filter deleted successfully:', response.data);
                console.log(formData);
                toast.success('Filter was successfully deleted');
                fetchFilters();
            })
            .catch((error) => {
                console.error('Error delete filter', error);
                console.log(formData);
            });
    };

    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-xxl-3 col-xl-4 col-lg-3 col-md-5 col-sm-8 col-10">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/recipes">recipes</Link>
                    </div>
                    <div className="title-row">
                        <h1 className='main-title'>RECIPES</h1>
                    </div>
                    <div className='d-flex flex-lg-row flex-column'>
                        <Menu
                            selectedCategories={selectedCategories}
                            selectedFilters={selectedFilters}
                            handleCategoryChange={handleCategoryChange}
                            handleFilterChange={handleFilterChange}
                            handleRadioChange={handleRadioChange}
                            handleShowAllergensChange={handleShowAllergensChange}
                            handleClear={handleClear}
                            toggleAddNewCategoryOverlay={toggleAddNewCategoryOverlay}
                            toggleAddNewFilterOverlay={toggleAddNewFilterOverlay}
                            toggleEditCategoryOverlay={toggleEditCategoryOverlay}
                            toggleEditFilterOverlay={toggleEditFilterOverlay}
                            setCategoryId={setCategoryId}
                            setFilterId={setFilterId}
                            handleDeleteCategory={handleDeleteCategory}
                            handleDeleteFilter={handleDeleteFilter}
                            fetchFilters={fetchFilters}
                            categoriesData={categoriesData}
                            filtersData={filtersData}
                        />
                        <RecipesContent
                            noRecipesFound={noRecipesFound}
                            setCurrentPage={setCurrentPage}
                            currentPage={currentPage}
                            isRecipeInFavorites={isRecipeInFavorites}
                            handleAddToFavorites={handleAddToFavorites}
                            recipesData={recipesData}
                            toggleAddToPlanOverlay={toggleAddToPlanOverlay}
                            setRecipeAdd={setRecipeAdd}
                        />
                        {showAddToPlanOverlay && (
                            <AddToPlan
                                recipeId={recipeAdd}
                                toggleAddToPlanOverlay={toggleAddToPlanOverlay}
                            />
                        )}
                        {showAddNewCategoryOverlay && (
                            <AddNewFilters
                                toggleAddNewCategoryOverlay={toggleAddNewCategoryOverlay}
                                handleSubmit={handleCategorySubmit}
                                handleInputChange={handleInputChange}
                                formData={formData}
                            />
                        )}
                        {showEditCategoryOverlay && (
                            <EditFilters
                                toggleEditCategoryOverlay={toggleEditCategoryOverlay}
                                handleSubmit={handleEditCategorySubmit}
                                handleInputChange={handleInputChange}
                                formData={formData}
                            />
                        )}
                        {showAddNewFilterOverlay && (
                            <AddNewFilters
                                toggleAddNewFilterOverlay={toggleAddNewFilterOverlay}
                                handleSubmit={handleFilterSubmit}
                                handleInputChange={handleInputChange}
                                formData={formData}
                                filter={true}
                            />
                        )}
                        {showEditFilterOverlay && (
                            <EditFilters
                                toggleEditFilterOverlay={toggleEditFilterOverlay}
                                handleSubmit={handleEditFilterSubmit}
                                handleInputChange={handleInputChange}
                                formData={formData}
                                filter={true}
                            />
                        )}
                    </div>
                </div>
            </div>
        </div>
    )
}
export default RecipesBody;