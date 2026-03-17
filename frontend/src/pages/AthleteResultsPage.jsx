import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { fetchAthleteResults } from "../services/api";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    Legend
} from "recharts";

export default function AthleteResultsPage({auth}) {
    const { athleteId } = useParams();

    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [selectedDiscipline, setSelectedDiscipline] = useState("ALL");

    useEffect(() => {
        async function loadResults() {
            try {
                setLoading(true);
                setError("");

                const data = await fetchAthleteResults(athleteId, auth);

                data.sort((a, b) => new Date(b.resultDate) - new Date(a.resultDate));
                setResults(data);
            } catch (err) {
                setError(err.message || "Noget gik galt");
            } finally {
                setLoading(false);
            }
        }

        loadResults();
    }, [athleteId]);

    const athleteName = results.length > 0 ? results[0].athleteName : "Atlet";

    const filteredResults = useMemo(() => {
        if (selectedDiscipline === "ALL") {
            return results;
        }

        return results.filter((result) => result.discipline === selectedDiscipline);
    }, [results, selectedDiscipline]);

    const prByDiscipline = useMemo(() => {
        const prs = {
            SHOT_PUT: null,
            DISCUS: null,
            HAMMER: null,
            JAVELIN: null,
            WEIGHT_THROW: null
        };

        results.forEach((r) => {
            if (!prs[r.discipline] || r.resultValue > prs[r.discipline]) {
                prs[r.discipline] = r.resultValue;
            }
        });

        return prs;
    }, [results]);

    const chartData = useMemo(() => {
        const source =
            selectedDiscipline === "ALL"
                ? results
                : results.filter((r) => r.discipline === selectedDiscipline);

        return source
            .slice()
            .sort((a, b) => new Date(a.resultDate) - new Date(b.resultDate))
            .map((r) => ({
                date: r.resultDate,
                value: r.resultValue,
                discipline: r.discipline,
            }));
    }, [results, selectedDiscipline]);

    const prProgression = useMemo(() => {
        if (selectedDiscipline === "ALL") return [];

        const disciplineResults = results
            .filter(r => r.discipline === selectedDiscipline)
            .sort((a,b) => new Date(a.resultDate) - new Date(b.resultDate));

        let currentPr = 0;

        return disciplineResults
            .map(r => {
                if (r.resultValue > currentPr) {
                    currentPr = r.resultValue;
                    return {
                        date: r.resultDate,
                        value: r.resultValue
                    };
                }
                return null;
            })
            .filter(Boolean);

    }, [results, selectedDiscipline]);

    const combinedChartData = useMemo(() => {
        const map = new Map();

        chartData.forEach(d => {
            map.set(d.date, { date: d.date, result: d.value });
        });

        prProgression.forEach(p => {
            const existing = map.get(p.date) || { date: p.date };
            existing.pr = p.value;
            map.set(p.date, existing);
        });

        return Array.from(map.values());
    }, [chartData, prProgression]);

    const yDomain = useMemo(() => {
        if (chartData.length === 0) return [0, 10];

        const values = chartData.map(d => d.value);

        const min = Math.min(...values);
        const max = Math.max(...values);

        const lower = Math.floor(min) - 1;
        const upper = Math.ceil(max) + 1;

        return [lower, upper];
    }, [chartData]);

    return (
        <div
            style={{
                minHeight: "100vh",
                backgroundColor: "#f9fafb",
                padding: "32px",
                fontFamily: "Arial, sans-serif",
            }}
        >
            <div style={{ maxWidth: "1200px", margin: "0 auto" }}>
                <Link
                    to="/"
                    style={{
                        display: "inline-block",
                        marginBottom: "20px",
                        color: "#2563eb",
                        textDecoration: "none",
                    }}
                >
                    ← Tilbage til overview
                </Link>

                <h1 style={{ marginBottom: "8px" }}>{athleteName}</h1>
                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "repeat(auto-fit, minmax(150px, 1fr))",
                        gap: "12px",
                        marginBottom: "20px"
                    }}
                >
                    <PrCard title="Kugle" value={prByDiscipline.SHOT_PUT} />
                    <PrCard title="Diskos" value={prByDiscipline.DISCUS} />
                    <PrCard title="Hammer" value={prByDiscipline.HAMMER} />
                    <PrCard title="Spyd" value={prByDiscipline.JAVELIN} />
                    <PrCard title="Vægt" value={prByDiscipline.WEIGHT_THROW} />
                </div>

                <div
                    style={{
                        backgroundColor: "white",
                        padding: "16px",
                        borderRadius: "8px",
                        boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
                        marginBottom: "20px",
                    }}
                >
                    <h3 style={{ marginTop: 0, marginBottom: "12px" }}>Udvikling</h3>

                    {selectedDiscipline === "ALL" ? (
                        <p>Vælg en disciplin for at se udviklingsgrafen.</p>
                    ) : chartData.length === 0 ? (
                        <p>Ingen data til grafen</p>
                    ) : (
                        <div style={{ width: "100%", height: 320 }}>
                            <ResponsiveContainer>
                                <LineChart data={combinedChartData}>
                                    <CartesianGrid strokeDasharray="2 2" />

                                    <XAxis
                                        dataKey="date"
                                        tickFormatter={(value) =>
                                            new Date(value).toLocaleDateString("da-DK")
                                        }
                                    />

                                    <YAxis
                                        domain={yDomain}
                                        tickFormatter={(v) => v.toFixed(1)}
                                    />

                                    <Tooltip
                                        labelFormatter={(value) =>
                                            new Date(value).toLocaleDateString("da-DK")
                                        }
                                        formatter={(value) => Number(value).toFixed(2)}
                                    />

                                    <Legend verticalAlign="top" height={36} />

                                    <Line
                                        type="monotone"
                                        dataKey="result"
                                        stroke="#8884d8"
                                        strokeWidth={2}
                                        dot={{ r: 2 }}
                                        name="Resultater"
                                    />

                                    <Line
                                        type="monotone"
                                        dataKey="pr"
                                        stroke="#ef4444"
                                        strokeWidth={3}
                                        dot={{ r: 4 }}
                                        name="PR progression"
                                    />

                                </LineChart>
                            </ResponsiveContainer>
                        </div>
                    )}
                </div>

                {!loading && !error && (
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
                            Disciplin{" "}
                            <select
                                value={selectedDiscipline}
                                onChange={(e) => setSelectedDiscipline(e.target.value)}
                                style={inputStyle}
                            >
                                <option value="ALL">Alle</option>
                                <option value="SHOT_PUT">Kugle</option>
                                <option value="DISCUS">Diskos</option>
                                <option value="HAMMER">Hammer</option>
                                <option value="JAVELIN">Spyd</option>
                                <option value="WEIGHT_THROW">Vægt</option>
                            </select>
                        </label>

                        <span style={{ color: "#555" }}>
              Viser {filteredResults.length} resultater
            </span>
                    </div>
                )}

                {loading && (
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

                {!loading && !error && (
                    <div style={{ overflowX: "auto" }}>
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
                                <th style={thStyle}>Dato</th>
                                <th style={thStyle}>Disciplin</th>
                                <th style={thStyle}>Resultat</th>
                                <th style={thStyle}>Konkurrence</th>
                                <th style={thStyle}>Sted</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filteredResults.map((result) => (
                                <tr key={result.id}>
                                    <td style={tdStyle}>{formatDate(result.resultDate)}</td>
                                    <td style={tdStyle}>{formatDiscipline(result.discipline)}</td>
                                    <td style={tdStyle}>{formatValue(result.resultValue)}</td>
                                    <td style={tdStyle}>{result.competition ?? "-"}</td>
                                    <td style={tdStyle}>{result.location ?? "-"}</td>
                                </tr>
                            ))}

                            {filteredResults.length === 0 && (
                                <tr>
                                    <td style={tdStyle} colSpan="5">
                                        Ingen resultater i denne disciplin
                                    </td>
                                </tr>
                            )}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}

function formatValue(value) {
    if (value === null || value === undefined) {
        return "-";
    }

    return Number(value).toFixed(2);
}

function formatDate(dateString) {
    if (!dateString) {
        return "-";
    }

    const date = new Date(dateString);
    return date.toLocaleDateString("da-DK");
}

function formatDiscipline(discipline) {
    switch (discipline) {
        case "SHOT_PUT":
            return "Kugle";
        case "DISCUS":
            return "Diskos";
        case "HAMMER":
            return "Hammer";
        case "JAVELIN":
            return "Spyd";
        case "WEIGHT_THROW":
            return "Vægt";
        default:
            return discipline;
    }
}

function PrCard({ title, value }) {
    return (
        <div
            style={{
                backgroundColor: "white",
                padding: "16px",
                borderRadius: "8px",
                boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
                textAlign: "center"
            }}
        >
            <div style={{ fontSize: "14px", color: "#666" }}>
                {title}
            </div>

            <div
                style={{
                    fontSize: "22px",
                    fontWeight: "bold",
                    marginTop: "6px"
                }}
            >
                {value ? value.toFixed(2) : "-"}
            </div>
        </div>
    );
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

const inputStyle = {
    marginLeft: "6px",
    padding: "8px 10px",
    borderRadius: "6px",
    border: "1px solid #ccc",
};