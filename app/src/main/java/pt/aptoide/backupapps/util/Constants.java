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

import android.util.Log;
import android.os.Environment;

/**
 * Constants, static java values repository
 *
 * @author dsilveira
 * @since 3.0
 *
 */
public class Constants {

	public static final int LOG_LEVEL_FILTER = Log.DEBUG;

	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final int THIRD = 2;

	public static final int AVAILABLE = FIRST;
	public static final int INSTALLED = SECOND;
	public static final int UPDATES = THIRD;

	public static final int ARRAY_INDEX_FROM_SIZE_CORRECTION = 1;
	public static final int KBYTES_TO_BYTES = 1024;
	public static final int EMPTY_INT = 0;
	public static final int NO_SCREEN = 0;

	public static final int FIRST_ELEMENT = 0;
	public static final int SKIP_URI_PREFIX = 7;
	/** DISPLAY_SIZE_COMPARATOR (1/((1/480)*10lines)) used to determine visible apps rows from a reference screen size with a height of 480px, and density 1 **/
	public static final int DISPLAY_SIZE_COMPARATOR = 48;
	/** Number of icons to cache between list refreshes **/
	public static final int ICONS_REFRESH_INTERVAL = 3;
	/**	Increase trigger level from beginning of cache page, in proportion (x/(x+decreaseTriggerLevel)) of page size **/
//	public static final int DISPLAY_LISTS_PAGE_INCREASE_OFFSET_TRIGGER_PROPORTION_LEVEL = 1;
	/**	Decrease trigger level from beginning of cache page, in proportion (x/x+increaseTriggerLevel) of page size **/
//	public static final int DISPLAY_LISTS_PAGE_DECREASE_OFFSET_TRIGGER_PROPORTION_LEVEL = 19;
	/** increaseLevel+decreaseLevel 	cache pageSize = multiplier*numberOfVisibleListItems->device dependent **/
//	public static final int DISPLAY_LISTS_PAGE_SIZE_MULTIPLIER = (DISPLAY_LISTS_PAGE_INCREASE_OFFSET_TRIGGER_PROPORTION_LEVEL+DISPLAY_LISTS_PAGE_DECREASE_OFFSET_TRIGGER_PROPORTION_LEVEL);

	public static final int DISPLAY_LISTS_FAST_RESET_INCREASE_TRIGGER_MULTIPLIER = 6;
	public static final int DISPLAY_LISTS_PAGE_SIZE_MULTIPLIER = 3;
	/** number of cache pages to keep in memory **/
	public static final int DISPLAY_LISTS_CACHE_SIZE_PAGES_MULTIPLIER = 30;
	public static final int DISPLAY_LISTS_PAGE_INCREASE_TRIGGER_MULTIPLIER = DISPLAY_LISTS_CACHE_SIZE_PAGES_MULTIPLIER/2;
	public static final int DISPLAY_LISTS_PAGE_DECREASE_TRIGGER_MULTIPLIER = DISPLAY_LISTS_CACHE_SIZE_PAGES_MULTIPLIER+1;

	public static final int MAX_APPLICATIONS_IN_STATIC_LIST_MODE = 3000;
	public static final int APPLICATIONS_IN_EACH_INSERT = 500;
	public static final int MAX_PARALLEL_DOWNLOADS = 1;
	public static final int MAX_PARALLEL_UPOADS = 3;
	public static final int MAX_PARALLEL_SERVICE_REQUESTS = 4;

	/** miliseconds **/
	public static final int SERVER_CONNECTION_TIMEOUT = 5000;
	public static final int SERVER_READ_TIMEOUT = 30000;

	public static final int NUMBER_OF_STARS = 5;
	/** interval between repos updating, in hours **/
	public static final int REPOS_UPDATE_INTERVAL = 24;

	public static final int HOURS_TO_MILISECONDS = 1000*60*60;
	public static final String UTC_TIMEZONE = "UTC";

	public static final String TERMINAL_INFO = android.os.Build.MODEL + "("+ android.os.Build.PRODUCT + ")"
											+";v"+android.os.Build.VERSION.RELEASE+";"+System.getProperty("os.arch");
	public static final String USER_AGENT_FORMAT = "aptoideAppsBackup-%1$s;"+TERMINAL_INFO+";%2$s;id:%3$s;%4$s";

	public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String PATH_CACHE = PATH_SDCARD + "/.aptoide/";
	public static final String PATH_CACHE_REPOS = PATH_CACHE + "repos/";
	public static final String PATH_CACHE_APKS = PATH_CACHE + "apks/";
	public static final String PATH_CACHE_ICONS = PATH_CACHE + "icons/";
	public static final String PATH_CACHE_SCREENS = PATH_CACHE + "screens/";
	public static final String PATH_CACHE_MYAPPS = PATH_CACHE + "myapps/";

	public static final String PATH_REPO_INFO_XML = "info.xml?";
	public static final String PATH_REPO_EXTRAS_XML = "extras.xml?";
	public static final String PATH_REPO_STATS_XML = "stats.xml?";

	public static final String DOMAIN_APTOIDE_STORE = ".store.aptoide.com/";

	public static final String URI_SEARCH_BAZAAR = "http://m.aptoide.com/searchview.php?search=";
	public static final String URI_FORMAT_LOGIN_WS = "http://webservices.aptoide.com/webservices/checkUserCredentials/%1$s/%2$s/repo/%3$s/json";		//TODO adapt to multiple servers
	public static final String URI_FORMAT_LOGIN_DEFAULT_REPO_WS = "http://webservices.aptoide.com/webservices/checkUserCredentials/%1$s/%2$s/json";

    public static final String URI_LOGIN_WS = "http://webservices.aptoide.com/webservices/2/checkUserCredentials";

    public static final String URI_FORMAT_ADD_LIKE_WS = "http://webservices.aptoide.com/webservices/addApkLike/%1$s/%2$s/apphashid/%3$s/like/json";
	public static final String URI_FORMAT_ADD_DISLIKE_WS = "http://webservices.aptoide.com/webservices/addApkLike/%1$s/%2$s/apphashid/%3$s/dontlike/json";
	public static final String URI_FORMAT_COMMENTS_WS = "http://webservices.aptoide.com/webservices/listApkComments/%1$s/apphashid/%2$s/json";
	public static final String URI_ADD_COMMENT_POST_WS = "http://webservices.aptoide.com/webservices/addApkComment";
	public static final String URI_UPLOAD_WS = "http://upload.webservices.aptoide.com/webservices/2/uploadAppToRepo";
	public static final String URI_LOGIN_CREATE_WS = "http://webservices.aptoide.com/webservices/createUser";
    public static final String URI_GET_APK_INFO_WS = "http://webservices.aptoide.com/webservices/getApkInfo/%1$s/%2$s/%3$s/json";

	public static final String URI_LATEST_VERSION_XML = "http://imgs.aptoide.com/latest_version_appsbackup.xml";
	public static final String FILE_LATEST_VERSION_INFO = PATH_CACHE + "latestVersionInfo.xml";
	public static final String FILE_SELF_UPDATE = PATH_CACHE + "latestSelfUpdate.apk";	//TODO possibly change apk name to reflect version code
	public static final String FILE_PREFERENCES = "aptoide_preferences";

