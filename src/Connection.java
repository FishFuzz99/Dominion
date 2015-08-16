/**
 * Created by Andrew on 7/28/2015.
 */

import game_server.game.dominion.CardMessage;
import game_server.game.dominion.DominionMessage;
import game_server.game.dominion.GameStateMessage;
import game_server.game.dominion.TurnMessage;
import game_server.message.Message;

import java.io.*;
import java.net.Socket;
public class Connection implements Runnable {

    private Socket dominionConnection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private DominionFrame frame;
    private Message incomingMessage;


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

            while (dominionConnection.isConnected() && !(dominionConnection.isClosed()))
            {
                try
                {
                    incomingMessage = (Message) input.readObject();
                    if (incomingMessage.getUsername().equals("Client"))
                    {
                        if (incomingMessage.getMessageType().equals(Message.Type.ACKNOWLEDGE)) {
                            // good to go

                        } else if (incomingMessage.getMessageType().equals(Message.Type.DENY)) {
                            // username already taken
                        }
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
                            }
                        }
                    }
                }
                catch(Exception e)
                {

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

    }



}
