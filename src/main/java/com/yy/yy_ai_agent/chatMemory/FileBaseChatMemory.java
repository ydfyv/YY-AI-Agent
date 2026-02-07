package com.yy.yy_ai_agent.chatMemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 阿狸
 * @date 2026-02-07
 */
public class FileBaseChatMemory implements ChatMemory {

    private final String BASE_DIR;
    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);

        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        kryo.register(ArrayList.class);
        kryo.register(UserMessage.class);
        kryo.register(AssistantMessage.class);
        kryo.register(SystemMessage.class);
    }

    public FileBaseChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        conversationMessages.addAll(messages);
        saveConversation(conversationId, conversationMessages);
    }

    @Override
    public List<Message> get(String conversationId) {
        List<Message> allMessages = getOrCreateConversation(conversationId);
        return allMessages.stream()
                .skip(Math.max(0, allMessages.size() - 3))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        File conversationFile = getConversationFile(conversationId);
        if (conversationFile.exists()) {
            conversationFile.delete();
        }
    }

    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try {
                Input input = new Input(new FileInputStream(file));
                messages = kryo.readObject(input, ArrayList.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return messages;
    }

    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }

    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try {
            kryo.writeObject(new Output(new FileOutputStream(file)), messages);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
