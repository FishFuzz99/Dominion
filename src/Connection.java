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

            sendStringToServer("Andrew Gray");


            //Connection to the server is to be accomplished by creating a connection and then sending a UTF string with a username.
             //   This will be responded to by the server in an ACKNOWLEDGE or DENY message (DENY only if the username passed is already in use).
            //All messages except the initial username are sent using the writeObject and readObject methods of the ObjectOutputStream and ObjectInputStream classes.
            this.input = new ObjectInputStream(new BufferedInputStream(dominionConnection.getInputStream()));

            boolean startReceived = false;
            boolean timerReceived = false;

            while (dominionConnection.isConnected() && !(dominionConnection.isClosed()))
            {
                try
                {
                    incomingMessage = (Message) input.readObject();
                    if (incomingMessage.getMessageType().equals(Message.Type.ACKNOWLEDGE))
                    {
                        frame.setOutput("Connected.");
                    }
                    else if (incomingMessage.getMessageType().equals(Message.Type.DENY))
                    {
                        frame.setOutput("Connection denied. Username already in use.");
                    }
                    else
                    {
                        if (incomingMessage.getMessageType().equals(Message.Type.GAME))
                        {
                            if (incomingMessage instanceof DominionMessage)
                            {
                                if (incomingMessage instanceof GameStateMessage)
                                {
                                     frame.setGameState(((GameStateMessage) incomingMessage).getGameState());
                                }
                                else if (incomingMessage instanceof TurnMessage)
                                {

                                }
                                //else if (incomingMessage instanceof StartMessage)
                                //{
                                //    startReceived = true;
                                //}
                            }
                        }
                    }
                    if (startReceived && incomingMessage instanceof TimerMessage)
                    {
                        timerReceived = true;
                    }
                    if (startReceived && timerReceived && incomingMessage instanceof StatusMessage)
                    {
                        startReceived = false;
                    }
                    if (!startReceived && timerReceived && incomingMessage instanceof TimerMessage)
                    {
                        // game is starting
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
        // well StatusMessage is still not visible in the .jar but apparently if I hard code it in it works?
    }



}
