package com.beemer.unofficial.fromis_9.view

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
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.adapter.AdapterSchedule
import com.beemer.unofficial.fromis_9.data.DataSchedule
import com.beemer.unofficial.fromis_9.databinding.FragmentMainScheduleBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryScheduleList
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.ui.ItemDecoratorDivider
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactory
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelScheduleList
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

class FragmentMainSchedule : Fragment() {
    private var _binding: FragmentMainScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityMain: ActivityMain
    private val viewModel: ViewModelScheduleList by lazy { ViewModelProvider(this, ViewModelFactory(RepositoryScheduleList(RetrofitService.apiScheduleList)))[ViewModelScheduleList::class.java] }

    private val adapterSchedule = AdapterSchedule()
    private val scheduleDataMap = mutableMapOf<LocalDate, List<DataSchedule>>()

    private var nowMonth = YearMonth.now()
    private var date: LocalDate = LocalDate.now()
    private var isFirstLoad = true

    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월", Locale.getDefault())
    private val dayFormatter = DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityMain = context as ActivityMain
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainScheduleBinding.inflate(inflater, container, false)

        setCalendarView()
        setRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setCalendarView() {
        val startMonth = nowMonth.minusMonths(100)
        val endMonth = nowMonth.plusMonths(100)
        val firstDayOfWeek = WeekFields.of(DayOfWeek.SUNDAY, 1).firstDayOfWeek
        val textBorder = ContextCompat.getDrawable(activityMain, R.drawable.drawable_calendar_border_today)

        binding.calendarView.apply {
            setup(startMonth, endMonth, firstDayOfWeek)

            monthScrollListener = {
                nowMonth = it.yearMonth
                binding.calendarYearMonth.text = nowMonth.format(yearMonthFormatter)
                viewModel.getScheduleList(nowMonth.year, nowMonth.monthValue)
            }

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
                            prevDate.let { binding.calendarView.notifyDateChanged(it) }

                            // 새로 선택한 날짜의 뷰 업데이트
                            binding.calendarView.notifyDateChanged(data.date)

                            setSchedule(date)
                            setScheduleVisibility(date)
                        }
                    }

                    when {
                        data.date.isEqual(LocalDate.now()) -> {
                            // 오늘 날짜에 대한 특별한 색상 설정
                            container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.colorPrimary))
                        }
                        data.date.dayOfWeek == DayOfWeek.SATURDAY -> {
                            container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.blue)) // 토요일
                        }
                        data.date.dayOfWeek == DayOfWeek.SUNDAY -> {
                            container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.red)) // 일요일
                        }
                        else -> {
                            container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.darker_gray)) // 평일
                        }
                    }

                    if (data.position == DayPosition.MonthDate) {
                        // 선택한 날짜에 테두리 적용
                        if (data.date == date) {
                            container.calendarDayText.background = textBorder
                            binding.textDate.text = data.date.format(dayFormatter)
                        } else {
                            container.calendarDayText.background = null
                        }

                        // 일정이 있는 날에만 점 표시
                        if (scheduleDataMap.containsKey(data.date) && scheduleDataMap[data.date]!!.isNotEmpty()) {
                            container.calendarDayDot.visibility = View.VISIBLE
                        } else {
                            container.calendarDayDot.visibility = View.GONE
                        }
                    } else {
                        container.calendarDayText.setTextColor(ContextCompat.getColor(activityMain, R.color.gray))
                        container.calendarDayText.background = null
                        container.calendarDayDot.visibility = View.GONE
                    }
                }
            }

            // 헤더 바인더 설정
            monthHeaderBinder = object : MonthHeaderFooterBinder<MonthHeaderViewContainer> {
                override fun create(view: View): MonthHeaderViewContainer {
                    return MonthHeaderViewContainer(view)
                }

                override fun bind(container: MonthHeaderViewContainer, data: CalendarMonth) {
                    val daysOfWeek = arrayOf(
                        DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY
                    )

                    daysOfWeek.forEachIndexed { index, dayOfWeek ->
                        val dayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
                        container.calendarWeekText[index].text = dayName
                    }
                }
            }
        }

        // 왼쪽 화살표 클릭
        binding.buttonPrevMonth.setOnClickListener {
            val currentMonth = binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: return@setOnClickListener
            val prevMonth = currentMonth.minusMonths(1)
            binding.calendarView.scrollToMonth(prevMonth)
        }

        // 오른쪽 화살표 클릭
        binding.buttonNextMonth.setOnClickListener {
            val currentMonth = binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: return@setOnClickListener
            val nextMonth = currentMonth.plusMonths(1)
            binding.calendarView.scrollToMonth(nextMonth)
        }
    }

    private fun setRecyclerView() {
        binding.recyclerView.apply {
            adapter = adapterSchedule
            addItemDecoration(ItemDecoratorDivider(20, 0, 0, 0, 1, 10, Color.GRAY))
            itemAnimator = null
        }
    }

    private fun observeViewModel() {
        viewModel.scheduleList.observe(viewLifecycleOwner) { updateScheduleList(it) }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(activityMain, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateScheduleList(scheduleMap: Map<LocalDate, List<DataSchedule>>) {
        // 캘린더 또는 RecyclerView 등 UI 업데이트
        if (scheduleMap != scheduleDataMap) {
            scheduleDataMap.clear()
            scheduleDataMap.putAll(scheduleMap)
            binding.calendarView.notifyCalendarChanged() // 캘린더 뷰 업데이트
        }

        if (isFirstLoad) {
            isFirstLoad = false
            setSchedule(date)
            setScheduleVisibility(date)
        }
    }

    // 선택한 날짜에 해당하는 일정을 RecyclerView에 표시
    private fun setSchedule(date: LocalDate) {
        val scheduleForDate = scheduleDataMap[date] ?: emptyList()
        adapterSchedule.setSchedule(scheduleForDate)
    }

    // 선택한 날짜에 일정이 없으면 일정이 없다는 텍스트 표시
    private fun setScheduleVisibility(selectedDate: LocalDate) {
        if (scheduleDataMap[selectedDate].isNullOrEmpty()) {
            binding.textNoSchedule.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.textNoSchedule.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            binding.calendarView.scrollToMonth(nowMonth) // 이번 달이 보이게 하기
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}