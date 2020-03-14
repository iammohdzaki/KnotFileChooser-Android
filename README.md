[![](https://jitpack.io/v/iammohdzaki/KnotFileChooser-Android.svg)](https://jitpack.io/#iammohdzaki/KnotFileChooser-Android)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Knot%20File%20Chooser-green.svg?style=flat )]( https://android-arsenal.com/details/1/8056 )

# KnotFileChooser-Android

<img src="https://github.com/iammohdzaki/KnotFileChooser-Android/blob/master/Screenshot_1565945793.png" alt="KnotDialog" width="250" height="450"/>

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.iammohdzaki:KnotFileChooser-Android:1.0.3'
	}
```
How To Use
```
KnotFileChooser(this,
            allowBrowsing = true, // Allow User Browsing 
            allowCreateFolder = true, // Allow User to create Folder
            allowMultipleFiles = false, // Allow User to Select Multiple Files
            allowSelectFolder = false, // Allow User to Select Folder
            minSelectedFiles = 0, // Allow User to Selec Minimum Files Selected
            maxSelectedFiles = 0, // Allow User to Selec Minimum Files Selected
            showFiles = true, // Show Files or Show Folder Only
            showFoldersFirst = true, // Show Folders First or Only Files
            showFolders = true, //Show Folders
            showHiddenFiles = false, // Show System Hidden Files
            initialFolder = Environment.getExternalStorageDirectory(), //Initial Folder 
            restoreFolder = false, //Restore Folder After Adding
            cancelable = true) //Dismiss Dialog On Cancel (Optional)
            .title("Select a File") // Title of Dialog
            .sorter(Sorter.ByNewestModification) // Sort Data (Optional)
	    fileType = KnotFileChooser.FileType.ALL //Select Which Files you want to show (By Default : ALL)
            .onSelectedFilesListener { // Callback Returns Selected File Object  (Optional)
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
	    .onSelectedFileUriListener { // Callback Returns Uri of File (Optional)
               
            }
            .show()
```
File Types
```
enum class FileType{
        ALL,
        IMAGE,
        DB,
        DOC,
        PDF,
        MUSIC,
        VIDEO,
        CODE
    }
```
Additional Feature
```
//If you want user to select a path to save a file then
	 minSelectedFiles = 0, // Allow User to Selec Minimum Files Selected
         maxSelectedFiles = 0, // Allow User to Selec Minimum Files Selected
	 //This will automatically return the path
```
Customise
```
<!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="kfc_theme_background">@color/corDefundo</item>
        <item name="kfc_theme_foreground">@color/colorPrimary</item>
        <item name="kfc_theme_title">@color/cinzaEscuro</item>
        <item name="kfc_theme_breadcrumb">@color/azulClaro</item>
        <item name="kfc_theme_toolbox">@color/colorPrimary</item>
        <item name="kfc_theme_search_text">@color/cinzaEscuro</item>
        <item name="kfc_theme_search_hint">@color/cinzaClaro</item>
        <item name="kfc_theme_status">@color/colorPrimary</item>
        <item name="kfc_theme_file_icon">@color/colorPrimary</item>
        <item name="kfc_theme_file_name">@color/colorPrimary</item>
        <item name="kfc_theme_file_information">@color/colorAccent</item>
        <item name="kfc_theme_file_flag">@color/cinzaClaro</item>
        <item name="kfc_theme_file_asterisk">@color/amarelo</item>
        <item name="kfc_theme_checkbox">@color/colorAccent</item>
        <item name="kfc_theme_cancel_button">@color/colorAccent</item>
        <item name="kfc_theme_ok_button">@color/colorAccent</item>
        <item name="kfc_theme_create_folder_button">@color/verde</item>
    </style>
```
Permissions
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
License
```
The MIT License (MIT)

Copyright (c) 2019 Tiago Melo & Mohammad Zaki

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
