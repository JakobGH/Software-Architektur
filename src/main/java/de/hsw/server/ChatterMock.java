package de.hsw.server;

import de.hsw.shared.IChatter;

public class ChatterMock implements IChatter {

    private final String username;

    public ChatterMock(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println(message);
    }
}
