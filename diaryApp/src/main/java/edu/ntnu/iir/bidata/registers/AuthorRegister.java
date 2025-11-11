package edu.ntnu.iir.bidata.registers;

import edu.ntnu.iir.bidata.models.Author;
import java.util.*;

public class AuthorRegister {
    private final Map<String, Author> authors;

    public AuthorRegister() {
        this.authors = new HashMap<>();
    }


    public void addAuthor(Author author) {
        if (author == null)
            throw new IllegalArgumentException("Author cannot be null");
        authors.put(author.getName(), author);
    }

    public void addAuthor(String firstName, String lastName) {
        Author author = new Author(firstName, lastName);
        authors.put(author.getName(), author);
    }

    public void addAuthor(String nickname) {
        Author author = new Author(nickname);
        authors.put(author.getName(), author);
    }

    public void addAuthor(String firstName, String lastName, String nickname) {
        Author author = new Author(firstName, lastName, nickname);
        authors.put(author.getName(), author);
    }


    public Optional<Author> updateName(String oldName, String firstName, String lastName) {
        if (oldName == null || oldName.isBlank())
            throw new IllegalArgumentException("Old name cannot be null or blank");

        Author author = authors.get(oldName);

        if (author == null)
            return Optional.empty();

        String newName = constructName(firstName, lastName, author.getNickname());
        if (authors.containsKey(newName))
            throw new IllegalArgumentException("Name already exists.");

        authors.remove(oldName);
        author.setFirstName(firstName);
        author.setLastName(lastName);
        authors.put(author.getName(), author);
        return Optional.of(author);
    }

    public Optional<Author> updateName(String oldName, String nickname) {
        if (oldName == null || oldName.isBlank())
            throw new IllegalArgumentException("Old name cannot be null or blank");
        Author author = authors.get(oldName);
        if (author == null)
            return Optional.empty();

        String newName = constructName(author.getFirstName(), author.getLastName(), nickname);
        if (authors.containsKey(newName))
            throw new IllegalArgumentException("Name already exists.");

        authors.remove(oldName);
        author.setNickname(nickname);
        authors.put(author.getName(), author);
        return Optional.of(author);
    }

    public Optional<Author> updateName(String oldName, String firstName, String lastName, String nickname) {
        if (oldName == null || oldName.isBlank())
            throw new IllegalArgumentException("Old name cannot be null or blank");
        Author author = authors.get(oldName);
        if (author == null)
            return Optional.empty();

        String newName = constructName(firstName, lastName, nickname);
        if (authors.containsKey(newName))
            throw new IllegalArgumentException("Name already exists.");

        authors.remove(oldName);
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setNickname(nickname);
        authors.put(author.getName(), author);
        return Optional.of(author);
    }

    public Optional<Author> updateFirstName(String oldName, String firstName) {
        if (oldName == null || oldName.isBlank())
            throw new IllegalArgumentException("Old name cannot be null or blank");
        Author author = authors.get(oldName);
        if (author == null)
            return Optional.empty();

        String newName = constructName(firstName, author.getLastName(), author.getNickname());
        if (authors.containsKey(newName))
            throw new IllegalArgumentException("Name already exists.");

        authors.remove(oldName);
        author.setFirstName(firstName);
        authors.put(author.getName(), author);
        return Optional.of(author);
    }

    public Optional<Author> updateLastName(String oldName, String lastName) {
        if (oldName == null || oldName.isBlank()) throw new IllegalArgumentException("Old name cannot be null or blank");
        Author author = authors.get(oldName);
        if (author == null)
            return Optional.empty();

        String newName = constructName(author.getFirstName(), lastName, author.getNickname());
        if (authors.containsKey(newName))
            throw new IllegalArgumentException("Name already exists.");

        authors.remove(oldName);
        author.setLastName(lastName);
        authors.put(author.getName(), author);
        return Optional.of(author);
    }


    public Optional<Author> removeAuthor(String name) {

        return Optional.ofNullable(authors.remove(name));
    }
    public Optional<Author> getAuthor(String name) {

        return Optional.ofNullable(authors.get(name));
    }
    public Iterator<Author> getAllAuthors() {
        return authors.values().iterator();
    }

    public boolean hasAuthor(String name) {
        return authors.containsKey(name);
    }

    public int getNumberOfAuthors() {
        return authors.size();
    }

    public boolean isEmpty() {
        return authors.isEmpty();
    }

