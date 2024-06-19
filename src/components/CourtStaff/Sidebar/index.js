import React from "react";
import { NavLink } from "react-router-dom";

const Sidebar = () => {
  return (
    <div className="w-64 bg-gray-800 text-white shadow-lg">
      <div className="p-6 text-center text-white text-2xl font-bold border-b border-blue-500">
        Court Staff
      </div>
      <nav className="mt-4">
        <ul>
          <li className="mb-2">
            <NavLink
              to="/court-staff/court-checkin"
              className="text-white no-underline block px-6 py-3 rounded hover:bg-blue-500"
              activeClassName="bg-blue-500"
            >
              Check-in Sân cầu lông
            </NavLink>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;
