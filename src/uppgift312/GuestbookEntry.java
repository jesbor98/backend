package uppgift312;

public class GuestbookEntry {
    private String name;
    private String email;
    private String website;
    private String comment;

    public GuestbookEntry(String name, String email, String website, String comment) {
        this.name = name;
        this.email = email;
        this.website = website;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getComment() {
        return comment;
    }
}