	public static final String APTOIDE_PACKAGE_NAME = "pt.aptoide.backupapps";
	public static final String APTOIDE_CLASS_NAME = APTOIDE_PACKAGE_NAME+".Aptoide";
	public static final String SERVICE_DATA_CLASS_NAME = APTOIDE_PACKAGE_NAME+".data.ServiceData";

	public static final String MIMETYPE_MYAPP = "application/vnd.cm.aptoide.pt";
	public static final String SCHEME_APTOIDE_REPO = "aptoiderepo";
	public static final String SCHEME_APTOIDE_XML = "aptoidexml";
	public static final String SCHEME_PACKAGE = "package";
	public static final String SCHEME_MARKET = "market";
	public static final String SCHEME_HTTPS = "https";
	public static final String SCHEME_HTTP_PREFIX = "http://";
	public static final String SCHEME_FILE_PREFIX = "file://";
	public static final String HOST_MARKET = "market.android.com";	//TODO support new play schema
//	public static final String QUERY_PARAMETER_ID = "id";
//	public static final String PREFIX_PNAME = "pname:";
//	public static final String PREFIX_PUB = "pub:";


	public static final String MYAPP_NEW_REPOS_WAITING = "myappNewRepos";


	public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
	public static final String FACEBOOK_APTOIDE_ADDRESS = "fb://profile/225295240870860";
	public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
	public static final String TWITTER_APTOIDE_URL = "http://www.twitter.com/aptoide";



	/**  repoHashid + uri + size + inUse + requiresLogin + login */
	public static final int NUMBER_OF_DISPLAY_FIELDS_REPO = 6;

	/**  appHashid + iconCachePath + appName + stars + downloads + upToDateVersionName  */
	public static final int NUMBER_OF_DISPLAY_FIELDS_APP_AVAILABLE = 6;
	/**  appHashid + iconCachePath + appName + installedVersionName + isUpdatable + upToDateVersionName + isDowngradable + downgradeVersionName  */
	public static final int NUMBER_OF_DISPLAY_FIELDS_APP_INSTALLED = 8;
	/**  appHashid + iconCachePath + appName + installedVersionName + upToDateVersionName  */
	public static final int NUMBER_OF_DISPLAY_FIELDS_APP_UPDATE = 5;


	public static final String DISPLAY_REPO_REQUIRES_LOGIN = "requires_login";
	public static final String DISPLAY_REPO_LOGIN = "login";
	public static final String DISPLAY_APP_ICON_CACHE_PATH = "iconCachePath";
	public static final String DISPLAY_APP_INSTALLED_VERSION_NAME = "installedVersionName";
	public static final String DISPLAY_APP_IS_UPDATABLE = "isUpdatable";
	public static final String DISPLAY_APP_UP_TO_DATE_VERSION_NAME = "upToDateVersionName";
	public static final String DISPLAY_APP_IS_DOWNGRADABLE = "isDowngradable";
	public static final String DISPLAY_APP_DOWNGRADE_VERSION_NAME = "downgradeVersionName";

	public static final String DISPLAY_APP_UP_TO_DATE_VERSION_CODE = "upToDateVersionCode";
	public static final String DISPLAY_APP_DOWNGRADE_VERSION_CODE = "downgradeVersionCode";

	public static final String DISPLAY_CATEGORY_APPS = "category_apps";

	//TODO create static display fields for the ones that are using keyes - change in unit tests and display views

	// **************************** Database definitions ********************************* //

	//TODO deprecate
	public static final String CATEGORY_APPLICATIONS = "Applications";
	public static final int CATEGORY_HASHID_APPLICATIONS = (CATEGORY_APPLICATIONS).hashCode();
	public static final String[] SUB_CATEGORIES_APPLICATIONS = {"Comics", "Communication", "Entertainment", "Finance", "Health", "Lifestyle", "Multimedia", "News&Weather", "Productivity"
															, "Reference", "Shopping", "Social", "Sports", "Themes", "Tools", "Travel", "Demo", "Software Libraries", "Business", "Weather"
															, "Travel&Local", "Transportation", "Medical", "Libraries&Demo", "News&Magazines", "Music&Audio", "Photography"
															, "Personalization", "Books&Reference", "Health&Fitness", "Media&Video", "Education"};
	//TODO deprecate
	public static final String CATEGORY_GAMES = "Games";
	public static final int CATEGORY_HASHID_GAMES = (CATEGORY_GAMES).hashCode();
	public static final String[] SUB_CATEGORIES_GAMES = {"Arcade&Action", "Brain&Puzzle", "Cards&Casino", "Casual", "Sports Games", "Racing"};

	//TODO deprecate
	public static final String CATEGORY_OTHERS = "Others";
	public static final int CATEGORY_HASHID_OTHERS = (CATEGORY_OTHERS).hashCode();


	public static final String APPS_REPO = SCHEME_HTTP_PREFIX+"apps"+DOMAIN_APTOIDE_STORE;
	public static final int APPS_REPO_HASHID = (APPS_REPO).hashCode();

	/** stupid sqlite doesn't know booleans */
	public static final int DB_TRUE = 1;
	/** stupid sqlite doesn't know booleans */
	public static final int DB_FALSE = 0;
	public static final int DB_ERROR = -1;
	/** 0 affected rows */
	public static final int DB_NO_CHANGES_MADE = 0;

	/** has no parent, so no parent hashid */
	public static final int TOP_CATEGORY = 0;

	public static final int COLUMN_FIRST = 0;
	public static final int COLUMN_SECOND = 1;
	public static final int COLUMN_THIRD = 2;
	public static final int COLUMN_FOURTH = 3;
	public static final int COLUMN_FIFTH = 4;
	public static final int COLUMN_SIXTH = 5;
	public static final int COLUMN_SEVENTH = 6;
	public static final int COLUMN_EIGTH = 7;
	public static final int COLUMN_NINTH = 8;
	public static final int COLUMN_TENTH = 9;
	public static final int COLUMN_ELEVENTH = 10;
	public static final int COLUMN_TWELVETH = 11;
	public static final int COLUMN_THERTEENTH = 12;
	public static final int COLUMN_FOURTEENTH = 13;



	/**
	 * 		HashIds are the hashcodes of the real E-A primary keys separated by pipe symbols.
	 *		Reasoning behind them is that sqlite is noticeably more efficient
	 *		handling integer indexes than text ones.
	 *		If for a table the primary key column is declared as INTEGER PRIMARY KEY
	 *		this value is used as rowid, thus searching for a value in this column takes
	 *		only one search in table's B-tree, making it twice as fast as any other index search.
	 *
	 *		Primary Key collision is a possibility due to java's hascode algorithm,
	 *		but, I expect, highly unlikely for our use case. Anyway, if it does happen,
	 *		hashids can still be used to speed up queries, we'll simply have to change db's PKs
	 *		to their actual entity keys to avoid collisions, or maybe add an auto-increment integer id.
	 *
	 *		For future reference:
	 *			for android <= 2.1 the version of sqlite is 3.5.9,
	 *			lacking the following features:
	 *				* Foreign key constraints	(only since 3.6.19)
	 *				* recursive triggers		(only since 3.6.18)
	 *				* Stored procedures
	 *				* multiple value inserts
	 *				* booleans
	 *				* Left inner joins
	 *				* full outer joins
	 *				* count(*distinct* column_name)
	 *				* ...
	 */

