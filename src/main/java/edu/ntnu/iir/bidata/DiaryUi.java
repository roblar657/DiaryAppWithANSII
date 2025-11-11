package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryEntry;
import edu.ntnu.iir.bidata.registers.AuthorRegister;
import edu.ntnu.iir.bidata.registers.DiaryEntryRegister;
import edu.ntnu.iir.bidata.ui.DiaryEntryUi;
import edu.ntnu.iir.bidata.ui.FindAuthorUi;
import edu.ntnu.iir.bidata.ui.UpdateNameUi;
import edu.ntnu.iir.bidata.ui.DiaryEntrySearchUi;
import edu.ntnu.iir.bidata.ui.data.EntrySearchResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.*;
import static java.lang.System.*;

public class DiaryUi {

    private BufferedReader reader;
    private final List<String> menuItems = new ArrayList<>();
    private final AuthorRegister authorRegister = new AuthorRegister();
    private final DiaryEntryRegister diaryEntryRegister = new DiaryEntryRegister();

    private UpdateNameUi updateNameUi;
    private FindAuthorUi findAuthorUi;
    private DiaryEntryUi diaryEntryUi;
    private DiaryEntrySearchUi diaryEntrySearchUi;

    private enum EMenuCommand {
        UP, DOWN, ENTER, BACK, INVALID;

        public static EMenuCommand fromString(String input) {
            return switch (input.toUpperCase()) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "E" -> ENTER;
                case "B" -> BACK;
                default -> INVALID;
            };
        }
    }

    private List<List<String>> constructCommands() {
        return List.of(
                List.of("U", "Up"),
                List.of("D", "Down"),
                List.of("E", "Enter"),
                List.of("B", "Back")
        );
    }

    private enum EMenuOption {
        CREATE_DIARY_ENTRY,
        UPDATE_DIARY_ENTRY,
        UPDATE_NAME,
        FIND_AUTHORS,
        SEARCH_DIARY_ENTRIES,
        EXIT;

        public static EMenuOption fromIndex(int index) {
            return switch (index) {
                case 0 -> CREATE_DIARY_ENTRY;
                case 1 -> UPDATE_DIARY_ENTRY;
                case 2 -> UPDATE_NAME;
                case 3 -> FIND_AUTHORS;
                case 4 -> SEARCH_DIARY_ENTRIES;
                case 5 -> EXIT;
                default -> throw new IllegalArgumentException("Invalid menu index: " + index);
            };
        }
    }

    public void init() {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.menuItems.addAll(Arrays.asList(
                "Create diary entry",
                "Update diary entry",
                "Update name",
                "Find authors",
                "Search diary entries",
                "Exit"
        ));

        this.diaryEntryUi = new DiaryEntryUi(authorRegister, diaryEntryRegister);
        this.updateNameUi = new UpdateNameUi();
        this.findAuthorUi = new FindAuthorUi(authorRegister);
        this.diaryEntrySearchUi = new DiaryEntrySearchUi(diaryEntryRegister, findAuthorUi);

        authorRegister.addAuthor("Robert", "te", "Nero");
        authorRegister.addAuthor("Tara", "Nordmann", "Nero");
        authorRegister.addAuthor("Tero", "Tatta", "Nero");
        authorRegister.addAuthor("Don", "Vivvi", "Doan");

        Author robert = authorRegister.getAuthor("Robert te(Nero)").orElseThrow();
        Author tara = authorRegister.getAuthor("Tara Nordmann(Nero)").orElseThrow();
        Author tero = authorRegister.getAuthor("Tero Tatta(Nero)").orElseThrow();
        Author don = authorRegister.getAuthor("Don Vivvi(Doan)").orElseThrow();

        DiaryEntry entry1 = new DiaryEntry(robert, STD_MAX_AMOUNT_WORDS, "Det");
        entry1.addPage("Det", "Jeg gikk en tur en rar stie");
        entry1.addPage("var", "Det skjedde mye spennende");
        entry1.addPage("sjokolade", "Det skjedde mye spennende");
        entry1.addPage("tre", "Det skjedde kunne spennende2");
        entry1.addPage("var", "Det skjedde mye spennende");

        diaryEntryRegister.addDiaryEntry(entry1);

        DiaryEntry entry2 = new DiaryEntry(tara, STD_MAX_AMOUNT_WORDS, "en gang");
        entry2.addPage("en", "Bakke-eventyr startet");
        entry2.addPage("gang", "Jeg møtte en rar venn");
        diaryEntryRegister.addDiaryEntry(entry2);

        DiaryEntry entry3 = new DiaryEntry(tero, STD_MAX_AMOUNT_WORDS, "morro");
        entry3.addPage("noe", "Nair var dagens høydepunkt");
        entry3.addPage("som", "Vi spilte spill");
        diaryEntryRegister.addDiaryEntry(entry3);

        DiaryEntry entry4 = new DiaryEntry(don, STD_MAX_AMOUNT_WORDS, "Eventyr");
        entry4.addPage("hoppet", "Startet reisen tidlig");
        entry4.addPage("ned i sjøen", "Oppdaget nye steder");
        diaryEntryRegister.addDiaryEntry(entry4);
    }

    public void start() throws IOException {
        navigateMenu();
    }

    private void showMenu(List<String> menuItems, int currentIndex) {
        IntStream.range(0, menuItems.size())
                .forEach(index -> {
                    String item = menuItems.get(index);
                    String line = (index == currentIndex)
                            ? BLUE_BG + BLACK_TEXT + "[ " + item + " ]" + DEFAULT
                            : "  " + item + "  ";
                    out.println(line);
                });
    }

    private void navigateMenu() throws IOException {
        int currentIndex = 0;
        boolean exitMenu = false;
        try {
            while (!exitMenu) {
                clearScreen();
                showSectionTitle("MAIN MENU");

                showMenu(this.menuItems, currentIndex);

                List<List<String>> commandDescriptions = constructCommands();
                showCommandDescriptions(commandDescriptions, STD_NUMBER_OF_COLUMNS, STD_COLUMN_WIDTH);

                EMenuCommand command = EMenuCommand.fromString(readCommand(reader));


                switch (command) {
                    case UP -> currentIndex = Math.max(0, currentIndex - 1);
                    case DOWN -> currentIndex = Math.min(this.menuItems.size() - 1, currentIndex + 1);
                    case ENTER -> {
                        EMenuOption option = EMenuOption.fromIndex(currentIndex);
                        switch (option) {
                            case CREATE_DIARY_ENTRY -> diaryEntryUi.navigateCreateMenu(reader);
                            case UPDATE_DIARY_ENTRY -> diaryEntryUi.updateDiaryEntryMenu(reader);
                            case UPDATE_NAME -> updateNameUi.updateNameMenu(reader, authorRegister);
                            case FIND_AUTHORS -> findAuthorUi.selectAuthorInteractive(reader);
                            case SEARCH_DIARY_ENTRIES -> {
                                Map<DiaryEntry, EntrySearchResult> entries = diaryEntrySearchUi.selectDiaryEntry(reader);
                                diaryEntryUi.navigateToEntryFromSearch(reader, entries,false);
                            }

                            case EXIT -> {
                                exitMenu = true;
                            }
                        }
                    }
                    case BACK, INVALID -> {
                    }
                }
            }
        }
        catch (Exception e){
            out.println("Program crashed. Try restarting the application.");

        }
        finally {
            this.reader.close();
        }
    }
}

