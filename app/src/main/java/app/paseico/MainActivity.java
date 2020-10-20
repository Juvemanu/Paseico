package app.paseico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    GoogleMap map;
    Marker marker;

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
            LatLng m1 = new LatLng(20, 40);
            marker = map.addMarker(new MarkerOptions()
                    .position(m1)
                    .draggable(true)
                    .title("My marker")
                    .snippet("Lat")
            );
            map.setOnInfoWindowClickListener(this);
            map.moveCamera(CameraUpdateFactory.newLatLng(m1));
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            Toast.makeText(this, "Info Window is clicked", Toast.LENGTH_LONG).show();
        }
    }