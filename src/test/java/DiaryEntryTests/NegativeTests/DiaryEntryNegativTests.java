package DiaryEntryTests.NegativeTests;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Negative tests for the DiaryEntry class.
 *
 * <ul>
 *   <li>Constructor
 *     <ul>
 *       <li>testConstructorWithNegativeMaxTextLength: Tests constructor when maxTextLength is negative</li>
 *       <li>testConstructorWithZeroMaxTextLength: Tests constructor when maxTextLength is zero</li>
 *     </ul>
 *   </li>
 *
 *   <li>Add page
 *     <ul>
 *       <li>testAddPageWithNullTitle: Tests addPage when title is null</li>
 *       <li>testAddPageWithEmptyTitle: Tests addPage when title is empty</li>
 *       <li>testAddPageWithBlankTitle: Tests addPage when title only contains spaces</li>
 *       <li>testAddPageWithNullText: Tests addPage when text is null</li>
 *       <li>testAddPageWithTooLongText: Tests addPage when text is too long</li>
 *       <li>testAddPageWithTextOneCharacterOverLimit: Tests addPage when text exceeds length by one character</li>
 *     </ul>
 *   </li>
 *
 *   <li>Remove page
 *     <ul>
 *       <li>testRemovePageWithPageNumberLessThanOne: Tests removePage when pageNumber < 1</li>
 *       <li>testRemovePageWithPageNumberGreaterThanSize: Tests removePage when pageNumber > total pages</li>
 *       <li>testRemovePageFromEmptyDiary: Tests removePage on empty diary</li>
 *       <li>testRemovePageWithNegativePageNumber: Tests removePage when pageNumber is negative</li>
 *     </ul>
 *   </li>
 *
 *   <li>Get page
 *     <ul>
 *       <li>testGetPageTextWithInvalidPageNumber: Tests getPageText with invalid pageNumber</li>
 *       <li>testGetPageTextWithPageNumberTooLarge: Tests getPageText with too large pageNumber</li>
 *       <li>testGetPageTextFromEmptyDiary: Tests getPageText on empty diary</li>
 *       <li>testGetPageTitleWithInvalidPageNumber: Tests getPageTitle with invalid pageNumber</li>
 *       <li>testGetPageTitleWithPageNumberTooLarge: Tests getPageTitle with too large pageNumber</li>
 *       <li>testGetPageTitleFromEmptyDiary: Tests getPageTitle on empty diary</li>
 *     </ul>
 *   </li>
 *
 *   <li>Navigation between pages
 *     <ul>
 *       <li>testGetNextPageTextWhenNoNextPage: Tests getNextPageText when there is no next page</li>
 *       <li>testGetNextPageTitleWhenNoNextPage: Tests getNextPageTitle when there is no next page</li>
 *       <li>testGetNextPageTextWithInvalidCurrentPage: Tests getNextPageText with invalid current page</li>
 *       <li>testGetNextPageTitleWithInvalidCurrentPage: Tests getNextPageTitle with invalid current page</li>
 *       <li>testGetPreviousPageTextWhenNoPreviousPage: Tests getPreviousPageText when there is no previous page</li>
 *       <li>testGetPreviousPageTitleWhenNoPreviousPage: Tests getPreviousPageTitle when there is no previous page</li>
 *       <li>testGetPreviousPageTextWithInvalidCurrentPage: Tests getPreviousPageText with invalid current page</li>
 *       <li>testGetPreviousPageTitleWithInvalidCurrentPage: Tests getPreviousPageTitle with invalid current page</li>
 *     </ul>
 *   </li>
 *
 *   <li>Edit page
 *     <ul>
 *       <li>testEditPageTextWithInvalidPageNumber: Tests editPageText with invalid page number</li>
 *       <li>testEditPageTextWithNullText: Tests editPageText when text is null</li>
 *       <li>testEditPageTextWithTooLongText: Tests editPageText when text is too long</li>
 *       <li>testEditPageTextWithPageNumberTooLarge: Tests editPageText when page number is too large</li>
 *       <li>testEditPageTextOnEmptyDiary: Tests editPageText on empty diary</li>
 *       <li>testEditPageTitleWithInvalidPageNumber: Tests editPageTitle with invalid page number</li>
 *       <li>testEditPageTitleWithNullTitle: Tests editPageTitle when title is null</li>
 *       <li>testEditPageTitleWithEmptyTitle: Tests editPageTitle when title is empty</li>
 *       <li>testEditPageTitleWithBlankTitle: Tests editPageTitle when title only contains spaces</li>
 *       <li>testEditPageTitleWithPageNumberTooLarge: Tests editPageTitle when page number is too large</li>
 *       <li>testEditPageTitleOnEmptyDiary: Tests editPageTitle on empty diary</li>
 *     </ul>
 *   </li>
 *
 *   <li>Edit diary title
 *     <ul>
 *       <li>testEditEntryTitleWithNullTitle: Tests editEntryTitle when title is null</li>
 *       <li>testEditEntryTitleWithEmptyTitle: Tests editEntryTitle when title is empty</li>
 *       <li>testEditEntryTitleWithBlankTitle: Tests editEntryTitle when title only contains spaces</li>
 *     </ul>
 *   </li>
 * </ul>
 */

