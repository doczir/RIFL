package node;

import gui.GUI;

import java.io.IOException;

public interface Node {
	public void next() throws IOException;
	public void setGui(GUI gui);
}
