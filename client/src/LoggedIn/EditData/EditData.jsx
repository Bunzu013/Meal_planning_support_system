import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import "../Styles/EditData.css"
import BackIcon from '../../icons/BackIcon';
import axios from 'axios';
import toast from 'react-hot-toast';

function EditData() {
    const [userData, setUserData] = useState({});
    const [formData, setFormData] = useState({
        userName: '',
        userSurname: '',
        phoneNumber: '',
    });

    const [errors, setErrors] = useState({
        userName: '',
        userSurname: '',
        phoneNumber: '',
    });
    useEffect(() => {
        const getUser = async () => {
            await axios.get(`http://localhost:8080/user/getUserData`)
                .then((response) => {
                    setUserData(response.data);
                    console.log(response.data);
                })
                .catch((error) => {
                    console.error('Error fetching user data', error);
                });
        }
        getUser();
    }, []);

    const validateForm = () => {
        let newErrors = {};
        if (!formData.userName.trim()) {
            newErrors.userName = 'Name is required';
        } else if (!/^[A-Za-z\s]+$/.test(formData.userName)) {
            newErrors.userName = 'Name can only contain letters and spaces.';
        }

        if (!formData.userSurname.trim()) {
            newErrors.userSurname = 'Surname is required';
        } else if (!/^[A-Za-z\s-]+$/.test(formData.userSurname)) {
            newErrors.userSurname = 'Surname can only contain letters, spaces, and hyphens.';
        }

        if (!formData.phoneNumber) {
            newErrors.phoneNumber = 'Phone number is required';
        } else if (!/^\d{9}$/.test(formData.phoneNumber)) {
            newErrors.phoneNumber = 'Please enter a valid 9-digit phone number.';
        }

        return newErrors;
    };

    const handleInputChange = (e) => {
        let value = e.target.value;
        if (e.target.name === 'phoneNumber') {
            value = parseInt(value, 10);
            if (isNaN(value)) {
                value = userData.phoneNumber;
            }
        }
        setFormData({ ...formData, [e.target.name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newErrors = validateForm();
        console.log(formData);
        if (Object.keys(newErrors).length === 0) {
            try {
                const response = await axios.post('http://localhost:8080/user/updateUserData', formData, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (response.status === 200) {
                    console.log("User data updated successfully");
                    toast.success(`User data updated successfully`, {
                        icon: "✅"
                    });
                }
            } catch (error) {
                console.error('Error updating user data', error);
            }
            setFormData({ userName: '', userSurname: '', phoneNumber: '' });
            setErrors({ userName: '', userSurname: '', phoneNumber: '' });
        } else {
            setErrors(newErrors);
        }
    };

    return (
        <div className='Page'>
            <div className='body'>
                <div className="container body-content col-10">
                    <div className="little-nav col-xxl-3 col-xl-3 col-l-2 col-md-5 col-9">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/profile">profile</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/editdata">edit data</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon to="/profile" />
                        <h1 className='main-title col-xl-6 col-lg-6 col-7'>EDIT DATA</h1>
                    </div>
                    <br></br>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label className="form-label col-lg-5 col-md-4" htmlFor="userName">NAME</label>
                            <div className="input-container col-lg-9">
                                <input
                                    type="text"
                                    className={`form-control ${errors.userName ? 'is-invalid' : ''}`}
                                    id="userName"
                                    name="userName"
                                    value={formData.userName}
                                    onChange={handleInputChange}
                                />
                                {errors.userName && <div className="invalid-feedback">{errors.userName}</div>}
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="form-label col-lg-5 col-md-4" htmlFor="userSurname">SURNAME</label>
                            <div className="input-container col-lg-9">
                                <input
                                    type="text"
                                    className={`form-control ${errors.userSurname ? 'is-invalid' : ''}`}
                                    id="userSurname"
                                    name="userSurname"
                                    value={formData.userSurname}
                                    onChange={handleInputChange}
                                />
                                {errors.userSurname && <div className="invalid-feedback">{errors.userSurname}</div>}
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="form-label col-lg-5 col-md-4" htmlFor="phoneNumber">PHONE NUMBER</label>
                            <div className="input-container col-lg-9">
                                <input
                                    type="tel"
                                    className={`form-control ${errors.phoneNumber ? 'is-invalid' : ''}`}
                                    id="phoneNumber"
                                    name="phoneNumber"
                                    value={formData.phoneNumber}
                                    onChange={handleInputChange}
                                />
                                {errors.phoneNumber && <div className="invalid-feedback">{errors.phoneNumber}</div>}
                            </div>
                        </div>
                        <div className="item-center">
                            <button type="submit" className="button">SAVE</button>
                        </div>
                    </form>
                    <br></br>
                    
                </div>
            </div>
        </div>
    )
}

export default EditData;