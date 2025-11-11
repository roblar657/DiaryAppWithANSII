package edu.ntnu.iir.bidata.ui;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryEntry;
import edu.ntnu.iir.bidata.models.Page;
import edu.ntnu.iir.bidata.registers.AuthorRegister;
import edu.ntnu.iir.bidata.registers.DiaryEntryRegister;
import edu.ntnu.iir.bidata.ui.data.EntrySearchResult;

import java.io.BufferedReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.*;
import static java.lang.System.*;

public class DiaryEntryUi {

    private final AuthorRegister authorRegister;
    private final DiaryEntryRegister diaryEntryRegister;
    private final FindAuthorUi findAuthorUi;
    private final DiaryEntrySearchUi diaryEntrySearchUi;

    public DiaryEntryUi(AuthorRegister authorRegister, DiaryEntryRegister diaryEntryRegister) {
        this.authorRegister = authorRegister;
        this.diaryEntryRegister = diaryEntryRegister;
        this.findAuthorUi = new FindAuthorUi(authorRegister);
        this.diaryEntrySearchUi = new DiaryEntrySearchUi(diaryEntryRegister, findAuthorUi);
    }

    public enum EAskNameCombination {
        FIRST_ONLY(0, true, false, false),
        LAST_ONLY(1, false, true, false),
        NICK_ONLY(2, false, false, true),
        FIRST_LAST(3, true, true, false),
        FIRST_NICK(4, true, false, true),
        LAST_NICK(5, false, true, true),
        FIRST_LAST_NICK(6, true, true, true);

        private final int index;
        private final boolean askFirst;
        private final boolean askLast;
        private final boolean askNickname;

        EAskNameCombination(int index, boolean askFirst, boolean askLast, boolean askNickname) {
            this.index = index;
            this.askFirst = askFirst;
            this.askLast = askLast;
            this.askNickname = askNickname;
        }

        public boolean askFirst() { return askFirst; }
        public boolean askLast() { return askLast; }
        public boolean askNickname() { return askNickname; }

        public static EAskNameCombination fromIndex(int index) {
            return switch (index) {
                case 0 -> FIRST_ONLY;
                case 1 -> LAST_ONLY;
                case 2 -> NICK_ONLY;
                case 3 -> FIRST_LAST;
                case 4 -> FIRST_NICK;
                case 5 -> LAST_NICK;
                case 6 -> FIRST_LAST_NICK;
                default -> FIRST_ONLY;
            };
        }
    }

    public enum ECommand {
        UP_PAGE("U"),
        DOWN_PAGE("D"),
        BACK("B"),
        ENTER("E"),
        PREV_PART_PAGE("P"),
        NEXT_PART_PAGE("N"),
        EDIT_TITLE_EDITMODE("T"),
        EDIT_CONTENT_EDITMODE("C"),
        REMOVE_PAGE_EDITMODE("R"),
        EDIT_MAIN_TITLE_EDITMODE("M"),
        ADD_PAGE_EDITMODE("A"),
        ERASE_ENTRY("E"),
        INVALID("");

        private final String code;

        ECommand(String code) { this.code = code; }

        public static ECommand fromString(String input, boolean editMode) {
            if (input == null) return INVALID;
            return switch (input.toUpperCase()) {
                case "U" -> UP_PAGE;
                case "D" -> DOWN_PAGE;
                case "B" -> BACK;
                case "E" -> editMode ? ERASE_ENTRY : ENTER;
                case "T" -> editMode ? EDIT_TITLE_EDITMODE : INVALID;
                case "P" -> PREV_PART_PAGE;
                case "N" -> NEXT_PART_PAGE;
                case "C" -> editMode ? EDIT_CONTENT_EDITMODE : INVALID;
                case "R" -> editMode ? REMOVE_PAGE_EDITMODE : INVALID;
                case "M" -> editMode ? EDIT_MAIN_TITLE_EDITMODE : INVALID;
                case "A" -> editMode ? ADD_PAGE_EDITMODE : INVALID;
                default -> INVALID;
            };
        }

