package edu.ntnu.iir.bidata.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class DiaryUiUtils {

    public static final String BLUE_BG = "\u001B[44m";
    public static final String DARK_BLUE_TEXT = "\u001B[34m";
    public static final String DARK_BLUE_BG = "\u001B[44;1m";
    public static final String WHITE_TEXT = "\u001B[37m";
    public static final String BLACK_TEXT = "\u001B[30m";
    public static final String GREEN_TEXT = "\u001B[32m";
    public static final String DEFAULT = "\u001B[0m";
    static final String UNDERLINE = "\u001B[4m";
    public static final int STD_NUMBER_OF_COLUMNS  = 4;
    public static final int STD_COLUMN_WIDTH       = 36;
    public static final int STD_MAX_AMOUNT_WORDS = 700;
    public static final int STD_WORDS_PER_PART     = 25;
    public static final int STD_MAX_LENGTH_TITLES  = 200;
    public static final int STD_MAX_LENGTH_NAME  = 200;
    public static final int STD_MAX_WORD_LENGTH  = 10;


    static final String TITLE_1 = "Went out with a friend";
    static final String TITLE_2 = "Went to the university";
    static final String TITLE_3 = "Ate with a friend at a restaurant";

    static final String TEXT_1 = "I went out today and had to hang out with someone I don't really like...";
    static final String TEXT_2 = "Today I actually went to university. It's boring, I'd rather hang out in the city.";
    static final String TEXT_3 = "I actually went on a date with someone, but she didn't like me, so we agreed to just be friends.";

    public static LocalDate readDate(BufferedReader reader, String prompt) {
        try {
            out.print(prompt);
            String input = reader.readLine();
            if (input == null || input.isBlank()) return null;
            return LocalDate.parse(input.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }


    static String padText(String text, int width) {
        if (text.length() > width) return text.substring(0, width);
        return text + " ".repeat(width - text.length());
    }

     static LocalDateTime readDateTime(BufferedReader reader, String prompt, DateTimeFormatter dtf) {
        while (true) {
            String in = readInput(reader, prompt);
            if (in == null || in.isBlank()) return null;
            try {
                return LocalDateTime.parse(in.trim(), dtf);
            } catch (Exception e) {
                out.println("Invalid format. Use yyyy-MM-dd HH:mm");
            }
        }
    }


    public static List<List<String>> constructMenuNavigationCommandDescriptions() {
        return List.of(
                List.of("U", "Up"),
                List.of("D", "Down"),
                List.of("E", "Edit"),
                List.of("B", "Back")
        );
    }



     public static void showCommandDescriptions(List<List<String>> commandDescriptions, int nmbColumns, int colWidth) {

        out.println();
        int nmbRows = Math.ceilDiv(commandDescriptions.size(), nmbColumns);


        IntStream.range(0, nmbRows).forEach(row -> {
            StringBuilder rowText = new StringBuilder();

            IntStream.range(0, nmbColumns).forEach(columnIndex -> {


                int index = row * nmbColumns + columnIndex;

                if (index < commandDescriptions.size()) {
                    List<String> commandDescription = commandDescriptions.get(index);
                    String command     = commandDescription.get(0);
                    String description = commandDescription.get(1);

                    String text = BLUE_BG + BLACK_TEXT +
                            "[" + command.toUpperCase() + "/" + command.toLowerCase() + "]" +
                            DEFAULT + " " + description;

                    rowText.append(padText(text, colWidth));
                } else {
                    rowText.append(padText(" ", colWidth));
                }
            });

            out.println(rowText);
        });

        out.print("> ");
    }



    public static String readCommand(BufferedReader reader) {
        try {
            return reader.readLine().trim().toUpperCase();
        } catch (IOException e) {
            return "";
        }
    }
    public static String readNonBlankInput(BufferedReader reader, String prompt) {
        String input = "";
        do {
            try {
                out.print(prompt);
                input = reader.readLine();
                if (input == null) input = "";
                input = input.trim();
                if (input.isBlank()) {
                    out.println("Input cannot be empty. Please try again.");
                }
            } catch (IOException e) {
                out.println("Error reading input. Please try again.");
                input = "";
            }
        } while (input.isBlank());
        return input;
    }


    public static String readInput(BufferedReader reader,String prompt) {
        try {
            out.print(prompt);
            return reader.readLine().trim();
        } catch (IOException e) {
            return "";
        }
    }
    public static void showSectionTitle(String title) {
        out.println(BLUE_BG + BLACK_TEXT + "     " + title + "     " + DEFAULT + "\n");
    }

    public static void clearScreen() {
        IntStream.range(0, 50).forEach(i -> out.println());
    }
}
