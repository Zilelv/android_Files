package com.owncloud.android.ui.adapter;

import android.view.MenuItem;

import com.owncloud.android.datamodel.OCFile;

//Files.fm completely new class to get info from FileListListAdapter to OCFileListFragment
public interface AdapterCallback {
    boolean onMethodCallback(OCFile file, MenuItem menuItem);
}
