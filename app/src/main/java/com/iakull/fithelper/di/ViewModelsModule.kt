package com.iakull.fithelper.di

import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.common.choose_program.ChooseProgramViewModel
import com.iakull.fithelper.ui.common.create_exercise.CreateExerciseViewModel
import com.iakull.fithelper.ui.common.create_post.CreatePostViewModel
import com.iakull.fithelper.ui.common.detailed_exercise.DetailedExerciseViewModel
import com.iakull.fithelper.ui.common.exercises.ExercisesViewModel
import com.iakull.fithelper.di.feed.FeedViewModel
import com.iakull.fithelper.di.feed.program_day_exercises.PublicProgramDayExercisesViewModel
import com.iakull.fithelper.di.feed.program_days.PublicProgramDaysViewModel
import com.iakull.fithelper.ui.guide.SearchViewModel
import com.iakull.fithelper.ui.guide.people.PeopleViewModel
import com.iakull.fithelper.ui.guide.public_programs.PublicProgramsViewModel
import com.iakull.fithelper.ui.home.HomeViewModel
import com.iakull.fithelper.ui.home.calendar_day.CalendarDayViewModel
import com.iakull.fithelper.ui.home.programs.create_program.CreateProgramViewModel
import com.iakull.fithelper.ui.home.programs.create_program_day.CreateProgramDayViewModel
import com.iakull.fithelper.ui.home.programs.exercises.ProgramDayExercisesViewModel
import com.iakull.fithelper.ui.home.programs.program_days.ProgramDaysViewModel
import com.iakull.fithelper.ui.home.trainings.add_training_set.AddTrainingSetViewModel
import com.iakull.fithelper.ui.home.trainings.create_training.CreateTrainingViewModel
import com.iakull.fithelper.ui.home.trainings.exercises.TrainingExercisesViewModel
import com.iakull.fithelper.ui.home.trainings.training_sets.TrainingSetsViewModel
import com.iakull.fithelper.ui.profile.edit_profile.EditProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { SharedViewModel(get(), get()) }
    viewModel { AddTrainingSetViewModel(get(), get()) }
    viewModel { CalendarDayViewModel(get()) }
    viewModel { ChooseProgramViewModel(get()) }
    viewModel { CreatePostViewModel(get(), get(), get()) }
    viewModel { CreateProgramViewModel(get()) }
    viewModel { CreateProgramDayViewModel(get()) }
    viewModel { CreateTrainingViewModel(get(), get(), get(), get(), get()) }
    viewModel { DetailedExerciseViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { ExercisesViewModel(get(), get(), get(), get()) }
    viewModel { FeedViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { PeopleViewModel() }
    viewModel { ProgramDayExercisesViewModel(get(), get()) }
    viewModel { ProgramDaysViewModel(get(), get()) }
    viewModel { PublicProgramsViewModel(get()) }
    viewModel { PublicProgramDaysViewModel(get(), get()) }
    viewModel { PublicProgramDayExercisesViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { TrainingExercisesViewModel(get(), get(), get()) }
    viewModel { TrainingSetsViewModel(get(), get(), get()) }
    viewModel { CreateExerciseViewModel(get(), get(), get()) }
}