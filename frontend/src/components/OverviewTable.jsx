import { Link } from "react-router-dom";

export default function OverviewTable({ rows }) {
    return (
        <div style={{ overflowX: "auto", marginTop: "20px" }}>
            <table
                style={{
                    width: "100%",
                    borderCollapse: "collapse",
                    backgroundColor: "white",
                    borderRadius: "8px",
                    overflow: "hidden",
                    boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
                }}
            >
                <thead>
                <tr style={{ backgroundColor: "#f3f4f6" }}>
                    <th style={thStyle}>Atlet</th>
                    <th style={thStyle}>Kugle</th>
                    <th style={thStyle}>Diskos</th>
                    <th style={thStyle}>Hammer</th>
                    <th style={thStyle}>Spyd</th>
                    <th style={thStyle}>Vægt</th>
                </tr>
                </thead>
                <tbody>
                {rows.map((row) => (
                    <tr key={row.athleteId}>
                        <td style={tdStyleName}>
                            <Link
                                to={`/athlete/${row.athleteId}`}
                                style={{ color: "#2563eb", textDecoration: "none" }}
                            >
                                {row.athleteName}
                            </Link>
                        </td>
                        <td style={tdStyle}>{formatValue(row.shotPut)}</td>
                        <td style={tdStyle}>{formatValue(row.discus)}</td>
                        <td style={tdStyle}>{formatValue(row.hammer)}</td>
                        <td style={tdStyle}>{formatValue(row.javelin)}</td>
                        <td style={tdStyle}>{formatValue(row.weightThrow)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

function formatValue(value) {
    if (value === null || value === undefined) {
        return "-";
    }

    return Number(value).toFixed(2);
}

const thStyle = {
    textAlign: "left",
    padding: "12px",
    borderBottom: "1px solid #ddd",
};

const tdStyle = {
    padding: "12px",
    borderBottom: "1px solid #eee",
};

const tdStyleName = {
    ...tdStyle,
    fontWeight: "600",
};