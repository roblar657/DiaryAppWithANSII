package DiaryRegisterTest.PositiveTests;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryEntry;
import edu.ntnu.iir.bidata.registers.DiaryEntryRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DiaryEntryRegisterPositiveTests {

    private DiaryEntryRegister register;
    private Author author1;
    private Author author2;
    private DiaryEntry entry1;
    private DiaryEntry entry2;
    private DiaryEntry entry3;

    private static final String FIRST_NAME_1 = "Robert";
    private static final String LAST_NAME_1 = "Larsen";
    private static final String NICKNAME_1 = "robelar";

    private static final String FIRST_NAME_2 = "Ola";
    private static final String LAST_NAME_2 = "Nordmann";

    private static final String ENTRY_TITLE_1 = "Det";
    private static final String ENTRY_TITLE_2 = "var";
    private static final String ENTRY_TITLE_3 = "en gang";
    private static final String ENTRY_TITLE_4 = "morro";

    private static final String PAGE_TITLE_1 = "siden det";
    private static final String PAGE_TITLE_2 = "en";
    private static final String PAGE_TITLE_3 = "bakke";
    private static final String PAGE_TITLE_4 = "Nair";

    private static final int MAX_TEXT_LENGTH = 500;


    private static final String WORD_1 = "moro";
    private static final String WORD_2 = "glede";
    private static final String WORD_3 = "fest";
    private static final String WORD_UNKOWN = "mirror";


    private static final String PAGE_TEXT_1 = "tok " + WORD_1 + " " + WORD_1;
    private static final String PAGE_TEXT_2 = "tare " + WORD_1 + " " + WORD_2;
    private static final String PAGE_TEXT_3 = "trogg " + WORD_3;
    private static final String PAGE_TEXT_4 = "Vir " + WORD_1;

    @BeforeEach
    void setUp() {
        register = new DiaryEntryRegister();

        author1 = new Author(FIRST_NAME_1, LAST_NAME_1, NICKNAME_1);
        author2 = new Author(FIRST_NAME_2, LAST_NAME_2);

        register.addAuthor(author1);
        register.addAuthor(author2);


        entry1 = new DiaryEntry(author1, MAX_TEXT_LENGTH, ENTRY_TITLE_1);
        entry1.addPage(PAGE_TITLE_1, PAGE_TEXT_1);


        entry2 = new DiaryEntry(author1, MAX_TEXT_LENGTH, ENTRY_TITLE_2);
        entry2.addPage(PAGE_TITLE_2, PAGE_TEXT_2);


        entry3 = new DiaryEntry(author2, MAX_TEXT_LENGTH, ENTRY_TITLE_3);
        entry3.addPage(PAGE_TITLE_3, PAGE_TEXT_3);

        register.addDiaryEntry(entry1);
        register.addDiaryEntry(entry2);
        register.addDiaryEntry(entry3);
    }



    @Test
    void testGetDiaryEntriesByAuthor() {
        List<DiaryEntry> entries = register.getDiaryEntriesByAuthor(author1);
        assertEquals(2, entries.size());
        assertTrue(entries.contains(entry1));
        assertTrue(entries.contains(entry2));
    }

    @Test
    void testFindDiaryEntryFromAuthorByTitle() {
        Optional<DiaryEntry> found = register.findDiaryEntryFromAuthorByTitle(author1, ENTRY_TITLE_1);
        assertTrue(found.isPresent());
        assertEquals(entry1.getEntryTitle(), found.get().getEntryTitle());
    }

    @Test
    void testRemoveDiaryEntry() {
        Optional<DiaryEntry> removed = register.removeDiaryEntry(author1, ENTRY_TITLE_1);
        assertTrue(removed.isPresent());
        assertEquals(entry1.getEntryTitle(), removed.get().getEntryTitle());

        List<DiaryEntry> remaining = register.getDiaryEntriesByAuthor(author1);
        assertEquals(1, remaining.size());
        assertEquals(entry2.getEntryTitle(), remaining.getFirst().getEntryTitle());
    }

    @Test
    void testAddDiaryEntry() {
        DiaryEntry newEntry = new DiaryEntry(author2, MAX_TEXT_LENGTH, ENTRY_TITLE_4);
        newEntry.addPage(PAGE_TITLE_4, PAGE_TEXT_4);

        register.addDiaryEntry(newEntry);

        List<DiaryEntry> entries = register.getDiaryEntriesByAuthor(author2);
        assertEquals(2, entries.size());
        assertTrue(entries.contains(entry3));
        assertTrue(entries.stream().anyMatch(entry -> entry.getEntryTitle().equals(ENTRY_TITLE_4)));
    }

    @Test
    void testGetEntriesCreatedAtDateGroupedByAuthor() {
        LocalDate today = LocalDate.now();
        Map<Author, List<DiaryEntry>> results = register.getEntriesCreatedAtDateGroupedByAuthor(today);
        assertEquals(2, results.size());
        assertEquals(2, results.get(author1).size());
        assertEquals(1, results.get(author2).size());
    }

    @Test
    void testGetEntriesCreatedBetweenGroupedByAuthor() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        Map<Author, List<DiaryEntry>> results = register.getEntriesCreatedBetweenGroupedByAuthor(start, end);

        assertEquals(2, results.size());
        assertTrue(results.get(author1).contains(entry1));
        assertTrue(results.get(author1).contains(entry2));
        assertTrue(results.get(author2).contains(entry3));
    }

    @Test
    void testGetEntriesChangedBetweenGroupedByAuthor() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        Map<Author, List<DiaryEntry>> results = register.getEntriesChangedBetweenGroupedByAuthor(start, end);

        assertEquals(2, results.size());
        assertTrue(results.containsKey(author1));
        assertTrue(results.containsKey(author2));
    }

    @Test
    void testGetNumberOfEntries() {
        assertEquals(2, register.getNumberOfEntries(author1));
        assertEquals(1, register.getNumberOfEntries(author2));
    }

    @Test
    void testGetEntriesCreatedOrChangedBetweenGroupedByAuthor() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        Map<Author, List<DiaryEntry>> results = register.getEntriesCreatedOrChangedBetweenGroupedByAuthor(start, end);

        assertEquals(2, results.size());
        assertTrue(results.get(author1).contains(entry1));
        assertTrue(results.get(author2).contains(entry3));
    }



    @Test
    void testSearchForWordWord1() {
        List<DiaryEntry> results = register.searchForWord(WORD_1, 10);

        assertTrue(results.contains(entry1));
        assertTrue(results.contains(entry2));
        assertFalse(results.contains(entry3));


        assertEquals(entry1, results.getFirst());
    }

    @Test
    void testSearchForWordWord2() {
        List<DiaryEntry> results = register.searchForWord(WORD_2, 10);

        assertEquals(1, results.size());
        assertEquals(entry2, results.getFirst());
    }

    @Test
    void testSearchForWordWord3() {
        List<DiaryEntry> results = register.searchForWord(WORD_3, 10);

        assertEquals(1, results.size());
        assertEquals(entry3, results.getFirst());
    }

    @Test
    void testSearchForWordWithinLimit() {
        List<DiaryEntry> results = register.searchForWord(WORD_1, 1);

        assertEquals(1, results.size());
        assertEquals(entry1, results.getFirst());
    }

    @Test
    void testSearchForWordNoMatches() {
        List<DiaryEntry> results = register.searchForWord(WORD_UNKOWN, 10);
        assertTrue(results.isEmpty());
    }
}
