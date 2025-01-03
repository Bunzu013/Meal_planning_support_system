import React, { useState, useEffect } from 'react';

function Contact() {

    return (
        <div className="Page">
            <div className='body'>
                <div className='container body-content col-10 d-flex flex-column align-items-center'>
                    <p className="main-title" style={{marginLeft: 0}}>CONTACT</p>
                    <div className="d-flex flex-row">
                        <p className="main-text">Phone: </p>
                        <span className="brown-title">+48 002193821</span>
                    </div>
                    <div className="d-flex flex-row">
                        <p className="main-text">Email: </p>
                        <span className="brown-title">mealplanhelp@gmail.com</span>
                    </div>
                    <div className="d-flex flex-row">
                        <p className="main-text">Working time: </p>
                        <span className="brown-title">8:00-18:00 Monday to Friday</span>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default Contact;