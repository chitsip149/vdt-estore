import React, { useEffect, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [orders, setOrders] = useState([]);
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    fetchProfile();
    fetchOrders();
  }, []);

  const fetchProfile = async () => {
    try {
      const res = await fetch(`${process.env.BACKEND_URL}/profile`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Failed to fetch profile");
      const data = await res.json();
      setProfile(data);
    } catch (err) {
      console.error("Error loading profile:", err);
    }
  };

  const fetchOrders = async () => {
    try {
      const res = await fetch(`${process.env.BACKEND_URL}/orders`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Failed to fetch orders");
      const data = await res.json();
      setOrders(data);
    } catch (err) {
      console.error("Error loading orders:", err);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("cartId");
    navigate("/login");
  };

  return (
    <div style={{ padding: "40px", fontFamily: "sans-serif", backgroundColor: "#f8f9fa", minHeight: "100vh" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "30px" }}>
        <h2>👤 Profile</h2>
        <div>
            <NavLink
          to="/home"
          style={{
            fontSize: "1.2rem",
            padding: "8px 16px",
            backgroundColor: "#007bff",
            color: "white",
            borderRadius: "6px",
            textDecoration: "none",
            fontWeight: "bold",
            margin: "15px"
          }}
        >
          Home
        </NavLink>
        <button
            style={{
              fontSize: "1.2rem",
              padding: "8px 15px",
              backgroundColor: "#dc3545",
              color: "#fff",
              border: "none",
              borderRadius: "6px",
              cursor: "pointer",
              fontWeight: "bold"
            }}
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </div>

      {profile ? (
        <div style={{ marginBottom: "40px", backgroundColor: "#fff", padding: "20px", borderRadius: "10px", boxShadow: "0 2px 4px rgba(0,0,0,0.05)" }}>
          <p><strong>Name:</strong> {profile.user.name}</p>
          <p><strong>Email:</strong> {profile.user.email}</p>
          <p><strong>Phone:</strong> {profile.phoneNumber}</p>
          <p><strong>Date of Birth:</strong> {profile.dateOfBirth}</p>
          <p><strong>Bio:</strong> {profile.bio}</p>
          <p><strong>Loyalty Points:</strong> {profile.loyaltyPoints}</p>
        </div>
      ) : (
        <p>Loading profile...</p>
      )}

      <h3 style={{ marginBottom: "10px" }}>📦 Order History</h3>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <div style={{ backgroundColor: "#fff", padding: "20px", borderRadius: "10px", boxShadow: "0 2px 4px rgba(0,0,0,0.05)" }}>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f1f3f5" }}>
                <th style={th}>Order ID</th>
                <th style={th}>Date</th>
                <th style={th}>Total</th>
                <th style={th}>Status</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.id} style={{ borderBottom: "1px solid #e9ecef" }}>
                  <td style={td}>{order.id}</td>
                  <td style={td}>{new Date(order.createdAt).toLocaleString()}</td>
                  <td style={td}>₫{order.totalPrice}</td>
                  <td style={td}>{order.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

const th = {
  padding: "12px",
  textAlign: "left",
  borderBottom: "2px solid #dee2e6",
  fontWeight: "bold",
};

const td = {
  padding: "10px",
};

export default Profile;
