package com.thyrocare.delegate;

import android.view.View;
import android.widget.EditText;

/**
 * Created by Orion on 5/8/2017.
 */

public interface OlcMasterBarcodeScanAdapterDelegate {
     void onScanClick(int position);
     void onAddClick(int position);
}
