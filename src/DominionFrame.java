import game_server.game.blackjack.CardMessage;
import game_server.game.dominion.*;
import game_server.message.ChatMessage;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
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
    private JTextField remainingBuys;
    private JTextField remainingActions;
    private JTextField remainingGold;
    private JTextField phase;
    //private Connection connection;

    DominionFrame()
    {
        //gtesting
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
        chat.setWrapStyleWord(true);
        chat.setLineWrap(true);
        chat.setEditable(false);

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
                connection.sendStartGameMessage();
            }
        });

        endTurnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connection.sendMessageObjectToServer(TurnMessage.getEndTurn());
            }
        });

        connectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame();
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

        remainingBuys = new JTextField("Buys");
        remainingActions = new JTextField("Actions");
        remainingGold = new JTextField("Gold");
        phase = new JTextField("Phase");

        remainingBuys.setEditable(false);
        remainingActions.setEditable(false);
        remainingGold.setEditable(false);
        phase.setEditable(false);




        JPanel remainingPanel = new JPanel(new BorderLayout());
        remainingPanel.add(remainingActions, BorderLayout.NORTH);
        remainingPanel.add(remainingBuys, BorderLayout.CENTER);
        remainingPanel.add(remainingGold, BorderLayout.SOUTH);

        JPanel turnPanel = new JPanel(new BorderLayout());
        turnPanel.setPreferredSize(new Dimension(100, 100));
        turnPanel.add(remainingPanel, BorderLayout.NORTH);
        turnPanel.add(phase, BorderLayout.SOUTH);


        // area that holds the phase info, actions buys money, buttons, chat
        JPanel turnArea = new JPanel(new BorderLayout());
        turnArea.setPreferredSize(new Dimension(300, 1080));
        turnArea.setBorder(border1);

        turnArea.add(chatPanel, BorderLayout.NORTH);
        turnArea.add(turnPanel, BorderLayout.CENTER);
        turnArea.add(buttons, BorderLayout.SOUTH);



        // area that holds the cards that can be purchased
        JPanel buyArea = new JPanel(new BorderLayout());
        buyArea.setPreferredSize(new Dimension(1620, 480));
        buyArea.setBorder(border1);

        // pane to display the played cards
        playPane = new JScrollPane();
        playPane.setPreferredSize(new Dimension(1400, 300));

        JPanel playingField = new JPanel(new GridBagLayout());
        playingField.setPreferredSize(new Dimension(1620, 300));
        playingField.setBorder(border1);
        playingField.add(playPane);

        //DominionCard copper = new DominionCard("copper", 0, 0, 0, 0, 1, 0, DominionCard.CardType.TREASURE);
        // the constructor of the DominionCard is private so I can't test this

        // pane to display the hand
        handPane = new JScrollPane();
        handPane.setPreferredSize(new Dimension(1400, 300));

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

    void setOutputSansUsername(String text)
    {
        if (text.equals(""))
        {

        }
        else
        {
            chat.append(text + "\n");
            chat.setPreferredSize(new Dimension(275, chat.getHeight() + 10));
            chat.setCaretPosition(chat.getDocument().getLength());
            chatInput.requestFocus();
        }
    }

    void setHandPane(List<DominionCard> hand)
    {
        handPane.removeAll();
        for(int i = 0; i < hand.size(); i++)
        {
            // add all teh cards in the hand to the pane
            //handPane.add(new CardPanel(hand.get(i), this, "hand"));
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(100,300));

            String path = "C:\\Users\\Andrew\\Documents\\cs3230_final_client\\";
            path += hand.get(i).getName() + ".jpg";

            ImageIcon image = new ImageIcon(path);
            JLabel label = new JLabel();
            label.setBounds(0, 0, 100, 300);
            label.setIcon(image);

            panel.add(label, BorderLayout.CENTER);

            handPane.add(panel);
        }
    }

    void setPlayPane(List<DominionCard> playedCards)
    {

        playPane.removeAll();
        for(int i = 0; i < playedCards.size(); i++)
        {
            // add all of the cards to the pane
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(100,300));

            String path = "C:\\Users\\Andrew\\Documents\\cs3230_final_client\\";
            path += playedCards.get(i).getName() + ".jpg";

            ImageIcon image = new ImageIcon(path);
            JLabel label = new JLabel();
            label.setBounds(0, 0, 100, 300);
            label.setIcon(image);

            panel.add(label, BorderLayout.CENTER);

            playPane.add(panel);
        }
    }

    void setDiscard(DominionCard lastDiscard)
    {

    }

    void setDeck(boolean isDeckEmpty)
    {

    }

    void setBuyStack(Map<DominionCard, Integer> buyStack)
    {

    }

    void setPhase(DominionPlayer.Phase phase)
    {
        this.phase.setText("Phase: " + phase.toString());
    }

    void setTurnNumbers(int buys, int actions, int gold)
    {
        remainingActions.setText("Actions: " + Integer.toString(actions));
        remainingBuys.setText("Buys:     " + Integer.toString(buys));
        remainingGold.setText("Gold:     " + Integer.toString(gold));
    }

    void setTurnState(Turn turnState)
    {
        setPhase(turnState.getPhase());
        setTurnNumbers(turnState.getBuysRemaining(), turnState.getActionsRemaining(), turnState.getGoldRemaining());
    }

    public void displayGameState()
    {
        setHandPane(gameState.getHand());
        setPlayPane(gameState.getPlayedCards());
        setDiscard(gameState.getLastDiscard());
        setDeck(gameState.isDeckEmpty());
        setBuyStack(gameState.getBuyStack());
        setTurnState(gameState.getTurnState());
    }

    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
        displayGameState();
    }

    public void doCardEvents(DominionCard card, String source)
    {
        game_server.game.dominion.CardMessage.CardAction action;
        if (source.equals("hand"))
        {
            action = game_server.game.dominion.CardMessage.CardAction.PLAY;
            if (card.getCardType().equals(DominionCard.CardType.ACTION))
            {
                if (gameState.getTurnState().getPhase().equals(DominionPlayer.Phase.ACTION))
                {
                    if (gameState.getTurnState().getActionsRemaining() > 0)
                    {
                        // if  it comes from your hand, is an action, and you have actions remaining, play the card
                        //connection.sendMessageObjectToServer(new CardMessage(card, action, username));
                    }
                }
            }
            if (card.getCardType().equals(DominionCard.CardType.TREASURE)) {
                // if it comes from your hand and is a treasure card, play the card
                //connection.sendMessageObjectToServer(new CardMessage(card, action, username));
            }
        }
        else if (source.equals("play")) {
            // if its in the playing field don't do anything
        }
        else if (source.equals("buy"))
        {
            action = game_server.game.dominion.CardMessage.CardAction.GAIN;
            if (card.getCost() > gameState.getTurnState().getGoldRemaining() || gameState.getTurnState().getBuysRemaining() < 1)
            {
                //if they have the money and the buys, buy the card
                //connection.sendMessageObjectToServer(new CardMessage(card, action, username));
            }
        }
    }
}
