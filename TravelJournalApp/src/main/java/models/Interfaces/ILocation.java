package models.Interfaces;

public interface ILocation {

    public int getId();
    public void setId(int id);
    public String getCountry();
    public void setCountry(String country);
    public String getCity();
    public void setCity(String city);
    public String toString();
    public boolean equals(Object o);
    public int hashCode();
}
