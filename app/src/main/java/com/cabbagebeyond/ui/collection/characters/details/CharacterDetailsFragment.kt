package com.cabbagebeyond.ui.collection.characters.details

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentCharacterDetailsBinding
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.ui.collection.characters.CharacterType
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import org.koin.android.ext.android.inject


class CharacterDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = CharacterDetailsFragment()
    }

    private val _viewModel: CharacterDetailsViewModel
        get() = viewModel as CharacterDetailsViewModel

    private lateinit var _adapter: CharacterDetailsAdapter
    private lateinit var _binding: FragmentCharacterDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater)

        val character = CharacterDetailsFragmentArgs.fromBundle(requireArguments()).character
        val isEditingActive = CharacterDetailsFragmentArgs.fromBundle(requireArguments()).startEditing

        val dataSource: CharacterDataSource by inject()
        val raceDataSource: RaceDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = CharacterDetailsViewModel(
            character, isEditingActive, dataSource, raceDataSource, worldDataSource, UserService.currentUser, requireActivity().application
        )

        _binding.viewModel = _viewModel
        _binding.lifecycleOwner = this

        _adapter = CharacterDetailsAdapter({
            _viewModel.expandHeader(it)
        }, {
            _viewModel.collapseHeader(it)
        }, {
            _viewModel.show(it)
        })
        _binding.readGroup.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(ArrayList(it))
            }
        }

        _viewModel.fabImage.observe(viewLifecycleOwner) {
            it?.let {
                _binding.floatingActionButton.setImageResource(it)
            }
        }

        _viewModel.isEditing.observe(viewLifecycleOwner) {
            it?.let { isEditing ->
                toggleVisibility(_binding.readGroup.root, _binding.editGroup.root, isEditing)
            }
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        _viewModel.types.observe(viewLifecycleOwner) {
            setupTypeSpinner(it.selected, it.values)
        }

        _viewModel.races.observe(viewLifecycleOwner) {
            setupRaceSpinner(it.selected, it.values)
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(it.selected, it.values)
        }

        setupChart()
        setHasOptionsMenu(true)

        return _binding.root
    }


    private fun setupChart() {

        val chart = _binding.readGroup.attributeChart

        // chart.setBackgroundColor(resources.getColor(android.R.color.holo_blue_bright))
        chart.description.isEnabled = false
        chart.webLineWidth = 1f
        chart.webColor = resources.getColor(R.color.primaryColor)
        chart.webLineWidthInner = 1f
        chart.webColorInner = resources.getColor(R.color.primaryDarkColor)
        chart.webAlpha = 100
        chart.isRotationEnabled = false

        setData()
        chart.animateY(1400, Easing.EaseInOutQuad)

        val xAxis = chart.xAxis
        xAxis.typeface = Typeface.MONOSPACE
        xAxis.textSize = 14f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        xAxis.valueFormatter = MyXAxisValueF(arrayOf("STR", "DEX", "WIL", "INT", "CON"))

        xAxis.textColor = resources.getColor(R.color.primaryTextColor)


        val yAxis: YAxis = chart.yAxis
        yAxis.typeface = Typeface.MONOSPACE
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 4f // Dice 4
        yAxis.axisMaximum = 12f // Dice 12
        yAxis.setDrawLabels(false)


        chart.legend.isEnabled = false
    }

    private fun setData() {
        val character = _viewModel.character.value!!

        val attributes = mutableListOf<String>()
        attributes.add(character.strength)
        attributes.add(character.dexterity)
        attributes.add(character.willpower)
        attributes.add(character.intelligence)
        attributes.add(character.constitution)

        val entries: List<RadarEntry> = attributes.map {
            val diceValue = it.replace("W", "")
            val value = diceValue.toFloat()
            RadarEntry(value)
        }

        val set = RadarDataSet(entries, "").also {
            it.color = resources.getColor(R.color.secondaryColor)
            it.lineWidth = 2f
            it.fillColor = resources.getColor(R.color.secondaryColor)
            it.fillAlpha = 180
            it.setDrawFilled(true)
            it.isDrawHighlightCircleEnabled = true
            it.setDrawHighlightIndicators(false)
        }

        val sets = listOf(set)

        val data = RadarData(sets).also {
            it.setValueTypeface(Typeface.MONOSPACE)
            it.setValueTextSize(8f)
            it.setDrawValues(false)
            it.setValueTextColor(resources.getColor(R.color.black))
        }

        _binding.readGroup.attributeChart.data = data
        _binding.readGroup.attributeChart.invalidate()
    }

    class MyXAxisValueF(private val mValues: Array<String>) : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            return mValues[value.toInt()]
        }
    }

    private fun setupTypeSpinner(preSelection: CharacterType?, types: List<CharacterType>) {
        setupSpinner(preSelection?.title, types.map { it.title }, _binding.editGroup.typeSpinner) { index ->
            _viewModel.onTypeSelected(types[index])
        }
    }

    private fun setupRaceSpinner(preSelection: Race?, races: List<Race>) {
        setupSpinner(preSelection?.name, races.map { it.name }, _binding.editGroup.spinnerRace) { index ->
            _viewModel.onRaceSelected(races[index])
        }
    }
    private fun setupWorldSpinner(preSelection: World?, worlds: List<World?>) {
        setupSpinner(preSelection?.name ?: "", worlds.mapNotNull { it?.name }, _binding.editGroup.worldSpinner) {
            _viewModel.onWorldSelected(worlds[it])
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(
            CharacterDetailsFragmentDirections.actionCharacterDetailsToOcr(
                _viewModel.properties
            )
        )
    }
}
