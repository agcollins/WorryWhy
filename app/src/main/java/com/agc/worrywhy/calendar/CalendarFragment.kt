package com.agc.worrywhy.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.agc.worrywhy.R
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_worry_calendar.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment() {
    private val viewModel: CalendarViewModel by viewModels()
    private val dayBinder by lazy {
        CalendarDayBinder()
    }
    private val monthHeaderBinder by lazy {
        HeaderBinder()
    }

    inner class HeaderBinder : MonthHeaderFooterBinder<MonthHeaderContainer> {
        var map: Map<YearMonth, CalendarViewModel.MonthSummary> = emptyMap()
        override fun bind(container: MonthHeaderContainer, month: CalendarMonth) {
            val monthSummary = map[month.yearMonth]
            val monthName = month.yearMonth.month.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            var monthCount = monthSummary?.count ?: 0
            container.month.text = monthName
            container.summary.text = resources.getQuantityString(R.plurals.template_month_header, monthCount, monthCount)
        }

        override fun create(view: View): MonthHeaderContainer {
            return MonthHeaderContainer(view)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worry_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = view.findViewById<CalendarView>(R.id.calendar)

        calendar.dayBinder = dayBinder
        calendar.monthHeaderBinder = monthHeaderBinder

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(12)
        val lastMonth = currentMonth.plusMonths(0)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendar.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendar.scrollToMonth(currentMonth)

        lifecycleScope.launch {
            viewModel.monthHeader.collect {
                if (it == null) return@collect
                monthHeaderBinder.map = it
                calendar.notifyCalendarChanged()
            }
        }

        lifecycleScope.launch {
            viewModel.monthWorries.collect {
                if (it == null) return@collect
                dayBinder.map = it
                calendar.notifyCalendarChanged()
            }
        }
    }

    interface Callbacks {
        fun onCalendarDayClicked(localDate: LocalDate)
    }

    val callbacks by lazy {
        requireParentFragment() as Callbacks
    }

    inner class CalendarDayBinder : DayBinder<CalendarViewContainer> {
        var map: Map<LocalDate, Int> = emptyMap()
        override fun bind(container: CalendarViewContainer, day: CalendarDay) {
            container.view.setOnClickListener {
                callbacks.onCalendarDayClicked(day.date)
            }

            container.day.text = day.date.dayOfMonth.toString()
            container.day.alpha = if (day.owner == DayOwner.THIS_MONTH) 1f.also {
                container.count.isVisible = true
                val count = map[day.date]
                if (count == null) {
                    container.count.isVisible = false
                } else {
                    container.count.text =
                        resources.getQuantityString(R.plurals.times_template_calendar, count, count)
                }
            } else 0.3f.also {
                container.count.isVisible = false
            }
        }

        override fun create(view: View) = CalendarViewContainer(view)
    }
}