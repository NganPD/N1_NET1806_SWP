import React, { useContext } from "react";
import { useDispatch, useSelector } from "react-redux";
import { NavLink, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { login, selectUser } from "../../redux/features/counterSlice";
import { auth, googleProvider } from "../../config/firebaseConfig";
import { signInWithPopup } from "firebase/auth";
import api from "../../config/axios";
import Swal from "sweetalert2";

const LoginForm = () => {
  // Initialize react-hook-form
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const dispatch = useDispatch();
  const user = useSelector(selectUser);
  const navigate = useNavigate();

  const onSubmit = async (data) => {
    try {
      const res = await api.post("/login", {
        email: data.email,
        password: data.password,
      });
      Swal.fire({
        icon: "success",
        title: "Login successfully!",
        showConfirmButton: false,
        timer: 2000,
      }).then(() => {
        localStorage.setItem("token", res.data.token);
        dispatch(login(res.data));
        navigate("/");
      });
    } catch (error) {
      console.log(error);
      Swal.fire({
        icon: "error",
        title: "Failed to login",
        text: "Please try again.",
      });
    }
  };

  const handleGoogleLogin = async () => {
    try {
      const result = await signInWithPopup(auth, googleProvider);
      console.log(result);
      const token = result.user.accessToken;
      console.log(token);
      const res = await api.post("/login-google", {
        token: token,
      });
      console.log(res.data);
      Swal.fire({
        icon: "success",
        title: "Login successfully!",
        showConfirmButton: false,
        timer: 2000,
      }).then(() => {
        localStorage.setItem("token", res.data.token);
        dispatch(login(res.data));
        navigate("/users");
      });
    } catch (error) {
      console.log(error);
      Swal.fire({
        icon: "error",
        title: "Failed to login",
        text: "Please try again.",
      });
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
          Sân Cầu Lông
        </h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="mb-4">
            <input
              type="email"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              placeholder="Nhập địa chỉ Email"
              {...register("email", {
                required: "Email is required",
                pattern: {
                  value: /^\S+@\S+$/i,
                  message: "Invalid email address",
                },
              })}
            />
            {errors.email && (
              <p className="text-red-600">{errors.email.message}</p>
            )}
          </div>
          <div className="mb-4">
            <input
              type="password"
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
              placeholder="Mật khẩu"
              {...register("password", {
                required: "Password is required",
                minLength: {
                  value: 6,
                  message: "Password must be at least 6 characters",
                },
                pattern: {
                  value: /^(?=.*[A-Z])[a-zA-Z0-9]{6,}$/,
                  message:
                    "Password must contain at least 6 characters with at least one uppercase letter and no special characters.",
                },
              })}
            />
            {errors.password && (
              <p className="text-red-600">{errors.password.message}</p>
            )}
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
