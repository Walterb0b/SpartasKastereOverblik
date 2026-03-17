import { useState } from "react";

export default function LoginForm({ onLogin, error }) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleSubmit(e) {
        e.preventDefault();
        onLogin({ username, password });
    }

    return (
        <div
            style={{
                minHeight: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "#f9fafb",
                fontFamily: "Arial, sans-serif",
            }}
        >
            <form
                onSubmit={handleSubmit}
                style={{
                    backgroundColor: "white",
                    padding: "24px",
                    borderRadius: "8px",
                    boxShadow: "0 2px 10px rgba(0,0,0,0.08)",
                    width: "320px",
                }}
            >
                <h2 style={{ marginTop: 0 }}>Login</h2>

                <div style={{ marginBottom: "12px" }}>
                    <label>Brugernavn</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        style={inputStyle}
                    />
                </div>

                <div style={{ marginBottom: "12px" }}>
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={inputStyle}
                    />
                </div>

                {error && (
                    <div
                        style={{
                            marginBottom: "12px",
                            color: "#991b1b",
                            backgroundColor: "#fef2f2",
                            padding: "8px",
                            borderRadius: "6px",
                        }}
                    >
                        {error}
                    </div>
                )}

                <button type="submit" style={buttonStyle}>
                    Log ind
                </button>
            </form>
        </div>
    );
}

const inputStyle = {
    width: "100%",
    marginTop: "4px",
    padding: "8px 10px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    boxSizing: "border-box",
};

const buttonStyle = {
    width: "100%",
    padding: "10px 14px",
    borderRadius: "6px",
    border: "none",
    backgroundColor: "#2563eb",
    color: "white",
    cursor: "pointer",
};