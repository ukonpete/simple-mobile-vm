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

import com.slickpath.mobile.android.simple.vm.IVMListener;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine;
import com.slickpath.mobile.android.simple.vm.parser.IParserListener;
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

public class SimpleMobileVMAppActivity extends Activity implements IVMListener, IParserListener{

	private ProgressDialog _dialog;
	private VirtualMachine _vm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final File filesDir = this.getApplicationContext().getFilesDir();

		final TextView textView = (TextView)findViewById(R.id.textViewPath);
		textView.setText(filesDir.getPath());

		final String[] sFiles = this.getApplicationContext().fileList();

		final Spinner spinner = (Spinner) findViewById(R.id.spinnerFiles);

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sFiles);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(final AdapterView<?> arg0, final View arg1,
					final int arg2, final long arg3) {
				getSelectedFileName(arg2);
			}

			@Override
			public void onNothingSelected(final AdapterView<?> arg0) {
				// Do Nothing
			}});


		final Button exeButton = (Button)findViewById(R.id.buttonExe);
		exeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				_vm = new VirtualMachine(SimpleMobileVMAppActivity.this.getApplicationContext());
				_vm.setVMListener(SimpleMobileVMAppActivity.this);
				parseFile((int) spinner.getSelectedItemId());
			}
		});

	}

	/**
	 * @param file
	 */
	public void parseFile(final int file)
	{
		final String selectedFile = getSelectedFileName(file);

		final File filesDir = this.getApplicationContext().getFilesDir();

		final SimpleParser parser = new SimpleParser(filesDir.getPath() + File.separator + selectedFile, this);

		_dialog = ProgressDialog.show(SimpleMobileVMAppActivity.this, "",
				"Please wait for few seconds...", true);

		parser.parse();
	}

	/**
	 * @param file
	 * @return
	 */
	private String getSelectedFileName(final int file) {
		String selectedFile = "N/A";
		final String[] sFiles = this.getApplicationContext().fileList();
		sSelectedFile = sFiles[file];
		final TextView textView = (TextView)findViewById(R.id.textViewFile);
		textView.setText(sFiles[file]);
		return sSelectedFile;
	}

	@Override
	public void completedParse(final VMError vmError, final CommandList commands)
	{
		if ( vmError != null)
		{
			System.out.println("ERROR PARSE");
			vmError.printStackTrace();
		}
		_vm.addCommands(commands);
	}

	@Override
	public void completedAddingInstructions(final VMError vmError) {
		if ( vmError != null)
		{
			System.out.println("ERROR ADD INST");
			vmError.printStackTrace();
		}
		_vm.runInstructions();
	}

	@Override
	public void completedRunningInstructions(final boolean bHalt, final int lineExecuted, final VMError vmError) {
		if ( vmError != null)
		{
			System.out.println("ERROR RUN INST lastLine=" + lineExecuted);
			vmError.printStackTrace();
		}
		_dialog.dismiss();
	}
}