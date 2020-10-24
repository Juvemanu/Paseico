package app.paseico;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.paseico.data.Route;
import app.paseico.service.FirebaseService;
import app.paseico.service.RouteFeedAdapter;
import com.squareup.okhttp.internal.http.RouteException;

import java.util.ArrayList;
import java.util.List;

public class RouteFeedActivity extends AppCompatActivity {

    private RecyclerView routesFeed;

    //Lo que se le pasa a la lista

    FirebaseService res = new FirebaseService();

    private ArrayList<String> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routesFeed = (RecyclerView) findViewById(R.id.routes_feed_recyclerView);

        //Añadir separadores
        routesFeed.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //Convertirlo en una lista
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        routesFeed.setLayoutManager(linearLayoutManager);
        routesFeed.setHasFixedSize(true);

        //Adaptador: quien le pasa los datos al recycler view
        System.out.println(routes  + " <<<<<<<<<ESTE ES EL VALOR DE ROUTES EN ROUTEFEEDACTIVITY");
        RouteFeedAdapter adapter = new RouteFeedAdapter(routes);

        //Añadir el adapter al recyclerview
        routesFeed.setAdapter(adapter);

        //FirebaseService.getRoutesAttribute("name");
    }
}