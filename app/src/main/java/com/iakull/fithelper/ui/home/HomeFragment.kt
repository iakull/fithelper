package com.iakull.fithelper.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.iakull.fithelper.AUTHORITY
import com.iakull.fithelper.data.remote.generateId
import com.iakull.fithelper.databinding.FragmentProgressBinding
import com.iakull.fithelper.ui.common.ProgramsAdapter
import com.iakull.fithelper.ui.home.HomeFragmentDirections.Companion.toCalendarDayDialog
import com.iakull.fithelper.ui.home.HomeFragmentDirections.Companion.toChooseProgramDayFragment
import com.iakull.fithelper.ui.home.HomeFragmentDirections.Companion.toCreateProgramDialog
import com.iakull.fithelper.ui.home.HomeFragmentDirections.Companion.toCreateTrainingDialog
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.toIsoString
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalTime
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.io.File
import java.io.IOException
import java.util.*

private const val RC_TAKE_PHOTO = 1

class HomeFragment : Fragment() {

    private val vm: HomeViewModel by viewModel()

    private lateinit var binding: FragmentProgressBinding

    private val calendarMonthBinder by lazy { CalendarMonthBinder() }
    private val calendarDayBinder by lazy {
        CalendarDayBinder {
            val dateString = it.atTime(LocalTime.NOON).toIsoString()
            navigate(toCalendarDayDialog(dateString))
        }
    }


    private val programsAdapter by lazy { ProgramsAdapter { navigate(toChooseProgramDayFragment(it.id)) } }

    private var lastPhotoId = ""
    private var lastPhotoUri = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProgressBinding.inflate(inflater)
        binding.vm = vm
        binding.programsRV.adapter = programsAdapter
        setupCalendar()
        binding.lifecycleOwner = this
        return binding.root
    }

    @SuppressLint("DefaultLocale")
    private fun setupCalendar() {
        binding.apply {
            calendarView.apply {
                dayBinder = calendarDayBinder
                monthHeaderBinder = calendarMonthBinder
                monthScrollListener = { month ->
                    val formatter = DateTimeFormatter.ofPattern("LLLL yyyy г.")
                    dateLabel.text = month.yearMonth.format(formatter).capitalize()
                }

                val currentMonth = YearMonth.now()
                val firstMonth = YearMonth.of(2000, 1)
                val lastMonth = currentMonth.plusMonths(1)
                val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
                setup(firstMonth, lastMonth, firstDayOfWeek)
                scrollToMonth(currentMonth)
            }

            prevMonthButton.setOnClickListener {
                calendarView.findFirstVisibleMonth()?.let {
                    calendarView.smoothScrollToMonth(it.yearMonth.previous)
                }
            }

            nextMonthButton.setOnClickListener {
                calendarView.findFirstVisibleMonth()?.let {
                    calendarView.smoothScrollToMonth(it.yearMonth.next)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.startTrainingButton.setOnClickListener { navigate(toCreateTrainingDialog(null)) }
        binding.programsLabel.setOnClickListener { navigate(toCreateProgramDialog()) }

        vm.trainings.observe(viewLifecycleOwner) {
            calendarDayBinder.submitList(it)
            binding.calendarView.notifyCalendarChanged()
        }
        vm.programs.observe(viewLifecycleOwner) { programsAdapter.submitList(it) }
    }

    private fun dispatchImageCaptureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        val photoFile: File? = try {
            lastPhotoId = generateId()
            createTempFile(lastPhotoId, ".jpg", context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        } catch (ex: IOException) {
            null
        }
        photoFile?.let { file ->
            val photoURI = FileProvider.getUriForFile(requireContext(), AUTHORITY, file)
            lastPhotoUri = photoURI.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, RC_TAKE_PHOTO)
        }
    }
}