package com.example.bloodfinder;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CreatePost extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private EditText etHeading,etPostMobile,etdesc,etPostBlood;
        private RatingBar rbUrgency;
        private Button btnCreatePost;
        private String Loclong="";
        private String Loclat="";
        private LocationManager mLocationManager;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_post, container, false);
            etHeading=(EditText)rootView.findViewById(R.id.etHeading);
            etPostMobile=(EditText)rootView.findViewById(R.id.etPostMobile);
            etPostBlood=(EditText)rootView.findViewById(R.id.etPostBlood);
            etdesc=(EditText)rootView.findViewById(R.id.etDesc);
            rbUrgency=(RatingBar)rootView.findViewById(R.id.rbUrgency);
            btnCreatePost=(Button)rootView.findViewById(R.id.btnCreatePost);

            mLocationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
            Log.i("Location","got Mabegr");
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,
                    0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Loclong= String.valueOf(location.getLongitude());
                    Loclat= String.valueOf(location.getLatitude());

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {
                    btnCreatePost.setEnabled(true);
                }

                @Override
                public void onProviderDisabled(String s) {
                    Toast.makeText(getActivity(),"Turn On Your GPS!",Toast.LENGTH_LONG).show();
                    btnCreatePost.setEnabled(false);
                }
            });
            Log.i("Location","got Manager");

            btnCreatePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new HttpAsyncTask().execute("http://bloodplus.netau.net/DB_Query.php?q=createPost&uid="+"&head="+etHeading.getText().toString()+"&desc="+etdesc.getText().toString()+"&mobile="+etPostMobile.getText().toString()+"&urgency="+(int)rbUrgency.getRating()+"&blood="+etPostBlood.getText().toString()+"&long="+Loclong+"&lat="+Loclat);
                }
            });


            return rootView;
        }
        public static String GET(String url){
            Log.i("url", url);
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
            while((line = bufferedReader.readLine()) != null )
                result += line;

            inputStream.close();
            return result;

        }


        public class HttpAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {

                return GET(urls[0]);
            }
            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                try{
                    JSONObject json=new JSONObject(result);
                    String status=json.getString("status");

                    if(status.compareTo("Success")==0){

                        startActivity(new Intent(getActivity(),DashBoard.class));
                    }
                    else{
                        Toast.makeText(getActivity(), "Wrong Credentials", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Log.e("jsonErr",e.getLocalizedMessage());
                }

            }


        }
    }

}
