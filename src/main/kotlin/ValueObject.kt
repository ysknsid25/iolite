package inorin

interface ValueObject<T> {
    fun parse(): T
    fun safeParse(): Result<T> {
        return try {
            Result.success(parse())
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        }
    }
}
