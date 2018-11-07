package nl.jstege.adventofcode.aoccommon.utils

import java.io.File
import kotlin.streams.asSequence

/**
 * Resource class, loads resources from the classpath.
 * @author Jelle Stege
 */
class Resource {
    companion object {
        /**
         * Returns the contents of the file as a sequence of lines.
         *
         * @param filename The file to read.
         * @return the contents of the given filename as a sequence of lines,
         * or an empty sequence if it does not exist.
         */
        fun readLinesAsSequence(filename: String): Sequence<String> =
            this::class.java.classLoader.getResourceAsStream(filename)
                ?.bufferedReader()
                ?.readLines()?.asSequence()
                    ?: emptySequence()

        /**
         * Lists all files in the given path.
         * @param path String The directory to read files from
         * @return List<String> A list of all files, with their path prepended.
         */
        fun getResourceFiles(path: String): Sequence<String> =
            this::class.java.classLoader.getResourceAsStream(path)?.let { stream ->
                stream
                    .bufferedReader()
                    .lines()!!
                    .map { "$path${File.separator}$it" }
                    .asSequence()
            } ?: emptySequence()
    }
}

