import { Routes, Route } from "react-router-dom";
import OverviewPage from "./pages/OverviewPage";
import AthleteResultsPage from "./pages/AthleteResultsPage";

export default function App() {
    return (
        <Routes>
            <Route path="/" element={<OverviewPage />} />
            <Route path="/athlete/:athleteId" element={<AthleteResultsPage />} />
        </Routes>
    );
}