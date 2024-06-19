import React from "react";
import { NavLink, Link } from "react-router-dom";

const newsData = [
  {
    id: 1,
    title: "Khám Phá Những Điểm Khác Biệt Giữa Pickleball Vs Tennis",
    date: "13-06-2024 17:35",
    image:
      "https://cdn.shopvnb.com/img/400x240/uploads/tin_tuc/san-cau-long-hoa-sen-5_1718240803.webp",
    description:
      "Tennis đã từ lâu luôn khẳng định được vị thế của mình là một bộ môn thể thao quý tộc, phổ biến trên toàn thế giới...",
  },
  {
    id: 2,
    title: "Quang Dương Pickleball - Tài Năng Trẻ Gốc Việt Đầy Triển Vọng",
    date: "13-06-2024 17:32",
    image:
      "https://cdn.shopvnb.com/img/400x240/uploads/tin_tuc/san-cau-long-hoa-sen-5_1718240803.webp",
    description:
      "Gần đây, Quang Dương Pickleball đã trở thành cái tên đáng chú ý đối với những người yêu thích bộ môn Pickleball...",
  },
  {
    id: 3,
    title:
      "Khám Phá Chi Tiết Sân Cầu Lông Huệ Thiên Tại Địa Chỉ Phường Hiệp Bình Phước",
    date: "13-06-2024 16:47",
    image:
      "https://cdn.shopvnb.com/img/400x240/uploads/tin_tuc/san-cau-long-hoa-sen-5_1718240803.webp",
    description:
      "Nếu bạn đang sinh sống tại phường Hiệp Bình Phước, khu vực thành phố Thủ Đức đang tìm kiếm cho mình...",
  },
  {
    id: 4,
    title: "Trải Nghiệm Và Đánh Giá Chi Tiết Sân Cầu Lông Hoa Sen Ở Thủ Đức",
    date: "13-06-2024 16:44",
    image:
      "https://cdn.shopvnb.com/img/400x240/uploads/tin_tuc/san-cau-long-hoa-sen-5_1718240803.webp",
    description:
      "Sân cầu lông Hoa Sen là một trong những sân cầu đáp ứng nhu cầu chất lượng của các lông thủ, thiết kế hiện đại...",
  },
  // Thêm các mục dữ liệu giả khác tương tự
];
const courtsData = [
  {
    id: 1,
    name: "Sân cầu lông Việt Đức",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
    price: "100.000 đ/giờ",
  },
  {
    id: 2,
    name: "Sân cầu lông Phú Thọ",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
    price: "150.000 đ/giờ",
  },
  {
    id: 3,
    name: "Sân cầu lông Thủ Đức",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
    price: "120.000 đ/giờ",
  },
  {
    id: 3,
    name: "Sân cầu lông Thủ Đức",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
    price: "120.000 đ/giờ",
  },
  {
    id: 3,
    name: "Sân cầu lông Thủ Đức",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
    price: "120.000 đ/giờ",
  },
  // Thêm các mục dữ liệu giả khác tương tự
];

const HomePage = (props) => {
  return (
    <div className="homepage-container bg-gray-100">
      <section className="relative text-center py-20">
        <img
          src="https://sieuthicaulong.vn/images/slider/1678970732-2880x1120_momota.jpg"
          alt="Badminton Banner"
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 flex flex-col items-center justify-center bg-black bg-opacity-40">
          <h1 className="text-5xl font-bold text-white">
            Welcome to Badminton Club - Đặt sân nhanh chóng và dễ dàng
          </h1>

        </div>
      </section>

      <section className="py-20 bg-gray-100">
        <div className="container mx-auto px-6">
          <h2 className="text-3xl font-bold text-gray-900 text-center">
            Sân cầu lông nổi bật
          </h2>
          <div className="flex justify-center mt-8">
            <nav className="flex space-x-4">
              <button className="bg-orange-500 text-white px-4 py-2 rounded">
                Tất cả
              </button>
              <button className="text-gray-600 hover:text-orange-500 px-4 py-2">
                Sân ở Hồ Chí Minh
              </button>
              <button className="text-gray-600 hover:text-orange-500 px-4 py-2">
                Sân ở Hà Nội
              </button>
              <button className="text-gray-600 hover:text-orange-500 px-4 py-2">
                Sân ở Đà Nẵng
              </button>
            </nav>
          </div>
          <div className="flex flex-wrap mt-8 justify-center">
            {courtsData.map((court) => (
              <div key={court.id} className="w-full md:w-1/5 p-4">
                <div className="bg-white rounded-lg shadow-lg p-6 border-2 border-orange-500">
                  <img
                    src={court.image}
                    alt={court.name}
                    className="w-full h-48 object-cover rounded-t-lg"
                  />
                  <h3 className="text-xl font-bold text-gray-800 mt-4">
                    {court.name}
                  </h3>
                  <p className="text-gray-600 mt-2">{court.location}</p>
                  <p className="text-red-500 mt-2">{court.price}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="py-20 bg-gray-100">
        <div className="container mx-auto px-6">
          <h2 className="text-3xl font-bold text-gray-900 text-center">
            Tin tức mới
          </h2>
          <div className="flex flex-wrap mt-8 justify-center">
            {newsData.map((news) => (
              <div key={news.id} className="w-full md:w-1/4 p-4">
                <div className="bg-white rounded-lg shadow-lg p-6 flex flex-col justify-between h-full">
                  <img
                    src={news.image}
                    alt={news.title}
                    className="w-full h-48 object-cover rounded-t-lg"
                  />
                  <h3 className="text-xl font-bold text-gray-800 mt-4">
                    {news.title}
                  </h3>
                  <p className="text-orange-500 mt-2">{news.date}</p>
                  <p className="text-gray-600 mt-2 flex-grow">
                    {news.description}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
