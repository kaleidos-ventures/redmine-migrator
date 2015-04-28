package net.kaleidos.redmine

import com.taskadapter.redmineapi.RedmineManagerFactory

final class RedmineClientFactory {

    public static RedmineClient newInstance(String host, String apiKey) {
        def httpConfig =
            RedmineManagerFactory.createLongTermConfiguration(
                RedmineManagerFactory.createInsecureConnectionManager(),
                10000,
                10000
            )
        return new RedmineClientImpl(
            redmineFileDownloader: new RedmineFileDownloader(host, apiKey),
            redmineManager: RedmineManagerFactory.createWithApiKey(host, apiKey, httpConfig)
        )
    }

}
