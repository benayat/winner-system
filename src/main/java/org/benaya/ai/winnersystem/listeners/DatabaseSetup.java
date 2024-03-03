package org.benaya.ai.winnersystem.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.benaya.ai.winnersystem.model.Team;
import org.benaya.ai.winnersystem.service.TeamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "database.init", havingValue = "true")
@Slf4j
public class DatabaseSetup {
    @Value("${database.teams_data}")
    private String csvFilePath;
    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    private final TeamService teamService;

    @EventListener
    public void loadTeamsData(ContextRefreshedEvent event) {
        // Load teams data from csv file
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .builder()
                     .setHeader().setSkipHeaderRecord(true)
                     .setTrim(true)
                     .setIgnoreEmptyLines(true)
                     .setIgnoreHeaderCase(true)
                     .build())) {
            List<Team> tempTeamsList = new ArrayList<>();
            for (CSVRecord csvRecord : csvParser) {
                Team client = Team.builder()
                        .name(csvRecord.get("Name"))
                        .goals(0)
                        .points(0)
                        .skillLevel(randomGenerator.nextInt(1, 10))
                        .injuries(randomGenerator.nextInt(0, 3))
                        .build();
                tempTeamsList.add(client);
                teamService.saveAll(tempTeamsList);
            }
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

}
