package is.aiga.bordid;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class Service {

    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {

        URL url;

        StringBuilder sb = new StringBuilder();
        try {
            // Connect to the server
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null){
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8")); // getKey = "image"
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8")); // getValue = Langur strengur af bitmap
        }
        Log.d("IED", result.toString());
        return result.toString(); // Skilar löngum streng... image=%2F9...
    }
}




















//    public boolean validateUser(String user){
//        class UploadImage extends AsyncTask<String,Void,String> {
//
////            ProgressDialog loading;
//            //RequestHandler rh = new RequestHandler();
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
////                loading = ProgressDialog.show(MainActivity.this, "Uploading...", null,true,true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                Log.d("IED", "S:" + s);
//                Log.d("IED", "boolean: " + s.equals("true"));
//                if(s.equals("true")) {
//                    validate = true;
//                }
////                loading.dismiss();
////                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                String user_pass = params[0]; //params[0] is in this case "username" and "password"
////                String password = params[1]; //params[0] is in this case "username" and "password"
//                HashMap<String,String> data = new HashMap<>();
//
//                data.put(UPLOAD_KEY, user_pass); // UPLOAD_KEY = "image", keyword fyrir php á server
//                String result = sendPostRequest(UPLOAD_URL,data); // Sendir langan streng t.d image=%2F9... til server
//                Log.d("IED", "Result: " + result);
//                return result;
//            }
//        }
//
//        UploadImage ui = new UploadImage();
//        ui.execute(user);
//        return validate;
//    }
