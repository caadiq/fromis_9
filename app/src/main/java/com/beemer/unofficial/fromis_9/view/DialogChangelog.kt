package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.beemer.unofficial.fromis_9.adapter.AdapterChangelog
import com.beemer.unofficial.fromis_9.data.DataChangelog
import com.beemer.unofficial.fromis_9.databinding.DialogChangelogBinding
import com.beemer.unofficial.fromis_9.ui.ItemDecoratorDivider

class DialogChangelog : DialogFragment() {
    private var _binding: DialogChangelogBinding? = null
    private val binding get() = _binding!!

    private val adapterChangelog = AdapterChangelog()
    private var changelogList: List<DataChangelog>? = null

    fun setChangelogList(list: List<DataChangelog>) {
        changelogList = list
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogChangelogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.attributes?.width = (context.resources.displayMetrics.widthPixels.times(0.85)).toInt()
        }

        binding.recyclerView.apply {
            adapter = adapterChangelog
            addItemDecoration(ItemDecoratorDivider(0, 80, 0, 0, 0, 0, null))
        }

        changelogList?.let { adapterChangelog.setChangelog(it) }

        binding.txtClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}