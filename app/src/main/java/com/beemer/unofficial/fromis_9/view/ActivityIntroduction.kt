package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.adapter.AdapterBannerImage
import com.beemer.unofficial.fromis_9.adapter.AdapterMembers
import com.beemer.unofficial.fromis_9.data.DataBannerImage
import com.beemer.unofficial.fromis_9.databinding.ActivityIntroductionBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryIntroduction
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactory
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelIntroduction
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ActivityIntroduction : AppCompatActivity() {
    private val binding by lazy { ActivityIntroductionBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelIntroduction by lazy { ViewModelProvider(this, ViewModelFactory(RepositoryIntroduction(RetrofitService.apiIntroduction)))[ViewModelIntroduction::class.java] }

    private lateinit var gridLayoutManager: GridLayoutManager
    private val adapterMembers = AdapterMembers()

    private var autoScrollJob: Job? = null
    private var imageListSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        autoScrollJob?.cancel()
    }

    override fun onPause() {
        super.onPause()
        autoScrollJob?.cancel()
    }

    override fun onResume() {
        super.onResume()
        setupAutoScroll()
    }

    private fun setupViewModel() {
        viewModel.apply {
            getIntroduction()
            bannerImage.observe(this@ActivityIntroduction) {
                setupViewPager(it ?: emptyList())
                imageListSize = it.size
                setupAutoScroll()
            }
            debutDate.observe(this@ActivityIntroduction) {
                val debut = LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
                val today = LocalDate.now()
                val dday = ChronoUnit.DAYS.between(debut, today)
                val ddayFormatted = NumberFormat.getNumberInstance().format(dday)

                binding.txtDebutDate.text = it.replace("-", ".")
                binding.txtDebutDDay.text = getString(R.string.str_activity_introduction_debut_dday, ddayFormatted)
            }
            members.observe(this@ActivityIntroduction) {
                adapterMembers.setMembers(it ?: emptyList())
            }
        }
    }

    private fun setupViewPager(images: List<DataBannerImage>) {
        binding.viewPager.apply {
            adapter = AdapterBannerImage(images)
            isUserInputEnabled = false
            offscreenPageLimit = images.size
        }
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    private fun setupAutoScroll() {
        autoScrollJob?.cancel()
        autoScrollJob = lifecycleScope.launch {
            while (isActive) {
                delay(5000)
                val nextItem = (binding.viewPager.currentItem + 1) % imageListSize
                binding.viewPager.setCurrentItem(nextItem, true)
            }
        }
    }

    private fun setupRecyclerView() {
        gridLayoutManager = GridLayoutManager(this, 2)

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = adapterMembers
            setHasFixedSize(true)
        }

        adapterMembers.setOnItemClickListener { item, _ ->
            DialogMember.newInstance(
                imageUrl = item.imageUrl,
                name = item.name,
                birth = item.birth,
                blood = item.blood,
                position = item.position
            ).show(supportFragmentManager, "DialogMember")
        }
    }
}