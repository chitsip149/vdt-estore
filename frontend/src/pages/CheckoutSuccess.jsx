import React from "react";
import { useLocation, NavLink } from "react-router-dom";

const CheckoutSuccess = () => {
  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const orderId = query.get("orderId");

  return (
    <div style={{
      padding: "60px 20px",
      textAlign: "center",
      fontFamily: "sans-serif",
      backgroundColor: "#f9f9f9",
      minHeight: "100vh"
    }}>
      <div style={{
        maxWidth: "600px",
        margin: "0 auto",
        backgroundColor: "#ffffff",
        padding: "40px",
        borderRadius: "12px",
        boxShadow: "0 4px 10px rgba(0, 0, 0, 0.1)"
      }}>
        <h1 style={{ color: "#28a745", fontSize: "2rem" }}>🎉 Payment Successful!</h1>
        <p style={{ fontSize: "1.1rem", marginTop: "20px" }}>
          Thank you for your purchase.
        </p>
        {orderId && (
          <p style={{ fontSize: "1rem", color: "#555" }}>
            Your order ID is <strong>{orderId}</strong>.
          </p>
        )}
        <NavLink
          to="/home"
          style={{
            display: "inline-block",
            marginTop: "30px",
            padding: "10px 20px",
            backgroundColor: "#007bff",
            color: "white",
            textDecoration: "none",
            borderRadius: "6px",
            fontWeight: "bold"
          }}
        >
          Back to Home
        </NavLink>
      </div>
    </div>
  );
};

export default CheckoutSuccess;
