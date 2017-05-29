package nl.jstege.adventofcode.aoccommon.utils

/**
 * Resource class
 * @author Jelle Stege
 */
class Resource {
    companion object {
        /**
         * Returns the contents of the file as a list of lines.
         *
         * @param filename The file to read
         * @return the contenst of the given filename as a list of lines,
         *         or an empty list if it does not exist.
         */
        @JvmStatic
        fun readLines(filename: String): List<String> =
                this::class.java.classLoader.getResourceAsStream(filename)
                        ?.bufferedReader()
                        ?.readLines()
                        ?: emptyList()

        @JvmStatic
        fun readLinesAsSequence(filename: String): Sequence<String> =
                this::class.java.classLoader.getResourceAsStream(filename)
                        ?.bufferedReader()
                        ?.readLines()?.asSequence()
                        ?: emptySequence()
    }
}