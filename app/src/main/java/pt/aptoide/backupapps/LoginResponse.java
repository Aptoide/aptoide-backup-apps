package pt.aptoide.backupapps;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 31-07-2013
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class LoginResponse {

  EnumServerLoginStatus error = EnumServerLoginStatus.SUCCESS;
  private String token;
  private String storeAvatar;
  private String defaultStore;
  private String errorString;
  private boolean fromSignup;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getStoreAvatar() {
    return storeAvatar;
  }

  public void setStoreAvatar(String storeAvatar) {
    this.storeAvatar = storeAvatar;
  }

  public String getDefaultStore() {
    return defaultStore;
  }

  public void setDefaultStore(String defaultStore) {
    this.defaultStore = defaultStore;
  }

  public EnumServerLoginStatus getError() {
    return error;
  }

  public void setError(EnumServerLoginStatus error) {
    this.error = error;
  }

  public String getErrorString() {
    return errorString;
  }

  public void setErrorString(String errorString) {
    this.errorString = errorString;
  }

  public boolean isFromSignup() {
    return fromSignup;
  }

  public void setFromSignup(boolean fromSignup) {
    this.fromSignup = fromSignup;
  }
}
