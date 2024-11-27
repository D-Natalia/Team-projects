package models.Interfaces;

import models.Location;

public interface IJournalEntry {
    public int getId();

    public String getTitle();

    public String getContent();
    public void setDescription(String description);

    public Location getLocation();

    public boolean isPublic();

    public double getCost();

    public String getImagePath();

    public int getUserId();
}
