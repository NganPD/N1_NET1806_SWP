import React from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { selectUser } from "../../redux/features/counterSlice";

const CourtCard = ({ court }) => {
  const navigate = useNavigate();
  const user = useSelector(selectUser);
  const dispatch = useDispatch();
  const handleViewDetails = () => {


      navigate("/court-details", { state: { court } });
    }
   

  return (
    <div onClick={() => navigate(`/court-details/${court.id}`, { state: { court } })} className="max-w-sm rounded overflow-hidden shadow-lg m-4 transition-transform transform hover:scale-105 hover:shadow-xl bg-white">
      <img className="w-full h-48 object-cover" src={court.imageUrl ? court.imageUrl : 'https://sancaulong.vn/upload_images/images/2022/04/18/3-san-cau-tada.jpg'} alt="Court" />
      <div className="px-6 py-4">
        <div className="font-bold text-xl mb-2 text-blue-600">{court.name}</div>
        <p className="text-gray-700 text-base">Số sân: {court.numberOfCourts}</p>
        <div className="flex items-center mb-2">
        </div>
        <div className="mb-2">
          <span className="text-lg font-semibold">Giờ mở cửa:</span>
          <p className="text-gray-700 text-base">{court.openingHour} </p>
        </div>
        <div className="mb-2">
          <span className="text-lg font-semibold">Giờ hoạt động:</span>
          <p className="text-gray-700 text-base">{court.operatingHours}</p>
        </div>
        <div className="mb-2">
          <span className="text-lg font-semibold">Địa chỉ</span>
          <p className="text-gray-700 text-base">{court.address}</p>
        </div>
      </div>
      <div className="px-6 pt-4 pb-2">
        <button
          onClick={handleViewDetails}
          className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600 transition-colors"
        >
          Đặt sân
        </button>
      </div>
    </div>
  );
};

export default CourtCard;
