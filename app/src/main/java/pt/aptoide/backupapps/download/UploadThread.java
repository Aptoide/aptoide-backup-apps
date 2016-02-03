package pt.aptoide.backupapps.download;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import pt.aptoide.backupapps.BackupAppsApplication;
import pt.aptoide.backupapps.FullErrorList;
import pt.aptoide.backupapps.analytics.Analytics;
import pt.aptoide.backupapps.download.state.EnumUploadFailReason;
import pt.aptoide.backupapps.download.state.ErrorState;
import pt.aptoide.backupapps.download.state.UploadErrorState;
import pt.aptoide.backupapps.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 02-07-2013
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class UploadThread implements Runnable, ManagerThread {

    private static final String uploadUrl = Constants.URI_UPLOAD_WS;
    private static final int CHUNK_SIZE = 1024*16;
    private static final String LINE_END = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private final UploadModel uploadModel;
    private final DownloadInfo parent;
    private int lenTotal;
    private boolean apkmd5 = true;
    private boolean mainobbmd5 = true;
    private boolean patchobbmd5 = true;
    private int progressSize = 0;
    private long fullSize = 0;
    private String boundary = generateBoundary();

    public UploadThread(UploadModel uploadModel, DownloadInfo parent) throws IOException {
        this.uploadModel = uploadModel;
        this.parent = parent;
        fullSize += uploadModel.getUploadFiles().getApk().length();

        if(uploadModel.getUploadFiles().getMainObb()!=null){
            fullSize += uploadModel.getUploadFiles().getMainObb().length();
        }

        if(uploadModel.getUploadFiles().getPatchObb()!=null){
            fullSize += uploadModel.getUploadFiles().getPatchObb().length();
        }

        Log.d("Tag", "Creating upload thread");
    }

    public static String generateBoundary() {
        try {
            // Create a secure random number generator
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

            // Get 1024 random bits
            byte[] bytes = new byte[1024/8];
            sr.nextBytes(bytes);

            int seedByteCount = 10;
            byte[] seed = sr.generateSeed(seedByteCount);

            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);

            return "***"+Long.toString(sr.nextLong())+"***";

        } catch (NoSuchAlgorithmException e) {
        }
        return "*********";
    }

    public static InputStream getFileInputStream(File file) throws IOException, InterruptedException {
        try
        {
            return new FileInputStream(file);
        }
        catch(Exception e)
        {
            Process p = Runtime.getRuntime().exec("su");

            DataOutputStream outputS = new DataOutputStream(p.getOutputStream());

            outputS.writeBytes("cat \"" + file.getPath() + "\"\n");

            outputS.writeBytes("exit\n");
            outputS.flush();
            outputS.close();

            return p.getInputStream();
        }
    }

    @Override
    protected void finalize() throws Throwable {

        Log.d("Garbage Collector", "UploadThread beeing destroyed.");

        super.finalize();
    }

    @Override
    public long getmDownloadedSize() {
        return progressSize;
    }

    @Override
    public long getmProgress() {
        return progressSize;
    }

    @Override
    public long getmFullSize() {
        return fullSize;
    }

    @Override
    public long getmRemainingSize() {
        return getmFullSize();
    }

    private String formPart(String fieldName, String fieldValue){
        return   TWO_HYPHENS + boundary + LINE_END
                + "Content-Disposition: form-data; name=\"" + fieldName + "\""
                + LINE_END + LINE_END + fieldValue + LINE_END;
    }

    public String getJSON(String url, int timeout) {
        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException ex) {

        }
        return null;
    }

    @Override
    public void run() {

        ArrayList<EnumUploadFailReason> uploadFailReasons = new ArrayList<EnumUploadFailReason>();
        DataOutputStream outputStream = null;
        try {

            boolean skipUpload = false;
            ApkMetaData metaData = uploadModel.getMetadata();


//            if (metaData.getCategory() == -1) {
//                try {
//                    String packageName = metaData.getPackageName();
//
//                    String json = getJSON("http://webservices.aptoide.com/webservices/2/getApkInfo/package:" + packageName + "/json", 10000);
//                    JSONObject object = new JSONObject(json);
//
//                    if (!object.isNull("errors")) {
//
//                        JSONArray array = object.getJSONArray("errors");
//
//                        for (int i = 0; i != array.length(); i++) {
//
//                            String code = array.getJSONObject(i).getString("code");
//
//                            if (code.equals("APK-4")) {
//                                skipUpload = true;
//                                uploadFailReasons.add(EnumUploadFailReason.APK_NOT_FOUND);
//                                parent.changeStatusState(new UploadErrorState(parent, uploadFailReasons));
//
//                            }
//
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }


//            if (!skipUpload) {
                HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();

                Log.d("TAG", "Starting thread");

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setConnectTimeout(120000);
                connection.setReadTimeout(120000);
                connection.setChunkedStreamingMode(CHUNK_SIZE);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                String body = formPart("uploadType", "aptbackup");
                Log.d("TAG", body);

                if (metaData.getCategory() != -1) {
                    body += formPart("category", String.valueOf(metaData.getCategory()));
                }
                if (metaData.getDescription() != null) {
                    body += formPart("description", metaData.getDescription());
                }
                if (metaData.getApk_phone() != null) {
                    body += formPart("apk_phone", metaData.getApk_phone());
                }
                if (metaData.getApk_email() != null) {
                    body += formPart("apk_email", metaData.getApk_email());
                }
                if (metaData.getApk_website() != null) {
                    body += formPart("apk_website", metaData.getApk_website());
                }

                if (metaData.getRating() != null) {
                    body += formPart("rating", metaData.getRating());
                }


                body += formPart("token", uploadModel.getUserToken())
                        + formPart("repo", uploadModel.getRepo())
                        + formPart("apkname", metaData.getName());


                Log.d("TAG", body);


                if (apkmd5) {
                    body += formPart("apk_md5sum", uploadModel.getUploadFiles().getApkMd5());
                }

                if (mainobbmd5 && uploadModel.getUploadFiles().getMainObb() != null) {
                    body += formPart("obb_main_filename", uploadModel.getUploadFiles().getMainObb().getName());
                    body += formPart("obb_main_md5sum", uploadModel.getUploadFiles().getMainObbMd5());
                    fullSize += uploadModel.getUploadFiles().getMainObb().length();
                }

                if (patchobbmd5 && uploadModel.getUploadFiles().getPatchObb() != null) {
                    body += formPart("obb_patch_filename", uploadModel.getUploadFiles().getPatchObb().getName());
                    body += formPart("obb_patch_md5sum", uploadModel.getUploadFiles().getPatchObbMd5());
                    fullSize += uploadModel.getUploadFiles().getPatchObb().length();
                }

                body += formPart("mode", "json");

                Log.d("TAG Upload", body);
                outputStream = new DataOutputStream(connection.getOutputStream());


                outputStream.writeBytes(body);

                if (!apkmd5 && uploadModel.getUploadFiles().getApk() != null) {
                    addFilePart("apk", uploadModel.getUploadFiles().getApk(), outputStream, new PrintWriter(outputStream));
                }

                if (!mainobbmd5 && uploadModel.getUploadFiles().getMainObb() != null) {
                    addFilePart("obb_main", uploadModel.getUploadFiles().getMainObb(), outputStream, new PrintWriter(outputStream));
                }

                if (!patchobbmd5 && uploadModel.getUploadFiles().getPatchObb() != null) {
                    addFilePart("obb_patch", uploadModel.getUploadFiles().getPatchObb(), outputStream, new PrintWriter(outputStream));
                }

                outputStream.writeBytes(TWO_HYPHENS + boundary + TWO_HYPHENS + LINE_END);

                outputStream.flush();
                outputStream.close();

                Log.d("TAG Upload", "Getting response");

                InputStream is = connection.getInputStream();

                Log.d("TAG Upload", "Creating buffered reader");

                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                Log.d("TAG Upload", "Reading line");
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.d("TAG Upload", "Creating json");
                JSONObject object = new JSONObject(response.toString());
                Log.d("TAG Upload", response.toString());

                if (object.getString("status").contains("FAIL")) {

                    JSONArray errors = object.getJSONArray("errors");

                    boolean restart = false;

                    for (int i = 0; i != errors.length(); i++) {
                        Log.d("TAG", errors.getJSONObject(i).getString("msg"));

                        String code = errors.getJSONObject(i).getString("code");

                        //MD5 NOT existent
                        if (code.equals("APK-5")) {
                            apkmd5 = false;
                            restart = true;
                        }

                        //OBB main MD5 NOT existent!
                        if (code.equals("OBB-1")) {
                            mainobbmd5 = false;
                            restart = true;
                        }

                        //OBB patch MD5 NOT existent!
                        if (code.equals("OBB-2")) {
                            patchobbmd5 = false;
                            restart = true;
                        }

                        uploadFailReasons.add(FullErrorList.values.get(code));

                        if (uploadFailReasons.size() > 0 && uploadFailReasons.get(0) != null) {
                            Analytics.Upload.uploadApp(uploadFailReasons.get(0).toString(BackupAppsApplication.getContext()));
                        }

//                    if (code.contains("Missing token parameter")) {
//                        uploadFailReasons.add(EnumUploadFailReason.MISSING_TOKEN);
//                    } else if (code.contains("You need to upload an APK")
//                            || code.contains("Missing apk parameter")) {
//                        uploadFailReasons.add(EnumUploadFailReason.MISSING_APK);
//                    } else if (code.contains("Missing apkname parameter")) {
//                        uploadFailReasons.add(EnumUploadFailReason.MISSING_APK_NAME);
//                    } else if (code.contains("Missing description parameter")) {
//                        uploadFailReasons.add(EnumUploadFailReason.MISSING_DESCRIPTION);
//                    } else if (code.contains("Missing rating parameter")) {
//                        uploadFailReasons.add(EnumUploadFailReason.MISSING_RATING);
//                    } else if (code.contains("Missing category parameter")) {
//                        uploadFailReasons.add(EnumUploadFailReason.MISSING_CATEGORY);
//                    } else if (code.contains("Invalid token!")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_TOKEN);
//                    } else if (code.contains("Invalid repo!")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_REPO);
//                    } else if (code.contains("An invalid APK was received, does not seem to contain all required data. Please verify that you selected the right file, and try again.")
//                            || code.contains("An invalid APK was received, does not seem to contain data about the name, version code or version name. Please verify that you selected the right file, and try again.")
//                            || code.contains("An invalid APK was received, does not seem to be a ZIP file or seems to have some errors. Please verify that you selected the right file, and try again.")
//                            || code.contains("An invalid APK was received, does not seem to contain data about the label or icon. Please verify that you selected the right file, and try again.")
//                            || code.contains("An invalid APK was received. Please verify that you selected the right file, and try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_APK);
//                    } else if (code.contains("Invalid rating!")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_RATING);
//                    } else if (code.contains("Invalid category!")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_CATEGORY);
//                    } else if (code.contains("The website is not a valid URL.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_WEBSITE);
//                    } else if (code.contains("The e-mail address is not valid.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_EMAIL);
//                    } else if (code.contains("Token doesn't match with Repo.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.TOKEN_INCONSISTENT_WITH_REPO);
//                    } else if (code.contains("Unable to upload the apk. Please try again.")
//                            || code.contains("The file transfer stopped before the upload was complete. Please try again.")
//                            || code.contains("The file transfer failed for some unknown reason. Please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.BAD_APK_UPLOAD);
//                    } else if (code.contains("The file you uploaded exceeds the maximum allowed size.")
//                            || code.contains("The file you uploaded exceeds the maximum size")) {
//                        uploadFailReasons.add(EnumUploadFailReason.APK_TOO_BIG);
//                    } else if (code.contains("Application duplicate: the uploaded apk already exists in this repository")) {
//                        uploadFailReasons.add(EnumUploadFailReason.APK_DUPLICATE);
//                    } else if (code.contains("It's not possible to upload the required APK since an infection was detected. If you are the developer/owner of the application, please contact Aptoide Staff.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.APK_INFECTED_WITH_VIRUS);
//                    } else if (code.startsWith("Due to Intelectual Property reasons, it's not possible to upload the required APK.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.APK_BLACKLISTED);
//                    } else if (code.contains("Unable to upload the apk icon.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.SERVER_ERROR_ICON_UPLOAD);
//                    } else if (code.contains("Unable to upload the Feature Graphic.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.SERVER_ERROR_GRAPHIC_UPLOAD);
//                    } else if (code.contains("MD5 processing failed, please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.SERVER_ERROR_MD5);
//                    } else if (code.contains("The file you sent is missing. Maybe the form session has expired. Please upload the file again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.SERVER_ERROR_MISSING_FILE);
//                    } else if (code.startsWith("Invalid screenshot (")//'<screenshot reference here>') uploaded! Please review your files.")
//                            || code.contains("Unable to download Google Market screenshots.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.SERVER_ERROR_SCREENSHOTS);
//                    } else if (code.startsWith("One of your screenshots (")//'<screenshot reference here>') failed to be uploaded. Please Try Again.")
//                            || code.contains("Unable to upload the screenshots.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.SERVER_ERROR_SCREENSHOTS_UPLOAD);
//                    } else if (code.contains("HMAC Authentication failure")) {
//                        uploadFailReasons.add(EnumUploadFailReason.HMAC_AUTHENTICATION_FAILURE);
//                    } else if (code.contains("The file you uploaded is empty.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.APK_EMPTY);
//                    } else if (code.startsWith("Invalid uploadType ")) {
//                        uploadFailReasons.add(EnumUploadFailReason.INVALID_UPLOAD_TYPE);
//                    } else if (code.startsWith("upload_from ")) {
//                        uploadFailReasons.add(EnumUploadFailReason.UPLOAD_FROM_INVALID);
//                    } else if (code.contains("You need to upload a main OBB")) {
//                        uploadFailReasons.add(EnumUploadFailReason.EMPTY_OBB_MAIN_FILE);
//                    } else if (code.contains("You need to upload a patch OBB")) {
//                        uploadFailReasons.add(EnumUploadFailReason.EMPTY_OBB_PATCH_FILE);
//                    } else if (code.contains("The uploaded main file is not a valid OBB.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.INVALID_OBB_MAIN_FILE);
//                    } else if (code.contains("The uploaded patch file is not a valid OBB.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.INVALID_OBB_PATCH_FILE);
//                    } else if (code.contains("The OBB main file you uploaded exceeds the maximum allowed size.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_EXCEEDS_MAXIMUM_SIZE);
//                    } else if (code.contains("The OBB patch file you uploaded exceeds the maximum allowed size.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_EXCEEDS_MAXIMUM_SIZE);
//                    } else if (code.contains("The OBB main file transfer stopped before the upload was complete. Please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_TRANSFER_STOPPED);
//                    } else if (code.contains("The OBB patch file transfer stopped before the upload was complete. Please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_TRANSFER_STOPPED);
//                    } else if (code.contains("The OBB main file transfer failed for some unknown reason. Please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_TRANSFER_FAILED);
//                    } else if (code.contains("The OBB patch file transfer failed for some unknown reason. Please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_TRANSFER_FAILED);
//                    } else if (code.contains("A problem occurred while processing the main OBB file. Please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_PROCESSING_PROBLEM);
//                    } else if (code.contains("A problem occurred while processing the patch OBB file. Please try again.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_PROCESSING_PROBLEM);
//                    } else if (code.contains("The uploaded main file does not have a valid OBB name.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.INVALID_OBB_MAIN_NAME);
//                    } else if (code.contains("The uploaded patch file does not have a valid OBB name.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.INVALID_OBB_PATCH_NAME);
//                    } else if (code.startsWith("OBB main file MD5 mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_MD5_MISMATCH);
//                    } else if (code.startsWith("OBB patch file MD5 mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_MD5_MISMATCH);
//                    } else if (code.startsWith("OBB main file type mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_TYPE_MISMATCH);
//                    } else if (code.startsWith("OBB patch file type mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_TYPE_MISMATCH);
//                    } else if (code.startsWith("OBB main file version mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_VERSION_MISMATCH);
//                    } else if (code.startsWith("\"OBB patch file version mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_VERSION_MISMATCH);
//                    } else if (code.startsWith("OBB main file package mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_MAIN_FILE_PACKAGE_MISMATCH);
//                    } else if (code.startsWith("OBB patch file package mismatch: expected")) {
//                        uploadFailReasons.add(EnumUploadFailReason.OBB_PATCH_FILE_PACKAGE_MISMATCH);
//                    } else if (code.contains("A patch OBB file was detected, but no main OBB file. Please upload a main OBB file with the patch OBB file.")) {
//                        uploadFailReasons.add(EnumUploadFailReason.PATCH_OBB_FILE_WITHOUT_MAIN_OBB);
//                    }
                    }

                    if (restart) {
                        run();
                        return;
                    }

                    if (uploadFailReasons.isEmpty()) {
                        uploadFailReasons.add(EnumUploadFailReason.SERVER_ERROR);
                    }

                    parent.changeStatusState(new UploadErrorState(parent, uploadFailReasons));
                } else{
                    Analytics.Upload.uploadApp("Success");
                }
//            }
        } catch (UnknownHostException e) {
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.CONNECTION_ERROR));
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.TIMEOUT));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.CONNECTION_ERROR));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            parent.changeStatusState(new ErrorState(parent, EnumDownloadFailReason.CONNECTION_ERROR));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void addFilePart(String fieldName, File uploadFile, OutputStream outputStream, PrintWriter writer)
            throws IOException {



        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_END);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_END);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_END);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_END);
        writer.append(LINE_END);
        writer.flush();


        FileInputStream inputStream = null;
        try {
            inputStream = (FileInputStream) getFileInputStream(uploadFile);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        byte[] buffer = new byte[1024*16];

        int bytesRead = -1;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            lenTotal += bytesRead;
            outputStream.write(buffer, 0, bytesRead);
            progressSize += bytesRead;

//            progressDialog.update((int) (lenTotal*100/uploadFile.length()), uploadFile.getName());

//			Log.d("OutputFile", "size: "+ uploadFile.length() +" bytes, Total: "+lenTotal + " progress:" + (long)(lenTotal*100/uploadFile.length()));
        }
        Log.d("OutputFile", "sent: "+lenTotal);

        inputStream.close();

        writer.append(LINE_END);
        writer.flush();
    }
}
