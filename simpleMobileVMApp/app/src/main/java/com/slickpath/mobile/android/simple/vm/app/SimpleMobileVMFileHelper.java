package com.slickpath.mobile.android.simple.vm.app;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.FileHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class SimpleMobileVMFileHelper implements FileHelper{

    private static String LOG_TAG = SimpleMobileVMFileHelper.class.getSimpleName();

    private String instructions;
    private Context context;
    private String instructionsFile;
    private String path;

    SimpleMobileVMFileHelper(@NonNull Context context, @NonNull String path, @NonNull String instructionsFile) throws IOException {
        this.context = context;
        this.instructionsFile = instructionsFile;
        this.path = path;
        readInstructionsString();
    }

    @Override
    public String getInstructionsString() {
        return instructions;
    }

    private BufferedReader getBufferedReader() throws IOException {
        String assetFilePlusPath = path + File.separator + instructionsFile;
        Log.d(LOG_TAG, "attempting getting file from: " + assetFilePlusPath);
        InputStreamReader is  = new InputStreamReader(context.getAssets().open(assetFilePlusPath));
        return new BufferedReader(is, 8192);
    }

    private void readInstructionsString() throws IOException {
        Log.d(LOG_TAG, "reading instruction string");
        final BufferedReader buffReader = getBufferedReader();

        StringBuilder stringBuilder = new StringBuilder();

        int lineCount = 0;
        String line = buffReader.readLine();
        if(line != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
            lineCount++;
            while (line != null) {
                line = buffReader.readLine();
                if (line != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                    lineCount++;
                }
            }
        }
        Log.d(LOG_TAG, "Lines read: " + lineCount);
        instructions = stringBuilder.toString();
    }
}
