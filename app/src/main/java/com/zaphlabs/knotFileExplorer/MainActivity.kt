package com.zaphlabs.knotFileExplorer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zaphlabs.filechooser.KnotFileChooser
import com.zaphlabs.filechooser.Sorter
import com.zaphlabs.filechooser.utils.FileType
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOpen.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1234)
            } else {
                showMaterialFileChooser()
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1234 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMaterialFileChooser()
                }
                return
            }
        }
    }

    private fun showMaterialFileChooser() {
        KnotFileChooser(this,
            allowBrowsing = true,
            allowCreateFolder = true,
            allowMultipleFiles = false,
            allowSelectFolder = false,
            minSelectedFiles = 1,
            maxSelectedFiles = 1,
            showFiles = true,
            showFoldersFirst = true,
            showFolders = true,
            showHiddenFiles = false,
            initialFolder = Environment.getExternalStorageDirectory(),
            restoreFolder = false,
            cancelable = true,
            fileType = FileType.ALL)
            .title("Select a File")
            .sorter(Sorter.ByNewestModification)
            .onSelectedFilesListener {
                //tvResult.text = Uri.fromFile(it[0]).toString()
            }
            .onSelectedFileUriListener {
                tvResult.text = it.toString()
            }
            .show()
    }
}