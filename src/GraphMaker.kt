import java.io.File

class GraphMaker {
    companion object {
        val SEPERATOR = ":"
        val LINKS_SEPERATOR = ","

        // String specifically
        private fun getValueFromLine(line: String): String {
            return if (line.contains(SEPERATOR)) line.split(SEPERATOR)[0].trim() else return line.trim()
        }

        private fun getConnectionsFromLine(line: String): List<String> {
            if (line.contains(SEPERATOR)) {
                var string: String = line.split(SEPERATOR)[1]
                string = string.replace("\\s".toRegex(), "")

                return if (string.isNotEmpty()) string.split(LINKS_SEPERATOR) else ArrayList()
            } else {
                return ArrayList()
            }
        }

        fun addStringGraphFromString(graph: Graph<String>, input: String): Graph<String> {
            var out: Graph<String> = graph

            // Add Front First
            for (line in input.lines()) {
                var value: String = getValueFromLine(line)
                out.vertices.add(Vertex(value))
            }

            // Go Back and connect
            var lineOn = 1
            for (line in input.lines()) {
                var value: String = getValueFromLine(line)
                var connections = getConnectionsFromLine(line)

                for(connection: String in connections) {
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

        fun addStringGraphFromFile(graph: Graph<String>, path: String): Graph<String> {
            var file = File(path)
            try {
                return addStringGraphFromString(graph, file.readText())
            } catch (error: NullPointerException) {
                throw NullPointerException("File: " + file.name + ", " + error.message)
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
        fun addIntGraphFromString(graph: Graph<Int>, input: String): Graph<Int> {
            var out: Graph<Int> = graph

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

        fun makeIntGraphFromString(input: String): Graph<Int> {
            return addIntGraphFromString(Graph<Int>(), input)
        }

        fun addIntGraphFromFile(graph: Graph<Int>, path: String): Graph<Int> {
            var file = File(path)
            try {
                return addIntGraphFromString(graph, file.readText())
            } catch (error: NullPointerException) {
                throw NullPointerException("File: " + file.name + ", " + error.message)
            }
        }

        fun makeIntGraphFromFile(path: String): Graph<Int> {
            return addIntGraphFromFile(Graph(),path)
        }

        fun <E> makeStringFromGraph(input: Graph<E>): String {
            var out = StringBuilder()

            for (vertex in input.vertices) {
                // Value
                out.append(vertex)

                // Connections
                if (vertex.connectionsToString().isNotEmpty()) out.append(SEPERATOR)
                out.append(vertex.connectionsToString())

                out.append('\n')
            }

            if (input.vertices.isNotEmpty()) out.replace(out.length-1, out.length, "")
            return out.toString()
        }

        fun <E> makeFileFromGraph(input: Graph<E>, name: String, path: String = "") {
            var nameFormatted = name
            if (!name.contains(".")) nameFormatted = name + ".grph"

            var file = File(path + nameFormatted)
            file.writeText(GraphMaker.makeStringFromGraph(input))
        }
    }
}

fun main() {
    var graph: Graph<String> = Graph(false, false,false)
    GraphMaker.addStringGraphFromFile(graph,"J:\\Programming\\Kotlin\\kotlinTest5\\src\\graphExamples\\graph1.grph")

    println(graph)

    println("Depth: \t " + graph.depthFirstSearch())
    println("Breadth: " + graph.breadthFirstSearch())

    for (i in 0 until graph.vertices.size) {
        println("D: " + graph.vertices[0] + " " + graph.vertices[i])
        println("E: " + graph.breadthFirstSearch(graph.vertices[0], graph.vertices[i]))
    }

    println("F: " + graph.breadthFirstSearch(graph.vertices[0], null))
}