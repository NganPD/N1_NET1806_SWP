import React from "react";
import { NavLink, Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout, selectUser } from "../../redux/features/counterSlice";
import Swal from "sweetalert2";
import goodminton from "D:/swp/N1_NET1806_SWP/src/download__1_-removebg-preview.png";
const Header = () => {
  const user = useSelector(selectUser);
  const dispatch = useDispatch();

  const handleLogout = () => {
    dispatch(logout());
    localStorage.removeItem("token");
    Swal.fire({
      icon: "success",
      title: "Logout successfully!",
      showConfirmButton: false,
      timer: 2000,
    });
  };

  return (
    <header className="bg-white shadow-md">
      <div className="bg-blue-600 py-2">
        <div className="container mx-auto px-6 flex justify-between items-center">
          <Link to="/" className="flex items-center">
            <img
              src={goodminton}
              style={{ filter: "brightness(0) invert(1)" }}
              alt="Logo"
              className="h-20 w-auto ml-9"

            />
          </Link>

          <nav className="flex space-x-4">
            <NavLink
              exact
              to="/"
              className="bg-white text-blue-600 px-4 py-2 rounded-full hover:bg-yellow-500 hover:text-white"
              activeClassName="bg-yellow-500 text-white"
            >
              Trang chủ
            </NavLink>
            <NavLink
              to="/courts"
              className="text-white hover:text-yellow-500 px-4 py-2"
              activeClassName="text-yellow-500"
            >
              Sân cầu lông
            </NavLink>
          
            <NavLink
              to="/contact"
              className="text-white hover:text-yellow-500 px-4 py-2"
              activeClassName="text-yellow-500"
            >
              Liên hệ
            </NavLink>
        
          </nav>

          {user ? (
            <div className="flex items-center space-x-4">
              <NavLink
                to="/profile"
                className="text-white hover:text-yellow-500"
              >
                Xin chào, {user.fullName}
              </NavLink>
              <button
                className="bg-white text-blue-600 px-4 py-2 rounded-full hover:bg-yellow-500 hover:text-white"
                onClick={handleLogout}
              >
                Đăng xuất
              </button>
            </div>
          ) : (
            <div className="flex items-center space-x-4">
              <Link
                to="/register"
                className="bg-yellow-500 text-white px-4 py-2 rounded-full hover:bg-yellow-600"
              >
                Đăng ký
              </Link>
              <Link
                to="/login"
                className="bg-white text-blue-600 px-4 py-2 rounded-full hover:bg-yellow-500 hover:text-white"
              >
                Đăng nhập
              </Link>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
