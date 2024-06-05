import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const CourtCard = ({ court }) => {
  const navigate = useNavigate();

  const handleBooking = () => {
    navigate("/payment", { state: { court } });
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
        <div className="flex space-x-2">
          {court.availableTimes.map((time, index) => (
            <span
              key={index}
              className="bg-yellow-300 rounded-full px-3 py-1 text-sm font-semibold text-gray-700"
            >
              {time}
            </span>
          ))}
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
        </div>
      </div>
      <div className="px-6 pt-4 pb-2">
        <button
          onClick={handleBooking}
          className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
        >
          Đặt Sân
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
      availableTimes: ["17:30", "16:00", "15:00"],
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
      availableTimes: ["18:00", "19:00", "20:00"],
      amenities: ["Wifi", "Parking"],
      price: 120000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 3,
      name: "Sân cầu lông Thủ Đức",
      location: "Hồ Chí Minh",
      courts: 3,
      rating: 3,
      availableTimes: ["16:30", "17:30", "18:30"],
      amenities: ["Canteen", "Parking"],
      price: 90000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 4,
      name: "Sân cầu lông Tân Bình",
      location: "Hồ Chí Minh",
      courts: 5,
      rating: 4,
      availableTimes: ["17:00", "18:00", "19:00"],
      amenities: ["Wifi", "Canteen"],
      price: 110000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 5,
      name: "Sân cầu lông Quận 7",
      location: "Hồ Chí Minh",
      courts: 2,
      rating: 4,
      availableTimes: ["17:30", "18:30", "19:30"],
      amenities: ["Wifi", "Parking"],
      price: 100000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 6,
      name: "Sân cầu lông Gò Vấp",
      location: "Hồ Chí Minh",
      courts: 6,
      rating: 5,
      availableTimes: ["18:00", "19:00", "20:00"],
      amenities: ["Wifi", "Canteen"],
      price: 130000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 7,
      name: "Sân cầu lông Bình Thạnh",
      location: "Hồ Chí Minh",
      courts: 3,
      rating: 4,
      availableTimes: ["17:30", "18:30", "19:30"],
      amenities: ["Wifi", "Parking"],
      price: 100000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 8,
      name: "Sân cầu lông Quận 1",
      location: "Hồ Chí Minh",
      courts: 4,
      rating: 4,
      availableTimes: ["18:00", "19:00", "20:00"],
      amenities: ["Wifi", "Canteen"],
      price: 120000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 9,
      name: "Sân cầu lông Bình Tân",
      location: "Hồ Chí Minh",
      courts: 5,
      rating: 5,
      availableTimes: ["18:30", "19:30", "20:30"],
      amenities: ["Wifi", "Parking"],
      price: 140000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 10,
      name: "Sân cầu lông Quận 3",
      location: "Hồ Chí Minh",
      courts: 3,
      rating: 3,
      availableTimes: ["17:00", "18:00", "19:00"],
      amenities: ["Canteen", "Parking"],
      price: 90000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 11,
      name: "Sân cầu lông Quận 4",
      location: "Hồ Chí Minh",
      courts: 4,
      rating: 4,
      availableTimes: ["17:30", "18:30", "19:30"],
      amenities: ["Wifi", "Parking"],
      price: 100000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
    {
      id: 12,
      name: "Sân cầu lông Phú Nhuận",
      location: "Hồ Chí Minh",
      courts: 2,
      rating: 4,
      availableTimes: ["17:00", "18:00", "19:00"],
      amenities: ["Wifi", "Canteen"],
      price: 100000,
      image:
        "https://www.shutterstock.com/image-photo/badminton-sport-equipments-rackets-shuttlecocks-600nw-2302308577.jpg",
    },
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
      <h1 className="text-4xl font-bold text-center my-8">List Court</h1>

      <div class="flex flex-row gap-4 items-center justify-center">
        <div className="flex justify-center ">
          <input
            class=""
            type="text"
            placeholder="Tìm kiếm sân..."
            className="px-4  py-2 border rounded-lg w-[20rem]"
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
