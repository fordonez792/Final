package com.example.afinal

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import coil.load
import com.example.afinal.databinding.FragmentAddItemBinding

class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private var imagePath: Uri? = null
//    private val loginViewModel: LoginViewModel by activityViewModels()

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.LocationDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Add a Location"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater, container, false)

        val getImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) {
                uri ->
                imagePath = uri
                binding.locationImage.apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    load(uri) {
                        size(60, 60)
                    }
                }
            }

        binding.addImage.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.deleteImage.setOnClickListener {
            imagePath = null
            binding.locationImage.setImageResource(android.R.drawable.ic_menu_camera)
        }

        binding.locationSave.setOnClickListener {
            if(binding.locationName.text.toString() == "") {
                binding.error.text = "Name can't be empty"
            } else if(binding.locationLatitude.text.toString() == "") {
                binding.error.text = "Latitude can't be empty"
            } else if(binding.locationLongitude.text.toString() == "") {
                binding.error.text = "Longitude can't be empty"
            } else if(binding.locationDescription.text.toString() == "") {
                binding.error.text = "Description can't be empty"
            } else {
//                loginViewModel.uploadMessage(imagePath.toString(), binding.locationName.text.toString())

                viewModel.addNewItem(
                    binding.locationName.text.toString(),
                    imagePath.toString(),
                    binding.locationDescription.text.toString(),
                    binding.locationLatitude.text.toString().toFloat(),
                    binding.locationLongitude.text.toString().toFloat(),
                )
                val action = AddItemFragmentDirections.navigateToHomeFragment(
                    binding.locationName.text.toString(),
                    binding.locationLatitude.text.toString().toFloat(),
                    binding.locationLongitude.text.toString().toFloat(),
                    binding.locationDescription.text.toString(),
                    imagePath.toString()
                )
                Navigation.findNavController(binding.root).navigate(action)
            }
        }

        return binding.root
    }
}