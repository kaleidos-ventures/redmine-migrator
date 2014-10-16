package net.kaleidos.redmine

import com.taskadapter.redmineapi.RedmineManagerFactory

final class RedmineClientFactory {

    public static RedmineClient newInstance(String host, String apiKey) {
        return new RedmineClientImpl(
            redmineFileDownloader: new RedmineFileDownloader(apiKey),
            redmineManager: RedmineManagerFactory.createWithApiKey(host, apiKey)
        )
    }

}
