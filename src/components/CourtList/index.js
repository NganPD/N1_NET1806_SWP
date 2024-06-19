import React, { useState } from "react";
import CourtCard from "../CourtCard";

const CourtList = () => {
  const courtData = [
    {
      id: 1,
      name: "sân cầu lông Cây Lộc Vừng",
      location: "1110 B2, Phạm Văn Đồng, Phường Linh Đông, Thủ Đức",
      courts: 2,
      rating: 4,
      amenities: ["Wifi", "Căng tin"],
      image: "https://babolat.com.vn/wp-content/uploads/2023/11/san-danh-cau-long-cay-loc-vung-thu-duc.jpg",
      operatingHours: { start: "07:00 AM", end: "10:00 PM" },
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
      name: "Sân cầu lông Bình Triệu",
      location: "Số 8 Đường 20, Khu phố 4, Phường Hiệp Bình Chánh, TP. Thủ Đức",
      courts: 2,
      rating: 5,
      amenities: ["Wifi", "Căng tin"],
      image: "https://babolat.com.vn/wp-content/uploads/2023/11/san-danh-cau-long-binh-trieu.jpg",
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
      name: "Sân Lan Anh – Linh Trung",
      location: "119 Đ. Số 7, Phường Linh Trung, Thủ Đức",
      courts: 2,
      rating: 5,
      amenities: ["Wifi", "Căng tin"],
      image: "https://babolat.com.vn/wp-content/uploads/2023/11/san-danh-cau-long-lan-anh.jpg",
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
      name: "Sân Hiển Hoa",
      location: "262/3 Đường Trần Não, P. Bình An, Quận 2",
      courts: 4,
      rating: 5,
      amenities: ["Wifi", "Căng tin"],
      image: "https://babolat.com.vn/wp-content/uploads/2023/11/san-cau-long-hien-hoa.jpg",
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
              <option value="Quận 1">Quận 1</option>
              <option value="Quận Bình Thạnh">Quận Bình Thạnh</option>
              <option value="Quận Tân Phú">Quận Tân Phú</option>
              <option value="Quận 2">Quận 2</option>
              <option value="Thủ Đức">Thủ Đức</option>
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
