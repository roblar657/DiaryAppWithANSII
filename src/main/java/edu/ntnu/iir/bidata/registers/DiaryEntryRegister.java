package edu.ntnu.iir.bidata.registers;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryEntry;

import java.time.LocalDate;
import java.util.*;

public class DiaryEntryRegister {

    private final Map<Author, List<DiaryEntry>> authorEntriesMap;

    public DiaryEntryRegister() {
        this.authorEntriesMap = new HashMap<>();
    }

    public Map<Author, List<DiaryEntry>> getEntriesCreatedAtDateGroupedByAuthor(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");

        Map<Author, List<DiaryEntry>> result = new HashMap<>();
        for (Map.Entry<Author, List<DiaryEntry>> entry : authorEntriesMap.entrySet()) {
            Author author = entry.getKey();
            if (author.getLastTimeCreated().toLocalDate().isEqual(date)) {
                result.put(author, new ArrayList<>(entry.getValue()));
                continue;
            }

            List<DiaryEntry> filtered = entry.getValue().stream()
                    .filter(diary -> diary.getTimeCreated().toLocalDate().isEqual(date))
                    .toList();

            if (!filtered.isEmpty()) {
                result.put(author, filtered);
            }
        }
        return result;
    }

    public Map<Author, List<DiaryEntry>> getEntriesChangedAtDateGroupedByAuthor(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");

        Map<Author, List<DiaryEntry>> result = new HashMap<>();
        for (Map.Entry<Author, List<DiaryEntry>> entry : authorEntriesMap.entrySet()) {
            Author author = entry.getKey();
            if (author.getLastTimeChanged().toLocalDate().isEqual(date)) {
                result.put(author, new ArrayList<>(entry.getValue()));
                continue;
            }

            List<DiaryEntry> filtered = entry.getValue().stream()
                    .filter(d -> d.getTimeChanged().toLocalDate().isEqual(date))
                    .toList();

            if (!filtered.isEmpty()) {
                result.put(author, filtered);
            }
        }
        return result;
    }

    public int getNumberOfEntries(Author author) {
        if (author == null) throw new IllegalArgumentException("Author cannot be null");
        return authorEntriesMap.getOrDefault(author, Collections.emptyList()).size();
    }

