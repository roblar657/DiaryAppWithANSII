package DiaryEntryTests.PositiveTests;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Iterator;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Positive tests for the DiaryEntry class.
 *
 * <ul>
 *   <li>Creating a diary entry
 *     <ul>
 *       <li>testCreateValidDiaryEntry: Tests constructor with valid author and maximum text length</li>
 *     </ul>
 *   </li>
 *
 *   <li>Adding a page
 *     <ul>
 *       <li>testAddValidPage: Tests addPage with valid title and text</li>
 *       <li>testAddMultiplePages: Tests addPage multiple times and verifies all pages are added</li>
 *       <li>testAddPageWithTextAtMaxLength: Tests addPage when text has the maximum length</li>
 *     </ul>
 *   </li>
 *
 *   <li>Removing a page
 *     <ul>
 *       <li>testRemovePage: Tests removePage for a page in the middle of the diary</li>
 *       <li>testRemoveLastPage: Tests removePage for the last page</li>
 *     </ul>
 *   </li>
 *
 *   <li>Getting a page
 *     <ul>
 *       <li>testGetPageText: Tests getPageText for a valid page</li>
 *       <li>testGetPageTitle: Tests getPageTitle for a valid page</li>
 *     </ul>
 *   </li>
 *
 *   <li>Navigation between pages
 *     <ul>
 *       <li>testGetNextPageText: Tests getNextPageText for a valid currentPageNumber</li>
 *       <li>testGetPreviousPageText: Tests getPreviousPageText for a valid currentPageNumber</li>
 *       <li>testGetNextPageTitle: Tests getNextPageTitle for a valid currentPageNumber</li>
 *       <li>testGetPreviousPageTitle: Tests getPreviousPageTitle for a valid currentPageNumber</li>
 *     </ul>
 *   </li>
 *
 *   <li>Editing a page
 *     <ul>
 *       <li>testPageTextAfterEditingOneTime: Tests editPageText once</li>
 *       <li>testPageTitleAfterEditingOneTime: Tests editPageTitle once</li>
 *     </ul>
 *   </li>
 *
 *   <li>Editing the diary entry title
 *     <ul>
 *       <li>testEntryTitleAfterEditingOneTime: Tests editEntryTitle once</li>
 *       <li>testEntryTitleAfterEditingMultipleTimes: Tests editEntryTitle multiple times</li>
 *     </ul>
 *   </li>
 *
 *   <li>Iterating over pages
 *     <ul>
 *       <li>testGetPageTextsAfterAddingPages: Tests getPageTexts after adding multiple pages</li>
 *       <li>testGetPageTitlesAfterAddingPages: Tests getPageTitles after adding multiple pages</li>
 *       <li>testGetEmptyPageTexts: Tests getPageTexts on an empty diary</li>
 *       <li>testGetEmptyPageTitles: Tests getPageTitles on an empty diary</li>
 *     </ul>
 *   </li>
 *
 *   <li>Timestamps
 *     <ul>
 *       <li>testUpdateTimeChangedAfterAddingPage: Tests that timeChanged updates after addPage</li>
 *       <li>testUpdateTimeChangedAfterRemovingPage: Tests that timeChanged updates after removePage</li>
 *       <li>testUpdateTimeChangedAfterEditingPageText: Tests that timeChanged updates after editPageText</li>
 *       <li>testUpdateTimeChangedAfterEditingPageTitle: Tests that timeChanged updates after editPageTitle</li>
 *       <li>testUpdateTimeChangedAfterEditingEntryTitle: Tests that timeChanged updates after editEntryTitle</li>
 *       <li>testKeepTimeCreatedAfterEditing: Tests that timeCreated does not change after add/edit</li>
 *     </ul>
 *   </li>
 *
 *   <li>Combined operations
 *     <ul>
 *       <li>testCombinedAddEditRemoveScenario: Tests combined usage of editEntryTitle, addPage, editPageText, and removePage</li>
 *     </ul>
 *   </li>
 * </ul>
 */
public class DiaryEntryPositiveTest {

    private DiaryEntry diaryEntry;
    private final Author AUTHOR = new Author("Ola Nordmann");
    private final int MAX_LENGTH = 256;
    private final String TITLE_1 = "Went out with a friend";
    private final String TITLE_2 = "Went to the university";
    private final String TITLE_3 = "Ate with a friend at a restaurant";

    private final String TEXT_1 = "I went out today and had to hang out with someone I don't really like...";
    private final String TEXT_2 = "Today I actually went to university. It's boring, I'd rather hang out in the city.";
    private final String TEXT_3 = "I actually went on a date with someone, but she didn't like me, so we agreed to just be friends.";

