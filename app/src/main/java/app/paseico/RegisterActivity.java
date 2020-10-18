package app.paseico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etSurname, etUsername, etEmail, etPassword, etPasswordConf;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.editTextName);
        etSurname = findViewById(R.id.editTextSurname);
        etEmail = findViewById(R.id.editTextEmail);
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        etPasswordConf = findViewById(R.id.editTextPasswordConf);
        btnRegister = findViewById(R.id.buttonRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegister();
            }
        });
    }

    private void checkRegister(){
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String email = etEmail.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConf = etPasswordConf.getText().toString();
        if(name != null
                && surname !=null
                && email != null
                && username != null
                && password != null
                && passwordConf != null){ //Check that the fields aren't empty
            if(password.length()>=6) {
                if (password.equals(passwordConf)) { //Check if passwords match
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference userNameRef = ref.child("users");
                    Query queries = userNameRef.orderByChild("username").equalTo(username);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                //create new user
                                submitRegister(name, surname, email, username, password);
                            } else {
                                Context context = getApplicationContext();
                                CharSequence text = "Ese nombre de usuario ya existe";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    queries.addListenerForSingleValueEvent(eventListener);
                } else { //Passwords doesn't match
                    Context context = getApplicationContext();
                    CharSequence text = "Las contraseñas no coinciden";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }else{
                Context context = getApplicationContext();
                CharSequence text = "La contraseña debe contener mínimo 6 caracteres";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        } else { //Empty fields
            Context context = getApplicationContext();
            CharSequence text = "Por favor, rellene todos los campos para poder registrarse";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private void submitRegister(String name, String surname, String email, String username, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserDao uDao = new UserDao();
                            uDao.addUser(user,username, name, surname);
                            Toast.makeText(RegisterActivity.this, "Registro completado!",
                                    Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() { //Wait 2 secs to load the next activity (LoginScreen)
                                @Override
                                public void run() {
                                    try {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }catch (Exception e){}
                                }
                            },2000);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Error: El correo electrónico ya existe",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}