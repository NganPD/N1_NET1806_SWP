import React, { useState } from "react";

const ContactPage = () => {

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-4xl font-bold text-center my-8">LIÊN HỆ VỚI CHÚNG TÔI</h1>
      <div className="text-center mb-8">
        <p><strong>Nhà thi đấu:</strong> Sân cầu lông TBA – Ngã Tư Gò Mây</p>
        <p><strong>Địa chỉ:</strong> 252/4/46, QL1A, Bình Hưng Hoà B, Bình Tân, Thành phố Hồ Chí Minh</p>
        <p><strong>SĐT 1:</strong> 0931992945</p>
        <p><strong>EMAIL:</strong> thinhndse171550@fpt.edu.vn</p>
        <div className="mt-4">
          <p><strong>Mạng xã hội:</strong></p>
          <a href="https://www.facebook.com/profile.php?id=100016686274220" className="text-black hover:text-yellow-500 px-4 py-2 inline-block mx-2">Facebook</a>
          <a href="https://www.youtube.com" className="text-black hover:text-yellow-500 px-4 py-2 inline-block mx-2">YouTube</a>
        </div>
      </div>

    </div>
  );
};

export default ContactPage;
