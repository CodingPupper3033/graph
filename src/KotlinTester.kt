import java.io.File

fun main() {
    var file = File("obama")

    println(file.extension == "")

    println(file.absolutePath)
}