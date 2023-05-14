package xyz.larkyy.packets;

public enum State {

    HANDSHAKING(0),
    STATUS(1),
    LOGIN(2),
    PLAY(3);

    private final int id;

    State(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static State getState(int id) {
        switch (id) {
            case 1 -> {
                return STATUS;
            }
            case 2 -> {
                return LOGIN;
            }
            case 3 -> {
                return PLAY;
            }
            default -> {
                return HANDSHAKING;
            }
        }
    }
}
