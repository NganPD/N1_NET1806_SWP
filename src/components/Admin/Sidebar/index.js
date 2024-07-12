import React from "react";
import { NavLink } from "react-router-dom";

const Sidebar = () => {
  return (
    <div className="w-64  text-white shadow-lg">
      <div className="p-6 text-center text-[black] text-2xl font-bold border-b border-blue-500">
        Admin Panel
      </div>
      <nav className="mt-4">
        <ul>
          <li className="mb-2">
            <NavLink
              to="/admin/accounts"
              className="text-[black] no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Thông tin tài khoản
            </NavLink>
          </li>
          <li className="mb-2">
            <NavLink
              to="/admin/courts"
              className="text-[black] no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Thông tin Sân cầu lông
            </NavLink>
          </li>
          <li className="mb-2">
            <NavLink
              to="/admin/new-court"
              className="text-[black] no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Đăng ký thông tin sân mới
            </NavLink>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;
