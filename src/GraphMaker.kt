import java.io.File

class GraphMaker {
    companion object {
        // String specifically
        private fun getValueFromLine(line: String): String {
            return if (line.contains(":")) line.split(":")[0].trim() else return line.trim()
        }

        private fun getConnectionsFromLine(line: String): List<String> {
            if (line.contains(":")) {
                var string: String = line.split(":")[1]
                string = string.replace("\\s".toRegex(), "")

                return if (string.isNotEmpty()) string.split(",") else ArrayList()
            } else {
                return ArrayList()
            }
        }


        // Int specifically
        private fun getIntConnectionsFromLine(line: String): List<Int> {
            var out: ArrayList<Int> = ArrayList()

            for (string: String in getConnectionsFromLine(line)) {
                out.add(string.toInt())
            }

            return out
        }

        private fun getIntValueFromLine(line: String): Int {
            return getValueFromLine(line).toInt()
        }


        // Int String -> Graph
        fun makeIntGraphFromString(input: String): Graph<Int> {
            var out: Graph<Int> = Graph()

            // Add Front First
            for (line in input.lines()) {
                var value: Int = getIntValueFromLine(line)
                out.vertices.add(Vertex(value))
            }

            // Go Back and connect
            var lineOn = 1
            for (line in input.lines()) {
                var value: Int = getIntValueFromLine(line)
                var connections = getIntConnectionsFromLine(line)

                for(connection: Int in connections) {
                    var sourceVertex = out.getVertexFromVale(value)
                    var connectionVertex = out.getVertexFromVale(connection)

                    if (sourceVertex != null && connectionVertex != null) {
                        out.addEdge(sourceVertex, connectionVertex)
                    } else {
                        throw NullPointerException("Line $lineOn: No existing node with value: $connection")
                    }
                }
                lineOn++
            }

          return out
        }

        fun makeIntGraphFromFile(path: String): Graph<Int> {
            var file = File(path)
            try {
                return makeIntGraphFromString(file.readText())
            } catch (error: NullPointerException) {
                throw NullPointerException("File: " + file.name + ", " + error.message)
            }
        }

        fun <E> makeStringFromGraph(input: Graph<E>): String {
            var out = StringBuilder()

            for (vertex in input.vertices) {
                // Value
                out.append(vertex)

                // Connections
                if (vertex.connectionsToString().isNotEmpty()) out.append(":")
                out.append(vertex.connectionsToString())

                out.append('\n')
            }

            if (input.vertices.isNotEmpty()) out.replace(out.length-1, out.length, "")
            return out.toString()
        }
    }
}

fun main() {
    var graph: Graph<Int> = GraphMaker.makeIntGraphFromFile("J:\\Programming\\Kotlin\\kotlinTest5\\src\\graphExamples\\graph0.grph")

    println(graph)
}