import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { NavLink, useNavigate } from "react-router-dom";
import {  selectUser } from "../../redux/features/counterSlice";
import api from "../../config/axios";
import {  toast } from 'react-toastify';


const RegisterForm = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  // const [confirmPassword, setConfirmPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState(""); // Thêm state cho số điện thoại
 
    //lưu redux bằng useDispatch
    const dispatch = useDispatch()
    // lấy user trong redux
    const useSelect = useSelector(selectUser)
    // sau khi đăng nhập xong muốn người dùng ở trang nào thì dùng navigate
    const navigate = useNavigate()
   
   

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Xử lý đăng ký tại đây
    console.log("Username:", username);
    console.log("Email:", email);
    console.log("Password:", password);
    // console.log("Confirm Password:", value.confirmPassword);
    console.log("Phone Number:", phoneNumber); // In số điện thoại ra console
    
    try {
      const res = await api.post("/register", {
        username:username,
        email: email,
        password: password,
        // confirmPassword:confirmPassword,
        phoneNumber:phoneNumber
      })
      
     toast.success('Sign up successfully')
      
      navigate("/")

    } catch (error) {
      console.log(error)
    }
  };



  return (
    <div
      className="flex items-center justify-center min-h-screen bg-gray-100 bg-cover bg-center"
      style={{
        backgroundImage:
          "url('https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg')",
      }}
    >
      <div className="bg-white bg-opacity-90 p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold text-center text-blue-600 mb-4">
          Đăng Ký
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <input
              type="text"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              id="username"
              placeholder="Tên đăng nhập"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="mb-4">
            <input
              type="email"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              id="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="mb-4">
            <input
              type="password"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              id="password"
              placeholder="Mật khẩu"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {/* <div className="mb-4">
            <input
              type="password"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              id="confirmPassword"
              placeholder="Xác nhận mật khẩu"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div> */}
          <div className="mb-4">
            <input
              type="tel"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              id="phoneNumber"
              placeholder="Số điện thoại (không bắt buộc)"
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
            />
          </div>
          <button 
            type="submit"
            className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition duration-200"
          >
            Đăng ký
          </button>
          <div className="text-center mt-4">
            <NavLink
              to="/login"
              className="text-blue-600 no-underline hover:underline"
            >
              Đã có tài khoản? Đăng nhập
            </NavLink>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegisterForm;