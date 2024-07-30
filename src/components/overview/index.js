import { Button, Form, InputNumber } from "antd";
import FormItem from "antd/es/form/FormItem";
import React, { useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import api from "../../config/axios";

function Overview() {
  // const data = [
  //   {
  //     name: "Sân 1",
  //     fixed: 4000,
  //     daily: 2400,
  //     flexible: 1000,
  //   },
  //   {
  //     name: "Sân 2",
  //     fixed: 3000,
  //     daily: 1398,
  //     flexible: 1000,
  //   },
  //   {
  //     name: "Sân 3",
  //     fixed: 2000,
  //     daily: 9800,
  //     flexible: 1000,
  //   },
  //   {
  //     name: "Sân 4",
  //     fixed: 2780,
  //     daily: 3908,
  //     flexible: 1000,
  //   },
  // ];

  const [data, setData] = useState([]);

  const dataWithTotal = data.map((item) => ({
    ...item,
    total: item.daily + item.fixed + item.flexible,
  }));

  const onFinish = async (values) => {
    console.log(values);
    try {
      const response = await api.get(
        `/booking/revenue?month=${values.month}&year=${values.year}`
      );
      setData(response.data);
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div
      style={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        flexDirection: "column",
      }}
    >
      <Form layout="vertical" onFinish={onFinish}>
        <Form.Item
          name={"month"}
          label="Nhập tháng"
          style={{ display: "flex", alignItems: "center" }}
        //   labelCol={{ span: 8 }}
        //   wrapperCol={{ span: 16 }}
        >
          <InputNumber style={{ width: "100%" }} />
        </Form.Item>
        <Form.Item
          name={"year"}
          label="Nhập năm"
          style={{ display: "flex", alignItems: "center" }}
        //   labelCol={{ span: 8 }}
        //   wrapperCol={{ span: 16 }}
        >
          <InputNumber style={{ width: "100%" }} />
        </Form.Item>
        <Button htmlType="submit" className="mb-3">
          Xem doanh thu
        </Button>
      </Form>

      {data.length > 0 && (
        <BarChart width={1200} height={450} data={dataWithTotal}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="daily" stackId="a" fill="#8884d8" />
          <Bar dataKey="fixed" stackId="a" fill="#82ca9d" />
          <Bar dataKey="flexible" stackId="a" fill="#ffc658" />
          <Bar dataKey="total" style={{ display: "none" }} fill="#ff8042" />
        </BarChart>
      )}
    </div>
  );
}

export default Overview;