package com.bandung.adddevice;

import android.view.View;
import android.widget.AdapterView;

interface MainActivitya {
    void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id);

    void onNothingSelected(AdapterView<?> adapterView);
}
