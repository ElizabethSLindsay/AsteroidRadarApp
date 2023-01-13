package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.BaseApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidEntities
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        AsteroidViewModelFactory(
            (activity?.application as BaseApplication).database.asteroidDao()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
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

        val adapter = AsteroidAdapter {  asteroid -> val action = MainFragmentDirections.actionShowDetail(asteroid)
            findNavController().navigate(action)

        }
        viewModel.allAsteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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
