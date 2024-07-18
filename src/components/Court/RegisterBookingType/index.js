// src/components/CourtManager/RegisterBookingType.js

import React, { useState } from "react";

const RegisterBookingType = () => {
  const [formData, setFormData] = useState({
    timeSlotID: 0,
    bookingType: "FLEXIBLE",
    price: 0,
    discount: 0,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = localStorage.getItem("token");

    const response = await fetch(
      "http://104.248.224.6:8082/api/time-slot-price",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      }
    );

    if (response.ok) {
      // Handle success
      const data = await response.json();
      console.log("Success:", data);
      alert("Registration successful!");
    } else {
      // Handle error
      const error = await response.json();
      console.error("Error:", error);
      alert("Registration failed. Please try again.");
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">
        Đăng ký thông tin loại hình đặt lịch, time slot của sân
      </h1>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700">Loại hình đặt lịch</label>
          <select
            name="bookingType"
            value={formData.bookingType}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
          >
            <option value="FLEXIBLE">Flexible</option>
            <option value="FIXED">Fixed</option>
          </select>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Time Slot ID</label>
          <input
            type="number"
            name="timeSlotID"
            value={formData.timeSlotID}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Giá</label>
          <input
            type="number"
            name="price"
            value={formData.price}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Giảm giá</label>
          <input
            type="number"
            name="discount"
            value={formData.discount}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
          />
        </div>
        <button
          type="submit"
          className="px-4 py-2 bg-blue-500 text-white rounded-lg"
        >
          Đăng ký
        </button>
      </form>
    </div>
  );
};

export default RegisterBookingType;
