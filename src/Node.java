public class Node
{
	Node parent;
	int move_form_parent;
	
	Board board;
	
	Node[] adj_states;
	int[] moves;
	
	int f_cost, g_cost, h_cost;
	
	// to construct the root
	public Node(Board board)
	{
		this.board = new Board(board);
		this.board.blank_pos = board.blank_pos;
		this.g_cost = 0;
	}
	
	public Node()
	{
	}
	
	void addAdjacent()
	{
		int i = 0;
		int g = this.g_cost + 1;
		
		this.adj_states = new Node[possibleMoves().length];
		this.moves = new int[possibleMoves().length];
		
		for(int move : possibleMoves())
		{
			this.adj_states[i] = new Node();
			this.adj_states[i].parent = this;
			this.adj_states[i].move_form_parent = move;
			this.adj_states[i].board = moveTileAt(new Board(this.board), move);
			this.adj_states[i].g_cost = g;
			this.adj_states[i].h_cost = manhattan(this.adj_states[i].board.tiles);
			this.adj_states[i].f_cost = this.adj_states[i].g_cost + this.adj_states[i].h_cost;
			this.moves[i++] = move;
		}
	}
	
	Board moveTileAt(Board board, int move)
	{
		Tile[] tiles = new Tile[8];
		// deep copy
		for(int i = 0; i < 8; i++)
		{
			tiles[i] = new Tile(board.tiles[i]);
		}
		int blank_pos = board.blank_pos;
		int tile_index = getIndexOfTileAt(board.tiles, move);
		
		for(int i = 0; i < 8; i++)
		{
			tiles[i] = board.tiles[i];
		}
		
		int temp = board.blank_pos;
		blank_pos = tiles[tile_index].curr_pos;
		tiles[tile_index].curr_pos = temp;
		
		return new Board(tiles, blank_pos);
	}
	
	int getIndexOfTileAt(Tile[] tiles, int pos)
	{
		int index = 0;
		for(int i=0; i < 8; i++)
		{
			if(tiles[i].curr_pos == pos)
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	int[] possibleMoves()
	{
		if(this.board.blank_pos == 0) return new int[]{1, 3};
		if(this.board.blank_pos == 1) return new int[]{0, 2, 4};
		if(this.board.blank_pos == 2) return new int[]{1, 5};
		if(this.board.blank_pos == 3) return new int[]{0, 4, 6};
		if(this.board.blank_pos == 4) return new int[]{1, 3, 5, 7};
		if(this.board.blank_pos == 5) return new int[]{2, 4, 8};
		if(this.board.blank_pos == 6) return new int[]{3, 7};
		if(this.board.blank_pos == 7) return new int[]{4, 6, 8};
		return new int[]{5, 7};
	}
	
	int manhattan(Tile[] tiles)
	{
		int h = 0;
		for(Tile t : tiles)
		{
			h += (
				Math.abs(row(t.curr_pos) - row(t.goal_pos)) +
				Math.abs(col(t.curr_pos) - col(t.goal_pos)) );
		}
		return h;
	}
	
	int row(int pos)
	{
		if(pos == 0 || pos == 1 || pos == 2 )	return 0;
		if(pos == 3 || pos == 4 || pos == 5 )	return 1;
		return 2;
	}
	
	int col(int pos)
	{
		if(pos == 0 || pos == 3 || pos == 6 )	return 0;
		if(pos == 1 || pos == 4 || pos == 7 )	return 1;
		return 2;
	}
	
}