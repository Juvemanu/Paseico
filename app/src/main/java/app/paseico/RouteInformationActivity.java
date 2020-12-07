package app.paseico;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

import app.paseico.data.PointOfInterest;
import app.paseico.data.Route;

public class RouteInformationActivity extends AppCompatActivity {

    private TextView textView_name;
    private TextView textView_theme;
    private TextView textView_rewardsPoints;
    private TextView textView_length;
    private TextView textView_estimatedTime;
    private TextView textView_ordered;
    private ImageView themeIcon;
    private ListView listView_poiList;
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
        textView_name = findViewById(R.id.textView_routeInfo_nameOfRoute);
        textView_theme = findViewById(R.id.textView_routeInfo_theme);
        textView_rewardsPoints = findViewById(R.id.textView_routeInfo_rewardPoints);
        textView_length = findViewById(R.id.textView_routeInfo_length);
        textView_estimatedTime = findViewById(R.id.textView_routeInfo_estimatedTime);
        listView_poiList = findViewById(R.id.listView_routeInfo_poiList);
        textView_ordered = findViewById(R.id.textView_routeInfo_ordered);

        themeIcon = (ImageView) findViewById(R.id.imageViewIconRouteInformation);


        Route route = (Route) getIntent().getExtras().get("route");

        String name = route.getName();
        String rewardsPoints = ((Integer) route.getRewardPoints()).toString();
        int kms = (int) route.getLength() / 1000;
        int meters = (int) route.getLength() % 1000;
        String length = kms + " km y " + meters + " metros";
        int hours = ((int) route.getEstimatedTime()) / 60;
        int minutes = ((int) route.getEstimatedTime()) % 60;
        String estimatedTime = hours + " horas y " + minutes + " minutos" ;
        ArrayList<PointOfInterest> pois = (ArrayList) route.getPointsOfInterest();
        String theme = (route.getTheme() == null) ? "Sin temática" : route.getTheme();

        int iconIdex = CategoryManager.ConvertCategoryToIntDrawable(theme);


        textView_name.setText(name);
        textView_theme.setText(theme);
        textView_rewardsPoints.setText(rewardsPoints);
        textView_length.setText(length);
        textView_estimatedTime.setText(estimatedTime);

        if (route.isOrdered() == 1) {
            textView_ordered.setText(R.string.yes);
        }else{
            textView_ordered.setText(R.string.no);
        }

        themeIcon.setImageResource(iconIdex);

        ArrayList<String> pointsOfInterestNames = new ArrayList<String>();
        for (int i = 0; i < pois.size(); i++) {
            pointsOfInterestNames.add(pois.get(i).getName());
        }

        ArrayAdapter<String> pointsOfInterestNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  pointsOfInterestNames);
        listView_poiList.setAdapter(pointsOfInterestNamesAdapter);

        listView_poiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    public void checkLocation(Route route){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        } else{
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
