package pt.aptoide.backupapps.download.state;

import android.content.Context;
import pt.aptoide.backupapps.R;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-08-2013
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public enum EnumUploadFailReason {
  SUCCESS,

  CONNECTION_ERROR, SERVER_ERROR, SERVER_ERROR_MISSING_FILE, SERVER_ERROR_SCREENSHOTS, SERVER_ERROR_SCREENSHOTS_UPLOAD, SERVER_ERROR_MD5, SERVER_ERROR_ICON_UPLOAD, SERVER_ERROR_GRAPHIC_UPLOAD,

  MISSING_TOKEN, MISSING_APK, MISSING_APK_NAME, MISSING_DESCRIPTION, MISSING_RATING, MISSING_CATEGORY,

  BAD_LOGIN, BAD_TOKEN, BAD_REPO, BAD_APK, BAD_RATING, BAD_CATEGORY, BAD_WEBSITE, BAD_EMAIL, TOKEN_INCONSISTENT_WITH_REPO,

  NO_MD5, BAD_APK_UPLOAD, APK_TOO_BIG, APK_DUPLICATE, APK_INFECTED_WITH_VIRUS, APK_BLACKLISTED, OBB_PATCH_APK_NO_MD5, OBB_PATCH_MAIN_APK_NO_MD5, OBB_MAIN_APK_NO_MD5, OBB_PATCH_NO_MD5, OBB_PATCH_MAIN_NO_MD5, OBB_MAIN_NO_MD5,

  APK_EMPTY, HMAC_AUTHENTICATION_FAILURE,

  INVALID_UPLOAD_TYPE, UPLOAD_FROM_INVALID,

  EMPTY_OBB_MAIN_FILE, EMPTY_OBB_PATCH_FILE, INVALID_OBB_MAIN_FILE, INVALID_OBB_PATCH_FILE, OBB_MAIN_FILE_EXCEEDS_MAXIMUM_SIZE, OBB_PATCH_FILE_EXCEEDS_MAXIMUM_SIZE, OBB_MAIN_FILE_TRANSFER_STOPPED, OBB_PATCH_FILE_TRANSFER_STOPPED, OBB_MAIN_FILE_TRANSFER_FAILED, OBB_PATCH_FILE_TRANSFER_FAILED, OBB_MAIN_FILE_PROCESSING_PROBLEM, OBB_PATCH_FILE_PROCESSING_PROBLEM,

  INVALID_OBB_MAIN_NAME, INVALID_OBB_PATCH_NAME, OBB_MAIN_FILE_MD5_MISMATCH, OBB_PATCH_FILE_MD5_MISMATCH, OBB_MAIN_FILE_TYPE_MISMATCH, OBB_PATCH_FILE_TYPE_MISMATCH, OBB_MAIN_FILE_VERSION_MISMATCH, OBB_PATCH_FILE_VERSION_MISMATCH, OBB_MAIN_FILE_PACKAGE_MISMATCH, OBB_PATCH_FILE_PACKAGE_MISMATCH, PATCH_OBB_FILE_WITHOUT_MAIN_OBB, BAD_USER_CATEGORY, APK_MAXIMUM_SIZE, UNKONWN, APK_NOT_FOUND, ANTI_SPAM_RULE, INVALID_APK_ORIGIN, INVALID_APK_DATA, INVALID_ORIGIN_FOR_SIGNUP;

  public static EnumUploadFailReason reverseOrdinal(int ordinal) {
    return values()[ordinal];
  }

  public String toString(Context context) {
    switch (this) {
      case APK_NOT_FOUND:
        return context.getString(R.string.upload_fail_message_no_apk_found);
      case APK_DUPLICATE:
        return context.getString(R.string.upload_fail_short_apk_duplicate);
      case APK_INFECTED_WITH_VIRUS:
        return context.getString(R.string.upload_fail_short_apk_infected);
      case APK_TOO_BIG:
        return context.getString(R.string.upload_fail_short_apk_too_big);
      case APK_EMPTY:
        return context.getString(R.string.upload_fail_short_apk_empty);
      case BAD_APK:
        return context.getString(R.string.upload_fail_short_invalid_apk);
      case BAD_APK_UPLOAD:
        return context.getString(R.string.upload_fail_short_failed_apk_upload);
      case BAD_CATEGORY:
        return context.getString(R.string.upload_fail_short_invalid_category);
      case BAD_EMAIL:
        return context.getString(R.string.upload_fail_short_invalid_email);
      case BAD_LOGIN:
        return context.getString(R.string.login_message_check_login);
      case BAD_RATING:
        return context.getString(R.string.upload_fail_short_invalid_rating);
      case BAD_REPO:
        return context.getString(R.string.upload_fail_short_invalid_repo_name);
      case BAD_TOKEN:
        return context.getString(R.string.upload_fail_short_token_error);
      case BAD_WEBSITE:
        return context.getString(R.string.upload_fail_short_invalid_website);
      case APK_BLACKLISTED:
        return context.getString(R.string.upload_fail_short_apk_blacklisted);
      case CONNECTION_ERROR:
        return context.getString(R.string.upload_fail_short_failed_server_connection);
      case MISSING_APK:
        return context.getString(R.string.upload_fail_short_missing_apk);
      case MISSING_APK_NAME:
        return context.getString(R.string.upload_fail_short_enter_apk_name);
      case MISSING_CATEGORY:
        return context.getString(R.string.upload_fail_short_select_category);
      case MISSING_DESCRIPTION:
        return context.getString(R.string.upload_fail_short_enter_description);
      case MISSING_RATING:
        return context.getString(R.string.upload_fail_short_select_rating);
      case MISSING_TOKEN:
        return context.getString(R.string.upload_fail_short_missing_token);
      case SERVER_ERROR_GRAPHIC_UPLOAD:
        return context.getString(R.string.upload_fail_short_server_error_graphic_upload);
      case SERVER_ERROR_ICON_UPLOAD:
        return context.getString(R.string.upload_fail_short_server_error_icon_upload);
      case SERVER_ERROR_MD5:
        return context.getString(R.string.upload_fail_short_server_error_md5);
      case SERVER_ERROR_MISSING_FILE:
        return context.getString(R.string.upload_fail_short_server_error_apk);
      case SERVER_ERROR_SCREENSHOTS_UPLOAD:
        return context.getString(R.string.upload_fail_message_server_error_screenshots_upload);
      case SERVER_ERROR_SCREENSHOTS:
        return context.getString(R.string.upload_fail_short_server_error_screenshots);
      case SERVER_ERROR:
        return context.getString(R.string.server_connection_short_server_error);
      case TOKEN_INCONSISTENT_WITH_REPO:
        return context.getString(R.string.repo_message_not_associated_with_user);
      case SUCCESS:
        return context.getString(R.string.upload_short_success);
      case HMAC_AUTHENTICATION_FAILURE:
        return context.getString(R.string.upload_fail_short_hmac_invalid);
      case UPLOAD_FROM_INVALID:
        return context.getString(R.string.upload_fail_short_upload_from_invalid);
      case INVALID_UPLOAD_TYPE:
        return context.getString(R.string.upload_fail_short_invalid_upload_type);
      case EMPTY_OBB_MAIN_FILE:
        return context.getString(R.string.upload_fail_short_empty_obb_main_file);
      case EMPTY_OBB_PATCH_FILE:
        return context.getString(R.string.upload_fail_short_empty_obb_patch_file);
      case INVALID_OBB_MAIN_FILE:
        return context.getString(R.string.upload_fail_short_invalid_obb_main_file);
      case INVALID_OBB_PATCH_FILE:
        return context.getString(R.string.upload_fail_short_invalid_obb_patch_file);
      case OBB_MAIN_FILE_EXCEEDS_MAXIMUM_SIZE:
        return context.getString(R.string.upload_fail_message_obb_main_file_exceeds_max_size);
      case OBB_PATCH_FILE_EXCEEDS_MAXIMUM_SIZE:
        return context.getString(R.string.upload_fail_message_obb_patch_file_exceeds_max_size);
      case OBB_MAIN_FILE_TRANSFER_STOPPED:
        return context.getString(R.string.upload_fail_message_obb_main_stopped);
      case OBB_PATCH_FILE_TRANSFER_STOPPED:
        return context.getString(R.string.upload_fail_message_obb_patch_stopped);
      case OBB_MAIN_FILE_TRANSFER_FAILED:
        return context.getString(R.string.upload_fail_message_obb_main_transfer_stopped);
      case OBB_PATCH_FILE_TRANSFER_FAILED:
        return context.getString(R.string.upload_fail_message_obb_patch_transfer_stopped);
      case OBB_MAIN_FILE_PROCESSING_PROBLEM:
        return context.getString(R.string.upload_fail_message_obb_main_processing_problem);
      case OBB_PATCH_FILE_PROCESSING_PROBLEM:
        return context.getString(R.string.upload_fail_message_obb_patch_processing_problem);
      case INVALID_OBB_MAIN_NAME:
        return context.getString(R.string.upload_fail_message_invalid_obb_main_name);
      case INVALID_OBB_PATCH_NAME:
        return context.getString(R.string.upload_fail_message_invalid_obb_patch_name);
      case OBB_MAIN_FILE_MD5_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_main_md5_mismatch);
      case OBB_PATCH_FILE_MD5_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_patch_md5_mismatch);
      case OBB_MAIN_FILE_TYPE_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_main_type_mismatch);
      case OBB_PATCH_FILE_TYPE_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_patch_type_mismatch);
      case OBB_MAIN_FILE_VERSION_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_main_version_mismatch);
      case OBB_PATCH_FILE_VERSION_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_patch_version_mismatch);
      case OBB_MAIN_FILE_PACKAGE_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_main_package_mismatch);
      case OBB_PATCH_FILE_PACKAGE_MISMATCH:
        return context.getString(R.string.upload_fail_message_obb_patch_package_mismatch);
      case PATCH_OBB_FILE_WITHOUT_MAIN_OBB:
        return context.getString(R.string.upload_fail_mesage_patch_obb_without_main_obb);
      case ANTI_SPAM_RULE:
        return context.getString(R.string.upload_fail_message_anti_spam_rule);
      case INVALID_APK_ORIGIN:
        return context.getString(R.string.upload_fail_message_invalid_apk_origin);
      case INVALID_APK_DATA:
        return context.getString(R.string.upload_fail_message_invalid_apk_data);
      case INVALID_ORIGIN_FOR_SIGNUP:
        return context.getString(R.string.upload_fail_message_invalid_origin_for_signup);
      case UNKONWN:
        return context.getString(R.string.upload_failed_short_unknown_error);

      default:
        return context.getString(R.string.server_connection_short_server_error);
    }
  }

}
