// src/index.js

import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import User from "./components/User/User";
import Admin from "./components/Admin/Admin";
import HomePage from "./components/Home/HomePage";
import LoginForm from "./components/Login/LoginForm";
import RegisterForm from "./components/Login/RegisterForm";
import CourtList from "./components/CourtList";
import PaymentPage from "./components/Payment";
import ContactPage from "./components/Contact";
import AdminLayout from "./layouts/AdminLayout";
import AccountManagement from "./components/Admin/AccountManagement";
import CourtManagement from "./components/Admin/CourtManagement";
import NewCourtRegistration from "./components/Admin/NewCourtRegistration";
import RegisterCourtInfo from "./components/Court/RegisterCourtInfo";
import RegisterBookingType from "./components/Court/RegisterBookingType";
import ManageSchedules from "./components/Court/ManageSchedules";
import CourtLayout from "./layouts/CourtLayout";
import ManageCourts from "./components/Court/ManageCourts";
import CourtDetails from "./components/CourtDetail";
import { persistor, store } from "./redux/store";
import { Provider } from "react-redux";
import { PersistGate } from "redux-persist/integration/react";
import CourtStaffCheckin from "./components/CourtStaff/ManageCourtCheckin";
import CourtStaffLayout from "./layouts/CourtStaffLayout";
import { GlobalStateProvider } from "./components/context/GlobalStateContext";
import UserProfile from "./components/UserProfile";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from "react-toastify";
import Overview from "./components/overview";
const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <Provider store={store}>

    <PersistGate loading={null} persistor={persistor}>
      <ToastContainer />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<App />}>
            <Route index element={<HomePage />} />
            <Route path="users" element={<User />} />
            <Route path="admins" element={<Admin />} />
            <Route path="login" element={<LoginForm />} />
            <Route path="register" element={<RegisterForm />} />
            <Route path="courts" element={<CourtList />} />
            <Route path="/payment" element={<PaymentPage />} />
            <Route path="/contact" element={<ContactPage />} />
            <Route path="/court-details/:id" element={<CourtDetails />} />
            <Route path="/profile" element={<UserProfile />} />
            {/* Thêm route cho UserProfile */}
          </Route>
          <Route path="/admin" element={<AdminLayout />}>
            <Route path="accounts" element={<AccountManagement />} />
            <Route path="courts" element={<CourtManagement />} />
            <Route path="new-court" element={<NewCourtRegistration />} />
            <Route path="overview" element={<Overview />} />
          </Route>
          <Route path="/court-manager" element={<CourtLayout />}>
            <Route path="register-court-info" element={<RegisterCourtInfo />} />
            <Route
              path="register-booking-type"
              element={<RegisterBookingType />}
            />
            <Route path="manage-courts" element={<ManageCourts />} />
            <Route path="manage-schedules" element={<ManageSchedules />} />
          </Route>

          {/* Staff */}
          <Route path="/court-staff" element={<CourtStaffLayout />}>
            <Route path="court-checkin" element={<CourtStaffCheckin />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </PersistGate>
  </Provider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
