package dk.anderslangballe

import dk.anderslangballe.attack.HttpAttack
import dk.anderslangballe.utility.getSamplePath
import java.io.File
import kotlin.system.exitProcess

fun main() {
    val targetHost = System.getenv("TARGET_HOST")
    val targetPort = System.getenv("TARGET_PORT")
    val requestFile = System.getenv("REQUEST_FILE")
    val numRequests = System.getenv("NUMBER_OF_REQUESTS")

    if (targetHost == null || targetPort == null) {
        println("Target host and port must be declared")

        exitProcess(1)
    }

    if (requestFile == null || !File(getSamplePath(requestFile)).exists()) {
        println("Request file must be declared and exist")

        exitProcess(1)
    }

    if (numRequests == null) {
        println("Number of requests must be declared")

        exitProcess(1)
    }

    HttpAttack(targetHost, Integer.parseInt(targetPort), Integer.parseInt(numRequests), getSamplePath(requestFile)).start()
}
