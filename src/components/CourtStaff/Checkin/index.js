import React, { useState, useEffect } from "react";
import { Table, Input, Space, Button, Row, message, Col, Typography } from "antd";
import moment from "moment";
import api from "../../../config/axios";
import { useSelector, useDispatch } from "react-redux";
import { logout, selectUser } from "../../../redux/features/counterSlice";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import Navbar from "../../NavBar/navbar";

const { Search } = Input;
const { Title } = Typography;

const CourtStaffCheckin = () => {
  const [data, setData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const user = useSelector(selectUser);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate('/login');
    }
  }, [user, navigate]);

  const fetch = async () => {
    try {
      setIsLoading(true);
      const response = await api.get("/court-time-slot/booked-or-checked");
      setData(response.data);
      setFilteredData(response.data);
      setIsLoading(false);
    } catch (error) {
      console.error(error);
      message.error("Lỗi tải dữ liệu, vui lòng thử lại sau");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  const handleSearch = (value) => {
    setSearchTerm(value);
    setFilteredData(data.filter((item) => {
      return (
        item.phoneNumber.includes(value) ||
        item.booker.toLowerCase().includes(value.toLowerCase()) ||
        moment(item.date).format("YYYY-MM-DD").includes(value)
      );
    }));
  };

  const formatMoneyVND = (amount) => {
    if (isNaN(amount)) {
      return "Invalid amount";
    }

    return amount.toLocaleString("vi-VN", {
      style: "currency",
      currency: "VND",
    });
  };

  const handleCheckIn = async (courtTimeSlotId) => {
    setIsLoading(true);
    try {
      const response = await api.patch(
        `/court-time-slot/checkin/${courtTimeSlotId}`
      );
      message.success("Check-in thành công!");
      fetch(); // Refresh the data after checking in
    } catch (error) {
      console.error(error);
      message.error("Lỗi khi check-in.");
    } finally {
      setIsLoading(false);
    }
  };

  const columns = [
    {
      title: "STT",
      dataIndex: "index",
      key: "index",
      render: (text, record, index) => index + 1,
    },
    {
      title: "Ngày",
      dataIndex: "date",
      key: "date",
      render: (text) => moment(text).format("YYYY-MM-DD"),
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      render: (text) => (
        <span
          className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${text === "BOOKED"
            ? "bg-yellow-100 text-yellow-800"
            : text === "CHECKED"
              ? "bg-green-100 text-green-800"
              : "bg-red-100 text-red-800"
            }`}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Tên sân",
      dataIndex: "courtName",
      key: "courtName",
    },
    {
      title: "Giá tiền",
      dataIndex: "price",
      key: "price",
      render: (text) => formatMoneyVND(text),
    },
    {
      title: "Khung giờ",
      dataIndex: "startTime",
      key: "startTime",
      render: (text, record) => `${text} - ${record.endTime}`,
    },
    {
      title: "Tên khách hàng",
      dataIndex: "booker",
      key: "booker",
    },
    {
      title: "Hành động",
      key: "action",
      render: (text, record) => (
        <Space size="middle">
          <Button type="primary" onClick={() => handleCheckIn(record.courtTimeSlotId)}>
            Check In
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Navbar />
      <div className="bg-white p-4">
        <Row>
          <Col span={16}>
            <h2 className="text-2xl font-bold mb-4">Quản lý Check-in Sân cầu lông</h2>
          </Col>
        </Row>

        <div className="mb-4">
          <Search
            placeholder="Nhập số điện thoại hoặc tên khách hàng"
            allowClear
            onSearch={handleSearch}
            className="w-full"
            style={{ width: 300, marginBottom: 20 }}
          />
        </div>
        <Table
          columns={columns}
          dataSource={filteredData}
          rowKey="courtTimeSlotId"
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
          }}
          loading={isLoading}
        />
      </div>
    </>
  );
};

export default CourtStaffCheckin;