	/**  force compatibility with all android versions */
	public static final String PRAGMA_FOREIGN_KEYS_OFF = "PRAGMA foreign_keys=OFF;";
	/**  force compatibility with all android versions */
	public static final String PRAGMA_RECURSIVE_TRIGGERS_OFF = "PRAGMA recursive_triggers=OFF;";


	public static final String DATABASE = "aptoide_db";


	public static final String TABLE_REPOSITORY = "repository";
	/** base: uri */
	public static final String KEY_REPO_HASHID = "repo_hashid";
	public static final String KEY_REPO_URI = "uri";
	public static final String KEY_REPO_BASE_PATH = "base_path";
	/** relative path from basepath */
	public static final String KEY_REPO_ICONS_PATH = "icons_path";
	/** relative path from basepath */
	public static final String KEY_REPO_SCREENS_PATH = "screens_path";
	public static final String KEY_REPO_SIZE = "repo_size";
	/** identifies a single version of all non volatile xml files */
	public static final String KEY_REPO_DELTA = "delta";
	public static final String KEY_REPO_LAST_SYNCHRO = "last_synchro";
	public static final String KEY_REPO_IN_USE = "in_use";
	public static final int NUMBER_OF_COLUMNS_REPO = 9;


	public static final String TABLE_LOGIN = "login";
	public static final String KEY_LOGIN_REPO_HASHID = KEY_REPO_HASHID;
	public static final String KEY_LOGIN_USERNAME = "username";
	public static final String KEY_LOGIN_PASSWORD = "password";
	public static final int NUMBER_OF_COLUMNS_LOGIN = 3;


	public static final String TABLE_APPLICATION = "application";
	/** base: app_hashid|repo_hashid */
	public static final String KEY_APPLICATION_FULL_HASHID = "app_full_hashid";
	public static final String KEY_APPLICATION_REPO_HASHID = KEY_REPO_HASHID;
	/** base: package_name|versioncode */
	public static final String KEY_APPLICATION_HASHID = "app_hashid";
	public static final String KEY_APPLICATION_PACKAGE_NAME = "package_name";
	public static final String KEY_APPLICATION_VERSION_CODE = "version_code";
	public static final String KEY_APPLICATION_VERSION_NAME = "version_name";
	public static final String KEY_APPLICATION_NAME = "app_name";		//TODO maybe create index, consider changing columns order to increase lookup performance
	public static final String KEY_APPLICATION_TIMESTAMP = "timestamp";
	public static final String KEY_APPLICATION_RATING = "rating";
	/** min_screen_size defaults to small (supports all screens) **/
	public static final String KEY_APPLICATION_MIN_SCREEN = "min_screen";
	/** min_sdk_version defaults to 1 (supports all versions) **/
	public static final String KEY_APPLICATION_MIN_SDK = "min_sdk";
	/** min_gles defaults to 1.0 encoded acording to integer representation rules of gles float version id
	 * found in: http://developer.android.com/guide/topics/manifest/uses-feature-element.html#glEsVersion  **/
	public static final String KEY_APPLICATION_MIN_GLES = "min_gles";
//	public static final int NUMBER_OF_COLUMNS_APPLICATION = 12;
	public static final int NUMBER_OF_COLUMNS_APPLICATION = 11;


	public static final String TABLE_CATEGORY = "category";
	/** base: category_name */
	public static final String KEY_CATEGORY_HASHID = "category_hashid";
	public static final String KEY_CATEGORY_NAME = "category_name";		//TODO maybe create index, consider changing columns order to increase lookup performance
	public static final int NUMBER_OF_COLUMNS_CATEGORY = 2;


	public static final String TABLE_SUB_CATEGORY = "sub_category";
	public static final String KEY_SUB_CATEGORY_PARENT = "category_parent";
	public static final String KEY_SUB_CATEGORY_CHILD = "category_child";
	public static final int NUMBER_OF_COLUMNS_SUB_CATEGORY = 2;


	public static final String TABLE_APP_CATEGORY = "app_category";
	public static final String KEY_APP_CATEGORY_CATEGORY_HASHID = KEY_CATEGORY_HASHID;
	public static final String KEY_APP_CATEGORY_APP_FULL_HASHID = KEY_APPLICATION_FULL_HASHID;
	public static final int NUMBER_OF_COLUMNS_APP_CATEGORY = 2;


	public static final String TABLE_APP_TO_INSTALL = "app_to_install";
	public static final String KEY_APP_TO_INSTALL_HASHID = KEY_APPLICATION_HASHID;
	public static final String KEY_APP_TO_INSTALL_SCHEDULED = "scheduled";
	public static final int NUMBER_OF_COLUMNS_APP_TO_INSTALL = 2;


	public static final String TABLE_APP_INSTALLED = "app_installed";
	/** base: package_name|versioncode */
	public static final String KEY_APP_INSTALLED_HASHID = KEY_APPLICATION_HASHID;
	public static final String KEY_APP_INSTALLED_PACKAGE_NAME = "package_name";
	public static final String KEY_APP_INSTALLED_VERSION_CODE = "version_code";
	public static final String KEY_APP_INSTALLED_VERSION_NAME = "version_name";
	public static final String KEY_APP_INSTALLED_NAME = "app_name";		//TODO maybe create index, consider changing columns order to increase lookup performance
	public static final String KEY_APP_INSTALLED_TIMESTAMP = KEY_APPLICATION_TIMESTAMP;
	public static final String KEY_APP_INSTALLED_SIZE = "upload_size";
	/** ordinal of EnumAppInstalledType */
	public static final String KEY_APP_INSTALLED_TYPE = "type";
	public static final int NUMBER_OF_COLUMNS_APP_INSTALLED = 6;


	public static final String TABLE_APP_TO_NEVER_UPDATE = "app_to_never_update";
	public static final String KEY_APP_TO_NEVER_UPDATE_PACKAGE_NAME = KEY_APPLICATION_PACKAGE_NAME;
	public static final int NUMBER_OF_COLUMNS_APP_TO_NEVER_UPDATE = 1;


	public static final String TABLE_ICON_INFO = "icon_info";
	public static final String KEY_ICON_APP_FULL_HASHID = KEY_APPLICATION_FULL_HASHID;
	public static final String KEY_ICON_REMOTE_PATH_TAIL = "icon_remote_path_tail";
	public static final int NUMBER_OF_COLUMNS_ICON_INFO = 2;


