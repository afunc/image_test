package org.afunc.image_test;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));
    }
}
