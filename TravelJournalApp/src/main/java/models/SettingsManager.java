package models;

public class SettingsManager {
    private static SettingsManager instance;
    private String theme;
    private String language;
    private int fontSize;
    private SettingsManager(){
        this.theme="light";
        this.language="EN";
        this.fontSize=14;
    }
    public static SettingsManager getInstance(){
        if(instance==null){
            instance=new SettingsManager();
        }
        return instance;
    }
    public String getTheme(){
        return theme;
    }
    public void setTheme(String theme){
        this.theme=theme;
    }
    public String getLanguage(){
        return language;
    }
    public void setLanguage(String language){
        this.language=language;
    }
    public int getFontSize(){
        return fontSize;
    }
    public void setFontSize(int fontSize){
        this.fontSize=fontSize;
    }
    @Override
    public String toString() {
return "Settings: "+
        "  theme= "+theme+'\''+
        ", language= "+language+'\''+
        ", font size= "+fontSize;
    }
}