        public void navigate(DiaryEntry entry, NavigationState nav) {
            switch (this) {
                case UP_PAGE -> nav.currentPageIndex = Math.max(1, nav.currentPageIndex - 1);
                case DOWN_PAGE -> nav.currentPageIndex = Math.min(entry.getNmbPages(), nav.currentPageIndex + 1);
                case PREV_PART_PAGE -> nav.offset = Math.max(0, nav.offset - NavigationState.WORDS_PER_PART);
                case NEXT_PART_PAGE -> {
                    int maxOffset = Math.max(0, nav.numberOfWords - NavigationState.WORDS_PER_PART);
                    nav.offset = Math.min(maxOffset, nav.offset + NavigationState.WORDS_PER_PART);
                }
                default -> {}
            }
        }
    }

    public enum EMenuChoice {
        CREATE_NEW_AUTHOR(0),
        USE_EXISTING_AUTHOR(1),
        BACK_TO_MAIN_MENU(2);

        private final int index;

        EMenuChoice(int index) { this.index = index; }

        public static EMenuChoice fromIndex(int index) {
            return switch (index) {
                case 0 -> CREATE_NEW_AUTHOR;
                case 1 -> USE_EXISTING_AUTHOR;
                default -> BACK_TO_MAIN_MENU;
            };
        }
    }

    public static class NavigationState {
        int currentPageIndex;
        int offset;
        static final int WORDS_PER_PART = STD_WORDS_PER_PART;
        int numberOfWords;

        NavigationState(int currentPageIndex, int offset, int numberOfWords) {
            this.currentPageIndex = currentPageIndex;
            this.offset = offset;
            this.numberOfWords = numberOfWords;
        }
    }

    public void navigateCreateMenu(BufferedReader reader) {
        List<String> options = List.of("Create new author", "Use existing author", "Back to main menu");
        int currentSelect = 0;
        boolean exit = false;

        while (!exit) {
            clearScreen();
            showSectionTitle("CREATE DIARY ENTRY");
            showMenuOptions(options, currentSelect);

            List<List<String>> commandDescriptions = constructCommandDescriptions(false);
            showCommandDescriptions(commandDescriptions, STD_NUMBER_OF_COLUMNS, STD_COLUMN_WIDTH);

            ECommand command = ECommand.fromString(readCommand(reader), false);
            currentSelect = doMenuNavigation(command, currentSelect, options.size());
            exit = doMenuSelection(command, currentSelect, reader);
        }
    }

    private void showMenuOptions(List<String> options, int selectedIndex) {
        IntStream.range(0, options.size()).forEach(optionIndex -> {
            if (optionIndex == selectedIndex)
                out.println(BLUE_BG + BLACK_TEXT + "[ " + options.get(optionIndex) + " ]" + DEFAULT);
            else
                out.println("  " + options.get(optionIndex));
        });
    }

    private int doMenuNavigation(ECommand command, int currentIndex, int optionCount) {
        return switch (command) {
            case UP_PAGE -> Math.max(0, currentIndex - 1);
            case DOWN_PAGE -> Math.min(optionCount - 1, currentIndex + 1);
            default -> currentIndex;
        };
    }

    private boolean doMenuSelection(ECommand command, int selectedIndex, BufferedReader reader) {
        if (command == ECommand.BACK) return true;
        if (command != ECommand.ENTER) return false;

        EMenuChoice choice = EMenuChoice.fromIndex(selectedIndex);
        switch (choice) {
            case CREATE_NEW_AUTHOR -> { createWithNewAuthor(reader); return false; }
            case USE_EXISTING_AUTHOR -> { createWithExistingAuthor(reader); return false; }
            case BACK_TO_MAIN_MENU -> {
                return true;
            }
        }
        return false;
    }

    private void createWithNewAuthor(BufferedReader reader) {
        List<String> options = List.of(
                "First name only", "Last name only", "Nickname only",
                "First + Last", "First + Nickname", "Last + Nickname", "First + Last + Nickname"
        );

        int currentSelected = 0;
        boolean exitMenu = false;

        while (!exitMenu) {
            clearScreen();
            showSectionTitle("Choose which fields to set");
            showNewAuthorMenuOptions(options, currentSelected);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions, STD_NUMBER_OF_COLUMNS, STD_COLUMN_WIDTH);

            ECommand command = ECommand.fromString(readCommand(reader), false);
            currentSelected = doNavigateCreateNewAuthorMenu(command, currentSelected, options.size());

            switch (command) {
                case BACK, ENTER -> exitMenu = true;
                case UP_PAGE, DOWN_PAGE -> {  }
                case INVALID -> out.println("Invalid command.");
                default -> {return;}
            }
        }

