package com.beemer.unofficial.fromis_9.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.DialogMemberBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class DialogMember : DialogFragment() {
    private var _binding: DialogMemberBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(imageUrl: String, name: String, birth: String, blood: String, position: String): DialogMember {
            val args = Bundle().apply {
                putString("imageUrl", imageUrl)
                putString("name", name)
                putString("birth", birth)
                putString("blood", blood)
                putString("position", position)
            }

            return DialogMember().apply {
                arguments = args
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.width = (context.resources.displayMetrics.widthPixels.times(0.85)).toInt()
        }

        arguments?.let {
            Glide.with(requireContext()).load(it.getString("imageUrl")).transition(DrawableTransitionOptions.withCrossFade()).into(binding.imgProfile)
            binding.txtName.text = it.getString("name")
            binding.txtBirth.text = it.getString("birth")?.replace("-", ".")
            binding.txtBlood.text = getString(R.string.str_dialog_member_blood_type, it.getString("blood"))
            binding.txtPosition.text = it.getString("position")?.replace(", ", "\n")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}