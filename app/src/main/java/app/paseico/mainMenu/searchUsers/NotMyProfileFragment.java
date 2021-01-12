package app.paseico.mainMenu.searchUsers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import app.paseico.R;
import app.paseico.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.List;

public class NotMyProfileFragment extends Fragment {
    ImageView imageProfile;
    TextView textViewFollowersNumber;
    TextView textViewFollowersText;
    TextView textViewFollowingText;
    TextView textViewFollowingNumber;
    TextView textViewFullname;
    TextView textViewUsername;
    FirebaseUser firebaseUser;
    String profileid;
    User actualUser;
    User foreignUser;
    String searchedUser;
    Button buttonLogOut;
    private Boolean firstTimeCheckBoost = false;
    private User user = new User();
    private String usernameFirebase;
    private UserAdapter userAdapter;
    private List<User> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaanceState) {

        View view = inflater.inflate(R.layout.fragment_notmyprofile, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");
        FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                actualUser = dataSnapshot.getValue(User.class);
                FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user.getUsername().equals(profileid)) {
                                searchedUser = snapshot.getKey();
                                FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        usernameFirebase = user.getUsername();
                                        userInfo();
                                        getFollowers();
                                        checkFollow();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageProfile = view.findViewById(R.id.image_profile);
        textViewFollowersNumber = view.findViewById(R.id.textView_followersNumber);
        textViewFollowingNumber = view.findViewById(R.id.textView_followingNumber);
        textViewUsername = view.findViewById(R.id.username);
        textViewFullname = view.findViewById(R.id.fullname);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);
        textViewFollowersText = view.findViewById(R.id.textView_FollowersText);
        textViewFollowingText = view.findViewById(R.id.textView_FollowingText);


        buttonLogOut.setOnClickListener(v -> {
            String btn = buttonLogOut.getText().toString();
            if (btn.equals("Seguir")) {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(actualUser.getUsername())
                        .child("following").child(profileid).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                        .child("followers").child(actualUser.getUsername()).setValue(true);
                buttonLogOut.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            } else if (btn.equals("Siguiendo")) {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(actualUser.getUsername())
                        .child("following").child(profileid).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                        .child("followers").child(actualUser.getUsername()).removeValue();
                buttonLogOut.setBackground(getResources().getDrawable(R.drawable.gradient));
            }
        });


        return view;
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(searchedUser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }
                User user = snapshot.getValue(User.class);
                foreignUser = user;
                textViewUsername.setText(user.getUsername());
                textViewFullname.setText(user.getName());
                activateFollowListeners();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkFollow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(actualUser.getUsername()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(profileid).exists()) {
                    buttonLogOut.setText("Siguiendo");
                    buttonLogOut.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    buttonLogOut.setText("Seguir");
                    buttonLogOut.setBackground(getResources().getDrawable(R.drawable.gradient));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewFollowersNumber.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewFollowingNumber.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void activateFollowListeners() {
        textViewFollowersNumber.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("id", foreignUser.getUsername());
            intent.putExtra("title", "followers");
            startActivity(intent);
        });

        textViewFollowersText.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("id", foreignUser.getUsername());
            intent.putExtra("title", "followers");
            startActivity(intent);
        });

        textViewFollowingNumber.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("id", foreignUser.getUsername());
            intent.putExtra("title", "following");
            startActivity(intent);
        });

        textViewFollowingText.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("id", foreignUser.getUsername());
            intent.putExtra("title", "following");
            startActivity(intent);
        });
    }

}
