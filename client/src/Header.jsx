import * as React from 'react';
import { Link } from 'react-router-dom';
import { styled, useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';

const drawerWidth = 240;

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
    justifyContent: 'flex-start',
}));

export default function LoggedInHeader() {
    const theme = useTheme();
    const [open, setOpen] = React.useState(false);
    const [isSmallScreen, setIsSmallScreen] = React.useState(window.innerWidth < 992);
    const [isLoggedIn, setIsLoggedIn] = React.useState(!!localStorage.getItem('token'));

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
    };

    React.useEffect(() => {
        const handleResize = () => {
            setIsSmallScreen(window.innerWidth < 992);
        };

        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    React.useEffect(() => {
        setIsLoggedIn(!!localStorage.getItem('token'));
    }, []);

    return (
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <header className="header">
                <nav className="navbar navbar-expand-lg">
                    <div className="container">
                    {isLoggedIn ?
                        <Link to="/" className="logo">MEALPLANNER</Link>
                        :
                        <Link to="/notloggedin" className="logo">MEALPLANNER</Link>
                    }
                        <IconButton
                            className="navbar-toggler"
                            color="inherit"
                            aria-label="open drawer"
                            edge="end"
                            onClick={handleDrawerOpen}
                            sx={{ ...(open && { display: 'none' }) }}
                        >
                            <MenuIcon />
                        </IconButton>
                        {isLoggedIn ?
                            <ul className={`navbar-nav ${isSmallScreen ? 'd-none' : ''}`}>
                                <li className="nav-item">
                                    <Link to="/mealplan" className="nav-link">mealplan</Link>
                                </li>
                                <li className="nav-item">
                                    <Link to="/recipes" className="nav-link">recipes</Link>
                                </li>
                                <li className="nav-item">
                                    <Link to="/profile" className="nav-link">profile</Link>
                                </li>
                                <li className="nav-item">
                                    <Link to="/signin" className="nav-link" style={{ color: '#ec8a8a' }} onClick={handleLogout}>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                                            <polyline points="16 17 21 12 16 7" />
                                            <line x1="21" y1="12" x2="9" y2="12" />
                                        </svg>
                                    </Link>
                                </li>
                            </ul>
                            :
                            <ul className={`navbar-nav ${isSmallScreen ? 'd-none' : ''}`}>
                                <li class="nav-item">
                                    <Link to="/signup" class="nav-link">sign up</Link>
                                </li>
                                <li class="nav-item">
                                    <Link to="/signin" class="nav-link">sign in</Link>
                                </li>
                            </ul>
                        }
                    </div>
                </nav>
                <Drawer
                    sx={{
                        width: drawerWidth,
                        flexShrink: 0,
                        '& .MuiDrawer-paper': {
                            width: drawerWidth,
                        },
                    }}
                    variant="persistent"
                    anchor="right"
                    open={open}
                >
                    <DrawerHeader>
                        <IconButton onClick={handleDrawerClose}>
                            {theme.direction === 'rtl' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
                        </IconButton>
                    </DrawerHeader>
                    {isLoggedIn ?
                        <List sx={{ display: 'flex', flexDirection: 'column' }}>
                            <Link to="/" className='small-nav'>mealplan</Link>
                            <Link to="/recipes" className='small-nav'>recipes</Link>
                            <Link to="/profile" className='small-nav'>profile</Link>
                            <Link to="/" className='small-nav'>logout</Link>
                        </List>
                        :
                        <List sx={{ display: 'flex', flexDirection: 'column' }}>
                            <Link to="/signup" className='small-nav'>sign up</Link>
                            <Link to="/signin" className='small-nav'>sign in</Link>
                        </List>
                    }
                </Drawer>
            </header >
        </Box>
    );
}