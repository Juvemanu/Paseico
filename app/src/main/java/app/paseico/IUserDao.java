package app.paseico;

import com.google.firebase.auth.FirebaseUser;


public interface IUserDao {
    public User getUser();
    public void addGoogleUser(FirebaseUser user);
}