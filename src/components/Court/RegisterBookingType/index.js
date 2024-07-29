// src/components/CourtManager/RegisterBookingType.js

import React, { useState } from "react";
import api from "../../../config/axios";
import { toast } from "react-toastify";

const RegisterBookingType = () => {
  const [formData, setFormData] = useState({

    startTime: "",
    endTime: "",
    venueId: 0

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

    const response = await api.post('/timeslots', formData)
    console.log(response.data)
    toast.success("Đặt sân thành công !!!");
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">
        Đăng ký slot
      </h1>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700">Start time</label>
          <input
            type="text"
            name="startTime"
            value={formData.startTime}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">End time</label>
          <input
            type="text"
            name="endTime"
            value={formData.endTime}
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
