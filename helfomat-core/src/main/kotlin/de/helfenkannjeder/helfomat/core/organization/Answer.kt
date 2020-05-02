package de.helfenkannjeder.helfomat.core.organization

/**
 * @author Valentin Zickner
 */
enum class Answer {
    YES, MAYBE, NO;

    val neighbours: List<Answer>
        get() = when (this) {
            YES -> listOf(MAYBE)
            MAYBE -> listOf(YES, NO)
            NO -> listOf(MAYBE)
        }

}