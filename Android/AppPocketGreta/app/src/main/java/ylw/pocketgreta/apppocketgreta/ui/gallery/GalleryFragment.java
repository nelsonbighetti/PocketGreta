package ylw.pocketgreta.apppocketgreta.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import ylw.pocketgreta.apppocketgreta.LogoutActivity;
import ylw.pocketgreta.apppocketgreta.MainActivity;
import ylw.pocketgreta.apppocketgreta.R;
import ylw.pocketgreta.apppocketgreta.Singleton;
import ylw.pocketgreta.apppocketgreta.ui.home.ChooseActivity;
import ylw.pocketgreta.apppocketgreta.ui.login.LoginActivity;

public class GalleryFragment extends Fragment {

    int bonus;
    TextView bonusText;
    private GalleryViewModel galleryViewModel;
    Button buttonLogin;
    TextView usernameText;
    TextView bonusTextView;
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        bonusText = root.findViewById(R.id.bonus_num);
        bonusTextView = root.findViewById(R.id.bonus);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String username) {
                textView.setText(username);
            }
        });
        buttonLogin = root.findViewById(R.id.button_login);
        usernameText = root.findViewById(R.id.text_name);
        buttonLogin.setVisibility(View.GONE);
        Singleton singleton = Singleton.getInstance();
        String username;
        username = singleton.getUsername();
        if(username != null) {
            if (username.equals("")) {
                buttonLogin.setVisibility(View.VISIBLE);
                usernameText.setText("");
                bonusTextView.setText("");
                bonusText.setText("No info your bonuses. Login!");
            } else {
                buttonLogin.setVisibility(View.GONE);
                bonusTextView.setText("Your bonuses");
                usernameText.setText("Username");
                String tokenAuth = singleton.getTokenAuth();
                bonusServerData(username, tokenAuth);
                bonusText.setText(String.valueOf(bonus));
            }
        }
        else
        {
            usernameText.setText("");
            bonusTextView.setText("");
            buttonLogin.setVisibility(View.VISIBLE);
            bonusText.setText("No info your bonuses. Login!");
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return root;
    }
    void bonusServerData(String username, String tokenAuth) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("authenticationToken", tokenAuth);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post ("http://457f5a38fd2e.ngrok.io/rest/acc/"+username)
                .addHeaders("Authorization", "Bearer "+tokenAuth)
                .setPriority (Priority.MEDIUM)
                .build ()
                .getAsJSONObject (new JSONObjectRequestListener() {
                    @Override
                    public void onResponse (JSONObject response) {
                        try {
                            bonus = response.getInt("bonuses");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError (ANError error) {
                        Log.d("Registration",error.toString());
                    }
                });

    }

}
