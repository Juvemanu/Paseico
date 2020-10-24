package app.paseico.service;

import android.util.Log;
import androidx.annotation.NonNull;
import app.paseico.data.Route;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;


public class FirebaseService{

    public static String saveRoute(Route route) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("route").add(route);
        return "Route " + route.getName() + " succesfully added to Firebase";
    }


    public static void getAllRoutes(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("route")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ROUTE", document.getId() + " => " + document.getData());
                            }
                        }else{
                            Log.d("ERROR", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static List<String> getRoutesAttribute(String attribute){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        List<String> routesAttributes = new ArrayList<>();
        database.collection("route")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String recievedAttribute = (String) document.getData().get(attribute);
                                Log.d("ROUTE NAME", document.getId() + " => " + recievedAttribute);
                                routesAttributes.add(recievedAttribute);
                            }

                        }else{
                            Log.d("ERROR", "Error getting documents: ", task.getException());
                        }
                    }
                });;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return routesAttributes;
    }

}
