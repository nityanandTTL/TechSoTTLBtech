package com.thyrocare.btechapp.service;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;

public class GlideCacheClearAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    public GlideCacheClearAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Glide.get(context).clearDiskCache();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Glide.get(context).clearMemory();
    }
}

