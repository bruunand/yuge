package dk.anderslangballe.attack

import dk.anderslangballe.ssl.getSecureSocketFactory
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.CountDownLatch

class HttpRequest(private val targetHost: String, private val targetPort: Int, private val secureConnection: Boolean,
                  private val launchLatch: CountDownLatch, private val completeLatch: CountDownLatch,
                  private val requestContent: String) {
    private val withheldBytes = 1

    fun start() {
        val socket: Socket = getSocket()
        val outputStream = socket.getOutputStream()
        val requestBytes = getRequestBytes()

        writeInitial(requestBytes, outputStream)

        launchLatch.await()

        writeFinal(requestBytes, outputStream)
    }

    private fun getRequestBytes(): ByteArray = requestContent.toByteArray(Charsets.UTF_8)

    private fun writeFinal(requestBytes: ByteArray, outputStream: OutputStream) {
        outputStream.write(requestBytes, requestBytes.size - 1, withheldBytes)
        outputStream.close()

        completeLatch.countDown()
    }

    private fun writeInitial(requestBytes: ByteArray, outputStream: OutputStream) {
        outputStream.write(requestBytes, 0, requestBytes.size - withheldBytes)
        outputStream.flush()

        launchLatch.countDown()
    }

    private fun getSocket(): Socket {
        val socket: Socket = if (secureConnection) {
            getSecureSocketFactory().createSocket(targetHost, targetPort)
        } else {
            Socket(targetHost, targetPort)
        }

        socket.tcpNoDelay = true

        return socket
    }
}