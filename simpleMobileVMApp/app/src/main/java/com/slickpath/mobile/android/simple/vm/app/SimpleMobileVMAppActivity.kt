package com.slickpath.mobile.android.simple.vm.app

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.slickpath.mobile.android.simple.vm.IVMListener
import com.slickpath.mobile.android.simple.vm.OutputListener
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
import com.slickpath.mobile.android.simple.vm.parser.IParserListener
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser
import com.slickpath.mobile.android.simple.vm.util.CommandList
import java.io.IOException

class SimpleMobileVMAppActivity : Activity(), IVMListener, IParserListener {
    private val stringBuilder = StringBuilder()

    // TODO ProgressDialog has been deprecated
    private var progressDialog: ProgressDialog? = null

    private lateinit var _vm: VirtualMachine
    private lateinit var textViewPath: TextView
    private lateinit var spinnerFiles: Spinner
    private lateinit var editTextOutput: EditText
    private lateinit var textViewFile: TextView
    private lateinit var buttonExe: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        textViewPath = findViewById(R.id.textViewPath)
        spinnerFiles = findViewById(R.id.spinnerFiles)
        editTextOutput = findViewById(R.id.editTextOutput)
        textViewFile = findViewById(R.id.textViewFile)
        buttonExe = findViewById(R.id.buttonExe)
        textViewPath.text = instructionPath
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, instructionFiles)
        spinnerFiles.adapter = adapter
        buttonExe.setOnClickListener {
            editTextOutput.setText("")
            _vm = VirtualMachine(this@SimpleMobileVMAppActivity.applicationContext, object : OutputListener {
                override fun charOutput(c: Char) {
                    stringBuilder.append(c)
                }

                override fun lineOutput(line: String) {
                    stringBuilder.append(line)
                }
            }, null)
            _vm.setVMListener(this@SimpleMobileVMAppActivity)
            parseFile(spinnerFiles.selectedItemId.toInt())
        }
        spinnerFiles.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                getSelectedFileName(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // DO NOTHING
            }
        }
    }

    /**
     * @param index index into file list
     */
    private fun parseFile(index: Int) {
        val selectedFile = getSelectedFileName(index)
        try {
            val simpleMobileVMFileHelper = SimpleMobileVMFileHelper(applicationContext, instructionPath, selectedFile)
            val parser = SimpleParser(simpleMobileVMFileHelper, this)
            progressDialog = ProgressDialog.show(this@SimpleMobileVMAppActivity, "",
                    "Please wait for few seconds...", true)
            parser.parse()
        } catch (e: IOException) {
            Toast.makeText(this, "Unable to find file $selectedFile", Toast.LENGTH_LONG).show()
        }
    }

    private val instructionPath: String
        get() = getString(R.string.instructions_path)

    private val instructionFiles: Array<String>
        get() {
            var result: Array<String>? = null
            try {
                result = assets.list(instructionPath)
            } catch (e: IOException) {
                Toast.makeText(this, "Unable to retrieve file list", Toast.LENGTH_LONG).show()
            }
            if (result == null) {
                return arrayOf()
            }
            return result
        }

    /**
     * @param index index of file to open
     * @return String file Name
     */
    private fun getSelectedFileName(index: Int): String {
        var selectedFile = "N/A"
        val files = instructionFiles
        if (index < files.size) {
            selectedFile = files[index]
            textViewFile.text = files[index]
        }
        return selectedFile
    }

    override fun completedParse(vmError: VMError?, commands: CommandList) {
        if (vmError != null) {
            Toast.makeText(this, "ERROR PARSE" + vmError.message, Toast.LENGTH_LONG).show()
            vmError.printStackTrace()
        }
        _vm.addCommands(commands)
    }

    override fun completedAddingInstructions(vmError: VMError?) {
        if (vmError != null) {
            Toast.makeText(this, "ERROR ADD INST" + vmError.message, Toast.LENGTH_LONG).show()
            vmError.printStackTrace()
        }
        _vm.runInstructions()
    }

    override fun completedRunningInstructions(bHalt: Boolean, lineExecuted: Int, vmError: VMError?) {
        progressDialog!!.dismiss()
        if (vmError != null) {
            Toast.makeText(this, "ERROR RUN INST lastLine=$lineExecuted", Toast.LENGTH_LONG).show()
            vmError.printStackTrace()
        } else {
            runOnUiThread {
                editTextOutput.setText(stringBuilder.toString())
                stringBuilder.setLength(0)
            }
        }
    }
}