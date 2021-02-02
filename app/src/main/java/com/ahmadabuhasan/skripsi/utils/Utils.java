package com.ahmadabuhasan.skripsi.utils;

import android.content.Context;

import com.ahmadabuhasan.skripsi.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/*
 * Created by Ahmad Abu Hasan on 02/02/2021
 */

public class Utils {

    public void interstitialAdsShow(Context context) {
        final InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.AdMob_Interstitial_Ads_ID));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }
}