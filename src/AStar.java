
public class AStar
{
	Board initial_board;
	
	List OPEN_LIST;
	List CLOSED_LIST;
	
	String AI_moves;
	
	int[] solution;
	
	public AStar(Board board)
	{
		AI_moves = new String();
		AI_moves =  "| 0 | 1 | 2 |\n" +
					"| 3 | 4 | 5 |\n" +
					"| 6 | 7 | 8 |\n";
		this.initial_board = new Board(board);
		solve();
	}
	
	void solve()
	{
		Node initial_state = new Node(initial_board);
		
		Node CURRENT = new Node();
		
		OPEN_LIST = new List();
		CLOSED_LIST = new List();
		
		OPEN_LIST.add(initial_state);
		
		boolean solved = false;
		
		while(!OPEN_LIST.list.isEmpty())
		{
			CURRENT = OPEN_LIST.dequeue();
			// relax
			CURRENT.addAdjacent();
			for( Node n : CURRENT.adj_states )
			{
				if(n.board.isSolved()) 
				{
					tracePath(n);
					solved = true;
					break;
				}
				if( !(OPEN_LIST.isAlreadyIn(n) || CLOSED_LIST.isAlreadyIn(n)) )
					OPEN_LIST.add(n);
			}
			CLOSED_LIST.add(CURRENT);
			if(solved == true)
			{
				break;
			}
		}
		
	}
	
	void tracePath(Node curr)
	{
		Node temp = curr;
		int i = 0, j;
		int[] temp_array = new int[50];

		while(temp.parent != null)
		{
			temp_array[i++] = temp.move_form_parent;
			temp = temp.parent;
		}
		
		int size = i;
		this.solution = new int[size];
		for(i = 0, j = size-1; i < size; i++, j--)
		{
			this.solution[i] = temp_array[j];
			AI_moves += ( "Move tile at index " + this.solution[i] + "\n");
		}
	}
	
	public String getAIMoves()
	{
		return AI_moves;
	}
	
}