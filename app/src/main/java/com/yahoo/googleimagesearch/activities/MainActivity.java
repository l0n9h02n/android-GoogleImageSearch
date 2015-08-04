package com.yahoo.googleimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yahoo.googleimagesearch.R;
import com.yahoo.googleimagesearch.adapters.ImageResultAdapter;
import com.yahoo.googleimagesearch.models.ImageResult;
import com.yahoo.googleimagesearch.activities.ImageDisplayActivity;

import java.util.ArrayList;




public class MainActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();

        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("result", result);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(View v) {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("eric", response.toString());
                JSONArray imageResultJson = null;
                try {
                    imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
