import React, { useState } from "react";
import api from "../../../config/axios";
import { toast } from "react-toastify";

const RegisterCourtInfo = () => {
  const [formData, setFormData] = useState({
    courtName: "",
    status: "AVAILABLE",
    amenities: "",
    description: "",
    services: "",
    venueId: 0,
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

    

    try {
      const response = await api.post("/courts", formData);

      if (response.status === 200) {
        console.log("Success:", response.data);
        toast.success("Mua giờ thành công !!!");
      } else {
        console.error("Error:", response.data);
        toast.error("Registration failed. Please try again.");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("Registration failed. Please try again.");
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Đăng ký thông tin sân mới</h1>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700">Tên sân</label>
          <input
            type="text"
            name="courtName"
            value={formData.courtName}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Tình trạng sân</label>
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          >
            <option value="AVAILABLE">Available</option>
            <option value="UNAVAILABLE">Unavailable</option>
          </select>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Tiện nghi</label>
          <input
            type="text"
            name="amenities"
            value={formData.amenities}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Mô tả</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          ></textarea>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Dịch vụ</label>
          <input
            type="text"
            name="services"
            value={formData.services}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700">Mã địa điểm</label>
          <input
            type="number"
            name="venueId"
            value={formData.venueId}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
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

export default RegisterCourtInfo;
