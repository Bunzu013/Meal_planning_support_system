import {React, useState, useEffect} from 'react';
import { Link } from 'react-router-dom';
import "../Styles/Profile.css";
import "../Styles/ToggleButton.css";
import axios from 'axios';

function ProfileBody() {
    const [userData, setUserData] = useState({});
    const phonePrefix = userData.phonePrefix || null;
    const phoneNumber = userData.phoneNumber || null;
    const [hiddenCalories, setHiddenCalories] = useState(false);
    const userName = userData.userName || null;
    const email = userData.email || null;
    const userSurname = userData.userSurname || null;

    useEffect(() => {
        const getUser = async () => {
            axios.get(`http://localhost:8080/user/getUserData`)
            .then((response) => {
                setUserData(response.data);
                setHiddenCalories(response.data.hiddenCalories);
                console.log(response.data);
            })
            .catch((error) => {
                console.error('Error fetching user data', error);
            });
        }
        getUser();
    }, []);

    useEffect(()=>{
        console.log("useeffect:",hiddenCalories);
    }, [hiddenCalories])

    const handleToggle = async () => {
        setHiddenCalories((prev) => !prev);
        try{
            console.log(hiddenCalories);
            await axios.post(`http://localhost:8080/user/hideCalories?hide=${!hiddenCalories}`);
        }catch(error){
            console.error("Error posting hide calories status", error);
        }
    };

    return(
        <div className="Page">
            <div className='body'>
                <div className="container body-content col-10">
                    <div className="little-nav col-1">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/profile">profile</Link>
                    </div>
                    <div className="row">
                        <h1 className='main-title col-xl-9 col-lg-8 col-md-7 col-sm-7 col-7'>PROFILE</h1>
                        <div className="col-xl-2 col-lg-3 col-sm-4 col-md-4 col-4">
                            <div>
                                <Link to="/editdata">
                                    <button className="btn button-maindark">EDIT DATA</button> 
                                </Link>
                            </div>
                        </div>
                    </div>
                    <br></br>
                    <div className="profile-data">
                        <div className="row">
                            <h4 className="brown-title col-auto">NAME:</h4>
                            <h5 className="brown-title col-auto">{userName}</h5>
                        </div>
                        <div className="row">
                            <h4 className="brown-title col-auto">SURNAME:</h4>
                            <h5 className="brown-title col-auto">{userSurname}</h5>
                        </div>
                        <div className="row">
                            <h4 className="brown-title col-auto">EMAIL:</h4>
                            <h5 className="brown-title col-auto">{email}</h5>
                        </div>
                        <div className="row">
                            <h4 className="brown-title col-auto">NUMBER:</h4>
                            <h5 className="brown-title col-auto">+{phonePrefix + " " + phoneNumber}</h5>
                        </div>
                    </div>
                    <br></br>
                    <Link to="/allergens"><button className="btn button-redirect col-12">ALLERGENS &gt;</button></Link>
                    <div className="col-12"> <br></br></div>
                    <Link to="/preferredingredients"><button className="btn button-redirect col-12">PREFERRED INGREDIENTS &gt;</button></Link>
                    <div className="col-12"><br></br></div>
                    <div className="row">
                        <h5 className="pink-title col-auto">SHOW RECIPE CALORIES</h5>
                        <div className="col-auto">
                            <button onClick={handleToggle} className={hiddenCalories ? 'toggled' : ''}>
                                {hiddenCalories ? 'YES' : 'NO'}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ProfileBody;