	public static final String TABLE_SCREEN_INFO = "screen_info";
	public static final String KEY_SCREEN_APP_FULL_HASHID = KEY_APPLICATION_FULL_HASHID;
	public static final String KEY_SCREEN_ORDER_NUMBER = "screen_order_number";
	public static final String KEY_SCREEN_REMOTE_PATH_TAIL = "screen_remote_path_tail";
	public static final int NUMBER_OF_COLUMNS_SCREEN_INFO = 2;


	public static final String TABLE_DOWNLOAD_INFO = "download_info";
	public static final String KEY_DOWNLOAD_APP_FULL_HASHID = KEY_APPLICATION_FULL_HASHID;
	public static final String KEY_DOWNLOAD_REMOTE_PATH_TAIL = "remote_path_tail";
	public static final String KEY_DOWNLOAD_MD5HASH = "md5hash";
	public static final String KEY_DOWNLOAD_SIZE = "download_size";
	public static final int NUMBER_OF_COLUMNS_DOWNLOAD_INFO = 4;


	public static final String TABLE_STATS_INFO = "stats_info";
	public static final String KEY_STATS_APP_FULL_HASHID = KEY_APPLICATION_FULL_HASHID;
	public static final String KEY_STATS_DOWNLOADS = "downloads";
	/** receives only one xml tag: likes|dislikes that feeds these next 3 columns after processing */
	public static final String KEY_STATS_STARS = "stars";
	public static final String KEY_STATS_LIKES = "likes";
	public static final String KEY_STATS_DISLIKES = "dislikes";
	public static final int NUMBER_OF_COLUMNS_STATS_INFO = 5;


	public static final String TABLE_EXTRA_INFO = "extra_info";
	public static final String KEY_EXTRA_APP_FULL_HASHID = KEY_APPLICATION_FULL_HASHID;
	public static final String KEY_EXTRA_DESCRIPTION = "description";
	public static final int NUMBER_OF_COLUMNS_EXTRA_INFO = 2;


	public static final String TABLE_APP_COMMENTS = "app_comments";
	public static final String KEY_APP_COMMENTS_APP_FULL_HASHID = KEY_APPLICATION_FULL_HASHID;	//TODO create index
	public static final String KEY_APP_COMMENT_ID = "comment_id";
	public static final String KEY_APP_COMMENT = "comment";
	public static final int NUMBER_OF_COLUMNS_APP_COMMENTS = 3;


	/**
	 * Table definitions
	 *
	 */

	public static final String CREATE_TABLE_REPOSITORY = "CREATE TABLE IF NOT EXISTS " + TABLE_REPOSITORY + " ("
			+ KEY_REPO_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_REPO_URI + " TEXT NOT NULL, "
			+ KEY_REPO_BASE_PATH + " TEXT, "
			+ KEY_REPO_ICONS_PATH + " TEXT, "
			+ KEY_REPO_SCREENS_PATH + " TEXT, "
			+ KEY_REPO_SIZE + " INTEGER DEFAULT (0) CHECK ("+KEY_REPO_SIZE+">=0), "
			+ KEY_REPO_DELTA + " TEXT DEFAULT (0), "
			+ KEY_REPO_LAST_SYNCHRO + " INTEGER DEFAULT (0), "
			+ KEY_REPO_IN_USE + " INTEGER NOT NULL DEFAULT (1) ); ";		/** stupid sqlite doesn't know booleans */
//			+ "PRIMARY KEY("+ KEY_REPO_HASHID +") );";

	public static final String FOREIGN_KEY_UPDATE_REPO_REPO_HASHID_STRONG = "foreign_key_update_repo_repo_hashid_strong";
	public static final String FOREIGN_KEY_DELETE_REPO = "foreign_key_delete_repo";

	public static final String INDEX_REPOSITORY_IN_USE = "index_repository_in_use";


	public static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + " ("
			+ KEY_LOGIN_REPO_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_LOGIN_USERNAME + " TEXT NOT NULL, "
			+ KEY_LOGIN_PASSWORD + " TEXT NOT NULL, "
			+ "FOREIGN KEY("+ KEY_LOGIN_REPO_HASHID +") REFERENCES "+ TABLE_REPOSITORY +"("+ KEY_REPO_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_LOGIN_REPO_HASHID +") );";

	public static final String FOREIGN_KEY_INSERT_LOGIN = "foreign_key_insert_login_repo";
	public static final String FOREIGN_KEY_UPDATE_LOGIN_REPO_HASHID_WEAK = "foreign_key_update_login_repo_hashid_weak";





	public static final String FOREIGN_KEY_INSERT_APPLICATION = "foreign_key_insert_application";
	public static final String FOREIGN_KEY_UPDATE_APPLICATION_REPO_HASHID_WEAK = "foreign_key_update_application_repo_hashid_weak";

	public static final String FOREIGN_KEY_UPDATE_APPLICATION_APP_FULL_HASHID_STRONG = "foreign_key_update_application_app_full_hashid_strong";
	public static final String FOREIGN_KEY_DELETE_APPLICATION = "foreign_key_delete_application";

	public static final String INDEX_APPLICATION_HASHID = "index_application_hashid";
	public static final String INDEX_APPLICATION_REPO_HASHID = "index_application_repohashid";
	public static final String INDEX_APPLICATION_PACKAGE_NAME = "index_application_package_name";


	public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + " ("
			+ KEY_CATEGORY_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_CATEGORY_NAME + " TEXT NOT NULL); ";
//			+ "PRIMARY KEY("+ KEY_CATEGORY_HASHID +"));";

	public static final String FOREIGN_KEY_UPDATE_CATEGORY_CATEGORY_HASHID_STRONG = "foreign_key_update_category_hashid_strong";
	public static final String FOREIGN_KEY_DELETE_CATEGORY = "foreign_key_delete_category";



	public static final String CREATE_TABLE_SUB_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_SUB_CATEGORY + " ("
			+ KEY_SUB_CATEGORY_CHILD + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_SUB_CATEGORY_PARENT + " INTEGER NOT NULL, "
			+ "FOREIGN KEY("+ KEY_SUB_CATEGORY_PARENT +") REFERENCES "+ TABLE_CATEGORY +"("+ KEY_CATEGORY_HASHID +"),"
			+ "FOREIGN KEY("+ KEY_SUB_CATEGORY_CHILD +") REFERENCES "+ TABLE_CATEGORY +"("+ KEY_CATEGORY_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_SUB_CATEGORY_CHILD +"));";

	public static final String FOREIGN_KEY_INSERT_SUB_CATEGORY = "foreign_key_insert_sub_category";
	public static final String FOREIGN_KEY_UPDATE_SUB_CATEGORY_PARENT_WEAK = "foreign_key_update_sub_category_parent_weak";
	public static final String FOREIGN_KEY_UPDATE_SUB_CATEGORY_CHILD_WEAK = "foreign_key_update_sub_category_child_weak";



	public static final String CREATE_TABLE_APP_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_APP_CATEGORY + " ("
			+ KEY_APP_CATEGORY_APP_FULL_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_APP_CATEGORY_CATEGORY_HASHID + " INTEGER NOT NULL, "
			+ "FOREIGN KEY("+ KEY_APP_CATEGORY_CATEGORY_HASHID +") REFERENCES "+ TABLE_CATEGORY +"("+ KEY_CATEGORY_HASHID +"),"
			+ "FOREIGN KEY("+ KEY_APP_CATEGORY_APP_FULL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_APP_CATEGORY_APP_FULL_HASHID +"));";

