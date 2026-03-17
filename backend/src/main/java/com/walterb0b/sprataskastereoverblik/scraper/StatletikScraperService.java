package com.walterb0b.sprataskastereoverblik.scraper;

import com.walterb0b.sprataskastereoverblik.model.Athlete;
import com.walterb0b.sprataskastereoverblik.model.Gender;
import com.walterb0b.sprataskastereoverblik.model.Result;
import com.walterb0b.sprataskastereoverblik.repository.ResultRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatletikScraperService {

    private final StatletikParser parser;
    private final ResultRepository resultRepository;

    public StatletikScraperService(StatletikParser parser, ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
        this.parser = parser;
    }

    public void importResultsForAthlete(Athlete athlete) {
        try {
            String sex = athlete.getGender() == Gender.MALE ? "1" : "2";
            String url = "https://www.statletik.eu/db/atden.php?Sex=" + sex + "&ID=" + athlete.getStatletikId();

            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(20000)
                    .get();

            List<ParsedResult> parsedResults = parser.parse(document, athlete);

            for (ParsedResult parsed : parsedResults) {
                boolean exists = resultRepository.existsByAthleteAndDisciplineAndResultDateAndResultValue(
                        athlete,
                        parsed.discipline(),
                        parsed.resultDate(),
                        parsed.resultValue()
                );

                if (exists) {
                    continue;
                }

                Result result = new Result();
                result.setAthlete(athlete);
                result.setDiscipline(parsed.discipline());
                result.setResultValue(parsed.resultValue());
                result.setResultDate(parsed.resultDate());
                result.setCompetition(parsed.competition());
                result.setLocation(parsed.location());

                resultRepository.save(result);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import results for athlete " + athlete.getId(), e);
        }
    }

}
