import React, { useState } from "react";
import api from "../../../config/axios";

const AccountRole = () => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('ADMIN');
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(null);

  const handleAddAccount = async (e) => {
    e.preventDefault();

    try {
      const newAccount = {
        fullName,
        email,
        phone,
        password,
        role
      };

      console.log(newAccount)

      const response = await api.post("/account/create", newAccount);
      setSuccess("Account created successfully!");
      setError(null);

      // Reset form
      setEmail("");
      setFullName("");
      setPassword("");
      setPhone("");
      setRole("ADMIN");

    } catch (error) {
      setError("Failed to create account. Please try again.");
      setSuccess(null);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Đăng ký tài khoản mới</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      {success && <div className="text-green-500 mb-4">{success}</div>}
      <form onSubmit={handleAddAccount}>
        <div className="mb-4">
          <label className="block mb-2">Tên</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Email</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Số điện thoại</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Mật khẩu</label>
          <input
            type="password"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2">Vai trò</label>
          <select
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            required
          >
            <option value="ADMIN">ADMIN</option>
            <option value="MANAGER">MANAGER</option>
            <option value="STAFF">STAFF</option>
          </select>
        </div>
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded-lg"
        >
          Đăng ký
        </button>
      </form>
    </div>
  );
};

export default AccountRole;
