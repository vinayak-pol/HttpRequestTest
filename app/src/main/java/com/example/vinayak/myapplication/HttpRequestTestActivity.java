package com.example.vinayak.myapplication;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpRequestTestActivity extends AppCompatActivity {


    TextView tvIsConnected,tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_request_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get reference to the views
        tvResponse = (TextView) findViewById(R.id.etResponse);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // check if you are connected or not
                if(isConnected()){
                    tvIsConnected.setBackgroundColor(0xFF00CC00);
                    tvIsConnected.setText("You are conncted");
                }
                else{
                    tvIsConnected.setText("You are NOT conncted");
                }

                // call AsynTask to perform network operation on separate thread

                //way 3 call
                // new HttpAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");

                //way 2 call
                new HttpAsyncTask().execute("https://fakerestapi.azurewebsites.net/api/Users");

            }
        });
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_SHORT).show();
            //tvResponse.setText(result);
            String UN="",PW="",user="";
            try {
                /*//Way 1
                //For Single Json Object URL::https://fakerestapi.azurewebsites.net/api/Users/1

                JSONObject json = new JSONObject(result); // convert String to JSONObject

                    UN=(String) json.get("UserName");
                    PW=(String) json.get("Password");

                    user = UN + " " + PW + "\n";
                */

                //Way 2
                //For Array of  Json Objects URL::https://fakerestapi.azurewebsites.net/api/Users
                JSONArray values = new JSONArray(result);
                for (int i = 0; i < values.length(); i++) {
                    JSONObject item = values.getJSONObject(i);
                    UN = item.getString("UserName");
                    PW = item.getString("Password");
                    user =user+""+UN + " " + PW + "\n";
                }

                //Way 3
                /*  //For Single Json Object having ArrayObj inside URL::http://hmkcode.appspot.com/rest/controller/get.json
                    {"articleList":
                                    [{
                                        "title":"Android Internet Connection Using HTTP GET (HttpClient)",
                                        "url":"http://hmkcode.com/android-internet-connection-using-http-get-httpclient/",
                                        "categories":["Android"],
                                        "tags":["android","httpclient","internet"]
                                    },
                                    {
                                        "title":" Android | Taking Photos with Android Camera ",
                                        "url":"http://hmkcode.com/android-camera-taking-photos-camera/",
                                        "categories":["Android"],
                                        "tags":["android","camera"]
                                    }]
                                }

                    JSONObject json = new JSONObject(result); // convert String to JSONObject
                    JSONArray articles = json.getJSONArray("articleList"); // get articles array
                    for (int i = 0; i < 2; i++) {
                        articles.getJSONObject(i); // get first article in the array
                        articles.getJSONObject(i).names(); // get first article keys [title,url,categories,tags]
                        user=user+"Title:"+articles.getJSONObject(i).getString("title")+"\nURL:"+
                        articles.getJSONObject(i).getString("url")+"\nCategories:"+
                        articles.getJSONObject(i).getString("categories")+"\nTags:"+
                        articles.getJSONObject(i).getString("tags")+"\n-------------------------\n";
                    }
                  */
                tvResponse.setText(user);
            }
            catch(JSONException e)
            {
                tvResponse.setText(e.toString());
                Toast.makeText(getBaseContext(), "JSONException!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
