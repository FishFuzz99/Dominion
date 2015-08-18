import game_server.game.dominion.DominionCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
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

    private String source;
    private DominionFrame frame;
    private DominionCard card;
    //private BufferedImage image;



    CardPanel(DominionCard card, DominionFrame frame, String source)
    {
        this.frame = frame;
        this.source = source;
        this.card = card;
        String path = "C:\\Users\\Andrew\\Documents\\cs3230_final_client\\";
        path += card.getName() + ".jpg";



        setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon(path);
        JLabel label = new JLabel();
        label.setBounds(0, 0, 100, 300);
        label.setIcon(image);

        add(label, BorderLayout.CENTER);

        //ImageIcon image = new ImageIcon("C:\\Users\\Andrew\\Documents\\cs3230_final_client\\Copper.jpg");
        //JLabel label = new JLabel();
        //label.setBounds(0,0,100,300);
        //label.setIcon(image);
//
        //buyArea.add(label, BorderLayout.CENTER);

    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() >= 2)
        {
            // depending on phase, do stuff
            frame.doCardEvents(card, source);
        }
    }

    //@Override
    //public void paintComponent(Graphics g)
    //{
    //    super.paintComponent(g);
    //    g.drawImage(image, 0, 0, null);
    //}

}
