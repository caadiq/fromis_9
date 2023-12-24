package com.beemer.unofficial.fromis_9

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.beemer.unofficial.fromis_9.adapter.AdapterSchedule
import com.beemer.unofficial.fromis_9.api.ApiSchedule
import com.beemer.unofficial.fromis_9.api.ScheduleResponse
import com.beemer.unofficial.fromis_9.data.DataSchedule
import com.beemer.unofficial.fromis_9.databinding.FragmentScheduleBinding
import com.beemer.unofficial.fromis_9.view.ItemDecoratorDivider
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class DayViewContainer(view: View) : ViewContainer(view) {
    val calendarDayText: TextView = view.findViewById(R.id.calendarDayText)
    val calendarDayDot: TextView = view.findViewById(R.id.calendarDayDot)
}

class MonthHeaderViewContainer(view: View) : ViewContainer(view) {
    val calendarWeekText = listOf<TextView>(
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
    private val textDate by lazy { binding.textDate }
    private val textNoSchedule by lazy { binding.textNoSchedule }
    private val recyclerView by lazy { binding.recyclerView }

    private val adapterSchedule by lazy { AdapterSchedule() }
    private val scheduleDataMap = mutableMapOf<LocalDate, List<DataSchedule>>()

    private var date: LocalDate = LocalDate.now()
    private var isFirstLoad = true

    private val scheduleApi: ApiSchedule by lazy {
        val scheduleUrl = BuildConfig.SCHEDULE_API
        val retrofit = Retrofit.Builder()
            .baseUrl(scheduleUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiSchedule::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityMain = context as ActivityMain
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val nowMonth = YearMonth.now()
        val startMonth = nowMonth.minusMonths(100)
        val endMonth = nowMonth.plusMonths(100)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val textBorder = ContextCompat.getDrawable(activityMain, R.drawable.drawable_calendar_border_today)

        recyclerView.apply {
            adapter = adapterSchedule
            addItemDecoration(ItemDecoratorDivider(20, 0, 0, 0, Color.GRAY, 1, 10))
        }

        calendarView.apply {
            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(nowMonth) // 현재 달이 보이게 하기

            // 날짜 바인더 설정
            dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.calendarDayText.text = data.date.dayOfMonth.toString()

                    // 날짜 클릭 이벤트
                    container.view.setOnClickListener {
                        if (data.position == DayPosition.MonthDate && data.date != date) {
                            val prevDate = date
                            date = data.date

                            // 이전에 선택되어 있던 날짜의 뷰 업데이트
                            prevDate.let { calendarView.notifyDateChanged(it) }

                            // 새로 선택한 날짜의 뷰 업데이트
                            calendarView.notifyDateChanged(data.date)

                            setSchedule(date)
                            setScheduleVisibility(date)
                        }
                    }

                    // 요일에 따른 글씨 색상 변경
                    when (data.date.dayOfWeek) {
                        DayOfWeek.SATURDAY -> container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.blue)) // 토요일
                        DayOfWeek.SUNDAY -> container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.red)) // 일요일
                        else -> container.calendarDayText.setTextColor(Color.BLACK) // 평일
                    }

                    // 지난달과 다음달 날짜 글씨 색상 및 배경 변경
                    if (data.position == DayPosition.OutDate || data.position == DayPosition.InDate) {
                        container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.gray))
                        container.calendarDayText.background = null
                    } else {
                        // 현재 달의 날짜에만 테두리 적용
                        if (data.date == date) {
                            container.calendarDayText.background = textBorder
                            textDate.text = data.date.format(DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN))
                        } else {
                            container.calendarDayText.background = null
                        }

                        // 일정이 있는 날에만 점 표시
                        if (scheduleDataMap.containsKey(data.date) && scheduleDataMap[data.date]!!.isNotEmpty()) {
                            container.calendarDayDot.visibility = View.VISIBLE
                        } else {
                            container.calendarDayDot.visibility = View.GONE
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
                        val dayName = daysOfWeek[i].getDisplayName(TextStyle.SHORT, Locale.KOREAN)
                        container.calendarWeekText[i].text = dayName
                    }
                }
            }

            // 달력 스크롤 이벤트
            monthScrollListener = {
                val formatYear = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())
                val formatMonth = DateTimeFormatter.ofPattern("MM", Locale.getDefault())
                val year = it.yearMonth.format(formatYear).toInt()
                val month = it.yearMonth.format(formatMonth).toInt()
                calendarYearMonth.text = getString(R.string.str_fragment_schedule_calendar_title, "$year", "$month")

                getScheduleFromApi(year, month)
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

    private fun getScheduleFromApi(year: Int, month: Int) {
        lifecycleScope.launch {
            try {
                val schedules = scheduleApi.getSchedules(year, month)
                saveScheduleData(schedules)
            } catch (e: Exception) {
                Toast.makeText(activityMain, "일정을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveScheduleData(schedules: List<ScheduleResponse>) {
        scheduleDataMap.clear() // map 초기화

        // map에 일정 데이터 저장
        schedules.forEach { schedule ->
            val dateTime = LocalDateTime.parse(schedule.date, DateTimeFormatter.ISO_DATE_TIME)
            val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            val dataSchedule = DataSchedule(
                time = time,
                title = schedule.title,
                description = schedule.description,
                image = schedule.platformIcon.imageUrl
            )

            val scheduleList = scheduleDataMap.getOrDefault(dateTime.toLocalDate(), mutableListOf()).toMutableList()
            scheduleList.add(dataSchedule)
            scheduleDataMap[dateTime.toLocalDate()] = scheduleList
        }

        calendarView.notifyCalendarChanged()

        if (isFirstLoad) {
            isFirstLoad = false
            setSchedule(date)
            setScheduleVisibility(date)
        }
    }

    // 선택한 날짜에 해당하는 일정을 RecyclerView에 표시
    private fun setSchedule(date: LocalDate) {
        val scheduleForDate = scheduleDataMap[date] ?: emptyList()
        adapterSchedule.itemList = scheduleForDate.toMutableList()
        adapterSchedule.notifyDataSetChanged()
    }


    private fun setScheduleVisibility(selectedDate: LocalDate) {
        if (scheduleDataMap[selectedDate].isNullOrEmpty()) {
            textNoSchedule.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            textNoSchedule.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}