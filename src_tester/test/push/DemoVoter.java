package test.push;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ui.dwr.Util;

public class DemoVoter {
	
	private List<Player> players = new ArrayList<Player>();
	
	public DemoVoter() {
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));
		players.add(new Player("Player 3"));
		players.add(new Player("Player 4"));
	}
	
	public void callReverseAjax() {
		
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String attributeName = "voter";
		scriptSession.setAttribute(attributeName, true);
		
		update();
		

	}
	
	public void callVote(String name) {
		System.out.println(name);
		for ( Player p : players ) {
			if ( p.getName().equals(name)) {
				p.vote();
				break;
			}
		}
		update();
	}
	
	public void update() {
		String page = ServerContextFactory.get().getContextPath() + "/push/voter.html";
		ScriptSessionFilter filter = new MyScriptSessionFilter("voter");
		Collections.sort(players, new PlayerComparator());
		Browser.withPageFiltered(page, filter, new Runnable() {
			public void run() {
				
				String s = "<ul>";
				for ( Player p : players ) {
					s += "<li style=\"cursor:pointer\" onclick=\"document.getElementById('playerId').value='" + p.getName() + "';\">" + p.getName() + "(" + p.getRank() + ")</li>";
				}
				s += "</li>";
				
				Util.setValue("div1", s);

			}
		});
	}
	
	static class PlayerComparator extends lebah.util.MyComparator2<Player> {
		@Override
		public int compare(Player o1, Player o2) {
			return o1.getRank() < o2.getRank() ? 1 : -1;
		}
	}

}
