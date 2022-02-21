package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "se.umu.maka0437.ou3.username";

    User newUser;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Add listeners to user and pass inputs

        //Add register button
        Button regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert viewer with hashed pass into database
                //newUser = new User(username, password);
                //db.userDao().insertUser(newUser);

                //Go back to main activity
                goToMain();
            }
        });
    }
    private void goToMain() {
        //Fix with start for result
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        startActivity(intent);
    }
}