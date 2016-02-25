package is.aiga.bordid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class NearMeActivity extends AppCompatActivity {

    ListView list;
    static final String[] list_restaurants_demo =
            new String[] { "Grillmarkaðurinn", "Argentína", "Eldsmiðjan", "Ruby Tuesday", "Fridays",
                           "Hamborgarafabrikkan", "Krua Thai", "Noodle Station"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListAdapter adapter = new ListAdapter(this, list_restaurants_demo);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(NearMeActivity.this, list_restaurants_demo[+position], Toast.LENGTH_SHORT).show();
                // Búa til activity ef klikkað er á item í listanum
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}