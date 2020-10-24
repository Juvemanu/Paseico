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

import java.util.List;

public class RouteFeedActivity extends AppCompatActivity {

    private Button mBtnData;

    //Lo que se le pasa a la lista
    private static final int LISTA_NUMEROS = 100;
    private static final List<String> routesNames = FirebaseService.getRoutesAttribute("name"); ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routesFeed = (RecyclerView) findViewById(R.id.routes_feed_recyclerView);
        routesFeed.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getRoutesFromFirebase();
    }

        //Adaptador: quien le pasa los datos al recycler view
        RouteFeedAdapter adapter = new RouteFeedAdapter(routesNames, R.layout.route_feed_row);

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

        FirebaseService.getRoutesAttribute("name");
    }
}
