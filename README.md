## Om projektet

Dette er et personligt projekt udviklet med det formål at skabe et struktureret overblik over en træningsgruppe af kastere fra atletikklubben Sparta.

Systemet indsamler, organiserer og præsenterer atletdata, herunder resultater hentet automatisk fra eksterne kilder. Projektet demonstrerer integration mellem flere systemer samt håndtering af real-world data.

---

## Teknologier

### Backend
- Java (Spring Boot)
- REST API

### Frontend
- React

### Infrastruktur & Deployment
- Vercel (frontend deployment)
- Render (backend deployment)
- Supabase (database)

---
## Webscraper & Dataindsamling

Projektet inkluderer en custom webscraper, der automatisk indsamler resultatdata fra statletik.eu — en central database for atletikresultater i Danmark.

### Funktionalitet

- Henter resultater for specifikke atleter
- Parser HTML-data til struktureret format
- Ekstraherer relevante metrics (disciplin, resultater, datoer)
- Opdaterer databasen automatisk med ny data

### Formål

Scraperen eliminerer behovet for manuel dataindtastning og sikrer, at systemet altid arbejder med opdaterede resultater.

---

### Teknisk tilgang

- HTTP requests til eksterne sider
- Parsing af HTML med Jsoup
- Databehandling og transformation
- Persistens via backend til Supabase

---

### Udfordringer & overvejelser

- Håndtering af ændringer i HTML-struktur (scraper-breaks)
- Rate limiting og respekt for eksterne services
- Datakvalitet og validering
- Fejlhåndtering ved manglende eller inkonsistent data

---

## Se projektet
- https://spartas-kastere-overblik.vercel.app/
- Bemærk at der kan være lang spin-up time på siden
