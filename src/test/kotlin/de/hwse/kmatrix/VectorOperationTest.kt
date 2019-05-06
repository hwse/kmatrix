package de.hwse.kmatrix

import junit.framework.TestCase

class VectorOperationTest: TestCase() {

    fun testMultiplication() {
        val a = fillVector(S3, 1.0)
        val b = fillVector(S3, 2.0)
        assertEquals(6.0, (a.T * b).toDouble())
    }

}