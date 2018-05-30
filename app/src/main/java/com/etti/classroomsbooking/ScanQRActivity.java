package com.etti.classroomsbooking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.etti.classroomsbooking.fragments.ScannedRoomFragment;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQ_CAMERA = 1;
    private ZXingScannerView scannerView;
    public static int scannedQRCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
                Toast.makeText(ScanQRActivity.this, "Persmission is granted!", Toast.LENGTH_LONG).show();
            } else{
                requestPermission();
            }
        }
    }
    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(ScanQRActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[] {CAMERA}, REQ_CAMERA);
    }

    public void onRequestPermissions(int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case REQ_CAMERA: if (grantResults.length > 0){
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted){
                    Toast.makeText(ScanQRActivity.this, "Permission granted!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(ScanQRActivity.this, "Permission denied!", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (shouldShowRequestPermissionRationale(CAMERA)){
                            displayAlertMessage("You need to allow access for both permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[] {CAMERA}, REQ_CAMERA);
                                        }
                                    });
                        }
                    }
                }
            } break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.startCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(ScanQRActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void handleResult(Result result) {
        try {
            scannedQRCode = Integer.parseInt(result.getText());
            scannerView.stopCamera();
            Intent resultIntent = getIntent();
            resultIntent.putExtra("SCANNED_CODE", "" + result);
            finish();
            setResult(Activity.RESULT_OK, resultIntent);
        } catch(NumberFormatException e){

        }
    }

}
