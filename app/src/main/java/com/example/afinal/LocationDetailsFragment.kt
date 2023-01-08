package com.example.afinal

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.afinal.databinding.FragmentLocationDetailsBinding

class LocationDetailsFragment : Fragment() {
    private lateinit var binding: FragmentLocationDetailsBinding
    val args: LocationDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Location Details"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)

        binding.title.text = args.name
        binding.description.text = args.description
        binding.timestamp.text = args.date
        binding.image.load(Uri.parse(args.url))
        binding.user.text = args.uploader

        var likes = 0

        binding.like.setOnClickListener {
            likes += 1
            binding.like.text = likes.toString()
        }

        return binding.root
    }
}