import React from 'react';
import { Link } from 'react-router-dom';
import './Icons.css';

function BackIcon({to}) {
    return (
        <Link to={to}>
            <div className="circle">
                <svg version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 90 90" className="svg-icon">
                    <g>
                        <g>
                            <path fill="#ffffff" d="M90,79c0,0-10-48.667-53.875-48.667V11L0,43.276l36.125,33.455V54.94C59.939,54.94,77.582,57.051,90,79z" />
                        </g>
                    </g>
                </svg>
            </div>
        </Link>
    )
}
export default BackIcon;