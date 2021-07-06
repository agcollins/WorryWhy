package com.agc.worrywhy.calendar

import android.view.View
import android.widget.TextView
import com.agc.worrywhy.R
import com.kizitonwose.calendarview.ui.ViewContainer

class MonthHeaderContainer(view: View) : ViewContainer(view) {
    val month: TextView = view.findViewById(R.id.text_month)
}