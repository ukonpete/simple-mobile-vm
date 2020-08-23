package com.slickpath.mobile.android.simple.vm.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slickpath.mobile.android.simple.vm.IVMListener;
import com.slickpath.mobile.android.simple.vm.OutputListener;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine;
import com.slickpath.mobile.android.simple.vm.parser.IParserListener;
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

import java.io.IOException;

@SuppressWarnings("FieldCanBeLocal")
public class SimpleMobileVMAppActivity extends Activity implements IVMListener, IParserListener {

    @NonNull
    private final StringBuilder stringBuilder = new StringBuilder();
    // TODO ProgressDialog has been deprecated
    private ProgressDialog progressDialog;
    private VirtualMachine _vm;

    private TextView textViewPath;
    private Spinner spinnerFiles;
    private EditText editTextOutput;
    private TextView textViewFile;
    private Button buttonExe;

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textViewPath = findViewById(R.id.textViewPath);
        spinnerFiles = findViewById(R.id.spinnerFiles);
        editTextOutput = findViewById(R.id.editTextOutput);
        textViewFile = findViewById(R.id.textViewFile);
        buttonExe = findViewById(R.id.buttonExe);

        textViewPath.setText(getInstructionPath());

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getInstructionFiles());
        spinnerFiles.setAdapter(adapter);

        buttonExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextOutput.setText("");
                _vm = new VirtualMachine(SimpleMobileVMAppActivity.this.getApplicationContext(), new OutputListener() {
                    @Override
                    public void charOutput(char c) {
                        stringBuilder.append(c);
                    }

                    @Override
                    public void lineOutput(String line) {
                        stringBuilder.append(line);
                    }
                }, null);
                _vm.setVMListener(SimpleMobileVMAppActivity.this);
                parseFile((int) spinnerFiles.getSelectedItemId());
            }
        });

        spinnerFiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedFileName(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });
    }

    /**
     * @param index index into file list
     */
    private void parseFile(final int index) {
        final String selectedFile = getSelectedFileName(index);

        try {
            SimpleMobileVMFileHelper simpleMobileVMFileHelper = new SimpleMobileVMFileHelper(getApplicationContext(), getInstructionPath(), selectedFile);
            final SimpleParser parser = new SimpleParser(simpleMobileVMFileHelper, this);

            progressDialog = ProgressDialog.show(SimpleMobileVMAppActivity.this, "",
                    "Please wait for few seconds...", true);

            parser.parse();
        } catch (IOException e) {
            Toast.makeText(this, "Unable to find file " + selectedFile, Toast.LENGTH_LONG).show();
        }
    }

    private String getInstructionPath() {
        return getString(R.string.instructions_path);
    }

    /**
     * @return list of file names from system
     */
    private String[] getInstructionFiles() {
        try {
            return this.getAssets().list(getInstructionPath());
        } catch (IOException e) {
            Toast.makeText(this, "Unable to retrieve file list", Toast.LENGTH_LONG).show();
        }
        return new String[]{};
    }

    /**
     * @param index index of file to open
     * @return String file Name
     */
    private String getSelectedFileName(final int index) {
        String selectedFile = "N/A";
        String[] files = getInstructionFiles();
        if (files != null && index < files.length) {
            selectedFile = files[index];
            textViewFile.setText(files[index]);
        }
        return selectedFile;
    }

    @Override
    public void completedParse(@Nullable final VMError vmError, final CommandList commands) {
        if (vmError != null) {
            Toast.makeText(this, "ERROR PARSE" + vmError.getMessage(), Toast.LENGTH_LONG).show();
            vmError.printStackTrace();
        }
        _vm.addCommands(commands);
    }

    @Override
    public void completedAddingInstructions(@Nullable final VMError vmError) {
        if (vmError != null) {
            Toast.makeText(this, "ERROR ADD INST" + vmError.getMessage(), Toast.LENGTH_LONG).show();
            vmError.printStackTrace();
        }
        _vm.runInstructions();
    }

    @Override
    public void completedRunningInstructions(final boolean bHalt, final int lineExecuted, @Nullable final VMError vmError) {
        progressDialog.dismiss();
        if (vmError != null) {
            Toast.makeText(this, "ERROR RUN INST lastLine=" + lineExecuted, Toast.LENGTH_LONG).show();
            vmError.printStackTrace();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editTextOutput.setText(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
            });
        }
    }
}