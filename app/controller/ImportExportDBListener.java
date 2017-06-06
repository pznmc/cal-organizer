package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import view.CalendarView;

import model.EventManager;
import model.ImportExportChooser;

public class ImportExportDBListener implements ActionListener {
	EventManager eventManager;
	ImportExportChooser choose;
	CalendarView calendarView;
	
	public ImportExportDBListener(EventManager eventManager, CalendarView calendarView, ImportExportChooser choose) {
		this.eventManager = eventManager;
		this.choose = choose;
		this.calendarView = calendarView;
	}

	public void actionPerformed(ActionEvent e) {
		
		if (this.eventManager.getEventList().size() == 0 && choose == ImportExportChooser.EXPORT) {
			JOptionPane.showMessageDialog(null,
				    "Brak wydarzeń do wyeksportowania!",
				    "Ostrzeżenie!",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		JFileChooser fileChooser = new JFileChooser();
		File file;
		
		if (choose == ImportExportChooser.IMPORT) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki SQLite (*.db)", "db");
			fileChooser.addChoosableFileFilter(filter);
			fileChooser.setAcceptAllFileFilterUsed(false);
			int val = fileChooser.showOpenDialog(null);		
			
			if (val == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				
				eventManager.importFromDB(file);
				calendarView.updateEventDays(
						eventManager.getEventsByYearAndMonth(
								calendarView.getYearSelectComboBoxSelectedItem(),
								calendarView.getMonthSelectComboBoxSelectedItem()));
				
				JOptionPane.showMessageDialog(fileChooser,
					    "Pomyślnie zaimportowano wydarzenia!",
					    "Informacja!",
					    JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			
		    int retrival = fileChooser.showSaveDialog(null);
		    if (retrival == JFileChooser.APPROVE_OPTION) {
		    	file = fileChooser.getSelectedFile();
		    	String filePath = file.toString();
		    	
		    	if (!filePath.endsWith(".db"))
		    		filePath += ".db";
		    	
		    	eventManager.exportToDB(new File(filePath));
		    	
		    	JOptionPane.showMessageDialog(fileChooser,
					    "Pomyślnie wyeksportowano wydarzenia!",
					    "Informacja!",
					    JOptionPane.INFORMATION_MESSAGE);
		    	
		    }
			
		}

	}
	
	
	
}