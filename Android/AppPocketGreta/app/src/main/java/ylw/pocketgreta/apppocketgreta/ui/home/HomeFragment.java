package ylw.pocketgreta.apppocketgreta.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
        LatLng pp = new LatLng(59.986509, 30.348182);
        MarkerOptions option = new MarkerOptions();
        option.position(pp).title(
                "Separate garbage collection").snippet("Будь лапочкой, разделяй мусор").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name));
        map.addMarker(option);
        map.moveCamera(CameraUpdateFactory.newLatLng(pp));
        map.setMyLocationEnabled(true);
        parsJSONfile();
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                startActivity(new Intent(getActivity(), ChooseActivity.class));
            }
        });

    }
    public void onMapReadyPoint(long latitude, long longitude, String type, String subtype,
                           String description, String details, int uid) {
        LatLng pp = new LatLng(latitude, longitude);
        MarkerOptions option = new MarkerOptions();
        option.position(pp).title(
                type).snippet(description);
        switch (type){
            case "trashbin":
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name));
                return;
            case "shop":
                return;
            case "event":
                return;
        }
    }
    public void parsJSONfile(){
        String url = "";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                long latitude = employee.getLong("latitude");
                                long longitude = employee.getLong("longitude");
                                String type = employee.getString("type");
                                String subtype = employee.getString("subtype");
                                String description = employee.getString("description");
                                String details = employee.getString("details");
                                int uid = employee.getInt("uid");
                                onMapReadyPoint(latitude, longitude, type, subtype, description, details, uid);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
