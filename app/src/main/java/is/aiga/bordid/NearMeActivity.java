package is.aiga.bordid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
    public static RecyclerView recyclerView;
    private VAdapter adapter;
    public static JSONArray restaurants = null; // Array of restaurants

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_restaurant);

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Download all restaurants
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

    // After receiving all data we extract information and put it in arrays
    public void init_restaurants(String jsonString) throws Exception {

        restaurants = (JSONArray)(new JSONTokener(jsonString).nextValue());

        Log.d("IED", jsonString);

        String[] rName = new String[restaurants.length()];
        String[] rImage = new String[restaurants.length()];
        String[] rphoneNumber = new String[restaurants.length()];
        String[] raddress = new String[restaurants.length()];
        String[] rwebsite = new String[restaurants.length()];
        String[] rDescription = new String[restaurants.length()];

        for(int i=0; i<restaurants.length(); i++) {
            JSONObject item = (JSONObject) restaurants.get(i);
            String n = item.getString("RestName");
            String im = item.getString("RestImage");
            String p = item.getString("RestPhoneNumber");
            String a = item.getString("RestAddress");
            String w = item.getString("RestWebsite");
            String d = item.getString("RestDescription");
            rName[i] = n;
            rImage[i] = im;
            rphoneNumber[i] = p;
            raddress[i] = a;
            rwebsite[i] = w;
            rDescription[i] = d;
        }
        populate(rName, rImage, rphoneNumber, raddress, rwebsite, rDescription);
    }

    // Populate recycleView list with our restaurant names and images
    private void populate(final String[] rName, final String[] rImage, final String[] rphoneNumber, final String[] raddress, final String[] rwebsite, final String[] rDescription) {

        recyclerView = (RecyclerView) this.findViewById(R.id.recycle_list);
        adapter = new VAdapter(this, getData(rName, rImage, rphoneNumber, raddress, rwebsite, rDescription));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static List<Restaurant> getData(String[] rName, String[] rImage, String[] rphoneNumber, String[] raddress, String[] rwebsite, String[] rDescription) {
        List<Restaurant> data = new ArrayList<>();

        for(int i = 0; i < rName.length; i++) {
            Restaurant current = new Restaurant();
            current.setName(rName[i]);
            current.setLogo(rImage[i]);
            current.setPhoneNumber(rphoneNumber[i]);
            current.setAddress(raddress[i]);
            current.setWebsite(rwebsite[i]);
            current.setDescription(rDescription[i]);
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