import React, { useState, useEffect } from "react";
import api from "../../../config/axios";

const ManageCourts = () => {
  const [courtData, setCourtData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await api.get("/courts", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setCourtData(response.data);
      } catch (error) {
        console.error("Error fetching court data:", error);
      }
    };

    fetchData();
  }, []);

  const handleDelete = async (courtId) => {
    const token = localStorage.getItem("token");
    const confirmation = window.confirm(
      "Are you sure you want to delete this court?"
    );
    if (!confirmation) return;

    try {
      const response = await api.delete(`/courts/${courtId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.status === 204) {
        setCourtData((prev) =>
          prev.map((court) =>
            court.id === courtId ? { ...court, status: "INACTIVE" } : court
          )
        );
      } else {
        console.error("Error:", response.data);
        alert("Failed to delete the court. Please try again.");
      }
    } catch (error) {
      console.error("Error deleting court:", error);
      alert("An error occurred. Please try again.");
    }
  };

  const handleEdit = async (courtId) => {
    const token = localStorage.getItem("token");
    try {
      const response = await api.patch(`/courts/${courtId}`, 
        { status: "AVAILABLE" }, 
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        setCourtData((prev) =>
          prev.map((court) =>
            court.id === courtId ? { ...court, status: "AVAILABLE" } : court
          )
        );
      } else {
        console.error("Error:", response.data);
        alert("Failed to update the court status. Please try again.");
      }
    } catch (error) {
      console.error("Error updating court status:", error);
      alert("An error occurred. Please try again.");
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Quản lý thông tin sân</h1>
      <table className="min-w-full bg-white border border-gray-300">
        <thead className="bg-gray-100">
          <tr>
            <th className="py-2 px-4 border-b">ID</th>
            <th className="py-2 px-4 border-b">Tên sân</th>
            <th className="py-2 px-4 border-b">Trạng thái</th>
            <th className="py-2 px-4 border-b">Mô tả</th>
            <th className="py-2 px-4 border-b">Hành động</th>
          </tr>
        </thead>
        <tbody>
          {courtData?.map((court) => (
            <tr key={court?.id} className="hover:bg-gray-100">
              <td className="py-2 px-4 border-b">{court?.id}</td>
              <td className="py-2 px-4 border-b">{court?.courtName}</td>
              <td className="py-2 px-4 border-b">{court?.status}</td>
              <td className="py-2 px-4 border-b">{court?.description}</td>
              <td className="py-2 px-4 border-b">
                <button
                  className="px-4 py-2 bg-yellow-500 text-white rounded-lg mr-2"
                  onClick={() => handleEdit(court?.id)}
                >
                  Chỉnh sửa
                </button>
                <button
                  className="px-4 py-2 bg-red-500 text-white rounded-lg"
                  onClick={() => handleDelete(court?.id)}
                  disabled={court?.status === "INACTIVE"}
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

export default ManageCourts;
