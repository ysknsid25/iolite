package inorin

interface StringValueObject {
    fun get(): String
    fun toInt(): Int {
        val value = get()
        return value.toInt()
    }
}
