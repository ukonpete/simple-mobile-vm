package com.slickpath.mobile.android.simple.vm.app

import android.app.Activity
import android.app.AppComponentFactory
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.slickpath.mobile.android.simple.vm.IVMListener
import com.slickpath.mobile.android.simple.vm.OutputListener
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.app.databinding.MainBinding
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
import com.slickpath.mobile.android.simple.vm.parser.IParserListener
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser
import com.slickpath.mobile.android.simple.vm.util.CommandList
import java.io.IOException


class SimpleMobileVMAppActivity : AppCompatActivity(), IVMListener, IParserListener {
    private val stringBuilder = StringBuilder()

    private lateinit var virtualMachine: VirtualMachine

    private val binding by viewBinding(MainBinding::inflate)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.editTextOutput.movementMethod = ScrollingMovementMethod()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, instructionFiles)
        binding.spinnerFiles.adapter = adapter
        binding.buttonExe.setOnClickListener(ExecuteButtonOnClickListener())
        binding.spinnerFiles.onItemSelectedListener = FileItemSelectedListener()
    }

    private fun onExecuteClicked() {
        binding.editTextOutput.text = ""
        binding.editTextOutput.scrollTo(0, 0)

        binding.progressBar.visibility = VISIBLE
        virtualMachine = VirtualMachine(applicationContext, SimpleVMOutputListener(), null)
        virtualMachine.vMListener = this
        parseFile(binding.spinnerFiles.selectedItemId.toInt())
    }

    private fun parseFile(fileListIndex: Int) {
        val selectedFile = getSelectedFileName(fileListIndex)
        try {
            val simpleMobileVMFileHelper = SimpleMobileVMFileHelper(applicationContext, instructionPath, selectedFile)
            val parser = SimpleParser(simpleMobileVMFileHelper, this)
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
            binding.textViewFile.text = files[index]
        }
        return selectedFile
    }

    override fun completedParse(vmError: VMError?, commands: CommandList?) {
        if (vmError != null) {
            Toast.makeText(this, "ERROR PARSE" + vmError.message, Toast.LENGTH_LONG).show()
            vmError.printStackTrace()
        }
        virtualMachine.addCommands(commands)
    }

    override fun completedAddingInstructions(vmError: VMError?) {
        if (vmError != null) {
            Toast.makeText(this, "ERROR ADD INST" + vmError.message, Toast.LENGTH_LONG).show()
            vmError.printStackTrace()
        }
        virtualMachine.runInstructions()
    }

    override fun completedRunningInstructions(bHalt: Boolean, lastLineExecuted: Int, vmError: VMError?) {
        runOnUiThread {
            binding.progressBar.visibility = GONE
        }
        if (vmError != null) {
            Toast.makeText(this, "ERROR RUN INST lastLine=$lastLineExecuted", Toast.LENGTH_LONG).show()
            vmError.printStackTrace()
        } else {
            runOnUiThread {
                binding.editTextOutput.text = stringBuilder.toString()
                stringBuilder.setLength(0)
            }
        }
    }


    inner class ExecuteButtonOnClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            onExecuteClicked()
        }
    }

    inner class SimpleVMOutputListener : OutputListener {
        override fun charOutput(c: Char) {
            stringBuilder.append(c)
        }

        override fun lineOutput(line: String) {
            stringBuilder.append(line)
        }
    }

    inner class FileItemSelectedListener : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            getSelectedFileName(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // DO NOTHING
        }
    }
}