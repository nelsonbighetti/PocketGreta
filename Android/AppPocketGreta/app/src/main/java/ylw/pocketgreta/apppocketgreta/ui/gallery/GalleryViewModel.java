package ylw.pocketgreta.apppocketgreta.ui.gallery;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import ylw.pocketgreta.apppocketgreta.Singleton;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public GalleryViewModel() {

        mText = new MutableLiveData<>();
        Singleton singleton = Singleton.getInstance();
        if(singleton.getUsername() != null) {
            if (singleton.getUsername().equals("")) {
                mText.setValue("LOGIN");
            } else {
                String username = singleton.getUsername();
                mText.setValue(username);

            }
        }
        else mText.setValue("LOGIN");
    }

    public LiveData<String> getText() {
        return mText;
    }
}