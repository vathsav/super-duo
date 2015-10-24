package it.jaschke.alexandria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        // Do something with the result here
        Log.v("Scanner", result.getContents()); // Prints scan results
        Log.v("Scanner", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        // Send the ISBN to the main activity
        Intent callMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        callMainActivity.putExtra("ISBN", result.getContents());
        startActivity(callMainActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent callMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(callMainActivity);
    }
}