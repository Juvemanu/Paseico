package app.paseico.service;

import androidx.annotation.NonNull;
import app.paseico.data.Route;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class FirebaseService{

    public static String saveRoute(Route route) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("route").add(route);
        return "Route " + route.getName() + " succesfully added to Firebase.";
    }

    //TODO: Get data form each document, in order to show it in the recycler view
    public static void getAllRoutes(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("route").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfRoutes = task.getResult().getDocuments();
                        }
                    }
                });;
    }

}
