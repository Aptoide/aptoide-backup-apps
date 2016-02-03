package pt.aptoide.backupapps;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 26-08-2013
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class ApkPermission {
    private final String description;
    private final String permission;

    public ApkPermission(String permission, String description) {
        this.description = description;
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }
}
