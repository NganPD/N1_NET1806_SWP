import { Button, Form, Input, Modal, Table } from 'antd';
import axios from 'axios';
import React, { useEffect, useState } from 'react';

function AccountManagement() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [data, setData] = useState([]);

    const showModal = () => {
        setIsModalOpen(true);
    };

    const handleOk = () => {
        setIsModalOpen(false);
    };

    const handleCancel = () => {
        setIsModalOpen(false);
    };

    const onFinish = async (values) => {
        try {
            const res = await axios.post("https://665d6f09e88051d604068e77.mockapi.io/category", values);
            setData([...data, res.data]);
            setIsModalOpen(false);
        } catch (error) {
            console.error("Error adding category:", error);
        }
    };

    const onFinishFailed = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };

    const handleDelete = async (values) => {
        try {
            await axios.delete(`https://665d6f09e88051d604068e77.mockapi.io/category/${values.id}`);
            setData(data.filter((item) => item.id !== values.id));
        } catch (error) {
            console.error("Error deleting category:", error);
        }
    };

    const fetchData = async () => {
        try {
            const res = await axios.get("https://665d6f09e88051d604068e77.mockapi.io/category");
            setData(res.data);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Category Name',
            dataIndex: 'categoryName',
            key: 'categoryName',
        },
        {
            title: 'Action',
            render: (values) => (
                <Button onClick={() => handleDelete(values)} danger type="primary">
                    Delete
                </Button>
            ),
        },
    ];

    return (
        <div>
            <Button type="primary" onClick={showModal}>
                Add new category
            </Button>
            <Modal footer={false} title="Add Category" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
                <Form
                    name="basic"
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                    style={{ maxWidth: 600 }}
                    initialValues={{ remember: true }}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    autoComplete="off"
                >
                    <Form.Item
                        label="Category Name"
                        name="categoryName"
                        rules={[{ required: true, message: 'Please input the category name!' }]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                        <Button type="primary" htmlType="submit">
                            Submit
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
            <Table dataSource={data} columns={columns} rowKey="id" />
        </div>
    );
}

export default AccountManagement;
