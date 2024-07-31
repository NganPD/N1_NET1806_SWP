import React, { useState, useEffect } from "react";
import api from "../../../config/axios";

const CourtManagement = () => {
  const [courts, setCourts] = useState([]);
  const token = localStorage.getItem("token");

  const fetchVenues = async () => {
    try {
      const response = await api.get("/venues");
      console.log(response.data);
      setCourts(response.data);
    } catch (error) {
      console.error("Error fetching courts data", error);
    }
  };

  useEffect(() => {
    fetchVenues();
  }, []);

  const handleDelete = async (id) => {
    try {
      await api.delete(`/venues/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCourts(courts.filter((court) => court.venueId !== id));
    } catch (error) {
      console.error("Failed to delete court:", error);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h2 className="text-2xl font-bold mb-4 text-center">Quản lý thông tin Sân cầu lông</h2>
      <table className="min-w-full border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-100">
            <th className="py-2 px-4 border border-gray-300">ID</th>
            <th className="py-2 px-4 border border-gray-300">Tên địa điểm</th>
            <th className="py-2 px-4 border border-gray-300">Hình Ảnh</th>
            <th className="py-2 px-4 border border-gray-300">Trạng thái địa điểm</th>
            <th className="py-2 px-4 border border-gray-300">Liên hệ chúng tôi</th>
            <th className="py-2 px-4 border border-gray-300">Giờ mở cửa</th>
            <th className="py-2 px-4 border border-gray-300">Giờ đóng cửa</th>
            <th className="py-2 px-4 border border-gray-300">Miêu tả</th>
            <th className="py-2 px-4 border border-gray-300">Dịch vụ</th>
            <th className="py-2 px-4 border border-gray-300">Hành động</th>
          </tr>
        </thead>
        <tbody>
          {courts.map((court) => (
            <tr key={court.venueId} className="hover:bg-gray-100">
              <td className="py-2 px-4 border border-gray-300 text-center">
                {court.venueId}
              </td>
              <td className="py-2 px-4 border border-gray-300 text-center">
                {court.name}
              </td>
              <td className="py-2 px-4 border border-gray-300">
                <img src={court.imageUrl} alt={court.name} className="h-16 w-16 object-cover mx-auto"/>
              </td>
              <td className="py-2 px-4 border border-gray-300 text-center">
                {court.venueStatus}
              </td>
              <td className="py-2 px-4 border border-gray-300">
                {court.contactInfor}
              </td>
              <td className="py-2 px-4 border border-gray-300 text-center">
                {court.openingHour}
              </td>
              <td className="py-2 px-4 border border-gray-300 text-center">
                {court.closingHour}
              </td>
              <td className="py-2 px-4 border border-gray-300">
                {court.description}
              </td>
              <td className="py-2 px-4 border border-gray-300">
                {court.services}
              </td>
              <td className="py-2 px-4 border border-gray-300 text-center">
                <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700">
                  Chỉnh sửa
                </button>
                <button
                  onClick={() => handleDelete(court.venueId)}
                  className="bg-red-500 text-white px-4 py-2 rounded ml-2 hover:bg-red-700"
                >
                  Xóa
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CourtManagement;
