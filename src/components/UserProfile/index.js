import React, { useEffect, useState } from "react";
import Modal from "react-modal";
<<<<<<< HEAD
import { Button, DatePicker, Form, Modal as ModalANTD, Select, Spin } from "antd";
=======
import { Button, DatePicker, Form, Modal as ModalANTD, Select } from "antd";
>>>>>>> 52bab79ac4c19363bdc3cea85271878fb7aac695
import axios from "axios";
import api from "../../config/axios";
import FormItem from "antd/es/form/FormItem";
import moment from "moment";
import { useForm } from "antd/es/form/Form";
import "./index.scss";
import { toast } from "react-toastify";
import { useSelector } from "react-redux";
import { selectUser } from "../../redux/features/counterSlice";
import useGetParams from "../../assets/utils/useGetParams";
Modal.setAppElement("#root");

const UserProfile = () => {
  // Giả lập dữ liệu người dùng và ví
  const [user, setUser] = useState({
    name: [],
    balance: 0,
    transactions: []
  });
  const userRedux = useSelector(selectUser);
<<<<<<< HEAD

  const getParams = useGetParams();
  const id = getParams("id");
  const status = getParams("vnp_ResponseCode");
  async function rechage() {
    try {
      const response = await api.post(`/wallet/recharge/${id}`);
      toast.success("Nạp tiền thành công");
      fetchBalance();
      fetchTransactionHistory();
    } catch (error) {
      // toast.error(error.response.data);
      console.error("Lỗi khi nạp tiền: ", error);
      toast.error("Nạp tiền thất bại");
    }
  }
  useEffect(() => {
    if (status == "00") {
      rechage();
    }
  }, []);
=======
>>>>>>> 52bab79ac4c19363bdc3cea85271878fb7aac695

  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(user.name);
  const [email, setEmail] = useState(user.email);
  const [transactions, setTransaction] = useState([]);
  const [noTransactions, setNoTransactions] = useState(false);
  const [loading, setLoading] = useState(true);
  const [isWithdrawModalOpen, setIsWithdrawModalOpen] = useState(false);
  const [isDepositModalOpen, setIsDepositModalOpen] = useState(false);
  const [withdrawInfo, setWithdrawInfo] = useState({
    accountNumber: "",
    accountName: "",
    bankName: "",
    amount: 0,
  });
  const [depositInfo, setDepositInfo] = useState({
    amount: 0,
  });
  const [activeTab, setActiveTab] = useState("profile");

  const [venueId, setVenueID] = useState(0);
  const handleSave = () => {
    setIsEditing(false);
    // Update user information logic here
    console.log("Updated user info:", { name, email });
  };

  const handleWithdrawChange = (e) => {
    const { name, value } = e.target;
    setWithdrawInfo((prevInfo) => ({ ...prevInfo, [name]: value }));
  };

  const [form] = useForm();

  const [court, setCourt] = useState([]);

  const [slotDaily, setSlotDaily] = useState([]);

  const [selectedDate, setSelectedDate] = useState("");

  const fetchSlotDaily = async () => {
    try {
      const response = await api.get(
        `/timeslots/available-slots?courtId=${courtSelect}&date=${selectedDate}&venueId=${venueId}`
      );
      setSlotDaily(response.data);
    } catch (error) {
      console.log(error);
    }
  };
<<<<<<< HEAD
=======

>>>>>>> 52bab79ac4c19363bdc3cea85271878fb7aac695
  const [courtSelect, setCourtSelect] = useState([]);
  const [data, setData] = useState([]);

  const fetch = async () => {
    try {
      const response = await api.get("/booking/booking-history");
      setData(response.data);
    } catch (error) {
      console.log(error);
    }
  };
<<<<<<< HEAD

  const fetchTransactionHistory = async () => {
    setLoading(true);
    try {
      const response = await api.get("/transaction/transactionById");
      setTransaction(response.data);
      setNoTransactions(response.data.length === 0);

    } catch (error) {
      console.error("Error fetching transactions: ", error);
    } finally {
      setLoading(false);
    }
  };
=======
>>>>>>> 52bab79ac4c19363bdc3cea85271878fb7aac695

  const fetchCourt = async () => {
    try {
      const response = await api.get(`/venues/${venueId}/courts`);
      setCourt(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getLabelSLot = (arrID = []) => {
    let listSlotName = [];
    console.log(slotDaily);
    arrID?.forEach((e) => {
      let filterSlot = slotDaily.filter((item) => item.id === e);
      if (filterSlot.length > 0) {
        console.log(filterSlot);
        listSlotName.push(
          filterSlot[0].startTime + " - " + filterSlot[0].endTime
        );
      } else {
      }
    });
    return listSlotName;
  };

  const getLableCourt = (e) => court.filter((item) => item.id == e);

  useEffect(() => {
    fetchCourt();
  }, [venueId]);

  useEffect(() => {
    fetch();
  }, []);
  useEffect(() => {
    fetchSlotDaily();
  }, [selectedDate, courtSelect]);

  const handleWithdrawSubmit = () => {
    setIsWithdrawModalOpen(false);
    // Handle withdraw logic here
    console.log("Withdraw info:", withdrawInfo);
  };

  const handleDepositChange = (e) => {
    const { name, value } = e.target;
    setDepositInfo((prevInfo) => ({ ...prevInfo, [name]: value }));
  };

  const [listCheck, setListCheck] = useState([]);
  const [listSubmit, setListSubmit] = useState([]);
  const [bookingID, setBookingID] = useState(0);

  const handleCreate = (e) => {
    try {
      const checkInDate = moment(e.checkInDate.$d).format("YYYY-MM-DD");
      e.checkInDate = checkInDate;
      const listSlot = getLabelSLot(e.timeslot);
      const courtName = getLableCourt(e.court);
      const newList = {
        checkInDate: e.checkInDate,
        court: courtName[0].courtName,
        timeslot: listSlot,
      };

      setListCheck([...listCheck, newList]);
      setListSubmit([
        ...listSubmit,
        {
          checkInDate: e.checkInDate,
          court: e.court,
          timeslot: e.timeslot,
        },
      ]);
      form.resetFields();
    } catch (error) {
      toast.error(error?.response.data);
    }
  };

  const handleBooking = async () => {
    try {
      if (listCheck.length > 0) {
        const response = await api.post("/booking/flexible", {
          bookingId: bookingID,
          flexibleTimeSlots: listSubmit,
        });
        toast.success("Booking successfully!!!");
        setOpen(false);
        setListCheck([]);
        form.resetFields();
        fetch();
      } else {
        toast.error("Vui lòng thêm trước khi tạo lịch");
      }
    } catch (error) {
      toast.error(error.response.data);
    }
  };

  const handleDepositSubmit = async () => {
    try {
      const token = localStorage.getItem("token");

      const newTransaction = {
        invoice: Date.now().toString(),
        transferTo: "VNPAY",
        date: new Date().toLocaleDateString(),
        tags: "Deposit",
        amount: parseFloat(depositInfo.amount),
      };

      // Cập nhật user balance và transactions
      setUser((prevUser) => ({
        ...prevUser,
        balance: prevUser.balance + parseFloat(depositInfo.amount),
        transactions: [...prevUser.transactions, newTransaction],
      }));

      // Gọi API để yêu cầu nạp tiền
      const response = await api.post(
        "/wallet/recharge-vnpay-url",
        {
          amount: depositInfo.amount.toString(),
          description: depositInfo.description,
          transitionDate: new Date().toISOString(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Kiểm tra và điều hướng người dùng đến URL của VNPAY
      if (response.data) {
        console.log("Im here");
        window.location.href = response.data.trim();
      } else {
        console.error("URL not found in response");
      }
    } catch (error) {
      console.error("Error during deposit request:", error);
    }

    setIsDepositModalOpen(false);
  };

  const [open, setOpen] = useState(false);

  const handleCancelOrder = (orderId) => {
    setUser((prevUser) => {
      const updatedOrders = prevUser.orders.map((order) =>
        order.id === orderId ? { ...order, status: "Cancelled" } : order
      );
      return { ...prevUser, orders: updatedOrders };
    });
  };

  const [balance, setBalance] = useState([]);

  const fetchBalance = async () => {
    try {
      const response = await api.get("/wallet/balance");
      setBalance(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchBalance();
  }, []);

  const [booked, setBooked] = useState([]);

  const fetchBooked = async () => {
    try {
      const response = await api.get("/booking/get-booked-slot");
      console.log(response.data);
      setBooked(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchBooked();
  }, []);

  const formatMoneyVND = (amount) => {
    if (isNaN(amount)) {
      return "Invalid amount";
    }

    return amount.toLocaleString("vi-VN", {
      style: "currency",
      currency: "VND",
    });
  };
  const renderOrders = () => {
    return (
      <div className="overflow-x-auto mt-8">
        <h2 className="text-2xl font-bold mb-4">Đặt lịch</h2>
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                STT
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Ngày tạo
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Ngày áp dụng
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Trạng thái
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Tổng tiền
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Tổng thời gian
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Thời gian còn lại
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Sân
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Loại lịch
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Hành động
              </th>
            </tr>
          </thead>
          <tbody>
            {data?.map((order, index) => {
              // Render rows only if bookingType is "FLEXIBLE"
              if (
                order.bookingType === "FLEXIBLE" &&
                order.remainingTimes > 0
              ) {
                return (
                  <tr key={order.id}>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {index + 1}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.bookingDate}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.applicationDate}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      <span
                        className={`px-2 py-1 rounded-full ${
                          order.status === "Completed"
                            ? "bg-green-200 text-green-800"
                            : order.status === "Pending"
                            ? "bg-yellow-200 text-yellow-800"
                            : "bg-red-200 text-red-800"
                        }`}
                      >
                        {order.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {formatMoneyVND(order.totalPrice)}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.totalTimes}h
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.remainingTimes}h
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.venueName}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.bookingType}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.status === "Pending" && (
                        <button
                          className="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
                          onClick={() => handleCancelOrder(order.id)}
                        >
                          Hủy đơn
                        </button>
                      )}
                      {/* Render the button only if bookingType is "FLEXIBLE" and remainingTimes > 0 */}
                      {order.bookingType === "FLEXIBLE" &&
                        order.remainingTimes > 0 && (
                          <button
                            className="bg-green-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
                            onClick={() => {
                              setBookingID(order?.id);
                              setVenueID(order?.venueId);
                              setOpen(true);
                            }}
                          >
                            Đặt lịch
                          </button>
                        )}
                    </td>
                  </tr>
                );
              } else {
                return null;
              }
            })}
          </tbody>
        </table>

        <ModalANTD
          onOk={handleBooking}
          onCancel={() => {
            setOpen(false);
            setListCheck([]);
            form.resetFields();
          }}
          open={open}
        >
          <Form onFinish={(e) => handleCreate(e)} form={form}>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn ngày bắt đầu:"
              name={"checkInDate"}
            >
              <DatePicker
                onChange={(e) =>
                  setSelectedDate(moment(e?.$d).format("YYYY-MM-DD"))
                }
              />
            </FormItem>
            <FormItem labelCol={{ span: "24" }} label="Chọn sân" name={"court"}>
              <Select
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={court?.map((item) => ({
                  value: item.id,
                  label: item.courtName,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn slot"
              name={"timeslot"}
            >
              <Select
                mode="tags"
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={slotDaily?.map((item) => ({
                  value: item.id,
                  label: `${item.startTime} - ${item.endTime}`,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <Button
              htmlType="submit"
              style={{
                marginBottom: "20px",
                background: "green",
              }}
              type="primary"
            >
              Thêm
            </Button>

            {listCheck.length > 0 && (
              <Button
                onClick={() => setListCheck([])}
                htmlType="submit"
                style={{
                  marginBottom: "20px",
                  marginLeft: "10px",
                }}
                type=""
              >
                Reset
              </Button>
            )}
          </Form>
          {listCheck.length > 0 &&
            listCheck?.map((e, slot) => (
              <div className="listCheck" style={{ marginBottom: "15px  0px" }}>
                <h3
                  style={{
                    display: "inline",
                    fontWeight: 500,
                  }}
                >
                  {" "}
                  Lịch{" "}
                </h3>{" "}
                {slot + 1}
                <br></br>
                <h4>Ngày bắt đầu : </h4> {e?.checkInDate}
                <br></br>
                <h4>Sân : </h4> {e?.court}
                <br></br>
                <h4>{"Thời gian: "}</h4>
                {e.timeslot.map((timeslot, index) => (
                  <React.Fragment key={index}>
                    {timeslot}
                    {index !== e.timeslot.length - 1 && ", "}
                  </React.Fragment>
                ))}
              </div>
            ))}
        </ModalANTD>
      </div>
    );
  };
  const renderHistoryOrder = () => {
    return (
      <div className="overflow-x-auto mt-8">
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                STT
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Ngày tạo
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Ngày áp dụng
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Trạng thái
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Tổng tiền
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Tổng thời gian
              </th>

              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Sân
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Loại lịch
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Hành động
              </th>
            </tr>
          </thead>
          <tbody>
            {data?.map((order, index) => (
              <tr key={order.id}>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {index + 1}
                </td>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {order.bookingDate}
                </td>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {order.applicationDate}
                </td>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  <span
                    className={`px-2 py-1 rounded-full ${
                      order.status === "Completed"
                        ? "bg-green-200 text-green-800"
                        : order.status === "Pending"
                        ? "bg-yellow-200 text-yellow-800"
                        : "bg-red-200 text-red-800"
                    }`}
                  >
                    {order.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {formatMoneyVND(order.totalPrice)}
                </td>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {order.totalTimes}h
                </td>

                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {order.venueName}
                </td>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {order.bookingType}
                </td>
                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                  {order.status === "Pending" && (
                    <button
                      className="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
                      onClick={() => handleCancelOrder(order.id)}
                    >
                      Hủy đơn
                    </button>
                  )}
                  {
                    <button
                      className="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
<<<<<<< HEAD
                      onClick={() => { }}
=======
                      onClick={() => {}}
>>>>>>> 52bab79ac4c19363bdc3cea85271878fb7aac695
                    >
                      Hủy lịch
                    </button>
                  }
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <ModalANTD
          onOk={handleBooking}
          onCancel={() => {
            setOpen(false);
            setListCheck([]);
            form.resetFields();
          }}
          open={open}
        >
          <Form onFinish={(e) => handleCreate(e)} form={form}>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn ngày bắt đầu:"
              name={"checkInDate"}
            >
              <DatePicker
                onChange={(e) =>
                  setSelectedDate(moment(e?.$d).format("YYYY-MM-DD"))
                }
              />
            </FormItem>
            <FormItem labelCol={{ span: "24" }} label="Chọn sân" name={"court"}>
              <Select
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={court?.map((item) => ({
                  value: item.id,
                  label: item.courtName,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn slot"
              name={"timeslot"}
            >
              <Select
                mode="tags"
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={slotDaily?.map((item) => ({
                  value: item.id,
                  label: `${item.startTime} - ${item.endTime}`,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <Button
              htmlType="submit"
              style={{
                marginBottom: "20px",
                background: "green",
              }}
              type="primary"
            >
              Thêm
            </Button>

            {listCheck.length > 0 && (
              <Button
                onClick={() => setListCheck([])}
                htmlType="submit"
                style={{
                  marginBottom: "20px",
                  marginLeft: "10px",
                }}
                type=""
              >
                Reset
              </Button>
            )}
          </Form>
          {listCheck.length > 0 &&
            listCheck?.map((e, slot) => (
              <div className="listCheck" style={{ marginBottom: "15px  0px" }}>
                <h3
                  style={{
                    display: "inline",
                    fontWeight: 500,
                  }}
                >
                  {" "}
                  Lịch{" "}
                </h3>{" "}
                {slot + 1}
                <br></br>
                <h4>Ngày bắt đầu : </h4> {e?.checkInDate}
                <br></br>
                <h4>Sân : </h4> {e?.court}
                <br></br>
                <h4>{"Thời gian: "}</h4>
                {e.timeslot.map((timeslot, index) => (
                  <React.Fragment key={index}>
                    {timeslot}
                    {index !== e.timeslot.length - 1 && ", "}
                  </React.Fragment>
                ))}
              </div>
            ))}
        </ModalANTD>
      </div>
<<<<<<< HEAD
=======
    );
  };
  const renderBooked = () => {
    return (
      <div className="overflow-x-auto mt-8">
        <h2 className="text-2xl font-bold mb-4">Orders</h2>
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Order ID
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Created Date
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Application Date
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Status
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Amount
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Total Times
              </th>

              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Sân
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Booking Type
              </th>
              {/* <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Actions
              </th> */}
            </tr>
          </thead>
          <tbody>
            {booked?.map(
              (order) =>
                order.status == "BOOKED" && (
                  <tr key={order.id}>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.id}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.bookingDate}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.applicationDate}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      <span
                        className={`px-2 py-1 rounded-full ${
                          order.status === "Completed"
                            ? "bg-green-200 text-green-800"
                            : order.status === "Pending"
                            ? "bg-yellow-200 text-yellow-800"
                            : "bg-red-200 text-red-800"
                        }`}
                      >
                        {order.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.totalPrice}$
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.totalTimes}h
                    </td>

                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.venueName}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.bookingType}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.status === "Pending" && (
                        <button
                          className="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
                          onClick={() => handleCancelOrder(order.id)}
                        >
                          Hủy đơn
                        </button>
                      )}
                      {/* {
                    <button
                      className="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
                      onClick={() => {}}
                    >
                      Hủy lịch
                    </button>
                  } */}
                    </td>
                  </tr>
                )
            )}
          </tbody>
        </table>
        <ModalANTD
          onOk={handleBooking}
          onCancel={() => {
            setOpen(false);
            setListCheck([]);
            form.resetFields();
          }}
          open={open}
        >
          <Form onFinish={(e) => handleCreate(e)} form={form}>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn ngày bắt đầu:"
              name={"checkInDate"}
            >
              <DatePicker
                onChange={(e) =>
                  setSelectedDate(moment(e?.$d).format("YYYY-MM-DD"))
                }
              />
            </FormItem>
            <FormItem labelCol={{ span: "24" }} label="Chọn sân" name={"court"}>
              <Select
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={court?.map((item) => ({
                  value: item.id,
                  label: item.courtName,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn slot"
              name={"timeslot"}
            >
              <Select
                mode="tags"
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={slotDaily?.map((item) => ({
                  value: item.id,
                  label: `${item.startTime} - ${item.endTime}`,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <Button
              htmlType="submit"
              style={{
                marginBottom: "20px",
                background: "green",
              }}
              type="primary"
            >
              Thêm
            </Button>

            {listCheck.length > 0 && (
              <Button
                onClick={() => setListCheck([])}
                htmlType="submit"
                style={{
                  marginBottom: "20px",
                  marginLeft: "10px",
                }}
                type=""
              >
                Reset
              </Button>
            )}
          </Form>
          {listCheck.length > 0 &&
            listCheck?.map((e, slot) => (
              <div className="listCheck" style={{ marginBottom: "15px  0px" }}>
                <h3
                  style={{
                    display: "inline",
                    fontWeight: 500,
                  }}
                >
                  {" "}
                  Lịch{" "}
                </h3>{" "}
                {slot + 1}
                <br></br>
                <h4>Ngày bắt đầu : </h4> {e?.checkInDate}
                <br></br>
                <h4>Sân : </h4> {e?.court}
                <br></br>
                <h4>{"Thời gian: "}</h4>
                {e.timeslot.map((timeslot, index) => (
                  <React.Fragment key={index}>
                    {timeslot}
                    {index !== e.timeslot.length - 1 && ", "}
                  </React.Fragment>
                ))}
              </div>
            ))}
        </ModalANTD>
      </div>
>>>>>>> 52bab79ac4c19363bdc3cea85271878fb7aac695
    );
  };
  const renderBooked = () => {
    return (
      <div className="overflow-x-auto mt-8">
        {/* <h2 className="text-2xl font-bold mb-4">Orders</h2> */}
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                STT
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Ngày tạo
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Ngày áp dụng
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Trạng thái
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Tổng tiền
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Tổng thời gian
              </th>

              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Sân
              </th>
              <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Loại lịch
              </th>
              {/* <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                Actions
              </th> */}
            </tr>
          </thead>
          <tbody>
            {booked?.map(
              (order, index) =>
                order.status == "BOOKED" && (
                  <tr key={order.id}>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {index + 1}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.bookingDate}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.applicationDate}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      <span
                        className={`px-2 py-1 rounded-full ${order.status === "Completed"
                          ? "bg-green-200 text-green-800"
                          : order.status === "Pending"
                            ? "bg-yellow-200 text-yellow-800"
                            : "bg-red-200 text-red-800"
                          }`}
                      >
                        {order.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {formatMoneyVND(order.totalPrice)}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.totalTimes}h
                    </td>

                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.venueName}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.bookingType}
                    </td>
                    <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                      {order.status === "Pending" && (
                        <button
                          className="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
                          onClick={() => handleCancelOrder(order.id)}
                        >
                          Hủy đơn
                        </button>
                      )}
                      {/* {
                    <button
                      className="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600"
                      onClick={() => {}}
                    >
                      Hủy lịch
                    </button>
                  } */}
                    </td>
                  </tr>
                )
            )}
          </tbody>
        </table>
        <ModalANTD
          onOk={handleBooking}
          onCancel={() => {
            setOpen(false);
            setListCheck([]);
            form.resetFields();
          }}
          open={open}
        >
          <Form onFinish={(e) => handleCreate(e)} form={form}>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn ngày bắt đầu:"
              name={"checkInDate"}
            >
              <DatePicker
                onChange={(e) =>
                  setSelectedDate(moment(e?.$d).format("YYYY-MM-DD"))
                }
              />
            </FormItem>
            <FormItem labelCol={{ span: "24" }} label="Chọn sân" name={"court"}>
              <Select
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={court?.map((item) => ({
                  value: item.id,
                  label: item.courtName,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <FormItem
              labelCol={{ span: "24" }}
              label="Chọn slot"
              name={"timeslot"}
            >
              <Select
                mode="tags"
                value={courtSelect}
                onChange={(e) => setCourtSelect(e)}
                options={slotDaily?.map((item) => ({
                  value: item.id,
                  label: `${item.startTime} - ${item.endTime}`,
                  disabled: item.status == "INACTIVE",
                }))}
              />
            </FormItem>
            <Button
              htmlType="submit"
              style={{
                marginBottom: "20px",
                background: "green",
              }}
              type="primary"
            >
              Thêm
            </Button>

            {listCheck.length > 0 && (
              <Button
                onClick={() => setListCheck([])}
                htmlType="submit"
                style={{
                  marginBottom: "20px",
                  marginLeft: "10px",
                }}
                type=""
              >
                Reset
              </Button>
            )}
          </Form>
          {listCheck.length > 0 &&
            listCheck?.map((e, slot) => (
              <div className="listCheck" style={{ marginBottom: "15px  0px" }}>
                <h3
                  style={{
                    display: "inline",
                    fontWeight: 500,
                  }}
                >
                  {" "}
                  Lịch{" "}
                </h3>{" "}
                {slot + 1}
                <br></br>
                <h4>Ngày bắt đầu : </h4> {e?.checkInDate}
                <br></br>
                <h4>Sân : </h4> {e?.court}
                <br></br>
                <h4>{"Thời gian: "}</h4>
                {e.timeslot.map((timeslot, index) => (
                  <React.Fragment key={index}>
                    {timeslot}
                    {index !== e.timeslot.length - 1 && ", "}
                  </React.Fragment>
                ))}
              </div>
            ))}
        </ModalANTD>
      </div>
    );
  };

  useEffect(() => {

    fetchTransactionHistory();
  }, []);

  const renderProfile = () => {
    return (
      <div>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-2xl font-bold mb-4">Hồ sơ cá nhân</h2>
          <div className="flex items-center">
            <img
              src="https://w7.pngwing.com/pngs/340/946/png-transparent-avatar-user-computer-icons-software-developer-avatar-child-face-heroes-thumbnail.png"
              alt="Avatar"
              className="rounded-full h-24 w-24 mr-4"
            />
            <button
              className="bg-blue-500 text-white px-4 py-2 rounded-full hover:bg-blue-600"
              onClick={() => setIsEditing(true)}
            >
              Chỉnh sửa hồ sơ
            </button>
          </div>
          {isEditing ? (
            <div className="mt-4">
              <label className="block mb-2">Họ tên:</label>
              <input
                type="text"
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              <label className="block mt-4 mb-2">Email:</label>
              <input
                type="email"
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <button
                className="mt-4 bg-green-500 text-white px-4 py-2 rounded-full hover:bg-green-600"
                onClick={handleSave}
              >
                Lưu
              </button>
            </div>
          ) : (
            <div className="mt-4">
              <p>
                <strong>Họ tên:</strong> {userRedux?.fullName}
              </p>
              <p>
                <strong>Email:</strong> {userRedux?.email}
              </p>
            </div>
          )}
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md mt-8">
          <h2 className="text-2xl font-bold mb-4">Ví tiền</h2>
          <div className="flex items-center justify-between">
            <p className="text-lg">
              Số dư hiện tại: <strong>{formatMoneyVND(balance)}</strong>
            </p>
            <div>
              <button
                className="bg-green-500 text-white px-4 py-2 rounded-full hover:bg-green-600 mr-2"
                onClick={() => setIsDepositModalOpen(true)}
              >
                Nạp thêm tiền qua VNPay
              </button>
              <button
                className="bg-black text-white px-4 py-2 rounded-full hover:bg-gray-800"
                onClick={() => setIsWithdrawModalOpen(true)}
              >
                Rút tiền
              </button>
            </div>
          </div>
          <div className="overflow-x-auto mt-4">
            {loading ? (
              <div className="flex justify-center items-center h-48">
                <Spin size="large" />
              </div>
            ) : noTransactions ? (
              <p>Không có dữ liệu giao dịch</p>
            ) : (
              <table className="min-w-full bg-white">
                <thead>
                  <tr>
                    <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                      Số hóa đơn
                    </th>
                    <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                      Loại giao dịch
                    </th>
                    <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                      Ngày
                    </th>
                    <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                      Mô tả
                    </th>
                    <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
                      Tổng tiền
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.map((transaction, index) => {
                    const formattedTransactionDate = moment(transaction.transactionDate).format('YYYY-MM-DD-HH-mm-ss');
                    return (
                      <tr key={transaction.id || index}>
                        <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                          {index + 1}
                        </td>
                        <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                          {transaction.transactionType}
                        </td>
                        <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                          {formattedTransactionDate || "NA"}
                        </td>
                        <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                          {transaction.description}
                        </td>
                        <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-500">
                          {formatMoneyVND(transaction.amount)}
                        </td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    );
  };

  return (
    <div className="container mx-auto my-8">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <nav className="flex space-x-4">
          <button
            className={`px-4 py-2 rounded-full ${
              activeTab === "profile"
                ? "bg-blue-500 text-white"
                : "bg-gray-200 text-gray-800"
            }`}
            onClick={() => setActiveTab("profile")}
          >
            Hồ sơ
          </button>
          <button
            className={`px-4 py-2 rounded-full ${
              activeTab === "orders"
                ? "bg-blue-500 text-white"
                : "bg-gray-200 text-gray-800"
            }`}
            onClick={() => setActiveTab("orders")}
          >
            Đặt lịch
          </button>
          <button
            className={`px-4 py-2 rounded-full ${
              activeTab === "booked"
                ? "bg-blue-500 text-white"
                : "bg-gray-200 text-gray-800"
            }`}
            onClick={() => setActiveTab("booked")}
          >
            Lịch đã đặt
          </button>
          <button
            className={`px-4 py-2 rounded-full ${
              activeTab === "history"
                ? "bg-blue-500 text-white"
                : "bg-gray-200 text-gray-800"
            }`}
            onClick={() => setActiveTab("history")}
          >
            Lịch sử đơn hàng
          </button>
        </nav>
        {activeTab === "profile" && renderProfile()}
        {activeTab === "orders" && renderOrders()}
        {activeTab === "history" && renderHistoryOrder()}
        {activeTab === "booked" && renderBooked()}
      </div>

      <Modal
        isOpen={isWithdrawModalOpen}
        onRequestClose={() => setIsWithdrawModalOpen(false)}
        contentLabel="Withdrawal Form"
        className="bg-white p-6 rounded-lg shadow-lg max-w-md mx-auto mt-20"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
        <h2 className="text-2xl font-bold mb-4">WITHDRAWAL FORM</h2>
        <label className="block mb-2">Account Number</label>
        <input
          type="text"
          name="accountNumber"
          className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
          value={withdrawInfo.accountNumber}
          onChange={handleWithdrawChange}
        />
        <label className="block mt-4 mb-2">Account Name</label>
        <input
          type="text"
          name="accountName"
          className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
          value={withdrawInfo.accountName}
          onChange={handleWithdrawChange}
        />
        <label className="block mt-4 mb-2">Bank Name</label>
        <input
          type="text"
          name="bankName"
          className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
          value={withdrawInfo.bankName}
          onChange={handleWithdrawChange}
        />
        <label className="block mt-4 mb-2">Amount</label>
        <input
          type="number"
          name="amount"
          className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
          value={withdrawInfo.amount}
          onChange={handleWithdrawChange}
        />
        <button
          className="mt-4 bg-blue-500 text-white px-4 py-2 rounded-full hover:bg-blue-600"
          onClick={handleWithdrawSubmit}
        >
          Submit
        </button>
      </Modal>

      <Modal
        isOpen={isDepositModalOpen}
        onRequestClose={() => setIsDepositModalOpen(false)}
        contentLabel="Deposit Form"
        className="bg-white p-6 rounded-lg shadow-lg max-w-md mx-auto mt-20"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
        <h2 className="text-2xl font-bold mb-4">DEPOSIT FORM</h2>
        <label className="block mb-2">Amount</label>
        <input
          type="number"
          name="amount"
          className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
          value={depositInfo.amount}
          onChange={handleDepositChange}
        />
        <button
          className="mt-4 bg-blue-500 text-white px-4 py-2 rounded-full hover:bg-blue-600"
          onClick={handleDepositSubmit}
        >
          Submit
        </button>
      </Modal>
    </div>
  );
};

export default UserProfile;
