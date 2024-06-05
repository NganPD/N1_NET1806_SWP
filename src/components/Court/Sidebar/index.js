import React from "react";
import { NavLink } from "react-router-dom";

const Sidebar = () => {
  return (
    <div className="w-64 bg-gray-800 text-white shadow-lg">
      <div className="p-6 text-center text-white text-2xl font-bold border-b border-blue-500">
        Court Manager
      </div>
      <nav className="mt-4">
        <ul>
          <li className="mb-2">
            <NavLink
              to="/court-manager/register-court-info"
              className="text-white no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Đăng ký thông tin sân
            </NavLink>
          </li>
          <li className="mb-2">
            <NavLink
              to="/court-manager/register-booking-type"
              className="text-white no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Đăng ký loại hình đặt lịch
            </NavLink>
          </li>
          <li className="mb-2">
            <NavLink
              to="/court-manager/manage-courts"
              className="text-white no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Quản lý thông tin sân
            </NavLink>
          </li>
          <li className="mb-2">
            <NavLink
              to="/court-manager/manage-schedules"
              className="text-white no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Quản lý lịch thi đấu
            </NavLink>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;
