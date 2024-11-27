package models;

public abstract class MediaContent {
    protected String title;
    protected String description;

    public MediaContent(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public abstract void display();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MediaContent{" + "title='" + title + '\'' + ", description='" + description + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaContent that = (MediaContent) o;
        return title.equals(that.title) && description.equals(that.description);
    }
}

