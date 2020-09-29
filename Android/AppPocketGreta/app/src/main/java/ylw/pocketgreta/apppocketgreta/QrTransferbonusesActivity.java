package ylw.pocketgreta.apppocketgreta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class QrTransferbonusesActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_transferbonuses);

        imageView = findViewById(R.id.imageQr);
        editText = findViewById(R.id.textQrTr);

    }
    public void dataServereload(){
        String str="";
        //получить от сервака какой код
        new ImageDownloaderTask(imageView).execute("https://api.qrserver.com/v1/create-qr-code/?size=1000x1000&data="+str);


    }
}
