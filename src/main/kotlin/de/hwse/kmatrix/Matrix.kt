package de.hwse.kmatrix

interface Matrix<R, C> where R: MSize, C: MSize {

    val rowCount: R
    val columnCount: C

    operator fun get(row: Int, column: Int): Double

    operator fun unaryMinus(): Matrix<R,C>

    operator fun plus(other: Double): Matrix<R,C>

    operator fun plus(other: Matrix<R, C>): Matrix<R, C>

    operator fun minus(other: Double): Matrix<R,C>

    operator fun minus(other: Matrix<R, C>): Matrix<R, C>

    operator fun times(other: Double): Matrix<R, C>

    operator fun <T> times(other: Matrix<C, T>): Matrix<R, T> where T: MSize

    fun transpose(): Matrix<C, R>

    val T: Matrix<C, R> get() = transpose()

}

fun Matrix<S1,S1>.toDouble(): Double {
    return this[0,0]
}

abstract class AbstractMatrix<R, C>: Matrix<R, C>
        where R: MSize, C: MSize {
    override operator fun unaryMinus(): Matrix<R,C> {
        return ArrayMatrix(rowCount, columnCount) {r,c -> -this[r,c]}
    }

    override operator fun plus(other: Double): Matrix<R,C> {
        return ArrayMatrix(rowCount, columnCount) {r,c -> this[r,c] + other}
    }

    override operator fun plus(other: Matrix<R, C>): Matrix<R, C> {
        return ArrayMatrix(rowCount, columnCount) {r,c -> this[r,c] + other[r,c]}
    }

    override operator fun minus(other: Double): Matrix<R, C> {
        return ArrayMatrix(rowCount, columnCount) {r,c -> this[r,c] - other}
    }

    override operator fun minus(other: Matrix<R, C>): Matrix<R, C> {
        return ArrayMatrix(rowCount, columnCount) {r,c -> this[r,c] - other[r,c]}
    }

    override operator fun times(other: Double): Matrix<R, C> {
        return ArrayMatrix(rowCount, columnCount) { r, c -> this[r,c] * other}
    }

    override operator fun <T> times(other: Matrix<C, T>): Matrix<R, T> where T: MSize {
        return ArrayMatrix(rowCount, other.columnCount) {r,c ->
            (0 until columnCount.size).map { j -> this[r,j] * other[j,c] }.sum()
        }
    }

    override fun transpose(): Matrix<C, R> {
        return TransposedMatrix(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractMatrix<*, *>) return false
        if (this.rowCount != other.rowCount || this.columnCount != other.columnCount) return false
        for (row in 0 until this.rowCount.size)
            for (col in 0 until this.columnCount.size)
                if (this[row,col] != other[row,col]) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}

class ArrayMatrix<R, C>(override val rowCount: R,
                        override val columnCount: C,
                        filler: (Int, Int) -> Double): AbstractMatrix<R, C>()
        where R: MSize, C: MSize {

    private val array: Array<DoubleArray>

    init {
        array = Array(rowCount.size) { row -> DoubleArray(columnCount.size){ col -> filler(row, col) } }
    }

    constructor(rowCount: R, columnCount: C, filler: Double):
            this(rowCount, columnCount, {_, _ -> filler})

    override operator fun get(row: Int, column: Int): Double {
        if (row < 0 || row >= rowCount.size || column < 0 || column >= columnCount.size)
            throw IndexOutOfBoundsException("index ($row,$column) can not be accessed in Matrix of " +
                    "size (${rowCount.size}, ${columnCount.size})")
        return array[row][column]
    }

    override fun toString(): String {
        return "[" + array.joinToString(separator = "|") { row -> row.joinToString() } + "]"
    }

}

class TransposedMatrix<R, C>(private val base: Matrix<C, R>): AbstractMatrix<R,C>() where R: MSize, C: MSize {
    override val rowCount: R
        get() = base.columnCount
    override val columnCount: C
        get() = base.rowCount
    override fun get(row: Int, column: Int): Double {
        return base[column, row]
    }
    override fun transpose(): Matrix<C, R> {
        return base
    }
}

class SingleValueMatrix<R,C>(override val rowCount: R,
                             override val columnCount: C,
                             private val nr: Double): AbstractMatrix<R,C>()
        where R: MSize, C: MSize {
    override fun get(row: Int, column: Int): Double {
        return nr
    }

}

fun <R,C> matrixOf(rowCount: R, columnCount: C, vararg rows: List<Double>): Matrix<R,C> where R: MSize, C: MSize {
    assert(rowCount.size == rows.size) { "rows vararg must have same size as rowCount" }
    assert(rows.all { row -> row.size == columnCount.size }) { "all rows must have same size as columnCount" }
    return ArrayMatrix(rowCount, columnCount) {row, col -> rows[row][col]}
}

fun <R,C> fillMatrix(rowCount: R, columnCount: C, filler: (Int, Int) -> Double): Matrix<R,C> where R: MSize, C: MSize  {
    return ArrayMatrix(rowCount, columnCount, filler)
}

fun <R,C> fillMatrix(rowCount: R, columnCount: C, filler: Double): Matrix<R, C> where R: MSize, C: MSize {
    return SingleValueMatrix(rowCount, columnCount, filler)
}