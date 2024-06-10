import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const CourtCard = ({ court }) => {
  const navigate = useNavigate();

  const handleViewDetails = () => {
    navigate("/court-details", { state: { court } });
  };

  return (
    <div className="max-w-sm rounded overflow-hidden shadow-lg m-4">
      <img className="w-[25rem] h-[20rem]" src={court.image} alt="Court" />
      <div className="px-6 py-4">
        <div className="font-bold text-xl mb-2">{court.name}</div>
        <p className="text-gray-700 text-base">Khu vực: {court.location}</p>
        <p className="text-gray-700 text-base">Số sân: {court.courts}</p>
        <div className="text-yellow-500">
          {"★".repeat(court.rating)}
          {"☆".repeat(5 - court.rating)}
        </div>
        <p className="text-gray-700 text-base">Giá: {court.price} VNĐ</p>
        <div className="mt-2">
          <span className="text-lg font-semibold">Giờ hoạt động:</span>
          <p className="text-gray-700 text-base">
            {court.operatingHours.start} - {court.operatingHours.end}
          </p>
        </div>
        <div className="flex space-x-2 mt-2">
          {court.amenities.includes("Wifi") && (
            <span className="bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
              Wifi
            </span>
          )}
          {court.amenities.includes("Canteen") && (
            <span className="bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
              Canteen
            </span>
          )}
          {court.amenities.includes("Parking") && (
            <span className="bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
              Parking
            </span>
          )}
        </div>
      </div>
      <div className="px-6 pt-4 pb-2">
        <button
          onClick={handleViewDetails}
          className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
        >
          Xem Chi Tiết
        </button>
      </div>
    </div>
  );
};

const CourtList = () => {
  const courtData = [
    {
      id: 1,
      name: "Sân cầu lông Việt Đức",
      location: "Hồ Chí Minh",
      courts: 2,
      rating: 4,
      operatingHours: { start: "10:00 AM", end: "10:00 PM" },
      amenities: ["Wifi", "Canteen"],
      price: 100000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 2,
      name: "Sân cầu lông Phú Thọ",
      location: "Hồ Chí Minh",
      courts: 4,
      rating: 5,
      operatingHours: { start: "10:00 AM", end: "10:00 PM" },
      amenities: ["Wifi", "Parking"],
      price: 120000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    // ... Các sân khác
  ];

  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedPrice, setSelectedPrice] = useState("");
  const [selectedRating, setSelectedRating] = useState("");
  const courtsPerPage = 6;

  const handleTimeChange = (event) => {
    setSelectedTime(event.target.value);
  };

  const handlePriceChange = (event) => {
    setSelectedPrice(event.target.value);
  };

  const handleRatingChange = (event) => {
    setSelectedRating(event.target.value);
  };

  const filteredCourts = courtData
    .filter(
      (court) =>
        court.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        court.location.toLowerCase().includes(searchTerm.toLowerCase())
    )
    .filter((court) =>
      selectedTime ? court.availableTimes.includes(selectedTime) : true
    )
    .filter((court) =>
      selectedPrice ? court.price <= parseInt(selectedPrice) : true
    )
    .filter((court) =>
      selectedRating ? court.rating >= parseInt(selectedRating) : true
    );

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
      <div className="flex flex-row gap-4 items-center justify-center">
        <div className="flex justify-center">
          <input
            type="text"
            placeholder="Tìm kiếm sân..."
            className="px-4 py-2 border rounded-lg w-[20rem]"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="flex justify-center mb-8">
          <div className="mr-4">
            <label className="block mb-2">Khung giờ</label>
            <select
              className="px-6 py-2 border rounded"
              value={selectedTime}
              onChange={handleTimeChange}
            >
              <option value="">Tất cả</option>
              <option value="15:00">15:00</option>
              <option value="16:00">16:00</option>
              <option value="17:00">17:00</option>
              <option value="18:00">18:00</option>
              <option value="19:00">19:00</option>
              <option value="20:00">20:00</option>
            </select>
          </div>
          <div className="mr-4">
            <label className="block mb-2">Giá</label>
            <select
              className="px-6 py-2 border rounded"
              value={selectedPrice}
              onChange={handlePriceChange}
            >
              <option value="">Tất cả</option>
              <option value="100000">Dưới 100,000 VND</option>
              <option value="120000">Dưới 120,000 VND</option>
              <option value="140000">Dưới 140,000 VND</option>
            </select>
          </div>
          <div>
            <label className="block mb-2">Đánh giá</label>
            <select
              className="px-4 py-2 border rounded"
              value={selectedRating}
              onChange={handleRatingChange}
            >
              <option value="">Tất cả</option>
              <option value="3">3 sao trở lên</option>
              <option value="4">4 sao trở lên</option>
              <option value="5">5 sao</option>
            </select>
          </div>
        </div>
      </div>
      <div className="flex flex-wrap justify-center">
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
