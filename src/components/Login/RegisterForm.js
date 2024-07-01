import React, { useContext, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { NavLink, useNavigate } from "react-router-dom";
import { selectUser } from "../../redux/features/counterSlice";
import api from "../../config/axios";
import Swal from "sweetalert2";

const RegisterForm = () => {
  const [fullName, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [passwordError, setPasswordError] = useState(""); // State for password validation error

  const dispatch = useDispatch();
  const user = useSelector(selectUser);
  const navigate = useNavigate();

  // const handleSubmit = async (e) => {
  //   e.preventDefault();

  //   try {
  //     const res = await api.post("/register", {
  //       fullName,
  //       email,
  //       password,
  //       phone: phoneNumber,
  //     });
  //     toast.success("Sign up successfully");
  //     setTimeout(() => {
  //       navigate("/");
  //     }, 2000); // Adjust the timeout duration (2000ms = 2 seconds) as needed
  //   } catch (error) {
  //     toast.error("Failed to register. Please try again.");
  //     console.log(error);
  //   }
  // };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post("/register", {
        fullName,
        email,
        password,
        phone: phoneNumber,
      });
      Swal.fire({
        icon: "success",
        title: "Sign up successfully",
        showConfirmButton: false,
        timer: 2000,
      }).then(() => {
        navigate("/");
      });
    } catch (error) {
      Swal.fire({
        icon: "error",
        title: "Failed to register",
        text: "Please try again.",
      });
      console.log(error);
    }
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    // Password validation logic
    if (e.target.value.length < 6) {
      setPasswordError("Password must be at least 6 characters");
    } else if (!/[A-Z]/.test(e.target.value)) {
      setPasswordError("Password must contain at least one uppercase letter");
    } else {
      setPasswordError("");
    }
  };

  return (
    <div
      style={{
        backgroundImage:
          "url('https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg')",
      }}
      className="flex items-center justify-center min-h-screen bg-gray-100 bg-cover bg-center"
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
              id="fullName"
              placeholder="Tên đăng nhập"
              value={fullName}
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
              onChange={handlePasswordChange}
              required
            />
            {passwordError && (
              <p className="text-red-500 text-xs mt-1">{passwordError}</p>
            )}
          </div>
          <div className="mb-4">
            <input
              type="tel"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              id="phone"
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
