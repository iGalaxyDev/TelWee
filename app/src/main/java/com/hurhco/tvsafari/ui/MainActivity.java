package com.hurhco.tvsafari.ui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hurhco.tvsafari.adapter.MainAdapter;
import com.hurhco.tvsafari.helper.RemoteConfig;
import com.hurhco.tvsafari.models.MainModel;
import com.hurhco.tvsafari.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<MainModel> testList;
    private RecyclerView.Adapter adapter;
    private AdView adView;
    private static ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    public static boolean isTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BannerAd();

        initBackendless();

        initToolBar();

        init();

        getData();

        checkConnection();
    }

    private void init() {
        RecyclerView mainRecyclerView = findViewById(R.id.main_list);
        progressDialog = new ProgressDialog(this);
        testList = new ArrayList<>();
        adapter = new MainAdapter(getApplicationContext(), testList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mainRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);
    }

    public static void showDialog() {
        progressDialog.setCancelable(false);
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public static void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initBackendless() {
        Backendless.setUrl("https://api.backendless.com");
        Backendless.initApp(getApplicationContext(),
                RemoteConfig.getInstance().backendlessAppId(),
                RemoteConfig.getInstance().backendlessAppKey());
    }

    private void getData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog));
        progressDialog.show();

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(20).setOffset(0);
        queryBuilder.setSortBy("id AESC");

        Backendless.Data.of(RemoteConfig.getInstance().mainTableName()).find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> items) {

                if (items.isEmpty()) return;
                for (Map item : items) {
                    MainModel myServer = new MainModel("pic", "name", 0);
                    myServer.pic = ((String) item.get("pic"));
                    myServer.name = ((String) item.get("name"));
                    myServer.id = ((Integer) item.get("id"));
                    testList.add(myServer);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void checkConnection() {
        String url = "http://ip-api.com/json/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String country = response.getString("country");
                            isTrue = country.equals("Iran") || country.equals("iran") || country.equals("IRAN");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAGOLLY", "onErrorResponse: " + error);
            }
        });
        requestQueue.add(request);
    }

/*
    private void makeRequest() {
        String url = "http://ip-api.com/json/";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
        queue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String country = response.getString("country");
                    if (country.equals("Iran") || country.equals("iran") || country.equals("IRAN")) {
                        Log.i("TAGCon", "onResponse: IRAN");
                        isValid = true;
                    } else {
                        Log.i("TAGCon", "onResponse: NOT IRAN");
                        isValid = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAGVOLLY", "onErrorResponse: " + error);
            }
        }));
    }
*/

    // region Toolbar
    public void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, " ");
                String Share = getResources().getString(R.string.share_message);
                Share = Share + "https://play.google.com/store/apps/details?id=" + getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, Share);
                startActivity(Intent.createChooser(i, "choose one"));
                return true;
            case R.id.rate:
                try {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                } catch (Exception e) {
                    Toast.makeText(this, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.privacy:
                String url = getResources().getString(R.string.address_pri);
                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse(url));
                startActivity(i1);
                return true;
            default:
                return true;
        }
    }
    // endregion

    //region Banned Ad
    private void BannerAd() {
        FrameLayout adContainerView = findViewById(R.id.adView);
        adView = new AdView(this);
        adView.setAdUnitId(RemoteConfig.getInstance().mainBannerUnitId());
        adContainerView.addView(adView);
        loadBanner();
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
    //endregion

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}