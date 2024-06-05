import React from "react";
import { NavLink, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

const featuredCourtsData = [
  {
    id: 1,
    name: "Sân cầu lông Việt Đức",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
  },
  {
    id: 2,
    name: "Sân cầu lông Phú Thọ",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
  },
  {
    id: 3,
    name: "Sân cầu lông Thủ Đức",
    location: "Hồ Chí Minh",
    image:
      "https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png",
  },
  // Thêm các mục dữ liệu giả khác tương tự
];

const HomePage = (props) => {
  return (
    <div className="homepage-container">
      <main className="bg-white">
        <section className="relative bg-gray-200 text-center py-20">
          <img
            src="https://i.pinimg.com/736x/d8/1a/6a/d81a6a84a1793ac23939d40ad1e75686.jpg"
            alt="Badminton Court"
            className="absolute inset-0 w-full h-full object-cover opacity-20"
          />
          <div className="relative z-10">
            <h1 className="text-5xl font-bold text-blue-600">
              Welcome to Badminton Club
            </h1>
            <p className="text-xl text-gray-700 mt-4">
              Join us to improve your skills and have fun!
            </p>
            <button className="mt-6 bg-blue-500 text-white rounded-full px-8 py-4 hover:bg-blue-600">
              Get Started
            </button>
          </div>
        </section>

        <section className="py-20">
          <div className="container mx-auto px-6">
            <h2 className="text-3xl font-bold text-gray-800 text-center">
              About Us
            </h2>
            <p className="text-gray-600 mt-4 text-center">
              We are a community of badminton enthusiasts of all levels. Whether
              you are a beginner or a professional, you'll find a place here!
            </p>
          </div>
        </section>

        <section className="bg-gray-200 py-20">
          <div className="container mx-auto px-6">
            <h2 className="text-3xl font-bold text-gray-800 text-center">
              Our Services
            </h2>
            <div className="flex flex-wrap mt-8">
              <div className="w-full md:w-1/3 p-4">
                <div className="bg-white rounded-lg shadow-lg p-6">
                  <img
                    src="https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png"
                    className="w-full h-48 object-cover rounded-t-lg"
                    alt="Training Sessions"
                  />
                  <h3 className="text-xl font-bold text-gray-800 mt-4">
                    Training Sessions
                  </h3>
                  <p className="text-gray-600 mt-2">
                    Join our training sessions to improve your skills.
                  </p>
                </div>
              </div>
              <div className="w-full md:w-1/3 p-4">
                <div className="bg-white rounded-lg shadow-lg p-6">
                  <img
                    src="https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png"
                    alt="Court Booking"
                    className="w-full h-48 object-cover rounded-t-lg"
                  />
                  <h3 className="text-xl font-bold text-gray-800 mt-4">
                    Court Booking
                  </h3>
                  <p className="text-gray-600 mt-2">
                    Book a court for your next game with ease.
                  </p>
                </div>
              </div>
              <div className="w-full md:w-1/3 p-4">
                <div className="bg-white rounded-lg shadow-lg p-6">
                  <img
                    src="https://inhat.vn/hcm/wp-content/uploads/2022/06/thue-san-cau-long-tphcm-2-min.png"
                    alt="Tournaments"
                    className="w-full h-48 object-cover rounded-t-lg"
                  />
                  <h3 className="text-xl font-bold text-gray-800 mt-4">
                    Tournaments
                  </h3>
                  <p className="text-gray-600 mt-2">
                    Participate in our regular tournaments and compete with
                    others.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section className="py-20">
          <div className="container mx-auto px-6">
            <h2 className="text-3xl font-bold text-gray-800 text-center">
              Featured Courts
            </h2>
            <div className="flex flex-wrap mt-8 justify-center">
              {featuredCourtsData.map((court) => (
                <div key={court.id} className="w-full md:w-1/3 p-4">
                  <div className="bg-white rounded-lg shadow-lg p-6">
                    <img
                      src={court.image}
                      alt={court.name}
                      className="w-full h-48 object-cover rounded-t-lg"
                    />
                    <h3 className="text-xl font-bold text-gray-800 mt-4">
                      {court.name}
                    </h3>
                    <p className="text-gray-600 mt-2">{court.location}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

export default HomePage;
