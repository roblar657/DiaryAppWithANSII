package edu.ntnu.iir.bidata.ui;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.registers.AuthorRegister;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.*;
import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.showCommandDescriptions;
import static java.lang.System.*;

public class UpdateNameUi {

    private enum ECommand {
        UP("U"), DOWN("D"), ENTER("E"), BACK("B"), INVALID("");

        private final String code;

        ECommand(String code) { this.code = code; }

        public static ECommand fromString(String input) {
            if (input == null) return INVALID;
            return switch (input.toUpperCase()) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "E" -> ENTER;
                case "B" -> BACK;
                default -> INVALID;
            };
        }
    }

    private enum EAuthorName {
        FIRST_NAME("First name"), LAST_NAME("Last name"), NICKNAME("Nickname");

        private final String name;


        EAuthorName(String name) { this.name = name; }

        public static EAuthorName fromIndex(int index) {
            return switch (index) {
                case 0 -> FIRST_NAME;
                case 1 -> LAST_NAME;
                case 2 -> NICKNAME;
                default -> throw new IllegalArgumentException("Invalid index: " + index);
            };
        }

        @Override
        public String toString() { return name; }
    }

    public void updateNameMenu(BufferedReader reader, AuthorRegister authorRegister) {
        List<Author> authorsList = new ArrayList<>();
        authorRegister.getAllAuthors().forEachRemaining(authorsList::add);
        if (authorsList.isEmpty()) {
            out.println("No authors available to update.");
            readCommand(reader);
            return;
        }

        int selectedIndex = 0;
        boolean backToMain = false;

        while (!backToMain) {
            clearScreen();
            showSectionTitle("UPDATE NAME");
            out.println("Select author to update:\n");
            showListOfAuthors(authorsList, selectedIndex);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions,STD_NUMBER_OF_COLUMNS,STD_COLUMN_WIDTH);


            ECommand command = ECommand.fromString(readCommand(reader));
            switch (command) {
                case UP -> selectedIndex = Math.max(0, selectedIndex - 1);
                case DOWN -> selectedIndex = Math.min(authorsList.size() - 1, selectedIndex + 1);
                case BACK -> backToMain = true;
                case ENTER -> updateAuthorName(reader, authorsList.get(selectedIndex));
                case INVALID -> { return; }
            }
        }
    }



    private void showListOfAuthors(List<Author> authorsList, int selectedIndex) {
        authorsList.forEach(author -> {
            int index = authorsList.indexOf(author);
            String line = (index == selectedIndex)
                    ? DARK_BLUE_BG + BLACK_TEXT + author.getName() + DEFAULT
                    : author.getName();
            out.println(line);
        });
    }

    private void updateAuthorName(BufferedReader reader, Author author) {
        boolean backToUpdateMenu = false;
        int currentSelected = 0;
        List<EAuthorName> names = new ArrayList<>(Arrays.asList(EAuthorName.values()));

        while (!backToUpdateMenu) {
            clearScreen();
            showSectionTitle("UPDATE AUTHOR");
            out.println("Author: " + author.getName() + "\n");
            showAuthorNames(names, currentSelected);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions,STD_NUMBER_OF_COLUMNS,STD_COLUMN_WIDTH);


            ECommand command = ECommand.fromString(readCommand(reader));
            switch (command) {
                case UP -> currentSelected = Math.max(0, currentSelected - 1);
                case DOWN -> currentSelected = Math.min(names.size() - 1, currentSelected + 1);
                case BACK -> backToUpdateMenu = true;
                case ENTER -> editAuthorName(reader, author, names.get(currentSelected));
                case INVALID -> { return; }
            }
        }
    }


    private void showAuthorNames(List<EAuthorName> names, int selectedIndex) {
        IntStream.range(0, names.size()).forEach(index -> {
            EAuthorName name = names.get(index);
            String output = (index == selectedIndex)
                    ? BLUE_BG + BLACK_TEXT + "[ " + name + " ]" + DEFAULT
                    : "  " + name;
            out.println(output);
        });
    }



    private void editAuthorName(BufferedReader reader, Author author, EAuthorName name) {
        String newName = readNonBlankInput(reader, "Enter new " + name + "> ");
        switch (name) {
            case FIRST_NAME -> author.setFirstName(newName);
            case LAST_NAME -> author.setLastName(newName);
            case NICKNAME -> author.setNickname(newName);
        }
        out.println("Updated successfully!");
        readCommand(reader);
    }


}
