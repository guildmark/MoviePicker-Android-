package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public abstract class ToolbarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                // User chose the "Settings" item, show the app settings UI...
                goToFilterActivity();
                return true;

            case R.id.action_profile:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                goToProfileActivity();
                return true;

            case R.id.action_importList:
                //Import CSV file to database
                //goToListActivity();

                //openFile();
                //Let user choose their own file to import
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void goToFilterActivity() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    private void goToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void goToListActivity() {
        //Go to list activity
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}