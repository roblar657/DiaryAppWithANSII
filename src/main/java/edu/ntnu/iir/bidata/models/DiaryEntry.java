package edu.ntnu.iir.bidata.models;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.STD_MAX_LENGTH_NAME;
import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.STD_MAX_LENGTH_TITLES;

/**
 * Consists of one or more pages,
 * where each page has both a title and text.
 * <p>
 * The following functionality is provided:
 * <ul>
 *   <li>Add new pages</li>
 *   <li>Remove existing pages</li>
 *   <li>Edit text and title of a page</li>
 *   <li>Change the title of the diary entry itself</li>
 *   <li>Navigate between pages</li>
 * </ul>
 */
public class DiaryEntry {

    private final int maxWordsPerPage;
    private final LinkedList<Page> pages;
    private final Author author;
    private String entryTitle;
    private final LocalDateTime timeCreated;
    private LocalDateTime timeChanged;

    private final Map<String, Long> wordCount;

    /**
     * Creates a new diary entry.
     *
     * @param author Author of the diary entry.
     * @param maxTextLengthPerPage Maximum text length per page.
     * @param entryTitle Initial title of the diary entry.
     * @throws IllegalArgumentException if author is null or empty,
     *                                  if entryTitle is null or empty,
     *                                  or if maxTextLengthPerPage is less than or equal to 0.
     */
    public DiaryEntry(Author author, int maxTextLengthPerPage, String entryTitle) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (maxTextLengthPerPage <= 0) {
            throw new IllegalArgumentException("Maximum text length per page must be greater than 0");
        }

        this.author = author;
        this.entryTitle = entryTitle;
        this.maxWordsPerPage = maxTextLengthPerPage;
        this.timeCreated = LocalDateTime.now();
        this.timeChanged = this.timeCreated;
        this.pages = new LinkedList<>();
        this.wordCount = new HashMap<>();

