/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.med.imii.MappingGUI;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author matesn
 */
public class ColorCellRenderer extends DefaultListCellRenderer {

    HashMap<Integer, Color> backgroundColorMap = new HashMap<Integer, Color>();
    HashMap<Integer, Color> fontColorMap = new HashMap<Integer, Color>();

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        Color backgroundColor = backgroundColorMap.get(index);
        Color fontColor = fontColorMap.get(index);


        if (backgroundColor != null && !isSelected) {
            c.setBackground(backgroundColor);
        }
        if (fontColor != null && isSelected) {
            //c.setForeground(fontColor);
        }
               
        return c;
    }

    void setBackgroundColor(int index, Color color) {
        backgroundColorMap.put(index, color);
    }

    void setFontColor(int index, Color color) {
         fontColorMap.put(index, color);
    }
}
