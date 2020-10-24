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
    public ArrayList<String> routesAttributes = new ArrayList<>();

    public static String saveRoute(Route route) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("route").add(route);
        return "Route " + route.getName() + " succesfully added to Firebase.";
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
                });;
    }

    public void getRoutesAttribute(String attribute){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("route")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String recievedAttribute = (String) document.getData().get(attribute);
                                Log.d("ROUTE NAME", document.getId() + " => " + recievedAttribute);
                                System.out.println(recievedAttribute + "ESTE ES EL ATRIBUTO RECIBIDO STRING");
                                routesAttributes.add(recievedAttribute);
                            }
                        }else{
                            Log.d("ERROR", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
