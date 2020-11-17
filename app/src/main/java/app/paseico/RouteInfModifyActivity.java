package app.paseico;

import android.content.Intent;
import android.os.Bundle;
import app.paseico.data.Route;

public class RouteInfModifyActivity extends RouteInformationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_modify_route);

        Route route = setFilteredInformation();
        String routeID = getIntent().getStringExtra("routeID");

        registerOnBackButtonClickedListener();

        registerOnStartRouteButtonClickedListener(route);

        findViewById(R.id.btn_routeInfo_modify).setOnClickListener(v -> {
            Intent goToModifyRoutesIntent = new Intent(getApplicationContext(), ModifyRouteActivity.class);
            goToModifyRoutesIntent.putExtra("route", route);
            goToModifyRoutesIntent.putExtra("routeID", routeID);
            startActivity(goToModifyRoutesIntent);
        });
    }
}