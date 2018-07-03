package com.etti.classroomsbooking;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static com.etti.classroomsbooking.util.Constant.ACCEPTED_QR_CODES;

// Reference: ZXing - open source library (https://opensource.google.com/projects/zxing)
public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQ_CAMERA = 1;
    private ZXingScannerView scannerView;
    public static int scannedQRCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            if (isPermissionGranted()) {
                Toast.makeText(ScanQRActivity.this, "Persmission granted!", Toast.LENGTH_LONG).show();
            } else {
                requestForPermission();
            }
        } else {
            Toast.makeText(ScanQRActivity.this, "Android API SDK must be higher than 23 for this feature!", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private boolean isPermissionGranted() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(ScanQRActivity.this, CAMERA);
    }

    private void requestForPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQ_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            if (isPermissionGranted()) {
                if (scannerView == null) {
                    setContentView(new ZXingScannerView(this));
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        try {
            if (ACCEPTED_QR_CODES.contains(result.getText())) {
                scannedQRCode = Integer.parseInt(result.getText().substring(10));
                scannerView.stopCamera();
                Intent resultIntent = getIntent();
                resultIntent.putExtra("SCANNED_CODE", "" + result);
                setResult(Activity.RESULT_OK, resultIntent);
            } else {
                scannedQRCode = -1;
            }
            finish();
        } catch (NumberFormatException e) {
        }
    }
}
