package com.iiitd.swindlesheet.swindlesheetmobile;

import android.view.View;

/**
 * Created by ashut on 18-Oct-16.
 */
public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
