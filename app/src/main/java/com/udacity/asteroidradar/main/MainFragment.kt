package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.BaseApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidEntities
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by activityViewModels {
        AsteroidViewModelFactory(
            (activity?.application as BaseApplication).database.asteroidDao()
        )
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.getAsteroidData()

        val imageUrl = "https://api.nasa.gov/planetary/apod?api_key=SNrG4C86m2Zxhx9b7HAAnOGdJQqB6BzYRLlTi0fp"

        Picasso.with(binding.activityMainImageOfTheDay.context).load(imageUrl).fit().centerCrop()
            .networkPolicy(NetworkPolicy.OFFLINE)

            .into(binding.activityMainImageOfTheDay, object : Callback {
                override fun onSuccess() {
                    //No op
                }

                override fun onError() {
                    Picasso.with(binding.activityMainImageOfTheDay.context).load(
                        "https://apod.nasa.gov/apod/image/2001/STSCI-H-p2006a-h-1024x614.jpg")
                        .placeholder(R.drawable.placeholder_picture_of_day)
                        .error(R.drawable.ic_connection_error)
                        .into(binding.activityMainImageOfTheDay)
                }
            }

            )


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val asteroidAdapter = AsteroidAdapter { asteroid ->
            val action = MainFragmentDirections.actionShowDetail(asteroid)
            findNavController().navigate(action)
        }
        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = asteroidAdapter
        }
        viewModel.allAsteroids.observe(viewLifecycleOwner) {
            asteroidAdapter.submitList(it)
            Log.d("View Model Updated", viewModel.allAsteroids.value.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
