package com.example.afinal

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.afinal.databinding.FragmentSingleLocationBinding
import com.google.android.material.snackbar.Snackbar

class SingleLocationFragment : Fragment() {

    private lateinit var binding: FragmentSingleLocationBinding
    val args: SingleLocationFragmentArgs by navArgs()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var item: Location

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.LocationDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "About the Location"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleLocationBinding.inflate(inflater, container,false)

        viewModel.retrieveItem(args.id).observe(viewLifecycleOwner) {
                selectedItem ->
            item = selectedItem
            binding.name.text = args.name
            binding.description.text = args.description
            binding.image.load(Uri.parse(args.image))

        }

        binding.weather.setOnClickListener {
            val action = SingleLocationFragmentDirections.navigateToWeatherFragment(args.name)
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.showMap.setOnClickListener{
            val action = SingleLocationFragmentDirections.navigateToGoogleMapsFragment(args.latitude, args.longitude)
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.sendRemote.setOnClickListener {
            loginViewModel.uploadMessage(args.image, binding.name.text.toString(), binding.description.text.toString())
            Snackbar.make(binding.root, "Upladed ${binding.name.text.toString()} successfully to database", 3000).show()
        }

        return binding.root
    }
}