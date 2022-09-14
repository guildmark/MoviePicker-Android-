package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private boolean checkPassword(String pass) {

        //Compare the hashed password that user typed in with the one in the database
        if(hashPassPBKDF2(pass).equals(currentUser.password)) {
            return true;
        }
        return false;
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

}