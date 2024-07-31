// components/Navbar.js
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { NavLink, useNavigate } from 'react-router-dom';
import { logout, selectUser } from '../../redux/features/counterSlice';

const Navbar = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector(selectUser);

  const handleLogout = () => {
    dispatch(logout());
    localStorage.removeItem('token');
    navigate('/login');
  };
  
  return (
    <nav className="bg-blue-600 p-4 text-white">
      <div className="container mx-auto flex justify-between items-center">
        <div>
          <NavLink to="/" className="text-lg font-semibold">
            Goodminton
          </NavLink>
        </div>
        <div className="flex items-center space-x-4">
          {user && (
            <>
              <span>Xin chào, {user.fullName}!</span>
              <button
                onClick={handleLogout}
                className="bg-red-500 py-2 px-4 rounded hover:bg-red-600 transition duration-200"
              >
                Đăng xuất
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;