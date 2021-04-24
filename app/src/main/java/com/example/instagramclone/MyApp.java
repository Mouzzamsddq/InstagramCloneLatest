package com.example.instagramclone;

import android.app.Application;
import android.content.Context;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyApp extends Application {
    public static final String TAG = "MY_APP";

    public void onCreate() {
        super.onCreate();
        printHashKey(this);
    }

    public static void printHashKey(Context pContext) {
        try {
            for (Signature signature : pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 64).signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i(TAG, "printHashKey() Hash Key: " + new String(Base64.encode(md.digest(), 0)));
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e2) {
            Log.e(TAG, "printHashKey()", e2);
        }
    }
}
