package is.aiga.bordid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NearMeActivity extends AppCompatActivity {

    public static final String GET_IMAGE_URL="http://bordid2.freeoda.com/Server/GetRestaurants.php";
    private RecyclerView recyclerView;
    private VAdapter adapter;
    public static JSONArray restaurants = null; // Array of restaurants

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_restaurant);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        downloadRestaurants();
    }

    private void downloadRestaurants() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NearMeActivity.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                try {
                    init_restaurants(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL);
    }

    public void init_restaurants(String jsonString) throws Exception {

        Log.d("IED", jsonString);

        restaurants = (JSONArray)(new JSONTokener(jsonString).nextValue());

        String[] rName = new String[restaurants.length()];
        String[] rImage = new String[restaurants.length()];

        for(int i=0; i<restaurants.length(); i++) {
            JSONObject item = (JSONObject) restaurants.get(i);
            String n = item.getString("RestName");
            String image = item.getString("RestImage");
            Log.d("IED", n);
            Log.d("IED", image);
            rName[i] = n;
            rImage[i] = image;
        }
        populate(rName, rImage);
    }

    private void populate(final String[] rName, final String[] rImage) {

        recyclerView = (RecyclerView) this.findViewById(R.id.recycle_list);
        adapter = new VAdapter(this, getData(rName, rImage));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static List<Information> getData(String[] rName, String[] rImage) {
        List<Information> data = new ArrayList<>();

        for(int i = 0; i < rName.length; i++) {
            Information current = new Information();
            current.rName = rName[i];
            current.rImage = rImage[i];
            data.add(current);
        }
        return data;
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