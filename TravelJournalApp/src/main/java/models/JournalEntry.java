package models;

import models.Interfaces.IJournalEntry;

public class JournalEntry implements IJournalEntry {
    private int id;
    private String title;
    private String content;
    private Location location;
    private boolean isPublic;
    private double cost;
    private String imagePath;
    private int userId;

    public JournalEntry(int id, String title, String content, Location location, boolean isPublic, double cost, String imagePath, int userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.location = location;
        this.isPublic = isPublic;
        this.cost = cost;
        this.imagePath = imagePath;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setDescription(String description) {
        this.content = description;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public double getCost() {
        return cost;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", location=" + location +
                ", isPublic=" + isPublic +
                ", cost=" + cost +
                ", imagePath='" + imagePath + '\'' +
                ", userId=" + userId ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalEntry that = (JournalEntry) o;
        return id == that.id &&
                isPublic == that.isPublic &&
                Double.compare(that.cost, cost) == 0 &&
                title.equals(that.title) &&
                content.equals(that.content) &&
                location.equals(that.location) &&
                imagePath.equals(that.imagePath) &&
                userId == that.userId;
    }
    public void setVisibility(boolean isPublic) {
       this.isPublic=isPublic;
    }
    public static class EventEntry extends JournalEntry{
        private String eventDate;
        public EventEntry(int id, String title, String content, Location location, boolean isPublic, double cost, String imagePath, int userId,String eventDate){
            super(id,title,content,location,isPublic,cost,imagePath,userId);
            this.eventDate=eventDate;
        }
        public String getEventDate(){
            return eventDate;
        }
        @Override
        public String toString(){
            return super.toString()+", eventDate="+eventDate+" }";
        }

    }

}
