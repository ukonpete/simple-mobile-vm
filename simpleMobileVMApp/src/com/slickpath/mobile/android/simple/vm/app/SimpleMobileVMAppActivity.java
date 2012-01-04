package com.slickpath.mobile.android.simple.vm.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.slickpath.mobile.android.simple.vm.*;
import com.slickpath.mobile.android.simple.vm.app.R;
import com.slickpath.mobile.android.simple.vm.app.R.id;
import com.slickpath.mobile.android.simple.vm.app.R.layout;

public class SimpleMobileVMAppActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        File filesDir = this.getApplicationContext().getFilesDir();
        
        TextView textView = (TextView)findViewById(R.id.textViewPath);
        textView.setText(filesDir.getPath());
        
        String[] sFiles = this.getApplicationContext().fileList();
        
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerFiles);
        {
        	
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sFiles);
        	spinner.setAdapter(adapter);
        	/*
        	ArrayAdapter adapter = new ArrayAdapter(this,
        			android.R.layout.spinnerFiles, spinner);
        			s.setAdapter(adapter);
        			*/
        }
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				// TODO Auto-generated method stub
				parseFile(position);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        });
    }
    
    public void parseFile(int file)
    {
    	String[] sFiles = this.getApplicationContext().fileList();
    	
        TextView textView = (TextView)findViewById(R.id.textViewFile);
        textView.setText(sFiles[file]);	
        
        VirtualMachine vm = new VirtualMachine(this.getApplicationContext());
        
        File filesDir = this.getApplicationContext().getFilesDir();
    
        SimpleParser parser = new SimpleParser(filesDir.getPath() + File.separator + sFiles[file]);
        
        List<List<Integer>> allParameters = new ArrayList<List<Integer>>();
        List<Integer> allInstructions = new ArrayList<Integer>();
        try {
        	allInstructions = parser.parse(allParameters);
		} catch (VMError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			vm.addInstructions(allInstructions, allParameters);
		} catch (VMError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			vm.runInstructions();
		} catch (VMError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}