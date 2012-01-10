package com.slickpath.mobile.android.simple.vm.app;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.slickpath.mobile.android.simple.vm.parser.SimpleParser;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.VirtualMachine;
import com.slickpath.mobile.android.simple.vm.VMListener;
import com.slickpath.mobile.android.simple.vm.parser.ParserListener;
import com.slickpath.mobile.android.simple.vm.util.CommandSet; 

public class SimpleMobileVMAppActivity extends Activity implements VMListener, ParserListener{

	ProgressDialog _dialog;
	VirtualMachine _vm;
	
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
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sFiles);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				getSelectedFileName(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
        
        
        Button exeButton = (Button)findViewById(R.id.buttonExe);
        exeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        _vm = new VirtualMachine(SimpleMobileVMAppActivity.this.getApplicationContext());
		        _vm.setVMListener(SimpleMobileVMAppActivity.this);
				parseFile((int) spinner.getSelectedItemId());
			}
        });
        
    }
    
    public void parseFile(int file)
    {
    	String sSelectedFile = getSelectedFileName(file);
        
        File filesDir = this.getApplicationContext().getFilesDir();
    
        SimpleParser parser = new SimpleParser(filesDir.getPath() + File.separator + sSelectedFile);
        parser.setListener(this);
        
        _dialog = ProgressDialog.show(SimpleMobileVMAppActivity.this, "",
        		"Please wait for few seconds...", true);

        parser.parse();
    }

	/**
	 * @param file
	 * @return
	 */
	private String getSelectedFileName(int file) {
		String sSelectedFile = "N/A";
    	String[] sFiles = this.getApplicationContext().fileList();
    	sSelectedFile = sFiles[file];
        TextView textView = (TextView)findViewById(R.id.textViewFile);
        textView.setText(sFiles[file]);
		return sSelectedFile;
	}

	@Override
	public void completedParse(VMError vmError, CommandSet commandSet)
	{
		if ( vmError != null)
		{
			System.out.println("ERROR PARSE");
			vmError.printStackTrace();
		}
        _vm.addInstructions(commandSet);
	}

	@Override
	public void completedAddingInstructions(VMError vmError) {
		if ( vmError != null)
		{
			System.out.println("ERROR ADD INST");
			vmError.printStackTrace();
		}
		_vm.runInstructions();
	}

	@Override
	public void completedRunningInstructions(VMError vmError) {
		if ( vmError != null)
		{
			System.out.println("ERROR RUN INST");
			vmError.printStackTrace();
		}
		_dialog.dismiss();
	}
}