import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const [products, setProducts] = useState([]);
  const [cartCount, setCartCount] = useState(0);

  const token = localStorage.getItem("token");
  const cartId = localStorage.getItem("cartId");
  const navigate = useNavigate();

  useEffect(() => {
    fetchProducts();
    if (token) {
      if (!cartId) {
        createCart();
      } else {
        fetchCartItemCount(cartId);
      }
    }
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await fetch(`${process.env.BACKEND_URL}/products`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error("Failed to fetch products");
      const data = await res.json();
      setProducts(data);
    } catch (err) {
      console.error("Error fetching products:", err);
    }
  };

  const createCart = async () => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error("Failed to create cart");
      const data = await res.json();
      localStorage.setItem("cartId", data.id);
      setCartCount(0);
    } catch (err) {
      console.error("Error creating cart:", err);
    }
  };

  const fetchCartItemCount = async (cartId) => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts/${cartId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error("Failed to get cart");
      const data = await res.json();
      const totalItems = data.items.reduce((sum, item) => sum + item.quantity, 0);
      setCartCount(totalItems);
    } catch (err) {
      console.error("Error fetching cart items:", err);
    }
  };

  const handleAddToCart = async (productId) => {
    try {
      const cartId = localStorage.getItem("cartId");
      if (!cartId) {
        alert("Cart not created yet!");
        return;
      }

      const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts/${cartId}/items`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ productId }),
      });

      if (!res.ok) throw new Error("Failed to add to cart");

      await fetchCartItemCount(cartId);
    } catch (err) {
      console.error("Error adding product to cart:", err);
    }
  };

  const handleCartClick = () => {
    navigate("/cart");
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("cartId");
    navigate("/login");
  };

  return (
    <div style={{ padding: "40px", fontFamily: "sans-serif", backgroundColor: "#f8f9fa" }}>
      <div style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        marginBottom: "30px"
      }}>
        <h1 style={{ color: "#343a40" }}>📚 VDT eStore</h1>
        <div style={{ display: "flex", alignItems: "center", gap: "15px" }}>
          <div
            style={{
              fontSize: "1.2rem",
              cursor: "pointer",
              padding: "8px 15px",
              borderRadius: "6px",
              backgroundColor: "#007bff",
              color: "#fff",
              fontWeight: "bold",
              transition: "background 0.2s"
            }}
            onClick={handleCartClick}
            title="Go to Cart"
          >
            Cart: {cartCount}
          </div>

          <div
            onClick={() => navigate("/profile")}
            style={{
              fontSize: "1.2rem",
              padding: "8px 15px",
              borderRadius: "6px",
              backgroundColor: "#6c757d",
              color: "#fff",
              fontWeight: "bold",
              cursor: "pointer"
            }}
          >
            Profile
          </div>

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

      <div style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
        gap: "20px"
      }}>
        {products.map((product) => (
          <div
            key={product.id}
            style={{
              backgroundColor: "#fff",
              border: "1px solid #dee2e6",
              borderRadius: "12px",
              padding: "20px",
              boxShadow: "0 2px 5px rgba(0, 0, 0, 0.05)"
            }}
          >
            <img
              src={`/assets/${product.id}.jpg`}
              alt={product.name}
              onError={(e) => {
                e.target.onerror = null;
                e.target.src = "/assets/default.jpg";
              }}
              style={{
                width: "100%",
                height: "250px",
                objectFit: "cover",
                borderRadius: "8px",
                marginBottom: "10px"
              }}
            />
            <h3 style={{ color: "#343a40", marginBottom: "10px" }}>{product.name}</h3>
            <p style={{ color: "#6c757d", fontSize: "0.95rem" }}>{product.description}</p>
            <strong style={{ display: "block", marginTop: "10px", fontSize: "1.1rem" }}>
              ₫{product.price}
            </strong>
            <button
              style={{
                marginTop: "15px",
                padding: "8px 12px",
                backgroundColor: "#28a745",
                color: "#fff",
                border: "none",
                borderRadius: "6px",
                cursor: "pointer",
                fontWeight: "bold",
                width: "100%"
              }}
              onClick={() => handleAddToCart(product.id)}
            >
              Add to Cart
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;
