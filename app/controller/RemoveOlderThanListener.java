package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.Event;
import model.EventManager;

import view.CalendarView;
import view.RemoveOlderThanDialog;

/**
 * Klasa implementuj�ca listener dla przycisku "Usu� starsze ni�...".
 * Zajmuje si� znalezieniem takich wydarze� i usuni�ciem poprzez wywo�anie metody removeByID z EventManager'a.
 * @see EventManager
 */
public class RemoveOlderThanListener implements ActionListener {
	private EventManager eventManager;
	private CalendarView calendarView;

	/**
	 * Konstruktor inicjalizuj�cy obiekt listenera.
	 * @param eventManager menad�er wydarze�
	 * @param calendarView g��wne okno wydarze�
	 */
	public RemoveOlderThanListener(EventManager eventManager, CalendarView calendarView) {
		this.eventManager = eventManager;
		this.calendarView = calendarView;
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (this.eventManager.getEventList().size() == 0) {
			JOptionPane.showMessageDialog(this.calendarView,
				    "Brak wydarze� do usuni�cia.",
				    "Uwaga!",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		final RemoveOlderThanDialog dialog = new RemoveOlderThanDialog();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		 
		dialog.addConfirmListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String confirmButtons[] = {"Tak","Nie"};
		        int PromptResult = JOptionPane.showOptionDialog(dialog,"Jeste� pewny?","Ostrze�enie",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,confirmButtons,confirmButtons[1]);
		        if(PromptResult!=JOptionPane.YES_OPTION)
		        {
		            dialog.dispose();
		            return;
		        }
		        
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				int removedCount = 0;
				String message;
				
				Date inputDate = null;
				try {
					inputDate = dateFormat.parse(dialog.getDate());
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				List<Event> eventList = eventManager.getEventList();
					
				List<Event> tmpEventList = new ArrayList<Event>(eventList);

				for (int i = 0; i < tmpEventList.size(); i++) {
					Date eventDate = tmpEventList.get(i).getDate();
					
					if (eventDate.before(inputDate)) {
						eventManager.removeById(tmpEventList.get(i).getID());
						removedCount += 1;
					}
				}
				
				if (removedCount == 0) {
					message = "Brak wydarze� starszych ni� " + dialog.getDate();
				} else {
					
					message = "Usuni�tych wydarze�: " + removedCount;
				}
				
				JOptionPane.showMessageDialog(dialog,
					    message,
					    "Informacja",
					    JOptionPane.INFORMATION_MESSAGE);
				
				
				calendarView.updateEventDays(
						eventManager.getEventsByYearAndMonth(
								calendarView.getYearSelectComboBoxSelectedItem(),
								calendarView.getMonthSelectComboBoxSelectedItem()));
				
				dialog.dispose();
			}
			 
		 });
	}


}
