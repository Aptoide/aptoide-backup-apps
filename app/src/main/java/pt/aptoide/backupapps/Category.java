package pt.aptoide.backupapps;

/**
 * Created by rmateus on 02-05-2014.
 */
public class Category {

    private long id;
    private String name;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
