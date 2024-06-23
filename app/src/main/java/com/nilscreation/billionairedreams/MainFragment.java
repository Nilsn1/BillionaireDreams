package com.nilscreation.billionairedreams;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.nilscreation.billionairedreams.model.APIConfig;
import com.nilscreation.billionairedreams.model.BloggerModel;
import com.nilscreation.billionairedreams.model.QuoteModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {

    RecyclerView recyclerView;
    List<QuoteModel> quotelist;
    private AdView mAdView;

    ProgressBar progressbar;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        progressbar = view.findViewById(R.id.progressbar);
        progressbar.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.yellow)));

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        quotelist = new ArrayList<>();

        loadData();

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }

    private void loadData() {

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BloggerApiService apiService = retrofit.create(BloggerApiService.class);
        Call<BloggerModel> call = apiService.getBlogPosts();
        call.enqueue(new Callback<BloggerModel>() {
            @Override
            public void onResponse(Call<BloggerModel> call, Response<BloggerModel> response) {

                BloggerModel mylist = response.body();

                List<QuoteModel> imagelist = extractmyList(mylist.getContent());
                QuoteAdapter adapter = new QuoteAdapter(getContext(), imagelist);
                progressbar.setVisibility(View.GONE);

//                               AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with("ca-app-pub-3940256099942544/2247696110", adapter, "medium").adItemInterval(4).build();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<BloggerModel> call, Throwable t) {

            }
        });

    }

    public static List<QuoteModel> extractmyList(String htmlContent) {
//        List<String> myList = new ArrayList<>();
        List<QuoteModel> myList = new ArrayList<>();
        Document doc = Jsoup.parse(htmlContent);

        Elements imgElements = doc.select("img[src]"); // Select all <img> tags with src attribute
        for (Element imgElement : imgElements) {
            String imageUrl = imgElement.attr("src");
            String title = imgElement.attr("alt");

            QuoteModel quoteModel = new QuoteModel(imageUrl, title);
//            myList.add(imageUrl);
            myList.add(quoteModel);
        }

//        Elements blockquoteElements = doc.select("h4");
//        for (Element blockquoteElement : blockquoteElements) {
//            String blockquoteText = blockquoteElement.outerHtml();
//            myList.add(blockquoteText);
//        }

        return myList;
    }
}