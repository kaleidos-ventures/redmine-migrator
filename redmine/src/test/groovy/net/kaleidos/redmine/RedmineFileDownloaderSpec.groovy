package net.kaleidos.redmine

import spock.lang.Specification

class RedmineFileDownloaderSpec extends Specification {

    def 'download a resource from an attachment'() {
        given: 'a uri'
            def url =
                new File("src/test/resources/debian.jpg")
                    .toURI()
                    .toString()
        when: 'trying to download it'
            def result =
                new RedmineFileDownloader("https://lala","asdfasd")
                    .download(url)
        then: 'it should get a non empty array of bytes'
            result instanceof byte[]
            result.length > 0
    }

}
