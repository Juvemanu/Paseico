package app.paseico.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.paseico.MainMenuActivity;
import app.paseico.MainMenuOrganizationActivity;
import app.paseico.R;
import app.paseico.data.UserDao;

public class LogInActivity extends AppCompatActivity {

    /**
     * Id para logearse en Google.
     */
    static final int GOOGLE_SIGN = 123;
    /**
     * Los milisegundos de retraso que se han acordado.
     * Valor inmutable de 3000 (milisegundos).
     */
    private static final long DELAY_MILLIS = 3000;
    /**
     * La base de datos sobre la cuál se
     * va a buscar los datos de usuario.
     * Pertenece a Firebase
     */
    private FirebaseAuth firebaseAuth;
    /**
     * El botón de Login de Google.
     */
    private SignInButton googleSignInButton;
    /**
     * Texto a editar con los datos de login,
     * siendo estos el mail y la contraseña.
     */
    private EditText etEmail;
    /**
     * Texto a editar con los datos de login,
     * siendo estos el mail y la contraseña.
     */
    private EditText etPassword;
    /**
     * El cliente de Google que se va a logear.
     */
    private GoogleSignInClient googleSignInClient;
    /**
     * El  DAO del usuario.
     */
    private final UserDao userDao = new UserDao();
    /**
     * El botón que te deja elegir si estás logeándote
     * como usuario.
     */
    private Button routerBtn;
    /**
     * El botón que te deja elegir si estás logeándote
     * como organización.
     */
    private Button organiBtn;

    /**
     * Extiende onCreate.
     * @param savedInstanceState Alude al estado guardado de instancia.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        Button btnLogin = findViewById(R.id.buttonLogIn);

        btnLogin.setOnClickListener(view -> signInPaseico());

        googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(v -> signInGoogle());

        TextView register = findViewById(R.id.textViewRegister);

        //On click you go to the register form
        register.setOnClickListener(view -> {
            Intent intent = new Intent();
            if (!isRouterTabSelected()) {
                intent = new Intent(LogInActivity.this, RegisterActivity.class);
            } else {
                intent = new Intent(LogInActivity.this,
                        RegisterOrganizationActivity.class);
            }
            startActivity(intent);
            finish();
        });

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.
                Builder().
                requestIdToken(getString(R.string.default_web_client_id)).
                requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        routerBtn = findViewById(R.id.buttonLoginRouter);
        organiBtn = findViewById(R.id.buttonLoginOrganization);
        routerBtn.setEnabled(false);
        routerBtn.setBackground(getResources().
                getDrawable(R.drawable.gradient));
        routerBtn.setTextColor(Color.WHITE);
        organiBtn.setEnabled(true);
        organiBtn.setBackgroundColor(Color.TRANSPARENT);
        organiBtn.setTextColor(getResources().
                getColor(R.color.colorPrimaryDark));

        routerBtn.setOnClickListener(new View.OnClickListener() { @Override
        public void onClick(final View view) {
            googleSignInButton.setVisibility(View.VISIBLE);
            routerBtn.setEnabled(false);
            routerBtn.setBackground(getResources().
                    getDrawable(R.drawable.gradient));
            routerBtn.setTextColor(Color.WHITE);
            organiBtn.setEnabled(true);
            organiBtn.setBackgroundColor(Color.TRANSPARENT);
            organiBtn.setTextColor(getResources().
                    getColor(R.color.colorPrimaryDark));
        }
        });

        organiBtn.setOnClickListener(new View.OnClickListener() { @Override
        public void onClick(final View view) {
            googleSignInButton.setVisibility(View.GONE);
            routerBtn.setEnabled(true);
            routerBtn.setBackgroundColor(Color.TRANSPARENT);
            routerBtn.setTextColor(getResources().
                    getColor(R.color.colorPrimaryDark));
            organiBtn.setEnabled(false);
            organiBtn.setBackground(getResources().
                    getDrawable(R.drawable.gradient));
            organiBtn.setTextColor(Color.WHITE);
        }
        });
    }

    private void signInGoogle() {
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    private void signInPaseico() {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        if (email.matches("") || pass.matches("")) {
            Toast.makeText(LogInActivity.this,
                    "Faltan campos por rellenar!", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(etEmail.getText().
                    toString(), etPassword.getText().toString()).
                    addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the
                    // signed-in user's information
                    checkCorrectTabUser(task.getResult().getUser());
                } else {
                    // If sign in fails, display a message
                    // to the user.
                    Log.w("TAG", "signInWithEmail:failure",
                            task.getException());
                    Toast.makeText(LogInActivity.this,
                            "Correo electronico o contraseña incorrectos!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Qué sucede cuando se devuelve el resultado de una
     * actividad.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode,
                                    final @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.
                        getResult(ApiException.class);

                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.
                getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential).
                addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseAuthCurrentUser =
                        firebaseAuth.getCurrentUser();
                Log.d("TAG", "Signin success");

                boolean isNewUser = task.getResult().
                        getAdditionalUserInfo().isNewUser();

                if (isNewUser) {
                    registerNewUserToDatabase(account,
                            task, firebaseAuthCurrentUser);
                } else {
                    goToMainScreen();
                }

            } else {
                Log.w("TAG", "Signin failed", task.getException());
                Toast.makeText(this,
                        "SingIn Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerNewUserToDatabase(final GoogleSignInAccount account,
                                           final Task<AuthResult> task,
                                           final FirebaseUser user) {
        Log.w("TAG", "NEW USER", task.getException());
        Log.w("TAG", user.getUid(), task.getException());

        //Wait 2 secs to load the next activity (LoginScreen)
        Handler handler = new Handler();
        handler.postDelayed(() -> {
                    Log.w("TAG", "GOOOOOOOOOOOOGLE", task.getException());
                    userDao.addGoogleUser(user, account.getDisplayName());
                    goToMainScreen();
                },
                DELAY_MILLIS);
    }

    private void goToMainScreen() {
        Intent intent = new Intent();
        if (!isRouterTabSelected()) {
            intent = new Intent(LogInActivity.this, MainMenuActivity.class);
        } else {
            intent = new Intent(LogInActivity.this,
                    MainMenuOrganizationActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private boolean isRouterTabSelected() {
        return routerBtn.isEnabled();
    }

    private void checkCorrectTabUser(final FirebaseUser user) {
        //CHECKS IF THE USER YOU ARE TRYING TO LOG IN
        // MATCHES WITH THE SELECTED TAB (ROUTER/ORGANIZATION)
        DatabaseReference mUsersReference = FirebaseDatabase.
                getInstance().getReference("users").
                child(user.getUid());
        DatabaseReference mOrganizationsReference = FirebaseDatabase.
                getInstance().getReference("organizations").
                child(user.getUid());

        ValueEventListener eventListener = new ValueEventListener() { @Override
        public void onDataChange(final DataSnapshot dataSnapshot) {
            if (!dataSnapshot.exists()) {
                if (!isRouterTabSelected()) {
                    Toast.makeText(LogInActivity.this,
                            "ERROR :Estas intentando acceder como "
                                    + "Organización desde apartado de Rutero",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogInActivity.this,
                            "ERROR :Estas intentando acceder como  "
                                    + "Rutero desde apartado de Organización",
                            Toast.LENGTH_SHORT).show();
                }

                FirebaseAuth.getInstance().signOut();
            } else {
                goToMainScreen();
            }
        }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        };
        if (!isRouterTabSelected()) {
            mUsersReference.addListenerForSingleValueEvent(eventListener);
        } else {
            mOrganizationsReference.
                    addListenerForSingleValueEvent(eventListener);
        }
    }
}
