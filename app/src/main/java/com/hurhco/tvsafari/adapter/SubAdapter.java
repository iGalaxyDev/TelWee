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
import com.hurhco.tvsafari.helper.RemoteConfig;
import com.hurhco.tvsafari.ui.PlayerActivity;
import com.hurhco.tvsafari.R;
import com.hurhco.tvsafari.models.SubModel;
import com.hurhco.tvsafari.ui.InfoActivity;
import com.hurhco.tvsafari.ui.MainActivity;
import com.hurhco.tvsafari.ui.SubActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    private Context context;
    private List<SubModel> list;
    private InterstitialAd mInterstitialAd;
    private String TAG = "AdLogs";

    public SubAdapter(Context context, List<SubModel> list) {
        this.context = context;
        this.list = list;

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(RemoteConfig.getInstance().subInterUnitId());
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_sub, parent, false);
        return new ViewHolder(v);
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
        public ImageView subImage;
        public TextView subText;
        private SubModel item;

        public void bind (SubModel item){
            this.item = item;
            subText.setText(String.valueOf(item.getChannelName()));
            if (item.getPic() != null && !item.getPic().isEmpty()) {
                Picasso.get()
                        .load(item.getPic())
                        .into(subImage);
            } else {
                Picasso.get()
                        .load(R.drawable.plcae_holder_sq)
                        .into(subImage);
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            subImage = itemView.findViewById(R.id.sub_img);
            subText = itemView.findViewById(R.id.sub_txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubActivity.showDialog();
                    interstitialAdSub(item);
                }
            });
        }
    }

    private void interstitialAdSub(final SubModel item) {
        if (RemoteConfig.getInstance().subMobRule()) {
            setAdCallBacks(item);
            if (mInterstitialAd.isLoaded()) {
                SubActivity.dismissDialog();
                mInterstitialAd.show();
            } else {
                SubActivity.dismissDialog();
                switchToPlayer(item);
            }
        } else {
            SubActivity.dismissDialog();
            switchToPlayer(item);
        }
    }

    private void switchToPlayer(SubModel item) {
        if (MainActivity.isTrue) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("link", item.getLink());
            intent.putExtra("name", item.getChannelName());
            context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            Intent intent = new Intent(context, InfoActivity.class);
            intent.putExtra("pic", item.getPic());
            intent.putExtra("name", item.getChannelName());
            intent.putExtra("country", item.getCountry());
            intent.putExtra("topic", item.getTopic());
            intent.putExtra("frequency", item.getFrequency());
            intent.putExtra("link", item.getLink());
            intent.putExtra("des", item.getDes());
            context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void setAdCallBacks(final SubModel item) {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.i(TAG, "Sub on Ad Left Application");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.i(TAG, "Sub on Ad Opened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //SubActivity.dismissDialog();
                Log.i(TAG, "Sub on Ad Loaded");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.i(TAG, "Sub on Ad Clicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.i(TAG, "Sub on Ad Impression");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                //SubActivity.dismissDialog();
                //switchToPlayer(item);
                Log.i(TAG, "Sub on Ad Failed To Load");
            }

            @Override
            public void onAdClosed() {
                Log.i(TAG, "Sub on Ad Closed");
                switchToPlayer(item);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

}
