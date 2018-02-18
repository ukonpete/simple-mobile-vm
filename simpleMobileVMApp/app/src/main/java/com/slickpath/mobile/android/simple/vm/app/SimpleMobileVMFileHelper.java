package com.slickpath.mobile.android.simple.vm.app;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.FileHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class SimpleMobileVMFileHelper implements FileHelper {

    private static final String LOG_TAG = SimpleMobileVMFileHelper.class.getSimpleName();

    @NonNull
    private final Context context;
    @NonNull
    private final String instructionsFile;
    @NonNull
    private final String path;
    @Nullable
    private String instructions;

    SimpleMobileVMFileHelper(@NonNull Context context, @NonNull String path, @NonNull String instructionsFile) throws IOException {
        this.context = context;
        this.instructionsFile = instructionsFile;
        this.path = path;
        readInstructionsString();
    }

    @Nullable
    @Override
    public String getInstructionsString() {
        return instructions;
    }

    @NonNull
    private BufferedReader getBufferedReader() throws IOException {
        String assetFilePlusPath = path + File.separator + instructionsFile;
        Log.d(LOG_TAG, "attempting getting file from: " + assetFilePlusPath);
        InputStreamReader is = new InputStreamReader(context.getAssets().open(assetFilePlusPath));
        return new BufferedReader(is, 8192);
    }

    private void readInstructionsString() throws IOException {
        Log.d(LOG_TAG, "reading instruction string");
        final BufferedReader buffReader = getBufferedReader();

        StringBuilder stringBuilder = new StringBuilder();

        int lineCount = 0;
        String line = buffReader.readLine();
        if (line != null) {
            stringBuilder
                    .append(line)
                    .append("\n");
            lineCount++;
            while (line != null) {
                line = buffReader.readLine();
                if (line != null) {
                    stringBuilder
                            .append(line)
                            .append("\n");
                    lineCount++;
                }
            }
        }
        Log.d(LOG_TAG, "Lines read: " + lineCount);
        instructions = stringBuilder.toString();
    }
}
