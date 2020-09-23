package ylw.pocketgreta.apppocketgreta.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import ylw.pocketgreta.apppocketgreta.MainActivity;
import ylw.pocketgreta.apppocketgreta.R;
import ylw.pocketgreta.apppocketgreta.SettingsActivity;
import ylw.pocketgreta.apppocketgreta.ui.login.LoginActivity;

public class RegistrationActivity extends AppCompatActivity {
    EditText firstName;
    EditText email;
    EditText password1;
    EditText password2;
    Button okButton;
    String firstnamestr;
    String emailstr;
    String passwordstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firstName = findViewById(R.id.firstName);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        okButton = findViewById(R.id.Button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEntered()) {
                    passwordstr = password1.getText().toString();
                    firstnamestr = firstName.getText().toString();
                    emailstr = email.getText().toString();
                    sendDataServer();
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    void sendDataServer(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", firstnamestr);
            jsonObject.put("email", emailstr);
            jsonObject.put("password", passwordstr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post ("http://457f5a38fd2e.ngrok.io/rest/auth/signup")
                .addJSONObjectBody(jsonObject)
                .setPriority (Priority.MEDIUM)
                .build ()
                .getAsJSONObject (new JSONObjectRequestListener() {
                    @Override
                    public void onResponse (JSONObject response) {
                        Log.d("Registration","Login Succesfull");
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d("Registration","Error");
                    }
                });
    }
    boolean checkDataEntered(){
        int flag =0;
        if (isEmpty(firstName)) {
            Toast t = Toast.makeText(this, "You must enter first name to register!", Toast.LENGTH_SHORT);
            t.show();
            flag++;
        }
        if (isEmail(email) == false) {
            email.setError("Enter valid email!");
            flag++;
        }
        if(password1.getText().length()<8){
            password1.setError("The password is too short\n" +
                    "The minimum password length is 8 characters.");
            flag++;
        }
        String str1 = password1.getText().toString();
        String str2 = password2.getText().toString();
        if(!str1.equals(str2)){
            password2.setError("Password mismatch");
            flag++;
        }
        if(flag==0)
            return true;
        else return false;
    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
