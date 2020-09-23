package ylw.pocketgreta.apppocketgreta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ylw.pocketgreta.apppocketgreta.ui.RegistrationActivity;
import ylw.pocketgreta.apppocketgreta.ui.login.LoginActivity;

public class LogoutActivity extends AppCompatActivity {

    Button LogoutYES;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        LogoutYES = findViewById(R.id.logoutBtnyes);
        LogoutYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("FLAGUS", false);
                editor.commit();
                Singleton singleton = Singleton.getInstance();
                singleton.setTokenAuth("");
                singleton.setUsername("");
                Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
