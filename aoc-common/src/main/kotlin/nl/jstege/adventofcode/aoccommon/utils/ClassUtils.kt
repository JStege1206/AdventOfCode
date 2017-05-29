package nl.jstege.adventofcode.aoccommon.utils

import org.reflections.Reflections

/**
 *
 * @author Jelle Stege
 */
object ClassUtils {
    inline fun <reified E> getClassesFromPackage(pack: String): List<Class<out E>> {
        return Reflections(pack)
                .getSubTypesOf(E::class.java)
                .filterNotNull()
                .sortedBy { it.simpleName }
                .toList()
    }
}