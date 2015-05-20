package net.kaleidos.redmine

import groovy.util.logging.Log4j
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.HttpClient


@Log4j
class RedmineFileDownloader {

    private final String apiKey
    private final String host
    private final String EMPTY = ''
    private final String FILE = 'file'
    private final String HTTP_FIX_PATTERN = "http[s]{0,1}"
    private final HttpClient client

    RedmineFileDownloader(HttpClient client, String host, String apiKey) {
        this.apiKey = apiKey
        this.host = host
        this.client = client
    }

    byte[] download(String uri) {
        def isLocal = new URL(uri).protocol == FILE
        def apiKeyUri = uri + (isLocal ? EMPTY : "?key=$apiKey")
        def fixedProtocolUri =
            isLocal ?
                apiKeyUri :
                apiKeyUri.replaceAll(
                    HTTP_FIX_PATTERN,
                    URI.create(host).scheme)

        def get      = new HttpGet(fixedProtocolUri)
        def response = client.execute(get)
        def entity   = response.entity
        def out      = new ByteArrayOutputStream()

        log.debug("Downloading attachment $fixedProtocolUri")

        entity.writeTo(out)

        return out.toByteArray()
    }

}
