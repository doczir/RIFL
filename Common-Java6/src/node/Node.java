package node;

import java.io.IOException;

import gui.GUI;

public interface Node {
	public void next() throws IOException;
	public void setGui(GUI gui);
}
