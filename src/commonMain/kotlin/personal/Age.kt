package iolite.personal

import iolite.ValueObject

@JvmInline
value class Age(private val value: Int) : ValueObject<Int> {

    override fun parse(): Int {
        require(value in MIN_AGE..MAX_AGE) {
            "Invalid age: $value. Age must be between $MIN_AGE and $MAX_AGE."
        }
        return value
    }

    companion object {
        const val MIN_AGE = 0
        const val MAX_AGE = 200 // I hope this number keeps getting bigger
    }
}
