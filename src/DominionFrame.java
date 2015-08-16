import game_server.game.dominion.DominionCard;
import game_server.game.dominion.GameState;
import game_server.game.dominion.Turn;
import game_server.message.ChatMessage;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.JTextComponent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * Created by Andrew on 7/28/2015.
 */
 public class DominionFrame extends JFrame {

    private String username = "Andrew Gray";
    private Connection connection;

    protected void startFrame()
    {
        connection = new Connection(this);
        Thread startup = new Thread(connection);
        startup.start();
    }

    private JScrollPane playPane;
    private JScrollPane handPane;
    private JTextArea chat;
    private JTextArea chatInput;
    private GameState gameState;
    //private Connection connection;

    DominionFrame()
    {
        startFrame();//gtesting
        //GameState gameState = new GameState();

        ArrayList<DominionCard> hand = new ArrayList<DominionCard>(); // This should be cards
        //DominionCard lastDiscard = new DominionCard();
        boolean deckEmpty;
        Map<DominionCard, Integer> buyStack;
        Turn turnState;
        ArrayList<DominionCard> playedCards = new ArrayList<DominionCard>(); // this should be cards
        Border border1 = BorderFactory.createLineBorder(Color.darkGray, 1);


        // textarea for chat
        chat = new JTextArea();
        chat.setPreferredSize(new Dimension(275, 10));
        chat.setBorder(border1);
        chat.setBackground(Color.lightGray);
        //chat.;

        JScrollPane chatScrollPane = new JScrollPane(chat);
        chatScrollPane.setPreferredSize(new Dimension(300, 375));


        // textfield for chat input
        chatInput = new JTextArea("");
        chatInput.setPreferredSize(new Dimension(233, 25));
        chatInput.setBorder(border1);
        chatInput.setBackground(Color.lightGray);

        JButton sendChatButton = new JButton("Send");
        sendChatButton.setPreferredSize(new Dimension(67, 25));
        sendChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setOutput(chatInput.getText());
            }
        });

        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.setPreferredSize(new Dimension(300, 25));
        chatInputPanel.add(chatInput, BorderLayout.WEST);
        chatInputPanel.add(sendChatButton, BorderLayout.EAST);


        // panel to hold the chat things because I am bad at this
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(300, 400));
        chatPanel.setBorder(border1);

        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
        chatPanel.add(chatScrollPane, BorderLayout.NORTH);

        Font btnFont = new Font("SansSerif", Font.PLAIN, 25);

        JButton endTurnBtn = new JButton("End Turn");
        JButton buyCardBtn = new JButton("Buy Card");
        JButton connectBtn = new JButton("Connect to Server");
        JButton startGameBtn = new JButton("Start Game");

        endTurnBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        buyCardBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        connectBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        startGameBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        endTurnBtn.setFont(btnFont);
        buyCardBtn.setFont(btnFont);
        connectBtn.setFont(btnFont);
        startGameBtn.setFont(btnFont);

        startGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });



        // panel to hold the buttons
        JPanel buttons = new JPanel();
        BoxLayout boxLayout = new BoxLayout(buttons, BoxLayout.Y_AXIS);
        buttons.setLayout(boxLayout);
        buttons.setPreferredSize(new Dimension(300, 300));
        buttons.setBorder(border1);

        buttons.add(endTurnBtn);
        buttons.add(buyCardBtn);
        buttons.add(startGameBtn);
        buttons.add(connectBtn);


        // area that holds the phase info, actions buys money, buttons, chat
        JPanel turnArea = new JPanel(new BorderLayout());
        turnArea.setPreferredSize(new Dimension(300, 1080));
        turnArea.setBorder(border1);

        turnArea.add(chatPanel, BorderLayout.NORTH);
        turnArea.add(buttons, BorderLayout.SOUTH);

        // area that holds the cards that can be purchased
        JPanel buyArea = new JPanel(new GridBagLayout());
        buyArea.setPreferredSize(new Dimension(1620, 480));
        buyArea.setBorder(border1);

        // pane to display the played cards
        playPane = new JScrollPane();

        JPanel playingField = new JPanel(new GridBagLayout());
        playingField.setPreferredSize(new Dimension(1620, 300));
        playingField.setBorder(border1);
        playingField.add(playPane);

        // pane to display the hand
        handPane = new JScrollPane();

        // holds the hand
        JPanel handArea = new JPanel();
        playingField.setPreferredSize(new Dimension(1620, 300));
        playingField.setBorder(border1);
        handArea.add(handPane);

        // area that holds the hand area, played cards area, buy area
        JPanel playArea = new JPanel(new BorderLayout());
        playArea.setPreferredSize(new Dimension(1620, 1080));
        playArea.add(buyArea, BorderLayout.NORTH);
        playArea.add(handArea, BorderLayout.SOUTH);
        playArea.add(playingField, BorderLayout.SOUTH);


        // main panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(1920, 1080));
        panel.setBackground(Color.lightGray);

        panel.add(turnArea, BorderLayout.EAST);

        panel.add(playArea, BorderLayout.WEST);


        // frame that holds everything
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultLookAndFeelDecorated(true);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);


    }
    void setOutput(String text)
    {
        if (text.equals(""))
        {
            // if there is no text, don't do anything
        }
        else {
            connection.sendMessageObjectToServer(new ChatMessage(text, username));
            chat.append(username + ": " + text + "\n");
            chat.setPreferredSize(new Dimension(275, chat.getHeight() + 10));
            chat.setCaretPosition(chat.getDocument().getLength());
            chatInput.setText("");
            chatInput.requestFocus();
        }
    }

    void setHandPane(List<DominionCard> hand)
    {
        handPane = new JScrollPane();
        for(int i = 0; i < hand.size(); i++)
        {
            CardPanel card = new CardPanel(hand.get(i));
            handPane.add(card);
        }
    }

    void setPlayPane(List<DominionCard> hand)
    {
        playPane = new JScrollPane();
        for(int i = 0; i < hand.size(); i++)
        {
            CardPanel card = new CardPanel(hand.get(i));
            playPane.add(card);
        }
    }




    public void displayGameState()
    {
        setHandPane(gameState.getHand());
        setPlayPane(gameState.getPlayedCards());
    }

    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
        displayGameState();
    }
}