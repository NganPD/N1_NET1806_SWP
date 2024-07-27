import React, { useEffect, useState } from "react";
import CourtCard from "../CourtCard";
import api from "../../config/axios";

const CourtList = () => {
  const [data, setData] = useState([]);
  const [locations, setLocations] = useState([]);
  const [openingHours, setOpeningHours] = useState([]);
  const fetch = async () => {
    try {
      const response = await api.get("/venues");
      console.log(response.data);
      setData(response.data);
      
      // Tạo mảng các khu vực từ address và loại bỏ trùng lặp
      const uniqueLocations = [...new Set(response.data.map(court => court.address))];
      setLocations(uniqueLocations);

      // Tạo mảng các giờ mở cửa từ openingHour và loại bỏ trùng lặp
      const uniqueOpeningHours = [...new Set(response.data.map(court => court.openingHour))];
      setOpeningHours(uniqueOpeningHours);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedLocation, setSelectedLocation] = useState("");
  const [selectedOperatingHour, setSelectedOperatingHour] = useState("");

  const courtsPerPage = 6;

  const handleLocationChange = (event) => {
    setSelectedLocation(event.target.value);
  };

  const handleOperatingHourChange = (event) => {
    setSelectedOperatingHour(event.target.value);
  };

  const filteredCourts = data
    .filter(
      (court) =>
        court.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        court.address.toLowerCase().includes(searchTerm.toLowerCase())
    )
    .filter((court) => {
      if (selectedLocation === "") return true;
      return court.address === selectedLocation;
    })
    .filter((court) => {
      if (selectedOperatingHour === "") return true;
      return court.openingHour === selectedOperatingHour;
    });

  const indexOfLastCourt = currentPage * courtsPerPage;
  const indexOfFirstCourt = indexOfLastCourt - courtsPerPage;
  const currentCourts = filteredCourts.slice(
    indexOfFirstCourt,
    indexOfLastCourt
  );

  const totalPages = Math.ceil(filteredCourts.length / courtsPerPage);

  return (
    <div className="container mx-auto mb-6">
      <h1 className="text-4xl font-bold text-center my-8">Danh sách sân</h1>
      <div className="bg-gray-100 p-6 rounded-lg shadow-md mb-8 flex items-center justify-between">
        <input
          type="text"
          placeholder="Tìm kiếm sân..."
          className="px-4 py-2 border rounded-lg w-full md:w-1/3 mr-4"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <div className="flex items-center space-x-4">
          <div>
            <label className="block mb-2">Khu vực</label>
            <select
              className="px-4 py-2 border rounded"
              value={selectedLocation}
              onChange={handleLocationChange}
            >
              <option value="">Tất cả</option>
              {locations.map((location, index) => (
                <option key={index} value={location}>
                  {location}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block mb-2">Giờ mở cửa</label>
            <select
              className="px-4 py-2 border rounded"
              value={selectedOperatingHour}
              onChange={handleOperatingHourChange}
            >
              <option value="">Tất cả</option>
              {openingHours.map((hour, index) => (
                <option key={index} value={hour}>
                  {hour}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>
      <div className="flex flex-wrap justify-center bg-white p-4 rounded-lg shadow-md">
        {currentCourts.map((court) => (
          <CourtCard key={court.id} court={court} />
        ))}
      </div>
      <div className="flex justify-center mt-4">
        <button
          className="px-4 py-2 mx-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          disabled={currentPage === 1}
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
        >
          Previous
        </button>
        {[...Array(totalPages)].map((_, index) => (
          <button
            key={index}
            className={`px-4 py-2 mx-2 ${currentPage === index + 1
                ? "bg-blue-700 text-white"
                : "bg-blue-500 text-white"
              } rounded hover:bg-blue-600`}
            onClick={() => setCurrentPage(index + 1)}
          >
            {index + 1}
          </button>
        ))}
        <button
          className="px-4 py-2 mx-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          disabled={currentPage === totalPages}
          onClick={() =>
            setCurrentPage((prev) => Math.min(prev + 1, totalPages))
          }
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default CourtList;