        EAskNameCombination askNameCombination = EAskNameCombination.fromIndex(currentSelected);

        String firstName = askNameCombination.askFirst() ? readNonBlankInput(reader, "Enter first name> ") : "";
        String lastName = askNameCombination.askLast() ? readNonBlankInput(reader, "Enter last name> ") : "";
        String nickname = askNameCombination.askNickname() ? readNonBlankInput(reader, "Enter nickname> ") : "";

        Author newAuthor = new Author(firstName, lastName, nickname);
        authorRegister.addAuthor(newAuthor);
        out.println("\nAuthor created: " + newAuthor.getName());
        readCommand(reader);

        createDiaryEntry(reader, newAuthor);
    }

    private void showNewAuthorMenuOptions(List<String> options, int selectedIndex) {
        IntStream.range(0, options.size()).forEach(i -> {
            if (i == selectedIndex)
                out.println(BLUE_BG + BLACK_TEXT + "[ " + options.get(i) + " ]" + DEFAULT);
            else
                out.println("  " + options.get(i));
        });
    }

    private int doNavigateCreateNewAuthorMenu(ECommand command, int currentIndex, int optionCount) {
        return switch (command) {
            case UP_PAGE -> Math.max(0, currentIndex - 1);
            case DOWN_PAGE -> Math.min(optionCount - 1, currentIndex + 1);
            default -> currentIndex;
        };
    }

    private void createWithExistingAuthor(BufferedReader reader) {
        if (authorRegister.isEmpty()) {
            out.println("No authors available. Please create a new author first.");
            readCommand(reader);
            return;
        }
        Author selected = findAuthorUi.selectAuthorInteractive(reader);
        if (selected != null) createDiaryEntry(reader, selected);
    }

    private void createDiaryEntry(BufferedReader reader, Author author) {
        clearScreen();
        out.println(BLUE_BG + BLACK_TEXT + "     CREATE DIARY ENTRY     " + DEFAULT + "\n");
        out.println("Author: " + DARK_BLUE_TEXT + author.getName() + DEFAULT + "\n");

        String title = readNonBlankInput(reader, "Enter diary entry title> ");

        int maxWordsPerPage = STD_MAX_AMOUNT_WORDS;
        boolean isFinished = false;
        while (!isFinished) {
            String input = readNonBlankInput(reader,
                    "Enter maximum number of words per page " +
                            "(or write " + BLUE_BG + BLACK_TEXT +
                            "[E/e]" + DEFAULT + " for default of " +
                            STD_MAX_AMOUNT_WORDS + ")> ");
            try {
                if(input.equalsIgnoreCase("E")){
                    isFinished = true;
                }
                else{
                    int value = Integer.parseInt(input.trim());
                    if (value > 0) {
                        maxWordsPerPage = value;
                        isFinished = true;
                    } else {
                        out.println("Number must be positive");
                    }
                }
            } catch (NumberFormatException e) {
                out.println("Invalid number.");
            }
        }

        DiaryEntry entry = new DiaryEntry(author, maxWordsPerPage, title);

        String pageTitle = readNonBlankInput(reader, "Enter first page title> ");
        String pageContent = "";
        isFinished = false;
        while(!isFinished) {
            pageContent = readNonBlankInput(reader, "Enter first page content> ");
            int wordCount = pageContent.split(" ").length;
            if(wordCount <= maxWordsPerPage)
                isFinished = true;
            else
                out.println("Page content exceeds maximum words (" + maxWordsPerPage + "). Please shorten it.");
        }

        entry.addPage(pageTitle, pageContent);
        diaryEntryRegister.addDiaryEntry(entry);

        navigateDiaryEntry(reader, entry, true, 1);
    }

    public void navigateToEntryFromSearch(BufferedReader reader, Map<DiaryEntry,EntrySearchResult> entries, Boolean editMode) {
        if (entries == null || entries.isEmpty()) return;

        DiaryEntry entry = entries.keySet().iterator().next();
        EntrySearchResult result = entries.get(entry);

        if (result.option() == DiaryEntrySearchUi.ESearchOption.BY_WORD) {
            String word = result.data().toString();
            Map<Integer, Page> matchingPages = entry.getPagesContainingWord(word);
            Map.Entry<Integer, Page> firstEntry = matchingPages.entrySet()
                    .stream()
                    .findFirst()
                    .orElse(null);

            int pageIndex = firstEntry != null ? firstEntry.getKey() : 1;
            navigateDiaryEntry(reader, entry, editMode, pageIndex);
        } else {
            navigateDiaryEntry(reader, entry, editMode);
        }
    }

    public void updateDiaryEntryMenu(BufferedReader reader) {
        Map<DiaryEntry, EntrySearchResult> entries = diaryEntrySearchUi.selectDiaryEntry(reader);
        navigateToEntryFromSearch(reader, entries, true);
    }

    public void navigateDiaryEntry(BufferedReader reader, DiaryEntry entry, boolean editMode) {
        navigateDiaryEntry(reader, entry, editMode, 1);
    }

    public void navigateDiaryEntry(BufferedReader reader, DiaryEntry entry, boolean editMode, int startPageIndex) {
        if (entry.getNmbPages() == 0) {
            out.println("No pages in this diary entry.");
            readCommand(reader);
            return;
        }

        int offset = 0;
        List<String> words = List.of(entry.getPageText(startPageIndex).split(" "));
        int numberOfWords = words.size();
        NavigationState navigation = new NavigationState(startPageIndex, offset, numberOfWords);

        boolean exitDiary = false;
        while (!exitDiary) {
            clearScreen();
            words = List.of(entry.getPageText(navigation.currentPageIndex).split(" "));
            navigation.numberOfWords = words.size();

            showSectionTitle(editMode ? "EDIT DIARY" : "MY DIARY");
            showEntryInformation(entry);
            showPageTitles(entry, navigation);
            showPageContent(entry, navigation);

            List<List<String>> commandDescriptions = constructCommandDescriptions(editMode);
            showCommandDescriptions(commandDescriptions, STD_NUMBER_OF_COLUMNS, STD_COLUMN_WIDTH);

            String commandStr = readCommand(reader);
            ECommand command = ECommand.fromString(commandStr, editMode);

            switch (command) {
                case UP_PAGE, DOWN_PAGE, PREV_PART_PAGE, NEXT_PART_PAGE -> command.navigate(entry, navigation);
                case BACK -> exitDiary = true;
                default -> {
                    if (doMenuDecision(reader, entry, navigation, command, editMode)) {
                        exitDiary = true;
                    }
                }
            }
        }
    }

    private void showEntryInformation(DiaryEntry entry){
        out.println("Author      : " + DARK_BLUE_TEXT + entry.getAuthor().getName() + DEFAULT);
        out.println("Created     : " + DARK_BLUE_TEXT + entry.getTimeChanged().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + DEFAULT);
        out.println("Last changed: " + DARK_BLUE_TEXT + entry.getTimeChanged().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + DEFAULT);
        out.println("Entry title : " + DARK_BLUE_TEXT + entry.getEntryTitle() + DEFAULT + "\n");
    }

    private void showPageTitles(DiaryEntry entry, NavigationState navigation) {
        int finalCurrentPageIndex = navigation.currentPageIndex;
        IntStream.rangeClosed(1, entry.getNmbPages()).forEach(pageIndex -> {
            int dotsCount = Math.max(1, 52 - entry.getPageTitle(pageIndex).length());
            String dots = ".".repeat(dotsCount);
            if (pageIndex == finalCurrentPageIndex)
                out.println(DARK_BLUE_BG + BLACK_TEXT + "[" + entry.getPageTitle(pageIndex) + " " + dots + " pp." + pageIndex + "]" + DEFAULT);
            else
                out.println(entry.getPageTitle(pageIndex) + " " + dots + " pp." + pageIndex + DEFAULT);
        });
    }

    private void showPageContent(DiaryEntry entry, NavigationState navigation) {
        List<String> words = List.of(entry.getPageText(navigation.currentPageIndex).split(" "));
        navigation.numberOfWords = words.size();

        int totalParts = Math.ceilDiv(words.size(), NavigationState.WORDS_PER_PART);
        int currentPart = navigation.offset / NavigationState.WORDS_PER_PART + 1;
        int start = navigation.offset;
        int end = Math.min(navigation.offset + NavigationState.WORDS_PER_PART, words.size());

        out.println("\n" + DARK_BLUE_TEXT + entry.getPageTitle(navigation.currentPageIndex) +
                GREEN_TEXT + " [Part " + currentPart + " of " + totalParts + "]" + DEFAULT + "\n");

        IntStream.range(start, end).forEach(wordIndex -> {
            out.print(words.get(wordIndex) + " ");
            if ((wordIndex - start + 1) % 12 == 0) out.println();
        });

        out.println("\n");
    }

    private List<List<String>> constructCommandDescriptions(boolean editMode) {
        List<List<String>> baseCommands = List.of(
                List.of("U", "Up page"),
                List.of("B", "Back"),
                List.of("D", "Down page"),
                List.of("P", "Prev part"),
                List.of("N", "Next part")
        );

        List<List<String>> editCommands = List.of(
                List.of("T", "Edit title"),
                List.of("C", "Edit content"),
                List.of("R", "Remove page"),
                List.of("M", "Edit main title"),
                List.of("A", "Add page"),
                List.of("E", "Erase entry")
        );

        List<List<String>> commands = new ArrayList<>(baseCommands);
        if (editMode) commands.addAll(editCommands);

        return commands;
    }

    private boolean doMenuDecision(BufferedReader reader, DiaryEntry entry, NavigationState navigation, ECommand command, boolean editMode) {
        switch (command) {
            case EDIT_TITLE_EDITMODE -> entry.setPageTitle(navigation.currentPageIndex, readNonBlankInput(reader, "Enter new page title> "));
            case EDIT_CONTENT_EDITMODE -> {
                boolean finished = false;
                while(!finished) {
                    String text = readNonBlankInput(reader, "Enter new page content> ");
                    if(text.split(" ").length <= entry.getMaxWordsPerPage()) {
                        entry.setPageText(navigation.currentPageIndex,text );
                        finished = true;
                    } else {
                        out.println("Content exceeds maximum length of " + entry.getMaxWordsPerPage());
                    }
                }
            }
            case REMOVE_PAGE_EDITMODE -> removePage(reader, entry, navigation);
            case ERASE_ENTRY -> {
                diaryEntryRegister.removeDiaryEntry(entry.getAuthor(), entry.getEntryTitle());
                out.println("Diary deleted.");
                readCommand(reader);
                return true;
            }
            case EDIT_MAIN_TITLE_EDITMODE -> entry.setEntryTitle(readNonBlankInput(reader, "Enter new diary main title> "));
            case ADD_PAGE_EDITMODE -> {
                String pageTitle = readNonBlankInput(reader, "Enter page title> ");
                String pageContent = readNonBlankInput(reader, "Enter page content> ");
                entry.addPage(pageTitle, pageContent);
            }
            case INVALID -> out.println("Invalid command.");
            default -> {}
        }
        return false;
    }

    private void removePage(BufferedReader reader, DiaryEntry entry, NavigationState navigation) {
        entry.removePage(navigation.currentPageIndex);
        if (entry.getNmbPages() == 0) {
            out.println("You removed the last page. Creating a new empty page.");

            String newPageTitle = readNonBlankInput(reader, "Enter title for the new page> ");
            String newPageContent = readNonBlankInput(reader, "Enter content for the new page> ");

            entry.addPage(newPageTitle, newPageContent);
            navigation.currentPageIndex = 1;
        } else if (navigation.currentPageIndex > entry.getNmbPages()) {
            navigation.currentPageIndex = entry.getNmbPages();
        }
    }
}
