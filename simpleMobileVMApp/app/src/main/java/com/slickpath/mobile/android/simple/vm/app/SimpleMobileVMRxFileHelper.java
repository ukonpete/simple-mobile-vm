package com.slickpath.mobile.android.simple.vm.app;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.FileHelperRx;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

class SimpleMobileVMRxFileHelper implements FileHelperRx {

    private static final String LOG_TAG = SimpleMobileVMRxFileHelper.class.getSimpleName();

    @NonNull
    private final Context context;
    @NonNull
    private final String instructionsFile;
    @NonNull
    private final String path;
    @Nullable
    private Observable<String> observable;

    SimpleMobileVMRxFileHelper(@NonNull Context context, @NonNull String path, @NonNull String instructionsFile) throws IOException {
        this.context = context;
        this.instructionsFile = instructionsFile;
        this.path = path;
        readInstructionsString();
    }

    @Nullable
    @Override
    public Observable<String> getInstructions() {
        return observable;
    }

    @NonNull
    private BufferedReader getBufferedReader() throws IOException {
        String assetFilePlusPath = path + File.separator + instructionsFile;
        Log.d(LOG_TAG, "attempting getting file from: " + assetFilePlusPath);
        InputStreamReader is = new InputStreamReader(context.getAssets().open(assetFilePlusPath));
        return new BufferedReader(is, 8192);
    }

    private void readInstructionsString() throws IOException {
        observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(LOG_TAG, "reading instruction string");
                final BufferedReader buffReader = getBufferedReader();

                int lineCount = 0;
                String line = buffReader.readLine();
                if (line != null) {
                    e.onNext(line);
                    lineCount++;
                    while (line != null) {
                        line = buffReader.readLine();
                        if (line != null) {
                            e.onNext(line);
                            lineCount++;
                        }
                    }
                    e.onComplete();
                }
                Log.d(LOG_TAG, "Lines read: " + lineCount);
            }
        });
    }
}