        author.setLastTimeCreated(this.timeCreated);
    }

    /**
     * Adds a new page to the diary entry.
     *
     * @param title Page title.
     * @param text  Page text.
     * @throws IllegalArgumentException if title is null or empty,
     *                                  if text is null, blank, or longer than maxTextLengthPerPage.
     */
    public void addPage(String title, String text) {
        validateTitle(title);
        validateText(text);
        this.pages.add(new Page(title, text));
        addToWordToWordCount(text);
        updateTimeChanged();
    }
    /**
     * Returns map between page numbers and Pages, containing spesific word
     *
     * @param word Word to search for.
     * @return  Returns map between page numbers and Pages,containing spesific word
     */
    public Map<Integer, Page> getPagesContainingWord(String word) {
        if (word == null || word.isBlank())
            return Map.of();

        Map<Integer, Page> result = new LinkedHashMap<>();
        IntStream.range(0, pages.size())
                .forEach(index -> {
                    Page page = pages.get(index);
                    if (page.getText().toLowerCase().contains(word.toLowerCase())) {
                        result.put(index+1, page);
                    }
                });

        return result;
    }




    /**
     * Removes a page from the diary entry.
     *
     * @param pageNumber The page number to remove (must be greater than 0).
     * @throws IllegalArgumentException if pageNumber is less than 1 or greater than the number of pages.
     */
    public void removePage(int pageNumber) {
        validatePageNumber(pageNumber);
        Page removed = this.pages.remove(pageNumber - 1);
        removeFromWordCount(removed.getText());
        updateTimeChanged();
    }

    /**
     * Retrieves the text from a specific page.
     *
     * @param pageNumber Page number (must be greater than 0).
     * @return The page text.
     * @throws IllegalArgumentException if pageNumber is less than 1 or greater than the number of pages.
     */
    public String getPageText(int pageNumber) {
        validatePageNumber(pageNumber);
        return pages.get(pageNumber - 1).getText();
    }

    /**
     * Retrieves the title from a specific page.
     *
     * @param pageNumber Page number (must be greater than 0).
     * @return The page title.
     * @throws IllegalArgumentException if pageNumber is less than 1 or greater than the number of pages.
     */
    public String getPageTitle(int pageNumber) {
        validatePageNumber(pageNumber);
        return pages.get(pageNumber - 1).getTitle();
    }

    /**
     * Retrieves the text of the next page.
     *
     * @param currentPageNumber Current page number.
     * @return The text of the next page.
     * @throws IllegalArgumentException if currentPageNumber is less than 1 or greater than the number of pages,
     *                                  or if there is no page after currentPageNumber.
     */
    public String getNextPageText(int currentPageNumber) {
        validatePageNumber(currentPageNumber);
        return getPageText(currentPageNumber + 1);
    }

    /**
     * Retrieves the text of the previous page.
     *
     * @param currentPageNumber Current page number.
     * @return The text of the previous page.
     * @throws IllegalArgumentException if currentPageNumber is less than 1 or greater than the number of pages,
     *                                  or if there is no page before currentPageNumber.
     */
    public String getPreviousPageText(int currentPageNumber) {
        return getPageText(currentPageNumber - 1);
    }

    /**
     * Retrieves the title of the next page.
     *
     * @param currentPageNumber Current page number.
     * @return The title of the next page.
     * @throws IllegalArgumentException if currentPageNumber is less than 1 or greater than the number of pages,
     *                                  or if there is no page after currentPageNumber.
     */
    public String getNextPageTitle(int currentPageNumber) {
        return getPageTitle(currentPageNumber + 1);
    }

    /**
     * Retrieves the title of the previous page.
     *
     * @param currentPageNumber Current page number.
     * @return The title of the previous page.
     * @throws IllegalArgumentException if currentPageNumber is less than 1 or greater than the number of pages,
     *                                  or if there is no page before currentPageNumber.
     */
    public String getPreviousPageTitle(int currentPageNumber) {
        return getPageTitle(currentPageNumber - 1);
    }

    /**
     * Changes the text of an existing page.
     *
     * @param pageNumber Page number (must be greater than 0).
     * @param newText    New text.
     * @throws IllegalArgumentException if pageNumber is less than 1 or greater than the number of pages,
     *                                  if newText is null, blank, or longer than maxTextLengthPerPage.
     */
    public void setPageText(int pageNumber, String newText) {
        validatePageNumber(pageNumber);
        validateText(newText);
        Page page = pages.get(pageNumber - 1);
        updateWordCount(page.getText(), newText);
        page.setText(newText);
        updateTimeChanged();
    }

    /**
     * Changes the title of an existing page.
     *
     * @param pageNumber Page number (must be greater than 0).
     * @param newTitle   New page title.
     * @throws IllegalArgumentException if pageNumber is less than 1 or greater than the number of pages,
     *                                  or if newTitle is null or empty.
     */
    public void setPageTitle(int pageNumber, String newTitle) {
        validatePageNumber(pageNumber);
        validateTitle(newTitle);
        pages.get(pageNumber - 1).setTitle(newTitle);
        updateTimeChanged();
    }
    public Map<String, Long> getWordCount() {
        return wordCount;
    }

    /**
     * Changes the title of the entire diary entry.
     *
     * @param newTitle New title.
     * @throws IllegalArgumentException if newTitle is null or empty.
     */
    public void setEntryTitle(String newTitle) {
        validateTitle(newTitle);
        this.entryTitle = newTitle;
        updateTimeChanged();
    }

    /**
     * Retrieves the number of pages in the diary entry.
     *
     * @return Number of pages.
     */
    public int getNmbPages() {
        return pages.size();
    }

    /**
     * Retrieves the maximum text length per page.
     *
     * @return Maximum text length per page.
     */
    public int getMaxWordsPerPage() {
        return maxWordsPerPage;
    }

    /**
     * Retrieves an iterator over all page texts in the diary entry.
     *
     * @return Iterator over page texts.
     */
    public Iterator<String> getPageTexts() {
        return pages.stream().map(Page::getText).iterator();
    }

    /**
     * Retrieves an iterator over all page titles in the diary entry.
     *
     * @return Iterator over page titles.
     */
    public Iterator<String> getPageTitles() {
        return pages.stream().map(Page::getTitle).iterator();
    }

    /**
     * Retrieves the title of the diary entry.
     *
     * @return Title of the diary entry.
     */
    public String getEntryTitle() {
        return entryTitle;
    }

    /**
     * Retrieves the author of the diary entry.
     *
     * @return Author.
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Retrieves the time when the diary entry was created.
     *
     * @return Creation time.
     */
    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    /**
     * Retrieves the time when the diary entry was last changed.
     *
     * @return Time of last change.
     */
    public LocalDateTime getTimeChanged() {
        return timeChanged;
    }


    /**
     * Updates the time of last change to the current time,
     * and also updates the author's first and last change timestamps.
     */
    private void updateTimeChanged() {
        this.timeChanged = LocalDateTime.now();
        author.setLastTimeChanged(this.timeChanged);
    }


    /**
     * Validates that a page number is valid.
     *
     * @param pageNumber Page number to validate.
     * @throws IllegalArgumentException if the page number is less than 1 or greater than the number of pages.
     */
    private void validatePageNumber(int pageNumber) {
        if (pageNumber < 1 || pageNumber > pages.size()) {
            throw new IllegalArgumentException(
                    pageNumber + " is an invalid page number. Must be between 1 and " + pages.size()
            );
        }
    }
    /**
     * Validates that text is valid.
     *
     * @param text Text to validate.
     * @throws IllegalArgumentException if text is null, blank, or longer than maxTextLengthPerPage.
     */
    private void validateText(String text) {
        if (text == null) throw new IllegalArgumentException("Text cannot be null");
        if (text.isBlank()) throw new IllegalArgumentException("Text cannot be blank");
        if (text.split(" ").length > maxWordsPerPage) {
            throw new IllegalArgumentException(
                    "Text exceeds the maximum length of " + maxWordsPerPage + " words"
            );
        }
    }

    /**
     * Validates that a title is valid.
     *
     * @param title Title to validate.
     * @throws IllegalArgumentException if title is null or empty.
     */
    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if(title.length() >= STD_MAX_LENGTH_TITLES){
            throw new IllegalArgumentException("Title cannot be longer than " + STD_MAX_LENGTH_TITLES + " characters");
        }
    }


    private void addToWordToWordCount(String text) {
        if (text == null || text.isBlank()) return;

        Arrays.stream(text.split(" "))
                .filter(word -> !word.isBlank())
                .map(String::toLowerCase)
                .forEach(word -> {
                    Long currentCount = wordCount.get(word);
                    if (currentCount == null) {
                        wordCount.put(word, 1L);
                    } else {
                        wordCount.put(word, currentCount + 1);
                    }
                });
        author.addWordToWordCount(this.wordCount);
    }
    private void updateWordCount(String oldText, String newText) {
        removeFromWordCount(oldText);
        addToWordToWordCount(newText);
    }


    private void removeFromWordCount(String text) {
        if (text == null || text.isBlank()) return;

        Arrays.stream(text.split(" "))
                .filter(word -> !word.isBlank())
                .map(String::toLowerCase)
                .forEach(word -> {
                    Long currentCount = wordCount.get(word);
                    if (currentCount != null) {
                        if (currentCount > 1) {
                            wordCount.put(word, currentCount - 1);
                        } else {
                            wordCount.remove(word);
                        }
                    }
                });
        author.removeWordFromWordCount(this.wordCount);
    }


}
