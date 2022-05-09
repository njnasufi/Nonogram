package nonogram;

import java.awt.BorderLayout;
import java.awt.evetn.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame implements ActionListener { 
    Main main;
    final Canvas canvas;
    final JLabel label;
    JPanel panel, buttonPanel;
    JButton startButton, loadButton;
    JTextField textfield;

    public Window(Main main){
        super("Nonogramet me Algoritem Gjenetik");
        this.main = main;
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas();
        getContentPane().add(canvas, BorderLayout.CENTER);

        panel = new JPanel();
        getContentPane().add(panel, BoredLayout.SOUTH);

        label = new JLabel("------Shtyp Start per te filluar------");
        panel.add(label, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(400, 50));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setActionCommand("start");
        startButton.setEnabled(false);
        buttonPanel.add(startButton, BorderLayout.WEST);

        loadButton = new JButton("Ngarko");
        loadButton.addActionListener(this);
        loadButton.setActionCommand("load");
        buttonPanel.add(loadButton, BorderLayout.CENTER);

        setPreferredSize(new Dimension(600, 600));
        pack();
        setVisible(true);

    }

    public void setNonogram(Nonogram nonogram){
        canvas.setNonogram(nonogram);
        pack();
    }

    public Nonogram getNonogram(){
        return canvas.getNonogram();
    }
}
