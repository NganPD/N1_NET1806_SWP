import React, { useState, useEffect } from "react";
import api from "../../../config/axios";
import { Table, Modal, Form, Input, Button, Select } from 'antd';
import { useSelector } from "react-redux";
import { selectUser } from "../../../redux/features/counterSlice";
import { toast } from "react-toastify";
import { render } from "@testing-library/react";
// import Navbar from "../../NavBar/navbar";

const { Option } = Select;
const ManageCourts = () => {
  const [venueId, setVenueId] = useState(null);
  const [courts, setCourts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isAddingCourt, setIsAddingCourt] = useState(false);
  const [isEditingCourt, setIsEditingCourt] = useState(false);
  const [editingCourt, setEditingCourt] = useState(null);

  const token = localStorage.getItem("token");
  const user = useSelector(selectUser);

  useEffect(() => {
    const fetchVenueId = async () => {
      setIsLoading(true);
      try {
        const response = await api.get(`/venues/manager/${user.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setVenueId(response.data.id);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching venue ID:", error);
        setError(error.message);
        setIsLoading(false);
      }
    };
    if (user) {
      fetchVenueId();
    }
  }, [user, token]);

  useEffect(() => {
    const fetchCourts = async () => {
      setIsLoading(true);
      try {
        if (venueId) {
          const response = await api.get(`/venues/${venueId}/courts`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          setCourts(response.data);
        }
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching courts:", error);
        setError(error.message);
        setIsLoading(false);
      }
    };
    if (venueId) {
      fetchCourts();
    }
  }, [venueId, token]);

  const handleAddCourt = () => {
    setIsAddingCourt(true);
  };

  const handleEditCourt = (court) => {
    setEditingCourt(court);
    setIsEditingCourt(true);
  };

  const handleDeleteCourt = async (courtId) => {
    Modal.confirm({
      title: 'Xác nhận xóa sân',
      content: 'Bạn có chắc chắn muốn xóa sân này?',
      okText: 'Xóa',
      okType: 'danger',
      cancelText: 'Hủy',
      onOk: async () => {
        try {
          await api.delete(`/api/venues/${venueId}/courts/${courtId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          setCourts(courts.filter((court) => court.id !== courtId));
          toast.success("Court deleted successfully!");
        } catch (error) {
          console.error("Error deleting court:", error);
          toast.error("Failed to delete court. Please try again.");
        }
      },
    });
  };

  const handleSaveCourt = async (values) => {
    try {
      const response = await api.post(`courts/venues/${venueId}/courts`, values, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setCourts([...courts, response.data]);
      setIsAddingCourt(false);
      toast.success("Court added successfully!");
    } catch (error) {
      console.error("Error adding court:", error);
      toast.error("Failed to add court. Please try again.");
    }
  };

  const handleUpdateCourt = async (values) => {
    try {
      const response = await api.put(`courts/venue/${venueId}/court/${editingCourt.id}`, values, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const updatedCourts = courts.map((court) => {
        if (court.id === editingCourt.id) {
          return response.data;
        }
        return court;
      });
      setCourts(updatedCourts);
      setIsEditingCourt(false);
      setEditingCourt(null);
      toast.success("Court updated successfully!");
    } catch (error) {
      console.error("Error updating court:", error);
      toast.error("Failed to update court. Please try again.");
    }
  };

  const handleDeactivateCourt = async (courtId) => {
    try {
      await api.delete(`/courts/${courtId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setCourts(courts.map(court =>
        court.id === courtId ? { ...court, status: 'INACTIVE' } : court
      ));
      toast.success("Court deactivated successfully!");
    } catch (error) {
      console.error("Error deactivating court:", error);
      toast.error("Failed to deactivate court. Please try again.");
    }
  };

  const handleActivateCourt = async (courtId) => {
    try {
      await api.patch(`/courts/${courtId}`, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setCourts(courts.map(court =>
        court.id === courtId ? { ...court, status: 'AVAILABLE' } : court
      ));
      toast.success("Court activated successfully!");
    } catch (error) {
      console.error("Error activating court:", error);
      toast.error("Failed to activate court. Please try again.");
    }
  };

  const columns = [
    {
      title: 'STT',
      dataIndex: 'id',
      key: 'id',
      render: (text, record, index) => index + 1, // Hiển thị STT cho mỗi dòng
    },
    {
      title: 'Tên sân',
      dataIndex: 'courtName',
      key: 'courtName',
    },
    {
      title: 'Trạng thái',
      key: 'status',
      render: (text, court) => (
        <Button
          type={court.status === 'INACTIVE' ? 'primary' : 'danger'}
          style={{ backgroundColor: court.status === 'INACTIVE' ? 'green' : 'red' }}
          onClick={() => {
            if (court.status === 'INACTIVE') {
              handleActivateCourt(court.id);
            } else {
              handleDeactivateCourt(court.id);
            }
          }}
        >
          {court.status === 'INACTIVE' ? 'ACTIVE' : 'INACTIVE'}
        </Button>
      ),
    },
    {
      title: 'Hành động',
      key: 'action',
      render: (text, court) => (
        <Button type="primary" style={{ marginLeft: '8px', backgroundColor: 'green' }} onClick={() => handleEditCourt(court)}>Sửa</Button>
      ),
    },
  ];

  const [form] = Form.useForm();
  const courtModalForm = (
    <Form
      name="courtForm"
      form={form}
      onFinish={isAddingCourt ? handleSaveCourt : () => handleUpdateCourt(editingCourt.id)}
      layout="vertical"
    >
      <Form.Item
        name="courtName"
        label="Tên sân"
        rules={[{ required: true, message: 'Vui lòng nhập tên sân' }]}
      >
        <Input placeholder="Nhập tên sân" />
      </Form.Item>
      <Form.Item
        name="status"
        label="Trạng thái"
        rules={[{ required: true, message: 'Vui lòng chọn trạng thái' }]}
      >
        <Select placeholder="Chọn trạng thái">
          <Option value="AVAILABLE">AVAILABLE</Option>
          <Option value="INACTIVE">INACTIVE</Option>
        </Select>
      </Form.Item>
      <Form.Item
        name="description"
        label="Mô tả"
        initialValue={isAddingCourt ? '' : editingCourt?.description}
      >
        <Input.TextArea rows={4} placeholder="Nhập mô tả" />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit">
          {isAddingCourt ? 'Thêm' : 'Lưu'}
        </Button>
        <Button
          onClick={() => {
            setIsAddingCourt(false);
            setIsEditingCourt(false);
            setEditingCourt(null);
            form.resetFields();
          }}
          style={{ marginLeft: '8px' }}
        >
          Hủy
        </Button>
      </Form.Item>
    </Form>
  );
  return (
    <>
      {/* <Navbar /> */}
      <div>
        {isLoading && <div>Loading...</div>}
        {error && <div className="text-red-500 mb-4">{error}</div>}
        <h1 className="text-2xl font-bold mb-4">
          Quản lý sân
        </h1>
        <Button type="primary" onClick={handleAddCourt}>Thêm sân mới</Button>
        <Table columns={columns} dataSource={courts} rowKey="id" />
        <Modal
          title={isAddingCourt ? 'Thêm sân' : 'Sửa sân'}
          visible={isAddingCourt || isEditingCourt}
          onCancel={() => {
            setIsAddingCourt(false);
            setIsEditingCourt(false);
            setEditingCourt(null);
          }}
          footer={null}
        >
          {courtModalForm}
        </Modal>
      </div>
    </>

  );
};

export default ManageCourts;
