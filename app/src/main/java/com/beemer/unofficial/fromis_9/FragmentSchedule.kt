package com.beemer.unofficial.fromis_9

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.databinding.FragmentScheduleBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.calendarDayText)
}

class MonthHeaderViewContainer(view: View) : ViewContainer(view) {
    val textView = listOf<TextView>(
        view.findViewById(R.id.sunday),
        view.findViewById(R.id.monday),
        view.findViewById(R.id.tuesday),
        view.findViewById(R.id.wednesday),
        view.findViewById(R.id.thursday),
        view.findViewById(R.id.friday),
        view.findViewById(R.id.saturday)
    )
}

class FragmentSchedule : Fragment() {
    private val binding by lazy { FragmentScheduleBinding.inflate(layoutInflater) }
    private lateinit var activityMain: ActivityMain

    private val calendarView by lazy { binding.calendarView }
    private val calendarYearMonth by lazy { binding.calendarYearMonth }
    private val buttonPrevMonth by lazy { binding.buttonPrevMonth }
    private val buttonNextMonth by lazy { binding.buttonNextMonth }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityMain = context as ActivityMain
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var date: LocalDate = LocalDate.now()
        val nowMonth = YearMonth.now()
        val startMonth = nowMonth.minusMonths(100)
        val endMonth = nowMonth.plusMonths(100)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val textBorder = ContextCompat.getDrawable(activityMain, R.drawable.drawable_calendar_border_today)

        calendarView.apply {
            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(nowMonth) // 현재 달이 보이게 하기

            // 날짜 바인더 설정
            dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.textView.text = data.date.dayOfMonth.toString()

                    // 날짜 클릭 이벤트
                    if (data.position == DayPosition.MonthDate) { // 현재 달의 날짜만 클릭 가능
                        container.view.setOnClickListener {
                            if (data.date != date) {
                                val prevDate = date
                                date = data.date

                                // 이전에 선택되어 있던 날짜의 뷰 업데이트
                                prevDate.let { calendarView.notifyDateChanged(it) }

                                // 새로 선택한 날짜의 뷰 업데이트
                                calendarView.notifyDateChanged(data.date)
                            }
                        }
                    }

                    // 날짜에 스타일 적용
                    if (data.date == date) {
                        container.textView.background = textBorder // 해당 날짜(초깃값은 오늘, 날짜 클릭 시 클릭한 날짜)에 테두리 추가
                    } else {
                        container.textView.background = null

                        // 요일에 따른 글씨 색상 변경
                        when (data.date.dayOfWeek) {
                            DayOfWeek.SATURDAY -> container.textView.setTextColor(ContextCompat.getColor(activityMain, R.color.blue)) // 토요일
                            DayOfWeek.SUNDAY -> container.textView.setTextColor(ContextCompat.getColor(activityMain, R.color.red)) // 일요일
                            else -> container.textView.setTextColor(Color.BLACK) // 평일
                        }

                        // 지난달과 다음달 날짜 글씨 색상 변경
                        if (data.position == DayPosition.OutDate || data.position == DayPosition.InDate) {
                            container.textView.setTextColor(ContextCompat.getColor(activityMain, R.color.gray))
                        }
                    }
                }
            }

            // 헤더 바인더 설정
            monthHeaderBinder = object : MonthHeaderFooterBinder<MonthHeaderViewContainer> {
                override fun create(view: View): MonthHeaderViewContainer {
                    return MonthHeaderViewContainer(view)
                }

                override fun bind(container: MonthHeaderViewContainer, data: CalendarMonth) {
                    val daysOfWeek = DayOfWeek.entries.toTypedArray()
                    for (i in daysOfWeek.indices) {
                        val dayName = daysOfWeek[i].getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        container.textView[i].text = dayName
                    }
                }
            }

            // 달력 스크롤 이벤트
            monthScrollListener = {
                val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월", Locale.getDefault())
                val title = it.yearMonth.format(formatter)
                calendarYearMonth.text = title
            }
        }

        // 왼쪽 화살표 클릭
        buttonPrevMonth.setOnClickListener {
            val currentMonth = calendarView.findFirstVisibleMonth()?.yearMonth ?: return@setOnClickListener
            val prevMonth = currentMonth.minusMonths(1)
            calendarView.scrollToMonth(prevMonth)
        }

        // 오른쪽 화살표 클릭
        buttonNextMonth.setOnClickListener {
            val currentMonth = calendarView.findFirstVisibleMonth()?.yearMonth ?: return@setOnClickListener
            val nextMonth = currentMonth.plusMonths(1)
            calendarView.scrollToMonth(nextMonth)
        }

        return binding.root
    }
}