	public static final String FOREIGN_KEY_INSERT_APP_CATEGORY = "foreign_key_insert_app_category";
	public static final String FOREIGN_KEY_UPDATE_APP_CATEGORY_CATEGORY_HASHID_WEAK = "foreign_key_update_app_category_category_hashid_weak";
	public static final String FOREIGN_KEY_UPDATE_APP_CATEGORY_APP_FULL_HASHID_WEAK = "foreign_key_update_app_category_app_full_hashid_weak";



	public static final String CREATE_TABLE_APP_TO_INSTALL = "CREATE TABLE IF NOT EXISTS " + TABLE_APP_TO_INSTALL + " ("
			+ KEY_APP_TO_INSTALL_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_APP_TO_INSTALL_SCHEDULED + " INTEGER DEFAULT (1) );";
//			+ "FOREIGN KEY("+ KEY_APP_TO_INSTALL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") ); "; //TODO

	public static final String FOREIGN_KEY_INSERT_APP_TO_INSTALL = "foreign_key_insert_app_to_install";
	public static final String FOREIGN_KEY_UPDATE_APP_TO_INSTALL_HASHID_WEAK = "foreign_key_update_app_to_install_hashid_weak";


	public static final String CREATE_TABLE_APP_INSTALLED = "CREATE TABLE IF NOT EXISTS " + TABLE_APP_INSTALLED + " ("
			+ KEY_APP_INSTALLED_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_APP_INSTALLED_PACKAGE_NAME + " TEXT NOT NULL, "
			+ KEY_APP_INSTALLED_VERSION_CODE + " INTEGER NOT NULL CHECK ("+KEY_APPLICATION_VERSION_CODE+">=0), "
			+ KEY_APP_INSTALLED_VERSION_NAME + " TEXT NOT NULL, "
			+ KEY_APP_INSTALLED_NAME + " TEXT NOT NULL,"
			+ KEY_APP_INSTALLED_TIMESTAMP + " INTEGER NOT NULL, "
			+ KEY_APP_INSTALLED_SIZE + " INTEGER NOT NULL, "
			+ KEY_APP_INSTALLED_TYPE + " INTEGER NOT NULL "
			+"); ";
//			+ "PRIMARY KEY("+ KEY_APP_INSTALLED_HASHID +") );";

	public static final String DROP_TABLE_APP_INSTALLED = "DROP TABLE IF EXISTS "+ TABLE_APP_INSTALLED;

	public static final String INDEX_APP_INSTALLED_PACKAGE_NAME = "index_app_installed_package_name";

	//TODO table never update pk = fk hashid from installed + triggers



	public static final String CREATE_TABLE_ICON_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_ICON_INFO + " ("
			+ KEY_ICON_APP_FULL_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_ICON_REMOTE_PATH_TAIL + " TEXT NOT NULL, "
			+ "FOREIGN KEY("+ KEY_ICON_APP_FULL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_ICON_APP_FULL_HASHID +"));";

	public static final String FOREIGN_KEY_INSERT_ICON_INFO = "foreign_key_insert_icon_info";
	public static final String FOREIGN_KEY_UPDATE_ICON_INFO_APP_FULL_HASHID_WEAK = "foreign_key_update_icon_info_app_full_hashid_weak";


	public static final String CREATE_TABLE_SCREEN_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_SCREEN_INFO + " ("
			+ KEY_SCREEN_APP_FULL_HASHID + " INTEGER NOT NULL, "
			+ KEY_SCREEN_ORDER_NUMBER +" INTEGER NOT NULL, "
			+ KEY_SCREEN_REMOTE_PATH_TAIL + " TEXT NOT NULL, "
			+ "FOREIGN KEY("+ KEY_SCREEN_APP_FULL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") "
			+ "PRIMARY KEY("+ KEY_SCREEN_APP_FULL_HASHID +","+KEY_SCREEN_ORDER_NUMBER+"));";

	public static final String FOREIGN_KEY_INSERT_SCREEN_INFO = "foreign_key_insert_screen_info";
	public static final String FOREIGN_KEY_UPDATE_SCREEN_INFO_APP_FULL_HASHID_WEAK = "foreign_key_update_screen_info_app_full_hashid_weak";


	public static final String CREATE_TABLE_DOWNLOAD_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_DOWNLOAD_INFO + " ("
			+ KEY_DOWNLOAD_APP_FULL_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_DOWNLOAD_REMOTE_PATH_TAIL + " TEXT NOT NULL, "
			+ KEY_DOWNLOAD_MD5HASH + " TEXT NOT NULL, "
			+ KEY_DOWNLOAD_SIZE + " INTEGER NOT NULL ," //TODO server send 1 if 0 CHECK ("+KEY_DOWNLOAD_SIZE+">0), "
			+ "FOREIGN KEY("+ KEY_DOWNLOAD_APP_FULL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_DOWNLOAD_APP_FULL_HASHID +") );";

	public static final String FOREIGN_KEY_INSERT_DOWNLOAD_INFO = "foreign_key_insert_download_info";
	public static final String FOREIGN_KEY_UPDATE_DOWNLOAD_INFO_APP_FULL_HASHID_WEAK = "foreign_key_update_download_info_app_full_hashid_weak";



	public static final String CREATE_TABLE_STATS_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_STATS_INFO + " ("
			+ KEY_STATS_APP_FULL_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_STATS_DOWNLOADS + " INTEGER CHECK ("+KEY_STATS_DOWNLOADS+">=0), "
			+ KEY_STATS_STARS + " REAL CHECK ("+KEY_STATS_STARS+">=0), "
			+ KEY_STATS_LIKES + " INTEGER CHECK ("+KEY_STATS_LIKES+">=0), "
			+ KEY_STATS_DISLIKES + " INTEGER CHECK ("+KEY_STATS_DISLIKES+">=0), "
			+ "FOREIGN KEY("+ KEY_STATS_APP_FULL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_STATS_APP_FULL_HASHID +") );";

	public static final String FOREIGN_KEY_INSERT_STATS_INFO = "foreign_key_insert_stats_info";
	public static final String FOREIGN_KEY_UPDATE_STATS_INFO_APP_FULL_HASHID_WEAK = "foreign_key_update_stats_info_app_full_hashid_weak";



	public static final String CREATE_TABLE_EXTRA_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_EXTRA_INFO + " ("
			+ KEY_EXTRA_APP_FULL_HASHID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_EXTRA_DESCRIPTION + " TEXT NOT NULL, "
			+ "FOREIGN KEY("+ KEY_EXTRA_APP_FULL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_EXTRA_APP_FULL_HASHID +") );";

	public static final String FOREIGN_KEY_INSERT_EXTRA_INFO = "foreign_key_insert_extra_info";
	public static final String FOREIGN_KEY_UPDATE_EXTRA_INFO_APP_FULL_HASHID_WEAK = "foreign_key_update_extra_info_app_full_hashid_weak";



