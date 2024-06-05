// src/components/CourtManager/ManageCourts.js

import React from "react";

const ManageCourts = () => {
  const courtData = [
    {
      id: "1",
      name: "Court 1",
      location: "Location 1",
      price: "100",
      status: "Active",
    },
    {
      id: "2",
      name: "Court 2",
      location: "Location 2",
      price: "200",
      status: "Inactive",
    },
    {
      id: "3",
      name: "Court 3",
      location: "Location 3",
      price: "150",
      status: "Active",
    },
  ];

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Quản lý thông tin sân</h1>
      <table className="min-w-full bg-white border border-gray-300">
        <thead className="bg-gray-100">
          <tr>
            <th className="py-2 px-4 border-b">ID</th>
            <th className="py-2 px-4 border-b">Name</th>
            <th className="py-2 px-4 border-b">Location</th>
            <th className="py-2 px-4 border-b">Price</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Actions</th>
          </tr>
        </thead>
        <tbody>
          {courtData.map((court) => (
            <tr key={court.id} className="hover:bg-gray-100">
              <td className="py-2 px-4 border-b">{court.id}</td>
              <td className="py-2 px-4 border-b">{court.name}</td>
              <td className="py-2 px-4 border-b">{court.location}</td>
              <td className="py-2 px-4 border-b">{court.price}</td>
              <td className="py-2 px-4 border-b">{court.status}</td>
              <td className="py-2 px-4 border-b">
                <button className="px-4 py-2 bg-yellow-500 text-white rounded-lg mr-2">
                  Edit
                </button>
                <button className="px-4 py-2 bg-red-500 text-white rounded-lg">
                  Delete
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
