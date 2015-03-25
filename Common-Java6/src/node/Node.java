package node;

import gui.GUI;

public interface Node {
	public void next() throws Exception;
	public void setGui(GUI gui);
}
