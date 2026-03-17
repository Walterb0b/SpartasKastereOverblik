import { useState } from "react";
import { Routes, Route } from "react-router-dom";
import OverviewPage from "./pages/OverviewPage";
import AthleteResultsPage from "./pages/AthleteResultsPage";
import LoginForm from "./components/LoginForm";
import { fetchPrOverview } from "./services/api";

export default function App() {
    const [auth, setAuth] = useState(null);
    const [loginError, setLoginError] = useState("");

    async function handleLogin(credentials) {
        try {
            await fetchPrOverview(credentials);
            setAuth(credentials);
            setLoginError("");
        } catch (err) {
            setLoginError("Forkert brugernavn eller password");
        }
    }

    if (!auth) {
        return <LoginForm onLogin={handleLogin} error={loginError} />;
    }

    return (
        <Routes>
            <Route path="/" element={<OverviewPage auth={auth} />} />
            <Route path="/athlete/:athleteId" element={<AthleteResultsPage auth={auth} />} />
        </Routes>
    );
}