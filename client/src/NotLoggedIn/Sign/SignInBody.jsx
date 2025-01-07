import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';
import toast from 'react-hot-toast';
import Form from './FormSign';
import axios from 'axios';


function SignInBody() {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const validateForm = () => {
        const newErrors = {};
        if (!formData.email) {
            newErrors.email = 'Email is required';
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = 'Invalid email format';
        }

        if (!formData.password) {
            newErrors.password = 'Password is required';
        } else if (formData.password.length < 6) {
            newErrors.password = 'Password must be at least 8 characters long';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) {
            toast.error('Please fix the errors in the form');
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/guest/login', formData, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (response.status === 200) {
                const token = response.data;
                localStorage.setItem('token', token);
                navigate('/recipes');
                toast.success('Login to your account was successful');
                window.location.reload(); 
            } else {
                console.error('Login error:', response.data.message);
            }
        } catch (error) {
            if (error.response) {
                if (error.response.status === 401) {
                    setErrors({ password: 'Email or password entered incorrectly' });
                } else {
                    console.error('Error communicating with server', error);
                }
            }
        }
    };

    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-lg-3 col-md-4 col-8">
                        <Link to="/notloggedin">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/signin">sign in</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon to="/notloggedin" />
                        <h1 className='main-title'>SIGN IN</h1>
                    </div>
                    <Form 
                        formData={formData} 
                        errors={errors} 
                        handleChange={handleChange} 
                        handleSubmit={handleSubmit} 
                        isSignIn={true} 
                    />
                </div>
            </div>
        </div>
    );
}

export default SignInBody;
