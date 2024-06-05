import React, { useState } from "react";
import { NavLink } from "react-router-dom";

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    // Xử lý đăng nhập tại đây
    console.log("Email:", email);
    console.log("Password:", password);
  };

  const handleGoogleLogin = () => {
    // Xử lý đăng nhập bằng Google tại đây
    console.log("Đăng nhập với Google");
  };

  const handleFacebookLogin = () => {
    // Xử lý đăng nhập bằng Facebook tại đây
    console.log("Đăng nhập với Facebook");
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
          Sân Cầu Lông
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <input
              type="email"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              id="email"
              placeholder="Email hoặc Số điện thoại"
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
          <button
            type="submit"
            className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition duration-200"
          >
            Đăng nhập
          </button>
          <div className="text-center mt-4">
            <a href="/" className="text-blue-600 hover:underline no-underline">
              Quên mật khẩu?
            </a>
          </div>
          <hr className="my-4" />
          <div className="text-center mb-4">
            <button
              type="button"
              className="w-full bg-red-500 text-white py-2 rounded-lg hover:bg-red-600 transition duration-200 mb-2"
              onClick={handleGoogleLogin}
            >
              Đăng nhập với Google
            </button>
            <button
              type="button"
              className="w-full bg-blue-700  text-white py-2 rounded-lg hover:bg-blue-800 transition duration-200"
              onClick={handleFacebookLogin}
            >
              Đăng nhập với Facebook
            </button>
          </div>
          <div className="text-center">
            <NavLink
              to="/register"
              className="w-full inline-block bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition duration-200 no-underline"
            >
              Đăng ký
            </NavLink>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
