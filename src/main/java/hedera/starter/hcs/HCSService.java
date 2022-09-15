package hedera.starter.hcs;

import com.hedera.hashgraph.sdk.*;
import hedera.starter.utilities.EnvUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Service
public class HCSService {

    /**
     * Create Topic
     * @return String
     */
    public String createTopic() throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        Client client = Client.forTestnet();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        var tx = new TopicCreateTransaction()
                .setAdminKey(Objects.requireNonNull(EnvUtils.getOperatorKey().getPublicKey()))
                .execute(client);
        TopicId topicId = tx.getReceipt(client).topicId;

        return topicId != null ? topicId.toString() : "Topic creation failed.";
    }

    /**
     * Create Topic with TopicMemo
     * @return String
     */
    public String createTopicMemo(String topicMemo) throws TimeoutException, PrecheckStatusException,
            ReceiptStatusException {
        Client client = Client.forTestnet();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        var tx = new TopicCreateTransaction()
                .setAdminKey(EnvUtils.getOperatorKey().getPublicKey())
                .setTopicMemo(topicMemo)
                .execute(client);
        TopicId topicId = tx.getReceipt(client).topicId;

        return topicId != null ? topicId.toString() : "Topic creation failed.";
    }

    /**
     * Delete Topic
     * @return boolean
     */
    public boolean deleteTopic(String topicId) throws TimeoutException, PrecheckStatusException,
            ReceiptStatusException {
        Client client = Client.forTestnet();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        var tx = new TopicDeleteTransaction()
                .setTopicId(TopicId.fromString(topicId))
                .execute(client)
                .getReceipt(client);
        return true;
    }


    /**
     * Get Topic information
     * @return ConsensusTopicInfo
     */
    public TopicInfo getTopicInfo(String topicId) throws TimeoutException, PrecheckStatusException {
        Client client = Client.forTestnet();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        TopicInfo info = new TopicInfoQuery()
                .setTopicId(TopicId.fromString(topicId))
                .execute(client);
        return info;
    }

    /**
     * Subscribe to messages on the topic, printing out the received message and metadata as it is published by the
     * @return boolean
     */
    public boolean subscribeToTopic(String topicId) {
        Client client = Client.forTestnet();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        new TopicMessageQuery()
                .setTopicId(TopicId.fromString(topicId))
                .setStartTime(Instant.ofEpochSecond(0))
                .subscribe(client, response -> {
                            String messageAsString = new String(response.contents, StandardCharsets.UTF_8);
                            System.out.println(response.consensusTimestamp + " received topic message: " +
                                    messageAsString);
                        });
        return true;
    }

    /**
     * Submit messages on the topic
     * @return boolean
     */
    public boolean submitMessage(String topicId, String message) throws PrecheckStatusException, TimeoutException,
            ReceiptStatusException {
        Client client = Client.forTestnet();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        System.out.println("Submitting message to " + topicId);
        new TopicMessageSubmitTransaction()
                .setTopicId(TopicId.fromString(topicId))
                .setMessage(message)
                .execute(client)
                .getReceipt(client);
        return true;
    }
}
