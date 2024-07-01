// import React, { useState } from "react";
// import { useLocation, useNavigate } from "react-router-dom";
// import Modal from "react-modal";
// import moment from "moment-timezone";

// Modal.setAppElement("#root");

// const CourtDetails = () => {
//   const location = useLocation();
//   const navigate = useNavigate();
//   const { court } = location.state || {};

//   const [step, setStep] = useState(1);
//   const [selectedDate, setSelectedDate] = useState("");
//   const [selectedTime, setSelectedTime] = useState("");
//   const [selectedSlot, setSelectedSlot] = useState(null);
//   const [isModalOpen, setIsModalOpen] = useState(false);
//   const [name, setName] = useState("");
//   const [email, setEmail] = useState("");
//   const [bookingType, setBookingType] = useState("fixed");
//   const [months, setMonths] = useState("");
//   const [startDate, setStartDate] = useState("");
//   const [flexibleBookings, setFlexibleBookings] = useState([]);
//   const [cardNumber, setCardNumber] = useState("");
//   const [expiryDate, setExpiryDate] = useState("");
//   const [cvv, setCvv] = useState("");

//   const fixedTimes = [
//     "05:00 - 06:00",
//     "06:00 - 07:00",
//     "07:00 - 08:00",
//     "08:00 - 09:00",
//     "09:00 - 10:00",
//     "11:00 - 12:00",
//     "12:00 - 13:00",
//     "13:00 - 14:00",
//     "14:00 - 15:00",
//     "15:00 - 16:00",
//     "16:00 - 17:00",
//     "17:00 - 18:00",
//     "18:00 - 19:00",
//     "19:00 - 20:00",
//     "20:00 - 21:00",
//     "21:00 - 22:00",
//   ];

//   if (!court) {
//     return <div>Không tìm thấy thông tin sân</div>;
//   }

//   const handleNextStep = () => {
//     setStep(step + 1);
//   };

//   const handlePreviousStep = () => {
//     setStep(step - 1);
//   };

//   const handleAddFlexibleBooking = () => {
//     if (selectedDate && selectedTime) {
//       setFlexibleBookings([
//         ...flexibleBookings,
//         { date: selectedDate, time: selectedTime },
//       ]);
//       setSelectedDate("");
//       setSelectedTime("");
//     } else {
//       alert("Vui lòng chọn ngày và thời gian trước khi thêm");
//     }
//   };

//   const handleRemoveFlexibleBooking = (index) => {
//     const updatedBookings = flexibleBookings.filter((_, i) => i !== index);
//     setFlexibleBookings(updatedBookings);
//   };

//   const handlePayment = (e) => {
//     e.preventDefault();
//     // Xử lý thanh toán tại đây
//     console.log("Name:", name);
//     console.log("Email:", email);
//     console.log("Selected Time:", selectedTime);
//     console.log("Selected Date:", selectedDate);
//     console.log("Booking Type:", bookingType);
//     console.log("Months:", months);
//     console.log("Start Date:", startDate);
//     console.log("Flexible Bookings:", flexibleBookings);
//     // Sau khi thanh toán thành công, điều hướng về trang xác nhận
//     navigate("/confirmation");
//   };

//   const today = moment.tz("Asia/Ho_Chi_Minh").format("YYYY-MM-DD");

//   return (
//     <div className="container mx-auto my-8">
//       <div className="flex flex-wrap">
//         <div className="w-full md:w-1/2">
//           <img
//             className="w-full h-[70vh] object-cover rounded-lg"
//             src={court.image}
//             alt="Court"
//           />
//         </div>
//         <div className="w-full md:w-1/2 px-8">
//           <h1 className="text-4xl font-bold mb-4">{court.name}</h1>
//           <p className="text-gray-700 text-base mb-2">
//             Khu vực: {court.location}
//           </p>
//           <p className="text-gray-700 text-base mb-2">Số sân: {court.courts}</p>
//           <div className="text-yellow-500 mb-2">
//             {"★".repeat(court.rating)}
//             {"☆".repeat(5 - court.rating)}
//           </div>
//           <p className="text-gray-700 text-base mb-2">Giá: {court.price} VNĐ</p>
//           <div className="mb-4">
//             <span className="text-lg font-semibold">Giờ hoạt động:</span>
//             <p className="text-gray-700 text-base">
//               {court.operatingHours.start} - {court.operatingHours.end}
//             </p>
//           </div>
//           <div className="flex space-x-2 mb-4">
//             {court.amenities.includes("Wifi") && (
//               <span className="bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
//                 Wifi
//               </span>
//             )}
//             {court.amenities.includes("Canteen") && (
//               <span className="bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
//                 Canteen
//               </span>
//             )}
//             {court.amenities.includes("Parking") && (
//               <span className="bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
//                 Parking
//               </span>
//             )}
//           </div>

