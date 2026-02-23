package net.jerrin.demo.socketstream.model;

public record MessageRecord(String id, String content) implements KafkaRecord {}
