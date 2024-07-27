import React, { useEffect, useState } from "react";
import axios from "axios";
import api from "../../../config/axios";
import { PlusOutlined } from '@ant-design/icons';
import { Image, Upload } from 'antd';
import uploadFile from "../../../utils/file";

const NewCourtRegistration = () => {
  const [venueName, setVenueName] = useState("");
  const [address, setAddress] = useState("");
  const [imageUrl, setImageUrl] = useState("");
  const [venueStatus, setVenueStatus] = useState("OPEN");
  const [contactInfor, setContactInfor] = useState("");
  const [openingHours, setOpeningHours] = useState("");
  const [closingHours, setClosingHours] = useState("");
  const [description, setDescription] = useState("");
  const [services, setServices] = useState("");
  const [managerId, setManagerId] = useState(0);
  const [availableManagers, setAvailableManagers] = useState([]);

  const token = localStorage.getItem("token");
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(null);
  const [previewOpen, setPreviewOpen] = useState(false);
  const [previewImage, setPreviewImage] = useState('');
  const [fileList, setFileList] = useState([]);

  const getBase64 = (file) =>
    new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });



  const handleAddCourt = async (e) => {
    e.preventDefault();

    try {
      const newVenue = {
        venueName,
        address,
        imageUrl,
        venueStatus,
        contactInfor,
        openingHours,
        closingHours,
        description,
        services,
        managerId,
      };

      if (fileList.length > 0) {
        newVenue.imageUrl = await uploadFile(fileList[0].originFileObj)
      }

      console.log(newVenue)

      const response = await api.post("/venues", newVenue);
      setSuccess("Venues created successfully!");
      setError(null);

      // Reset form
      setVenueName("");
      setAddress("");
      setImageUrl("");
      setVenueStatus("OPEN");
      setContactInfor("");
      setOpeningHours("");
      setClosingHours("");
      setDescription("");
      setServices("");
      setManagerId(0);
      setFileList([])
    } catch (error) {
      setError("Failed to create court. Please try again.");
      setSuccess(null);
    }
  };

  const uploadButton = (
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
  );

  const handleChange = ({ fileList: newFileList }) =>
    setFileList(
      newFileList.map((file, index) => ({
        ...file,
        key: file.uid || index, // Ensure unique key, use file.uid if available
      }))
    );

  const handlePreview = async (file) => {
    if (!file.imageUrl && !file.preview) {
      file.preview = await getBase64(file.originFileObj);
    }
    setPreviewImage(file.imageUrl || file.preview);
    setPreviewOpen(true);
  };

  const handleFetchManager = async () => {
    const response = await api.get('account/available-managers')
    setAvailableManagers(response.data)
  }

  useEffect(() => {
    handleFetchManager()
  }, [])

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Đăng ký thông tin sân mới</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      {success && <div className="text-green-500 mb-4">{success}</div>}
      <form onSubmit={handleAddCourt}>
        <div className="mb-4">
          <label className="block mb-2">Tên địa điểm</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venueName}
            onChange={(e) => setVenueName(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Địa chỉ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">URL hình ảnh</label>
          <Upload
            action="https://660d2bd96ddfa2943b33731c.mockapi.io/api/upload"
            listType="picture-card"
            fileList={fileList}
            onPreview={handlePreview}
            onChange={handleChange}
          >
            {fileList.length >= 8 ? null : uploadButton}
          </Upload>
        </div>
        <div className="mb-4">
          <label className="block mb-2">Trạng thái</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={venueStatus}
            onChange={(e) => setVenueStatus(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Thông tin liên hệ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={contactInfor}
            onChange={(e) => setContactInfor(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Giờ mở cửa</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={openingHours}
            onChange={(e) => setOpeningHours(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Giờ đóng cửa</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={closingHours}
            onChange={(e) => setClosingHours(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Mô tả</label>
          <textarea
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          ></textarea>
        </div>
        <div className="mb-4">
          <label className="block mb-2">Dịch vụ</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={services}
            onChange={(e) => setServices(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Mã quản lý</label>
          <select
            type="number"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={isNaN(managerId) ? '' : managerId}
            onChange={(e) => {
              console.log(e.target.value)
              setManagerId(e.target.value ? Number(e.target.value) : '')
            }}
            required
          >
            {availableManagers.map(manager => <option value={manager.id}>{manager.fullName}</option>)}
          </select>
        </div>
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded-lg"
        >
          Đăng ký
        </button>
      </form>
      {previewImage && (
        <Image
          wrapperStyle={{
            display: 'none',
          }}
          preview={{
            visible: previewOpen,
            onVisibleChange: (visible) => setPreviewOpen(visible),
            afterOpenChange: (visible) => !visible && setPreviewImage(''),
          }}
          src={previewImage}
        />
      )}
    </div>
  );
};

export default NewCourtRegistration;