class DiaryEntryNegativeTest {

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
    private final String ENTRY_TITLE_NEW = "An Amazing Year";
    private final String EDITED_TEXT = "Once upon a time";

    private final String ERROR_NULL_TITLE = null;
    private final String ERROR_EMPTY_TITLE = "";
    private final String ERROR_BLANK_TITLE = "   ";
    private final String ERROR_NULL_TEXT = null;
    private final String ERROR_TOO_LONG_TEXT = "a".repeat(257);
    private final int ERROR_NEGATIVE_MAX_LENGTH = -1;
    private final int ERROR_ZERO_MAX_LENGTH = 0;
    private final int ERROR_PAGE_NUMBER_ZERO = 0;
    private final int ERROR_PAGE_NUMBER_NEGATIVE = -1;
    private final int ERROR_PAGE_NUMBER_TOO_LARGE = 999;

    @BeforeEach
    void setUp() {
        diaryEntry = new DiaryEntry(AUTHOR, MAX_LENGTH, ENTRY_TITLE);
    }

    /** Tests constructor with negative maxTextLength. */
    @Test
    void testConstructorWithNegativeMaxTextLength() {
        assertThrows(IllegalArgumentException.class, () ->
                new DiaryEntry(AUTHOR, ERROR_NEGATIVE_MAX_LENGTH, ENTRY_TITLE)
        );
    }

    /** Tests constructor with zero maxTextLength. */
    @Test
    void testConstructorWithZeroMaxTextLength() {
        assertThrows(IllegalArgumentException.class, () ->
                new DiaryEntry(AUTHOR, ERROR_ZERO_MAX_LENGTH, ENTRY_TITLE)
        );
    }

