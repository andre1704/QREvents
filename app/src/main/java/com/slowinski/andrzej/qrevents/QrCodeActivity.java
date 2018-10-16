package com.slowinski.andrzej.qrevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class QrCodeActivity extends AppCompatActivity {

    private final static int CAMERA_REQUEST_PERMISSIONS_CODE = 445;
    public final static String BUNDLE_QR_RESULT_FORMAT = "qr_result_format";
    public final static String BUNDLE_QR_RESULT_TEXT = "qr_result_text";

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkCameraPermissions();
    }

    private void checkCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, CAMERA) == PERMISSION_DENIED) {
            requestPermissions(new String[]{CAMERA}, CAMERA_REQUEST_PERMISSIONS_CODE);
        } else {
            startQrCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_PERMISSIONS_CODE)
            if (grantResults[0] == PERMISSION_DENIED) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            } else startQrCamera();
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startQrCamera() {
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this::onResultsHandled);
        scannerView.startCamera();
    }

    private void onResultsHandled(Result result) {
        String resultFormat = result.getBarcodeFormat().toString();
        String resultText = result.getText();

        Intent intent = new Intent();
        intent.putExtra(BUNDLE_QR_RESULT_FORMAT, resultFormat);
        intent.putExtra(BUNDLE_QR_RESULT_TEXT, resultText);
        // TODO: 13.08.18
        // UWAGA!!!! Możesz tutaj dowolnie dodawać typy proste.
        // A jak chcesz cały obiekt Result upchać do Bundla, to musi on być serializowany lub parcelizowany -
        // trzeba zobaczyć implementację czy tak jest a jak nie to nalezy go rozszerzyc o klasę podrzedną i
        // dodać interfejs Serializable lub Parcelable

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        scannerView.stopCamera();
        super.onPause();
    }
}
