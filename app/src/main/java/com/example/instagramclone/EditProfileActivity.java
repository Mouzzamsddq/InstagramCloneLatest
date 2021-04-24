package com.example.instagramclone;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private CircularImageView profileImageView;
    private ImageView closeImageView;
    private TextView saveTextView,emailTextView,birthdateTextView,genderTextView,changeProfileTextView;
    String profileId;
    private MaterialEditText bioEditText,usernameEditText,fullNameEditText;
    public static final int REQUEST_IMAGE = 100;
    boolean check = false;
    Uri pickedImgUri;

    private RelativeLayout relativeLayout;

    String updatedUsername,updatedFullName;

    private ProgressBar editProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profileImageView);
        closeImageView = findViewById(R.id.closeImageView);
        saveTextView = findViewById(R.id.saveTextView);
        emailTextView = findViewById(R.id.emailTextView);
        birthdateTextView = findViewById(R.id.birthdateTextView);
        genderTextView = findViewById(R.id.genderTextView);
        profileId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bioEditText = findViewById(R.id.bioEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        changeProfileTextView = findViewById(R.id.changeProfileTextView);
        relativeLayout = findViewById(R.id.relativeLayout);
        editProgressBar = findViewById(R.id.editProgressBar);

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });







        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(usernameEditText.getText().toString()))
                {
                    usernameEditText.setError("username must not be empty");
                    return;
                }
                if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
                {
                    fullNameEditText.getText().toString();
                    return;
                }


                editProgressBar.setVisibility(View.VISIBLE);
                saveTextView.setVisibility(View.GONE);


                updatedUsername = usernameEditText.getText().toString();
                updatedFullName = fullNameEditText.getText().toString();
                String bio = bioEditText.getText().toString();

                HashMap<String,Object>  userData = new HashMap<>();
                userData.put("username",updatedUsername);
                userData.put("fullName",updatedFullName);
                userData.put("bio",bio);


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.updateChildren(userData);


                if(pickedImgUri != null) {

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    //get Bitmap from image uri
                    Bitmap bitmap = null;
//                                     bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), pickedImgUri);
                    bitmap = compressImage(pickedImgUri.toString());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                    byte[] data = baos.toByteArray();  // convert images to byte

                    storageReference.child("Users_profile_image").child(mAuth.getCurrentUser().getUid()).putBytes(data)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    //image uploaded
                                    //get uri of uploaded image
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful()) ;
                                    String downloadUri = uriTask.getResult().toString();
//
                                    if (uriTask.isSuccessful()) {


                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                                        databaseReference1.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                child("imageUri").setValue(downloadUri)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Snackbar snackbar = Snackbar.make(relativeLayout,"profile Updated",Snackbar.LENGTH_SHORT);
                                                            snackbar.setBackgroundTint(ContextCompat.getColor(EditProfileActivity.this,R.color.facebook_blue));
                                                            snackbar.setTextColor(ContextCompat.getColor(EditProfileActivity.this,android.R.color.white));
                                                            snackbar.show();
                                                            Intent intent1 = new Intent(getApplicationContext() , HomeActivity.class);
                                                            intent1.putExtra("publisherUid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            startActivity(intent1);

                                                        } else {


                                                            saveTextView.setVisibility(View.VISIBLE);
                                                            editProgressBar.setVisibility(View.GONE);
                                                            Toast.makeText(EditProfileActivity.this, "error occured ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    saveTextView.setVisibility(View.VISIBLE);
                                    editProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(EditProfileActivity.this, "Error while uploading image", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {

                    Snackbar snackbar = Snackbar.make(relativeLayout,"profile Updated",Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(EditProfileActivity.this,R.color.facebook_blue));
                    snackbar.setTextColor(ContextCompat.getColor(EditProfileActivity.this,android.R.color.white));
                    snackbar.show();
                    Intent intent1 = new Intent(getApplicationContext() , HomeActivity.class);
                    intent1.putExtra("publisherUid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    startActivity(intent1);
                }

            }
        });





        changeProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile();
            }
        });


        getUserInfo();


    }

    private void changeProfile() {

        alertDialog(EditProfileActivity.this);
    }


    private void getUserInfo()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(profileId);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(EditProfileActivity.this).load(user.getImageUri()).into(profileImageView);
                emailTextView.setText(user.getEmail());
                birthdateTextView.setText(user.getDateOfBirth());
                bioEditText.setText(user.getBio());
                usernameEditText.setText(user.getUsername());
                fullNameEditText.setText(user.getFullName());
                genderTextView.setText(user.getGender());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





    public void alertDialog(final Context context) {
        final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.attatch_button_dialog, null);
        final TextView selectImageFiles = mView.findViewById(R.id.selectImageTextView);

        alert.setView(mView);

        final androidx.appcompat.app.AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        selectImageFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                new uploadProfileBackground().execute();
            }
        });

        alertDialog.show();
    }




    public class uploadProfileBackground extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            Dexter.withActivity(EditProfileActivity.this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                                showImagePickerOptions();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

            return null;
        }
    }


    private void showSettingsDialog() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));


        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
                openSettings();

            }
        }).setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().toString(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(EditProfileActivity.this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }


    private void launchCameraIntent() {
        Intent intent = new Intent(EditProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }


    private void launchGalleryIntent() {
        Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri uri = data.getParcelableExtra("path");
                pickedImgUri = uri;
                profileImageView.setImageURI(pickedImgUri);
                check = true;

            }
        }
    }



    public class uploadImageBackground extends AsyncTask<Uri,Uri,Void>
    {

        @Override
        protected Void doInBackground(Uri... uris) {







            return null;
        }
    }


    public Bitmap compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return scaledBitmap;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    //test code
    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }


    @Override
    protected void onResume() {
        super.onResume();

        editProgressBar.setVisibility(View.GONE);
        saveTextView.setVisibility(View.VISIBLE);
        Log.d("kkk","onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        editProgressBar.setVisibility(View.GONE);
        saveTextView.setVisibility(View.VISIBLE);
        Log.d("kkk","onRestart");
    }


}