package ylw.pocketgreta.apppocketgreta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class ActivityWrittenOff extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    Button button;
    int bonuses;
    boolean flag;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written_off);
        code = UUID.randomUUID().toString();

        editText = findViewById(R.id.textQrTr);
        button = findViewById(R.id.SendBonuses);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                bonuses = Integer.parseInt(text);
                dataServerload(code, bonuses);
                imageView = findViewById(R.id.imageQr);
                new ImageDownloaderTask(imageView).execute("https://api.qrserver.com/v1/create-qr-code/?size=1000x1000&data="+code);
            }
        });




    }
    public void dataServerload(String code1, int bonuses1){
        String str="";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("code", code1);
            jsonObject.put("bonuses", bonuses1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Singleton singleton = Singleton.getInstance();
        String tokenAuth = singleton.getTokenAuth();
        AndroidNetworking.post ("http://457f5a38fd2e.ngrok.io/rest/bonuses/addcode")
                .addHeaders("Authorization", "Bearer "+tokenAuth)
                .setPriority (Priority.MEDIUM)
                .addJSONObjectBody(jsonObject)
                .build ()
                .getAsJSONObject (new JSONObjectRequestListener() {
                    @Override
                    public void onResponse (JSONObject response) {
                    }
                    @Override
                    public void onError (ANError error) {
                    }
                });
        //получить от сервака какой код

    }
}
