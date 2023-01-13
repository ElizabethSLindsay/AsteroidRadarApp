package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        binding.asteroid = asteroid

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        binding.distanceFromEarth.text = asteroid.distanceFromEarth.toString()
        binding.relativeVelocity.text = asteroid.relativeVelocity.toString()
        binding.estimatedDiameter.text = asteroid.estimatedDiameter.toString()
        binding.absoluteMagnitude.text = asteroid.absoluteMagnitude.toString()
        binding.closeApproachDate.text = asteroid.closeApproachDate.toString()

        if (asteroid.isPotentiallyHazardous) {
            binding.activityMainImageOfTheDay.setImageResource(R.drawable.ic_status_potentially_hazardous)
            binding.activityMainImageOfTheDay.contentDescription = getString(R.string.potentially_hazardous_asteroid_image)
        } else {
            binding.activityMainImageOfTheDay.setImageResource(R.drawable.ic_status_normal)
            binding.activityMainImageOfTheDay.contentDescription = getString(R.string.not_hazardous_asteroid_image)
        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
