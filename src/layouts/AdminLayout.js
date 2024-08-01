import React from "react";
import { Navigate, Outlet, useNavigate } from "react-router-dom";
import Sidebar from "../components/Admin/Sidebar";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../redux/features/counterSlice";
import { toast } from "react-toastify";
const AdminLayout = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleLogout = () => {
    dispatch(logout());
    localStorage.removeItem("token");
    navigate("/login")
    toast.success("Logout successfully!")
  };
  return (
    <div className="flex min-h-screen bg-gray-100">
      <Sidebar />
      
      <div className="flex-1 p-6 ">
      <div className="flex justify-end items-center mb-4">
          <button 
            onClick={handleLogout} 
            className="bg-red-500 text-white py-2 px-4 rounded hover:bg-red-700 "
          >
            Logout
          </button>
        </div>
        <Outlet />
      </div>
      
    </div>
    
  );
};

export default AdminLayout;
