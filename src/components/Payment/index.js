import React from "react";
import { useNavigate, useLocation } from "react-router-dom";

const PaymentPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const court = location.state?.court;
  const selectedTime = location.state?.selectedTime;
  const selectedDate = location.state?.selectedDate;
  const name = location.state?.name;
  const email = location.state?.email;
  const bookingType = location.state?.bookingType;
  const dayOfWeek = location.state?.dayOfWeek;
  const months = location.state?.months;
  const startDate = location.state?.startDate;
  const flexibleBookings = location.state?.flexibleBookings;

  const handlePayment = (e) => {
    e.preventDefault();
    // Xử lý thanh toán tại đây
    console.log("Name:", name);
    console.log("Email:", email);
    console.log("Selected Time:", selectedTime);
    console.log("Selected Date:", selectedDate);
    console.log("Booking Type:", bookingType);
    console.log("Day of Week:", dayOfWeek);
    console.log("Months:", months);
    console.log("Start Date:", startDate);
    console.log("Flexible Bookings:", flexibleBookings);
    // Sau khi thanh toán thành công, điều hướng về trang chủ hoặc trang xác nhận
    navigate("/confirmation");
  };

  return (
    <div className="container mx-auto py-8">
      <div className="max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold text-center text-blue-600 mb-4">
          Thanh Toán
        </h2>
        {court && (
          <>
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
          </>
        )}

        <form onSubmit={handlePayment}>
          <div>
            <h3 className="text-lg font-bold mb-4">Thông tin cá nhân</h3>
            <div className="mb-4">
              <label className="block mb-2">Tên</label>
              <input
                type="text"
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                value={name}
                readOnly
              />
            </div>
            <div className="mb-4">
              <label className="block mb-2">Email</label>
              <input
                type="email"
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                value={email}
                readOnly
              />
            </div>
            {bookingType === "fixed" && (
              <>
                <div className="mb-4">
                  <label className="block mb-2">Thứ</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={dayOfWeek}
                    readOnly
                  />
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Thời gian đã chọn</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={selectedTime}
                    readOnly
                  />
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Số tháng đăng ký</label>
                  <input
                    type="number"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={months}
                    readOnly
                  />
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Ngày bắt đầu</label>
                  <input
                    type="date"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={startDate}
                    readOnly
                  />
                </div>
              </>
            )}
            {bookingType === "flexible" && (
              <div className="mb-4">
                <label className="block mb-2">Lịch linh hoạt đã chọn</label>
                {flexibleBookings.map((booking, index) => (
                  <div key={index} className="flex items-center mb-2">
                    <p className="mr-2">
                      {booking.date} - {booking.time}
                    </p>
                  </div>
                ))}
              </div>
            )}
            <div className="mb-4">
              <h3 className="text-lg font-bold mb-4">
                Quét mã QR để thanh toán
              </h3>
              <div className="mb-4 text-center">
                <img
                  src="https://firebasestorage.googleapis.com/v0/b/unicourse-f4020.appspot.com/o/images%2F0276bb70-990f-4092-9db3-6c5b1e175db2.jpeg?alt=media&token=a9bcfdd5-dc85-4e8d-8f49-1d009a20e73e"
                  alt="QR Code"
                  className="w-64 h-64 mx-auto"
                />
                <p className="text-gray-700 mt-4">
                  Vui lòng quét mã QR để thanh toán. Đợi phản hồi trong vòng 5 -
                  10 phút.
                </p>
              </div>
            </div>
            <div className="flex justify-between">
              <button
                type="button"
                className="bg-gray-500 text-white px-4 py-2 rounded-lg"
                onClick={() => navigate(-1)}
              >
                Quay lại
              </button>
              <button
                type="submit"
                className="bg-blue-500 text-white px-4 py-2 rounded-lg"
              >
                Thanh Toán
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PaymentPage;
