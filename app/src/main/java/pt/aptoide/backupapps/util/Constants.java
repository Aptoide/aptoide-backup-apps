/**
 * Constants, part of Aptoide
 * Copyright (C) 2011 Duarte Silveira
 * duarte.silveira@caixamagica.pt
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package pt.aptoide.backupapps.util;

import android.os.Environment;

/**
 * Constants, static java values repository
 *
 * @author dsilveira
 * @since 3.0
 */
public class Constants {

  public static final String TERMINAL_INFO = android.os.Build.MODEL
      + "("
      + android.os.Build.PRODUCT
      + ")"
      + ";v"
      + android.os.Build.VERSION.RELEASE
      + ";"
      + System.getProperty("os.arch");

  public static final String PATH_SDCARD = Environment.getExternalStorageDirectory()
      .getAbsolutePath();
  public static final String PATH_CACHE = PATH_SDCARD + "/.aptoide/";
  public static final String PATH_CACHE_APKS = PATH_CACHE + "apks/";

  public static final String DOMAIN_APTOIDE_STORE = ".store.aptoide.com/";

  public static final String URI_LOGIN_WS =
      "http://webservices.aptoide.com/webservices/2/checkUserCredentials";

  public static final String URI_UPLOAD_WS =
      "http://upload.webservices.aptoide.com/webservices/2/uploadAppToRepo";
  public static final String URI_LOGIN_CREATE_WS =
      "http://webservices.aptoide.com/webservices/createUser";

  public static final String URI_LATEST_VERSION_XML =
      "http://imgs.aptoide.com/latest_version_appsbackup.xml";

  public static final String APTOIDE_PACKAGE_NAME = "pt.aptoide.backupapps";
  public static final String APTOIDE_CLASS_NAME = APTOIDE_PACKAGE_NAME + ".Aptoide";

  public static final String LOGIN_USER_LOGIN = "login_username";
  public static final String LOGIN_USER_PASSWORD = "login_password";
  public static final String LOGIN_USER_TOKEN = "login_token";
  public static final String LOGIN_USER_DEFAULT_REPO = "login_default_repo";
  public static final String LOGIN_USER_AVATAR = "login_avatar";
  public static final String LOGIN_FROM_SIGNUP = "login_from_signup";
  public static final String LOGIN_USER_REPO_PRIVACY = "login_repo_privacy";
  public static final String LOGIN_USER_PRIVATE_REPO_USERNAME = "login_repo_username";
  public static final String LOGIN_USER_PRIVATE_REPO_PASSWORD = "login_repo_password";
  public static final String LOGGED_FROM_ACCOUNT_MANAGER = "Logged_from_accountManager";
}
