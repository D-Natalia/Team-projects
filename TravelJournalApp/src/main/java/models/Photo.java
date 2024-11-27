package models;

public class Photo extends MediaContent  {
    private String resolution;

    public Photo(String title, String description, String resolution) {
        super(title, description);
        this.resolution = resolution;
    }


    public void display() {
        System.out.println("Displaying photo: " + title + " with resolution: " + resolution);
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return "Photo{" + "resolution='" + resolution + '\'' + "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Photo photo = (Photo) o;
        return resolution.equals(photo.resolution);
    }
}

