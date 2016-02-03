package pt.aptoide.backupapps.download;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-08-2013
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class ApkMetaData {

    private String name;
    private String packageName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getRating() {

        if(rating.equals(EnumRating.NONE)){
           return null;
        }

        return rating.ordinal()+"";
    }

    public void setRating(EnumRating rating) {
        this.rating = rating;
    }

    public String getApk_phone() {
        return apk_phone;
    }

    public void setApk_phone(String apk_phone) {
        this.apk_phone = apk_phone;
    }

    public String getApk_website() {
        return apk_website;
    }

    public void setApk_website(String apk_website) {
        this.apk_website = apk_website;
    }

    public String getApk_email() {
        return apk_email;
    }

    public void setApk_email(String apk_email) {
        this.apk_email = apk_email;
    }

    private String description;
    private long category = -1;
    private EnumRating rating = EnumRating.NONE;
    private String apk_phone;
    private String apk_website;
    private String apk_email;


    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}

