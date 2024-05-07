package data;

public enum Language {
    RU("Новости"),
    EN("News");

    public final String description;

    Language(String description) {
        this.description = description;
    }
}
