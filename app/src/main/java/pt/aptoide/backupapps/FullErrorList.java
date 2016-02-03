package pt.aptoide.backupapps;

import java.util.HashMap;

import pt.aptoide.backupapps.download.state.EnumUploadFailReason;

/**
 * Created by rmateus on 30-04-2014.
 */
public class FullErrorList {

     /* for reference
        AUTH-4	"HMAC Authentication failure"	hmac invalid
        MARG-3	"Missing apkversion parameter"	apkversion not provided
        MARG-5	"Missing apk parameter"	apk not provided
        MARG-100	"Missing apkname parameter"	apkname not provided
        MARG-101	"Missing description parameter"	description not provided
        MARG-102	"Missing category parameter"	category not provided
        MARG-103	"Missing rating parameter"	rating not provided
        IARG-1	"Invalid repo"	repo does not exist in the system
        IARG-2	"Invalid token"	token does not exist in the system
        IARG-103	"Invalid category!"	category does not exist in the system
        IARG-104	"Invalid rating!"	rating does not exist in the system
        IARG-105	"The website is not a valid URL."	apk_website is not a valid URL value
        IARG-106	"The e-mail address is not valid."	apk_email is not a valid email value
        IARG-202	"Invalid upload_from!"	Invalid upload_from provided
        IARG-300	"Invalid user_categs!"	Invalid user_categs provided
        APK-5	"MD5 NOT existent"	No viable application found with the provided apk_md5sum
        APK-101	"Due to Intelectual Property reasons, it's not possible to upload the required APK. If you are the developer/owner of the application, please contact Aptoide Staff."	Apk blacklisted
        APK-102	"It's not possible to upload the required APK since an infection was detected. If you are the developer/owner of the application, please contact Aptoide Staff."	Apk infected
        APK-103	"Application duplicate: the uploaded apk already exists in this repository"	Apk duplicated in store
        FILE-1	"The file you uploaded is empty."	The file uploaded has no contents
        FILE-2	"The file you uploaded exceeds the maximum allowed size."	The file upload exceeds the maximum allowed file size
        FILE-3	"The file transfer stopped before the upload was complete. Please try again."	The file upload was interrupted
        FILE-4	"The file transfer failed for some unknown reason. Please try again."	The file upload failed for an unknown reason
        FILE-5	"The file you sent is missing. Please upload the file again."	The file upload is missing
        FILE-100	"You need to upload an APK"	The file must be an APK
        FILE-101	"The OBB format upload is not currently supported."	OBB upload not supported
        FILE-102	"One or more screenshots failed to be uploaded."	One or more screenshots weren't uploaded correctly or may have an incorrect format
        FILE-200	"An invalid APK was received, does not seem to contain all required data. Please verify that you selected the right file, and try again."	No data extracted or no permissions found in the apk file
        FILE-201	"An invalid APK was received, does not seem to contain data about the name, version code or version name. Please verify that you selected the right file, and try again."	Name, version code or version name not found in the apk file
        FILE-202	"An invalid APK was received, does not seem to contain data about the label or icon. Please verify that you selected the right file, and try again."	Label or icon not found in the apk file
        FILE-203	"An invalid APK was received. Please verify that you selected the right file, and try again."	Invalid apk file (unknown error)
        FILE-204	"An invalid APK was received, does not seem to be a ZIP file or seems to have some errors. Please verify that you selected the right file, and try again."	Invalid or corrupted apk file
        FILE-205	"An invalid APK was received, seems to have duplicate files. Please verify the apk and try again."	Apk file with duplicated files (signature exploit detection)
        FILE-206	"An invalid APK was received, does not seem to be a ZIP file or seems to be missing icon data. Please verify that you selected the right file, and try again."	Invalid or corrupted icon from apk file
        FILE-300	"The OBB main file you uploaded is empty"	Empty OBB main file received
        FILE-301	"The OBB patch file you uploaded is empty"	Empty OBB patch file received
        FILE-302	"The OBB main file you uploaded exceeds the maximum allowed size."	OBB main file exceeds the max allowed file size
        FILE-303	"The OBB patch file you uploaded exceeds the maximum allowed size."	OBB patch file exceeds the max allowed file size
        FILE-304	"You need to upload a main OBB"	OBB main file sent is not an OBB
        FILE-305	"You need to upload a patch OBB"	OBB patch file sent is not an OBB
        FILE-306	"The uploaded main file does not have a valid OBB name."	OBB main file sent does not have the correct naming convention
        FILE-307	"The uploaded patch file does not have a valid OBB name."	OBB patch file sent does not have the correct naming convention
        FILE-308	"The OBB main file transfer stopped before the upload was complete. Please try again."	The OBB main file upload was interrupted
        FILE-309	"The OBB patch file transfer stopped before the upload was complete. Please try again."	The OBB patch file upload was interrupted
        FILE-310	"The OBB main file transfer does not have any file."	No OBB main file received
        FILE-311	"The OBB patch file transfer does not have any file."	No OBB patch file received
        FILE-312	"The OBB main file transfer failed for some unknown reason. Please try again."	OBB main file upload failed (unknown reason)
        FILE-313	"The OBB patch file transfer failed for some unknown reason. Please try again."	OBB patch file upload failed (unknown reason)
        FILE-314	"A problem occurred while processing the main OBB file. Please try again."	Error processing the main OBB file upload (unknown reason)
        FILE-315	"A problem occurred while processing the patch OBB file. Please try again."	Error processing the patch OBB file upload (unknown reason)
        FILE-316	"A patch OBB file was detected, but no main OBB file. Please upload a main OBB file with the patch OBB file."	A patch OBB file was uploaded, but not its mandatory main OBB file
        OBB-1	"OBB main MD5 NOT existent!"	OBB main file with MD5 hash obb_main_md5sum not found
        OBB-2	"OBB patch MD5 NOT existent!"	OBB patch file with MD5 hash obb_patch_md5sum not found
        OBB-3	"Invalid OBB type"	Invalid OBB type
        OBB-4	"The given OBB main filename is not valid."	Invalid OBB main filename provided
        OBB-5	"The given OBB patch filename is not valid."	Invalid OBB patch filename provided
        OBB-101	"OBB main file type mismatch"	An OBB file of a specific type was received when another type was expected
        OBB-102	"OBB main file version mismatch"	An OBB file of a lower version was received relative the version of the apk
        OBB-103	"OBB main file package mismatch"	An OBB file of a specific package was received when one equal to the apk package was expected
        */

