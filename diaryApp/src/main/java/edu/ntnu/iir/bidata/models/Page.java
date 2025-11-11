package edu.ntnu.iir.bidata.models;

import static edu.ntnu.iir.bidata.ui.DiaryUiUtils.*;

/**
 * A page in a diary entry.
 */
public class Page {
    private String title;
    private String text;

    /**
     * Creates a new page.
     *
     * @param title Page title.
     * @param text  Page text.
     * @throws IllegalArgumentException if title is null or empty, or if text is null.
     */
    public Page(String title, String text) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if(title.length() >= STD_MAX_LENGTH_TITLES){
            throw new IllegalArgumentException("Title cannot be longer than " + STD_MAX_LENGTH_TITLES + " characters");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        this.title = title;
        this.text = text;
    }

    /**
     * Retrieves the page title.
     *
     * @return The page title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the page text.
     *
     * @return The page text.
     */
    public String getText() {
        return text;
    }

    /**
     * Changes the page title.
     *
     * @param title New page title.
     * @throws IllegalArgumentException if title is null or empty.
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if(title.length() >= STD_MAX_LENGTH_TITLES){
            throw new IllegalArgumentException("Title cannot be longer than " + STD_MAX_LENGTH_TITLES + " characters");
        }
        this.title = title;
    }

    /**
     * Changes the page text.
     *
     * @param text New page text.
     * @throws IllegalArgumentException if text is null.
     */
    public void setText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        this.text = text;
    }
}
