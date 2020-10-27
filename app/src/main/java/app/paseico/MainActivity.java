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
import android.content.Intent;
import android.net.Uri;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.ComponentName;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    GoogleMap map;
    Marker marker;
    static final LatLng EXAMPLE = new LatLng(39.46945815285248, -0.3717630753705533);

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
        Marker marker = map.addMarker(new MarkerOptions()
                .position(EXAMPLE)
        );
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(EXAMPLE,18),5000,null); //animation
        getInfoWindow(marker);
        map.setInfoWindowAdapter(new UserInfoWindowAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);
        //map.moveCamera(CameraUpdateFactory.newLatLng(EXAMPLE));
    }

    public void getInfoWindow(Marker marker){
        Geocoder geocoder;
        List<Address> addresses;
        String title = "Starbuck Coffee";
        String city = "";
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            Address place = addresses.get(0);
            //title = place.getLocality();
            city = place.getAddressLine(0);
        }catch (IOException e){
            e.printStackTrace();
        }

        marker.setTitle(title);
        marker.setSnippet(city + "\nClick aquí para ver en Google Maps");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Redirigiendo a Google Maps", Toast.LENGTH_LONG).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com?q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude));
        startActivity(browserIntent);
    }
}