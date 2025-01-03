import React, { useState, useEffect } from 'react';
import Axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import toast from 'react-hot-toast';


function AddNewFilters({ toggleEditFilterOverlay, toggleEditCategoryOverlay, handleSubmit, handleInputChange, formData, filter }) {

    return (
        <div className="overlay">
            <div className="container body-content col-5 overlay-content">
                <div className="title-row">
                    {filter ?
                        <Link onClick={toggleEditFilterOverlay} А>
                            <BackIcon />
                        </Link>
                        :
                        <Link onClick={toggleEditCategoryOverlay} А>
                            <BackIcon />
                        </Link>
                    }
                    <h1 className='main-title col-10'>EDIT {filter ? 'FILTER' : 'CATEGORY'}</h1>
                </div>
                <div className="row mt-1">
                    <div className="mb-3 d-flex justify-content-between">
                        <label className='main-text col-4' htmlFor="inputRecipename"> {filter ? 'Filter' : 'Category'} Name: </label>
                        {filter ?
                            <input
                                className="form-control"
                                style={{ 'marginRight': 0 }}
                                name='filterName'
                                value={formData.filterName}
                                onChange={handleInputChange}
                                required
                            />
                            :
                            <input
                                className="form-control"
                                style={{ 'marginRight': 0 }}
                                name='categoryName'
                                value={formData.categoryName}
                                onChange={handleInputChange}
                                required
                            />
                        }
                    </div>
                </div>
                <br />
                <div className="d-flex justify-content-center">
                    <button type="submit" className="button" onClick={handleSubmit}>
                        EDIT
                    </button>
                </div>
            </div>
        </div>
    );
}
export default AddNewFilters;