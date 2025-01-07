import React from 'react';
import { Link } from 'react-router-dom';

function Form({ formData, handleSubmit, handleChange, errors, isSignIn }) {
    return (
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label className="form-label col-lg-5 col-md-4">EMAIL</label>
                <div className='col-lg-9 input-block'>
                    <input
                        type="email"
                        className="form-control"
                        id="inputEmail"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                    {errors.email && <div className="error-message">{errors.email}</div>}
                </div>
            </div>
            <div className="form-group">
                <label className="form-label col-4">PASSWORD</label>
                <div className='col-lg-9 input-block'>
                    <input
                        type="password"
                        className="form-control"
                        id="inputPassword"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                    {errors.password && <div className="error-message">{errors.password}</div>}
                </div>
            </div>
            {!isSignIn && (
                <>
                    <div className="form-group">
                        <label className="form-label col-lg-5 col-md-4">NAME</label>
                        <div className='col-lg-9 input-block'>
                            <input
                                type="text"
                                className="form-control"
                                id="inputName"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                            {errors.name && <div className="error-message">{errors.name}</div>}
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="form-label col-lg-5 col-md-4">SURNAME</label>
                        <div className='col-lg-9 input-block'>
                            <input
                                type="text"
                                className="form-control"
                                id="inputSurname"
                                name="surname"
                                value={formData.surname}
                                onChange={handleChange}
                                required
                            />
                            {errors.surname && <div className="error-message">{errors.surname}</div>}
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="form-label col-lg-5 col-md-4">PHONE PREFIX</label>
                        <div className='col-lg-9 input-block'>
                            <input
                                type="tel"
                                className="form-control"
                                id="inputPhoneprefix"
                                name="phonePrefix"
                                value={formData.phonePrefix}
                                onChange={handleChange}
                                required
                            />
                            {errors.phonePrefix && <div className="error-message">{errors.phonePrefix}</div>}
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="form-label col-lg-5 col-md-4">PHONE NUMBER</label>
                        <div className='col-lg-9 input-block'>
                            <input
                                type="tel"
                                className="form-control"
                                id="inputPhonenumber"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                required
                            />
                            {errors.phoneNumber && <div className="error-message">{errors.phoneNumber}</div>}
                        </div>
                    </div>
                </>
            )}
            <div className="item-center">
               
                <button type="submit" className="button">
                    {isSignIn ? 'SIGN IN' : 'CREATE ACCOUNT'}
                </button>
                <div className='signUp_link mt-2'>
                    {isSignIn ? <span>Don't have an account? </span> : ''}
                    {isSignIn ? <Link to="/signup">SIGN UP</Link> : ''}
                </div>
            </div>
        </form>
    );
}

export default Form;
