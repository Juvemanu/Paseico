package app.paseico;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import app.paseico.data.PointOfInterest;
import app.paseico.data.Route;
import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class RouteInformationActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewTheme;
    private TextView textViewRewardsPoints;
    private TextView textViewLength;
    private TextView textViewEstimatedTime;
    private TextView textViewOrdered;
    private ImageView themeIcon;
    private ListView listViewPoiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_information);

        Route route = setFilteredInformation();


        registerOnBackButtonClickedListener();

        registerOnStartRouteButtonClickedListener(route);


    }

    protected void registerOnStartRouteButtonClickedListener(Route route) {
        findViewById(R.id.btn_routeInfo_startRoute).setOnClickListener(v -> {
            Intent startRouteIntent = new Intent(RouteInformationActivity.this,
                    route.isOrdered() == 1 ? RouteRunnerOrderedActivity.class : RouteRunnerNotOrderedActivity.class);
            startRouteIntent.putExtra("route", route);
            startActivity(startRouteIntent);
        });
    }

    protected void registerOnBackButtonClickedListener() {
        findViewById(R.id.btn_routeInfo_back).setOnClickListener(v -> finish());
    }

    @NotNull
    protected Route setFilteredInformation() {
        textViewName = findViewById(R.id.textView_routeInfo_nameOfRoute);
        textViewTheme = findViewById(R.id.textView_routeInfo_theme);
        textViewRewardsPoints = findViewById(R.id.textView_routeInfo_rewardPoints);
        textViewLength = findViewById(R.id.textView_routeInfo_length);
        textViewEstimatedTime = findViewById(R.id.textView_routeInfo_estimatedTime);
        listViewPoiList = findViewById(R.id.listView_routeInfo_poiList);
        textViewOrdered = findViewById(R.id.textView_routeInfo_ordered);

        themeIcon = (ImageView) findViewById(R.id.imageViewIconRouteInformation);


        Route route = (Route) getIntent().getExtras().get("route");

        String name = route.getName();
        String rewardsPoints = ((Integer) route.getRewardPoints()).toString();
        int kms = (int) route.getLength() / 1000;
        int meters = (int) route.getLength() % 1000;
        String length = kms + " km y " + meters + " metros";
        int hours = ((int) route.getEstimatedTime()) / 60;
        int minutes = ((int) route.getEstimatedTime()) % 60;
        String estimatedTime = hours + " horas y " + minutes + " minutos";
        ArrayList<PointOfInterest> pois = (ArrayList) route.getPointsOfInterest();
        String theme = (route.getTheme() == null) ? "Sin temática" : route.getTheme();

        int iconIdex = CategoryManager.convertCategoryToIntDrawable(theme);


        textViewName.setText(name);
        textViewTheme.setText(theme);
        textViewRewardsPoints.setText(rewardsPoints);
        textViewLength.setText(length);
        textViewEstimatedTime.setText(estimatedTime);

        if (route.isOrdered() == 1) {
            textViewOrdered.setText(R.string.yes);
        } else {
            textViewOrdered.setText(R.string.no);
        }

        themeIcon.setImageResource(iconIdex);

        ArrayList<String> pointsOfInterestNames = new ArrayList<String>();
        for (int i = 0; i < pois.size(); i++) {
            pointsOfInterestNames.add(pois.get(i).getName());
        }

        ArrayAdapter<String> pointsOfInterestNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pointsOfInterestNames);
        listViewPoiList.setAdapter(pointsOfInterestNamesAdapter);

        listViewPoiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PointOfInterest selectedPoi = pois.get(position);

                Intent selectedPOIIntent = new Intent(RouteInformationActivity.this, SelectedPoiActivity.class);
                selectedPOIIntent.putExtra("poi", (Serializable) selectedPoi);
                startActivity(selectedPOIIntent);
            }
        });

        findViewById(R.id.btn_routeInfo_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_routeInfo_startRoute).setOnClickListener(v -> {
            checkLocation(route);
        });
        return route;
    }

    public void checkLocation(Route route) {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            Intent startRouteIntent = new Intent(RouteInformationActivity.this,
                    route.isOrdered() == 1 ? RouteRunnerOrderedActivity.class : RouteRunnerNotOrderedActivity.class);
            startRouteIntent.putExtra("route", route);
            startActivity(startRouteIntent);
            finish();
        }


    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Para poder usar Paseico necesitas activar la ubicación")
                .setCancelable(false)
                .setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No activar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
