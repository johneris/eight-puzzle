public class Board
{
	Tile[] tiles;
	int blank_pos;
	
	public Board()
	{
		tiles = new Tile[8];
	}
	
	public Board(Tile[] tiles, int blank_pos)
	{
		this.tiles = new Tile[8];
		// deep copy
		for(int i = 0; i < 8; i++)
		{
			this.tiles[i] = new Tile(tiles[i]);
		}
		this.blank_pos = blank_pos;
	}
	
	public Board(Board board)
	{
		this.tiles = new Tile[8];
		// deep copy
		for(int i = 0; i < 8; i++)
		{
			this.tiles[i] = new Tile(board.tiles[i]);
		}
		this.blank_pos = board.blank_pos;
	}
	
	boolean isSolved()
	{
		for(int i=0; i < 8; i++)
		{
			if(tiles[i].curr_pos != tiles[i].goal_pos)
				return false;
		}
		return true;
	}
	
	boolean isEquals(Tile[] tiles)
	{
		for(int i = 0; i < 8; i++)
		{
			if( this.tiles[i].curr_pos != tiles[i].curr_pos )
				return false;
		}
		return true;
	}
}