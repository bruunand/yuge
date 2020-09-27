package dk.anderslangballe.utility

import java.nio.file.Paths

fun getSamplePath(sampleName: String): String {
    return Paths.get("samples", sampleName).toString()
}