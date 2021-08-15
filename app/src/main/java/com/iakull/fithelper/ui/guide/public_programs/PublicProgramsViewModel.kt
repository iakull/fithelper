package com.iakull.fithelper.ui.guide.public_programs

import androidx.lifecycle.ViewModel
import com.iakull.fithelper.data.repository.ProgramRepository

class PublicProgramsViewModel(repository: ProgramRepository) : ViewModel() {

    val programs = repository.publicPrograms()
}