	public static final String CREATE_TABLE_APP_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TABLE_APP_COMMENTS + " ("
			+ KEY_APP_COMMENT_ID + " INTEGER PRIMARY KEY NOT NULL CHECK ("+KEY_APP_COMMENT_ID+">0), "
			+ KEY_APP_COMMENTS_APP_FULL_HASHID + " INTEGER NOT NULL, "
			+ KEY_APP_COMMENT + " TEXT NOT NULL, "
			+ "FOREIGN KEY("+ KEY_APP_COMMENTS_APP_FULL_HASHID +") REFERENCES "+ TABLE_APPLICATION +"("+ KEY_APPLICATION_FULL_HASHID +") );";
//			+ "PRIMARY KEY("+ KEY_APP_COMMENT_ID +") );";

	public static final String FOREIGN_KEY_INSERT_APP_COMMENT = "foreign_key_insert_app_comment";
	public static final String FOREIGN_KEY_UPDATE_APP_COMMENT_APP_FULL_HASHID_WEAK = "foreign_key_update_app_comment_app_full_hashid_weak";



	/**
	 * Triggers,	Stupid sqlite only constraints foreign keys after 3.6.19 which means android 2.2
	 * 				only hope of implementing those constraints is by using triggers, as explained in this sqlite wiki webpage:
	 * 				http://www.sqlite.org/cvstrac/wiki?p=ForeignKeyTriggers	 *
	 */

	public static final String CREATE_TRIGGER_REPO_UPDATE_REPO_HASHID_STRONG = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_REPO_REPO_HASHID_STRONG
			+ " AFTER UPDATE OF "+ KEY_REPO_HASHID  +" ON " + TABLE_REPOSITORY
			+ " FOR EACH ROW BEGIN"
			+ "     UPDATE "+ TABLE_LOGIN +" SET "+ KEY_LOGIN_REPO_HASHID +" = NEW."+ KEY_REPO_HASHID +" WHERE "+ KEY_LOGIN_REPO_HASHID +" = OLD."+ KEY_REPO_HASHID +";"
			+ "     UPDATE "+ TABLE_APPLICATION +" SET "+ KEY_APPLICATION_REPO_HASHID +" = NEW."+ KEY_REPO_HASHID +" WHERE "+ KEY_APPLICATION_REPO_HASHID +" = OLD."+ KEY_REPO_HASHID +";"
			+ " END;";

	public static final String CREATE_TRIGGER_REPO_DELETE_REPO = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_DELETE_REPO
			+ " BEFORE DELETE ON " + TABLE_REPOSITORY
			+ " FOR EACH ROW BEGIN"
			+ "     DELETE FROM "+ TABLE_LOGIN +" WHERE "+ KEY_LOGIN_REPO_HASHID +" = OLD."+ KEY_REPO_HASHID +";"
			+ "     DELETE FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_REPO_HASHID +" = OLD."+ KEY_REPO_HASHID +";"	//TODO since there are no chained triggers check if this correctly deletes all app's childs
			+ " END;";




	public static final String CREATE_TRIGGER_LOGIN_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_LOGIN
			+ " BEFORE INSERT ON " + TABLE_LOGIN
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_LOGIN +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_LOGIN +"')"
			+ "     WHERE NEW."+ KEY_LOGIN_REPO_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_REPO_HASHID +" FROM "+ TABLE_REPOSITORY +" WHERE "+ KEY_REPO_HASHID +" = NEW."+ KEY_LOGIN_REPO_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_LOGIN_UPDATE_REPO_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_LOGIN_REPO_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_LOGIN_REPO_HASHID  +" ON " + TABLE_LOGIN
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_LOGIN +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_LOGIN_REPO_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_REPO_HASHID +" FROM "+ TABLE_REPOSITORY +" WHERE "+ KEY_REPO_HASHID +" = NEW."+ KEY_LOGIN_REPO_HASHID +") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_APPLICATION_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_APPLICATION
			+ " BEFORE INSERT ON " + TABLE_APPLICATION
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_APPLICATION +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_APPLICATION +"')"
			+ "     WHERE NEW."+ KEY_APPLICATION_REPO_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_REPO_HASHID +" FROM "+ TABLE_REPOSITORY +" WHERE "+ KEY_REPO_HASHID +" = NEW."+ KEY_APPLICATION_REPO_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_APPLICATION_UPDATE_REPO_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_APPLICATION_REPO_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_APPLICATION_REPO_HASHID  +" ON " + TABLE_APPLICATION
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_APPLICATION +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_APPLICATION_REPO_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_REPO_HASHID +" FROM "+ TABLE_REPOSITORY +" WHERE "+ KEY_REPO_HASHID +" = NEW."+ KEY_APPLICATION_REPO_HASHID +") IS NULL;"
			+ " END;";


	public static final String CREATE_TRIGGER_APPLICATION_UPDATE_APP_FULL_HASHID_STRONG = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_APPLICATION_APP_FULL_HASHID_STRONG
			+ " AFTER UPDATE OF "+ KEY_APPLICATION_FULL_HASHID  +" ON " + TABLE_APPLICATION
			+ " FOR EACH ROW BEGIN"
			+ "     UPDATE "+ TABLE_APP_CATEGORY +" SET "+ KEY_APP_CATEGORY_APP_FULL_HASHID +" = NEW."+ KEY_APPLICATION_FULL_HASHID +" WHERE "+ KEY_APP_CATEGORY_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     UPDATE "+ TABLE_ICON_INFO +" SET "+ KEY_ICON_APP_FULL_HASHID +" = NEW."+ KEY_APPLICATION_FULL_HASHID +" WHERE "+ KEY_ICON_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     UPDATE "+ TABLE_DOWNLOAD_INFO +" SET "+ KEY_DOWNLOAD_APP_FULL_HASHID +" = NEW."+ KEY_APPLICATION_FULL_HASHID +" WHERE "+ KEY_DOWNLOAD_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     UPDATE "+ TABLE_STATS_INFO +" SET "+ KEY_STATS_APP_FULL_HASHID +" = NEW."+ KEY_APPLICATION_FULL_HASHID +" WHERE "+ KEY_STATS_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     UPDATE "+ TABLE_EXTRA_INFO +" SET "+ KEY_EXTRA_APP_FULL_HASHID +" = NEW."+ KEY_APPLICATION_FULL_HASHID +" WHERE "+ KEY_EXTRA_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ " END;";

	public static final String CREATE_TRIGGER_APPLICATION_DELETE = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_DELETE_APPLICATION
			+ " BEFORE DELETE ON " + TABLE_APPLICATION
			+ " FOR EACH ROW BEGIN"
			+ "     DELETE FROM "+ TABLE_APP_CATEGORY +" WHERE "+ KEY_APP_CATEGORY_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     DELETE FROM "+ TABLE_ICON_INFO +" WHERE "+ KEY_ICON_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     DELETE FROM "+ TABLE_DOWNLOAD_INFO +" WHERE "+ KEY_DOWNLOAD_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     DELETE FROM "+ TABLE_STATS_INFO +" WHERE "+ KEY_STATS_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     DELETE FROM "+ TABLE_EXTRA_INFO +" WHERE "+ KEY_EXTRA_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ "     DELETE FROM "+ TABLE_SCREEN_INFO +" WHERE "+ KEY_SCREEN_APP_FULL_HASHID +" = OLD."+ KEY_APPLICATION_FULL_HASHID +";"
			+ " END;";




