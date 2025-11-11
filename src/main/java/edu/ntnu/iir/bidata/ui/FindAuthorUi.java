package edu.ntnu.iir.bidata.ui;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.registers.AuthorRegister;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.*;
import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.showCommandDescriptions;
import static java.lang.System.*;

public class FindAuthorUi {

    private final AuthorRegister authorRegister;

    public FindAuthorUi(AuthorRegister authorRegister) {

        this.authorRegister = authorRegister;
    }


    private enum ECommand {
        UP("U"), DOWN("D"), ENTER("E"), BACK("B"), INVALID("");

        private final String code;

        ECommand(String code) { this.code = code; }

        public static ECommand fromString(String input) {
            if (input == null) return INVALID;
            return switch (input.toUpperCase()) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "B" -> BACK;
                case "E" -> ENTER;
                default -> INVALID;
            };
        }
    }

    private enum EYesNo {
        YES("Y"), NO("N"), INVALID("");

        private final String code;

        EYesNo(String code) { this.code = code; }

        public static EYesNo fromString(String input) {
            if (input == null) return INVALID;
            return switch (input.toUpperCase()) {
                case "Y" -> YES;
                case "N" -> NO;
                default -> INVALID;
            };
        }
    }

    private enum ENameSearch {
        FIRST_NAME("first name"),
        LAST_NAME("last name"),
        NICKNAME("nickname"),
        FIRST_LAST("first + last name"),
        FIRST_LAST_NICKNAME("first + last + nickname"),
        INVALID("");

        private final String name;

        ENameSearch(String name) { this.name = name; }

        public static ENameSearch fromString(String input) {
            if (input == null) return INVALID;
            return switch (input.toLowerCase()) {
                case "first name" -> FIRST_NAME;
                case "last name" -> LAST_NAME;
                case "nickname" -> NICKNAME;
                case "first + last name" -> FIRST_LAST;
                case "first + last + nickname" -> FIRST_LAST_NICKNAME;
                default -> INVALID;
            };
        }
    }

    public Author selectAuthorInteractive(BufferedReader reader) {
        if (authorRegister.isEmpty()) {
            out.println("No authors available.");
            readCommand(reader);
            return null;
        }

        List<ENameSearch> searchOptions = List.of(
                ENameSearch.FIRST_NAME, ENameSearch.LAST_NAME, ENameSearch.NICKNAME,
                ENameSearch.FIRST_LAST, ENameSearch.FIRST_LAST_NICKNAME
        );

        int searchIndex = 0;
        boolean backToMain = false;

        while (!backToMain) {
            clearScreen();
            showSectionTitle("FIND AUTHORS");
            out.println(DARK_BLUE_TEXT + "Search criteria:" + DEFAULT + "\n");
            showSearchOptions(searchOptions, searchIndex);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions,STD_NUMBER_OF_COLUMNS,STD_COLUMN_WIDTH);


            ECommand command = ECommand.fromString(readCommand(reader));
            if (command == ECommand.INVALID) continue;

            switch (command) {
                case UP -> searchIndex = Math.max(0, searchIndex - 1);
                case DOWN -> searchIndex = Math.min(searchOptions.size() - 1, searchIndex + 1);
                case BACK -> backToMain = true;
                case ENTER -> {
                    Author selected = orderByMenu(reader, searchOptions.get(searchIndex));
                    if (selected != null) return selected;
                }
                default -> { return null; }
            }
        }
        return null;
    }



    private void showSearchOptions(List<ENameSearch> searchOptions, int selectedIndex) {
        IntStream.range(0, searchOptions.size()).forEach(searchOptionsIndex -> {
            String optionName = searchOptions.get(searchOptionsIndex).name;
            String line = (searchOptionsIndex == selectedIndex)
                    ? BLUE_BG + BLACK_TEXT + "[ " + optionName + " ]" + DEFAULT
                    : "  " + optionName;
            out.println(line);
        });
    }



    private Author orderByMenu(BufferedReader reader, ENameSearch searchCriteria) {
        List<ENameSearch> options = List.of(
                ENameSearch.FIRST_NAME,
                ENameSearch.LAST_NAME,
                ENameSearch.NICKNAME
        );


        List<ENameSearch> orderOptions = new ArrayList<>(options);
        options.stream()
                .map(option -> option.name.split(" ")[0].toLowerCase())
                .filter(orderByName -> searchCriteria.name.toLowerCase().contains(orderByName))
                .forEach(name -> {
                    if (orderOptions.size() > 1) {
                        orderOptions.removeIf(orderByName -> orderByName.name.toLowerCase().startsWith(name));
                    }
                });


        int orderIndex = 0;
        boolean backToSearch = false;

        while (!backToSearch) {
            clearScreen();
            showSectionTitle("ORDER BY");
            out.println("Search by : " + DARK_BLUE_TEXT + searchCriteria.name + DEFAULT + "\n");
            showOrderOptions(orderOptions, orderIndex);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions,STD_NUMBER_OF_COLUMNS,STD_COLUMN_WIDTH);

            ECommand command = ECommand.fromString(readCommand(reader));
            if (command == ECommand.INVALID) continue;

            switch (command) {
                case UP -> orderIndex = Math.max(0, orderIndex - 1);
                case DOWN -> orderIndex = Math.min(orderOptions.size() - 1, orderIndex + 1);
                case BACK -> backToSearch = true;
                case ENTER -> {
                    return showAuthorList(reader, searchCriteria, orderOptions.get(orderIndex));
                }
                default ->{
                    return null;
                }
            }
        }

        return null;
    }


    private void showOrderOptions(List<ENameSearch> options, int selectedIndex) {
        IntStream.range(0, options.size()).forEach(optionIndex -> {
            if (optionIndex == selectedIndex)
                out.println(BLUE_BG + BLACK_TEXT + "[ " + options.get(optionIndex).name + " ]" + DEFAULT);
            else
                out.println("  " + options.get(optionIndex).name);
        });
    }






    private Author showAuthorList(BufferedReader reader, ENameSearch searchBy, ENameSearch orderBy) {
        List<Author> authors = new ArrayList<>();

        String firstName = null;
        String lastName = null;
        String nickname = null;

        boolean isFirstNameStartWith = false;
        boolean isLastNameStartWith = false;
        boolean isNicknameStartWith = false;

        switch (searchBy) {
            case FIRST_NAME -> {

                isFirstNameStartWith = askStartWith(reader, "first name");
                firstName = readNonBlankInput(reader, "Enter first name> ");

                authorRegister.findByFirstName(firstName, isFirstNameStartWith).forEachRemaining(authors::add);
            }
            case LAST_NAME -> {

                isLastNameStartWith = askStartWith(reader, "last name");

                lastName = readNonBlankInput(reader, "Enter last name> ");
                authorRegister.findByLastName(lastName, isLastNameStartWith).forEachRemaining(authors::add);

            }
            case NICKNAME -> {

                isNicknameStartWith = askStartWith(reader, "nickname");

                nickname = readNonBlankInput(reader, "Enter nickname> ");
                authorRegister.findByNickname(nickname, isNicknameStartWith).forEachRemaining(authors::add);
            }
            case FIRST_LAST -> {

                isFirstNameStartWith = askStartWith(reader, "first name");
                firstName = readNonBlankInput(reader, "Enter first name> ");

                isLastNameStartWith = askStartWith(reader, "last name");
                lastName = readNonBlankInput(reader, "Enter last name> ");

                authorRegister.findByFullName(firstName, lastName, isFirstNameStartWith, isLastNameStartWith)
                        .forEachRemaining(authors::add);
            }
            case FIRST_LAST_NICKNAME -> {

                isFirstNameStartWith = askStartWith(reader, "first name");
                firstName = readNonBlankInput(reader, "Enter first name> ");

                isLastNameStartWith = askStartWith(reader, "last name");
                lastName = readNonBlankInput(reader, "Enter last name> ");

                isNicknameStartWith = askStartWith(reader, "nickname");
                nickname = readNonBlankInput(reader, "Enter nickname> ");

                authorRegister.findByFullNameWithNickname(firstName, lastName, nickname,
                                isFirstNameStartWith, isLastNameStartWith, isNicknameStartWith)
                        .forEachRemaining(authors::add);
            }
            default -> {return null;}
        }

        if (authors.isEmpty()) {
            out.println("No authors found.");
            readCommand(reader);
            return null;
        }

        authors.sort(Comparator.comparing(author -> switch (orderBy) {
            case FIRST_NAME -> author.getFirstName();
            case LAST_NAME -> author.getLastName();
            case NICKNAME -> author.getNickname();
            default -> author.getName();
        }));

        return showAuthorTableSection(reader, authors, searchBy, orderBy);
    }

    private void showSectionInformation(ENameSearch searchBy, ENameSearch orderBy) {
        out.println("Search by: " + DARK_BLUE_TEXT + searchBy.name + DEFAULT);
        out.println("Order by : " + GREEN_TEXT + orderBy.name + DEFAULT + "\n");
    }

    private Author showAuthorTableSection(BufferedReader reader, List<Author> authors,
                                          ENameSearch searchBy, ENameSearch orderBy) {
        int selectedIndex = 0;
        final int COL_WIDTH = 15;
        boolean exitList = false;
        List<String> headers = List.of("FIRST NAME", "LAST NAME", "NICKNAME");

        while (!exitList) {
            clearScreen();

            showSectionTitle("RESULTS");
            showSectionInformation(searchBy,orderBy);
            showAuthorTable(authors, selectedIndex, headers, orderBy, COL_WIDTH);

            List<List<String>> commandDescriptions = constructMenuNavigationCommandDescriptions();
            showCommandDescriptions(commandDescriptions,STD_NUMBER_OF_COLUMNS,STD_COLUMN_WIDTH);


            ECommand command = ECommand.fromString(readCommand(reader));
            if (command == ECommand.INVALID) continue;

            switch (command) {
                case UP -> selectedIndex = Math.max(0, selectedIndex - 1);
                case DOWN -> selectedIndex = Math.min(authors.size() - 1, selectedIndex + 1);
                case BACK -> exitList = true;
                case ENTER -> { return authors.get(selectedIndex); }
                default -> {
                    return null;
                }
            }
        }
        return null;
    }



    private void showAuthorTable(List<Author> authors, int selectedIndex, List<String> headers,
                                 ENameSearch orderBy, int colWidth) {

        for (String header : headers) {
            String headerText = padText(header, colWidth);
            boolean isOrderBy = header.equalsIgnoreCase(orderBy.name);
            out.print(UNDERLINE + (isOrderBy ? GREEN_TEXT : "") + headerText + DEFAULT + UNDERLINE + " ");
        }
        out.println(DEFAULT);

        authors.forEach(author -> {
            List<String> columns = List.of(author.getFirstName(), author.getLastName(), author.getNickname());
            IntStream.range(0, calculateMaxLines(columns, colWidth))
                    .forEach(line -> showAuthorTableRow(
                            columns,
                            line,
                            authors.indexOf(author) == selectedIndex,
                            headers,
                            orderBy,
                            colWidth
                    ));
        });


    }

    private int calculateMaxLines(List<String> lines, int colWidth) {
        return lines.stream()
                .mapToInt(line -> Math.ceilDiv(line.length(), colWidth))
                .max()
                .orElse(1);
    }


    private void showAuthorTableRow(List<String> columns, int line, boolean isSelected,
                                    List<String> headers, ENameSearch orderBy, int colWidth) {

        IntStream.range(0, columns.size()).forEach(columnIndex -> {

            String paddedSubString = getPaddedRow(columns.get(columnIndex), line, colWidth);

            boolean isOrderColumn = headers.get(columnIndex).equalsIgnoreCase(orderBy.name);

            String textToShow;
            if (isSelected) {
                textToShow = (BLUE_BG + BLACK_TEXT + paddedSubString + DEFAULT + BLUE_BG + " " + DEFAULT);
            } else {
                if (isOrderColumn) textToShow = GREEN_TEXT + paddedSubString + DEFAULT + " ";
                else textToShow = paddedSubString + " ";
            }

            out.print(textToShow);
        });
        out.println();

    }

    private String getPaddedRow(String string, int rowNumber, int colWidth) {
        int start = rowNumber * colWidth;
        int end = Math.min(start + colWidth, string.length());
        String subString = (start >= string.length()) ? "" : string.substring(start, end);
        return padText(subString, colWidth);
    }


    private boolean askStartWith(BufferedReader reader, String name) {
        while (true) {
            String input = readNonBlankInput(reader, "Search beginning of " + name + "? [Y/y] or [N/n]: ");
            EYesNo answer = EYesNo.fromString(input);
            switch (answer) {
                case YES -> { return true; }
                case NO -> { return false; }
                default -> out.println("Please enter [Y/y] or [N/n].");
            }
        }
    }
}