    /** Tests addPage with null title. */
    @Test
    void testAddPageWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.addPage(ERROR_NULL_TITLE, TEXT_1)
        );
    }

    /** Tests addPage with empty title. */
    @Test
    void testAddPageWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.addPage(ERROR_EMPTY_TITLE, TEXT_1)
        );
    }

    /** Tests addPage with blank title. */
    @Test
    void testAddPageWithBlankTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.addPage(ERROR_BLANK_TITLE, TEXT_1)
        );
    }

    /** Tests addPage with null text. */
    @Test
    void testAddPageWithNullText() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.addPage(TITLE_1, ERROR_NULL_TEXT)
        );
    }




    /** Tests removePage with page number < 1. */
    @Test
    void testRemovePageWithPageNumberLessThanOne() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.removePage(ERROR_PAGE_NUMBER_ZERO)
        );
    }

    /** Tests removePage with page number > total pages. */
    @Test
    void testRemovePageWithPageNumberGreaterThanSize() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.removePage(ERROR_PAGE_NUMBER_TOO_LARGE)
        );
    }

    /** Tests removePage on empty diary. */
    @Test
    void testRemovePageFromEmptyDiary() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.removePage(1)
        );
    }

    /** Tests removePage with negative page number. */
    @Test
    void testRemovePageWithNegativePageNumber() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.removePage(ERROR_PAGE_NUMBER_NEGATIVE)
        );
    }

    /** Tests getPageText with invalid page number. */
    @Test
    void testGetPageTextWithInvalidPageNumber() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPageText(ERROR_PAGE_NUMBER_ZERO)
        );
    }

    /** Tests getPageText with page number too large. */
    @Test
    void testGetPageTextWithPageNumberTooLarge() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPageText(ERROR_PAGE_NUMBER_TOO_LARGE)
        );
    }

    /** Tests getPageText from empty diary. */
    @Test
    void testGetPageTextFromEmptyDiary() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPageText(1)
        );
    }

    /** Tests getPageTitle with invalid page number. */
    @Test
    void testGetPageTitleWithInvalidPageNumber() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPageTitle(ERROR_PAGE_NUMBER_ZERO)
        );
    }

    /** Tests getPageTitle with page number too large. */
    @Test
    void testGetPageTitleWithPageNumberTooLarge() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPageTitle(ERROR_PAGE_NUMBER_TOO_LARGE)
        );
    }

    /** Tests getPageTitle from empty diary. */
    @Test
    void testGetPageTitleFromEmptyDiary() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPageTitle(1)
        );
    }

    /** Tests getNextPageText when there is no next page. */
    @Test
    void testGetNextPageTextWhenNoNextPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getNextPageText(1)
        );
    }

    /** Tests getNextPageTitle when there is no next page. */
    @Test
    void testGetNextPageTitleWhenNoNextPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getNextPageTitle(1)
        );
    }

    /** Tests getNextPageText with invalid current page. */
    @Test
    void testGetNextPageTextWithInvalidCurrentPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getNextPageText(ERROR_PAGE_NUMBER_ZERO)
        );
    }

    /** Tests getNextPageTitle with invalid current page. */
    @Test
    void testGetNextPageTitleWithInvalidCurrentPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getNextPageTitle(ERROR_PAGE_NUMBER_NEGATIVE)
        );
    }

    /** Tests getPreviousPageText when there is no previous page. */
    @Test
    void testGetPreviousPageTextWhenNoPreviousPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPreviousPageText(1)
        );
    }

    /** Tests getPreviousPageTitle when there is no previous page. */
    @Test
    void testGetPreviousPageTitleWhenNoPreviousPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPreviousPageTitle(1)
        );
    }

    /** Tests getPreviousPageText with invalid current page. */
    @Test
    void testGetPreviousPageTextWithInvalidCurrentPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPreviousPageText(ERROR_PAGE_NUMBER_ZERO)
        );
    }

    /** Tests getPreviousPageTitle with invalid current page. */
    @Test
    void testGetPreviousPageTitleWithInvalidCurrentPage() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        diaryEntry.addPage(TITLE_2, TEXT_2);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.getPreviousPageTitle(ERROR_PAGE_NUMBER_NEGATIVE)
        );
    }

    /** Tests editPageText with invalid page number. */
    @Test
    void testEditPageTextWithInvalidPageNumber() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageText(ERROR_PAGE_NUMBER_ZERO, EDITED_TEXT)
        );
    }

    /** Tests setPageText with null text. */
    @Test
    void testEditPageTextWithNullText() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageText(1, ERROR_NULL_TEXT)
        );
    }



    /** Tests setPageText with page number too large. */
    @Test
    void testEditPageTextWithPageNumberTooLarge() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageText(ERROR_PAGE_NUMBER_TOO_LARGE, EDITED_TEXT)
        );
    }

    /** Tests setPageText on empty diary. */
    @Test
    void testSetPageTextOnEmptyDiary() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageText(1, EDITED_TEXT)
        );
    }

    /** Tests setPageTitle with invalid page number. */
    @Test
    void testEditPageTitleWithInvalidPageNumber() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageTitle(ERROR_PAGE_NUMBER_ZERO, ENTRY_TITLE_NEW)
        );
    }

    /** Tests setPageTitle with null title. */
    @Test
    void testEditPageTitleWithNullTitle() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageTitle(1, ERROR_NULL_TITLE)
        );
    }

    /** Tests setPageTitle with empty title. */
    @Test
    void testEditPageTitleWithEmptyTitle() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageTitle(1, ERROR_EMPTY_TITLE)
        );
    }

    /** Tests setPageTitle with blank title. */
    @Test
    void testEditPageTitleWithBlankTitle() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageTitle(1, ERROR_BLANK_TITLE)
        );
    }

    /** Tests setPageTitle with page number too large. */
    @Test
    void testEditPageTitleWithPageNumberTooLarge() {
        diaryEntry.addPage(TITLE_1, TEXT_1);
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageTitle(ERROR_PAGE_NUMBER_TOO_LARGE, ENTRY_TITLE_NEW)
        );
    }

    /** Tests setPageTitle on empty diary. */
    @Test
    void testEditPageTitleOnEmptyDiary() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setPageTitle(1, ENTRY_TITLE_NEW)
        );
    }

    /** Tests setEntryTitle with null title. */
    @Test
    void testEditEntryTitleWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setEntryTitle(ERROR_NULL_TITLE)
        );
    }

    /** Tests setEntryTitle with empty title. */
    @Test
    void testEditEntryTitleWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setEntryTitle(ERROR_EMPTY_TITLE)
        );
    }

    /** Tests setEntryTitle with blank title. */
    @Test
    void testEditEntryTitleWithBlankTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                diaryEntry.setEntryTitle(ERROR_BLANK_TITLE)
        );
    }
}
