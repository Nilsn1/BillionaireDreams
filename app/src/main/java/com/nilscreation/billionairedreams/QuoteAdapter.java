package com.nilscreation.billionairedreams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.interstitial.InterstitialAd;
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.nilscreation.billionairedreams.model.QuoteModel;

import java.util.ArrayList;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.viewholder> {
    Context context;
    List<QuoteModel> quotelist;
    int mCounter = 0;
    private InterstitialAd mInterstitialAd;

    public QuoteAdapter(Context context, List<QuoteModel> quotelist) {
        this.context = context;
        this.quotelist = quotelist;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        QuoteModel quote = quotelist.get(position);
        Glide.with(context).load(quote.getUrl()).placeholder(R.drawable.progress_bar).into(holder.imageView);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("url", quote.getUrl());
//                bundle.putString("title", quote.getTitle());
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

                mCounter++;
//                Toast.makeText(context, "" + mCounter, Toast.LENGTH_SHORT).show();

                if (mCounter == 3) {
                    mCounter = 0;
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show((Activity) context);

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                mInterstitialAd = null;
                            }
                        });

                    } else {

                    }
                } else if (mCounter == 2) {
                    loadInterstitialAd();
                } else {
//                    Dont show ad
                }
            }
        });
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, "ca-app-pub-9137303962163689/1983944823", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
//                Toast.makeText(context, "loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return quotelist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ConstraintLayout constraintLayout;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            constraintLayout = itemView.findViewById(R.id.main_layout);
        }
    }
}
