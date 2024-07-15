import React, { useState } from "react";
import axios from "axios";
import api from "../../../config/axios";

const NewCourtRegistration = () => {
  const [venueName, setVenueName] = useState("");
  const [address, setAddress] = useState("");
  const [imageUrl, setImageUrl] = useState("");
  const [venueStatus, setVenueStatus] = useState("OPEN");
  const [contactInfor, setContactInfor] = useState("");
  const [openingHours, setOpeningHours] = useState("");
  const [closingHours, setClosingHours] = useState("");
  const [description, setDescription] = useState("");
  const [services, setServices] = useState("");
  const [managerId, setManagerId] = useState(0);

  const token = localStorage.getItem("token");
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(null);

  const handleAddCourt = async (e) => {
    e.preventDefault();

    try {
      const newVenue = {
        venueName,
        address,
        imageUrl,
        venueStatus,
        contactInfor,
        openingHours,
        closingHours,
        description,
        services,
        managerId,
      };

  const response = await api.post("/venues",newVenue)
      setSuccess("Venues created successfully!");
      setError(null);

      // Reset form
      setVenueName("");
      setAddress("");
      setImageUrl("");
      setVenueStatus("OPEN");
      setContactInfor("");
      setOpeningHours("");
      setClosingHours("");
      setDescription("");
      setServices("");
      setManagerId(0);
    } catch (error) {
      setError("Failed to create court. Please try again.");
      setSuccess(null);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Đăng ký thông tin sân mới</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      {success && <div className="text-green-500 mb-4">{success}</div>}
      <form onSubmit={handleAddCourt}>
        <div className="mb-4">
          <label className="block mb-2">Tên địa điểm</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venueName}
            onChange={(e) => setVenueName(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Địa chỉ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">URL hình ảnh</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={imageUrl}
            onChange={(e) => setImageUrl(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Trạng thái</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venueStatus}
            onChange={(e) => setVenueStatus(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Thông tin liên hệ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={contactInfor}
            onChange={(e) => setContactInfor(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Giờ mở cửa</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={openingHours}
            onChange={(e) => setOpeningHours(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Giờ đóng cửa</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={closingHours}
            onChange={(e) => setClosingHours(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Mô tả</label>
          <textarea
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          ></textarea>
        </div>
        <div className="mb-4">
          <label className="block mb-2">Dịch vụ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={services}
            onChange={(e) => setServices(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Mã quản lý</label>
          <input
            type="number"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={managerId}
            onChange={(e) => setManagerId(parseInt(e.target.value))}
            required
          />
        </div>
        <button
          // type="submit"
        type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded-lg"
        >
          Đăng ký
        </button>
      </form>
    </div>
  );
};

export default NewCourtRegistration;
