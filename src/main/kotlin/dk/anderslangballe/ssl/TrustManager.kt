package dk.anderslangballe.ssl

import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

private class TrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        return null
    }

    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
}

fun getSecureSocketFactory(): SSLSocketFactory {
    val context = SSLContext.getInstance("TLS")
    context.init(null, arrayOf<javax.net.ssl.TrustManager>(TrustManager()), null)

    return context.socketFactory
}