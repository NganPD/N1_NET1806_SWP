import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

function Overview() {
    const data = [
        {
            "name": "Sân 1",
            "fixed": 4000,
            "daily": 2400,
            "flexible": 1000,
        },
        {
            "name": "Sân 2",
            "fixed": 3000,
            "daily": 1398,
            "flexible": 1000
        },
        {
            "name": "Sân 3",
            "fixed": 2000,
            "daily": 9800,
            "flexible": 1000
        },
        {
            "name": "Sân 4",
            "fixed": 2780,
            "daily": 3908,
            "flexible": 1000
        },

    ];

    const dataWithTotal = data.map(item => ({
        ...item,
        total: item.daily + item.fixed + item.flexible
    }));

    return (
        <div style={{
            height: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center"
        }}>
            <BarChart width={730} height={250} data={dataWithTotal}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="daily" stackId="a" fill="#8884d8" />
                <Bar dataKey="fixed" stackId="a" fill="#82ca9d" />
                <Bar dataKey="flexible" stackId="a" fill="#ffc658" />
                <Bar dataKey="total" style={{ display: "none" }} fill="#ff8042" />
            </BarChart>
        </div>
    );
}

export default Overview;
