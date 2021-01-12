package app.paseico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import app.paseico.data.PointOfInterest;
import app.paseico.data.Route;
import app.paseico.data.Router;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.List;

public class RouteFinishedActivity extends AppCompatActivity {

    Intent intent;
    String nombreRuta;
    List<PointOfInterest> nombrePOIs;
    int rewpoints;
    private DatabaseReference myUsersRef = FirebaseDatabase.getInstance().getReference("users"); //Node users reference
    private Router currentRouter = new Router();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser fbusr = firebaseAuth.getCurrentUser();
    private DatabaseReference myActualUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //goToMenu.setClickable(false);
        Route route = (Route) getIntent().getExtras().get("route");
        myActualUserRef = myUsersRef.child(fbusr.getUid());
        myActualUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentRouter = snapshot.getValue(Router.class);
                int points = route.getRewardPoints();
                int actualpoints = currentRouter.getPoints();
                int updatedpoints = points + actualpoints;
                final DatabaseReference mypointsreference = myActualUserRef.child("points");
                mypointsreference.setValue(updatedpoints);
                //goToMenu.setClickable(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finished);

        TextView nombreRutaTV = findViewById(R.id.textViewTemporalMenuName);
        TextView cantPuntos = findViewById(R.id.textViewRouteFinishedPoints);

        nombreRuta = route.getName();
        if (nombreRuta != null) {
            nombreRutaTV.setText(nombreRuta);
        }

        rewpoints = route.getRewardPoints();
        cantPuntos.setText("Puntos: " + rewpoints);

        System.out.println(route.isOrdered());

        Button goToMenu = findViewById(R.id.buttonTemporalMenuStartRoute);
        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }
}