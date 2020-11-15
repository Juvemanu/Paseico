package app.paseico.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import app.paseico.R;
import app.paseico.data.User;
import app.paseico.login.LogInActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class profileFragment extends Fragment {

    private profileViewModel profileViewModel;
    private DatabaseReference myUsersRef = FirebaseDatabase.getInstance().getReference("users"); //Node users reference
    private User user = new User();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser fbusr = firebaseAuth.getCurrentUser();
    private DatabaseReference myActualUserRef;
    private  Boolean firstTimeCheckBoost = false;

    private TextView textView;
    private TextView nickText, userPointsText, numberOfUserRoutes;
    int numberOfRoutes = 0;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(profileViewModel.class);



        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //textView = root.findViewById(R.id.text_home);
        nickText = root.findViewById(R.id.nickProfileText);
        userPointsText = root.findViewById(R.id.userPointsProfileText);
        numberOfUserRoutes = root.findViewById(R.id.numberOfRoutesText);

        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference routesReference = database.collection("route");


        if (user != null) {
            //String name = userAuth.getEmail();
            String uidUser = userAuth.getUid();


            routesReference.whereEqualTo("authorId", uidUser)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    numberOfRoutes++;
                                    System.out.println (" numero de rutas para ti es:"+ numberOfRoutes);

                                }
                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }


                    });

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userName = snapshot.child(uidUser).child("username").getValue(String.class);
                    int userPoints = snapshot.child(uidUser).child("points").getValue(Integer.class);
                    profileViewModel.getText().observe(getViewLifecycleOwner(), s -> nickText.setText(userName));
                    profileViewModel.getText().observe(getViewLifecycleOwner(), s -> numberOfUserRoutes.setText(Integer.toString(numberOfRoutes)));
                    profileViewModel.getText().observe(getViewLifecycleOwner(), s -> userPointsText.setText(Integer.toString(userPoints)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            myUsersRef.addListenerForSingleValueEvent(eventListener);

        }



        //profileViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText("hkjasdhfkja"));



        super.onCreate(savedInstanceState);

        Button btnLogOut = root.findViewById(R.id.buttonLogOut);
        btnLogOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LogInActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        myActualUserRef = myUsersRef.child(fbusr.getUid());
        myActualUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if(user.isBoost() && !firstTimeCheckBoost){
                    checkBoost();
                    firstTimeCheckBoost = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The db connection failed");
            }
        });


        return root;
    }

    public void checkBoost() {  //Check if the boost its already gone
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if(user.getBoostExpires() != null) {
            String strBoostDateEnding = user.getBoostExpires();
            Date actualDate = new Date();
            Date boostDateEnding = null;
            try {
                boostDateEnding = dateFormat.parse(strBoostDateEnding);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (actualDate.compareTo(boostDateEnding) >= 0) {
                Toast.makeText(getActivity(), "Tu boost ha caducado!", Toast.LENGTH_SHORT).show();
                final DatabaseReference myBoostReference = myActualUserRef.child("boost");
                myBoostReference.setValue(false);
            } else {
                Toast.makeText(getActivity(), "Tienes un boost activo que caducará el día :" + user.getBoostExpires(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}