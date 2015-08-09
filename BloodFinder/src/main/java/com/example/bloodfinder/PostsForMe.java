package com.example.bloodfinder;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

public class PostsForMe extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_for_me);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.posts_for_me, menu);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_posts_for_me, container, false);

            SharedPreferences sharedPref =getActivity().getSharedPreferences("bloodFinderSf", MODE_PRIVATE);
            String blood=sharedPref.getString("uBlood","noUser");
            Toast.makeText(getActivity(),"http://bloodplus.netau.net/DB_Query.php?q=getPostOfUser&blood="+blood,Toast.LENGTH_LONG).show();
            new HttpAsyncTask().execute("http://bloodplus.netau.net/DB_Query.php?q=getPostOfUser&blood="+blood);

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
                    String status=json.getString("count");
                    Log.i("JSON",status);
                    if(status.compareTo("0")!=0){
                        List<Post> dataList=new ArrayList<Post>();
                        int i=0;
                        while(i < Integer.parseInt(status)){
                            JSONObject item=json.getJSONObject(String.valueOf(i));
                            Log.i("JSON"+i,item.toString());
                            Post p=new Post(item.getString("mobile"),item.getString("heading"),item.getString("descPost"),item.getString("blood"),item.getString("postDate"));

                            dataList.add(p);

                            i++;

                        }
                        Post[] data=dataList.toArray(new Post[dataList.size()]);

                        PostAdapter pa=new PostAdapter(getActivity(),R.layout.post_item_row,data);
                        ListView lvPost=(ListView)getActivity().findViewById(R.id.lvPostForMe
                        );
                        lvPost.setAdapter(pa);
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
