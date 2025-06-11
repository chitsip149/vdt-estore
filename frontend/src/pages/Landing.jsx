import React from "react";
import { useNavigate } from "react-router-dom";

const Landing = () => {
  const navigate = useNavigate();

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        background: "linear-gradient(135deg, #e3f2fd, #fce4ec)",
        fontFamily: "sans-serif",
        padding: "20px"
      }}
    >
      <h1 style={{ fontSize: "3rem", color: "#343a40", marginBottom: "1rem" }}>
        📚 Welcome to VDT eStore!
      </h1>
      <p style={{ fontSize: "1.2rem", color: "#555", marginBottom: "3rem" }}>
        Discover and shop your favorite books.
      </p>
      <button
        onClick={() => navigate("/login")}
        style={{
          padding: "12px 30px",
          fontSize: "1.1rem",
          backgroundColor: "#007bff",
          color: "#fff",
          border: "none",
          borderRadius: "8px",
          cursor: "pointer",
          boxShadow: "0 4px 12px rgba(0, 0, 0, 0.1)",
          transition: "background 0.3s ease"
        }}
        onMouseOver={(e) => (e.target.style.backgroundColor = "#0056b3")}
        onMouseOut={(e) => (e.target.style.backgroundColor = "#007bff")}
      >
        Login
      </button>
    </div>
  );
};

export default Landing;
