import React from "react";
import { Link } from "react-router-dom";

function LoggedInFooter() {
    const [isLoggedIn, setIsLoggedIn] = React.useState(!!localStorage.getItem('token'));

    React.useEffect(() => {
        setIsLoggedIn(!!localStorage.getItem('token'));
    }, []);

    return (
        <footer className="footer">
            <div className="container">
                {isLoggedIn ?
                    <div className="row">
                        <div className="col-xl-3 col-lg-3 col-3 d-flex flex-column">
                            <h1 className="footer-logo">MEALPLANNER</h1>
                        </div>
                        <div className="col-md-2 d-flex flex-column">
                        <Link to="/" className="main-row">Home</Link>
                        <Link to="/mealplan" className="main-row">Mealplan</Link>
                        </div>
                        <div className="col-md-2 d-flex flex-column">
                            <Link to='/recipes' className="main-row">Recipes</Link>
                            <Link to='/favorites'>My Favorites</Link>
                            <Link to='/myrecipes'>My Recipes</Link>
                        </div>
                        <div className="col-3 d-flex flex-column">
                            <Link to='/profile' className="main-row">Profile</Link>
                            <Link to='/preferredingredients'>Preferred Ingredients</Link>
                            <Link to='/allergens'>Allergens</Link>
                        </div>
                        <div className="col-md-2 d-flex flex-column">
                            <Link to="/contact" className="main-row">Help</Link>
                            <Link to="/contact">Contact</Link>
                        </div>
                    </div>
                    :
                    <div className="row">
                        <div className="col-xl-4 col-lg-3 col-3 d-flex flex-column">
                            <h1 className="footer-logo">MEALPLANNER</h1>
                        </div>
                        <div className="col-md-2 d-flex flex-column">
                            <Link to="/notloggedin" className="main-row">Home</Link>
                        </div>
                        <div className="col-md-2 d-flex flex-column">
                            <Link to="/signup" className="main-row">Sign Up</Link>
                        </div>
                        <div className="col-md-2 d-flex flex-column">
                            <Link to="/signin" className="main-row">Sign In</Link>
                            <Link to="/forgotpassword">Forgot Password</Link>
                        </div>
                        <div className="col-md-2 d-flex flex-column">
                            <Link to="/contact" className="main-row">Help</Link>
                            <Link to="/contact">Contact</Link>
                        </div>
                    </div>
                }
            </div>
        </footer>

    );
}

export default LoggedInFooter;