package de.hwse.kmatrix

import junit.framework.TestCase

class MatrixOperationTest: TestCase() {

    /**
     * plus operator must add matrix element wise
     */
    fun testAddition() {
        val firsts = 1..100
        val seconds = 1..100
        for (first in firsts) {
            for (second in seconds) {
                val a = fillMatrix(S2, S2, first.toDouble())
                val b = fillMatrix(S2, S2, second.toDouble())
                val sum = a + b
                assertEquals(fillMatrix(S2, S2, first.toDouble() + second.toDouble()), sum)
            }
        }
    }

    fun testMatrixMultiplication() {
        val a = matrixOf(S2, S3,
            listOf(3.0, 2.0, 1.0),
            listOf(1.0, 0.0, 2.0))

        val b = matrixOf(S3, S2,
            listOf(1.0, 2.0),
            listOf(0.0, 1.0),
            listOf(4.0, 0.0))

        val product = a * b

        val expectedProduct = matrixOf(S2, S2,
            listOf(7.0,8.0),
            listOf(9.0,2.0))

        assertEquals(expectedProduct, product)
    }
}