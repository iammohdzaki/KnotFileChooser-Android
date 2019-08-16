package com.zaphlabs.filechooser

import android.content.Context
import android.os.Environment
import androidx.annotation.StringRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.*
import android.util.TypedValue
import android.view.View
import android.widget.*
import br.tiagohm.breadcrumbview.BreadCrumbItem
import br.tiagohm.breadcrumbview.BreadCrumbView
import br.tiagohm.easyadapter.EasyAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.zaphlabs.filechooser.filters.Filter
import com.zaphlabs.filechooser.utils.*
import java.io.File
import java.io.FileFilter
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class KnotFileChooser(
    val context: Context,
    val allowMultipleFiles: Boolean = false,
    val allowCreateFolder: Boolean = false,
    var initialFolder: File = Environment.getExternalStorageDirectory(),
    val allowSelectFolder: Boolean = false,
    val minSelectedFiles: Int = 0,
    val maxSelectedFiles: Int = Int.MAX_VALUE,
    val showHiddenFiles: Boolean = false,
    val showFoldersFirst: Boolean = true,
    val showFiles: Boolean = true,
    val showFolders: Boolean = true,
    val allowBrowsing: Boolean = true,
    val restoreFolder: Boolean = true,
    val cancelable:Boolean=true
) {

    //Constants.
    private val typedValue = TypedValue()
    private val theme = context.theme
    private val archivesListAdapter = EasyAdapter()
    private val pathPack = LinkedList<File>()
    private val seletionStatus = ConcurrentHashMap<File, Boolean>()
    private val filters = ArrayList<Filter>()
    private val chooserTextWatcher = ChooserTextWatcher()
    private val chooserFileFilter = ChooserFileFilter()
    private val backgroundColor: Int by lazy {
        theme.resolveAttribute(R.attr.kfc_theme_background, typedValue, true)
        typedValue.data
    }
    private val forgroundColor: Int by lazy {
        theme.resolveAttribute(R.attr.kfc_theme_foreground, typedValue, true)
        typedValue.data
    }
    private val titleTheme: Int by lazy {
        theme.resolveAttribute(R.attr.kfc_theme_title, typedValue, true)
        typedValue.data
    }
    private val cancelTheme: Int by lazy {
        theme.resolveAttribute(R.attr.kfc_theme_cancel_button, typedValue, true)
        typedValue.data
    }
    private val okTheme: Int by lazy {
        theme.resolveAttribute(R.attr.kfc_theme_ok_button, typedValue, true)
        typedValue.data
    }

    //Variables.
    private var title: CharSequence? = ""
    private val archivesList: MutableSet<File> = Collections.newSetFromMap(ConcurrentHashMap<File, Boolean>())
    private var cbSelectItem: CheckBox? = null
    private var selectedFile: File? = null
    private lateinit var file: File
    private var sort: Sorter = Sorter.ByNameInAscendingOrder
    private lateinit var dialog: DialogBuilder
    private var searchText = ""
    private var currentArchives: List<File> = Collections.emptyList()
    private var selectedArchiveTotalSize = 0L
    private var onSelectedFilesListener: (files: List<File>) -> Unit = {}

    init {
        //Default Home Folder.
        defaultHomeFolder(ChooserSharedPreference.getPreviouslySelectedDirectory(context, restoreFolder, initialFolder))
    }

    /**
     * Sets the title of the window.
     */
    fun title(title: CharSequence?): KnotFileChooser {
        this.title = title
        return this
    }

    /**
     * Sets the title of the window.
     */
    fun title(@StringRes resId: Int): KnotFileChooser {
        return title(context.getText(resId))
    }

    /**
     * Defines the sorting method for listing files and folders.
     */
    fun sorter(sorter: Sorter): KnotFileChooser {
        sort = sorter
        return this
    }

    fun onSelectedFilesListener(listener: (files: List<File>) -> Unit): KnotFileChooser {
        onSelectedFilesListener = listener
        return this
    }

    private fun defaultHomeFolder(folder: File): KnotFileChooser {
        initialFolder = folder
        file = folder
        //Clear the Path
        pathPack.clear()
        //Insert the Path.
        pathPack.addFirst(file)
        //Selected File.
        seletionStatus[file] = false
        return this
    }

    private fun displayBreadCrumbView(file: File) {
        var mfile = file
        //Clean.
        dialog.mDirectoryPath.itens.clear()
        //Checks if it is not a folder.
        if (!mfile.isFolder) {
            mfile = mfile.parentFile
        }
        //Gets the parent folder.
        val parent = mfile.parentFile
        //It has no parent folder.
        if (parent == null) {
            val item = RootFileBreadCrumbItem(mfile)
            dialog.mDirectoryPath.addItem(item)
        }
        //Has parent folder.
        else {
            displayBreadCrumbView(parent)
            val item = FileBreadCrumbItem(mfile)
            dialog.mDirectoryPath.addItem(item)
        }
    }

    private fun selectFile(buttonView: CompoundButton?, file: File, selected: Boolean) {
        //Checkbox selection.
        if (selected) {
            //It is not multi-selectable and has a file selected.
            if (!allowMultipleFiles && cbSelectItem != null) {
                val cb = cbSelectItem!!
                cbSelectItem = null
                //Two files that are in the same folder.
                if (buttonView !== cb && file.parent == selectedFile?.parent) {
                    //Deselect what is selected.
                    cb.isChecked = false
                } else {
                    //Remove what is selected.
                    archivesList.remove(selectedFile)
                    selectedFile = null
                }
            }
            //Adds the file.
            if (!archivesList.contains(file)) {
                selectedArchiveTotalSize += if (file.isFolder) 0 else file.length()
            }
            archivesList.add(file)
        } else {
            //Remove the file.
            if (archivesList.contains(file)) {
                selectedArchiveTotalSize -= if (file.isFolder) 0 else file.length()
            }
            archivesList.remove(file)
            selectedFile = null
        }
        //Marks the file that has been selected.
        cbSelectItem = buttonView as CheckBox?
        selectedFile = file
        //Updates the number of folders selected by plurality.
        dialog.displayQuantityOfSelectedItems(selectedArchiveTotalSize)
    }

    /**
     *   Go to a specific folder.
     */
    fun goTo(file: File) {
        //Do not allow browsing.
        if (!allowBrowsing) {
            //nothing
        }
        //Browse only if it is a folder.
        else if (file.isFolder) {
            this.file = file
            pathPack.addFirst(this.file)
            //Browse only You have not yet browsed this folder. Select all is disabled if it is a folder.
            if (allowMultipleFiles && !seletionStatus.containsKey(file)) {
                seletionStatus[file] = false
            }
            loadCurrentFolder()
            //Sets the state of the select all button.
            dialog.mSelectAll.isChecked = allowMultipleFiles && seletionStatus[file]!!
        }
    }

    /**
     * go to home folder.
     */
    fun goToStart() {
        goTo(initialFolder)
    }

    private fun backTo(file: File): Boolean {
        //You can browse and it is a folder.
        return if (allowBrowsing && file.isFolder) {
            this.file = file
            loadCurrentFolder()
            //Sets the state of the select all button.
            dialog.mSelectAll.isChecked = allowMultipleFiles && seletionStatus[file] ?: false
            true
        } else {
            false
        }
    }

    /**
    Go to previous folder.
     */
    fun back(): Boolean {
        //If you can browse and there is a folder to browse.
        return if (allowBrowsing && pathPack.size > 1) {
            //Remove current folder from stack.
            pathPack.removeFirst()
            //Return to previous folder.
            backTo(pathPack.first)
        } else {
            false
        }
    }

    private fun compareFile(a: File, b: File): Int {
        //Sort by folders first.
        return if (showFoldersFirst) {
            when {
                a.isDirectory == b.isDirectory -> sort.compare(a, b)
                a.isDirectory -> -1
                else -> 1
            }
        } else {
            when {
                a.isFile == b.isFile -> sort.compare(a, b)
                a.isFile -> -1
                else -> 1
            }
        }
    }

    private fun scanFiles(file: File): List<File> {
        //Get file list.
        val fileList = file.listFiles(chooserFileFilter)?.toMutableList() ?: Collections.emptyList()
        //Sets the number of items displayed..
        dialog.mItemQuantity.text = fileList.size.toString()
        //Order by.
        fileList.sortWith(Comparator { a, b -> compareFile(a, b) })
        return fileList
    }

    private fun displayRecyclerView(file: File) {
        currentArchives = scanFiles(file)
        archivesListAdapter.setData(currentArchives)
    }

    private fun loadCurrentFolder() {
        displayBreadCrumbView(file)
        displayRecyclerView(file)
        dialog.mTotalSize.text = file.sizeAsString
    }

    /**
     * Exit a dialog.
     */
    fun show() {
        DialogBuilder()
        dialog.show()
    }

    private inner class DialogBuilder : MaterialDialog.Builder(context) {

        //Views on the Dialog.
        val mTitle: TextView by lazy { customView.findViewById<TextView>(R.id.tvTitle) }
        val mDirectoryPath: BreadCrumbView<File> by lazy { customView.findViewById<BreadCrumbView<File>>(R.id.directoryPath) }
        val mListFileFolders: RecyclerView by lazy { customView.findViewById<RecyclerView>(R.id.rvArchivesList) }
        val mTotalSize: TextView by lazy { customView.findViewById<TextView>(R.id.totalSize) }
        val mItemQuantity: TextView by lazy { customView.findViewById<TextView>(R.id.tvItemQuantity) }
        val mBack: ImageView by lazy { customView.findViewById<ImageView>(R.id.ivBack) }
        val mHomeDirectory: ImageView by lazy { customView.findViewById<ImageView>(R.id.ivHomeDirectory) }
        val mQuantityItemsSelected: TextView by lazy { customView.findViewById<TextView>(R.id.tvTotalItemsSelected) }
        val mSearch: ImageView by lazy { customView.findViewById<ImageView>(R.id.ivSearch) }
        val mSearchField: EditText by lazy { customView.findViewById<EditText>(R.id.etSearch) }
        val mSearchView: View by lazy { customView.findViewById<View>(R.id.flSearchBox) }
        val mSwipeRefreshLayout: SwipeRefreshLayout by lazy { customView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout) }
        val mSelectAll: CheckBox by lazy { customView.findViewById<CheckBox>(R.id.cbSelectAll) }
        val mCreateFolder: FloatingActionButton by lazy { customView.findViewById<FloatingActionButton>(R.id.fbAddFolder) }

        init {
            dialog = this
            customView(R.layout.dialog_file_chooser, false)
            if (this@KnotFileChooser.title.isNullOrEmpty()) mTitle.visibility = View.GONE else mTitle.text =
                this@KnotFileChooser.title
            positiveText(android.R.string.ok)
            negativeText(android.R.string.cancel)
            backgroundColor(this@KnotFileChooser.backgroundColor)
            positiveColor(forgroundColor)
            negativeColor(forgroundColor)
            mSwipeRefreshLayout.setColorSchemeColors(forgroundColor)
            cancelable(cancelable)
            canceledOnTouchOutside(false)
            autoDismiss(false)
            //Configure Recycler View
            mListFileFolders.layoutManager = LinearLayoutManager(context)
            archivesListAdapter.map<File>(R.layout.file_item) { file, injector ->
                //Set File Icon.
                injector.image(
                    R.id.fileIcon,
                    if (file.isFolder) R.drawable.folder else getIconByExtension(file)
                )
                //Set Protected File.
                injector.image(
                    R.id.protectedFile,
                    if (file.isProtected) R.drawable.lock else 0
                )
                injector.image(
                    R.id.itemSelectedFolder,
                    if (file.isFolder && containsSelectedFile(file)) R.drawable.asterisk else 0
                )
                injector.using<View>(R.id.fileIcon) { alpha = if (file.isHidden) 0.4f else 1f }
                injector.text(R.id.tvFileName, file.name)
                if (file.isFile) {
                    injector.text(R.id.tvFileSize, file.sizeAsString)
                } else {
                    val itemCount = file.count(chooserFileFilter)
                    val stringRes = if (itemCount > 1) R.string.plural_items else R.string.singular_item
                    injector.text(R.id.tvFileSize, context.getString(stringRes, itemCount))
                }
                injector.text(R.id.lastModificationDate, file.lastModified)
                injector.show(R.id.cbSelectFile, allowSelectFolder || file.isFile)
                injector.click<View>(EasyAdapter.ROOT_VIEW) {
                    goTo(file)
                }
                injector.using<CheckBox>(R.id.cbSelectFile) {
                    tag = file
                    setOnCheckedChangeListener(null)
                    isChecked = archivesList.contains(file)
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        selectFile(buttonView, file, isChecked)
                    }
                }
            }
            archivesListAdapter.mapEmpty(R.layout.dialog_empty_folder)
            mSwipeRefreshLayout.setOnRefreshListener {
                loadCurrentFolder()
                mSwipeRefreshLayout.isRefreshing = false
            }
            mSelectAll.setOnCheckedChangeListener { _, checked ->
                seletionStatus[file] = checked
                currentArchives.forEach {
                    if (allowSelectFolder || !it.isFolder) {
                        selectFile(null, it, checked)
                    }
                }
                loadCurrentFolder()
            }
            mDirectoryPath.setBreadCrumbListener(object : BreadCrumbView.BreadCrumbListener<File> {
                override fun onItemClicked(
                    breadCrumbView: BreadCrumbView<File>,
                    breadCrumbItem: BreadCrumbItem<File>,
                    i: Int
                ) {
                    goTo(breadCrumbItem.selectedItem)
                }

                override fun onItemValueChanged(
                    breadCrumbView: BreadCrumbView<File>,
                    breadCrumbItem: BreadCrumbItem<File>,
                    i: Int,
                    file: File,
                    t1: File
                ): Boolean {
                    return false
                }
            })
            mBack.setOnClickListener { back() }
            mHomeDirectory.setOnClickListener { goToStart() }
            mSearch.setOnClickListener {
                if (mSearchView.visibility == View.VISIBLE) {
                    mSearchView.visibility = View.GONE
                } else {
                    mSearchView.visibility = View.VISIBLE
                }
            }
            mSearchField.removeTextChangedListener(chooserTextWatcher)
            mSearchField.addTextChangedListener(chooserTextWatcher)
            archivesListAdapter.attachTo(mListFileFolders)
            //Create Folder.
            mCreateFolder.setOnClickListener {
                MaterialDialog.Builder(context)
                    .title(R.string.create_new_folder)
                    .titleColor(titleTheme)
                    .inputRangeRes(1, -1, R.color.input_out_of_range)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .negativeText(android.R.string.cancel)
                    .negativeColor(cancelTheme)
                    .positiveColor(okTheme)
                    .backgroundColor(this@KnotFileChooser.backgroundColor)
                    .input(R.string.type_folder_name, 0, false) { _, input ->
                        val newFolder = File(file, input.toString())
                        try {
                            if (!newFolder.mkdir()) {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                            } else {
                                loadCurrentFolder()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "error: " + e.message, Toast.LENGTH_SHORT).show()
                        }
                    }.show()
            }
            onPositive { dialog, _ ->
                //You have selected the minimum number of files..
                if (archivesList.size in minSelectedFiles..maxSelectedFiles) {
                    onSelectedFilesListener(archivesList.toList())
                    //Show directory Path if no file selected
                    if(archivesList.size == 0){
                        archivesList.add(getPath())
                        onSelectedFilesListener(archivesList.toList())
                    }
                    //Close a dialog.
                    dialog.dismiss()
                }

            }
            onNegative { dialog, _ ->
                if(!cancelable){
                    //You have selected the minimum amount of files.
                    if (archivesList.size in minSelectedFiles..maxSelectedFiles) {
                        //Dismiss a dialog.
                        dialog.dismiss()
                    }
                }else{
                    dialog.dismiss()
                }

            }
            dismissListener {
                //
                //Saves the current folder if restoring folders is allowed.
                if (restoreFolder) {
                    ChooserSharedPreference.setPreviouslySelectedDirectory(context, file)
                }
            }
            //Allows multiple selection.
            mSelectAll.visibility = if (allowMultipleFiles) View.VISIBLE else View.GONE
            //Allow create folder.
            dialog.mCreateFolder.visibility = if (allowCreateFolder) View.VISIBLE else View.GONE
            //Start.
            displayQuantityOfSelectedItems(0)
            loadCurrentFolder()
        }

        fun displayQuantityOfSelectedItems(size: Long) {
            //Updates the number of folders selected by plurality.
            if (archivesList.size > 1) {
                dialog.mQuantityItemsSelected.text =
                    context.getString(R.string.plural_selected_items, archivesList.size, size.toSizeString())
            } else {
                dialog.mQuantityItemsSelected.text =
                    context.getString(R.string.singular_selected_item, archivesList.size, size.toSizeString())
            }
        }

        fun getPath():File{
            val path=StringBuilder()
            for(i in 1 until mDirectoryPath.itens.size){
                path.append("/"+mDirectoryPath.itens[i].text)
            }
            return File("$path")
        }

        private fun containsSelectedFile(parent: File): Boolean {
            for (file in archivesList) {
                if (file.absolutePath != parent.absolutePath &&
                    file.absolutePath.startsWith(parent.absolutePath)
                ) {
                    return true
                }
            }
            return false
        }

        private fun getIconByExtension(file: File): Int {
            return when (file.extension) {
                "mp4" -> R.drawable.video
                "c", "cpp", "cs", "js", "h", "java", "kt", "php", "xml" -> R.drawable.code
                "avi" -> R.drawable.avi
                "doc" -> R.drawable.doc
                "flv" -> R.drawable.flv
                "jpg", "jpeg" -> R.drawable.jpg
                "json" -> R.drawable.json
                "mov" -> R.drawable.mov
                "mp3" -> R.drawable.mp3
                "pdf" -> R.drawable.pdf
                "txt" -> R.drawable.txt
                else -> R.drawable.archive
            }
        }
    }



    private object ChooserSharedPreference {

        private const val NAME = "knotfilechooser_prefs"
        private const val PREVIOUS_SELECTED_FOLDER = "prev_selected_folder"

        fun getPreviouslySelectedDirectory(context: Context, restoreFolder: Boolean, initialFolder: File): File {
            if (!restoreFolder) return initialFolder
            val path = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                .getString(PREVIOUS_SELECTED_FOLDER, null)
            return if (path != null) File(path) else initialFolder
        }

        fun setPreviouslySelectedDirectory(context: Context, file: File) {
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(PREVIOUS_SELECTED_FOLDER, file.absolutePath)
                .apply()
        }
    }

    //Item for root folder.
    private class RootFileBreadCrumbItem(file: File) : FileBreadCrumbItem(file) {

        override fun getText() = "/"
    }

    //Item for a folder.
    private open class FileBreadCrumbItem(file: File) : BreadCrumbItem<File>() {

        init {
            itens = listOf(file)
        }

        override fun getText(): String? = selectedItem.name
    }

    //File Filter.
    private inner class ChooserFileFilter : FileFilter {

        override fun accept(f: File): Boolean {
            val showHidden = showHiddenFiles || !f.isHidden
            return (searchText.isEmpty() || f.name.contains(searchText, true)) &&
                    //It is a hidden file and can be displayed.
                    showHidden &&
                    //View files and / or folders.
                    (showFiles && f.isFile || showFolders && f.isFolder) &&
                    //Filters.
                    filter(f)
        }

        private fun filter(f: File): Boolean {
            //No filters.
            if (filters.size == 0) return true
            //Filters.
            for (filter in filters) {
                if (filter.accept(f)) {
                    return true
                }
            }
            return false
        }
    }

    private inner class ChooserTextWatcher : TextWatcher {

        override fun afterTextChanged(s: Editable) {
            searchText = s.toString().toLowerCase()
            loadCurrentFolder()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }
    }
}