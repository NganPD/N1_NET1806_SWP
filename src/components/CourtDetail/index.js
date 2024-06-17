import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Modal from "react-modal";

Modal.setAppElement("#root");

const CourtDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { court } = location.state || {};

  const [selectedDate, setSelectedDate] = useState("");
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentWeek, setCurrentWeek] = useState(0);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [bookingType, setBookingType] = useState("fixed");
  const [dayOfWeek, setDayOfWeek] = useState("");
  const [months, setMonths] = useState("");
  const [startDate, setStartDate] = useState("");
  const [flexibleBookings, setFlexibleBookings] = useState([]);
  const [selectedTime, setSelectedTime] = useState("");

  const fixedTimes = [
    "10:00 - 12:00",
    "12:00 - 14:00",
    "14:00 - 16:00",
    "16:00 - 18:00",
    "18:00 - 20:00",
    "20:00 - 22:00",
  ];

  if (!court) {
    return <div>Không tìm thấy thông tin sân</div>;
  }

  const handleBooking = () => {
    navigate("/payment", {
      state: {
        court,
        selectedDate,
        selectedTime,
        name,
        email,
        bookingType,
        dayOfWeek,
        months,
        startDate,
        flexibleBookings,
      },
    });
  };

  const openModal = (index) => {
    setSelectedSlot(index);
    setIsModalOpen(true);
  };

  const getWeekDates = (weekOffset = 0) => {
    const now = new Date();
    const startOfWeek = new Date(
      now.setDate(now.getDate() - now.getDay() + 1 + 7 * weekOffset)
    );
    return Array.from({ length: 7 }, (_, i) => {
      const date = new Date(startOfWeek);
      date.setDate(startOfWeek.getDate() + i);
      return date;
    });
  };

  const weekDates = getWeekDates(currentWeek);

  const renderTimeslots = (date) => {
    return court.availableTimes.map((slot, index) => {
      const isPast = date < new Date() || !slot.status;
      return (
        <div
          key={index}
          className={`m-2 p-2 border rounded-lg shadow-lg cursor-pointer ${
            selectedSlot === index ? "bg-blue-300" : "bg-blue-100"
          } ${isPast ? "bg-gray-300 cursor-not-allowed" : "cursor-pointer"}`}
          onClick={() => !isPast && openModal(index)}
        >
          <p className="text-center">{slot.time}</p>
          <p className="text-center">120k</p>
        </div>
      );
    });
  };

  return (
    <div className="container mx-auto my-8">
      <div className="flex flex-wrap">
        <div className="w-full md:w-1/2">
          <img
            className="w-full h-[70vh] object-cover rounded-lg"
            src={court.image}
            alt="Court"
          />
        </div>
        <div className="w-full md:w-1/2 px-8">
          <h1 className="text-4xl font-bold mb-4">{court.name}</h1>
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

          <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
            <h2 className="text-2xl font-bold mb-4">Chọn loại lịch đặt sân</h2>
            <div className="mb-4">
              <label className="block mb-2">Loại lịch:</label>
              <select
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                value={bookingType}
                onChange={(e) => setBookingType(e.target.value)}
              >
                <option value="fixed">Lịch cố định</option>
                <option value="flexible">Lịch linh hoạt</option>
              </select>
            </div>
            {bookingType === "fixed" && (
              <>
                <div className="mb-4">
                  <label className="block mb-2">Chọn thứ</label>
                  <select
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={dayOfWeek}
                    onChange={(e) => setDayOfWeek(e.target.value)}
                    required
                  >
                    <option value="">Chọn thứ</option>
                    <option value="Monday">Thứ Hai</option>
                    <option value="Tuesday">Thứ Ba</option>
                    <option value="Wednesday">Thứ Tư</option>
                    <option value="Thursday">Thứ Năm</option>
                    <option value="Friday">Thứ Sáu</option>
                    <option value="Saturday">Thứ Bảy</option>
                    <option value="Sunday">Chủ Nhật</option>
                  </select>
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Chọn giờ chơi cố định</label>
                  <div className="flex flex-wrap">
                    {fixedTimes.map((time, index) => (
                      <button
                        key={index}
                        type="button"
                        className={`m-2 px-4 py-2 rounded-full ${
                          selectedTime === time
                            ? "bg-blue-500 text-white"
                            : "bg-gray-200 text-gray-700"
                        }`}
                        onClick={() => setSelectedTime(time)}
                      >
                        {time}
                      </button>
                    ))}
                  </div>
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Đăng ký bao nhiêu tháng</label>
                  <input
                    type="number"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={months}
                    onChange={(e) => setMonths(e.target.value)}
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Bắt đầu từ ngày</label>
                  <input
                    type="date"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    required
                  />
                </div>
              </>
            )}
            {bookingType === "flexible" && (
              <div>
                <div className="mb-4">
                  <label className="block mb-2">Chọn ngày và giờ</label>
                  <div className="flex mb-2">
                    <input
                      type="date"
                      className="w-1/2 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                      value={selectedDate}
                      onChange={(e) => setSelectedDate(e.target.value)}
                    />
                    <select
                      className="w-1/2 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                      value={selectedTime}
                      onChange={(e) => setSelectedTime(e.target.value)}
                    >
                      <option value="">Chọn giờ</option>
                      {fixedTimes.map((time, index) => (
                        <option key={index} value={time}>
                          {time}
                        </option>
                      ))}
                    </select>
                  </div>
                  <button
                    type="button"
                    className="bg-green-500 text-white px-4 py-2 rounded-lg"
                    onClick={() => {
                      if (selectedDate && selectedTime) {
                        setFlexibleBookings([
                          ...flexibleBookings,
                          { date: selectedDate, time: selectedTime },
                        ]);
                        setSelectedDate("");
                        setSelectedTime("");
                      } else {
                        alert("Vui lòng chọn ngày và thời gian trước khi thêm");
                      }
                    }}
                  >
                    Thêm ngày
                  </button>
                </div>
                {flexibleBookings.length > 0 && (
                  <div>
                    <h4 className="text-lg font-bold mb-2">
                      Lịch linh hoạt đã chọn
                    </h4>
                    {flexibleBookings.map((booking, index) => (
                      <div key={index} className="flex items-center mb-2">
                        <p className="mr-2">
                          {booking.date} - {booking.time}
                        </p>
                        <button
                          type="button"
                          className="bg-red-500 text-white px-2 py-1 rounded-lg"
                          onClick={() => {
                            const updatedBookings = flexibleBookings.filter(
                              (_, i) => i !== index
                            );
                            setFlexibleBookings(updatedBookings);
                          }}
                        >
                          Xóa
                        </button>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}
            <div className="mb-4">
              <label className="block mb-2">Họ và tên:</label>
              <input
                type="text"
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            <div className="mb-4">
              <label className="block mb-2">Email:</label>
              <input
                type="email"
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <button
              onClick={handleBooking}
              className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
            >
              Đặt Sân
            </button>
          </div>
        </div>
      </div>

      <div className="mt-8 bg-white p-4 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-4">
          Đặt sân theo khung thời gian
        </h2>
        <div className="flex justify-between items-center mb-4">
          <button
            onClick={() => setCurrentWeek((prev) => prev - 1)}
            className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
          >
            Tuần trước
          </button>
          <h3 className="text-xl font-semibold">
            Từ ngày {weekDates[0].toLocaleDateString()} đến ngày{" "}
            {weekDates[6].toLocaleDateString()}
          </h3>
          <button
            onClick={() => setCurrentWeek((prev) => prev + 1)}
            className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
          >
            Tuần sau
          </button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-7 gap-4">
          {weekDates.map((date, index) => (
            <div key={index} className="flex flex-col items-center">
              <h4 className="font-bold">{date.toLocaleDateString()}</h4>
              {renderTimeslots(date)}
            </div>
          ))}
        </div>
      </div>

      <Modal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        contentLabel="Confirm Booking"
        className="bg-white p-6 rounded-lg shadow-lg max-w-md mx-auto mt-20"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
        {selectedSlot !== null && (
          <>
            <h2 className="text-2xl font-bold mb-4">Xác nhận đặt sân</h2>
            <p>Tên sân: {court.name}</p>
            <p>Thời gian: {court.availableTimes[selectedSlot].time}</p>
            <p>Giá: 120k</p>
            <div className="flex justify-end mt-4">
              <button
                onClick={() => setIsModalOpen(false)}
                className="bg-gray-300 text-gray-700 rounded-full px-4 py-2 mr-2 hover:bg-gray-400"
              >
                Hủy
              </button>
              <button
                onClick={handleBooking}
                className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
              >
                Xác nhận
              </button>
            </div>
          </>
        )}
      </Modal>
    </div>
  );
};

export default CourtDetails;
