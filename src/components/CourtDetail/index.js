import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Modal from "react-modal";

Modal.setAppElement("#root");

const CourtDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { court } = location.state || {};

  const [step, setStep] = useState(1);
  const [selectedDate, setSelectedDate] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [bookingType, setBookingType] = useState("fixed");
  const [months, setMonths] = useState("");
  const [startDate, setStartDate] = useState("");
  const [flexibleBookings, setFlexibleBookings] = useState([]);
  const [cardNumber, setCardNumber] = useState("");
  const [expiryDate, setExpiryDate] = useState("");
  const [cvv, setCvv] = useState("");

  const fixedTimes = [
    "05:00 - 06:00",
    "06:00 - 07:00",
    "07:00 - 08:00",
    "08:00 - 09:00",
    "09:00 - 10:00",
    "11:00 - 12:00",
    "12:00 - 13:00",
    "13:00 - 14:00",
    "14:00 - 15:00",
    "15:00 - 16:00",
    "16:00 - 17:00",
    "17:00 - 18:00",
    "18:00 - 19:00",
    "19:00 - 20:00",
    "20:00 - 21:00",
    "21:00 - 22:00",
  ];

  if (!court) {
    return <div>Không tìm thấy thông tin sân</div>;
  }

  const handleNextStep = () => {
    setStep(step + 1);
  };

  const handlePreviousStep = () => {
    setStep(step - 1);
  };

  const handleAddFlexibleBooking = () => {
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
  };

  const handleRemoveFlexibleBooking = (index) => {
    const updatedBookings = flexibleBookings.filter((_, i) => i !== index);
    setFlexibleBookings(updatedBookings);
  };

  const handlePayment = (e) => {
    e.preventDefault();
    // Xử lý thanh toán tại đây
    console.log("Name:", name);
    console.log("Email:", email);
    console.log("Selected Time:", selectedTime);
    console.log("Selected Date:", selectedDate);
    console.log("Booking Type:", bookingType);
    console.log("Months:", months);
    console.log("Start Date:", startDate);
    console.log("Flexible Bookings:", flexibleBookings);
    // Sau khi thanh toán thành công, điều hướng về trang xác nhận
    navigate("/confirmation");
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

          {step === 1 && (
            <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
              <h2 className="text-2xl font-bold mb-4">
                Chọn loại lịch đặt sân
              </h2>
              <div className="mb-4">
                <label className="block mb-2">Loại lịch:</label>
                <select
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={bookingType}
                  onChange={(e) => setBookingType(e.target.value)}
                >
                  <option value="fixed">Lịch cố định</option>
                  <option value="one-time">Lịch ngày</option>
                  <option value="flexible">Lịch linh hoạt</option>
                </select>
              </div>
              <button
                onClick={handleNextStep}
                className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
              >
                Tiếp tục
              </button>
            </div>
          )}

          {step === 2 && (
            <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
              <h2 className="text-2xl font-bold mb-4">Chi tiết lịch đặt sân</h2>
              {bookingType === "fixed" && (
                <>
                  <div className="mb-4">
                    <label className="block mb-2">Chọn ngày</label>
                    <input
                      type="date"
                      className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                      value={selectedDate}
                      onChange={(e) => setSelectedDate(e.target.value)}
                      required
                    />
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
                    <label className="block mb-2">
                      Đăng ký bao nhiêu tháng
                    </label>
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
              {bookingType === "one-time" && (
                <div className="mb-4">
                  <label className="block mb-2">Chọn ngày</label>
                  <input
                    type="date"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                    required
                  />
                  <div className="mb-4">
                    <label className="block mb-2">Chọn giờ chơi</label>
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
                </div>
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
                      onClick={handleAddFlexibleBooking}
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
                            onClick={() => handleRemoveFlexibleBooking(index)}
                          >
                            Xóa
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
              <div className="flex justify-between mt-4">
                <button
                  type="button"
                  className="bg-gray-500 text-white px-4 py-2 rounded-lg"
                  onClick={handlePreviousStep}
                >
                  Quay lại
                </button>
                <button
                  type="button"
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg"
                  onClick={handleNextStep}
                >
                  Tiếp tục
                </button>
              </div>
            </div>
          )}

          {step === 3 && (
            <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
              <h2 className="text-2xl font-bold mb-4">Thông tin cá nhân</h2>
              <div className="mb-4">
                <label className="block mb-2">Tên</label>
                <input
                  type="text"
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                />
              </div>
              <div className="mb-4">
                <label className="block mb-2">Email</label>
                <input
                  type="email"
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="flex justify-between mt-4">
                <button
                  type="button"
                  className="bg-gray-500 text-white px-4 py-2 rounded-lg"
                  onClick={handlePreviousStep}
                >
                  Quay lại
                </button>
                <button
                  type="button"
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg"
                  onClick={handleNextStep}
                >
                  Tiếp tục
                </button>
              </div>
            </div>
          )}

          {step === 4 && (
            <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
              <h2 className="text-2xl font-bold mb-4">Thanh toán</h2>
              <form onSubmit={handlePayment}>
                <div className="mb-4">
                  <label className="block mb-2">Số thẻ</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={cardNumber}
                    onChange={(e) => setCardNumber(e.target.value)}
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Ngày hết hạn</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={expiryDate}
                    onChange={(e) => setExpiryDate(e.target.value)}
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block mb-2">CVV</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={cvv}
                    onChange={(e) => setCvv(e.target.value)}
                    required
                  />
                </div>
                <div className="flex justify-between mt-4">
                  <button
                    type="button"
                    className="bg-gray-500 text-white px-4 py-2 rounded-lg"
                    onClick={handlePreviousStep}
                  >
                    Quay lại
                  </button>
                  <button
                    type="submit"
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg"
                  >
                    Thanh toán
                  </button>
                </div>
              </form>
              <div className="mt-4">
                <h3 className="text-xl font-bold mb-4">Hoặc quét mã QR</h3>
                <img
                  src="https://via.placeholder.com/200" // Thay thế bằng URL của mã QR thực tế
                  alt="QR Code"
                  className="w-1/2 mx-auto"
                />
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CourtDetails;
