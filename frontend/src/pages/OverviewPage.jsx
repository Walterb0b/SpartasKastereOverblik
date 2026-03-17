import { useEffect, useState } from "react";
import OverviewTable from "../components/OverviewTable";
import {
    fetchPrOverview,
    fetchSbOverview,
    importAllAthletes,
} from "../services/api";

export default function OverviewPage({auth}) {
    const currentYear = new Date().getFullYear();

    const [mode, setMode] = useState("PR");
    const [year, setYear] = useState(currentYear);
    const [rows, setRows] = useState([]);
    const [loading, setLoading] = useState(true);
    const [importing, setImporting] = useState(false);
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    async function loadData() {
        try {
            setLoading(true);
            setError("");

            const data =
                mode === "PR"
                    ? await fetchPrOverview(auth)
                    : await fetchSbOverview(year, auth);

            setRows(data);
        } catch (err) {
            setError(err.message || "Noget gik galt");
        } finally {
            setLoading(false);
        }
    }

    async function handleImportAll() {
        try {
            setImporting(true);
            setError("");
            setMessage("Import kører...");

            await importAllAthletes(auth);
            await loadData();

            setMessage("Import færdig");
        } catch (err) {
            setError(err.message || "Import fejlede");
            setMessage("");
        } finally {
            setImporting(false);
        }
    }

    useEffect(() => {
        loadData();
    }, [mode, year]);

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
                    maxWidth: "1100px",
                    margin: "0 auto",
                }}
            >
                <h1 style={{ marginBottom: "8px" }}>Kaster-overblik</h1>
                <p style={{ color: "#555", marginBottom: "24px" }}>
                    Oversigt over træningsgruppens resultater
                </p>

                <div
                    style={{
                        display: "flex",
                        gap: "12px",
                        alignItems: "center",
                        flexWrap: "wrap",
                        marginBottom: "20px",
                        backgroundColor: "white",
                        padding: "16px",
                        borderRadius: "8px",
                        boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
                    }}
                >
                    <label>
                        Visning{" "}
                        <select
                            value={mode}
                            onChange={(e) => setMode(e.target.value)}
                            style={inputStyle}
                        >
                            <option value="PR">PR</option>
                            <option value="SB">SB</option>
                        </select>
                    </label>

                    {mode === "SB" && (
                        <label>
                            År{" "}
                            <input
                                type="number"
                                value={year}
                                onChange={(e) => setYear(Number(e.target.value))}
                                style={inputStyle}
                            />
                        </label>
                    )}

                    {auth.username === "admin" && (
                        <button onClick={handleImportAll} disabled={importing} style={buttonStyle}>
                            {importing ? "Importerer..." : "Import all"}
                        </button>
                    )}
                </div>

                {message && (
                    <div
                        style={{
                            marginBottom: "16px",
                            padding: "12px",
                            backgroundColor: "#ecfdf5",
                            border: "1px solid #a7f3d0",
                            borderRadius: "8px",
                        }}
                    >
                        {message}
                    </div>
                )}

                {error && (
                    <div
                        style={{
                            marginBottom: "16px",
                            padding: "12px",
                            backgroundColor: "#fef2f2",
                            border: "1px solid #fecaca",
                            borderRadius: "8px",
                            color: "#991b1b",
                        }}
                    >
                        {error}
                    </div>
                )}

                {loading ? (
                    <div
                        style={{
                            backgroundColor: "white",
                            padding: "20px",
                            borderRadius: "8px",
                            boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
                        }}
                    >
                        Loader...
                    </div>
                ) : (
                    <OverviewTable rows={rows} />
                )}
            </div>
        </div>
    );
}

const inputStyle = {
    marginLeft: "6px",
    padding: "8px 10px",
    borderRadius: "6px",
    border: "1px solid #ccc",
};

const buttonStyle = {
    padding: "10px 14px",
    borderRadius: "6px",
    border: "none",
    backgroundColor: "#2563eb",
    color: "white",
    cursor: "pointer",
};