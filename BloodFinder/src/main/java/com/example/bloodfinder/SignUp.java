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

public class SignUp extends Activity {
    LocationManager mLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
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
    public static class PlaceholderFragment extends Fragment  {


        private Button btnSignUp;
        private String blood;
        private String Loclong="";
        private String Loclat="";
        private LocationManager mLocationManager;
        private EditText etNewName,etNewPass,etNewMobile,etDob,etBlood;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {


            Log.i("Location","onCreateView");
            View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
            Log.i("Location","View Inflated");
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

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
            Log.i("Location","got Manager");


            etNewName=(EditText)rootView.findViewById(R.id.etNewName);

            etNewMobile=(EditText)rootView.findViewById(R.id.etNewMobile);
            etNewPass=(EditText)rootView.findViewById(R.id.etNewPass);
            etDob=(EditText)rootView.findViewById(R.id.etDob);
            etBlood=(EditText)rootView.findViewById(R.id.etBlood);
            btnSignUp=(Button)rootView.findViewById(R.id.btnSignUp);

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name=etNewName.getText().toString().replaceAll(" ","+");
                    blood=etBlood.getText().toString().replace('+','P');
                    blood=etBlood.getText().toString().replace('-','N');
                    new HttpAsyncTask().execute("http://bloodplus.netau.net/DB_Query.php?q=createUser&name="+etNewName.getText().toString()+"&mobile="+etNewMobile.getText().toString()+"&dob="+etDob.getText().toString()+"&pass="+etNewPass.getText().toString()+"&blood="+etBlood.getText().toString()+"&long="+Loclong+"&lat="+Loclat);
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
                        SharedPreferences sharedPref =getActivity().getSharedPreferences("bloodFinderSf", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("uBlood",blood);

                        editor.commit();
                        Toast.makeText(getActivity(),blood,Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), DashBoard.class));
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
