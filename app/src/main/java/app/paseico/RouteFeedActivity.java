package app.paseico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.paseico.data.PointOfInterest;
import app.paseico.data.Route;
import app.paseico.service.FirebaseService;
import app.paseico.service.RouteFeedAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.internal.http.RouteException;

import java.util.ArrayList;

public class RouteFeedActivity extends AppCompatActivity {

    private Button mBtnData;

    private DatabaseReference mDatabase;
    private RouteFeedAdapter mAdapter;
    private RecyclerView routesFeed;
    private ArrayList<Route> mRouteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_feed);

        routesFeed = (RecyclerView) findViewById(R.id.routes_feed_recyclerView);
        routesFeed.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getRoutesFromFirebase();
    }

    private void getRoutesFromFirebase(){
        mDatabase.child("routes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String name = ds.child("name").getValue().toString();
                        mRouteList.add(new Route(name, new ArrayList<PointOfInterest>())); //CORREGIR QUE IMPRIMA LOS PUNTOS DE LA RUTA EN CUESTION
                    }
                    mAdapter = new RouteFeedAdapter(mRouteList, R.layout.route_feed_row);
                    routesFeed.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}