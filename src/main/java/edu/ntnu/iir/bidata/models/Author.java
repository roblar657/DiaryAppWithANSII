package edu.ntnu.iir.bidata.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.STD_MAX_LENGTH_NAME;
import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.STD_MAX_LENGTH_TITLES;

/**
 * Author of a diary entry.
 * An author can have a first name, last name, and/or nickname.
 */
public class Author {
    private String firstName;
    private String lastName;
    private String nickname;

    private LocalDateTime lastTimeCreated;
    private LocalDateTime lastTimeChanged;

    private final Map<String, Long> wordCount = new HashMap<>();

    /**
     * Creates a new author with first name, last name, and nickname.
     *
     * @param firstName Author's first name.
     * @param lastName Author's last name.
     * @param nickname Author's nickname.
     * @throws IllegalArgumentException if all parameters are null or empty.
     */
    public Author(String firstName, String lastName, String nickname) {
        if ((firstName == null || firstName.isBlank()) &&
                (lastName == null || lastName.isBlank()) &&
                (nickname == null || nickname.isBlank())) {
            throw new IllegalArgumentException(
                    "At least one of the fields (firstName, lastName, nickname) must have a value"
            );
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
    }

    /**
     * Creates a new author with first name and last name.
     *
     * @param firstName First name.
     * @param lastName Last name.
     * @throws IllegalArgumentException if both parameters are null or empty.
     */
    public Author(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    /**
     * Creates a new author, with only a nickname.
     *
     * @param nickname  Nickname.
     * @throws IllegalArgumentException if nickname is null, empty or blank.
     */
    public Author(String nickname) {
        validateNickname(nickname);
        this.firstName = null;
        this.lastName = null;
        this.nickname = nickname;
    }

    /**
     * Validates nickname is not null or blank.
     *
     * @param nickname  Nickname.
     * @throws IllegalArgumentException if nickname is null or blank.
     */
    private void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or blank");
        }
        if(nickname.length() >= STD_MAX_LENGTH_NAME){
            throw new IllegalArgumentException("Nickname cannot be longer than " + STD_MAX_LENGTH_NAME + " characters");
        }
    }

    /**
     * Validates first name is not null or blank.
     *
     * @param firstName  first name.
     * @throws IllegalArgumentException if firstName is null or blank.
     */
    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if(firstName.length() >= STD_MAX_LENGTH_NAME){
            throw new IllegalArgumentException("First name cannot be longer than " + STD_MAX_LENGTH_NAME + " characters");
        }
    }

    /**
     * Validates last name is not null or blank.
     *
     * @param lastName last name
     * @throws IllegalArgumentException if lastName is null or blank.
     */
    private void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if(lastName.length() >= STD_MAX_LENGTH_NAME){
            throw new IllegalArgumentException("Last name cannot be longer than " + STD_MAX_LENGTH_NAME + " characters");
        }
    }

    /**
     * Gets author's first name.
     *
     * @return  author's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets author's last name.
     *
     * @return author's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets author's full name.
     *
     * @return author's full name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets author's nickname.
     *
     * @return Author's nickname.
     */
    public String getNickname() {
        return nickname;
    }


    public enum ENameCombination {
        FIRST_LAST_NICK,   // first + last + nickname
        FIRST_LAST,        // first + last
        FIRST_NICK,        // first + nickname
        LAST_NICK,         // last + nickname
        FIRST,             // first only
        LAST,              // last only
        NICK,              // nickname only
        NONE;              // ingen

        public static ENameCombination fromBooleans(boolean hasFirst, boolean hasLast, boolean hasNick) {
            if (hasFirst && hasLast && hasNick) return FIRST_LAST_NICK;
            if (hasFirst && hasLast) return FIRST_LAST;
            if (hasFirst && hasNick) return FIRST_NICK;
            if (hasLast && hasNick) return LAST_NICK;
            if (hasFirst) return FIRST;
            if (hasLast) return LAST;
            if (hasNick) return NICK;
            return NONE;
        }
    }

    /**
     * Gets the author's name.
     * Returns the full name (first name + last name) if both are set.
     * If only one of them is set, that one is returned.
     * If neither is set, return nickname.
     *
     * @return  author's name (first name, last name, full name or nickname).
     */
    public String getName() {
        boolean hasFirst = firstName != null && !firstName.isBlank();
        boolean hasLast = lastName != null && !lastName.isBlank();
        boolean hasNick = nickname != null && !nickname.isBlank();

        ENameCombination combination = ENameCombination.fromBooleans(hasFirst, hasLast, hasNick);

        return switch (combination) {
            case FIRST_LAST_NICK -> firstName + " " + lastName + "(" + nickname + ")";
            case FIRST_LAST -> firstName + " " + lastName;
            case FIRST_NICK -> firstName + "(" + nickname + ")";
            case LAST_NICK -> lastName + "(" + nickname + ")";
            case FIRST -> firstName;
            case LAST -> lastName;
            case NICK -> nickname;
            case NONE -> "";
        };
    }

    /**
     * Sets the author's first name.
     *
     * @param firstName New first name.
     * @throws IllegalArgumentException if firstName is null or blank.
     */
    public void setFirstName(String firstName) {
        validateFirstName(firstName);
        this.firstName = firstName;
        updateChangeTime();
    }

    /**
     * Sets author's last name.
     *
     * @param lastName New last name.
     * @throws IllegalArgumentException if lastName is null or blank.
     */
    public void setLastName(String lastName) {
        validateLastName(lastName);
        this.lastName = lastName;
        updateChangeTime();
    }

    /**
     * Sets  author's nickname.
     *
     * @param nickname New nickname.
     * @throws IllegalArgumentException if nickname is null or blank.
     */
    public void setNickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
        updateChangeTime();
    }



    public LocalDateTime getLastTimeCreated() {
        return lastTimeCreated;
    }

    public void setLastTimeCreated(LocalDateTime lastTimeCreated) {
        this.lastTimeCreated = lastTimeCreated;
    }

    public LocalDateTime getLastTimeChanged() {
        return lastTimeChanged;
    }

    public void setLastTimeChanged(LocalDateTime lastTimeChanged) {
        this.lastTimeChanged = lastTimeChanged;
    }

    private void updateChangeTime() {

        lastTimeChanged = LocalDateTime.now();
    }


    public void addWordToWordCount(Map<String, Long> newWords) {
        if (newWords == null) return;
        for (Map.Entry<String, Long> entry : newWords.entrySet()) {
            String word = entry.getKey();
            Long count = entry.getValue();
            Long currentCount = wordCount.getOrDefault(word, 0L);
            wordCount.put(word, currentCount + count);
        }
    }

    public Map<String, Long> getWordCount() {
        return wordCount;
    }

    public void removeWordFromWordCount(Map<String, Long> words) {
        if (words == null) return;
        for (Map.Entry<String, Long> entry : words.entrySet()) {
            String word = entry.getKey();
            Long count = entry.getValue();
            Long currentCount = wordCount.get(word);
            if (currentCount != null) {
                Long newCount = currentCount - count;
                if (newCount > 0L) {
                    wordCount.put(word, newCount);
                } else {
                    wordCount.remove(word);
                }
            }
        }
    }
}