//           {step === 1 && (
//             <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
//               <h2 className="text-2xl font-bold mb-4">
//                 Chọn loại lịch đặt sân
//               </h2>
//               <div className="mb-4">
//                 <label className="block mb-2">Loại lịch:</label>
//                 <select
//                   className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                   value={bookingType}
//                   onChange={(e) => setBookingType(e.target.value)}
//                 >
//                   <option value="fixed">Lịch cố định</option>
//                   <option value="one-time">Lịch ngày</option>
//                   <option value="flexible">Lịch linh hoạt</option>
//                 </select>
//               </div>
//               <button
//                 onClick={handleNextStep}
//                 className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
//               >
//                 Tiếp tục
//               </button>
//             </div>
//           )}

//           {step === 2 && (
//             <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
//               <h2 className="text-2xl font-bold mb-4">Chi tiết lịch đặt sân</h2>
//               {bookingType === "fixed" && (
//                 <>
//                   <div className="mb-4">
//                     <label className="block mb-2">Chọn ngày</label>
//                     <input
//                       type="date"
//                       className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                       value={selectedDate}
//                       onChange={(e) => setSelectedDate(e.target.value)}
//                       min={today}
//                       required
//                     />
//                   </div>
//                   <div className="mb-4">
//                     <label className="block mb-2">Chọn giờ chơi cố định</label>
//                     <div className="flex flex-wrap">
//                       {fixedTimes.map((time, index) => (
//                         <button
//                           key={index}
//                           type="button"
//                           className={`m-2 px-4 py-2 rounded-full ${
//                             selectedTime === time
//                               ? "bg-blue-500 text-white"
//                               : "bg-gray-200 text-gray-700"
//                           }`}
//                           onClick={() => setSelectedTime(time)}
//                         >
//                           {time}
//                         </button>
//                       ))}
//                     </div>
//                   </div>
//                   <div className="mb-4">
//                     <label className="block mb-2">
//                       Đăng ký bao nhiêu tháng
//                     </label>
//                     <input
//                       type="number"
//                       className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                       value={months}
//                       onChange={(e) => setMonths(e.target.value)}
//                       required
//                     />
//                   </div>
//                   <div className="mb-4">
//                     <label className="block mb-2">Bắt đầu từ ngày</label>
//                     <input
//                       type="date"
//                       className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                       value={startDate}
//                       onChange={(e) => setStartDate(e.target.value)}
//                       min={today}
//                       required
//                     />
//                   </div>
//                 </>
//               )}

//               {bookingType === "one-time" && (
//                 <>
//                   <div className="mb-4">
//                     <label className="block mb-2">Chọn ngày</label>
//                     <input
//                       type="date"
//                       className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                       value={selectedDate}
//                       onChange={(e) => setSelectedDate(e.target.value)}
//                       min={today}
//                       required
//                     />
//                   </div>
//                   <div className="mb-4">
//                     <label className="block mb-2">Chọn giờ chơi</label>
//                     <div className="flex flex-wrap">
//                       {fixedTimes.map((time, index) => (
//                         <button
//                           key={index}
//                           type="button"
//                           className={`m-2 px-4 py-2 rounded-full ${
//                             selectedTime === time
//                               ? "bg-blue-500 text-white"
//                               : "bg-gray-200 text-gray-700"
//                           }`}
//                           onClick={() => setSelectedTime(time)}
//                         >
//                           {time}
//                         </button>
//                       ))}
//                     </div>
//                   </div>
//                 </>
//               )}

//               {bookingType === "flexible" && (
//                 <>
//                   <div className="mb-4">
//                     <label className="block mb-2">Chọn ngày</label>
//                     <input
//                       type="date"
//                       className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                       value={selectedDate}
//                       onChange={(e) => setSelectedDate(e.target.value)}
//                       min={today}
//                     />
//                   </div>
//                   <div className="mb-4">
//                     <label className="block mb-2">Chọn giờ chơi</label>
//                     <div className="flex flex-wrap">
//                       {fixedTimes.map((time, index) => (
//                         <button
//                           key={index}
//                           type="button"
//                           className={`m-2 px-4 py-2 rounded-full ${
//                             selectedTime === time
//                               ? "bg-blue-500 text-white"
//                               : "bg-gray-200 text-gray-700"
//                           }`}
//                           onClick={() => setSelectedTime(time)}
//                         >
//                           {time}
//                         </button>
//                       ))}
//                     </div>
//                   </div>
//                   <button
//                     onClick={handleAddFlexibleBooking}
//                     className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
//                   >
//                     Thêm vào lịch
//                   </button>
//                   <div className="mt-4">
//                     <h3 className="text-xl font-semibold mb-2">
//                       Lịch linh hoạt đã chọn
//                     </h3>
//                     {flexibleBookings.map((booking, index) => (
//                       <div
//                         key={index}
//                         className="flex justify-between items-center mb-2"
//                       >
//                         <span>
//                           {booking.date} - {booking.time}
//                         </span>
//                         <button
//                           onClick={() => handleRemoveFlexibleBooking(index)}
//                           className="bg-red-500 text-white rounded-full px-2 py-1 hover:bg-red-600"
//                         >
//                           Xóa
//                         </button>
//                       </div>
//                     ))}
//                   </div>
//                 </>
//               )}
//               <div className="flex justify-between mt-4">
//                 <button
//                   onClick={handlePreviousStep}
//                   className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
//                 >
//                   Quay lại
//                 </button>
//                 <button
//                   onClick={handleNextStep}
//                   className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
//                 >
//                   Tiếp tục
//                 </button>
//               </div>
//             </div>
//           )}