    public List<DiaryEntry> getDiaryEntriesByAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("Author cannot be null");
        return authorEntriesMap.getOrDefault(author, new ArrayList<>());
    }

    public Map<Author, List<DiaryEntry>> getEntriesCreatedBetweenGroupedByAuthor(LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end cannot be null");

        Map<Author, List<DiaryEntry>> result = new HashMap<>();
        for (Map.Entry<Author, List<DiaryEntry>> entry : authorEntriesMap.entrySet()) {
            Author author = entry.getKey();
            LocalDate lastDateAuthorCreated = author.getLastTimeCreated().toLocalDate();

            if (!lastDateAuthorCreated.isBefore(start) && !lastDateAuthorCreated.isAfter(end)) {
                result.put(author, new ArrayList<>(entry.getValue()));
                continue;
            }

            List<DiaryEntry> filtered = entry.getValue().stream()
                    .filter(diary -> {
                        LocalDate createdDate = diary.getTimeCreated().toLocalDate();
                        return !createdDate.isBefore(start) && !createdDate.isAfter(end);
                    })
                    .toList();

            if (!filtered.isEmpty()) {
                result.put(author, filtered);
            }
        }
        return result;
    }

    public Map<Author, List<DiaryEntry>> getEntriesChangedBetweenGroupedByAuthor(LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end cannot be null");

        Map<Author, List<DiaryEntry>> result = new HashMap<>();
        for (Map.Entry<Author, List<DiaryEntry>> entry : authorEntriesMap.entrySet()) {
            Author author = entry.getKey();
            LocalDate lastDateAuthorChanged = author.getLastTimeChanged().toLocalDate();

            if (!lastDateAuthorChanged.isBefore(start) && !lastDateAuthorChanged.isAfter(end)) {
                result.put(author, new ArrayList<>(entry.getValue()));
                continue;
            }

            List<DiaryEntry> filtered = entry.getValue().stream()
                    .filter(diary -> {
                        LocalDate changedDate = diary.getTimeChanged().toLocalDate();
                        return !changedDate.isBefore(start) && !changedDate.isAfter(end);
                    })
                    .toList();

            if (!filtered.isEmpty()) {
                result.put(author, filtered);
            }
        }
        return result;
    }

    public Map<Author, List<DiaryEntry>> getEntriesCreatedOrChangedBetweenGroupedByAuthor(LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end cannot be null");

        Map<Author, List<DiaryEntry>> result = new HashMap<>();
        for (Map.Entry<Author, List<DiaryEntry>> entry : authorEntriesMap.entrySet()) {
            Author author = entry.getKey();
            LocalDate lastDateAuthorCreated = author.getLastTimeCreated().toLocalDate();
            LocalDate lastDateAuthorChanged = author.getLastTimeChanged().toLocalDate();

            if ((!lastDateAuthorCreated.isBefore(start) && !lastDateAuthorCreated.isAfter(end)) ||
                    (!lastDateAuthorChanged.isBefore(start) && !lastDateAuthorChanged.isAfter(end))) {
                result.put(author, new ArrayList<>(entry.getValue()));
                continue;
            }

            List<DiaryEntry> filtered = entry.getValue().stream()
                    .filter(diary -> {
                        LocalDate createdDate = diary.getTimeCreated().toLocalDate();
                        LocalDate changedDate = diary.getTimeChanged().toLocalDate();
                        return (!createdDate.isBefore(start) && !createdDate.isAfter(end)) ||
                                (!changedDate.isBefore(start) && !changedDate.isAfter(end));
                    })
                    .toList();

            if (!filtered.isEmpty()) {
                result.put(author, filtered);
            }
        }
        return result;
    }

    public void addAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("Author cannot be null");
        authorEntriesMap.putIfAbsent(author, new ArrayList<>());
    }

    public void addDiaryEntry(DiaryEntry entry) {
        if (entry == null) throw new IllegalArgumentException("Diary entry cannot be null");

        Author author = entry.getAuthor();
        authorEntriesMap.putIfAbsent(author, new ArrayList<>());
        authorEntriesMap.get(author).add(entry);
    }

    public Optional<DiaryEntry> findDiaryEntryFromAuthorByTitle(Author author, String entryTitle) {
        if (author == null) throw new IllegalArgumentException("Author cannot be null");
        if (entryTitle == null || entryTitle.isBlank()) throw new IllegalArgumentException("Entry title cannot be null or blank");

        List<DiaryEntry> entries = authorEntriesMap.get(author);
        if (entries == null) return Optional.empty();

        return entries.stream()
                .filter(entry -> entry.getEntryTitle().equalsIgnoreCase(entryTitle))
                .findFirst();
    }

    public List<DiaryEntry> searchForWord(String word, int limit) {
        if (word == null || word.isBlank()) throw new IllegalArgumentException("word cannot be null or blank");

        List<DiaryEntry> matches = new ArrayList<>();

        List<Author> sortedAuthors = authorEntriesMap.keySet().stream()
                .sorted((author1, author2) -> Long.compare(
                        author2.getWordCount().getOrDefault(word, 0L),
                        author1.getWordCount().getOrDefault(word, 0L)
                ))
                .toList();

        int found = 0;

        for (Author author : sortedAuthors) {
            long authorCount = author.getWordCount().getOrDefault(word, 0L);
            if (authorCount == 0) continue;

            List<DiaryEntry> entries = authorEntriesMap.getOrDefault(author, Collections.emptyList());

            for (DiaryEntry entry : entries) {
                long entryCount = entry.getWordCount().getOrDefault(word.toLowerCase(), 0L);
                if (entryCount > 0) {
                    matches.add(entry);
                    found++;
                    if (found >= limit) return matches;
                }
            }
        }

        return matches;
    }

    public Optional<DiaryEntry> removeDiaryEntry(Author author, String entryTitle) {
        if (author == null) throw new IllegalArgumentException("Author cannot be null");
        if (entryTitle == null || entryTitle.isBlank()) throw new IllegalArgumentException("Entry title cannot be null or empty");

        List<DiaryEntry> entries = authorEntriesMap.get(author);
        if (entries == null || entries.isEmpty()) return Optional.empty();

        Iterator<DiaryEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            DiaryEntry entry = iterator.next();
            if (entry.getEntryTitle().equalsIgnoreCase(entryTitle)) {
                iterator.remove();
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }
}
