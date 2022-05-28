package com.hurhco.tvsafari.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.hurhco.tvsafari.R;
import com.hurhco.tvsafari.helper.RemoteConfig;
import com.hurhco.tvsafari.models.MainModel;
import com.hurhco.tvsafari.ui.MainActivity;
import com.hurhco.tvsafari.ui.SubActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private List<MainModel> list;
    private InterstitialAd mInterstitialAd;
    private String TAG = "AdLogs";

    public MainAdapter(Context context, List<MainModel> list) {
        this.context = context;
        this.list = list;

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(RemoteConfig.getInstance().mainInterUnitId());
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mainImage;
        public TextView mainText;
        private MainModel item;

        public void bind(MainModel item) {
            this.item = item;
            mainText.setText(String.valueOf(item.getName()));

            if (item.getPic() != null && !item.getPic().isEmpty()) {
                Picasso.get()
                        .load(item.getPic())
                        .into(mainImage);
            } else {
                Picasso.get()
                        .load(R.drawable.plcae_holder_sq)
                        .into(mainImage);
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            mainImage = itemView.findViewById(R.id.main_img);
            mainText = itemView.findViewById(R.id.main_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.showDialog();
                    interstitialAdMain(item);
                }
            });
        }
    }

    private void interstitialAdMain(final MainModel item) {
        if (RemoteConfig.getInstance().mainMobRule()) {
            setAdCallBacks(item);
            if (mInterstitialAd.isLoaded()) {
                MainActivity.dismissDialog();
                mInterstitialAd.show();
            } else {
                MainActivity.dismissDialog();
                goToSubCategory(item);
            }
        } else {
            MainActivity.dismissDialog();
            goToSubCategory(item);
        }
    }

    private void goToSubCategory(MainModel item) {
        Intent intent = new Intent(context, SubActivity.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("id", item.getId());
        context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setAdCallBacks(final MainModel item) {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.i(TAG, "Main on Ad Left Application");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.i(TAG, "Main on Ad Opened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //MainActivity.dismissDialog();
                Log.i(TAG, "Main on Ad Loaded");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.i(TAG, "Main on Ad Clicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.i(TAG, "Main on Ad Impression");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                //MainActivity.dismissDialog();
                //goToSubCategory(item);
                Log.i(TAG, "Main on Ad Failed To Load");
            }

            @Override
            public void onAdClosed() {
                Log.i(TAG, "Main on Ad Closed");
                goToSubCategory(item);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
}