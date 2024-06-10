import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

const CourtDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { court } = location.state;

  const handleBooking = () => {
    navigate("/payment", { state: { court } });
  };

  return (
    <div className="container mx-auto my-8 flex">
      <div className="w-1/2">
        <img
          className="w-full h-full object-cover"
          src={court.image}
          alt="Court"
        />
      </div>
      <div className="w-1/2 px-8">
        <h1 className="text-3xl font-bold mb-4">{court.name}</h1>
        <p className="text-gray-700 text-base mb-2">
          Khu vực: {court.location}
        </p>
        <p className="text-gray-700 text-base mb-2">Số sân: {court.courts}</p>
        <div className="text-yellow-500 mb-2">
          {"★".repeat(court.rating)}
          {"☆".repeat(5 - court.rating)}
        </div>
        <p className="text-gray-700 text-base mb-2">Giá: {court.price} VNĐ</p>
        <div className="mb-4">
          <span className="text-lg font-semibold">Giờ hoạt động:</span>
          <p className="text-gray-700 text-base">
            {court.operatingHours.start} - {court.operatingHours.end}
          </p>
        </div>
        <div className="flex space-x-2 mb-4">
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

export default CourtDetails;
