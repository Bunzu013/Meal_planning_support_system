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
    const navigate = useNavigate();
    const [errors, setErrors] = useState({});
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
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
                console.log(localStorage.getItem('token'));
            } else {
                console.error('Błąd logowania:', response.data.message);
                console.log(localStorage.getItem('token'));
            }
        } catch (error) {
            if (error.response) {
                if (error.response.status === 401) {
                    setErrors({ password: 'Email or password entered incorrectly' });
                    console.log(localStorage.getItem('token'));
                } else {
                    console.error('Błąd podczas komunikacji z serwerem', error);
                    console.log(localStorage.getItem('token'));
                }
            }
        }
    };

    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-lg-3 col-md-4 col-8">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/signin">sign in</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon to="/" />
                        <h1 className='main-title'>SIGN IN</h1>
                    </div>
                    <Form formData={formData} errors={errors} handleChange={handleChange} handleSubmit={handleSubmit} isSignIn={true} />
                </div>
            </div>
        </div>
    )
}

export default SignInBody;