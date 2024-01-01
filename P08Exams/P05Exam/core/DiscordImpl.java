package P08Exams.P05Exam.core;

import P08Exams.P05Exam.models.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DiscordImpl implements Discord {
    private Map<String, Message> data;

    public DiscordImpl() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public void sendMessage(Message message) {
        data.putIfAbsent(message.getId(), message);
    }

    @Override
    public boolean contains(Message message) {
        return data.containsKey(message.getId());
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Message getMessage(String messageId) {
        Message message = data.get(messageId);
        if (message == null){
            throw new IllegalArgumentException();
        }

        return message;
    }

    @Override
    public void deleteMessage(String messageId) {
        Message message = data.get(messageId);
        if (message == null){
            throw new IllegalArgumentException();
        }

        data.remove(messageId);
    }

    @Override
    public void reactToMessage(String messageId, String reaction) {
        Message message = data.get(messageId);
        if (message == null){
            throw new IllegalArgumentException();
        }

        message.getReactions().add(reaction);

    }

    @Override
    public Iterable<Message> getChannelMessages(String channel) {
        List<Message> result = data.values()
                .stream()
                .filter(element -> element.getChannel().equals(channel))
                .collect(Collectors.toList());
        if (result.isEmpty()){
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    public Iterable<Message> getMessagesByReactions(List<String> reactions) {
        List<Message> result = new ArrayList<>();

        for (Message message : data.values()) {
            if (new HashSet<>(message.getReactions()).containsAll(reactions)) {
                result.add(message);
            }
        }

        return result.stream()
                .sorted(Comparator
                        .comparing((Message message) -> message.getReactions().size()).reversed()
                        .thenComparing(Message::getTimestamp))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Message> getMessageInTimeRange(Integer lowerBound, Integer upperBound) {
        List<Message> result = data.values().stream()
                .filter(message -> message.getTimestamp() >= lowerBound && message.getTimestamp() <= upperBound)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Long> channelCounts = result.stream()
                .collect(Collectors.groupingBy(Message::getChannel, Collectors.counting()));

        result.sort(Comparator.comparingLong((Message message) -> channelCounts.get(message.getChannel()))
                .reversed());

        return result;
    }

    @Override
    public Iterable<Message> getTop3MostReactedMessages() {
        return data.values().stream()
                .sorted(Comparator.comparing(message -> message.getReactions().size(), Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getAllMessagesOrderedByCountOfReactionsThenByTimestampThenByLengthOfContent() {
        List<Message> sortedMessages = new ArrayList<>(data.values());

        sortedMessages.sort(Comparator
                .comparing((Message message) -> message.getReactions().size(),Comparator.reverseOrder())
                .thenComparing(Message::getTimestamp)
                .thenComparing(message -> message.getContent().length()));
        return sortedMessages;
    }
}
