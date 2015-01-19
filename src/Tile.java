import javax.swing.*;

public class Tile extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int curr_pos;
	int goal_pos;
	
	public Tile()
	{
	}
	
	public Tile(ImageIcon icon, int pos)
	{
		setIcon(icon);
		curr_pos = goal_pos = pos;
	}
	
	public Tile(Tile tile)
	{
		curr_pos = tile.curr_pos;
		goal_pos = tile.goal_pos;
	}
}