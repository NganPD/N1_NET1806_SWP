import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import Modal from "react-modal";
import moment from "moment-timezone";
import api from "../../config/axios";
import { Select } from "antd";

Modal.setAppElement("#root");

const CourtDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { court } = location.state || {};

  const [step, setStep] = useState(1);
  const [selectedDate, setSelectedDate] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [selectedCourt, setSelectedCourt] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [bookingType, setBookingType] = useState("fixed");
  const [months, setMonths] = useState("");
  const [startDate, setStartDate] = useState([]);
  const [flexibleBookings, setFlexibleBookings] = useState([]);
  const [cardNumber, setCardNumber] = useState("");
  const [expiryDate, setExpiryDate] = useState("");
  const [cvv, setCvv] = useState("");
  const [errors, setErrors] = useState({});
  const [currentWeek, setCurrentWeek] = useState(0);
  const [walletBalance] = useState(500000); // Example wallet balance, replace with real data

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

  const daysOfWeek = [
    { label: "Thứ Hai", value: "Monday" },
    { label: "Thứ Ba", value: "Tuesday" },
    { label: "Thứ Tư", value: "Wednesday" },
    { label: "Thứ Năm", value: "Thursday" },
    { label: "Thứ Sáu", value: "Friday" },
    { label: "Thứ Bảy", value: "Saturday" },
    { label: "Chủ Nhật", value: "Sunday" },
  ];
  const { id } = useParams()
  const [data, setData] = useState([])
  const fetch = async () => {
    try {
      const response = await api.get(`/venues/${id}`)
      console.log(response.data)
      setData(response.data)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    fetch()
  }, [])

  const [courtData, setCourtData] = useState([])

  const fetCourt = async () => {
    try {
      const response = await api.get(`/venues/${id}/courts`)
      console.log(response.data)
      setCourtData(response.data)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    fetCourt()
  }, [])


  const handleCreateBooking = () => {
    console.log(selectedDate)
    console.log(months)
    console.log(startDate)
    console.log(courtSelect)
    console.log(selectedSlot)
    // console.log(first)
  }
  const [slots, setSlots] = useState([])
  const [courtSelect, setCourtSelect] = useState([])
  const fetchSlot = async () => {

    try {
      const response = await api.get(`/timeslots/available-fixed-slots?dayOfWeek=${startDate}&applicationDate=${selectedDate}&durationMonth=${months}&courtId=${selectedCourt}`)
      console.log(response.data)
      setSlots(response.data)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    fetchSlot()
  }, [selectedDate, months, startDate, selectedCourt])

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
    if (bookingType === "fixed" && !selectedDate) {
      newErrors.selectedDate = "Vui lòng chọn ngày.";
    }
    if (bookingType === "fixed" && !selectedCourt) {
      newErrors.selectedCourt = "Vui lòng chọn sân.";
    }
    if (bookingType === "fixed" && !selectedTime) {
      // newErrors.selectedTime = "Vui lòng chọn giờ chơi.";
    }
    if (bookingType === "fixed" && !months) {
      newErrors.months = "Vui lòng nhập số tháng.";
    }
    if (bookingType === "fixed" && !startDate) {
      newErrors.startDate = "Vui lòng chọn thứ bắt đầu.";
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

  const handleAddToListBooking = () => {
    console.log(selectedDate)
    console.log(months)
    console.log(startDate)
    console.log(selectedCourt)
    console.log(selectedTime)
  }

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
    setIsPaymentModalOpen(true);
  };

  const handleConfirmPayment = () => {
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

  const today = moment.tz("Asia/Ho_Chi_Minh").format("YYYY-MM-DD");

  const weekDates = getWeekDates(currentWeek);

  const renderTimeslots = (date) => {
    return court.availableTimes.map((slot, index) => {
      const isPast = date < new Date() || !slot.status;
      return (
        <div
          key={index}
          className={`m-2 p-2 border rounded-lg shadow-lg ${selectedSlot === index ? "bg-blue-300" : "bg-blue-100"
            } ${isPast ? "bg-gray-300 cursor-not-allowed" : "cursor-pointer"}`}
          onClick={() => !isPast && setSelectedSlot(index)}
        >
          <p className="text-center">{slot.time}</p>
          <p className="text-center">120k</p>
        </div>
      );
    });
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const openPaymentModal = () => {
    setIsPaymentModalOpen(true);
  };

  const closePaymentModal = () => {
    setIsPaymentModalOpen(false);
  };

  const handleBooking = () => {
    navigate("/payment", {
      state: {
        court,
        selectedDate,
        selectedTime,
        name,
        email,
        bookingType,
      },
    });
  };

  const handleChange = (selectedOption) => {
    setSelectedSlot(selectedOption);
    // console.log(selectedOption);
  };


  const handleChange2 = (selectedOption) => {
    setStartDate(selectedOption);
    // console.log(selectedOption);
  };

  const options = data?.courts?.map((item) => ({
    value: item.id,
    label: item.courtName
  }));
  return (
    <div className="container mx-auto my-8">
      <div className="flex flex-wrap">
        <div className="w-full md:w-1/2">
          <img
            className="w-full h-[70vh] object-cover rounded-lg"
            src={data?.image_URL}
            alt="Court"
          />
        </div>
        <div className="w-full md:w-1/2 px-8">
          <h1 className="text-4xl font-bold mb-4">{data?.name}</h1>
          <p className="text-gray-700 text-base mb-2">
            Khu vực: {data?.address
            }
          </p>
          <p className="text-gray-700 text-base mb-2">Số sân: {data?.numberOfCourts}</p>
          <div className="text-yellow-500 mb-2">
            {"★".repeat(data?.rating)}
            {"☆".repeat(5 - data?.rating)}
          </div>
          <p className="text-gray-700 text-base mb-2">Giá: {data?.price} VNĐ</p>
          <div className="mb-4">
            <span className="text-lg font-semibold">Giờ hoạt động:  {data?.openingHour}</span>
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
                  <option value="flexible">Lịch linh hoạt</option>
                  <option value="daily">Lịch ngày</option>
                </select>
                {errors.bookingType && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.bookingType}
                  </p>
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

          {step === 2 && bookingType === "fixed" && (
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
                  <p className="text-red-500 text-sm mt-1">
                    {errors.selectedDate}
                  </p>
                )}
              </div>
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
              <div className="mb-4">
                <label className="block mb-2">Chọn thứ bắt đầu:</label>
                <Select mode="tags"
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={startDate}
                  onChange={handleChange2}
                  options={daysOfWeek.map((item) => ({
                    label: item.label,
                    value: item.value
                  }))}
                >

                </Select>
                {errors.startDate && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.startDate}
                  </p>
                )}
              </div>
              <div className="mb-4">
                <label className="block mb-2">Chọn sân:</label>
                <Select
                  className="w-full h-fit px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={courtSelect}
                  onChange={(e) => setCourtSelect(e)}
                  options={courtData.map((item) => ({
                    value: item.id,
                    label: item.courtName,
                    disabled: item.status == "INACTIVE"
                  }))}
                />
                {errors.selectedCourt && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.selectedCourt}
                  </p>
                )}
              </div>
              <div className="mb-4">
                <label className="block mb-2">Chọn giờ:</label>
                <Select
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  mode="tags"
                  style={{ width: '100%' }}
                  placeholder="Chọn giờ"
                  onChange={handleChange}
                  options={slots.map((item) => ({
                    value: item.id,
                    label: `${item.startTime} - ${item.endTime}`
                  }))}
                />

                {errors.selectedTime && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.selectedTime}
                  </p>
                )}
              </div>
              <button
                style={{
                  marginBottom: "20px"
                }}
                onClick={handleAddToListBooking}
                className="bg-green-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
              >
                Thêm
              </button>

              <div className="flex justify-between">
                <button
                  onClick={handlePreviousStep}
                  className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
                >
                  Quay lại
                </button>

                <button
                  // onClick={handleNextStep}
                  onClick={handleCreateBooking}
                  className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                >
                  Tiếp tục
                </button>
              </div>
            </div>
          )}

          {step === 2 && bookingType === "flexible" && (
            <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
              <div className="flex flex-row justify-between items-center">
                <h2 className="text-2xl font-bold mb-4">Chọn giờ chơi</h2>
                <div className="flex flex-row items-center justify-between gap-3">
                  <p>(còn lại 6 giờ chơi)</p>
                  <button className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600">
                    + ( thêm giờ )
                  </button>
                </div>
              </div>
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
                  <p className="text-red-500 text-sm mt-1">
                    {errors.selectedDate}
                  </p>
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
                  <p className="text-red-500 text-sm mt-1">
                    {errors.selectedTime}
                  </p>
                )}
              </div>
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

          {step === 2 && bookingType === "daily" && (
            <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
              <h2 className="text-2xl font-bold mb-4">
                Đặt sân theo khung thời gian
              </h2>
              <div className="mb-4">
                <label className="block mb-2">Chọn ngày:</label>
                <input
                  type="date"
                  min={today}
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                />
              </div>
              <div className="mb-4">
                <label className="block mb-2">Chọn sân:</label>
                <Select
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={selectedCourt}
                  onChange={(e) => console.log(e.target.value)}
                >
                </Select>
              </div>

              <div className="flex justify-between">
                <button
                  onClick={handlePreviousStep}
                  className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
                >
                  Quay lại
                </button>
                <button
                  onClick={openModal}
                  className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                >
                  Chọn giờ chơi
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
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="Chọn giờ chơi"
        className="bg-white p-6 rounded-lg shadow-lg max-w-4xl mx-auto mt-20"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >

      </Modal>

      <Modal
        isOpen={isPaymentModalOpen}
        onRequestClose={closePaymentModal}
        contentLabel="Xác nhận thanh toán"
        className="bg-white p-6 rounded-lg shadow-lg max-w-4xl mx-auto mt-20"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
        <div>
          <h2 className="text-2xl font-bold mb-4">Xác nhận thanh toán</h2>
          <p>Tên: {name}</p>
          <p>Email: {email}</p>
          <p>Ngày: {selectedDate}</p>
          <p>Giờ: {selectedTime}</p>
          <p>Sân: {selectedCourt}</p>
          <p>Loại đặt sân: {bookingType}</p>
          <p>Tổng tiền: 120k</p>
          <p>Số dư ví: {walletBalance} VNĐ</p>
          <div className="flex justify-end mt-4">
            <button
              onClick={closePaymentModal}
              className="bg-gray-300 text-gray-700 rounded-full px-4 py-2 mr-2 hover:bg-gray-400"
            >
              Hủy
            </button>
            <button
              onClick={handleConfirmPayment}
              className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
            >
              Xác nhận thanh toán
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default CourtDetails;
