import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import "../Styles/ShoppingList.css"
import axios from 'axios';
import toast from 'react-hot-toast';

function ShoppingList() {
    const [shoppingListStatus, setShoppingListStatus] = useState(false);
    const [shoppingList, setShoppingList] = useState([]);

    useEffect(()=>{
        const getShoppingListStatus = async () => {
            try{
                const response = await axios.get(`http://localhost:8080/user/getShoppingListStatus`);
                setShoppingListStatus(response.data);
            }catch(error){
                console.error("Error getting shopping list status", error);
            }
        }
        getShoppingListStatus();

    }, [])

    useEffect(()=>{
        const getShoppingList = async () => {
            try{
                const response = await axios.post(`http://localhost:8080/user/generateShoppingList`);
                setShoppingList(response.data);
            }catch(error){
                toast.error('Error generating shopping list');
                console.error("Error generating shopping list", error);
            }
        }
        getShoppingList();
    }, [])

    const handleToggle = async () => {
        setShoppingListStatus((prev) => !prev);
        try{
            await axios.post(`http://localhost:8080/user/shoppingListStatus?change=${!shoppingListStatus}`);
        }catch(error){
            console.error("Error posting shopping list status", error);
        }
    };
    
    return(
        <div className='Page'>
            <div className='body'>
                <div className='container body-content col-10'>
                    <div className="little-nav col-xxl-4 col-xl-4 col-l-4 col-md-5 col-sm-8 col-10">
                        <Link to="/">home</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/">mealplan</Link>
                        <span><code>&gt;</code></span>
                        <Link to="/shoppinglist">shopping list</Link>
                    </div>
                    <h1 className='main-title'>SHOPPING LIST</h1>
                    <div className="row justify-content-center">
                        <div className='shopping-list-border col-9'>
                            <ul className="shopping-list text-center">
                            {Object.entries(shoppingList).map(([ingredientName, details]) => (
                                    <li key={ingredientName}>
                                        {ingredientName} - {details[0].quantity} {details[0].unit}
                                    </li>
                            ))}
                            </ul>
                        </div>
                    </div>
                        <div className="row justify-content-center align-items-center">
                            <div className="col-auto">
                                <h4 className="item-center dark-pink-title">DONE?</h4>
                            </div>
                            <div className="col-auto item-center">
                            <button onClick={handleToggle} className={shoppingListStatus ? 'toggled' : ''}>
                                {shoppingListStatus ? 'YES' : 'NO'}
                            </button>
                            </div>
                        </div>
                </div>
            </div>
        </div>
    )
}

export default ShoppingList;