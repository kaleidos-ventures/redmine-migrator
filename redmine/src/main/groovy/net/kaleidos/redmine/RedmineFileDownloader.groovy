package net.kaleidos.redmine

import groovy.util.logging.Log4j

@Log4j
class RedmineFileDownloader {

    private final String apiKey
    private final String EMPTY = ''
    private final String FILE = 'file'

    RedmineFileDownloader(String apiKey) {
        this.apiKey = apiKey
    }

    byte[] download(String uri) {
        def isLocal = new URL(uri).protocol == FILE
        def url = new URL(uri + (isLocal ? EMPTY : "?key=$apiKey"))

        log.debug("Downloading attachment $url")

        return url.bytes
    }

}
