package edu.ntnu.iir.bidata.ui.data;

import edu.ntnu.iir.bidata.ui.DiaryEntrySearchUi;

/**
 * result of a diary entry search.
 *
 * @param option Search option used.
 * @param data   Data resulting from search.
 */
public record EntrySearchResult(DiaryEntrySearchUi.ESearchOption option, Object data) { }
