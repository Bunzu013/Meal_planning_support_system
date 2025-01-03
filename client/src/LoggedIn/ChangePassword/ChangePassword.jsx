import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import "../Styles/EditData.css"
import BackIcon from '../../icons/BackIcon';
import axios from 'axios';
import toast from 'react-hot-toast';

function ChangePassword() {
    const [formData, setFormData] = useState({
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
    });

    const [errors, setErrors] = useState({
        newPassword: '',
        oldPassword: '',
        confirmPassword: '',
    });

    const [showPassword, setShowPassword] = useState(false);

    const handleTogglePassword = () => {
        setShowPassword(!showPassword);
    };
    const validateForm = () => {
        let newErrors = {};
        if (!formData.oldPassword.trim()) {
            newErrors.oldPassword = 'Old password is required.';
        }
        if (!formData.newPassword.trim()) {
            newErrors.newPassword = 'New password is required.';
        }
        if (!formData.confirmPassword.trim()) {
            newErrors.confirmPassword = 'Confirm password is required.';
        }
        if (formData.newPassword.trim() !== formData.confirmPassword.trim()) {
            newErrors.confirmPassword = "Passwords don't match.";
        }
        if (!/^(?=.*[A-Z])(?=.*\d).{8,}$/.test(formData.newPassword)) {
            newErrors.newPassword = 'Password must be at least 8 characters and contain upper case (A-Z), lower case (a-z) and number (0-9).';
        }
        return newErrors;
    };

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newErrors = validateForm();
        console.log(formData);
        if (Object.keys(newErrors).length === 0) {
            try {
                const response = await axios.post('http://localhost:8080/user/updatePassword', formData,
                    {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                console.log(response.data);
                if (response.status === 200) {
                    setErrors({ newPassword: '', oldPassword: '', confirmPassword: '', });
                    console.log("Password changed successfully");
                    toast.success(`Password changed successfully`, {
                        icon: "✅"
                    });
                }
            } catch (error) {
                if (error.response) {
                    if (error.response.data === 'New password must be different') {
                        setErrors({
                            newPassword: 'New password must be different from the old one',
                            oldPassword: '',
                            confirmPassword: ''
                        });
                    }
                    if (error.response.data === 'Incorrect password') {
                        setErrors({ oldPassword: 'Incorrect password', newPassword: '', confirmPassword: '' })
                        console.error('Password was incorrect', error);
                    }
                    else {
                        console.error('Error updating user password', error);
                        toast.error('Error changing password');
                    }
                }
            }
            if (Object.keys(errors).length === 0) {
                setFormData({ newPassword: '', oldPassword: '', confirmPassword: '', });
                setErrors({ newPassword: '', oldPassword: '', confirmPassword: '', });
            }
        } else {
            setErrors(newErrors);
        }
    };

    return (
        <div className='Page'>
            <div className='body'>
                <div className="container body-content col-10">
                    <div className="little-nav col-xxl-5 col-xl-5 col-l-6 col-md-7 col-8">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/profile">profile</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/editdata">edit data</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/changepassword">change password</Link>
                    </div>
                    <div className="title-row">
                        <BackIcon to="/editdata" />
                        <h1 className='main-title col-xl-6 col-lg-6 col-7'>CHANGE PASSWORD</h1>
                    </div>
                    <br></br>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label className="form-label col-lg-5 col-md-4" htmlFor="oldPassword">OLD PASSWORD</label>
                            <div className="input-container col-lg-9">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    className={`form-control ${errors.oldPassword ? 'is-invalid' : ''}`}
                                    id="oldPassword"
                                    name="oldPassword"
                                    value={formData.oldPassword}
                                    onChange={handleInputChange}
                                />
                                {errors.oldPassword && <div className="invalid-feedback">{errors.oldPassword}</div>}
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="form-label col-lg-5 col-md-4" htmlFor="newPassword">NEW PASSWORD</label>
                            <div className="input-container col-lg-9">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    className={`form-control ${errors.newPassword ? 'is-invalid' : ''}`}
                                    id="newPassword"
                                    name="newPassword"
                                    value={formData.newPassword}
                                    onChange={handleInputChange}
                                />
                                {errors.newPassword && <div className="invalid-feedback">{errors.newPassword}</div>}
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="form-label col-lg-5 col-md-4" htmlFor="confirmPassword">CONFIRM PASSWORD</label>
                            <div className="input-container col-lg-9">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    className={`form-control ${errors.confirmPassword ? 'is-invalid' : ''}`}
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    value={formData.confirmPassword}
                                    onChange={handleInputChange}
                                />
                                {errors.confirmPassword && <div className="invalid-feedback">{errors.confirmPassword}</div>}
                            </div>
                        </div>
                        <div className="flex-row item-center">
                            <button type="button" className="button-hide col-4 col-xlg-3 mt-3" onClick={handleTogglePassword}>
                                {showPassword ? "Hide Passwords" : "Show Passwords"}
                            </button>
                        </div>
                        <div className="flex-row text-center">
                            <button type="submit" className="button col-4 col-xlg-3 mt-4">SAVE</button>
                        </div>
                    </form>
                    <br></br>
                </div>
            </div>
        </div>
    )
}

export default ChangePassword;