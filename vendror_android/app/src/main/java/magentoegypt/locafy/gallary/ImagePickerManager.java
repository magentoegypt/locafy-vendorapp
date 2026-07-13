package magentoegypt.locafy.gallary;

import static magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity.getResizedBitmap;
import static magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity.rotateBitmap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class ImagePickerManager {

    public static final int REQUEST_CAMERA = 100001;
    public static final int REQUEST_GALLERY = 100002;
    private static ImagePickerCallback callback;
    private static Uri cameraImageUri;

    public interface ImagePickerCallback {
        void onImagePicked(@Nullable Bitmap bitmap);
    }

    public static void presentImagePicker(final Activity activity,int caseNumber, ImagePickerCallback imageCallback) {
        callback = imageCallback;

        if(caseNumber == 1){
            // Camera
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            cameraImageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            activity.startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }else if(caseNumber == 2){
            // Gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            activity.startActivityForResult(galleryIntent, REQUEST_GALLERY);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Select Image Source")
                    .setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                // Camera
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                                cameraImageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                                activity.startActivityForResult(cameraIntent, REQUEST_CAMERA);
                            } else {
                                // Gallery
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryIntent.setType("image/*");
                                activity.startActivityForResult(galleryIntent, REQUEST_GALLERY);
                            }
                        }
                    });
            builder.show();
        }
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (resultCode != Activity.RESULT_OK) {
            if (callback != null) callback.onImagePicked(null);
            return;
        }

        try {
            Bitmap bitmap = null;

            if (requestCode == REQUEST_CAMERA && cameraImageUri != null) {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), cameraImageUri);
                ExifInterface exif = null;
                try {
                    File pictureFile = new File(getPath(cameraImageUri,activity));
                    exif = new ExifInterface(pictureFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    //  Toast.makeText(this,"imagePath " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                int orientation = ExifInterface.ORIENTATION_NORMAL;

                if (exif != null)
                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bitmap = rotateBitmap(bitmap, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bitmap = rotateBitmap(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bitmap = rotateBitmap(bitmap, 270);
                        break;
                }

                bitmap = getResizedBitmap(bitmap, 300, 300);
            } else if (requestCode == REQUEST_GALLERY && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
                bitmap = getResizedBitmap(bitmap, 300, 300);
            }

            if (callback != null) {
                callback.onImagePicked(bitmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Image selection failed", Toast.LENGTH_SHORT).show();
            if (callback != null) callback.onImagePicked(null);
        }
    }
    public static String getPath(Uri contentUri, final Activity activity) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(activity, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
