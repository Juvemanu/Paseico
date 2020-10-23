package app.paseico;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.paseico.service.RouteFeedAdapter;
import com.squareup.okhttp.internal.http.RouteException;

public class RouteFeedActivity extends AppCompatActivity {

    private RecyclerView routesFeed;

    //Lo que se le pasa a la lista
    private static final int LISTA_NUMEROS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_feed);

        routesFeed = (RecyclerView) findViewById(R.id.routes_feed_recyclerView);

        //Añadir separadores
        routesFeed.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //Convertirlo en una lista
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        routesFeed.setLayoutManager(linearLayoutManager);
        routesFeed.setHasFixedSize(true);

        //Adaptador: quien le pasa los datos al recycler view
        RouteFeedAdapter adapter = new RouteFeedAdapter(LISTA_NUMEROS);

        //Añadir el adapter al recyclerview
        routesFeed.setAdapter(adapter);
    }
}