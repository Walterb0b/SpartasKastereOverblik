const API_BASE_URL = import.meta.env.VITE_API_URL;

async function apiFetch(path, options = {}) {
    const headers = {
        ...(options.headers || {}),
    };

    const response = await fetch(`${API_BASE_URL}${path}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        const text = await response.text();
        throw new Error(`Request fejlede (${response.status}): ${text}`);
    }

    return response;
}

async function apiFetchWithBasicAuth(path, auth, options = {}) {
    const headers = {
        ...(options.headers || {}),
    };

    if (auth) {
        headers.Authorization = "Basic " + btoa(`${auth.username}:${auth.password}`);
    }

    return apiFetch(path, {
        ...options,
        headers,
    });
}

export async function fetchPrOverview() {
    const response = await apiFetch("/api/overview/pr");
    return response.json();
}

export async function fetchSbOverview(year) {
    const response = await apiFetch(`/api/overview/sb/${year}`);
    return response.json();
}

export async function importAllAthletes(auth) {
    await apiFetchWithBasicAuth("/api/import/all", auth, {
        method: "POST",
    });
}

export async function fetchAthleteResults(athleteId) {
    const response = await apiFetch(`/api/results/athlete/${athleteId}`);
    return response.json();
}