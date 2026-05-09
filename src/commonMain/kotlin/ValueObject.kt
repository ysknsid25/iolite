package iolite

interface ValueObject<T> {
    fun parse(): T
    fun safeParse(): Result<T> {
        return try {
            Result.success(parse())
        } catch (e: IoliteException) {
            Result.failure(e)
        }
    }
}
