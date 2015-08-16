import game_server.game.dominion.DominionCard;

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

/**
 * Created by Andrew on 8/13/2015.
 */
public class CardPanel extends JPanel
{

   //CardPanel(DominionCard card)
   //{
   //    ImageIcon image = new ImageIcon();
   //    JLabel label = new JLabel();
   //    JPanel theCard = new JPanel();

   //    image = new ImageIcon(path + card.getName());
   //    label = new JLabel(image);
   //    theCard.add(label);
   //    // using the cards id or something, make a frame for it
   //}

    String source;
    DominionFrame frame;
    DominionCard card;

    CardPanel(DominionCard card, DominionFrame frame, String source)
    {
        this.card = card;
        String path = "C:\\Users\\Andrew\\Documents\\cs3230_final_client\\";
        ImageIcon image = new ImageIcon();
        JLabel label = new JLabel();
        JPanel cardPanel = new JPanel();
        this.frame = frame;
        this.source = source;

        image = new ImageIcon(path + card.getName());
        label = new JLabel(image);
        cardPanel.add(label);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() >= 2)
        {
            // depending on phase, do stuff
            frame.doCardEvents(card, source);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {

    }

}
