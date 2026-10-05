package fr.tse.fise2.ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Classe pour personnaliser l'apparence de la barre de défilement
 * 
 * @version 1.0
 * @author Vivien KIM et Samuel PLET
 */
public class CustomScrollBarUI extends BasicScrollBarUI {

	@Override
	protected void configureScrollBarColors() {
		this.thumbColor = Color.CYAN;
		this.trackColor = Color.LIGHT_GRAY;
	}

	@Override
	protected void installComponents() {
		super.installComponents();
		scrollbar.setUnitIncrement(16);
	}

	/**
	 * Méthode statique pour créer un JScrollPane avec une scrollbar personnalisée
	 * @param panel le panel à afficher dans le JScrollPane
	 * @return le JScrollPane créé
	 */
	public static JScrollPane createScrollPane(JPanel panel) {
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize(new Dimension(400, 300));

		// Personnaliser la barre de défilement
		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		verticalScrollBar.setUI(new CustomScrollBarUI());

		return scrollPane;
	}
}