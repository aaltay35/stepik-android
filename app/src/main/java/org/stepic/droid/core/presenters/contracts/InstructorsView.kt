package org.stepic.droid.core.presenters.contracts

import org.stepic.droid.model.User

interface InstructorsView {
    fun showInstructors(instructors: List<User>)
}
