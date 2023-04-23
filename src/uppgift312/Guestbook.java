package uppgift312;

public class Guestbook {
    private String name;
    private String email;
    private String website;
    private String comment;

    public Guestbook(String name, String email, String website, String comment) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s\n%s", name, email, website, comment);
    }
}

