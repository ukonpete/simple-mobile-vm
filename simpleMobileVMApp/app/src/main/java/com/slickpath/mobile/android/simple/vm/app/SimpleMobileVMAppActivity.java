package com.slickpath.mobile.android.simple.vm.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

public class SimpleMobileVMAppActivity extends Activity implements IVMListener, IParserListener{

	private static final Object object = new Object();
	private ProgressDialog _dialog;
	private VirtualMachine _vm;
	private StringBuilder stringBuilder = new StringBuilder();

	@Override
	public void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final TextView textView = (TextView)findViewById(R.id.textViewPath);
		textView.setText(getInstructionPath());

		final Spinner spinner = (Spinner) findViewById(R.id.spinnerFiles);
		final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getInstructionFiles());
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
				final EditText editTextOutput = (EditText)findViewById(R.id.editTextOutput);
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
				});
				_vm.setVMListener(SimpleMobileVMAppActivity.this);
				parseFile((int) spinner.getSelectedItemId());
			}
		});

	}

	/**
	 * @param index index into file list
	 */
	private void parseFile(final int index)
	{
		final String sSelectedFile = getSelectedFileName(index);

		try {
			SimpleMobileVMFileHelper simpleMobileVMFileHelper = new SimpleMobileVMFileHelper(getApplicationContext(), getInstructionPath(), sSelectedFile);
			final SimpleParser parser = new SimpleParser(simpleMobileVMFileHelper, this);

			_dialog = ProgressDialog.show(SimpleMobileVMAppActivity.this, "",
					"Please wait for few seconds...", true);

			parser.parse();
		} catch (IOException e) {
			Toast.makeText(this, "Unable to find file " + sSelectedFile, Toast.LENGTH_LONG).show();
		}
	}

	private String getInstructionPath() {
		return getString(R.string.instructions_path);
	}

	/**
	 * @return list of file names from system
	 */
	private String[] getInstructionFiles()  {
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
		String sSelectedFile = "N/A";
		String[] files = getInstructionFiles();
		if(files != null && index < files.length){
			sSelectedFile = files[index];
			final TextView textView = (TextView)findViewById(R.id.textViewFile);
			textView.setText(files[index]);
		}
		return sSelectedFile;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.parser.ParserListener#completedParse(com.slickpath.mobile.android.simple.vm.VMError, com.slickpath.mobile.android.simple.vm.util.CommandList)
	 */
	@Override
	public void completedParse(final VMError vmError, final CommandList commands)
	{
		if ( vmError != null)
		{
			Toast.makeText(this, "ERROR PARSE" + vmError.getMessage(), Toast.LENGTH_LONG).show();
			vmError.printStackTrace();
		}
		_vm.addCommands(commands);
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.VMListener#completedAddingInstructions(com.slickpath.mobile.android.simple.vm.VMError)
	 */
	@Override
	public void completedAddingInstructions(final VMError vmError) {
		if ( vmError != null)
		{
			Toast.makeText(this, "ERROR ADD INST" + vmError.getMessage(), Toast.LENGTH_LONG).show();
			vmError.printStackTrace();
		}
		_vm.runInstructions();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.VMListener#completedRunningInstructions(boolean, int, com.slickpath.mobile.android.simple.vm.VMError)
	 */
	@Override
	public void completedRunningInstructions(final boolean bHalt, final int lineExecuted, final VMError vmError) {
		_dialog.dismiss();
		if ( vmError != null)
		{
			Toast.makeText(this, "ERROR RUN INST lastLine=" + lineExecuted, Toast.LENGTH_LONG).show();
			vmError.printStackTrace();
		} else {
			final EditText editTextOutput = (EditText)findViewById(R.id.editTextOutput);
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