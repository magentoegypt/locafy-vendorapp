package magentoegypt.locafy.gallary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import magentoegypt.locafy.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class GalleryActivity extends AppCompatActivity {

    private ImageGalleryAdapter mGalleryAdapter;
    public ImageInternalFetcher mImageFetcher;
    public static List<Image> mSelectedImages;
    public static int limitImage = 0;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectedImages = new ArrayList<>();
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatTextView doneTopBar = findViewById(R.id.done);
        doneTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                // TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("isFrom", "additionalGallary_lin");
                resultIntent.putExtra("isNeedRefresh", "Yes");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        toolbar.setTitle(R.string.gallery);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        mGalleryAdapter = new ImageGalleryAdapter(getApplicationContext());
        GridView galleryGridView =findViewById(R.id.pp__gallery_grid);
        mImageFetcher = new ImageInternalFetcher(this, 500);
        Cursor imageCursor = null;
        try {

            final String[] columns = {MediaStore.Images.ImageColumns.DATA};//{MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
                    //getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            Toast.makeText(GalleryActivity.this, "cursor.getCount() "+ imageCursor.getCount(), Toast.LENGTH_SHORT).show();
            while (imageCursor.moveToNext()) {
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                int orientation = 0;//imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                mGalleryAdapter.add(new Image(uri, orientation));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(GalleryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if(imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }

        galleryGridView.setAdapter(mGalleryAdapter);

        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Image image = mGalleryAdapter.getItem(i);
                if (!GalleryActivity.mSelectedImages.contains(image)) {
                    if(GalleryActivity.limitImage < 10) {
                        GalleryActivity.mSelectedImages.add(image);
                        GalleryActivity.limitImage += 1;
                    }else{
                        Toast.makeText(GalleryActivity.this, getString(R.string.you_can_not_select_more_than_10_images_please_deselect_another_image_before_tring_to_select_again), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    GalleryActivity.mSelectedImages.remove(image);
                    GalleryActivity.limitImage -= 1;
                }
                // refresh the view to
                // mGalleryAdapter.getView(i, view, adapterView);
                mGalleryAdapter.notifyDataSetChanged();
            }
        });
    }



    class ViewHolder {
        AppCompatImageView mThumbnail;
        // This is like storing too much data in memory.
        // find a better way to handle this
        Image mImage;
    }

    public class ImageGalleryAdapter extends ArrayAdapter<Image> {

        public ImageGalleryAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.pp__grid_item_gallery_thumbnail, null);
                holder = new ViewHolder();
                holder.mThumbnail = (AppCompatImageView) convertView.findViewById(R.id.pp__thumbnail_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Image image = getItem(position);
            boolean isSelected = GalleryActivity.mSelectedImages.contains(image);

            ((FrameLayout) convertView).setForeground(isSelected ? getResources().getDrawable(R.drawable.gallery_photo_selected) : null);

            if (holder.mImage == null || !holder.mImage.equals(image)) {
                mImageFetcher.loadImage(image.mUri, holder.mThumbnail, image.mOrientation);
                holder.mImage = image;
            }
            return convertView;
        }
    }
}