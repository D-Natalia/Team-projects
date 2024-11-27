package models.Interfaces;

import models.JournalEntry;
import models.exceptions.EntryNotFoundException;

import java.util.List;

public interface IJournal {


    public void addEntry(JournalEntry entry);
    public JournalEntry findEntryById(int id) throws EntryNotFoundException;
    public void removeEntryById(int id) throws EntryNotFoundException;
    public List<JournalEntry> getEntries();
    public String toString();
}
