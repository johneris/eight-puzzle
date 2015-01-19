import java.util.*;

public class List
{
	ArrayList<Node> list;

	public List()
	{
		list = new ArrayList<Node>();
	}
	
	public void add(Node node)
	{
		list.add(node);
		sort();
	}
	
	public Node dequeue()
	{
		Node temp = new Node();
		temp = list.get(0);
		list.remove(0);
		return temp;
	}
	
	public Node pop()
	{
		Node temp = list.get(list.size()-1);
		list.remove(list.size()-1);
		return temp;
	}
	
	public void sort()
	{
		int i, j;
		Node temp = new Node();
		for(i=1; i<list.size(); i++)	{
			temp = list.get(i);
			j = i;
			while( (j>0) && (list.get(j-1).f_cost > temp.f_cost) )	{
				list.remove(j);
				list.add(j, list.get(j-1));
				j -= 1;
			}
			list.remove(j);
			list.add(j, temp);
		}
	}
	
	boolean isAlreadyIn(Node node)
	{
		for( Node n : list )
		{
			if(n.board.isEquals(node.board.tiles))
				return true;
		}
		return false;
	}
	
}