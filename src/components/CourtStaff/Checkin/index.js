import { Button, Table, message, Input } from "antd";
import React, { useEffect, useState } from "react";
import api from "../../../config/axios";
import moment from "moment";

const { Search } = Input;

function CheckIn() {
  const [data, setData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);

  const fetch = async () => {
    try {
      const response = await api.get("/booking/get-booked-slot");
      console.log(response.data);
      setData(response.data);
      setFilteredData(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  const handleCheckIn = async (bookingId) => {
    const checkInDate = moment().format("YYYY-MM-DD");
    try {
      const response = await api.patch(
        `/booking/bookingId/${bookingId}/checkInDate/${checkInDate}`
      );
      message.success("Checked in successfully!");
      fetch(); // Refresh the data after checking in
    } catch (error) {
      console.error(error);
      message.error("Failed to check in.");
    }
  };

  const columns = [
    {
      title: "Ngày lên sân",
      dataIndex: "applicationDate",
      key: "applicationDate",
    },
    {
      title: "Tên sân",
      dataIndex: "venueName",
      key: "venueName",
    },
    {
      title: "Tổng giá tiền",
      dataIndex: "totalPrice",
      key: "totalPrice",
    },
    {
      title: "Loại lịch",
      dataIndex: "bookingType",
      key: "bookingType",
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
    },
    {
      title: "Tên khách hàng",
      dataIndex: "accountName",
      key: "accountName",
    },
    {
      title: "Số điện thoại",
      dataIndex: "accountName",
      key: "accountName",
    },
    {
      title: "Action",
      key: "action",
      render: (text, record) => (
        <Button type="primary" onClick={() => handleCheckIn(record.id)}>
          Check In
        </Button>
      ),
    },
  ];

  const onSearch = async (value) => {
    try {
      const response = await api.get(
        `/account/${value}/find-account-by-identifier`
      );
      const accountName = response.data.accountName;
      const filteredBookings = data.filter(
        (booking) => booking.accountName === accountName
      );
      setFilteredData(filteredBookings);
    } catch (error) {
      console.error(error);
      message.error("Failed to fetch account.");
    }
  };

  return (
    <div>
      <Search
        placeholder="Nhập số điện thoại khách hàng"
        onSearch={onSearch}
        style={{ width: 300, marginBottom: 20 }}
      />
      <Table dataSource={filteredData} columns={columns} rowKey="id" />
    </div>
  );
}

export default CheckIn;
