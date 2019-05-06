package de.hwse.kmatrix

interface Vector<R>: Matrix<R, S1> where R: MSize


class ArrayVector<R>(override val rowCount: R, filler: (Int)->Double): AbstractMatrix<R, S1>(), Vector<R>
    where R: MSize {

    private val array: DoubleArray = DoubleArray(rowCount.size, filler)

    constructor(rowCount: R, filler: Double): this(rowCount, {filler})

    override val columnCount: S1
        get() = S1

    override fun get(row: Int, column: Int): Double {
        return array[row]
    }
}

fun <R> vectorOf(rowCount: R, vararg nrs: Double): Vector<R> where R: MSize {
    assert(rowCount.size == nrs.size) { "row count did not match vararg count" }
    return ArrayVector(rowCount) {r -> nrs[r]}
}

fun <R> fillVector(rowCount: R, filler: Double): Vector<R> where R: MSize {
    return ArrayVector(rowCount, filler)
}