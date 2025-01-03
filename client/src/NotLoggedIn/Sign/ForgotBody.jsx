import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import BackIcon from '../../icons/BackIcon';

function ForgotPassword() {
    const [email, setEmail] = useState('');

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    }
    const handleSubmit = async (e) => {
        e.preventDefault();

        // try {
        //     const response = await fetch('http://localhost:8080/forgot_password', {
        //         method: 'POST',
        //         headers: {
        //             'Content-Type': 'application/json',
        //         },
        //         body: JSON.stringify({ email }),
        //     });

        //     if (response.ok) {
        //         const data = await response.json();
        //         if (data.message) {
        //             alert(data.message);
        //         } else if (data.error) {
        //             alert(data.error);
        //         }
        //     } else {
        //         alert('An error occurred while processing your request1');
        //         console.log(email);
        //     }
        // } catch (error) {
        //     alert('An error occurred while processing your request.');
        //     console.log(email);
        // }
    }

    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-xl-4 col-md-6 col-10 mb-3">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/signin">sign in</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/forgot_password">forgot password</Link>
                    </div>
                    <div className="title-row">
                        <Link to="/signin">
                            <BackIcon />
                        </Link>
                        <h1 className='main-title'>FORGOT PASSWORD</h1>
                    </div>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label className="form-label" htmlFor="email">EMAIL</label>
                            <input
                                type="email"
                                className="form-control"
                                id="email"
                                aria-describedby="emailHelp"
                                value={email}
                                onChange={handleEmailChange}
                            />
                        </div>
                        <div className='item-center'>
                            <button type="submit" className="button">RESET PASSWORD</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default ForgotPassword;
