package com.example.afinal

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.afinal.databinding.FragmentDiscoveryBinding
import com.example.afinal.databinding.FragmentHomeBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class DiscoveryFragment : Fragment() {
    private lateinit var binding: FragmentDiscoveryBinding
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        binding = FragmentDiscoveryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Locations on Server"

        setupMenu()

        val query = loginViewModel.collectionRef.orderBy("timestamp")
        val options = FirestoreRecyclerOptions.Builder<ImageFirestore>()
            .setQuery(query, ImageFirestore::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
        val adapter = DiscoveryRCVAdapter(options) { item ->
            val action = DiscoveryFragmentDirections.navigateToLocationDetailsFragment(
                item.description.toString(),
                item.timestamp?.toDate().toString(),
                item.imageUrl.toString(),
                item.uploader.toString(),
                item.name.toString()
            )
            findNavController().navigate(action)
        }
        val manager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = manager
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.logout_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.logout -> AuthUI.getInstance().signOut(requireContext())
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}