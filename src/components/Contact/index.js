import React, { useState } from "react";

const ContactPage = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    // Xử lý gửi liên hệ tại đây
    console.log("Name:", name);
    console.log("Email:", email);
    console.log("Message:", message);
    // Xóa các trường sau khi gửi
    setName("");
    setEmail("");
    setMessage("");
    alert("Your message has been sent!");
  };

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-4xl font-bold text-center my-8">LIÊN HỆ VỚI CHÚNG TÔI</h1>
      <div className="text-center mb-8">
        <p><strong>Nhà thi đấu:</strong> Sân cầu lông TBA – Ngã Tư Gò Mây</p>
        <p><strong>Địa chỉ:</strong> 252/4/46, QL1A, Bình Hưng Hoà B, Bình Tân, Thành phố Hồ Chí Minh</p>
        <p><strong>SĐT 1:</strong> 0931992945</p>
        <p><strong>EMAIL:</strong>thinhndse171550@fpt.edu.vn</p>
        <div className="mt-4">
          <p><strong>Mạng xã hội:</strong></p>
          <a href="https://www.facebook.com/profile.php?id=100016686274220" className="inline-block mx-2">Facebook</a>
          <a href="https://www.youtube.com" className="inline-block mx-2">YouTube</a>
        </div>
      </div>
      {/* <form
        onSubmit={handleSubmit}
        className="max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg"
      >
        <div className="mb-4">
          <label className="block mb-2 text-gray-700">Name</label>
          <input
            type="text"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2 text-gray-700">Email</label>
          <input
            type="email"
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2 text-gray-700">Message</label>
          <textarea
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
            rows="5"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            required
          ></textarea>
        </div>
        <button
          type="submit"
          className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition duration-200"
        >
          Send Message
        </button>
      </form> */}
    </div>
  );
};

export default ContactPage;
