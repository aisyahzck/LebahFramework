/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.db;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class LoggerWindow extends JFrame implements ActionListener {
	
	public static LoggerWindow instance = null;
	JTextArea areaLog = new JTextArea();
	JButton clearButton = new JButton("Clear");
	long counter = 0;
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if ( src == clearButton ) {
			areaLog.setText("");	
		}	
	}
	
	private LoggerWindow() {
		super("Merak Portal Logger");	
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		areaLog = new JTextArea();
		areaLog.setForeground(Color.green);
		areaLog.setBackground(Color.black);
		areaLog.setLineWrap(true);
		areaLog.setWrapStyleWord(true);
		JScrollPane scrollpane = new JScrollPane(areaLog);
		JScrollBar scrollbar = scrollpane.getVerticalScrollBar();
		ScrollAdjustmentListener adjListener = new ScrollAdjustmentListener(scrollbar);
		
		getContentPane().setLayout(new BorderLayout());
	
		clearButton.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(clearButton);
		
		getContentPane().add(scrollpane, BorderLayout.CENTER);	
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		setSize(500, 400);	    
	    setVisible(true);			
	}
	
	public static LoggerWindow getInstance() {
		if ( instance == null ) instance = new LoggerWindow();
		return instance;
	}	
	
	public void Log(String str) {
		if ( instance != null ) {
			areaLog.append("[".concat(Long.toString(++counter)).concat("]  ").concat(str).concat("\n"));
		}
	}
	
	
	class ScrollAdjustmentListener implements AdjustmentListener {
		JScrollBar aScrollBar;
		ScrollAdjustmentListener(JScrollBar s) {
			aScrollBar = s;
			aScrollBar.addAdjustmentListener(this);
		}
		public void adjustmentValueChanged(AdjustmentEvent e) {
			aScrollBar.setValue(aScrollBar.getMaximum());
		}
	}
		
}
