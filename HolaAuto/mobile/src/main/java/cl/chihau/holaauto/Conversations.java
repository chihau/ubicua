package cl.chihau.holaauto;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple class that denotes unread conversations and messages. In a real world application,
 * this would be replaced by a content provider that actually gets the unread messages to be
 * shown to the user.
 */
public class Conversations {

    /**
     * Set of strings used as messages by the sample.
     */
    private static final String[] MESSAGES = new String[]{
            "¿Estás en casa?",
            "¿Puedes llamarme devuelta?",
            "Hola!",
            "No olvides comprar la leche a la vuelta",
            "¿Terminaste el proyecto?",
            "¿Terminaste la aplicación?",
            "Pasa al supermercado por favor",
            "Llámame si puedes"
    };

    /**
     * Senders of all the messages.
     */
    public static final String SENDER_NAME = "John Doe";

    /**
     * A static conversation Id for all our messages.
     */
    public static final int CONVERSATION_ID = 13;

    private Conversations() {
    }

    public static String getUnreadMessage() {
        return MESSAGES[ThreadLocalRandom.current().nextInt(0, MESSAGES.length)];
    }
}