	public static final String CREATE_TRIGGER_CATEGORY_UPDATE_CATEGORY_HASHID_STRONG = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_CATEGORY_CATEGORY_HASHID_STRONG
			+ " AFTER UPDATE OF "+ KEY_CATEGORY_HASHID  +" ON " + TABLE_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "     UPDATE "+ TABLE_SUB_CATEGORY +" SET "+ KEY_SUB_CATEGORY_PARENT +" = NEW."+ KEY_CATEGORY_HASHID +" WHERE "+ KEY_SUB_CATEGORY_PARENT +" = OLD."+ KEY_CATEGORY_HASHID +";"
			+ "     UPDATE "+ TABLE_SUB_CATEGORY +" SET "+ KEY_SUB_CATEGORY_PARENT +" = NEW."+ KEY_CATEGORY_HASHID +" WHERE "+ KEY_SUB_CATEGORY_CHILD +" = OLD."+ KEY_CATEGORY_HASHID +";"
			+ "     UPDATE "+ TABLE_APP_CATEGORY +" SET "+ KEY_APP_CATEGORY_CATEGORY_HASHID +" = NEW."+ KEY_CATEGORY_HASHID+" WHERE "+ KEY_APP_CATEGORY_CATEGORY_HASHID +" = OLD."+ KEY_CATEGORY_HASHID +";"
			+ " END;";

	public static final String CREATE_TRIGGER_CATEGORY_DELETE = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_DELETE_CATEGORY
			+ " BEFORE DELETE ON " + TABLE_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "     DELETE FROM "+ TABLE_SUB_CATEGORY +" WHERE "+ KEY_SUB_CATEGORY_PARENT +" = OLD."+ KEY_CATEGORY_HASHID +";"
			+ "     DELETE FROM "+ TABLE_SUB_CATEGORY +" WHERE "+ KEY_SUB_CATEGORY_CHILD +" = OLD."+ KEY_CATEGORY_HASHID +";"
			+ "     DELETE FROM "+ TABLE_APP_CATEGORY +" WHERE "+ KEY_APP_CATEGORY_CATEGORY_HASHID +" = OLD."+ KEY_CATEGORY_HASHID +";"
			+ " END;";




	public static final String CREATE_TRIGGER_SUB_CATEGORY_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_SUB_CATEGORY
			+ " BEFORE INSERT ON " + TABLE_SUB_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_SUB_CATEGORY +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_SUB_CATEGORY +"')"
			+ "     WHERE (NEW."+ KEY_SUB_CATEGORY_PARENT +" IS NOT NULL"
			+ "	        	AND (SELECT "+ KEY_CATEGORY_HASHID +" FROM "+ TABLE_CATEGORY +" WHERE "+ KEY_CATEGORY_HASHID +" = NEW."+ KEY_SUB_CATEGORY_PARENT +") IS NULL)"
			+ "			OR (NEW."+ KEY_SUB_CATEGORY_CHILD +" IS NOT NULL"
			+ "	        	AND (SELECT "+ KEY_CATEGORY_HASHID +" FROM "+ TABLE_CATEGORY +" WHERE "+ KEY_CATEGORY_HASHID +" = NEW."+ KEY_SUB_CATEGORY_CHILD +") IS NULL);"
			+ " END;";

