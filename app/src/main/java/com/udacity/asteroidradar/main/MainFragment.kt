package com.udacity.asteroidradar.main

import android.os.Bundle
import android.os.Debug
import android.os.StrictMode
import android.util.Log
import android.util.Log.d
import android.view.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.BaseApplication
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.PotD
import com.udacity.asteroidradar.databinding.FragmentMainBinding


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

        viewModel.picture.observe(viewLifecycleOwner) {PotD ->
            if (PotD != null && PotD.url != "") {
                Log.d("PotD", PotD.toString())
                getPictureOfTheDay(PotD.url)
                binding.activityMainImageOfTheDay.contentDescription = getString(R.string.nasa_picture_of_day_content_description_format, PotD.title)
            }
        }

        setHasOptionsMenu(true)

        viewModel.getPictureOfTheDay()

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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> viewModel.filterAsteroids("Week")
            R.id.show_rent_menu -> viewModel.filterAsteroids("Today")
            R.id.show_buy_menu -> viewModel.filterAsteroids("Week")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getPictureOfTheDay(imageUrl: String) {
        Picasso.with(binding.activityMainImageOfTheDay.context).load(imageUrl).fit().centerCrop()
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.ic_connection_error)
            .into(binding.activityMainImageOfTheDay)
    }
}