import React, { useState, useEffect } from "react";
import api from "../../../config/axios";
import { Table, Spin, Alert, Modal, Form, Input, Button, Select, notification } from 'antd';
import { useSelector } from "react-redux";
import { selectUser } from "../../../redux/features/counterSlice";
import { toast } from "react-toastify";

const RegisterBookingType = () => {
  const [venueId, setVenueId] = useState(null);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [detailsVisible, setDetailsVisible] = useState(false);
  const [detailsData, setDetailsData] = useState(null);
  const [timeSlotsWithoutPricing, setTimeSlotsWithoutPricing] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [modalType, setModalType] = useState(null); // 'pricing' or 'timeSlot'
  const [currentRecord, setCurrentRecord] = useState(null);
  const [selectedBookingType, setSelectedBookingType] = useState(null);
  const [isCreatePricingModalVisible, setIsCreatePricingModalVisible] = useState(false);
  const [isUpdatePricingModalVisible, setIsUpdatePricingModalVisible] = useState(false);
  const [isCreateTimeSlotModalVisible, setIsCreateTimeSlotModalVisible] = useState(false);
  const [isUpdateTimeSlotModalVisible, setIsUpdateTimeSlotModalVisible] = useState(false);

  const [form] = Form.useForm();
  const token = localStorage.getItem("token");
  const user = useSelector(selectUser);



  const fetchVenueId = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/venues/manager/${user.id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setVenueId(response.data.id);
    } catch (error) {
      console.error("Error fetching venue ID:", error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchData = async () => {
    if (!venueId) return;
    setLoading(true);
    try {
      const response = await api.get(`/pricing/${venueId}/pricing-and-schedule`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setData(response.data);
    } catch (error) {
      console.error("Error fetching venue data:", error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchTimeSlotWithoutPricing = async () => {
    if (!venueId) return;
    setLoading(true);
    try {
      const response = await api.get(`/timeslots/without-pricing-booking-type`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setTimeSlotsWithoutPricing(response.data);
    } catch (error) {
      console.error("Error fetching time slots without pricing:", error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    const fetchPricingData = async () => {
      if (user) {
        await fetchVenueId();
        await fetchData();
        await fetchTimeSlotWithoutPricing();
      } else {
        setError("Người dùng chưa đăng nhập.");
        setLoading(false);
      }
    };
    fetchPricingData();
  }, [user, token, venueId]);

  const formatMoneyVND = (amount) => {
    if (isNaN(amount)) {
      return "Invalid amount";
    }

    return amount.toLocaleString("vi-VN", {
      style: "currency",
      currency: "VND",
    });
  };

  const handleCreateOrUpdatePricing = async (values) => {
    setLoading(true);
    try {
      if (currentRecord) {
        await api.put('/pricing/${currentRecord.id}/update', values);
        toast.success('Cập nhật thành công');
      } else {
        await api.post(`/pricing/timeSlots/${values.timeSlotId}/create`, values);
        toast.success('Tạo bảng giá thành công!');
      }
      setIsModalVisible(false);
      setCurrentRecord(null);
      form.resetFields();
      fetchData();
      fetchTimeSlotWithoutPricing();

    } catch (error) {
      console.error("Lỗi tạo bảng giá", error);
      toast.error('operation error');
    } finally {
      setLoading(false);
    }
  }


  const handleCreateOrUpdateTimeSlot = async (values) => {
    setLoading(true);
    try {
      if (currentRecord) {
        await api.put('/timeslots/{currentRecord.id}', values);
        toast.success('Cập nhật thành công');
      } else {
        await api.post(`/timeslots`, values);
        toast.success('Tạo khung giờ thành công!');
      }
      setIsModalVisible(false);
      setCurrentRecord(null);
      form.resetFields();
      fetchData();
      fetchTimeSlotWithoutPricing();
    } catch (error) {
      console.error("Lỗi tạo bảng giá", error);
      toast.error('operation error');
    } finally {
      setLoading(false);
    }
  }
  const handleCreateOrUpdate = async (values) => {
    setLoading(true);
    try {
      if (modalType === 'pricing') {
        if (currentRecord) {
          await api.put('/pricing/${currentRecord.id}/update', values);
          toast.success('Cập nhật thành công');
        } else {
          await api.post(`/pricing/timeSlots/${values.timeSlotId}/create`, values, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          toast.success('Tạo bảng giá thành công!');

        }

      } else if (modalType === 'timeSlots') {
        if (currentRecord) {
          await api.put('/timeslots/{currentRecord.id}', values);
          toast.success('Cập nhật thành công');
        } else {
          await api.post(`/timeslots`, values, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          toast.success('Tạo khung giờ thành công!');
        }
      }
      setIsModalVisible(false);
      setCurrentRecord(null);
      form.resetFields();
      fetchData();
      fetchTimeSlotWithoutPricing();

    } catch (error) {
      console.error("Error creating record:", error);
      notification.error({ message: 'Operation failed', description: error.message });
    } finally {
      setLoading(false);
    }
  };

  const showCreatePricingModal = () => {
    setIsCreatePricingModalVisible(true);
  };

  const showUpdatePricingModal = async (record) => {
    setCurrentRecord(record);
    setIsUpdatePricingModalVisible(true);

    form.setFieldsValue({
      bookingType: record.bookingType,
      timeSlotId: record.timeSlotId,
      pricePerHour: record.pricePerHour,
    });
  };

  const showCreateTimeSlotModal = () => {
    setIsCreateTimeSlotModalVisible(true);
  };

  const showUpdateTimeSlotModal = async (record) => {
    setCurrentRecord(record);
    setIsUpdateTimeSlotModalVisible(true);

    form.setFieldsValue({
      startTime: record.startTime,
      endTime: record.endTime,
    });
  };

  const handleModalCancel = () => {
    setIsCreatePricingModalVisible(false);
    setIsUpdatePricingModalVisible(false);
    setIsCreateTimeSlotModalVisible(false);
    setIsUpdateTimeSlotModalVisible(false);
    setCurrentRecord(null);
  };

  const showDetailsModal = (record) => {
    setDetailsData(record);
    setDetailsVisible(true);
  };

  const handleDetailsModalCancel = () => {
    setDetailsVisible(false);
    setDetailsData(null);
  };


  const showModal = (type, record = null) => {
    setModalType(type);
    setCurrentRecord(record);
    if (type === 'pricing') {
      if (record) {
        form.setFieldsValue({
          timeSlotId: record.timeSlotId,
          bookingType: record.bookingType,
          pricePerHour: record.pricePerHour,
        });
      } else {
        form.resetFields();
      }
    } else if (type === 'timeSlots') {
      if (record) {
        form.setFieldsValue({
          startTime: record.startTime,
          endTime: record.endTime,
        });
      } else {
        form.resetFields();
      }
    }
    setIsModalVisible(true);
  };
  
  if (loading) {
    return <Spin tip="Loading..." />;
  }
  if (error) {
    return <Alert message="Error" description={error} type="error" />;
  }

  const timeSlots = data?.timeSlots || [];
  const combinedData = timeSlots.flatMap(slot =>
    slot.pricingList.map(pricing => ({
      ...slot,
      bookingType: pricing.bookingType,
      pricePerHour: pricing.pricePerHour
    }))
  );

  const columns = [
    {
      title: 'STT',
      dataIndex: 'id',
      key: 'id',
      render: (text, record, index) => index + 1,
      align: 'center',
    },
    {
      title: 'Khung giờ',
      key: 'timeRange',
      render: (_, record) => record.startTime && record.endTime ? `${record.startTime} - ${record.endTime}` : '-',
      align: 'center',
    },
    {
      title: 'Loại đặt',
      dataIndex: 'bookingType',
      key: 'bookingType',
      render: (_, record) => record.bookingType || '-',
      align: 'center',
    },
    {
      title: 'Giá',
      key: 'price',
      render: (_, record) => record.pricePerHour ? formatMoneyVND(record.pricePerHour) : '-',
      align: 'center',
    },
    {
      title: 'Hành động',
      key: 'action',
      render: (text, record) => (
        <span>
          <Button type="primary" style={{ marginLeft: '8px' }} onClick={() => showDetailsModal(record)}>Xem chi tiết</Button>
          <Button type="primary" style={{ marginLeft: '8px', backgroundColor: 'green' }} onClick={() => showUpdatePricingModal(record)}>Sửa</Button>
        </span>
      ),
      align: 'center',
    },
  ];

  return (
    <div>
      {/* Buttons for adding pricing and time slots */}
      <Button
        type="primary"
        onClick={showCreatePricingModal}
        style={{ marginBottom: '16px' }}
      >
        Thêm Bảng Giá
      </Button>
      <Button
        type="primary"
        onClick={showCreateTimeSlotModal}
        style={{ marginBottom: '16px', marginLeft: '8px' }}
      >
        Thêm Khung Giờ
      </Button>

      {/* Table for displaying pricing and time slot data */}
      <Table
        dataSource={combinedData}
        columns={columns}
        rowKey="id"
      />

      {/* Modal for creating or updating pricing */}
      <Modal
        title={currentRecord ? 'Cập nhật Bảng Giá' : 'Thêm Bảng Giá'}
        visible={isCreatePricingModalVisible || isUpdatePricingModalVisible}
        onCancel={handleModalCancel}
        footer={[
          <Button key="cancel" onClick={handleModalCancel}>
            Hủy
          </Button>,
          <Button key="submit" type="primary" onClick={() => form.submit()}>
            {currentRecord ? 'Cập nhật' : 'Tạo'}
          </Button>,
        ]}
      >
        <Form form={form} onFinish={handleCreateOrUpdatePricing} layout="vertical">
          <Form.Item name="bookingType" label="Loại đặt" rules={[{ required: true, message: 'Vui lòng chọn loại đặt!' }]}>
            <Select
              placeholder="Chọn loại đặt"
              options={[
                { value: 'FIXED', label: 'Cố Định' },
                { value: 'DAILY', label: 'Ngày' },
                { value: 'FLEXIBLE', label: 'Linh Hoạt' },
              ]}
            />
          </Form.Item>
          <Form.Item name="timeSlotId" label="Khung giờ" rules={[{ required: true, message: 'Vui lòng chọn khung giờ!' }]}>
            <Select
              placeholder="Chọn khung giờ"
              options={timeSlotsWithoutPricing.map(slot => ({
                value: slot.id,
                label: `${slot.startTime} - ${slot.endTime}`,
              }))}
            />
          </Form.Item>
          <Form.Item name="pricePerHour" label="Giá mỗi giờ" rules={[{ required: true, message: 'Vui lòng nhập giá!' }]}>
            <Input type="number" min={0} step={1000} placeholder="Nhập giá mỗi giờ" />
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal for creating or updating time slots */}
      <Modal
        title={currentRecord ? 'Cập nhật Khung Giờ' : 'Thêm Khung Giờ'}
        visible={isCreateTimeSlotModalVisible || isUpdateTimeSlotModalVisible}
        onCancel={handleModalCancel}
        footer={[
          <Button key="cancel" onClick={handleModalCancel}>
            Hủy
          </Button>,
          <Button key="submit" type="primary" onClick={() => form.submit()}>
            {currentRecord ? 'Cập nhật' : 'Tạo'}
          </Button>,
        ]}
      >
        <Form form={form} onFinish={handleCreateOrUpdateTimeSlot} layout="vertical">
          <Form.Item name="startTime" label="Giờ bắt đầu" rules={[{ required: true, message: 'Please enter the start time' }]}>
            <Input placeholder="time" />
          </Form.Item>
          <Form.Item name="endTime" label="Giờ kết thúc" rules={[{ required: true, message: 'Please enter the end time' }]}>
            <Input placeholder="time" />
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal for displaying details of pricing or time slot */}
      <Modal
        title="Chi tiết"
        visible={detailsVisible}
        onCancel={handleDetailsModalCancel}
        footer={[
          <Button key="cancel" onClick={handleDetailsModalCancel}>
            Đóng
          </Button>,
        ]}
      >
        {detailsData && (
          <div>
            <p><strong>Khung giờ:</strong> {detailsData.startTime} - {detailsData.endTime}</p>
            <p><strong>Giá mỗi giờ:</strong> {formatMoneyVND(detailsData.pricePerHour)}</p>
            <p><strong>Loại đặt:</strong> {detailsData.bookingType}</p>
            <p><strong>Thời lượng: </strong>{detailsData.duration} giờ</p>
          </div>
        )}
      </Modal>
    </div>
  );
};
export default RegisterBookingType;