// src/components/Header/Header.js
import React from "react";
import { LOGO } from "../../assets";
import { NavLink } from "react-router-dom";

const Header = () => {
  return (
    <header className="pb-3">
      <div className="w-full  m-0 flex flex-column items-center justify-between gap-4">
        <nav className="w-full p-3 flex items-center justify-center space-x-20 bg-gray-100">
          <NavLink to="/" className="flex items-center">
            <img src={LOGO} alt="Logo" className="h-[5rem]  rounded-full" />
          </NavLink>
          <NavLink
            to="/"
            className="text-gray-900 font-bold no-underline  hover:text-blue-600"
          >
            Home
          </NavLink>
          <NavLink
            to="/courts"
            className="text-gray-500 no-underline hover:text-blue-600"
          >
            Court
          </NavLink>
          <NavLink
            to="/contact"
            className="text-gray-500 no-underline hover:text-blue-600"
          >
            Contacts
          </NavLink>
          <div class="flex flex-row gap-2">
            <NavLink
              to="login"
              className="border border-blue-500 text-blue-500 rounded-full px-4 py-2 hover:bg-blue-100 no-underline"
            >
              Sign in
            </NavLink>
            <NavLink
              to="register"
              className="bg-[#5BD1FF] text-white rounded-full px-4 py-2 hover:bg-blue-600 no-underline"
            >
              Sign up
            </NavLink>
          </div>
        </nav>
      </div>
    </header>
  );
};

export default Header;
