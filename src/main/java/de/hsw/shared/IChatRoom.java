package de.hsw.shared;

public interface IChatRoom {

    void joinRoom(IChatter chatter);
    void writeMessage(IChatter chatter, String message);
    void leave(IChatter chatter);
}