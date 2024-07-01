import React from "react";
import { NavLink, Link } from "react-router-dom";
import { FaSearch, FaShoppingCart } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import { logout, selectUser } from "../../redux/features/counterSlice";
import Swal from "sweetalert2";

const Header = () => {
  const user = useSelector(selectUser);
  const dispatch = useDispatch();
  console.log("user", user);

  const handleLogout = () => {
    dispatch(logout());
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
              src="https://sieuthicaulong.vn/images/logo/1678420509-final-logo.png?ver=1.2.2"
              alt="Logo"
              className="h-12 mr-2"
              style={{ filter: "brightness(0) invert(1)" }}
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
              to="/news"
              className="text-white hover:text-yellow-500 px-4 py-2"
              activeClassName="text-yellow-500"
            >
              Bản tin
            </NavLink>
            <NavLink
              to="/knowledge"
              className="text-white hover:text-yellow-500 px-4 py-2"
              activeClassName="text-yellow-500"
            >
              Chia sẻ kiến thức
            </NavLink>
            <NavLink
              to="/contact"
              className="text-white hover:text-yellow-500 px-4 py-2"
              activeClassName="text-yellow-500"
            >
              Liên hệ
            </NavLink>
            <NavLink
              to="/about"
              className="text-white hover:text-yellow-500 px-4 py-2"
              activeClassName="text-yellow-500"
            >
              Về chúng tôi
            </NavLink>
          </nav>
          {user ? (
            <h2
              style={{
                cursor: "pointer",
              }}
              onClick={handleLogout}
            >
              Logout
            </h2>
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
