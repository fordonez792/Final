package com.example.afinal

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.databinding.FragmentHomeBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var location: Location
    private lateinit var loginViewModel: LoginViewModel

    val authResult = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
        if(result.resultCode == RESULT_OK)
            Log.d("Main", "login success")
        else
            Log.d("Main", "login failed")
    }

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory((activity?.application as InventoryApplication).database.LocationDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "My Favorite Locations"
    }

    private fun setupMenu(adapter: HomeRcvAdapter) {
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.search -> {
                        val searchView = menuItem?.actionView as SearchView
                        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                TODO("Not yet implemented")
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                val searchText = newText!!.toLowerCase(Locale.getDefault())

                                if(searchText.isNotEmpty()) {
                                    val searchQuery = "%$searchText%"

                                    viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { list ->
                                        list.let {
                                            adapter.submitList(it)
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                                else{
                                    viewModel.locations.observe(viewLifecycleOwner) {
                                            locations ->
                                        locations.let {
                                            adapter.submitList(it)
                                        }
                                    }
                                    adapter.notifyDataSetChanged()
                                }
                                return true
                            }

                        })
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.authenticateState.observe(viewLifecycleOwner) { state ->
            if(state != LoginViewModel.AuthenticateState.AUNTHENTICATED) {
                launchSignIn()
            }
        }

        binding.rcv.layoutManager = LinearLayoutManager(activity)
        val adapter = HomeRcvAdapter {
            val action = HomeFragmentDirections.navigateToSingleLocationFragment(
                it.id, it.name, it.description, it.image, it.latitude, it.longitude
            )
            this.findNavController().navigate(action)
        }

        setupMenu(adapter)

        binding.rcv.adapter = adapter

        viewModel.locations.observe(viewLifecycleOwner) {
            locations ->
            locations.let {
                adapter.submitList(it)
            }
        }

        binding.addItem.setOnClickListener{
            val action = HomeFragmentDirections.navigateToAddItemFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                location = viewModel.locations.value!![pos]
                showConfirmationDialog()
            }
        }).attachToRecyclerView(binding.rcv)

        return binding.root
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete the item")
            .setMessage("Are you sure?")
            .setCancelable(false)
            .setNegativeButton("No") {_, _ -> }
            .setPositiveButton("Yes") {_, _ ->
                // perform deletion
                deleteItem()
            }
            .show()
    }

    private fun deleteItem() {
        viewModel.deleteItem(location)
    }

    private fun launchSignIn() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        authResult.launch(loginIntent)
    }
}
