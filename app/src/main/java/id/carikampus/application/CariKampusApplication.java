package id.carikampus.application;

import android.app.Application;
import android.util.Log;

import id.carikampus.repository.KampusRepository;
import id.carikampus.repository.ProdiRepository;

public class CariKampusApplication extends Application {

    private static final String TAG = "CariKampusApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, TAG + ".onCreate");
        ProdiRepository.initialize(this);
        KampusRepository.initialize(this);
    }
}