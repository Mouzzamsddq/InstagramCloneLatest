package com.example.instagramclone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.List;

public class ImagePickerActivity extends AppCompatActivity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String INTENT_ASPECT_RATIO_X = "aspect_ratio_x";
    public static final String INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y";
    public static final String INTENT_BITMAP_MAX_HEIGHT = "max_height";
    public static final String INTENT_BITMAP_MAX_WIDTH = "max_width";
    public static final String INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality";
    public static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    public static final String INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio";
    public static final String INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height";
    public static final int REQUEST_GALLERY_IMAGE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final String TAG = ImagePickerActivity.class.getSimpleName();
    public static String fileName;
    private int ASPECT_RATIO_X = 16;
    private int ASPECT_RATIO_Y = 9;
    private int IMAGE_COMPRESSION = 80;
    private int bitmapMaxHeight = 1000;
    private int bitmapMaxWidth = 1000;
    private boolean lockAspectRatio = false;
    private boolean setBitmapMaxWidthHeight = false;

    public interface PickerOptionListener {
        void onChooseGallerySelected();

        void onTakeCameraSelected();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_image_picker);
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_image_intent_null), 1).show();
            return;
        }
        this.ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, this.ASPECT_RATIO_X);
        this.ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, this.ASPECT_RATIO_Y);
        this.IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, this.IMAGE_COMPRESSION);
        this.lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false);
        this.setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        this.bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, this.bitmapMaxWidth);
        this.bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, this.bitmapMaxHeight);
        if (intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1) == 0) {
            takeCameraImage();
        } else {
            chooseImageFromGallery();
        }
    }

    public static void showImagePickerOptions(Context context, final PickerOptionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle((CharSequence) context.getString(R.string.lbl_set_profile_photo));
        builder.setItems((CharSequence[]) new String[]{context.getString(R.string.lbl_take_camera_picture), context.getString(R.string.lbl_choose_from_gallery)}, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    listener.onTakeCameraSelected();
                }
                if (which == 1) {
                    listener.onChooseGallerySelected();
                }
            }
        });
        builder.create().show();
    }

    private void takeCameraImage() {
        Dexter.withActivity(this).withPermissions("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    ImagePickerActivity.fileName = System.currentTimeMillis() + ".jpg";
                    Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    takePictureIntent.putExtra("output", ImagePickerActivity.this.getCacheImagePath(ImagePickerActivity.fileName));
                    if (takePictureIntent.resolveActivity(ImagePickerActivity.this.getPackageManager()) != null) {
                        ImagePickerActivity.this.startActivityForResult(takePictureIntent, 0);
                    }
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void chooseImageFromGallery() {
        Dexter.withActivity(this).withPermissions("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    ImagePickerActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0) {
            if (requestCode != 1) {
                if (requestCode != 69) {
                    if (requestCode != 96) {
                        setResultCancelled();
                        return;
                    }
                    Log.e(TAG, "Crop error: " + UCrop.getError(data));
                    setResultCancelled();
                } else if (resultCode == -1) {
                    handleUCropResult(data);
                } else {
                    setResultCancelled();
                }
            } else if (resultCode == -1) {
                cropImage(data.getData());
            } else {
                setResultCancelled();
            }
        } else if (resultCode == -1) {
            cropImage(getCacheImagePath(fileName));
        } else {
            setResultCancelled();
        }
    }

    private void cropImage(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(this.IMAGE_COMPRESSION);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.facebook_blue));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.facebook_blue));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.facebook_blue));
        if (this.lockAspectRatio) {
            options.withAspectRatio((float) this.ASPECT_RATIO_X, (float) this.ASPECT_RATIO_Y);
        }
        if (this.setBitmapMaxWidthHeight) {
            options.withMaxResultSize(this.bitmapMaxWidth, this.bitmapMaxHeight);
        }
        UCrop.of(sourceUri, destinationUri).withOptions(options).start(this);
    }

    private void handleUCropResult(Intent data) {
        if (data == null) {
            setResultCancelled();
        } else {
            setResultOk(UCrop.getOutput(data));
        }
    }

    private void setResultOk(Uri imagePath) {
        Intent intent = new Intent();
        intent.putExtra("path", imagePath);
        setResult(-1, intent);
        finish();
    }

    private void setResultCancelled() {
        setResult(0, new Intent());
        finish();
    }

    /* access modifiers changed from: private */
    public Uri getCacheImagePath(String fileName2) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) {
            path.mkdirs();
        }
        return FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(path, fileName2));
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor = resolver.query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (returnCursor != null) {
            int nameIndex = returnCursor.getColumnIndex("_display_name");
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            returnCursor.close();
            return name;
        }
        throw new AssertionError();
    }

    public static void clearCache(Context context) {
        File path = new File(context.getExternalCacheDir(), "camera");
        if (path.exists() && path.isDirectory()) {
            for (File child : path.listFiles()) {
                child.delete();
            }
        }
    }
}
