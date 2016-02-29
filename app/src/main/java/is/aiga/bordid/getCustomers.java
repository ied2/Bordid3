package is.aiga.bordid;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getCustomers {

    public static final String GET_IMAGE_URL="http://bordid.freeoda.com/PhotoUpload/init.php";

    public void init() {
        class GetURLs extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    resultsToArray(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]); // strings[0] = "http://bordid.freeoda.com/PhotoUpload/getCustomers.php"
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

    // Populating array DUMMY_CREDENTIALS in LoginActivity
    private void resultsToArray(String array) throws JSONException {

//        JSONObject jsnobject = new JSONObject(array);
//        JSONArray jsonArray = jsnobject.getJSONArray("result");
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            String x = "";
//            x += jsonArray.getJSONObject(i).getString("username");
//            x += ":";
//            x += jsonArray.getJSONObject(i).getString("password");
//            Log.d("IED", x);
//            LoginActivity.DUMMY_CREDENTIALS.add(x); // <------- populating this array
//        }
    }
}
