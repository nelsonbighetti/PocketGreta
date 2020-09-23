package ylw.pocketgreta.apppocketgreta.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ylw.pocketgreta.apppocketgreta.MainActivity;
import ylw.pocketgreta.apppocketgreta.MapsActivity;
import ylw.pocketgreta.apppocketgreta.R;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    GoogleMap map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        parsJSONfile();
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                startActivity(new Intent(getActivity(), ChooseActivity.class));
            }
        });

    }
    public void onMapReadyPoint(Double latitude, Double longitude, String type, String subtype,
                           String description, String details, int uid) {
        LatLng pp = new LatLng(latitude, longitude);
        MarkerOptions option = new MarkerOptions();
        option.position(pp).title(
                type).snippet(description);
        switch (type){
            case "TRASHBIN":
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_trash));
                map.addMarker(option);
                break;
            case "SHOP":
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_shop));
                map.addMarker(option);
                break;
            case "EVENT":
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_event));
                map.addMarker(option);
                break;
            default:
                return;

        }
    }
    public void parsJSONfile(){
        String url = "http://457f5a38fd2e.ngrok.io/rest/map/all";
        AndroidNetworking.get("http://457f5a38fd2e.ngrok.io/rest/map/all")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject employee = null;
                            try {
                                employee = response.getJSONObject(i);

                            Double latitude = employee.getDouble("latitude");
                            Double longitude = employee.getDouble("longitude");
                            String type = employee.getString("type");
                            String subtype = employee.getString("subtype");
                            String description = employee.getString("descriptions");
                            String details = employee.getString("details");
                            int uid = employee.getInt("id");
                            Log.d("JSON",type+subtype+description+details);

                            onMapReadyPoint(latitude, longitude, type, subtype, description, details, uid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }
}
