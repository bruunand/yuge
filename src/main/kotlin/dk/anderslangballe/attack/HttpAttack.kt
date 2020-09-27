package dk.anderslangballe.attack

import java.io.File
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

class HttpAttack(private val targetHost: String, private val targetPort: Int, private val numRequests: Int,
                 private val sampleFile: String) {
    private val launchLatch = CountDownLatch(numRequests)
    private val completeLatch = CountDownLatch(numRequests)

    init {
        println("Initialized attack with $numRequests requests")
    }

    private fun getRequestContent(): String {
        val content = File(sampleFile).readText(Charsets.UTF_8)

        // Ensure connection stays alive
        return content.replace("Connection: close", "Connection: keep-alive")
    }

    fun start() {
        val requestContent = getRequestContent()
        val secureConnection = targetPort == 443

        for (i in 1..numRequests) {
            thread {
                HttpRequest(targetHost, targetPort, secureConnection, launchLatch, completeLatch, requestContent).start()
            }
        }

        launchLatch.await()

        println("Finished writing initial payload")

        val startTime = System.currentTimeMillis()

        completeLatch.await()

        println("RPS: ${numRequests / ((System.currentTimeMillis() - startTime) / 1000F)}")
    }
}