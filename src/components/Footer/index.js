// src/components/Footer.js
import React from "react";
import goodminton from "D:/Swp-301/N1_NET1806_SWP/src/download__1_-removebg-preview.png";
const Footer = () => {
  return (
    <footer className="bg-white shadow-md mt-20 py-10">
      <div className="container mx-auto px-10">
        <div className="ml-20 flex flex-wrap justify-center md:justify-between items-start">
          <div className="w-full md:w-1/4 mb-6 md:mb-0 text-center md:text-left">
            <img
              src={goodminton}
              alt="Logo"
              className="h-20 mb-4 mx-auto md:mx-0"
            />
            <p className="text-gray-700">
              Vinhomes Grand Park, S10.02, Hồ Chí Minh, Thành phố Hồ Chí Minh
            </p>
            <p className="text-gray-700 mt-2">Liên hệ: 0911 048 699</p>
            <p className="text-gray-700 mt-2">
              Email: marketing.sieuthicaulong@gmail.com
            </p>
          </div>
          <div className="w-full md:w-1/4 mb-6 md:mb-0 text-center md:text-left">
            <h3 className="text-lg font-bold text-gray-800 mb-4">
              Về chúng tôi
            </h3>
            <ul className="text-gray-700">
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Giới thiệu
                </a>
              </li>
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Chính sách & Quy định
                </a>
              </li>
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Điều khoản & Bảo mật
                </a>
              </li>
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Liên hệ
                </a>
              </li>
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Sitemap
                </a>
              </li>
            </ul>
          </div>
          <div className="w-full md:w-1/4 mb-6 md:mb-0 text-center md:text-left">
            <h3 className="text-lg font-bold text-gray-800 mb-4">
              Hỗ trợ khách hàng
            </h3>
            <ul className="text-gray-700">
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Chính sách đổi trả hàng
                </a>
              </li>
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Chính sách vận chuyển
                </a>
              </li>
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Hướng dẫn thanh toán
                </a>
              </li>
              <li className="mt-2">
                <a href="#" className="hover:text-blue-600">
                  Liên hệ
                </a>
              </li>
            </ul>
          </div>
          <div className="w-full md:w-1/4 text-center md:text-left">
            <h3 className="text-lg font-bold text-gray-800 mb-4">
              Kết nối với chúng tôi
            </h3>
            <div className="flex justify-center md:justify-start space-x-4">
              <a href="#" className="text-gray-700 hover:text-blue-600">
                <img
                  src="https://img.icons8.com/fluent/48/000000/facebook-new.png"
                  alt="Facebook"
                  className="h-6 w-6"
                />
              </a>
              <a href="#" className="text-gray-700 hover:text-blue-600">
                <img
                  src="https://img.icons8.com/fluent/48/000000/twitter.png"
                  alt="Twitter"
                  className="h-6 w-6"
                />
              </a>
              <a href="#" className="text-gray-700 hover:text-blue-600">
                <img
                  src="https://img.icons8.com/fluent/48/000000/instagram-new.png"
                  alt="Instagram"
                  className="h-6 w-6"
                />
              </a>
              <a href="#" className="text-gray-700 hover:text-blue-600">
                <img
                  src="https://img.icons8.com/fluent/48/000000/youtube-play.png"
                  alt="YouTube"
                  className="h-6 w-6"
                />
              </a>
            </div>
          </div>
        </div>
        <div className="border-t mt-10 pt-4 text-center text-gray-600">
          © 2024 Siêu Thị Cầu Lông. All rights reserved.
        </div>
      </div>
    </footer>
  );
};

export default Footer;
