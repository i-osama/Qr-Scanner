package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    public ClipboardManager clipboardManager;
    private CodeScannerView myScanner;
    private ImageView copyOption;
    private TextView outputTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myScanner = findViewById(R.id.myScanner);
        copyOption = findViewById(R.id.copyOption);
        outputTxt = findViewById(R.id.outputText);

        setOptionView(false);

        String[] permissions = {
                android.Manifest.permission.CAMERA
        };

        if (hasPermissions(this, permissions)){
            runScanner();

//            Log.i("TAG", "test: 1");
        }else {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

//        copy section
        copyOption.setOnClickListener(view -> {
            clipboardManager = (ClipboardManager) getSystemService((Context.CLIPBOARD_SERVICE));
            ClipData clipData = ClipData.newPlainText("Copy", outputTxt.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
            Log.i("TAG", "clip worked-----");
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        });

    }

    private void setOptionView(boolean show) {
        if (show) {
            copyOption.setVisibility(View.VISIBLE);
            outputTxt.setVisibility(View.VISIBLE);
        }else {
            copyOption.setVisibility(View.GONE);
            outputTxt.setVisibility(View.GONE);
        }
    }

    private void runScanner() {
        codeScanner = new CodeScanner(getApplicationContext(), myScanner);
//        codeScanner.startPreview();
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//            Log.i("TAG", "test: 3");
                        setOptionView(true);
                        outputTxt.setText(result.getText());
                    }
                });
            }
        });

//        myScanner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                codeScanner.startPreview();
//            }
//        });
        codeScanner.startPreview();
    }

    public static  boolean hasPermissions(Context context, String... permissions){
        if (context != null && permissions != null){
            for (String permission:permissions){
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                    }
                }
            }

        return true;
    }
}