    public void clear() {
        authors.clear();
    }
    public enum ENameCombination {
        FIRST_LAST_NICK,   // first + last + nickname
        FIRST_LAST,        // first + last
        FIRST_NICK,        // first + nickname
        LAST_NICK,         // last + nickname
        FIRST,             // first only
        LAST,              // last only
        NICK,              // nickname only
        NONE;              // nothing

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


    public String constructName(String firstName, String lastName, String nickname) {
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



    public Iterator<Author> findByFirstName(String firstName, boolean isFirstNameStartWith) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be null or blank");
        firstName = firstName.toLowerCase();

        String finalFirstName = firstName;
        return authors.values().stream()
                .filter(author -> isFirstNameStartWith
                        ? author.getFirstName().toLowerCase().startsWith(finalFirstName)
                        : author.getFirstName().equalsIgnoreCase(finalFirstName))
                .toList()
                .iterator();
    }

    public Iterator<Author> findByLastName(String lastName, boolean isLastNameStartWith) {
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be null or blank");
        lastName = lastName.toLowerCase();

        String finalLastName = lastName;
        return authors.values().stream()
                .filter(author -> isLastNameStartWith
                        ? author.getLastName().toLowerCase().startsWith(finalLastName)
                        : author.getLastName().equalsIgnoreCase(finalLastName))
                .toList()
                .iterator();
    }

    public Iterator<Author> findByNickname(String nickname, boolean isNicknameStartWith) {
        if (nickname == null || nickname.isBlank())
            throw new IllegalArgumentException("Nickname cannot be null or blank");
        nickname = nickname.toLowerCase();

        String finalNickname = nickname;
        return authors.values().stream()
                .filter(author -> isNicknameStartWith
                        ? author.getNickname().toLowerCase().startsWith(finalNickname)
                        : author.getNickname().equalsIgnoreCase(finalNickname))
                .toList()
                .iterator();
    }

    public Iterator<Author> findByFullName(String firstName, String lastName, boolean isFirstNameStartWith, boolean isLastNameStartWith) {
        if ((firstName == null || firstName.isBlank()) || (lastName == null || lastName.isBlank()))
            throw new IllegalArgumentException("First and last name cannot be null or blank");

        firstName = firstName.toLowerCase();
        lastName =  lastName.toLowerCase();

        String finalLastName = lastName;
        String finalFirstName = firstName;
        return authors.values().stream()
                .filter(author -> {
                    boolean firstMatches =
                            isFirstNameStartWith
                                    ? author.getFirstName().toLowerCase().startsWith(finalFirstName)
                                    : author.getFirstName().equalsIgnoreCase(finalFirstName);
                    boolean lastMatches =
                            isLastNameStartWith
                                    ? author.getLastName().toLowerCase().startsWith(finalLastName)
                                    : author.getLastName().equalsIgnoreCase(finalLastName);
                    return firstMatches && lastMatches;
                })
                .toList()
                .iterator();
    }

    public Iterator<Author> findByFullNameWithNickname(String firstName, String lastName, String nickname,
                                                       boolean isFirstNameStartWith, boolean isLastNameStartWith, boolean isNicknameStartWith) {
        if ((firstName == null || firstName.isBlank()) ||
                (lastName == null || lastName.isBlank()) ||
                (nickname == null || nickname.isBlank()))
            throw new IllegalArgumentException("First name, last name, and nickname cannot be null or blank");

        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();
        nickname = nickname.toLowerCase();

        String key = constructName(firstName, lastName, nickname);
        if (authors.containsKey(key)) {
            return Collections.singletonList(authors.get(key)).iterator();
        }

        String finalNickname = nickname;
        String finalLastName = lastName;
        String finalFirstName = firstName;
        return authors.values().stream()
                .filter(author -> {
                    boolean firstMatches = isFirstNameStartWith
                            ? author.getFirstName().toLowerCase().startsWith(finalFirstName)
                            : author.getFirstName().equalsIgnoreCase(finalFirstName);
                    boolean lastMatches = isLastNameStartWith
                            ? author.getLastName().toLowerCase().startsWith(finalLastName)
                            : author.getLastName().equalsIgnoreCase(finalLastName);
                    boolean nickMatches = isNicknameStartWith
                            ? author.getNickname().toLowerCase().startsWith(finalNickname)
                            : author.getNickname().equalsIgnoreCase(finalNickname);
                    return firstMatches && lastMatches && nickMatches;
                })
                .toList()
                .iterator();
    }
}
