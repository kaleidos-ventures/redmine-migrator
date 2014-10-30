package net.kaleidos.redmine

import groovy.util.logging.Log4j

@Log4j
class RedmineFileDownloader {

    private final String apiKey
    private final String host
    private final String EMPTY = ''
    private final String FILE = 'file'
    private final String HTTP_FIX_PATTERN = "http[s]{0,1}"

    RedmineFileDownloader(String host, String apiKey) {
        this.apiKey = apiKey
        this.host = host
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

        def url = new URL(fixedProtocolUri)

        log.debug("Downloading attachment $url")

        return url.bytes
    }

}
