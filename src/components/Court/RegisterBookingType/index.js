// src/components/CourtManager/RegisterBookingType.js

import React from "react";

const RegisterBookingType = () => {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">
        Đăng ký thông tin loại hình đặt lịch, time slot của sân
      </h1>
      <form>
        <div className="mb-4">
          <label className="block text-gray-700">Loại hình đặt lịch</label>
          <input type="text" className="w-full px-4 py-2 border rounded-lg" />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Time Frame</label>
          <input type="text" className="w-full px-4 py-2 border rounded-lg" />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Giá</label>
          <input type="number" className="w-full px-4 py-2 border rounded-lg" />
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