    private final String ENTRY_TITLE = "The Diary";
    private final String ENTRY_TITLE_EDITED_FIRST = "My Turn";
    private final String ENTRY_TITLE_EDITED_SECOND = "In the Mountains";
    private final String ENTRY_TITLE_EDITED_THIRD = "In the Garage";
    private final String ENTRY_TITLE_NEW = "A Fantastic Year";

    private final String EDITED_TEXT = "Once upon a time";

    @BeforeEach
    void setUp() {
        diaryEntry = new DiaryEntry(AUTHOR, MAX_LENGTH, ENTRY_TITLE);
    }

    /**
     * Tests constructor with valid author and max text length.
     */
    @Test
    void testCreateValidDiaryEntry() {
        assertEquals(AUTHOR, diaryEntry.getAuthor());
        assertEquals(MAX_LENGTH, diaryEntry.getMaxWordsPerPage());
        assertEquals(0, diaryEntry.getNmbPages());
    }

    /**
     * Tests addPage with valid title and text.
     */
    @Test
    void testAddValidPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertEquals(1, diaryEntry.getNmbPages());
        assertEquals(TITLE_1, diaryEntry.getPageTitle(1));
        assertEquals(TEXT_1, diaryEntry.getPageText(1));
    }

    /**
     * Tests addPage multiple times and verifies all pages are added.
     */
    @Test
    void testAddMultiplePages() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);
        assertEquals(3, diaryEntry.getNmbPages());
        assertEquals(TITLE_1, diaryEntry.getPageTitle(1));
        assertEquals(TITLE_2, diaryEntry.getPageTitle(2));
        assertEquals(TITLE_3, diaryEntry.getPageTitle(3));
    }

    /**
     * Tests addPage when text has the maximum length.
     */
    @Test
    void testAddPageWithTextAtMaxLength() {
        String textAtMaxLength = "x".repeat(MAX_LENGTH);
        diaryEntry.addPage(TITLE_1, textAtMaxLength);
        assertEquals(1, diaryEntry.getNmbPages());
        assertEquals(textAtMaxLength, diaryEntry.getPageText(1));
    }

    /**
     * Tests removePage for a page in the middle of the diary.
     */
    @Test
    void testRemovePage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.removePage(1);
        assertEquals(1, diaryEntry.getNmbPages());
        assertEquals(TITLE_2, diaryEntry.getPageTitle(1));
    }

    /**
     * Tests removePage for the last page.
     */
    @Test
    void testRemoveLastPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.removePage(2);
        assertEquals(1, diaryEntry.getNmbPages());
        assertEquals(TITLE_1, diaryEntry.getPageTitle(1));
    }

    /**
     * Tests getPageText for a valid page.
     */
    @Test
    void testGetPageText() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertEquals(TEXT_1, diaryEntry.getPageText(1));
    }

    /**
     * Tests getPageTitle for a valid page.
     */
    @Test
    void testGetPageTitle() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertEquals(TITLE_1, diaryEntry.getPageTitle(1));
    }

    /**
     * Tests getNextPageText for a valid currentPageNumber.
     */
    @Test
    void testGetNextPageText() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);
        assertEquals(TEXT_2, diaryEntry.getNextPageText(1));
        assertEquals(TEXT_3, diaryEntry.getNextPageText(2));
    }

    /**
     * Tests getPreviousPageText for a valid currentPageNumber.
     */
    @Test
    void testGetPreviousPageText() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);
        assertEquals(TEXT_1, diaryEntry.getPreviousPageText(2));
        assertEquals(TEXT_2, diaryEntry.getPreviousPageText(3));
    }

    /**
     * Tests getNextPageTitle for a valid currentPageNumber.
     */
    @Test
    void testGetNextPageTitle() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);
        assertEquals(TITLE_2, diaryEntry.getNextPageTitle(1));
        assertEquals(TITLE_3, diaryEntry.getNextPageTitle(2));
    }

    /**
     * Tests getPreviousPageTitle for a valid currentPageNumber.
     */
    @Test
    void testGetPreviousPageTitle() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);
        assertEquals(TITLE_1, diaryEntry.getPreviousPageTitle(2));
        assertEquals(TITLE_2, diaryEntry.getPreviousPageTitle(3));
    }

    /**
     * Tests editPageText once.
     */
    @Test
    void testPageTextAfterEditingOneTime() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.setPageText(1, TEXT_2);
        assertEquals(TEXT_2, diaryEntry.getPageText(1));
    }

    /**
     * Tests editPageTitle once.
     */
    @Test
    void testPageTitleAfterEditingOneTime() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.setPageTitle(1, TITLE_2);
        assertEquals(TITLE_2, diaryEntry.getPageTitle(1));
    }

    /**
     * Tests editEntryTitle once.
     */
    @Test
    void testEntryTitleAfterEditingOneTime() {
        diaryEntry.setEntryTitle(ENTRY_TITLE_NEW);
        assertEquals(ENTRY_TITLE_NEW, diaryEntry.getEntryTitle());
    }

    /**
     * Tests editEntryTitle multiple times.
     */
    @Test
    void testEntryTitleAfterEditingMultipleTimes() {
        diaryEntry.setEntryTitle(ENTRY_TITLE_EDITED_FIRST);
        diaryEntry.setEntryTitle(ENTRY_TITLE_EDITED_SECOND);
        diaryEntry.setEntryTitle(ENTRY_TITLE_EDITED_THIRD);
        assertEquals(ENTRY_TITLE_EDITED_THIRD, diaryEntry.getEntryTitle());
    }

    /**
     * Tests getPageTexts after adding multiple pages.
     */
    @Test
    void testGetPageTextsAfterAddingPages() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);
        Iterator<String> iterator = diaryEntry.getPageTexts();
        assertTrue(iterator.hasNext());
        assertEquals(TEXT_1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(TEXT_2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(TEXT_3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests getPageTitles after adding multiple pages.
     */
    @Test
    void testGetPageTitlesAfterAddingPages() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);
        Iterator<String> iterator = diaryEntry.getPageTitles();
        assertTrue(iterator.hasNext());
        assertEquals(TITLE_1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(TITLE_2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(TITLE_3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests getPageTexts on an empty diary.
     */
    @Test
    void testGetEmptyPageTexts() {
        Iterator<String> iterator = diaryEntry.getPageTexts();
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests getPageTitles on an empty diary.
     */
    @Test
    void testGetEmptyPageTitles() {
        Iterator<String> iterator = diaryEntry.getPageTitles();
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests that timeChanged updates after addPage.
     */
    @Test
    void testUpdateTimeChangedAfterAddingPage() throws InterruptedException {
        LocalDateTime timeCreated = diaryEntry.getTimeCreated();
        sleep(10);
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertTrue(diaryEntry.getTimeChanged().isAfter(timeCreated));
    }

    /**
     * Tests that timeChanged updates after removePage.
     */
    @Test
    void testUpdateTimeChangedAfterRemovingPage() throws InterruptedException {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        LocalDateTime timeAfterAdd = diaryEntry.getTimeChanged();
        sleep(10);
        diaryEntry.removePage(1);
        assertTrue(diaryEntry.getTimeChanged().isAfter(timeAfterAdd));
    }

    /**
     * Tests that timeChanged updates after editPageText.
     */
    @Test
    void testUpdateTimeChangedAfterEditingPageText() throws InterruptedException {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        LocalDateTime timeAfterAdd = diaryEntry.getTimeChanged();
        sleep(10);
        diaryEntry.setPageText(1, TEXT_2);
        assertTrue(diaryEntry.getTimeChanged().isAfter(timeAfterAdd));
    }

    /**
     * Tests that timeChanged updates after setPageTitle.
     */
    @Test
    void testUpdateTimeChangedAfterEditingPageTitle() throws InterruptedException {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        LocalDateTime timeAfterAdd = diaryEntry.getTimeChanged();
        sleep(10);
        diaryEntry.setPageTitle(1, TITLE_2);
        assertTrue(diaryEntry.getTimeChanged().isAfter(timeAfterAdd));
    }

    /**
     * Tests that timeChanged updates after setEntryTitle.
     */
    @Test
    void testUpdateTimeChangedAfterEditingEntryTitle() throws InterruptedException {
        LocalDateTime timeCreated = diaryEntry.getTimeCreated();
        sleep(10);
        diaryEntry.setEntryTitle(ENTRY_TITLE_NEW);
        assertTrue(diaryEntry.getTimeChanged().isAfter(timeCreated));
    }

    /**
     * Tests that timeCreated does not change after add/edit
     */
    @Test
    void testKeepTimeCreatedAfterSet() throws InterruptedException {
        LocalDateTime timeCreated = diaryEntry.getTimeCreated();
        sleep(10);
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.setEntryTitle(ENTRY_TITLE_NEW);
        assertEquals(timeCreated, diaryEntry.getTimeCreated());
    }

    /**
     * Tests combined usage of setEntryTitle, addPage, setPageText, and removePage.
     */
    @Test
    void testCombinedAddEditRemoveScenario() {
        diaryEntry.setEntryTitle(ENTRY_TITLE);
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        diaryEntry.addPage(TITLE_3, TEXT_3);

        assertEquals(3, diaryEntry.getNmbPages());
        assertEquals(ENTRY_TITLE, diaryEntry.getEntryTitle());

        diaryEntry.setPageText(2, EDITED_TEXT);
        assertEquals(EDITED_TEXT, diaryEntry.getPageText(2));

        diaryEntry.removePage(1);
        assertEquals(2, diaryEntry.getNmbPages());
        assertEquals(TITLE_2, diaryEntry.getPageTitle(1));
        assertEquals(TITLE_3, diaryEntry.getPageTitle(2));
    }
}
