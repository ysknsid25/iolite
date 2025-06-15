package inorin

interface ValueObject {
    fun parse(): Any
    fun safeParse(): Result<Any> {
        return try {
            Result.success(parse())
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        }
    }
}
