import React, { useState, useEffect } from "react";
import api from "../../../config/axios";

const AccountManagement = () => {
    const [accounts, setAccounts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchAccounts = async () => {
            try {
                const response = await api.get("/account");
                setAccounts(response.data);
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchAccounts();
    }, []);

    const handleDelete = async (id) => {
        console.log(`Attempting to delete account with id: ${id}`);
        try {
            const response = await api.delete(`/account/account/${id}`);
            console.log("Delete response:", response);
            setAccounts(accounts.filter((account) => account.id !== id));
        } catch (error) {
            console.error("Failed to delete account:", error);
        }
    };

    const handleEdit = (id) => {
        // Implement logic to edit the account here
        console.log("Edit account with id:", id);
    };

    if (loading) {
        return <div className="text-center py-4">Loading...</div>;
    }

    if (error) {
        return <div className="text-center text-red-500 py-4">Error: {error.message}</div>;
    }

    return (
        <div className="bg-white p-6 rounded shadow-lg">
            <h2 className="text-2xl font-bold mb-6">Quản lý thông tin tài khoản</h2>
            <table className="min-w-full table-auto">
                <thead>
                    <tr className="bg-gray-200">
                        <th className="py-2 px-4 border-b text-left">ID</th>
                        <th className="py-2 px-4 border-b text-left">Email</th>
                        <th className="py-2 px-4 border-b text-left">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    {accounts.map((account, index) => (
                        <tr key={account.id} className={index % 2 === 0 ? 'bg-gray-100' : 'bg-white'}>
                            <td className="py-2 px-4 border-b text-left">{account.id}</td>
                            <td className="py-2 px-4 border-b text-left">{account.email}</td>
                            <td className="py-2 px-4 border-b text-left">
                                <button
                                    onClick={() => handleEdit(account.id)}
                                    className="bg-blue-500 text-white px-4 py-2 rounded mr-2 hover:bg-blue-700"
                                >
                                    Chỉnh sửa
                                </button>
                                <button
                                    onClick={() => handleDelete(account.id)}
className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-700"
                                >
                                    Xóa
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AccountManagement;