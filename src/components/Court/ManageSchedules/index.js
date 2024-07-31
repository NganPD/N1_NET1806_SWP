// src/components/CourtManager/ManageSchedules.js

import React from "react";

const ManageSchedules = () => {
  const schedules = [
    { id: "1", court: "Court 1", time: "10:00 - 11:00", status: "Inactive" },
    { id: "2", court: "Court 2", time: "12:00 - 13:00", status: "Active" },
    { id: "3", court: "Court 3", time: "14:00 - 15:00", status: "Inactive" },
  ];

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Quản lý lịch thi đấu</h1>
      <table className="min-w-full bg-white border border-gray-300">
        <thead className="bg-gray-100">
          <tr>
            <th className="py-2 px-4 border-b">ID</th>
            <th className="py-2 px-4 border-b">Court</th>
            <th className="py-2 px-4 border-b">Time</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Actions</th>
          </tr>
        </thead>
        <tbody>
          {schedules.map((schedule) => (
            <tr key={schedule.id} className="hover:bg-gray-100">
              <td className="py-2 px-4 border-b">{schedule.id}</td>
              <td className="py-2 px-4 border-b">{schedule.court}</td>
              <td className="py-2 px-4 border-b">{schedule.time}</td>
              <td className="py-2 px-4 border-b">{schedule.status}</td>
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

export default ManageSchedules;
