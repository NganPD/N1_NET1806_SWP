import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const PaymentPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const court = location.state.court;

  const [step, setStep] = useState(1);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [expiryDate, setExpiryDate] = useState("");
  const [cvv, setCvv] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedDate, setSelectedDate] = useState("");
  const [bookingType, setBookingType] = useState("fixed");
  const [dayOfWeek, setDayOfWeek] = useState("");
  const [months, setMonths] = useState("");
  const [startDate, setStartDate] = useState("");

  const handleNextStep = () => {
    if (step === 1) {
      if (
        bookingType === "fixed" &&
        (!dayOfWeek || !selectedTime || !months || !startDate)
      ) {
        alert("Vui lòng điền đầy đủ thông tin cho lịch cố định");
        return;
      }
      if (
        (bookingType === "one-time" || bookingType === "flexible") &&
        (!selectedDate || !selectedTime)
      ) {
        alert("Vui lòng chọn ngày và thời gian đặt sân");
        return;
      }
    }
    setStep(step + 1);
  };

  const handlePreviousStep = () => {
    setStep(step - 1);
  };

  const handlePayment = (e) => {
    e.preventDefault();
    // Xử lý thanh toán tại đây
    console.log("Name:", name);
    console.log("Email:", email);
    console.log("Card Number:", cardNumber);
    console.log("Expiry Date:", expiryDate);
    console.log("CVV:", cvv);
    console.log("Selected Time:", selectedTime);
    console.log("Selected Date:", selectedDate);
    console.log("Booking Type:", bookingType);
    console.log("Day of Week:", dayOfWeek);
    console.log("Months:", months);
    console.log("Start Date:", startDate);
    // Sau khi thanh toán thành công, điều hướng về trang chủ hoặc trang xác nhận
    navigate("/confirmation");
  };

  return (
    <div className="container mx-auto py-8">
      <div className="max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold text-center text-blue-600 mb-4">
          Thanh Toán
        </h2>
        <div className="mb-4">
          <img
            src={court.image}
            alt="Court"
            className="w-full h-48 object-cover rounded-lg"
          />
        </div>
        <div className="mb-4">
          <h3 className="text-xl font-bold">{court.name}</h3>
          <p>Khu vực: {court.location}</p>
          <p>Số sân: {court.courts}</p>
          <p className="text-yellow-500">
            {"★".repeat(court.rating)}
            {"☆".repeat(5 - court.rating)}
          </p>
          <p>Giá: {court.price} VND</p>
        </div>

        <form onSubmit={handlePayment}>
          {step === 1 && (
            <div>
              <h3 className="text-lg font-bold mb-4">Chọn loại lịch đặt sân</h3>
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
                      {court.availableTimes.map((time, index) => (
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
              {(bookingType === "one-time" || bookingType === "flexible") && (
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
              )}
              <div className="flex flex-wrap mb-4">
                {court.availableTimes.map((time, index) => (
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
              <div className="flex justify-between">
                <button
                  type="button"
                  className="bg-gray-500 text-white px-4 py-2 rounded-lg"
                  onClick={handlePreviousStep}
                  disabled
                >
                  Quay lại
                </button>
                <button
                  type="button"
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg"
                  onClick={handleNextStep}
                >
                  Tiếp theo
                </button>
              </div>
            </div>
          )}

          {step === 2 && (
            <div>
              <h3 className="text-lg font-bold mb-4">Thông tin cá nhân</h3>
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
              <div className="flex justify-between">
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
                  Tiếp theo
                </button>
              </div>
            </div>
          )}

          {step === 3 && (
            <div>
              <h3 className="text-lg font-bold mb-4">Thông tin thẻ</h3>
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
              <div className="flex justify-between">
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
            </div>
          )}
        </form>
      </div>
    </div>
  );
};

export default PaymentPage;
