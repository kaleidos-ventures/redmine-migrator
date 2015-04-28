package net.kaleidos.redmine

import com.taskadapter.redmineapi.RedmineManagerFactory

final class RedmineClientFactory {

    public static RedmineClient newInstance(String host, String apiKey) {
        def httpConfig =
            RedmineManagerFactory.createLongTermConfiguration(
                RedmineManagerFactory.createInsecureConnectionManager(),
                10000, // timeout
                10000 // eviction check
            )

        return new RedmineClientImpl(
            redmineFileDownloader: new RedmineFileDownloader(httpConfig.client, host, apiKey),
            redmineManager: RedmineManagerFactory.createWithApiKey(host, apiKey, httpConfig)
        )
    }

}
