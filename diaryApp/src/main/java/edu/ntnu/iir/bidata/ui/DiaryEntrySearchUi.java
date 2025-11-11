package edu.ntnu.iir.bidata.ui;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryEntry;
import edu.ntnu.iir.bidata.models.Page;
import edu.ntnu.iir.bidata.registers.DiaryEntryRegister;
import edu.ntnu.iir.bidata.ui.data.EntrySearchResult;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.*;
import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.showCommandDescriptions;
import static java.lang.System.*;

public class DiaryEntrySearchUi {

    private final DiaryEntryRegister diaryRegister;
    private final FindAuthorUi findAuthorUi;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DiaryEntrySearchUi(DiaryEntryRegister diaryRegister, FindAuthorUi findAuthorUi) {
        this.diaryRegister = diaryRegister;
        this.findAuthorUi = findAuthorUi;
    }


    public enum ECommand {
        UP("U"),
        DOWN("D"),
        BACK("B"),
        SELECT("E"),
        INVALID("");

        private final String code;

        ECommand(String code) { this.code = code; }

        public static ECommand fromString(String input) {
            if (input == null) return INVALID;
            return switch (input.toUpperCase()) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "B" -> BACK;
                case "E" -> SELECT;
                default -> INVALID;
            };
        }

        public int move(int current, int max) {
            return switch (this) {
                case UP -> Math.max(0, current - 1);
                case DOWN -> Math.min(max - 1, current + 1);
                default -> current;
            };
        }
    }

    public enum ESearchOption {
        BY_AUTHOR(0),
        BY_WORD(1),
        BY_DATE_RANGE(2),
        BY_CREATED_DATE(3),
        BY_CHANGED_DATE(4),
        BACK(5);

        private final int index;

        ESearchOption(int index) { this.index = index; }

        public int getIndex() { return index; }

        public static ESearchOption fromIndex(int index) {
            return switch (index) {
                case 0 -> BY_AUTHOR;
                case 1 -> BY_WORD;
                case 2 -> BY_DATE_RANGE;
                case 3 -> BY_CREATED_DATE;
                case 4 -> BY_CHANGED_DATE;
                case 5 -> BACK;
                default -> BACK;
            };
        }
    }

    public Map<DiaryEntry, EntrySearchResult> selectDiaryEntry(BufferedReader reader) {
        List<String> options = List.of(
                "Search by author",
                "Search by word",
                "Search by date range",
                "Search by creation date",
                "Search by changed date",
                "Back"
        );

        int index = 0;
        boolean exit = false;

        while (!exit) {
            clearScreen();
            showSectionTitle("SEARCH DIARY ENTRIES");
            showSearchMenuOptions(options, index);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions, STD_NUMBER_OF_COLUMNS, STD_COLUMN_WIDTH);

            ECommand command = ECommand.fromString(readCommand(reader));

            switch (command) {
                case UP, DOWN -> index = navigateIndex(command, index, options.size());
                case BACK -> exit = true;
                case SELECT -> {
                    ESearchOption option = ESearchOption.fromIndex(index);
                    DiaryEntry entry = getEntryByMenuChoice(option, reader);
                    if (entry == null) return null;

                    Object dataToReturn = null;
                    if (option == ESearchOption.BY_WORD) {
                        dataToReturn = searchWord;
                    }

                    Map<DiaryEntry, EntrySearchResult> entryMap = new HashMap<>();
                    entryMap.put(entry, new EntrySearchResult(option, dataToReturn));
                    return entryMap;
                }
                case INVALID -> out.println("Invalid command.");
            }
        }
        return null;
    }

    private String searchWord;

    private void showSearchMenuOptions(List<String> options, int selectedIndex) {
        options.forEach(option -> {
            int index = options.indexOf(option);
            String prefix = (index == selectedIndex)
                    ? BLUE_BG + BLACK_TEXT + "[ " + option + " ]" + DEFAULT
                    : "  " + option;
            out.println(prefix);
        });
    }

    private int navigateIndex(ECommand command, int currentIndex, int optionCount) {
        return switch (command) {
            case UP -> Math.max(0, currentIndex - 1);
            case DOWN -> Math.min(optionCount - 1, currentIndex + 1);
            default -> currentIndex;
        };
    }

    private DiaryEntry getEntryByMenuChoice(ESearchOption option, BufferedReader reader) {
        return switch (option) {
            case BY_AUTHOR -> getEntryByAuthor(reader);
            case BY_WORD -> getEntryByWord(reader);
            case BY_DATE_RANGE -> getEntryByDateRange(reader);
            case BY_CREATED_DATE -> getEntryByCreatedDate(reader);
            case BY_CHANGED_DATE -> getEntryByChangedDate(reader);
            case BACK -> null;
        };
    }

    private DiaryEntry getEntryByAuthor(BufferedReader reader) {
        Author selected = findAuthorUi.selectAuthorInteractive(reader);
        if (selected == null) return null;

        int entryCount = diaryRegister.getNumberOfEntries(selected);
        if (entryCount == 0) {
            out.println("No entries for " + selected.getName());
            readCommand(reader);
            return null;
        }

        List<DiaryEntry> entries = diaryRegister.getDiaryEntriesByAuthor(selected);

        String title = "ENTRIES FOR " + selected.getName();
        String subtitle = "Number of entries: " + entryCount;

        return getEntryFromMenuChoice(reader, entries, title, subtitle, false);
    }

    private DiaryEntry getEntryByWord(BufferedReader reader) {
        searchWord = readNonBlankInput(reader, "Enter search word> ").trim().toLowerCase();

        List<DiaryEntry> entries = diaryRegister.searchForWord(searchWord, 50);
        if (entries.isEmpty()) {
            out.println("No matches found for word: " + searchWord);
            readCommand(reader);
            return null;
        }

        Map<DiaryEntry, String> pageTitles = new HashMap<>();
        for (DiaryEntry entry : entries) {
            Map<Integer, Page> matchingPages = entry.getPagesContainingWord(searchWord);
            String firstPageTitle = matchingPages.isEmpty() ? "" : matchingPages.values().iterator().next().getTitle();
            pageTitles.put(entry, firstPageTitle);
        }

        return getEntryFromMenuChoice(reader, entries, pageTitles, "WORD SEARCH: \"" + searchWord + "\"");
    }

    private DiaryEntry getEntryByDateRange(BufferedReader reader) {
        LocalDate start = readDate(reader, "Enter start date (yyyy-MM-dd)> ");
        LocalDate end = readDate(reader, "Enter end date (yyyy-MM-dd)> ");
        if (start == null || end == null || end.isBefore(start)) {
            out.println("Invalid date range.");
            readCommand(reader);
            return null;
        }

        Map<Author, List<DiaryEntry>> results = diaryRegister.getEntriesCreatedOrChangedBetweenGroupedByAuthor(start, end);
        List<DiaryEntry> allEntries = new ArrayList<>();
        results.values().forEach(allEntries::addAll);

        if (allEntries.isEmpty()) {
            out.println("No entries found.");
            readCommand(reader);
            return null;
        }
        return getEntryFromMenuChoice(reader, allEntries, "ENTRIES BETWEEN " + start + " - " + end, null, true);
    }

    private DiaryEntry getEntryByCreatedDate(BufferedReader reader) {
        LocalDate date = readDate(reader, "Enter creation date (yyyy-MM-dd)> ");
        if (date == null) return null;

        Map<Author, List<DiaryEntry>> results = diaryRegister.getEntriesCreatedAtDateGroupedByAuthor(date);
        List<DiaryEntry> allEntries = new ArrayList<>();
        results.values().forEach(allEntries::addAll);

        if (allEntries.isEmpty()) {
            out.println("No entries found for that creation date.");
            readCommand(reader);
            return null;
        }
        return getEntryFromMenuChoice(reader, allEntries, "ENTRIES CREATED ON " + date, null, true);
    }

    private DiaryEntry getEntryByChangedDate(BufferedReader reader) {
        LocalDate date = readDate(reader, "Enter changed date (yyyy-MM-dd)> ");
        if (date == null) return null;

        Map<Author, List<DiaryEntry>> results = diaryRegister.getEntriesChangedAtDateGroupedByAuthor(date);
        List<DiaryEntry> allEntries = new ArrayList<>();
        results.values().forEach(allEntries::addAll);

        if (allEntries.isEmpty()) {
            out.println("No entries found for that changed date.");
            readCommand(reader);
            return null;
        }
        return getEntryFromMenuChoice(reader, allEntries, "ENTRIES CHANGED ON " + date, null, true);
    }

    private DiaryEntry getEntryFromMenuChoice(BufferedReader reader, List<DiaryEntry> entries,
                                              String title, String subtitle, boolean showAuthor) {
        int selectedIndex = 0;
        boolean exit = false;

        while (!exit) {
            clearScreen();
            showSectionTitle(title);
            if (subtitle != null) {
                out.println(subtitle + "\n");
            }

            //Gets wrong without keeping order with linkedHashmap,
            //normal hashmap makes it wrong.
            Map<String, Integer> headers = new LinkedHashMap<>();

            if (showAuthor) {
                headers.put("AUTHOR", 35);
            }
            headers.put("TITLE", 30);
            headers.put("PAGES", 6);
            headers.put("CREATED", 16);
            headers.put("LAST CHANGED", 16);

            showEntryTableHeaders(headers);
            showEntryTableContent(entries, selectedIndex, showAuthor,headers.values().stream().toList(),null);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions, STD_NUMBER_OF_COLUMNS, STD_COLUMN_WIDTH);

            ECommand command = ECommand.fromString(readCommand(reader));
            switch (command) {
                case UP, DOWN -> selectedIndex = command.move(selectedIndex, entries.size());
                case BACK -> exit = true;
                case SELECT -> { return entries.get(selectedIndex); }
                case INVALID -> out.println("Invalid command.");
            }
        }
        return null;
    }

    private DiaryEntry getEntryFromMenuChoice(BufferedReader reader,
                                              List<DiaryEntry> entries,
                                              Map<DiaryEntry, String> entryToPageTitle,
                                              String title) {
        int selectedIndex = 0;
        boolean exit = false;

        while (!exit) {
            clearScreen();
            showSectionTitle(title);
            //Gets wrong without keeping order with linkedHashmap,
            //normal hashmap makes it wrong.
            Map<String, Integer> headers = new LinkedHashMap<>();
            headers.put("AUTHOR", 35);
            headers.put("TITLE", 30);
            headers.put("PAGE TITLE", 30);
            headers.put("CREATED", 16);
            headers.put("LAST CHANGED", 16);

            showEntryTableHeaders(headers);
            showEntryTableContent(entries, selectedIndex,true,headers.values().stream().toList(),entryToPageTitle);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions, STD_NUMBER_OF_COLUMNS, STD_COLUMN_WIDTH);

            ECommand command = ECommand.fromString(readCommand(reader));
            switch (command) {
                case UP, DOWN -> selectedIndex = command.move(selectedIndex, entries.size());
                case BACK -> exit = true;
                case SELECT -> { return entries.get(selectedIndex); }
                case INVALID -> out.println("Invalid command.");
            }
        }
        return null;
    }

    private void showEntryTableHeaders(Map<String, Integer> header) {
        StringBuilder headerText = new StringBuilder();

        header.forEach((text, width) ->
                headerText.append(padText(text, width)).append("  ")
        );

        out.println(UNDERLINE + headerText + DEFAULT);
    }

    private void showEntryTableContent(List<DiaryEntry> entries,
                                       int selectedIndex,
                                       boolean showAuthor,
                                       List<Integer> widths,
                                       Map<DiaryEntry, String> pageTitles) {

        IntStream.range(0, entries.size()).forEach(index -> {
            DiaryEntry entry = entries.get(index);
            String pageTitleOrNmbPages = (pageTitles != null)
                    ? pageTitles.getOrDefault(entry, String.valueOf(entry.getNmbPages()))
                    : String.valueOf(entry.getNmbPages());

            int offset = showAuthor ? 1 : 0;
            int startWidth = showAuthor ? widths.getFirst() : 0;

            StringBuilder rowText = new StringBuilder();
            if (showAuthor) rowText.append(padText(entry.getAuthor().getName(), startWidth)).append("  ");

            rowText.append(padText(entry.getEntryTitle(), widths.get(offset))).append("  ")
                    .append(padText(pageTitleOrNmbPages, widths.get(offset + 1))).append("  ")
                    .append(padText(entry.getTimeCreated().format(dtf), widths.get(offset + 2))).append("  ")
                    .append(padText(entry.getTimeChanged().format(dtf), widths.get(offset + 3)));

            if (index == selectedIndex)
                out.println(DARK_BLUE_BG + BLACK_TEXT + rowText + DEFAULT);
            else
                out.println(rowText);
        });
    }


}
