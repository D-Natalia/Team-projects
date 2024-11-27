package models;

import models.Interfaces.IJournal;
import models.exceptions.EntryNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Journal implements IJournal {
    private List<JournalEntry> entries;

    public Journal() {
        entries = new ArrayList<>();
    }

    public void addEntry(JournalEntry entry) {
        entries.add(entry);
    }

    public JournalEntry findEntryById(int id) throws EntryNotFoundException {
        for (JournalEntry entry : entries) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        throw new EntryNotFoundException("Entry with ID " + id + " not found.");
    }

    public void removeEntryById(int id) throws EntryNotFoundException {
        JournalEntry entry = findEntryById(id);
        entries.remove(entry);
    }

    public List<JournalEntry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "Journal{" + "entries=" + entries + '}';
    }
}

