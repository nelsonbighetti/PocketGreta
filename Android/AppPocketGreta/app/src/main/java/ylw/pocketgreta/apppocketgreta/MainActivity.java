package ylw.pocketgreta.apppocketgreta;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ylw.pocketgreta.apppocketgreta.ui.RegistrationActivity;
import ylw.pocketgreta.apppocketgreta.ui.home.ChooseActivity;
import ylw.pocketgreta.apppocketgreta.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    public String username;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private AppBarConfiguration mAppBarConfiguration;
    boolean USER;
    String tokenRefresh;
    String tokenAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidNetworking.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        USER = pref.getBoolean("FLAGUS", false);
        if(USER == true) {
            tokenRefresh = pref.getString("tokenRefresh", "");
            username = pref.getString("username", "");
            USER = pref.getBoolean("FLAGUS", false);
            Log.d("Token", tokenRefresh);
            ServerDataLoad();
        }

    }

    void ServerDataLoad()
    {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("refreshToken", tokenRefresh);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post ("http://457f5a38fd2e.ngrok.io/rest/auth/refresh/token")
                .addJSONObjectBody(jsonObject)
                .setPriority (Priority.MEDIUM)
                .build ()
                .getAsJSONObject (new JSONObjectRequestListener() {
                    @Override
                    public void onResponse (JSONObject response) {
                        Log.d("Login","Login Succesfull");
                        try {
                            tokenAuth  = response.getString("authenticationToken");
                            Singleton.getInstance(tokenAuth, username);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d("Login","Login Error");
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        USER = pref.getBoolean("FLAGUS", true);
        if(USER==true){
            menu.findItem(R.id.action_login).setVisible(false);
            menu.findItem(R.id.action_registration).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(true);
        }
        else{
            menu.findItem(R.id.action_login).setVisible(true);
            menu.findItem(R.id.action_registration).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        Log.d("flag","HER");
        return true;
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateMenuItems(menu);
        return super.onPrepareOptionsMenu(menu);
    }
    private void updateMenuItems(Menu menu){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        USER = pref.getBoolean("FLAGUS", true);
        if(USER==true){
            menu.findItem(R.id.action_login).setVisible(false);
            menu.findItem(R.id.action_registration).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(true);
        }
        else{
            menu.findItem(R.id.action_login).setVisible(true);
            menu.findItem(R.id.action_registration).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(false);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d("flag","click");
        // Операции для выбранного пункта меню
        switch (item.getItemId())
        {
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                USER = true;
                return true;
            case R.id.action_registration:
                startActivity(new Intent(this, RegistrationActivity.class));
                return true;
            case R.id.action_logout:
                startActivity(new Intent(this, LogoutActivity.class));
                USER = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
