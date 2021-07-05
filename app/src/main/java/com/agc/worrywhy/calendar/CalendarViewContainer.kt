package com.agc.worrywhy.calendar

import android.view.View
import android.widget.TextView
import com.agc.worrywhy.R
import com.kizitonwose.calendarview.ui.ViewContainer

class CalendarViewContainer(view: View) : ViewContainer(view) {
    val day = view.findViewById<TextView>(R.id.text_day)
    val count = view.findViewById<TextView>(R.id.text_count)
}