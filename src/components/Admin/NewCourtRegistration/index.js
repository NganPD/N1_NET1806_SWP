import React, { useState } from "react";
import axios from "axios";

const NewCourtRegistration = () => {
  const [courtName, setCourtName] = useState("");
  const [status, setStatus] = useState("AVAILABLE");
  const [amenities, setAmenities] = useState("");
  const [description, setDescription] = useState("");
  const [services, setServices] = useState("");
  const [venueId, setVenueId] = useState(0);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const token = localStorage.getItem("token");

  const handleAddCourt = async (e) => {
    e.preventDefault();

    try {
      const newCourt = {
        courtName,
        status,
        amenities,
        description,
        services,
        venueId: parseInt(venueId, 10),
      };

      const response = await axios.post(
        "http://104.248.224.6:8082/api/courts",
        newCourt,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setSuccess("Court created successfully!");
      setError(null);

      // Reset form
      setCourtName("");
      setStatus("AVAILABLE");
      setAmenities("");
      setDescription("");
      setServices("");
      setVenueId(0);
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
          <label className="block mb-2">Tên sân</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={courtName}
            onChange={(e) => setCourtName(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Tiện nghi</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={amenities}
            onChange={(e) => setAmenities(e.target.value)}
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
          <label className="block mb-2">Mã địa điểm</label>
          <input
            type="number"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venueId}
            onChange={(e) => setVenueId(e.target.value)}
            required
          />
        </div>
        <button
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
