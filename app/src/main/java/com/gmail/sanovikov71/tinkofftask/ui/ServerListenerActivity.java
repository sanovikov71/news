package com.gmail.sanovikov71.tinkofftask.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.gmail.sanovikov71.tinkofftask.network.DataManager;

public abstract class ServerListenerActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (DataManager.ACTION_FETCH_OK.equals(action)) {
                    onFetchOk();
                } else if (DataManager.ACTION_FETCH_ERROR.equals(action)) {
                    onFetchError();
                }

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataManager.ACTION_FETCH_OK);
        intentFilter.addAction(DataManager.ACTION_FETCH_ERROR);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    protected abstract void onFetchOk();

    protected abstract void onFetchError();
}
