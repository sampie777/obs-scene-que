package mocks

import java.awt.Color
import java.awt.Dimension
import java.awt.Point

object ConfigMock {
    var stringProperty1: String = "stringValue1"
    var stringProperty2: String = "stringValue2"
    var longProperty1: Long = 100
    var hashMapProperty1: HashMap<String, Int> = HashMap()
    var nullableColorProperty1: Color? = null
    var pointProperty1: Point = Point(0, 0)
    var dimensionProperty1: Dimension = Dimension(10, 10)
}