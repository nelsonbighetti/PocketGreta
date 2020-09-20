package ylw.pocketgreta.apppocketgreta.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import ylw.pocketgreta.apppocketgreta.R;
import ylw.pocketgreta.apppocketgreta.Singleton;

public class GalleryFragment extends Fragment {

    int bonus;
    TextView bonusText;
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        bonusText = root.findViewById(R.id.bonus_num);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String username) {
                textView.setText(username);
            }
        });
        Singleton singleton = Singleton.getInstance();
        String username;
        username = singleton.getUsername();
        if(username != null) {
            if (username.equals("")) {
                bonusText.setText("No info your bonuses. Login!");
            } else {
                String tokenAuth = singleton.getTokenAuth();
                bonusServerData(username, tokenAuth);
                bonusText.setText(String.valueOf(bonus));
            }
        }
        else bonusText.setText("No info your bonuses. Login!");
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
