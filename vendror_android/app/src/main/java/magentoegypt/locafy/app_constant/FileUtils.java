package magentoegypt.locafy_constant;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.content.Intent;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URISyntaxException;

public class FileUtils {
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if (uri == null) return null;
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        // content:// (system photo picker, documents, gallery): copy the picked
        // item into app cache via ContentResolver. This never touches
        // MediaStore.Images.Media.DATA, so it works without READ_MEDIA_IMAGES.
        return copyUriToCache(context, uri);
    }

    /** Copy a content:// Uri into app cache and return its file path. No storage permission needed. */
    public static String copyUriToCache(Context context, Uri uri) {
        if (uri == null) return null;
        try {
            String mime = context.getContentResolver().getType(uri);
            String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime);
            File out = new File(context.getCacheDir(),
                    "pick_" + System.currentTimeMillis() + (ext != null ? "." + ext : ".jpg"));
            InputStream in = context.getContentResolver().openInputStream(uri);
            if (in == null) return null;
            OutputStream os = new FileOutputStream(out);
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) os.write(buf, 0, n);
            os.flush();
            os.close();
            in.close();
            return out.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * System image picker with no storage permission: the Android photo picker on
     * API 33+, a system content picker on older versions. Replaces ACTION_PICK on
     * EXTERNAL_CONTENT_URI and the app's custom gallery so READ_MEDIA_IMAGES can go.
     */
    public static Intent imagePickIntent() {
        if (Build.VERSION.SDK_INT >= 33) {
            Intent i = new Intent(MediaStore.ACTION_PICK_IMAGES);
            i.setType("image/*");
            return i;
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        return i;
    }

    /**
     * Multi-image system picker with no storage permission: the Android photo
     * picker (API 33+) capped at {@code max}, or a multi-select content picker on
     * older versions. Used where several images are chosen at once.
     */
    public static Intent imagePickMultipleIntent(int max) {
        if (Build.VERSION.SDK_INT >= 33) {
            Intent i = new Intent(MediaStore.ACTION_PICK_IMAGES);
            i.setType("image/*");
            if (max > 1) {
                i.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, max);
            }
            return i;
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        return i;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
