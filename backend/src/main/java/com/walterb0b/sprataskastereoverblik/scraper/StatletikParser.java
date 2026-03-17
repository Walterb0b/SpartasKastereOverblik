package com.walterb0b.sprataskastereoverblik.scraper;

import com.walterb0b.sprataskastereoverblik.model.Athlete;
import com.walterb0b.sprataskastereoverblik.model.Discipline;
import com.walterb0b.sprataskastereoverblik.model.Gender;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StatletikParser {

    private static final Pattern RESULT_PATTERN = Pattern.compile("^\\d+[.,]\\d+$");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.\\d{2}");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yy");

    public List<ParsedResult> parse(Document document, Athlete athlete) {
        List<ParsedResult> results = new ArrayList<>();
        Elements rows = document.select("tr");

        Discipline currentDiscipline = null;

        for (Element row : rows) {

            if (isEventHeaderRow(row)) {
                String headerText = normalize(row.text());
                currentDiscipline = mapSeniorDiscipline(headerText, athlete.getGender());
                continue;
            }

            if (currentDiscipline == null) {
                continue;
            }

            if (isSeriesRow(row)) {
                continue;
            }

            ParsedResult parsed = parseResultRow(row, currentDiscipline);
            if (parsed != null) {
                results.add(parsed);
            }
        }

        return deduplicate(results);
    }

    private boolean isEventHeaderRow(Element row) {
        return row.hasClass("event");
    }

    private boolean isSeriesRow(Element row) {
        return !row.select("table").isEmpty();
    }

    private ParsedResult parseResultRow(Element row, Discipline discipline) {
        if (!isValidMainResultRow(row)) {
            return null;
        }

        Elements cells = row.select("> td");
        Element resultCell = row.selectFirst("td.pr-0");
        if (resultCell == null) {
            return null;
        }

        String resultText = extractOwnText(resultCell);
        if (!isValidResult(resultText)) {
            return null;
        }

        Double resultValue = parseDouble(resultText);
        if (resultValue == null) {
            return null;
        }

        LocalDate resultDate = extractDateFromRow(cells);
        if (resultDate == null) {
            return null;
        }

        String competition = extractCompetition(row);
        String location = extractLocation(row);

        // hvis begge mangler, er det næsten helt sikkert en summary/dublet-række
        if (competition == null && location == null) {
            return null;
        }

        return new ParsedResult(
                discipline,
                resultValue,
                resultDate,
                competition,
                location,
                resultDate.getYear(),
                false
        );
    }

    private boolean isValidMainResultRow(Element row) {
        if (row == null) {
            return false;
        }

        if (isSeriesRow(row)) {
            return false;
        }

        Element resultCell = row.selectFirst("td.pr-0");
        if (resultCell == null) {
            return false;
        }

        Elements cells = row.select("> td");
        if (cells.size() < 6) {
            return false;
        }

        LocalDate date = extractDateFromRow(cells);
        if (date == null) {
            return false;
        }

        boolean hasLocationCell = row.selectFirst("td.plats") != null;
        boolean hasNonEmptyLink = row.select("td a").stream()
                .map(Element::text)
                .map(this::normalize)
                .anyMatch(text -> !text.isBlank());

        return hasLocationCell || hasNonEmptyLink;
    }

    private String extractOwnText(Element cell) {
        String text = normalize(cell.ownText());

        if (!text.isBlank()) {
            return text;
        }

        String fallback = normalize(cell.text());
        Matcher matcher = RESULT_PATTERN.matcher(fallback);
        if (matcher.find()) {
            return matcher.group();
        }

        return fallback;
    }

    private LocalDate extractDateFromRow(Elements cells) {
        for (int i = cells.size() - 1; i >= 0; i--) {
            String text = normalize(cells.get(i).text());
            if (DATE_PATTERN.matcher(text).matches()) {
                try {
                    return LocalDate.parse(text, DATE_FORMATTER);
                } catch (Exception ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    private String extractCompetition(Element row) {
        Elements links = row.select("td a");
        List<String> nonEmptyLinks = new ArrayList<>();

        for (Element link : links) {
            String text = normalize(link.text());
            if (!text.isBlank()) {
                nonEmptyLinks.add(text);
            }
        }

        if (nonEmptyLinks.isEmpty()) {
            return null;
        }

        // hvis der er mindst 2 links, er første typisk competition og sidste typisk location
        if (nonEmptyLinks.size() >= 2) {
            String first = nonEmptyLinks.get(0);
            String last = nonEmptyLinks.get(nonEmptyLinks.size() - 1);

            if (!first.equals(last)) {
                return first;
            }
        }

        // fallback: mobile cell som "DM, Hvidovre"
        Element mobileCell = row.selectFirst("td.d-xl-none.d-lg-none a");
        if (mobileCell != null) {
            String text = normalize(mobileCell.text());
            if (text.contains(",")) {
                return text.split(",", 2)[0].trim();
            }
        }

        return null;
    }

    private String extractLocation(Element row) {
        Element platsCell = row.selectFirst("td.plats a");
        if (platsCell != null) {
            String text = normalize(platsCell.text());
            if (!text.isBlank()) {
                return text;
            }
        }

        Element mobileCell = row.selectFirst("td.d-xl-none.d-lg-none a");
        if (mobileCell != null) {
            String text = normalize(mobileCell.text());
            if (!text.isBlank()) {
                if (text.contains(",")) {
                    return text.split(",", 2)[1].trim();
                }
                return text;
            }
        }

        Elements links = row.select("td a");
        List<String> nonEmptyLinks = new ArrayList<>();

        for (Element link : links) {
            String text = normalize(link.text());
            if (!text.isBlank()) {
                nonEmptyLinks.add(text);
            }
        }

        if (!nonEmptyLinks.isEmpty()) {
            return nonEmptyLinks.get(nonEmptyLinks.size() - 1);
        }

        return null;
    }

    private boolean looksLikeLocation(String text) {
        return !text.isBlank() && Character.isUpperCase(text.charAt(0)) && !text.equalsIgnoreCase("DM");
    }

    private boolean isValidResult(String text) {
        return RESULT_PATTERN.matcher(text).matches();
    }

    private Double parseDouble(String text) {
        try {
            return Double.parseDouble(text.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.replace('\u00A0', ' ').trim().replaceAll("\\s+", " ");
    }

    private Discipline mapSeniorDiscipline(String text, Gender gender) {
        String t = normalize(text).toLowerCase(Locale.ROOT);

        if (gender == Gender.MALE) {
            if (t.contains("kuglestød") && t.contains("7.26")) return Discipline.SHOT_PUT;
            if (t.contains("diskoskast") && (t.contains("2.0") || t.contains("2 kg"))) return Discipline.DISCUS;
            if (t.contains("hammerkast") && t.contains("7.26")) return Discipline.HAMMER;
            if (t.contains("spydkast") && t.contains("800")) return Discipline.JAVELIN;
            if ((t.contains("vægtkast") || t.contains("vaegtkast")) &&
                    (t.contains("15.88") || t.contains("15.89"))) return Discipline.WEIGHT_THROW;
        }

        if (gender == Gender.FEMALE) {
            if (t.contains("kuglestød") && t.contains("4")) return Discipline.SHOT_PUT;
            if (t.contains("diskoskast") && (t.contains("1.0") || t.contains("1 kg"))) return Discipline.DISCUS;
            if (t.contains("hammerkast") && t.contains("4")) return Discipline.HAMMER;
            if (t.contains("spydkast") && t.contains("600")) return Discipline.JAVELIN;
            if ((t.contains("vægtkast") || t.contains("vaegtkast")) && t.contains("9.08")) return Discipline.WEIGHT_THROW;
        }

        return null;
    }

    private List<ParsedResult> deduplicate(List<ParsedResult> input) {
        Map<String, ParsedResult> unique = new LinkedHashMap<>();

        for (ParsedResult result : input) {
            String key = result.discipline() + "|" +
                    result.resultDate() + "|" +
                    result.resultValue() + "|" +
                    Objects.toString(result.location(), "");

            unique.putIfAbsent(key, result);
        }

        return new ArrayList<>(unique.values());
    }

    private record MetaInfo(LocalDate date, String competition, String location, boolean indoor) {
    }
}