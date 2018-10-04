package pt.aptoide.backupapps.model;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 22-08-2013
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public class NewAccount {

  private String email;
  private String password;
  private String storeName;
  private boolean isPrivate;

  public NewAccount(String email, String password, String storeName, boolean aPrivate) {
    this.email = email;
    this.password = password;
    this.storeName = storeName;
    isPrivate = aPrivate;
  }

  public String getPassword() {
    return password;
  }

  public String getStoreName() {
    return storeName;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public String getEmail() {
    return email;
  }
}
