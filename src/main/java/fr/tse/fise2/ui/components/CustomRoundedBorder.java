package fr.tse.fise2.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

/**
 * Classe permettant de créer une bordure arrondie
 * Source: https://stackoverflow.com/questions/15025092/how-to-make-a-rounded-border-with-java-swing
 */
public class CustomRoundedBorder extends AbstractBorder {
    private int radius;
    private Color color;

    public CustomRoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int value = radius / 2;
        return new Insets(value, value, value, value);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        int value = radius / 2;
        insets.top = insets.left = insets.bottom = insets.right = value;
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));

        int adjustedWidth = width - 1;
        int adjustedHeight = height - 1;

        g2.drawRoundRect(x, y, adjustedWidth, adjustedHeight, radius, radius);
        g2.dispose();
    }
}
