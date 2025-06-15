import React, { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";

const Cart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const token = localStorage.getItem("token");

  useEffect(() => {
    ensureCart();
  }, []);

  const ensureCart = async () => {
    let cartId = localStorage.getItem("cartId");

    try {
      const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts/${cartId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Cart not found");

      const data = await res.json();
      setCartItems(sortCartItemsByName(data.items));
      setTotalPrice(data.totalPrice || 0);
    } catch {
      localStorage.removeItem("cartId");
      const newRes = await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts`, { method: "POST" });
      if (newRes.ok) {
        const newCart = await newRes.json();
        localStorage.setItem("cartId", newCart.id);
        fetchCart(newCart.id);
      }
    }
  };

  const fetchCart = async (cartId) => {
    try {
      const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts/${cartId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      setCartItems(sortCartItemsByName(data.items));
      setTotalPrice(data.totalPrice || 0);
    } catch (err) {
      console.error("Error fetching cart:", err);
    }
  };

  const updateQuantity = async (productId, quantity) => {
    if (quantity < 1) return;
    const cartId = localStorage.getItem("cartId");

    try {
      await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts/${cartId}/items/${productId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ quantity }),
      });
      fetchCart(cartId);
    } catch (err) {
      console.error("Error updating quantity:", err);
    }
  };

  const handleRemoveItem = async (productId) => {
    const cartId = localStorage.getItem("cartId");

    try {
      await fetch(`${process.env.REACT_APP_BACKEND_URL}/carts/${cartId}/items/${productId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchCart(cartId);
    } catch (err) {
      console.error("Error removing item:", err);
    }
  };

  const handleCheckout = async () => {
    const cartId = localStorage.getItem("cartId");

    try {
      const res = await fetch(`${process.env.REACT_APP_BACKEND_URL}/checkout`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ cartId }),
      });

      if (!res.ok) throw new Error("Checkout failed");

      const data = await res.json();
      window.location.href = data.checkoutUrl;
    } catch (err) {
      console.error("Error during checkout:", err);
    }
  };

  return (
    <div style={{ padding: "40px", fontFamily: "sans-serif", background: "#f9f9f9", minHeight: "100vh" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "30px" }}>
        <h2 style={{ margin: 0 }}>🛒 Your Cart</h2>
        <NavLink
          to="/home"
          style={{
            padding: "8px 16px",
            backgroundColor: "#007bff",
            color: "white",
            borderRadius: "6px",
            textDecoration: "none",
            fontWeight: "bold",
          }}
        >
          Home
        </NavLink>
      </div>

      {cartItems.length === 0 ? (
        <p style={{ fontSize: "1.2rem", color: "#666" }}>Your cart is empty.</p>
      ) : (
        <div style={{ overflowX: "auto", backgroundColor: "white", padding: "20px", borderRadius: "10px" }}>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f2f2f2" }}>
                <th style={thStyle}>Product</th>
                <th style={thStyle}>Price</th>
                <th style={thStyle}>Qty</th>
                <th style={thStyle}>Total</th>
                <th style={thStyle}>Remove</th>
              </tr>
            </thead>
            <tbody>
              {cartItems.map((item) => (
                <tr key={item.product.id} style={{ borderBottom: "1px solid #eee" }}>
                  <td style={tdStyle}>{item.product.name}</td>
                  <td style={tdStyle}>₫{item.product.price}</td>
                  <td style={tdStyle}>
                    <button onClick={() => updateQuantity(item.product.id, item.quantity - 1)} style={qtyBtn}>–</button>
                    <span style={{ margin: "0 10px" }}>{item.quantity}</span>
                    <button onClick={() => updateQuantity(item.product.id, item.quantity + 1)} style={qtyBtn}>+</button>
                  </td>
                  <td style={tdStyle}>₫{item.totalPrice}</td>
                  <td style={tdStyle}>
                    <button onClick={() => handleRemoveItem(item.product.id)} style={removeBtn}>
                      Remove
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <h3 style={{ marginTop: "30px", textAlign: "right" }}>Total: ₫{totalPrice}</h3>
          <div style={{ textAlign: "right" }}>
            <button onClick={handleCheckout} style={checkoutBtn}>Proceed to Checkout</button>
          </div>
        </div>
      )}
    </div>
  );
};

// Sort helper
const sortCartItemsByName = (items) =>
  [...items].sort((a, b) => a.product.name.localeCompare(b.product.name));

// 🧩 Extracted Styles
const thStyle = {
  padding: "12px",
  textAlign: "left",
  fontWeight: "bold",
  color: "#333",
  borderBottom: "2px solid #ddd",
};

const tdStyle = {
  padding: "12px",
  verticalAlign: "middle",
};

const qtyBtn = {
  padding: "4px 10px",
  backgroundColor: "#e0e0e0",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

const removeBtn = {
  padding: "6px 12px",
  backgroundColor: "#dc3545",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

const checkoutBtn = {
  marginTop: "10px",
  padding: "10px 20px",
  backgroundColor: "#28a745",
  color: "white",
  fontWeight: "bold",
  fontSize: "1rem",
  border: "none",
  borderRadius: "6px",
  cursor: "pointer",
};

export default Cart;
