import React, { useState, useEffect } from "react";
import axios from "axios";
import { PlusOutlined } from '@ant-design/icons';
import { Upload, Image, Select, message } from 'antd';
import api from "../../../config/axios"; // Ensure correct API config import
import Navbar from "../../NavBar/navbar";
import { useSelector } from "react-redux";
import { selectUser } from "../../../redux/features/counterSlice";
import { toast } from "react-toastify";

const { Option } = Select;

const UpdateCourtInfo = () => {
  const [venue, setVenue] = useState({});
  const [fileList, setFileList] = useState([]);
  const [previewOpen, setPreviewOpen] = useState(false);
  const [previewImage, setPreviewImage] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const token = localStorage.getItem("token");
  const user = useSelector(selectUser);

  useEffect(() => {
    const fetchVenue = async () => {
      try {
        const response = await api.get(`/venues/manager/${user.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const venueData = response.data;
        setVenue(venueData); 
      } catch (error) {
        console.error("Error fetching venue data:", error);
      }
    };
    fetchVenue();
  }, [user, token]);

  const handleUpdateCourt = async (e) => {
    e.preventDefault();
    try {
      console.log("updatedVenue:", venue);

      const response = await api.put(`/venues/update`, venue, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("response:", response);

      if (response.status === 200) {
        setSuccess("Venue updated successfully!");
        setError(null);
        toast.success("Venue updated successfully!");
      } else {
        setError(response.data.message || "Failed to update venue. Please try again.");
        toast.error(response.data.message || "Failed to update venue. Please try again.");
        setSuccess(null);
      }
    } catch (error) {
      console.error("Error updating venue:", error);
      setError(error.response?.data?.message || "Failed to update venue. Please try again.");
      toast.error(error.response?.data?.message || "Failed to update venue. Please try again.");
      setSuccess(null);
    }
  };


  const getBase64 = (file) => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });

  const handleChange = async ({ fileList: newFileList }) => {
    setFileList(newFileList);
    if (newFileList.length > 0) {
      const file = newFileList[newFileList.length - 1].originFileObj;
      const base64 = await getBase64(file);
      setVenue({ ...venue, imageUrl: base64 }); // Cập nhật imageUrl trong state venue
    } else {
      setVenue({ ...venue, imageUrl: "" }); // Cập nhật imageUrl trong state venue
    }
  };

  const handlePreview = async (file) => {
    if (!file.imageUrl && !file.preview) {
      file.preview = await getBase64(file.originFileObj);
    }
    setPreviewImage(file.imageUrl || file.preview);
    setPreviewOpen(true);
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Cập nhật thông tin sân</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      {success && <div className="text-green-500 mb-4">{success}</div>}
      <form onSubmit={handleUpdateCourt}>
        <div className="mb-4">
          <label className="block mb-2">Tên địa điểm</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venue.name}
            onChange={(e) => {
              setVenue({ ...venue, name: e.target.value });
            }}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Địa chỉ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venue.address}
            onChange={(e) => {
              setVenue({ ...venue, address: e.target.value });
            }}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Hình ảnh</label>
          <Upload
            listType="picture-card"
            fileList={fileList}
            onPreview={handlePreview}
            onChange={handleChange}
          >
            {fileList.length >= 8 ? null : (
              <button
                style={{
                  border: 0,
                  background: 'none',
                }}
                type="button"
              >
                <PlusOutlined />
                <div
                  style={{
                    marginTop: 8,
                  }}
                >
                  Upload
                </div>
              </button>
            )}
          </Upload>
        </div>
        <div className="mb-4">
          <label className="block mb-2">Trạng thái</label>
          <Select
            className="w-full"
            value={venue.venueStatus}
            onChange={(value) => {
              setVenue({ ...venue, venueStatus: value });
            }}
          >
            <Option value="OPEN">OPEN</Option>
            <Option value="CLOSE">CLOSE</Option>
          </Select>
        </div>
        <div className="mb-4">
          <label className="block mb-2">Thông tin liên hệ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venue.contactInfor}
            onChange={(e) => {
              setVenue({ ...venue, contactInfor: e.target.value });
            }}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Giờ mở cửa</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venue.openingHour}
            onChange={(e) => {
              setVenue({ ...venue, openingHour: e.target.value });
            }}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Giờ đóng cửa</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venue.closingHour}
            onChange={(e) => {
              setVenue({ ...venue, closingHour: e.target.value });
            }}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Mô tả</label>
          <textarea
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venue.description}
            onChange={(e) => {
              setVenue({ ...venue, description: e.target.value });
            }}
            required
          ></textarea>
        </div>
        <div className="mb-4">
          <label className="block mb-2">Dịch vụ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venue.services}
            onChange={(e) => {
              setVenue({ ...venue, services: e.target.value });
            }}
            required
          />
        </div>
        <button
          type="submit"
          className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-600"
        >
          Cập nhật
        </button>
      </form>
    </div>
  );
};

export default UpdateCourtInfo;
