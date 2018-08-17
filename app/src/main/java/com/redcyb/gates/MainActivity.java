package com.redcyb.gates;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;

    private ImageView imageCall;
    private Handler mHandler;
    private int counter = 1;

    private void useHandler() {
        mHandler = new Handler();
        mHandler.post(mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.w("***** Handlers", "Call " + counter);
            makePhoneCall();
            if (counter < 5) {
                imageCall.setImageResource(R.drawable.ic_phone_in_talk_black_24dp);
                mHandler.postDelayed(mRunnable, 27000);
                counter++;
            } else {
                mHandler.removeCallbacks(mRunnable);
                imageCall.setImageResource(R.drawable.ic_phone_black_24dp);
                counter = 1;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageCall = findViewById(R.id.imageView2);
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useHandler();
            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
            mHandler.removeCallbacks(mRunnable);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    private void makePhoneCall() {
        String phoneNumber = "tel:+380957453141";
        if (ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