    public static HashMap<String, EnumUploadFailReason> values = new HashMap<String, EnumUploadFailReason>();

    static {
        values.put("AUTH-4", EnumUploadFailReason.HMAC_AUTHENTICATION_FAILURE);
        values.put("MARG-5", EnumUploadFailReason.MISSING_APK);
        values.put("MARG-3", EnumUploadFailReason.BAD_APK_UPLOAD);
        values.put("MARG-5", EnumUploadFailReason.MISSING_APK);
        values.put("MARG-100", EnumUploadFailReason.MISSING_APK_NAME);
        values.put("MARG-101", EnumUploadFailReason.MISSING_DESCRIPTION);
        values.put("MARG-102", EnumUploadFailReason.MISSING_CATEGORY);
        values.put("MARG-103", EnumUploadFailReason.MISSING_RATING);
        values.put("IARG-1", EnumUploadFailReason.BAD_REPO);
        values.put("IARG-2", EnumUploadFailReason.BAD_TOKEN);
        values.put("IARG-103", EnumUploadFailReason.BAD_CATEGORY);
        values.put("IARG-104", EnumUploadFailReason.BAD_RATING);
        values.put("IARG-105", EnumUploadFailReason.BAD_WEBSITE);
        values.put("IARG-106", EnumUploadFailReason.BAD_EMAIL);
        values.put("IARG-202", EnumUploadFailReason.UPLOAD_FROM_INVALID);
        values.put("IARG-300", EnumUploadFailReason.BAD_USER_CATEGORY);
        values.put("APK-101", EnumUploadFailReason.APK_BLACKLISTED);
        values.put("APK-102", EnumUploadFailReason.APK_INFECTED_WITH_VIRUS);
        values.put("APK-103", EnumUploadFailReason.APK_DUPLICATE);
        values.put("FILE-1", EnumUploadFailReason.APK_EMPTY);
        values.put("FILE-2", EnumUploadFailReason.APK_MAXIMUM_SIZE);
        values.put("FILE-3", EnumUploadFailReason.BAD_APK_UPLOAD);
        values.put("FILE-4", EnumUploadFailReason.BAD_APK_UPLOAD);
        values.put("FILE-5", EnumUploadFailReason.BAD_APK_UPLOAD);
        values.put("FILE-100", EnumUploadFailReason.BAD_APK);
        values.put("FILE-101", EnumUploadFailReason.SERVER_ERROR);
        values.put("FILE-102", EnumUploadFailReason.SERVER_ERROR_SCREENSHOTS_UPLOAD);
        values.put("FILE-200", EnumUploadFailReason.BAD_APK);
        values.put("FILE-201", EnumUploadFailReason.BAD_APK);
        values.put("FILE-202", EnumUploadFailReason.BAD_APK);
        values.put("FILE-203", EnumUploadFailReason.BAD_APK);
        values.put("FILE-204", EnumUploadFailReason.BAD_APK);
        values.put("FILE-205", EnumUploadFailReason.BAD_APK);
        values.put("FILE-206", EnumUploadFailReason.BAD_APK);
        values.put("FILE-300", EnumUploadFailReason.EMPTY_OBB_MAIN_FILE);
        values.put("FILE-301", EnumUploadFailReason.EMPTY_OBB_PATCH_FILE);
        values.put("FILE-302", EnumUploadFailReason.OBB_MAIN_FILE_EXCEEDS_MAXIMUM_SIZE);
        values.put("FILE-303", EnumUploadFailReason.OBB_PATCH_FILE_EXCEEDS_MAXIMUM_SIZE);
        values.put("FILE-304", EnumUploadFailReason.EMPTY_OBB_MAIN_FILE);
        values.put("FILE-305", EnumUploadFailReason.EMPTY_OBB_PATCH_FILE);
        values.put("FILE-306", EnumUploadFailReason.INVALID_OBB_MAIN_NAME);
        values.put("FILE-307", EnumUploadFailReason.INVALID_OBB_PATCH_NAME);
        values.put("FILE-308", EnumUploadFailReason.OBB_MAIN_FILE_TRANSFER_STOPPED);
        values.put("FILE-309", EnumUploadFailReason.OBB_PATCH_FILE_TRANSFER_STOPPED);
        values.put("FILE-310", EnumUploadFailReason.EMPTY_OBB_MAIN_FILE);
        values.put("FILE-311", EnumUploadFailReason.EMPTY_OBB_PATCH_FILE);
        values.put("FILE-312", EnumUploadFailReason.OBB_MAIN_FILE_TRANSFER_FAILED);
        values.put("FILE-313", EnumUploadFailReason.OBB_PATCH_FILE_TRANSFER_FAILED);
        values.put("FILE-314", EnumUploadFailReason.OBB_MAIN_FILE_PROCESSING_PROBLEM);
        values.put("FILE-315", EnumUploadFailReason.OBB_PATCH_FILE_PROCESSING_PROBLEM);
        values.put("FILE-316", EnumUploadFailReason.PATCH_OBB_FILE_WITHOUT_MAIN_OBB);
//    values.put( "OBB-1",OBBmainMD5NOTexistent!
//    values.put( "OBB-2",OBBpatchMD5NOTexistent!
        values.put("OBB-3", EnumUploadFailReason.EMPTY_OBB_MAIN_FILE);
        values.put("OBB-4", EnumUploadFailReason.INVALID_OBB_MAIN_NAME);
        values.put("OBB-5", EnumUploadFailReason.INVALID_OBB_PATCH_NAME);
        values.put("OBB-101", EnumUploadFailReason.OBB_MAIN_FILE_TYPE_MISMATCH);
        values.put("OBB-102", EnumUploadFailReason.OBB_MAIN_FILE_VERSION_MISMATCH);
        values.put("OBB-103", EnumUploadFailReason.OBB_MAIN_FILE_PACKAGE_MISMATCH);

        // Operation "Clean Fakes"
        values.put("APK-107", EnumUploadFailReason.ANTI_SPAM_RULE);
        values.put("APK-108", EnumUploadFailReason.INVALID_APK_ORIGIN);
        values.put("APK-109", EnumUploadFailReason.INVALID_APK_DATA);
        values.put("REPO-8", EnumUploadFailReason.INVALID_ORIGIN_FOR_SIGNUP);
    }
}
