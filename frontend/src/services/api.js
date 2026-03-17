async function apiFetch(url, auth, options = {}) {
    const headers = {
        ...(options.headers || {}),
    };

    if (auth) {
        headers.Authorization = "Basic " + btoa(`${auth.username}:${auth.password}`);
    }

    const response = await fetch(url, {
        ...options,
        headers,
    });

    if (!response.ok) {
        const text = await response.text();
        throw new Error(`Request fejlede (${response.status}): ${text}`);
    }

    return response;
}

export async function fetchPrOverview(auth) {
    const response = await apiFetch("http://localhost:8080/api/overview/pr", auth);
    return response.json();
}

export async function fetchSbOverview(year, auth) {
    const response = await apiFetch(`http://localhost:8080/api/overview/sb/${year}`, auth);
    return response.json();
}

export async function importAllAthletes(auth) {
    await apiFetch("http://localhost:8080/api/import/all", auth, {
        method: "POST",
    });
}

export async function fetchAthleteResults(athleteId, auth) {
    const response = await apiFetch(
        `http://localhost:8080/api/results/athlete/${athleteId}`,
        auth
    );
    return response.json();
}