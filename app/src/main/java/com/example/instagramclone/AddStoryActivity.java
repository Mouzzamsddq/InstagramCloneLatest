package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.instagramclone.ImagePickerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AddStoryActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE = 100;
    String imageUri;
    private Uri mImageUri;
    String myUri = "";
    Uri pickedImgUri;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    private StorageTask storageTask;
    Exception e ;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_add_story);
        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setCancelable(false);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setMessage("Adding story...");
        this.progressDialog.setTitle("Story.");
        this.storageReference = FirebaseStorage.getInstance().getReference().child("story");
        alertDialog(this);
    }

    public void alertDialog(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.attatch_button_dialog, (ViewGroup) null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        ((TextView) mView.findViewById(R.id.selectImageTextView)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                new uploadProfileBackground().execute(new Void[0]);
            }
        });
        alertDialog.show();
    }

    public class uploadProfileBackground extends AsyncTask<Void, Void, Void> {
        public uploadProfileBackground() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voids) {
            Dexter.withActivity(AddStoryActivity.this).withPermissions("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        AddStoryActivity.this.showImagePickerOptions();
                    }
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        AddStoryActivity.this.showSettingsDialog();
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void showSettingsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle((CharSequence) getString(R.string.dialog_permission_title));
        builder.setMessage((CharSequence) getString(R.string.dialog_permission_message));
        builder.setPositiveButton((CharSequence) getString(R.string.go_to_settings), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
                AddStoryActivity.this.openSettings();
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });
    }

    /* access modifiers changed from: private */
    public void openSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getApplicationContext().toString(), (String) null));
        startActivityForResult(intent, 101);
    }

    /* access modifiers changed from: private */
    public void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            public void onTakeCameraSelected() {
                AddStoryActivity.this.launchCameraIntent();
            }

            public void onChooseGallerySelected() {
                AddStoryActivity.this.launchGalleryIntent();
            }
        });
    }

    /* access modifiers changed from: private */
    public void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, 0);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, 100);
    }

    /* access modifiers changed from: private */
    public void launchGalleryIntent() {
        Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, 1);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 100);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1) {
            this.progressDialog.show();
            Uri uri = (Uri) data.getParcelableExtra("path");
            this.pickedImgUri = uri;
            publishStory(uri);
        }
    }

    private void publishStory(Uri pickedImgUri2) {
        Bitmap bitmap = compressImage(pickedImgUri2.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        this.storageReference.child(String.valueOf(System.currentTimeMillis())).putBytes(baos.toByteArray()).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                do {
                } while (!uriTask.isSuccessful());
                String downloadUri = uriTask.getResult().toString();
                if (uriTask.isSuccessful()) {
                    String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Story").child(myUid);
                    String storyId = databaseReference.push().getKey();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imageUri", downloadUri);
                    hashMap.put("timestart", ServerValue.TIMESTAMP);
                    hashMap.put("timesEnd", Long.valueOf(System.currentTimeMillis() + 86400000));
                    hashMap.put("storyId", storyId);
                    hashMap.put("userId", myUid);
                    databaseReference.child(storyId).setValue(hashMap);
                    AddStoryActivity.this.progressDialog.dismiss();
                    AddStoryActivity.this.finish();
                }
            }
        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
            public void onFailure(Exception e) {
            }
        });
    }

    public Bitmap compressImage(String imageUri2) {
        String filePath = getRealPathFromURI(imageUri2);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) (actualWidth / actualHeight);
        float maxRatio = 612.0f / 816.0f;
        if (((float) actualHeight) > 816.0f || ((float) actualWidth) > 612.0f) {
            if (imgRatio < maxRatio) {
                actualWidth = (int) (((float) actualWidth) * (816.0f / ((float) actualHeight)));
                actualHeight = (int) 816.0f;
            } else if (imgRatio > maxRatio) {
                actualHeight = (int) (((float) actualHeight) * (612.0f / ((float) actualWidth)));
                actualWidth = (int) 612.0f;
            } else {
                actualHeight = (int) 816.0f;
                actualWidth = (int) 612.0f;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = ((float) actualWidth) / ((float) options.outWidth);
        float ratioY = ((float) actualHeight) / ((float) options.outHeight);
        BitmapFactory.Options options2 = options;
        float middleX = ((float) actualWidth) / 2.0f;
        int i = actualHeight;
        float middleY = ((float) actualHeight) / 2.0f;
        int i2 = actualWidth;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Matrix matrix = scaleMatrix;
        float f = middleX;
        float f2 = middleY;
        canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
        try {
            int orientation = new ExifInterface(filePath).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix2 = new Matrix();
            if (orientation == 6) {
                try {
                    matrix2.postRotate(90.0f);
                    Log.d("EXIF", "Exif: " + orientation);
                } catch (Exception e2) {
                    e = e2;
                    float f3 = ratioX;
                    e.printStackTrace();
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(getFilename()));
                    return scaledBitmap;
                }
            } else if (orientation == 3) {
                matrix2.postRotate(180.0f);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix2.postRotate(270.0f);
                Log.d("EXIF", "Exif: " + orientation);
            }
            float f4 = ratioX;
            try {
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix2, true);
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(getFilename()));
                return scaledBitmap;
            }
        } catch (IOException e4) {
            e = e4;
            float f5 = ratioX;
            e.printStackTrace();
            try {
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(getFilename()));
            } catch (FileNotFoundException e5) {
                e5.printStackTrace();
            }
            return scaledBitmap;
        }
        try {
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(getFilename()));
        } catch (FileNotFoundException e5) {
            e5.printStackTrace();
        }
        return scaledBitmap;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (cursor == null) {
            return contentUri.getPath();
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("_data"));
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(((float) height) / ((float) reqHeight));
            int widthRatio = Math.round(((float) width) / ((float) reqWidth));
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) (reqWidth * reqHeight * 2))) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static final Uri getUriToDrawable(Context context, int drawableId) {
        return Uri.parse("android.resource://" + context.getResources().getResourcePackageName(drawableId) + '/' + context.getResources().getResourceTypeName(drawableId) + '/' + context.getResources().getResourceEntryName(drawableId));
    }
}
