package app.paseico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private TextView userPointsText;
    private TextView numberOfRoutesText;
    private TextView nickText;
    //private  FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userPointsText = findViewById(R.id.userPointsDashboardText);
        numberOfRoutesText = findViewById(R.id.numberOfRoutesDashboardText);
        nickText = findViewById(R.id.nickDashboardText);

        //

        numberOfRoutesText.setText("10");

        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = mDatabase.child("users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("UXb19yqyQSNvAdQiEAbK6PcYIlo2").child("username").getValue(String.class);
                int userPoints = snapshot.child("UXb19yqyQSNvAdQiEAbK6PcYIlo2").child("points").getValue(Integer.class);
                nickText.setText(userName);
                userPointsText.setText(Integer.toString(userPoints));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);




    }
}