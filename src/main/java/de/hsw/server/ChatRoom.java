package de.hsw.server;

import de.hsw.shared.IChatRoom;
import de.hsw.shared.IChatter;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom implements IChatRoom {

    private final List<IChatter> currentChatters;

    public ChatRoom() {
        currentChatters = new ArrayList<>();
    }

    @Override
    public synchronized void joinRoom(IChatter chatter) {
        if (currentChatters.contains(chatter)) {
            throw new RuntimeException("User is already in this chatroom!");
        }
        currentChatters.add(chatter);
        writeMessage(chatter, "I joined the chatroom!");
    }

    @Override
    public synchronized void writeMessage(IChatter chatter, String message) {
        currentChatters.forEach(c -> {
            if (!c.equals(chatter)) {
                c.receiveMessage(chatter.getUsername() + ": " + message);
            } else {
                c.receiveMessage("Me: " + message);
            }
        });
    }

    @Override
    public synchronized void leave(IChatter chatter) {
        currentChatters.remove(chatter);
        writeMessage(chatter, "Left the chat");
    }
}