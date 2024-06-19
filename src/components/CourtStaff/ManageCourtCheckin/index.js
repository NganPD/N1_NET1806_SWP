import React, { useState, useEffect } from "react";

const CourtStaffCheckin = () => {
  const [courts, setCourts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    // Fake data for courts
    const fakeCourts = [
      {
        id: 1,
        name: "Sân Cầu Lông A",
        location: "Quận 1, TP.HCM",
        courts: 5,
        price: "200,000 VND/giờ",
        image: "https://via.placeholder.com/150",
        availableTimes: ["08:00", "10:00", "12:00", "14:00", "16:00"],
        status: true,
      },
      {
        id: 2,
        name: "Sân Cầu Lông B",
        location: "Quận 3, TP.HCM",
        courts: 3,
        price: "180,000 VND/giờ",
        image: "https://via.placeholder.com/150",
        availableTimes: ["09:00", "11:00", "13:00", "15:00", "17:00"],
        status: false,
      },
      {
        id: 3,
        name: "Sân Cầu Lông C",
        location: "Quận 5, TP.HCM",
        courts: 4,
        price: "150,000 VND/giờ",
        image: "https://via.placeholder.com/150",
        availableTimes: ["08:30", "10:30", "12:30", "14:30", "16:30"],
        status: true,
      },
      {
        id: 4,
        name: "Sân Cầu Lông D",
        location: "Quận 7, TP.HCM",
        courts: 6,
        price: "220,000 VND/giờ",
        image: "https://via.placeholder.com/150",
        availableTimes: ["09:30", "11:30", "13:30", "15:30", "17:30"],
        status: false,
      },
      {
        id: 5,
        name: "Sân Cầu Lông E",
        location: "Quận 9, TP.HCM",
        courts: 2,
        price: "170,000 VND/giờ",
        image: "https://via.placeholder.com/150",
        availableTimes: ["08:00", "10:00", "12:00", "14:00", "16:00"],
        status: true,
      },
    ];
    setCourts(fakeCourts);
  }, []);

  const filteredCourts = courts.filter((court) =>
    court.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="bg-white p-4">
      <h2 className="text-2xl font-bold mb-4">Quản lý Check-in Sân cầu lông</h2>
      <div className="mb-4">
        <input
          type="text"
          placeholder="Tìm kiếm sân..."
          className="px-4 py-2 border rounded-lg w-full"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                ID
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tên sân
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Khu vực
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Số sân
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Giá
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Trạng thái
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Hành động
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredCourts.map((court) => (
              <tr key={court.id}>
                <td className="px-6 py-4 whitespace-nowrap">{court.id}</td>
                <td className="px-6 py-4 whitespace-nowrap">{court.name}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  {court.location}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">{court.courts}</td>
                <td className="px-6 py-4 whitespace-nowrap">{court.price}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      court.status
                        ? "bg-green-100 text-green-800"
                        : "bg-red-100 text-red-800"
                    }`}
                  >
                    {court.status ? "Trống" : "Đã đặt"}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <button
                    className={`px-4 py-2 rounded ${
                      court.status
                        ? "bg-green-500 text-white"
                        : "bg-gray-300 text-gray-700 cursor-not-allowed"
                    }`}
                    onClick={() =>
                      alert(`Check-in thành công cho sân có ID: ${court.id}`)
                    }
                    disabled={!court.status}
                  >
                    Check-in
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default CourtStaffCheckin;
