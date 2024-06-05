// src/components/CourtManager/RegisterCourtInfo.js

import React from "react";

const RegisterCourtInfo = () => {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">
        Đăng ký thông tin sân; thông tin tài khoản thanh toán
      </h1>
      <form>
        <div className="mb-4">
          <label className="block text-gray-700">Tên sân</label>
          <input type="text" className="w-full px-4 py-2 border rounded-lg" />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Địa điểm</label>
          <input type="text" className="w-full px-4 py-2 border rounded-lg" />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Giá</label>
          <input type="number" className="w-full px-4 py-2 border rounded-lg" />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Tình trạng</label>
          <select className="w-full px-4 py-2 border rounded-lg">
            <option value="available">Active</option>
            <option value="booked">Inactive</option>
          </select>
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

export default RegisterCourtInfo;
