import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import toast from 'react-hot-toast';
import axios from 'axios';
import Form from './FormSign';

function SignUpBody() {
    const [formData, setFormData] = useState({
        name: '',
        surname: '',
        email: '',
        password: '',
        phoneNumber: '',
        phonePrefix: '',
    });
    const navigate = useNavigate();
    const [errors, setErrors] = useState({});

    const validateFormData = () => {
        const errors = {};
        if (!/^(?=.*[A-Z])(?=.*\d).{8,}$/.test(formData.password)) {
            errors.password = 'Password must be at least 8 characters and contain upper case (A-Z), lower case (a-z) and number (0-9)';
        }
        if (!/^[A-Za-z\s]+$/.test(formData.name)) {
            errors.name = 'Name can only contain letters and spaces';
        }
        if (!/^[A-Za-z\s-]+$/.test(formData.surname)) {
            errors.surname = 'Surname can only contain letters, spaces, and hyphens';
        }
        if (!/^[0-9]{1,3}$/.test(formData.phonePrefix)) {
            errors.phonePrefix = 'Phone prefix can have 1 to 3 digits';
        }
        return errors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const validationErrors = validateFormData();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }
        try {
            const response = await axios.post('http://localhost:8080/guest/signup', formData, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 201) {
                navigate('/signin');
                toast.success('Successfully register');

            } else {
                console.error('Error during user registration');
            }
        } catch (error) {
            if (error.response) {
                if (error.response.status === 409) {
                    const errorMessage = error.response.data;
                    if (errorMessage === 'User with this email already exists') {
                        setErrors({ email: 'User with this email already exists' });
                    } else if (errorMessage === 'User with this phone number already exists') {
                        setErrors({ phoneNumber: 'User with this phone number already exists' });
                    }
                } else {
                    console.error('Error while communicating with the server', error);
                }
            }
        }

    };
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
        setErrors({ ...errors, [name]: '' });
    };
    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-lg-3 col-md-4 col-8">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/signup">sign up</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon to="/" />
                        <h1 className='main-title'>SIGN UP</h1>
                    </div>
                    <Form 
                        formData={formData} 
                        isSignIn={false}
                        handleChange={handleChange}
                        handleSubmit={handleSubmit}
                        errors={errors} 
                    />
                </div>
            </div>
        </div>
    )
}

export default SignUpBody;