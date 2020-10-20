package app.paseico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    GoogleMap map;
    Marker marker;
    static final LatLng EXAMPLE = new LatLng(37.380346, -6.007743);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.animateCamera(CameraUpdateFactory.zoomTo(13f));

        Marker marker = map.addMarker(new MarkerOptions()
                .position(EXAMPLE)
                .draggable(true)
        );
        getInfoWindow(EXAMPLE.latitude, EXAMPLE.longitude, marker);
        map.setInfoWindowAdapter(new UserInfoWindowAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);
        map.moveCamera(CameraUpdateFactory.newLatLng(EXAMPLE));
    }

    public void getInfoWindow(double lat, double lng, Marker marker){
        Geocoder geocoder;
        List<Address> addresses;
        String title = "";
        String city = "";
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(EXAMPLE.latitude, EXAMPLE.longitude, 1);
            Address place = addresses.get(0);
            title = place.getLocality();
            city = place.getAddressLine(0);
        }catch (IOException e){
            e.printStackTrace();
        }

        marker.setTitle(title);
        marker.setSnippet(city);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info Window is clicked", Toast.LENGTH_LONG).show();
        marker.showInfoWindow();
    }
}