//           {step === 3 && (
//             <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
//               <h2 className="text-2xl font-bold mb-4">Chi tiết thanh toán</h2>
//               <form onSubmit={handlePayment}>
//                 <div className="mb-4">
//                   <label className="block mb-2">Họ tên</label>
//                   <input
//                     type="text"
//                     className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                     value={name}
//                     onChange={(e) => setName(e.target.value)}
//                     required
//                   />
//                 </div>
//                 <div className="mb-4">
//                   <label className="block mb-2">Email</label>
//                   <input
//                     type="email"
//                     className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                     value={email}
//                     onChange={(e) => setEmail(e.target.value)}
//                     required
//                   />
//                 </div>
//                 <div className="mb-4">
//                   <label className="block mb-2">Số thẻ</label>
//                   <input
//                     type="text"
//                     className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                     value={cardNumber}
//                     onChange={(e) => setCardNumber(e.target.value)}
//                     required
//                   />
//                 </div>
//                 <div className="mb-4">
//                   <label className="block mb-2">Ngày hết hạn</label>
//                   <input
//                     type="text"
//                     className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                     value={expiryDate}
//                     onChange={(e) => setExpiryDate(e.target.value)}
//                     required
//                   />
//                 </div>
//                 <div className="mb-4">
//                   <label className="block mb-2">CVV</label>
//                   <input
//                     type="text"
//                     className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
//                     value={cvv}
//                     onChange={(e) => setCvv(e.target.value)}
//                     required
//                   />
//                 </div>
//                 <div className="flex justify-between mt-4">
//                   <button
//                     onClick={handlePreviousStep}
//                     type="button"
//                     className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
//                   >
//                     Quay lại
//                   </button>
//                   <button
//                     type="submit"
//                     className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
//                   >
//                     Thanh toán
//                   </button>
//                 </div>
//               </form>
//             </div>
//           )}
//         </div>
//       </div>
//     </div>
//   );
// };

