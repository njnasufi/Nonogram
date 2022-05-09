package nonogram;
import java.awt.*;
import javax.swing.*;

import nonogram.Nonogram;

import java.util.*;


public class Canvas extends JPanel{

    Nonogram nonogram;
    Solution solution;
    int SQ_WIDTH = 20;
    int LABEL_WIDTH = 60;
    int LABEL_HEIGHT = 70;

    public void setNonogram(Nonogram nonogram){
        this.nonogram = nonogram;
        solution = null;

        int gridWidth = SQ_WIDTH * nonogram.getColumnHeaders().size();
        int gridHeight = SQ_WIDTH * nonogram.getRowHeaders().size();

        int panelWidth = gridWidth + LABEL_WIDTH;
        int panelHeight = gridHeight + LABEL_HEIGHT;

        setPreferredSize(new Dimension(panelWidth, panelHeight));
        repaint();

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getBounds().width, getBounds().height);

        if(nonogram != null){

            int xMax = LABEL_WIDTH + nonogram.getColumnHeaders().size() * SQ_WIDTH;
            int yMax = LABEL_HEIGHT + nonogram.getRowHeaders().size() * SQ_WIDTH;

            int x = LABEL_WIDTH;
            int y = LABEL_HEIGHT;

            if(solution != null){
                g.setColor(Color.BLACK);
                for (int i = 0; i < nonogram.getColumnHeaders().size(); i++ ){

                    for (int j = 0; j < nonogram.getRowHeaders().size(); j++){

                        if(solution.getA){}
                    }
                }
            }


        }
    }



    

    public Nonogram getNonogram(){
        return nonogram;
    }
}