	public static final String CREATE_TRIGGER_SUB_CATEGORY_UPDATE_PARENT_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_SUB_CATEGORY_PARENT_WEAK
			+ " BEFORE UPDATE OF "+ KEY_SUB_CATEGORY_PARENT  +" ON " + TABLE_SUB_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_SUB_CATEGORY +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_SUB_CATEGORY_PARENT_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_CATEGORY_HASHID +" FROM "+ TABLE_CATEGORY +" WHERE "+ KEY_CATEGORY_HASHID +" = NEW."+ KEY_SUB_CATEGORY_PARENT +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_SUB_CATEGORY_UPDATE_CHILD_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_SUB_CATEGORY_CHILD_WEAK
			+ " BEFORE UPDATE OF "+ KEY_SUB_CATEGORY_CHILD  +" ON " + TABLE_SUB_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_SUB_CATEGORY +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_SUB_CATEGORY_CHILD_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_CATEGORY_HASHID +" FROM "+ TABLE_CATEGORY +" WHERE "+ KEY_CATEGORY_HASHID +" = NEW."+ KEY_SUB_CATEGORY_CHILD +") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_APP_CATEGORY_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_APP_CATEGORY
			+ " BEFORE INSERT ON " + TABLE_APP_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_APP_CATEGORY +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_APP_CATEGORY +"')"
			+ "     WHERE (NEW."+ KEY_APP_CATEGORY_APP_FULL_HASHID +" IS NOT NULL"
			+ "	        	AND (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_APP_CATEGORY_APP_FULL_HASHID +") IS NULL)"
			+ "			OR (NEW."+ KEY_APP_CATEGORY_CATEGORY_HASHID +" IS NOT NULL"
			+ "	        	AND (SELECT "+ KEY_CATEGORY_HASHID +" FROM "+ TABLE_CATEGORY +" WHERE "+ KEY_CATEGORY_HASHID +" = NEW."+ KEY_APP_CATEGORY_CATEGORY_HASHID +") IS NULL);"
			+ " END;";

	public static final String CREATE_TRIGGER_APP_CATEGORY_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_APP_CATEGORY_APP_FULL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_APP_CATEGORY_APP_FULL_HASHID +" ON " + TABLE_APP_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_APP_CATEGORY +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_APP_CATEGORY_APP_FULL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_APP_CATEGORY_APP_FULL_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_APP_CATEGORY_UPDATE_CATEGORY_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_APP_CATEGORY_CATEGORY_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_APP_CATEGORY_CATEGORY_HASHID  +" ON " + TABLE_APP_CATEGORY
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_APP_CATEGORY +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_APP_CATEGORY_CATEGORY_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_CATEGORY_HASHID +" FROM "+ TABLE_CATEGORY +" WHERE "+ KEY_CATEGORY_HASHID +" = NEW."+ KEY_APP_CATEGORY_CATEGORY_HASHID +") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_APP_TO_INSTALL_INSERT = "CREATE TRIGGER IF NOT EXISTS "+FOREIGN_KEY_INSERT_APP_TO_INSTALL
			+ " BEFORE INSERT ON "+TABLE_APP_TO_INSTALL
			+ " FOR EACH ROW BEGIN"
			+ "		SELECT RAISE(ROLLBACK, 'insert on table "+TABLE_APP_TO_INSTALL+" violates foreign key constraint "+FOREIGN_KEY_INSERT_APP_TO_INSTALL+"')"
			+ "     WHERE NEW."+KEY_APP_TO_INSTALL_HASHID+" IS NOT NULL"
			+ "	        AND (SELECT "+KEY_APPLICATION_HASHID+" FROM "+TABLE_APPLICATION+" WHERE "+KEY_APPLICATION_HASHID+" = NEW."+KEY_APP_TO_INSTALL_HASHID+") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_APP_TO_INSTALL_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_APP_TO_INSTALL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_APP_TO_INSTALL_HASHID  +" ON " + TABLE_APP_TO_INSTALL
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_APP_TO_INSTALL +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_APP_TO_INSTALL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_HASHID +" = NEW."+ KEY_APP_TO_INSTALL_HASHID +") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_ICON_INFO_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_ICON_INFO
			+ " BEFORE INSERT ON " + TABLE_ICON_INFO
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_ICON_INFO +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_ICON_INFO +"')"
			+ "     WHERE NEW."+ KEY_ICON_APP_FULL_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_ICON_APP_FULL_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_ICON_INFO_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_ICON_INFO_APP_FULL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_ICON_APP_FULL_HASHID  +" ON " + TABLE_ICON_INFO
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_ICON_INFO +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_ICON_INFO_APP_FULL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_ICON_APP_FULL_HASHID +") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_SCREEN_INFO_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_SCREEN_INFO
			+ " BEFORE INSERT ON " + TABLE_SCREEN_INFO
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_SCREEN_INFO +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_SCREEN_INFO +"')"
			+ "     WHERE NEW."+ KEY_SCREEN_APP_FULL_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_SCREEN_APP_FULL_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_SCREEN_INFO_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_SCREEN_INFO_APP_FULL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_SCREEN_APP_FULL_HASHID  +" ON " + TABLE_SCREEN_INFO
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_SCREEN_INFO +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_SCREEN_INFO_APP_FULL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_SCREEN_APP_FULL_HASHID +") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_DOWNLOAD_INFO_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_DOWNLOAD_INFO
			+ " BEFORE INSERT ON " + TABLE_DOWNLOAD_INFO
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_DOWNLOAD_INFO +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_DOWNLOAD_INFO +"')"
			+ "     WHERE NEW."+ KEY_DOWNLOAD_APP_FULL_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_DOWNLOAD_APP_FULL_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_DOWNLOAD_INFO_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_DOWNLOAD_INFO_APP_FULL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_DOWNLOAD_APP_FULL_HASHID  +" ON " + TABLE_DOWNLOAD_INFO
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_DOWNLOAD_INFO +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_DOWNLOAD_INFO_APP_FULL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_DOWNLOAD_APP_FULL_HASHID +") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_STATS_INFO_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_STATS_INFO
			+ " BEFORE INSERT ON " + TABLE_STATS_INFO
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_STATS_INFO +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_STATS_INFO +"')"
			+ "     WHERE NEW."+ KEY_STATS_APP_FULL_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_STATS_APP_FULL_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_STATS_INFO_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_STATS_INFO_APP_FULL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_STATS_APP_FULL_HASHID  +" ON " + TABLE_STATS_INFO
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_STATS_INFO +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_STATS_INFO_APP_FULL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_STATS_APP_FULL_HASHID+") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_EXTRA_INFO_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_EXTRA_INFO
			+ " BEFORE INSERT ON " + TABLE_EXTRA_INFO
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_EXTRA_INFO +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_EXTRA_INFO +"')"
			+ "     WHERE NEW."+ KEY_EXTRA_APP_FULL_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_EXTRA_APP_FULL_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_EXTRA_INFO_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_EXTRA_INFO_APP_FULL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_EXTRA_APP_FULL_HASHID  +" ON " + TABLE_EXTRA_INFO
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_EXTRA_INFO +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_EXTRA_INFO_APP_FULL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_EXTRA_APP_FULL_HASHID+") IS NULL;"
			+ " END;";




	public static final String CREATE_TRIGGER_APP_COMMENT_INSERT = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_INSERT_APP_COMMENT
			+ " BEFORE INSERT ON " + TABLE_APP_COMMENTS
			+ " FOR EACH ROW BEGIN"
			+ "     SELECT RAISE(ROLLBACK, 'insert on table "+ TABLE_APP_COMMENTS +" violates foreign key constraint "+ FOREIGN_KEY_INSERT_APP_COMMENT +"')"
			+ "     WHERE NEW."+ KEY_APP_COMMENTS_APP_FULL_HASHID +" IS NOT NULL"
			+ "	        AND (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_APP_COMMENTS_APP_FULL_HASHID +") IS NULL;"
			+ " END;";

	public static final String CREATE_TRIGGER_APP_COMMENT_UPDATE_APP_FULL_HASHID_WEAK = "CREATE TRIGGER IF NOT EXISTS "+ FOREIGN_KEY_UPDATE_APP_COMMENT_APP_FULL_HASHID_WEAK
			+ " BEFORE UPDATE OF "+ KEY_APP_COMMENTS_APP_FULL_HASHID  +" ON " + TABLE_APP_COMMENTS
			+ " FOR EACH ROW BEGIN"
			+ "    SELECT RAISE(ROLLBACK, 'update on table "+ TABLE_APP_COMMENTS +" violates foreign key constraint "+ FOREIGN_KEY_UPDATE_APP_COMMENT_APP_FULL_HASHID_WEAK +"')"
			+ "    WHERE (SELECT "+ KEY_APPLICATION_FULL_HASHID +" FROM "+ TABLE_APPLICATION +" WHERE "+ KEY_APPLICATION_FULL_HASHID +" = NEW."+ KEY_APP_COMMENTS_APP_FULL_HASHID+") IS NULL;"
			+ " END;";



	public static final String CREATE_INDEX_APPLICATION_HASHID = "CREATE INDEX IF NOT EXISTS "+INDEX_APPLICATION_HASHID+" ON "+TABLE_APPLICATION+" ("+KEY_APPLICATION_HASHID+" ASC);";
	public static final String CREATE_INDEX_APPLICATION_REPO_HASHID = "CREATE INDEX IF NOT EXISTS "+INDEX_APPLICATION_REPO_HASHID+" ON "+TABLE_APPLICATION+" ("+KEY_APPLICATION_REPO_HASHID+" ASC);";
	public static final String CREATE_INDEX_APPLICATION_PACKAGE_NAME = "CREATE INDEX IF NOT EXISTS "+INDEX_APPLICATION_PACKAGE_NAME+" ON "+TABLE_APPLICATION+" ("+KEY_APPLICATION_PACKAGE_NAME+" ASC);";

	public static final String CREATE_INDEX_APP_INSTALLED_PACKAGE_NAME = "CREATE INDEX IF NOT EXISTS "+INDEX_APP_INSTALLED_PACKAGE_NAME+" ON "+TABLE_APP_INSTALLED+" ("+KEY_APP_INSTALLED_PACKAGE_NAME+" ASC);";



	public static final String ANALYZE = "ANALYZE";
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
