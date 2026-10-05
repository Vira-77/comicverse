package fr.tse.fise2.ui.panels;

import java.util.List;

import javax.swing.JPanel;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicsCharacter;

/**
 * Interface pour la vue principale
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public interface MainView {
	void updateContentPanel(JPanel panel);
	void refreshAfterLogin();
	JPanel createCharactersSearchResultsPanel(List<ComicsCharacter> characters);
	JPanel createComicsSearchResultsPanel(List<Comic> comics);
	void showError(String message);
}