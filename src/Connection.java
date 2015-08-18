/**
 * Created by Andrew on 7/28/2015.
 */

import game_server.game.GameType;
import game_server.game.dominion.CardMessage;
import game_server.game.dominion.DominionMessage;
import game_server.game.dominion.GameStateMessage;
import game_server.game.dominion.TurnMessage;
import game_server.game.message.StatusMessage;
import game_server.message.Message;
import game_server.message.TimerMessage;

import java.io.*;
import java.net.Socket;
public class Connection implements Runnable {

    private Socket dominionConnection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private DominionFrame frame;
    private Message incomingMessage;
    public final static String username = "AndrewGray";
    boolean startSent = false;

    public Connection(DominionFrame frame)
    {
        this.frame = frame;
        //run();
    }

    @Override
    public void run()
    {
        System.out.println("Run");
        try
        {
            dominionConnection = new Socket("localhost", 8989);

            this.output = new ObjectOutputStream(new BufferedOutputStream(dominionConnection.getOutputStream()));

            sendStringToServer(username);


            //Connection to the server is to be accomplished by creating a connection and then sending a UTF string with a username.
             //   This will be responded to by the server in an ACKNOWLEDGE or DENY message (DENY only if the username passed is already in use).
            //All messages except the initial username are sent using the writeObject and readObject methods of the ObjectOutputStream and ObjectInputStream classes.
            this.input = new ObjectInputStream(new BufferedInputStream(dominionConnection.getInputStream()));

            boolean startAck = false;
            boolean startReceived = false;
            boolean timerReceived = false;
            boolean connected = false;

            while (dominionConnection.isConnected() && !(dominionConnection.isClosed()))
            {
                try
                {
                    incomingMessage = (Message) input.readObject();
                    if (incomingMessage.getMessageType().equals(Message.Type.ACKNOWLEDGE) && !connected)
                    {
                        frame.setOutputSansUsername("Connected.");
                        connected = true;
                    }
                    else if (incomingMessage.getMessageType().equals(Message.Type.DENY) && !connected)
                    {
                        frame.setOutputSansUsername("Connection denied. Username already in use.");
                    }
                    else if (incomingMessage.getMessageType().equals(Message.Type.ACKNOWLEDGE) && connected && startSent)
                    {
                        startAck = true;
                    }
                    else if (incomingMessage.getMessageType().equals(Message.Type.TIMER) && connected && startAck)
                    {
                        frame.setOutputSansUsername("Game will begin shortly.");
                    }
                    else if (incomingMessage.getMessageType().equals(Message.Type.START_GAME) && connected && startAck)
                    {
                        startReceived = true;
                    }
                    // > username
                    // < acknowledge/deny
                    // > start game
                    // <acknowledge/deny
                    // < timer
                    // < start game
                    // < timer
                    // < game
                    else
                    {
                        if (incomingMessage.getMessageType().equals(Message.Type.GAME))
                        {
                            if (incomingMessage instanceof DominionMessage)
                            {
                                if (incomingMessage.getUsername().equals(null)) // if it has a null username it is for this player
                                {
                                    if (((DominionMessage) incomingMessage).getType().equals(DominionMessage.DominionType.START_TURN))
                                    {
                                        int i = 0;
                                    }
                                }
                                {
                                     frame.setGameState(((GameStateMessage) incomingMessage).getGameState());
                                }
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void terminate()
    {
        try
        {
            input.close();
        }
        catch( IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendStringToServer(String message)
    {
        try
        {
            output.writeUTF(message);
            output.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessageObjectToServer(Message message)
    {
        try
        {
            output.writeObject(message);
            output.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendStartGameMessage()
    {
        sendMessageObjectToServer(StatusMessage.getStartMessage(GameType.DOMINION, username));
        startSent = true;
        frame.setOutputSansUsername("Starting game...");
        // well StatusMessage is still not visible in the .jar but apparently if I hard code it in it works?
    }



}
