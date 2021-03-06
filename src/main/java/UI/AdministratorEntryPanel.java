package UI;

import javax.swing.*;
import java.awt.*;

class AdministratorEntryPanel extends GeneralPanel
{
    protected AdministratorEntryPanel()
    {
        /*
            Initiate the change value panel given the UI design

            This pages requires following components:
            (1) label 1: JLabel, display the current user ID
            (2) button_1: JButton, display "Log out"
            (3) button_2: JButton, display "Back"
            (4) button_3: JButton, display "Manage Account"
            (5) button_4: JButton, display "Manage Log File"
         */
        label_1=setLabel("User ID: ",false);

        button_1=setButton("Log out",true);
        button_2=setButton("Back",true);
        button_3=setButton("Manage Account",true);
        button_4=setButton("Setting/Log File",true);
        //Set the panel for labels and buttons
        JPanel userPanel=new JPanel(new FlowLayout(FlowLayout.TRAILING,44,0));
        userPanel.add(label_1);

        JPanel buttonPanel_1=new JPanel(new FlowLayout(FlowLayout.TRAILING,44,0));
        buttonPanel_1.add(button_1);

        JPanel buttonPanel_2=new JPanel(new FlowLayout(FlowLayout.LEADING,44,0));
        buttonPanel_2.add(button_2);
        //Set the north panel in the border layout
        JPanel northPanel=new JPanel(new GridLayout(9,1));
        northPanel.add(new JLabel(""));
        northPanel.add(buttonPanel_2);
        northPanel.add(userPanel);
        northPanel.add(buttonPanel_1);
        northPanel.add(new JLabel(""));
        northPanel.add(new JLabel(""));
        northPanel.add(new JLabel(""));
        northPanel.add(new JLabel(""));
        northPanel.add(new JLabel(""));
        //Set the content panel which display buttons at a preferred size and position
        JPanel contentPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,200,0));
        button_3.setPreferredSize(new Dimension(250,180));
        button_4.setPreferredSize(new Dimension(250,180));
        contentPanel.add(button_3);
        contentPanel.add(button_4);
        //Set the central panel to display content at the central area
        JPanel centralPanel=new JPanel(new GridLayout(3,1));
        centralPanel.add(new JLabel(""));
        centralPanel.add(contentPanel);
        centralPanel.add(new JLabel(""));
        //Add panels to the administrator entry page
        add(northPanel,BorderLayout.NORTH);
        add(centralPanel,BorderLayout.CENTER);
    }
}
