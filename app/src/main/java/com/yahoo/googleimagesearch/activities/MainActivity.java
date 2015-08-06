package com.yahoo.googleimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.yahoo.googleimagesearch.fragments.SettingsFragment;
import com.yahoo.googleimagesearch.helpers.EndlessScrollListener;
import com.yahoo.googleimagesearch.models.ImageResult;
import com.yahoo.googleimagesearch.models.ImageSearchSettings;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private String strQuery;
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultAdapter aImageResults;
    private ImageSearchSettings searchSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();

        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);

        searchSettings = new ImageSearchSettings();
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
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchData((page - 1) * 8);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                imageResults.clear();

                strQuery = query;
                fetchData(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

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

    /*
    public void onImageSearch(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        imageResults.clear();
        strQuery = etQuery.getText().toString();
        fetchData(0);
    }
    */

    private void fetchData(int offset) {
        AsyncHttpClient client = new AsyncHttpClient();
        String baseUrl = getString(R.string.baseurl);
        String searchUrl = baseUrl + "&q=" + strQuery + "&rsz=8&start=" + offset;
        String setting;
        if(searchSettings.getColor() > 0) {
            setting = getResources().getStringArray(R.array.image_colors)[searchSettings.getColor()].toLowerCase();
            searchUrl = searchUrl + "&imgcolor=" + setting;
        }
        if(searchSettings.getSize() > 0) {
            setting = getResources().getStringArray(R.array.image_sizes)[searchSettings.getSize()].toLowerCase();
            searchUrl = searchUrl + "&imgsz=" + setting;
        }
        if(searchSettings.getType() > 0) {
            setting = getResources().getStringArray(R.array.image_types)[searchSettings.getType()].toLowerCase();
            searchUrl = searchUrl + "&imgtype=" + setting;
        }
        if(searchSettings.getSite().length() > 0) {
            searchUrl = searchUrl + "&as_sitesearch=" + searchSettings.getSite();
        }

        while (this.isNetworkAvailable()) {
            client.get(searchUrl, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray imageResultJson = null;
                    try {
                        imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
                        // imageResults.clear();
                        aImageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            break;
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void showSettings(MenuItem item) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        SettingsFragment settingsDialog = SettingsFragment.newInstance(searchSettings);
        settingsDialog.show(fm, "fragment_settings");
    }
}
