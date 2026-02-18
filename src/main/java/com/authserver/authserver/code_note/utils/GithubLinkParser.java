package com.authserver.authserver.code_note.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.authserver.authserver.code_note.entry.CodeNoteEntry;

@Component
public class GithubLinkParser {

    private static final RestTemplate restTemplate = new RestTemplate();

    private List<String> fetchPrCommits(String owner, String repo, int prNumber) {

        String url = "https://api.github.com/repos/" +
                owner + "/" + repo + "/pulls/" + prNumber + "/commits";

        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

        List<Map<String, Object>> commits = response.getBody();

        return commits.stream()
                .map(c -> (String) c.get("sha"))
                .toList();
    }

    private List<Map<String, Object>> fetchCommitFiles(
            String owner,
            String repo,
            String sha) {

        String url = "https://api.github.com/repos/" +
                owner + "/" + repo + "/commits/" + sha;

        Map response = restTemplate.getForObject(url, Map.class);

        return (List<Map<String, Object>>) response.get("files");
    }

    private List<CodeNoteEntry> parseDiffBlocks(
            String patch,
            String filePath,
            String sha,
            String owner,
            String repo,
            Long notebookId) {

        List<CodeNoteEntry> result = new ArrayList<>();

        if (patch == null || patch.isBlank()) {
            return result;
        }

        String[] blocks = patch.split("\n@@");

        for (String block : blocks) {

            String[] lines = block.split("\n");
            if (lines.length == 0)
                continue;

            String header = lines[0];

            String[] plusParts = header.split("\\+");
            if (plusParts.length < 2)
                continue;

            String newPart = plusParts[1].split(" ")[0];

            int currentLine = Integer.parseInt(newPart.split(",")[0]);

            Integer start = null;

            for (int i = 1; i < lines.length; i++) {

                String line = lines[i];

                if (line.startsWith("+") && !line.startsWith("+++")) {

                    if (start == null) {
                        start = currentLine;
                    }
                    currentLine++;

                } else {

                    if (start != null) {

                        CodeNoteEntry entry = new CodeNoteEntry();

                        entry.setNotebookId(notebookId);
                        entry.setTitle(filePath);

                        String link = "https://github.com/" + owner + "/" + repo +
                                "/blob/" + sha + "/" + filePath +
                                "#L" + start + "-L" + (currentLine - 1);

                        entry.setPermanentLink(link);

                        entry.setDescription(
                                "Added lines " + start + " to " + (currentLine - 1));

                        result.add(entry);
                        start = null;
                    }

                    if (!line.startsWith("-")) {
                        currentLine++;
                    }
                }
            }

            if (start != null) {

                CodeNoteEntry entry = new CodeNoteEntry();

                entry.setNotebookId(notebookId);
                entry.setTitle(filePath);

                String link = "https://github.com/" + owner + "/" + repo +
                        "/blob/" + sha + "/" + filePath +
                        "#L" + start + "-L" + (currentLine - 1);

                entry.setPermanentLink(link);

                entry.setDescription(
                        "Added lines " + start + " to " + (currentLine - 1));

                result.add(entry);
            }
        }

        return result;
    }

    public List<CodeNoteEntry> processPR(String prLink, Long notebookId) {

        String[] splits = prLink.split("/");
        String owner = splits[3];
        String repo = splits[4];
        Integer prNumber = Integer.parseInt(splits[6]);

        List<CodeNoteEntry> newEntries = new ArrayList<>();

        List<String> commits = fetchPrCommits(owner, repo, prNumber);

        for (String sha : commits) {

            List<Map<String, Object>> files = fetchCommitFiles(owner, repo, sha);

            for (Map<String, Object> file : files) {

                String filename = (String) file.get("filename");
                String patch = (String) file.get("patch");

                if (patch == null)
                    continue;

                List<CodeNoteEntry> entries = parseDiffBlocks(patch, filename, sha, owner, repo, notebookId);

                newEntries.addAll(entries);
            }
        }

        return newEntries;
    }
}
