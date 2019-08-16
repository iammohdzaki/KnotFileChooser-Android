[![](https://jitpack.io/v/iammohdzaki/KnotFileChooser-Android.svg)](https://jitpack.io/#iammohdzaki/KnotFileChooser-Android)

# KnotFileChooser-Android

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
	        implementation 'com.github.iammohdzaki:KnotFileChooser-Android:1.0.1'
	}
```
How To Use
```
KnotFileChooser(this,
            allowBrowsing = true,
            allowCreateFolder = true,
            allowMultipleFiles = false,
            allowSelectFolder = false,
            minSelectedFiles = 0,
            maxSelectedFiles = 0,
            showFiles = true,
            showFoldersFirst = true,
            showFolders = true,
            showHiddenFiles = false,
            initialFolder = Environment.getExternalStorageDirectory(),
            restoreFolder = false,
            cancelable = true) //Dismiss Dialog On Cancel
            .title("Select a File")
            .sorter(Sorter.ByNewestModification)
            .onSelectedFilesListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
            .show()
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
