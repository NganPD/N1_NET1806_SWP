import React, { useState } from "react";
import CourtCard from "../CourtCard";

const CourtList = () => {
  const courtData = [
    {
      id: 1,
      name: "Sân bóng đá đại học Thủy Lợi",
      location: "Quận Đống Đa - Hà Nội",
      courts: 4,
      rating: 4,
      amenities: ["Wifi", "Căng tin"],
      image: "https://via.placeholder.com/400x300",
      operatingHours: { start: "10:00 AM", end: "10:00 PM" },
      availableTimes: [
        { time: "14:00 - 15:30", status: true },
        { time: "15:30 - 17:00", status: false },
        { time: "17:00 - 18:30", status: true },
        { time: "18:30 - 20:00", status: true },
        { time: "20:00 - 21:30", status: true },
      ],
    },
    {
      id: 2,
      name: "Sân bóng ĐH Giao Thông Vận Tải",
      location: "Quận Đống Đa - Hà Nội",
      courts: 5,
      rating: 5,
      amenities: ["Wifi", "Căng tin"],
      image: "https://via.placeholder.com/400x300",
      operatingHours: { start: "10:00 AM", end: "10:00 PM" },
      availableTimes: [
        { time: "14:00 - 15:30", status: true },
        { time: "15:30 - 17:00", status: true },
        { time: "17:00 - 18:30", status: false },
        { time: "18:30 - 20:00", status: true },
        { time: "20:00 - 21:30", status: true },
      ],
    },
    {
      id: 3,
      name: "Sân bóng Akka Vĩnh Hoàng",
      location: "Hoàng Mai - Hà Nội",
      courts: 6,
      rating: 5,
      amenities: ["Wifi", "Căng tin"],
      image: "https://via.placeholder.com/400x300",
      operatingHours: { start: "10:00 AM", end: "10:00 PM" },
      availableTimes: [
        { time: "14:00 - 15:30", status: true },
        { time: "15:30 - 17:00", status: true },
        { time: "17:00 - 18:30", status: true },
        { time: "18:30 - 20:00", status: false },
        { time: "20:00 - 21:30", status: true },
      ],
    },
    {
      id: 4,
      name: "Nhà thi đấu cầu lông Triều Khúc",
      location: "Thanh Trì - Hà Nội",
      courts: 6,
      rating: 5,
      amenities: ["Wifi", "Căng tin"],
      image: "https://via.placeholder.com/400x300",
      operatingHours: { start: "10:00 AM", end: "10:00 PM" },
      availableTimes: [
        { time: "14:00 - 15:30", status: true },
        { time: "15:30 - 17:00", status: true },
        { time: "17:00 - 18:30", status: true },
        { time: "18:30 - 20:00", status: true },
        { time: "20:00 - 21:30", status: false },
      ],
    },
    // Thêm dữ liệu khác ở đây
  ];

  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedLocation, setSelectedLocation] = useState("");
  const [selectedOperatingHour, setSelectedOperatingHour] = useState("");

  const courtsPerPage = 6;

  const handleTimeChange = (event) => {
    setSelectedTime(event.target.value);
  };

  const handleLocationChange = (event) => {
    setSelectedLocation(event.target.value);
  };

  const handleOperatingHourChange = (event) => {
    setSelectedOperatingHour(event.target.value);
  };

  const filteredCourts = courtData
    .filter(
      (court) =>
        court.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        court.location.toLowerCase().includes(searchTerm.toLowerCase())
    )
    .filter((court) => {
      if (selectedOperatingHour === "") return true;
      return court.operatingHours.start === selectedOperatingHour;
    });

  const indexOfLastCourt = currentPage * courtsPerPage;
  const indexOfFirstCourt = indexOfLastCourt - courtsPerPage;
  const currentCourts = filteredCourts.slice(
    indexOfFirstCourt,
    indexOfLastCourt
  );

  const totalPages = Math.ceil(filteredCourts.length / courtsPerPage);

  return (
    <div className="container mx-auto mb-6">
      <h1 className="text-4xl font-bold text-center my-8">Danh sách sân</h1>
      <div className="bg-gray-100 p-6 rounded-lg shadow-md mb-8 flex items-center justify-between">
        <input
          type="text"
          placeholder="Tìm kiếm sân..."
          className="px-4 py-2 border rounded-lg w-full md:w-1/3 mr-4"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <div className="flex items-center space-x-4">
          <div>
            <label className="block mb-2">Khung giờ</label>
            <select
              className="px-4 py-2 border rounded"
              value={selectedTime}
              onChange={handleTimeChange}
            >
              <option value="">Tất cả</option>
              <option value="10:00 AM">10:00 AM</option>
              <option value="11:00 AM">11:00 AM</option>
              <option value="12:00 PM">12:00 PM</option>
              <option value="01:00 PM">01:00 PM</option>
              <option value="02:00 PM">02:00 PM</option>
              <option value="03:00 PM">03:00 PM</option>
              <option value="04:00 PM">04:00 PM</option>
              <option value="05:00 PM">05:00 PM</option>
              <option value="06:00 PM">06:00 PM</option>
              <option value="07:00 PM">07:00 PM</option>
              <option value="08:00 PM">08:00 PM</option>
              <option value="09:00 PM">09:00 PM</option>
              <option value="10:00 PM">10:00 PM</option>
            </select>
          </div>
          <div>
            <label className="block mb-2">Khu vực</label>
            <select
              className="px-4 py-2 border rounded"
              value={selectedLocation}
              onChange={handleLocationChange}
            >
              <option value="">Tất cả</option>
              <option value="Hồ Chí Minh">Hồ Chí Minh</option>
              <option value="Hà Nội">Hà Nội</option>
              <option value="Đà Nẵng">Đà Nẵng</option>
              <option value="Cần Thơ">Cần Thơ</option>
              <option value="Nha Trang">Nha Trang</option>
            </select>
          </div>
          <div>
            <label className="block mb-2">Giờ mở cửa</label>
            <select
              className="px-4 py-2 border rounded"
              value={selectedOperatingHour}
              onChange={handleOperatingHourChange}
            >
              <option value="">Tất cả</option>
              <option value="10:00 AM">10:00 AM</option>
              <option value="11:00 AM">11:00 AM</option>
            </select>
          </div>
        </div>
      </div>
      <div className="flex flex-wrap justify-center bg-white p-4 rounded-lg shadow-md">
        {currentCourts.map((court) => (
          <CourtCard key={court.id} court={court} />
        ))}
      </div>
      <div className="flex justify-center mt-4">
        <button
          className="px-4 py-2 mx-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          disabled={currentPage === 1}
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
        >
          Previous
        </button>
        {[...Array(totalPages)].map((_, index) => (
          <button
            key={index}
            className={`px-4 py-2 mx-2 ${
              currentPage === index + 1
                ? "bg-blue-700 text-white"
                : "bg-blue-500 text-white"
            } rounded hover:bg-blue-600`}
            onClick={() => setCurrentPage(index + 1)}
          >
            {index + 1}
          </button>
        ))}
        <button
          className="px-4 py-2 mx-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          disabled={currentPage === totalPages}
          onClick={() =>
            setCurrentPage((prev) => Math.min(prev + 1, totalPages))
          }
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default CourtList;
