package dk.anderslangballe.attack

import java.io.File
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

class HttpAttack(private val targetHost: String, private val targetPort: Int, private val sampleFile: String) {
    private val numberOfRequests = 1000

    private val launchLatch = CountDownLatch(numberOfRequests)
    private val completeLatch = CountDownLatch(numberOfRequests)

    private fun getRequestContent(): String {
        val content = File(sampleFile).readText(Charsets.UTF_8)

        // Ensure connection stays alive
        return content.replace("Connection: Close", "Connection: Keep-Alive")
    }

    fun start() {
        val requestContent = getRequestContent()

        for (i in 1..numberOfRequests) {
            thread {
                HttpRequest(targetHost, targetPort, false, launchLatch, completeLatch, requestContent).start()
            }
        }

        launchLatch.await()

        val startTime = System.currentTimeMillis()

        completeLatch.await()

        println("RPS: ${numberOfRequests / ((System.currentTimeMillis() - startTime) / 1000F)}")
    }
}