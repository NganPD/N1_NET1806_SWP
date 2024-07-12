import React, { useState, useEffect } from "react";
import axios from "axios";
import api from "../../../config/axios";

const CourtManagement = () => {
  const [courts, setCourts] = useState([]);

  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchVenues = async () => { 
      try {
        const response = api.get("/venues");
        console.log(response.data)
        const availableCourts = response.data.filter(
          (court) => court.status === "AVAILABLE"
        );
        console.log("🚀 ~ fetchVenues ~ availableCourts:", availableCourts);
        setCourts(availableCourts);
      } catch (error) {
        console.error("Error fetching courts data", error);
      }
    };

    fetchVenues();
  }, []);

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://104.248.224.6:8082/api/courts/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCourts(
        courts.filter(
          (court) => court.id !== id && court.status === "AVAILABLE"
        )
      );
    } catch (error) {
      console.error("Failed to delete court:", error);
    }
  };

  return (
    <div className="bg-white p-4">
      <h2 className="text-2xl font-bold mb-4">
        Quản lý thông tin Sân cầu lông
      </h2>
      <table className="ml-2 min-w-full border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-100">
            <th className="py-2 px-4 border border-gray-300">ID</th>
            <th className="py-2 px-4 border border-gray-300">Tên sân</th>
            <th className="py-2 px-4 border border-gray-300">Khu vực</th>
            <th className="py-2 px-4 border border-gray-300">Trạng thái</th>
            <th className="py-2 px-4 border border-gray-300">Tên địa điểm</th>
            <th className="py-2 px-4 border border-gray-300">Hành động</th>
          </tr>
        </thead>
        <tbody>
          {courts.map((court) => (
            <tr key={court.id}>
              <td className="py-2 px-4 border border-gray-300 text-center">
                {court.id}
              </td>
              <td className="py-2 px-4 border border-gray-300">
                {court.courtName}
              </td>
              <td className="py-2 px-4 border border-gray-300">
                {court.venue.address}
              </td>
              <td className="py-2 px-4 border border-gray-300 text-center">
                {court.status}
              </td>
              <td className="py-2 px-4 border border-gray-300">
                {court.venue.name}
              </td>
              <td className="py-2 px-4 border border-gray-300 text-center">
                <button className="bg-blue-500 text-white px-4 py-2 rounded">
                  Chỉnh sửa
                </button>
                <button
                  onClick={() => handleDelete(court.id)}
                  className="bg-red-500 text-white px-4 py-2 rounded ml-2"
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
