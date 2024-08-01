import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Modal from "react-modal";
import moment from "moment-timezone";
import api from "../../config/axios";
import {  useSelector } from "react-redux";
import { selectUser } from "../../redux/features/counterSlice";
import {
  InputNumber,
  Select,
  Modal as ModalANTD,
  Form,
  Button,
  DatePicker,
} from "antd";
import { toast } from "react-toastify";
import { useForm } from "antd/es/form/Form";

Modal.setAppElement("#root");


const CourtDetails = () => {
  const navigate = useNavigate();
  const user = useSelector(selectUser);
  const [form] = useForm();
  const [step, setStep] = useState(1);
  const [selectedDate, setSelectedDate] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [selectedCourt, setSelectedCourt] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [bookingType, setBookingType] = useState("fixed");
  const [months, setMonths] = useState("");
  const [startDate, setStartDate] = useState([]);

  const [errors, setErrors] = useState({});

  const [openModal2, setOpenModal] = useState(false);
  const daysOfWeek = [
    { label: "Thứ Hai", value: "Monday" },
    { label: "Thứ Ba", value: "Tuesday" },
    { label: "Thứ Tư", value: "Wednesday" },
    { label: "Thứ Năm", value: "Thursday" },
    { label: "Thứ Sáu", value: "Friday" },
    { label: "Thứ Bảy", value: "Saturday" },
    { label: "Chủ Nhật", value: "Sunday" },
  ];
  const { id } = useParams();
  const [data, setData] = useState([]);
  const fetch = async () => {
    try {
      const response = await api.get(`/venues/${id}`);
      console.log(response.data);
      setData(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  const [courtData, setCourtData] = useState([]);

  const fetCourt = async () => {
    try {
      const response = await api.get(`/venues/${id}/courts`);
      console.log(response.data);
      setCourtData(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetCourt();
  }, []);

  const [slotDaily, setSlotDaily] = useState([]);

  const fetchSlotDaily = async () => {
    try {
      const response = await api.get(
        `/timeslots/available-slots?date=${selectedDate}&venueId=${id}`
      );
      console.log(response.data);
      setSlotDaily(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleCreateBooking = async () => {
    console.log(selectedDate);
    console.log(months);
    console.log(startDate);
    console.log(selectedCourt);
    console.log(selectedSlot);
    try {
      let response;
      if (bookingType == "fixed") {
        response = await api.post("/booking/fixed-schedule", {
          applicationStartDate: selectedDate,
          durationInMonths: months,
          dayOfWeek: startDate,
          court: courtSelect,
          timeslot: selectedSlot,
        });
      } else if (bookingType == "daily") {
        response = await api.post("/booking/daily-schedule", {
          checkInDate: selectedDate,
          court: selectedCourt,
          timeslot: selectedSlot,
        });
      } else {
      }
      console.log(response);
      toast.success("Đặt lịch thành công !!!");
    } catch (error) {
      toast.error(error.response.data);
    }
  };

  const handleBuyHour = async (values) => {
    console.log(values);
    const days = moment(values.days.$d).format("YYYY-MM-DD");
    console.log(days);
    try {
      const response = await api.post(
        `/booking/purchase-hours/hours/${values.hours}/id/${id}/applicationDate/${days}`
      );
      toast.success("Mua giờ thành công !!!");
      setOpenModal(false);
    } catch (error) {
      toast.error(error.response.data);
    }
  };

  const [slots, setSlots] = useState([]);
  const [courtSelect, setCourtSelect] = useState([]);
  const fetchSlot = async () => {
    try {
      const response = await api.get(
        `/timeslots/venue-slots?venueId=${id}`
      );
      console.log(response.data);
      setSlots(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (bookingType === "fixed") {
      fetchSlot();
    } else if (bookingType === "daily") {
      fetchSlotDaily();
    }
  }, [selectedDate, months, startDate, selectedCourt]);

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

  const handleNextStep = () => {
    if (!user) {
      navigate("/login");
      return;
    }
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


  const today = moment.tz("Asia/Ho_Chi_Minh").format("YYYY-MM-DD");

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleChange = (selectedOption) => {
    setSelectedSlot(selectedOption);
    // console.log(selectedOption);
  };



  const formatMoneyVND = (amount) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
  };

  const handleChange2 = (selectedOption) => {
    setStartDate(selectedOption);
    // console.log(selectedOption);
  };

  const options = data?.courts?.map((item) => ({
    value: item.id,
    label: item.courtName,
  }));
  return (
    <div className="container mx-auto my-8">
      <div className="flex flex-wrap">
        <div className="w-full md:w-1/2">
          <img
            className="w-full h-[70vh] object-cover rounded-lg"
            src={data?.imageUrl}
            alt="Court"
          />
        </div>
        <div className="w-full md:w-1/2 px-8">
          <h1 className="text-4xl font-bold mb-4">{data?.name}</h1>
          <p className="text-gray-700 text-base mb-2">
            Khu vực: {data?.address}
          </p>
          <p className="text-gray-700 text-base mb-2">
            Số sân: {data?.numberOfCourts}
          </p>
          <div className="text-yellow-500 mb-2">
            {"★".repeat(data?.rating)}
            {"☆".repeat(5 - data?.rating)}
          </div>
          <p className="text-gray-700 text-base mb-2">Giá lịch cố định:{formatMoneyVND(data?.fixedPrice)} </p>
          <p className="text-gray-700 text-base mb-2">Giá lịch linh hoạt: {formatMoneyVND(data?.flexiblePrice)} </p>
          <p className="text-gray-700 text-base mb-2">Giá lịch ngày: {formatMoneyVND(data?.dailyPrice)}   </p>
          <div className="mb-4">
            <span className="text-lg font-semibold">
              Giờ hoạt động: {data?.openingHour}
            </span>
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
                <Select
                  mode="tags"
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={startDate}
                  onChange={handleChange2}
                  options={daysOfWeek.map((item) => ({
                    label: item.label,
                    value: item.value,
                  }))}
                ></Select>
                {errors.startDate && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.startDate}
                  </p>
                )}
              </div>
              <div className="mb-4">
                <label className="block mb-2">Chọn giờ:</label>
                <Select
                  className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  mode="tags"
                  style={{ width: "100%" }}
                  placeholder="Chọn giờ"
                  onChange={handleChange}
                  options={slots.map((item) => ({
                    value: item.id,
                    label: `${item.startTime.substring(0, 5)} - ${item.endTime.substring(0, 5)}`,
                  }))}
                />

                {errors.selectedTime && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.selectedTime}
                  </p>
                )}
              </div>
              <div className="mb-4">
                <label className="block mb-2">Chọn sân:</label>
                <Select
                  className="w-full h-fit px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  value={courtSelect}
                  onChange={(e) => setCourtSelect(e)}
                  options={courtData?.map((item) => ({
                    value: item.id,
                    label: item.courtName,
                    disabled: item.status == "INACTIVE",
                  }))}
                />
                {errors.selectedCourt && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.selectedCourt}
                  </p>
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
                  onClick={handleCreateBooking}
                  className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                >
                  Đặt lịch
                </button>
              </div>
            </div>
          )}
          {step === 2 && bookingType === "flexible" && (
            <>
              <div className="bg-gray-100 p-4 rounded-lg shadow-md mb-4">
                <ModalANTD
                  footer={false}
                  onOk={() => form.submit()}
                  onCancel={() => setOpenModal(false)}
                  title="Chọn số giờ"
                  open={openModal2}
                >
                  <Form
                    form={form}
                    name="basic"
                    labelCol={{
                      span: 8,
                    }}
                    wrapperCol={{
                      span: 16,
                    }}
                    style={{
                      maxWidth: 600,
                    }}
                    initialValues={{
                      remember: true,
                    }}
                    onFinish={handleBuyHour}
                    autoComplete="off"
                  >
                    <Form.Item
                      labelCol={{ span: "24" }}
                      label="Số giờ muốn mua"
                      name="hours"
                      rules={[
                        {
                          required: true,
                          message: "Vui lòng nhập số giờ!",
                        },
                      ]}
                    >
                      <InputNumber />
                    </Form.Item>

                    <Form.Item
                      labelCol={{ span: "24" }}
                      label="Ngày áp dụng"
                      name="days"
                      rules={[
                        {
                          required: true,
                          message: "Vui lòng chọn ngày áp dụng!",
                        },
                      ]}
                    >
                      <DatePicker />
                    </Form.Item>

                    <Form.Item
                      wrapperCol={{
                        offset: 8,
                        span: 16,
                      }}
                    >
                      <Button type="primary" htmlType="submit">
                        Submit
                      </Button>
                    </Form.Item>
                  </Form>
                </ModalANTD>
                <div className="flex flex-row justify-between items-center">
                  <h2 className="text-2xl font-bold mb-4">Chọn giờ chơi</h2>
                  
                </div>

                <div className="flex justify-between">
                  <button
                    onClick={handlePreviousStep}
                    className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
                  >
                    Quay lại
                  </button>
                  <div className="flex flex-row items-center justify-between gap-3">
                    <button
                      onClick={() => setOpenModal(true)}
                      className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                    >
                      + ( Mua giờ )
                    </button>
                  </div>
                </div>
              </div>
            </>
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
                <label className="block mb-2">Chọn Slots:</label>
                <Select
                  className="w-full mb-3"
                  mode="tags"
                  value={selectedSlot}
                  options={slotDaily?.map((item) => ({
                    label: `${item.startTime.substring(0, 5)} - ${item.endTime.substring(0, 5)}`,
                    value: item.id,
                    disabled:item.available === false
                  }))}
                  onChange={(e) => setSelectedSlot(e)}
                ></Select>
                <label className="block mb-2">Chọn sân:</label>
                <Select
                  className="w-full"
                  value={selectedCourt}
                  options={courtData?.map((item) => ({
                    value: item.id,
                    label: item.courtName,
                    disabled: item.status == "INACTIVE",
                  }))}
                  onChange={(e) => setSelectedCourt(e)}
                ></Select>
              </div>
              <div className="flex justify-between">
                <button
                  onClick={handlePreviousStep}
                  className="bg-gray-500 text-white rounded-full px-4 py-2 hover:bg-gray-600"
                >
                  Quay lại
                </button>
                <button
                  onClick={handleCreateBooking}
                  className="bg-blue-500 text-white rounded-full px-4 py-2 hover:bg-blue-600"
                >
                  Đặt lịch
                </button>
              </div>
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
      ></Modal>
    </div>
  );
};

export default CourtDetails;
