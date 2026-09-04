/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.dbbackup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Seun Owa
 */
@Service
public class DatabaseBackupService {

    private final DatabaseBackupProperties backupProperties;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private static final DateTimeFormatter FILE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public DatabaseBackupService(DatabaseBackupProperties backupProperties) {
        this.backupProperties = backupProperties;
    }

    public BackupResult backup() throws Exception {

        DatabaseConnectionInfo db = parseDatasourceUrl(datasourceUrl);

        Path backupDirectory = Paths.get(
                backupProperties.getDirectory()
        );

        Files.createDirectories(backupDirectory);

        String filename = String.format(
                "storrity_%s.dump",
                LocalDateTime.now().format(FILE_DATE_FORMAT)
        );

        Path backupFile = backupDirectory.resolve(filename);

        List<String> command = new ArrayList<>();

        command.add(backupProperties.getPgDump());

        command.add("-h");
        command.add(db.host());

        command.add("-p");
        command.add(String.valueOf(db.port()));

        command.add("-U");
        command.add(username);

        command.add("-d");
        command.add(db.database());

        command.add("-F");
        command.add("c");

        command.add("-f");
        command.add(backupFile.toString());

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        // Don't put password directly in command arguments.
        processBuilder.environment().put(
                "PGPASSWORD",
                password
        );

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {

            // Remove incomplete backup if pg_dump failed
            Files.deleteIfExists(backupFile);

            throw new IllegalStateException(
                    "Database backup failed. pg_dump output: "
                    + output
            );
        }

        return new BackupResult(
                true,
                filename,
                backupFile.toString(),
                Files.size(backupFile)
        );
    }

    private DatabaseConnectionInfo parseDatasourceUrl(
            String url
    ) {

        // jdbc:postgresql://localhost:5432/storrity

        String prefix = "jdbc:postgresql://";

        if (!url.startsWith(prefix)) {
            throw new IllegalArgumentException(
                    "Unsupported PostgreSQL datasource URL: " + url
            );
        }

        String connection = url.substring(prefix.length());

        int slashIndex = connection.indexOf('/');

        if (slashIndex < 0) {
            throw new IllegalArgumentException(
                    "Invalid PostgreSQL datasource URL: " + url
            );
        }

        String hostPort = connection.substring(0, slashIndex);
        String database = connection.substring(slashIndex + 1);

        // Remove query parameters
        int queryIndex = database.indexOf('?');

        if (queryIndex >= 0) {
            database = database.substring(0, queryIndex);
        }

        String host;
        int port = 5432;

        int colonIndex = hostPort.lastIndexOf(':');

        if (colonIndex >= 0) {
            host = hostPort.substring(0, colonIndex);
            port = Integer.parseInt(
                    hostPort.substring(colonIndex + 1)
            );
        } else {
            host = hostPort;
        }

        return new DatabaseConnectionInfo(
                host,
                port,
                database
        );
    }

    private record DatabaseConnectionInfo(
            String host,
            int port,
            String database
    ) {
    }

    public record BackupResult(
            boolean successful,
            String filename,
            String path,
            long size
    ) {
    }
}