// export default CourtDetails;
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Modal from "react-modal";
import moment from "moment-timezone";

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
  const [errors, setErrors] = useState({});

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

  const validateStep1 = () => {
    const newErrors = {};
    if (!bookingType) {
      newErrors.bookingType = "Vui lòng chọn loại lịch.";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const validateStep2 = () => {
    const newErrors = {};
    if (!selectedDate) {
      newErrors.selectedDate = "Vui lòng chọn ngày.";
    }
    if (!selectedTime) {
      newErrors.selectedTime = "Vui lòng chọn giờ chơi.";
    }
    if (bookingType === "fixed" && !months) {
      newErrors.months = "Vui lòng nhập số tháng.";
    }
    if (bookingType === "fixed" && !startDate) {
      newErrors.startDate = "Vui lòng chọn ngày bắt đầu.";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const validateStep3 = () => {
    const newErrors = {};
    if (!name) {
      newErrors.name = "Vui lòng nhập họ tên.";
    }
    if (!email) {
      newErrors.email = "Vui lòng nhập email.";
    }
    if (!cardNumber) {
      newErrors.cardNumber = "Vui lòng nhập số thẻ.";
    }
    if (!expiryDate) {
      newErrors.expiryDate = "Vui lòng nhập ngày hết hạn.";
    }
    if (!cvv) {
      newErrors.cvv = "Vui lòng nhập CVV.";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleNextStep = () => {
    let isValid = false;
    if (step === 1) {
      isValid = validateStep1();
    } else if (step === 2) {
      isValid = validateStep2();
    }
    if (isValid) {
      setStep(step + 1);
    }
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
    if (validateStep3()) {
      console.log("Name:", name);
      console.log("Email:", email);
      console.log("Selected Time:", selectedTime);
      console.log("Selected Date:", selectedDate);
      console.log("Booking Type:", bookingType);
      console.log("Months:", months);
      console.log("Start Date:", startDate);
      console.log("Flexible Bookings:", flexibleBookings);
      // Navigate to confirmation page
      navigate("/confirmation");
    }
  };

  const today = moment.tz("Asia/Ho_Chi_Minh").format("YYYY-MM-DD");

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
                {errors.bookingType && (
                  <p className="text-red-500 text-sm mt-1">{errors.bookingType}</p>
                )}
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
              <h2 className="text-2xl font-bold mb-4">Chọn giờ chơi</h2>
              <div className="mb-4">
                <label className="block mb-2">Chọn ngày:</label>
                <input
                  type="date"
                  min={today}
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                />
                {errors.selectedDate && (
                  <p className="text-red-500 text-sm mt-1">{errors.selectedDate}</p>
                )}
              </div>
              <div className="mb-4">
                <label className="block mb-2">Chọn giờ:</label>
                <select
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={selectedTime}
                  onChange={(e) => setSelectedTime(e.target.value)}
                >
                  <option value="">Chọn giờ chơi</option>
                  {fixedTimes.map((time) => (
                    <option key={time} value={time}>
                      {time}
                    </option>
                  ))}
                </select>
                {errors.selectedTime && (
                  <p className="text-red-500 text-sm mt-1">{errors.selectedTime}</p>
                )}
              </div>
              {bookingType === "fixed" && (
                <div className="mb-4">
                  <label className="block mb-2">Số tháng đặt cố định:</label>
                  <input
                    type="number"
                    min="1"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={months}
                    onChange={(e) => setMonths(e.target.value)}
                    placeholder="Nhập số tháng"
                  />
                  {errors.months && (
                    <p className="text-red-500 text-sm mt-1">{errors.months}</p>
                  )}
                </div>
              )}
              {bookingType === "fixed" && (
                <div className="mb-4">
                  <label className="block mb-2">Chọn ngày bắt đầu:</label>
                  <input
                    type="date"
                    min={today}
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                  />
                  {errors.startDate && (
                    <p className="text-red-500 text-sm mt-1">{errors.startDate}</p>
                  )}
                </div>
              )}
              {bookingType === "flexible" && (
                <div>
                  <div className="flex mb-4">
                    <button
                      onClick={handleAddFlexibleBooking}
                      className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                    >
                      Thêm
                    </button>
                  </div>
                  <ul className="list-disc pl-6">
                    {flexibleBookings.map((booking, index) => (
                      <li key={index} className="mb-2">
                        Ngày: {booking.date}, Giờ: {booking.time}{" "}
                        <button
                          onClick={() => handleRemoveFlexibleBooking(index)}
                          className="text-red-500 hover:underline ml-2"
                        >
                          Xóa
                        </button>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
              <div className="flex justify-between">
                <button
                  onClick={handlePreviousStep}
                  className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
                >
                  Quay lại
                </button>
                <button
                  onClick={handleNextStep}
                  className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                >
                  Tiếp tục
                </button>
              </div>
            </div>
          )}

          {step === 3 && (
            <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
              <h2 className="text-2xl font-bold mb-4">Thông tin thanh toán</h2>
              <form onSubmit={handlePayment}>
                <div className="mb-4">
                  <label className="block mb-2">Họ tên:</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />
                  {errors.name && (
                    <p className="text-red-500 text-sm mt-1">{errors.name}</p>
                  )}
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Email:</label>
                  <input
                    type="email"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  />
                  {errors.email && (
                    <p className="text-red-500 text-sm mt-1">{errors.email}</p>
                  )}
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Số thẻ:</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={cardNumber}
                    onChange={(e) => setCardNumber(e.target.value)}
                  />
                  {errors.cardNumber && (
                    <p className="text-red-500 text-sm mt-1">{errors.cardNumber}</p>
                  )}
                </div>
                <div className="mb-4">
                  <label className="block mb-2">Ngày hết hạn:</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={expiryDate}
                    onChange={(e) => setExpiryDate(e.target.value)}
                  />
                  {errors.expiryDate && (
                    <p className="text-red-500 text-sm mt-1">{errors.expiryDate}</p>
                  )}
                </div>
                <div className="mb-4">
                  <label className="block mb-2">CVV:</label>
                  <input
                    type="text"
                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                    value={cvv}
                    onChange={(e) => setCvv(e.target.value)}
                  />
                  {errors.cvv && (
                    <p className="text-red-500 text-sm mt-1">{errors.cvv}</p>
                  )}
                </div>
                <div className="flex justify-between">
                  <button
                    onClick={handlePreviousStep}
                    className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
                  >
                    Quay lại
                  </button>
                  <button
                    type="submit"
                    className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                  >
                    Xác nhận
                  </button>
                </div>
              </form>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CourtDetails;



