import { useState } from "react";
import { importAllAthletes } from "../services/api";

export default function AdminPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [importing, setImporting] = useState(false);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    async function handleImport() {
        try {
            setImporting(true);
            setError("");
            setMessage("Import kører...");

            await importAllAthletes({ username, password });

            setMessage("Import færdig");
        } catch (err) {
            setError(err.message || "Import fejlede");
            setMessage("");
        } finally {
            setImporting(false);
        }
    }

    return (
        <div
            style={{
                minHeight: "100vh",
                backgroundColor: "#f9fafb",
                padding: "32px",
                fontFamily: "Arial, sans-serif",
            }}
        >
            <div
                style={{
                    maxWidth: "500px",
                    margin: "0 auto",
                    backgroundColor: "white",
                    padding: "24px",
                    borderRadius: "10px",
                    boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
                }}
            >
                <h1 style={{ marginBottom: "8px" }}>Admin panel</h1>
                <p style={{ color: "#555", marginBottom: "20px" }}>
                    Log ind for at køre import
                </p>

                <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
                    <input
                        type="text"
                        placeholder="Brugernavn"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        style={inputStyle}
                    />

                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={inputStyle}
                    />

                    <button
                        onClick={handleImport}
                        disabled={importing || !username || !password}
                        style={buttonStyle}
                    >
                        {importing ? "Importerer..." : "Import all"}
                    </button>
                </div>

                {message && (
                    <div style={successStyle}>
                        {message}
                    </div>
                )}

                {error && (
                    <div style={errorStyle}>
                        {error}
                    </div>
                )}
            </div>
        </div>
    );
}

const inputStyle = {
    padding: "10px 12px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    fontSize: "14px",
};

const buttonStyle = {
    padding: "10px 14px",
    borderRadius: "6px",
    border: "none",
    backgroundColor: "#2563eb",
    color: "white",
    cursor: "pointer",
    fontSize: "14px",
};

const successStyle = {
    marginTop: "16px",
    padding: "12px",
    backgroundColor: "#ecfdf5",
    border: "1px solid #a7f3d0",
    borderRadius: "8px",
};

const errorStyle = {
    marginTop: "16px",
    padding: "12px",
    backgroundColor: "#fef2f2",
    border: "1px solid #fecaca",
    borderRadius: "8px",
    color: "#991b1b",
};