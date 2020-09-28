package ylw.pocketgreta.apppocketgreta.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ylw.pocketgreta.apppocketgreta.MainActivity;
import ylw.pocketgreta.apppocketgreta.R;
import ylw.pocketgreta.apppocketgreta.Singleton;
import ylw.pocketgreta.apppocketgreta.ui.RegistrationActivity;
import ylw.pocketgreta.apppocketgreta.ui.login.LoginActivity;

import static java.lang.Thread.sleep;

public class ChooseActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        cameraPreview = (SurfaceView) findViewById(R.id.camera_QR);
        txtResult = (TextView) findViewById(R.id.text_resultQR);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();
        //Add Event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(ChooseActivity.this,
                            new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0)
                {
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            //Create vibrate
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(10);
                            txtResult.setText(qrcodes.valueAt(0).displayValue);
                            SendDataToServerQR(qrcodes.valueAt(0).displayValue);
                            Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
    public void SendDataToServerQR(String text){
        Singleton singleton = Singleton.getInstance();
        String username = singleton.getUsername();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", text);//
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tokenAuth = singleton.getTokenAuth();
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post ("http://457f5a38fd2e.ngrok.io/rest/bonuses/code")//
                .addHeaders("Authorization", "Bearer "+tokenAuth)
                .addJSONObjectBody(jsonObject)
                .setPriority (Priority.MEDIUM)
                .build ()
                .getAsJSONObject (new JSONObjectRequestListener() {
                    @Override
                    public void onResponse (JSONObject response) {
                        Toast.makeText(getApplicationContext(), "You have received bonuses!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d("Error",error.toString());
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
