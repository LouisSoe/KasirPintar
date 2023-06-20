package com.louissoe.kasirpintar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;


public class ScannerActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scanner;
//    TextView resBar;
    String resBar;
    SeekBar seekBar;
    int CAMERA_REQUEST_CODE = 101;

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scanner = findViewById(R.id.scanner_view);
//        resBar = findViewById(R.id.resultBarcode);
        seekBar = findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewGroup.LayoutParams layoutParams = scanner.getLayoutParams();
                layoutParams.height = (int) (progress * getResources().getDisplayMetrics().density);
                int height = (int) (progress * getResources().getDisplayMetrics().density);
                Log.e("height", String.valueOf(height));
                scanner.setLayoutParams(layoutParams);
                scanner.requestLayout();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                codeScanner.startPreview();
            }
        });
        codeScanning();
    }

    private void codeScanning() {
        codeScanner = new CodeScanner(this, scanner);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resBar = result.getText();
                        if(resBar.length() >= 12){
                            startActivity(new Intent(ScannerActivity.this, DetailProductActivity.class).putExtra("barcode", resBar));
                        }

                    }
                });
            }
        });
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }


    void setupPermission(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }
    }

    void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults == null || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Kamu harus menyalakan ijin penggunaan kamera pada aplikasi ini!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}