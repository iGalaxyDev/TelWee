package com.hurhco.tvsafari.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hurhco.tvsafari.R;
import com.hurhco.tvsafari.adapter.SubAdapter;
import com.hurhco.tvsafari.helper.RemoteConfig;
import com.hurhco.tvsafari.models.SubModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubActivity extends AppCompatActivity {

    private TextView nameapp;
    private int id;
    private String name;
    private RecyclerView subRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<SubModel> subModels;
    private RecyclerView.Adapter adapter;
    private DividerItemDecoration dividerItemDecoration;
    private FrameLayout adContainerView;
    private AdView adView;
    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        bannerAd();

        getData();

        init();

        if (MainActivity.isTrue) {
            getChannelData();
        } else {
            getInfoData();
        }
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        name = bundle.getString("name");
    }


    private void init() {
        nameapp = findViewById(R.id.subappname);
        nameapp.setText(name);
        subRecyclerView = findViewById(R.id.main_list);
        subModels = new ArrayList<>();
        adapter = new SubAdapter(getApplicationContext(), subModels);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(subRecyclerView.getContext(), linearLayoutManager.getOrientation());
        subRecyclerView.setHasFixedSize(true);
        subRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        subRecyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);

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

    private void getChannelData() {
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog));
        progressDialog.show();

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100).setOffset(0);
        queryBuilder.setSortBy("sort AESC");

        Backendless.Data.of(RemoteConfig.getInstance().subTableName()).find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> items) {

                if (items.isEmpty()) return;

                for (Map item : items) {
                    SubModel myServer = new SubModel(0,
                            "link",
                            "name",
                            0,
                            "pic",
                            "des",
                            "country",
                            "topic",
                            "frequency",
                            "referer");

                    myServer.link = ((String) item.get("link"));
                    myServer.channelName = ((String) item.get("name"));
                    myServer.sortId = ((Integer) item.get("sort"));
                    myServer.pic = ((String) item.get("pic"));
                    myServer.referer = ((String) item.get("referer"));

                    if (id == (myServer.channelId = ((Integer) item.get("id")))) {
                        subModels.add(myServer);
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(SubActivity.this, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getInfoData() {
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog));
        progressDialog.show();

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100).setOffset(0);
        queryBuilder.setSortBy("sort AESC");

        Backendless.Data.of(RemoteConfig.getInstance().subInfoTableName()).find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> items) {

                if (items.isEmpty()) return;

                for (Map item : items) {
                    SubModel myServer = new SubModel(0,
                            "link",
                            "name",
                            0,
                            "pic",
                            "des",
                            "country",
                            "topic",
                            "frequency",
                            "referer");

                    myServer.link = ((String) item.get("link"));
                    myServer.channelName = ((String) item.get("name"));
                    myServer.sortId = ((Integer) item.get("sort"));
                    myServer.pic = ((String) item.get("pic"));
                    myServer.des = ((String) item.get("des"));
                    myServer.country = ((String) item.get("country"));
                    myServer.topic = ((String) item.get("topic"));
                    myServer.frequency = (String) item.get("frequency");
                    if (id == (myServer.channelId = ((Integer) item.get("id")))) {
                        subModels.add(myServer);
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(SubActivity.this, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    //region Banner Ad
    private void bannerAd() {
        adContainerView = findViewById(R.id.adView);
        adView = new AdView(this);
        adView.setAdUnitId(RemoteConfig.getInstance().subBannerUnitId());
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

}
