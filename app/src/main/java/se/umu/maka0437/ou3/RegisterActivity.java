package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "se.umu.maka0437.ou3.username";
    public static final String EXTRA_PASSWORD = "se.umu.maka0437.ou3.password";
    public static final String EXTRA_USER = "se.umu.maka0437.ou3.user";

    User newUser;
    String username, password, hashedPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Add listeners to user and pass inputs
        EditText userText = findViewById(R.id.userInput);
        userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userText.setText("");
            }
        });
        EditText passText = findViewById(R.id.passInput);

        //Add register button
        Button regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert viewer with hashed pass into database
                //newUser = new User(username, password);
                //db.userDao().insertUser(newUser);

                username = userText.getText().toString();
                //hash the password before sending it
                password = hashPassPBKDF2(passText.getText().toString());
                //Check for requirements for password, capital letters and numbers etc?

                Toast.makeText(RegisterActivity.this, username, Toast.LENGTH_SHORT).show();
                Toast.makeText(RegisterActivity.this, password, Toast.LENGTH_SHORT).show();



                //Go back to main activity
                goToMain();
            }
        });
    }

    public String hashPassPBKDF2(String pass) {
        byte[] hash;

        //Create salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = null;
        try {
            //Change string?
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            if (factory != null) {
                hash = factory.generateSecret(spec).getEncoded();
                //Get string of hashed password and return it
                return new String(hash, StandardCharsets.UTF_8);
            }
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Change this for return with result!
    private void goToMain() {
        //Fix with start for result

        newUser.username = username;
        newUser.password = password;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, hashedPass);
        intent.putExtra(EXTRA_USER, newUser);
        startActivity(intent);
    }
    /*
    private void insertUser(User user) {
        new Thread(() -> db.userDao().insertUser(user));
    }

    private void deleteUser(User user) {
        new Thread(() -> db.userDao().deleteUser(user));
    }
    */
}