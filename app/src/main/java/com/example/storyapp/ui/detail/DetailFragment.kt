package com.example.storyapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = DetailFragmentArgs.fromBundle(arguments as Bundle)
        binding.apply {
            Glide.with(root)
                .load(bundle.image)
                .into(imgStory)
            tvName.text = bundle.name
            tvDescription.text = bundle.desc
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}