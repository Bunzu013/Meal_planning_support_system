import React, { useState, useEffect } from 'react';
import Axios from 'axios';
import '../Styles/Menu.css'
import { jwtDecode } from "jwt-decode";
import toast from 'react-hot-toast';
import EditIcon from '../../icons/EditIcon'
import DeleteIcon from '../../icons/DeleteIcon'


function Menu({ selectedCategories, fetchFilters, categoriesData, filtersData, selectedFilters, toggleEditCategoryOverlay, handleCategoryChange, handleFilterChange, handleRadioChange, handleShowAllergensChange, handleClear, toggleAddNewCategoryOverlay, toggleAddNewFilterOverlay, setCategoryId, toggleEditFilterOverlay, setFilterId, handleDeleteFilter, handleDeleteCategory }) {
    const [selectedType, setSelectedType] = useState('');

    const [userRoles, setUserRoles] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);

                const roles = decoded.roles || [];

                setUserRoles(roles);
            } catch (error) {
                console.error('Error decoding JWT:', error);
            }
        }
    }, []);
    const isAdmin = userRoles.includes('ROLE_ADMIN');

    useEffect(() => {
        fetchFilters();
    }, []);



    return (
        <div className="col-lg-3 col-12">
            <div className="menu">
                <h2>CATEGORIES</h2>
            
                <ul className='menu-text'>
                    {categoriesData.map((category, index) => (
                        <li key={index} className='d-flex flex-row justify-content-between'>
                            <div className='d-flex flex-row col-8'>
                                <input className="form-check-input" type="checkbox"
                                    id={category.categoryName} value={category.categoryName}
                                    checked={selectedCategories.includes(category.categoryName)}
                                    onChange={() => handleCategoryChange(category.categoryName)}
                                />
                                <label htmlFor={category.categoryName}>{category.categoryName}</label>
                            </div>
                            {isAdmin && (
                                <div className="d-flex flex-row">
                                    <button className="btn pt-0" onClick={() => {
                                        setCategoryId(category.categoryId);
                                        toggleEditCategoryOverlay(category.categoryId)
                                    }}>
                                        <EditIcon />
                                    </button>
                                    <button className="btn pt-0" onClick={() => { handleDeleteCategory(category.categoryId) }}>
                                        <DeleteIcon />
                                    </button>
                                </div>
                            )}

                        </li>
                    ))}
                </ul>
                {isAdmin && (
                    <div className='item-center'>
                        <button className="button-clear" onClick={() => { toggleAddNewCategoryOverlay() }}>
                            Add New Category
                        </button>
                    </div>
                )}
            </div>
            <div className="menu">
                <h2>FILTERS</h2>
                <ul className='menu-text'>
                    {filtersData.map((filter, index) => (
                        <li key={index} className='d-flex flex-row justify-content-between'>
                            <div className='d-flex flex-row col-8'>
                                <input
                                    className="form-check-input"
                                    type="checkbox"
                                    id={filter.filterName}
                                    value={filter.filterName}
                                    checked={selectedFilters.includes(filter.filterName)}
                                    onChange={() => handleFilterChange(filter.filterName)}
                                />
                                <label htmlFor={filter.filterName}>{filter.filterName}</label>
                            </div>
                            {isAdmin && (
                                <div className="d-flex flex-row">
                                    <button className="btn pt-0" onClick={() => { setFilterId(filter.filterId); toggleEditFilterOverlay() }}>
                                        <EditIcon />
                                    </button>
                                    <button className="btn pt-0" onClick={() => { handleDeleteFilter(filter.filterId) }}>
                                        <DeleteIcon />
                                    </button>
                                </div>
                            )}
                        </li>
                    ))}
                    {isAdmin && (
                        <div className='item-center'>
                            <button className="button-clear" onClick={() => { toggleAddNewFilterOverlay() }}>Add New Filter</button>
                        </div>
                    )}
                </ul>

                <hr className='linia'></hr>
                <ul className='menu-text'>
                    <li className="list-group-item ">
                        <input
                            className="form-check-input mt-1"
                            type="radio"
                            name="listGroupRadio"
                            value="1"
                            checked={selectedType === '1'}
                            onChange={() => handleRadioChange('1')}
                        />
                        <label className="form-check-label">My Favorites</label>
                    </li>
                    <li className="list-group-item">
                        <input className="form-check-input mt-1" type="radio" name="listGroupRadio" value="2" checked={selectedType === '2'} onChange={() => handleRadioChange('2')} />
                        <label className="form-check-label">Preffered Ingredients</label>
                    </li>
                    <li className="list-group-item">
                        <input className="form-check-input mt-1" type="radio" name="listGroupRadio" value="3" checked={selectedType === '3'} onChange={() => handleRadioChange('3')} />
                        <label className="form-check-label">My Recipes</label>
                    </li>
                </ul>
                <hr className='linia'></hr>
                <div className="menu-text mb-2">
                    <input
                        className="form-check-input"
                        type="checkbox"
                        onChange={handleShowAllergensChange}
                    />
                    <label>Show recipes with allergens</label>
                </div>
                <div className='item-center'>
                    <button className="button-clear" onClick={handleClear}>CLEAR</button>
                </div>
            </div>
        </div>
    );
}
export default Menu;
