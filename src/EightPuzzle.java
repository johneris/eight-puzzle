import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.border.*;

enum Mode	{ AI, Shuffle, None }

public class EightPuzzle extends JFrame
	implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel puzzle_panel, center_panel, bottom_panel;
	TextArea txtarea;
	JButton btn_solve, btn_shuffle, btn_reset;
	JButton btn_image;
	
	Board board;
	JLabel blank;
	
	int no_of_moves;
	
	Image source;
	int width;
	int height;

	final int[][] pos =
		new int[][]{
			{0, 1, 2},
			{3, 4, 5},
			{6, 7, 8}
		};
	
	public EightPuzzle(String url, String url_min)
	{
		initUI(url, url_min);
	}
	
	void initUI(String url, String url_min)
	{
		ImageIcon image = new ImageIcon(EightPuzzle.class.getResource(url));
		ImageIcon image_min = new ImageIcon(EightPuzzle.class.getResource(url_min));
		source = image.getImage();
		
		width = image.getIconWidth();
		height = image.getIconHeight();
	
		board = new Board();
		board.tiles = new Tile[8];
		board.blank_pos = 8;
		blank = new JLabel("");
	
		puzzle_panel = new JPanel();
		puzzle_panel.setLayout(new GridLayout(3, 3, 0, 0));
		puzzle_panel.setBorder(new EmptyBorder(new Insets(25, 15, 25, 15)));
		puzzle_panel.setBackground(Color.GRAY);
	
		center_panel = new JPanel();
		center_panel.setLayout(new FlowLayout());
		center_panel.setBackground(Color.BLACK);
	
		bottom_panel = new JPanel();
		bottom_panel.setLayout(new FlowLayout());
		bottom_panel.setBackground(Color.BLACK);
		
		btn_solve = new JButton("Solve");
		btn_shuffle = new JButton("Shuffle");
		btn_reset = new JButton("Reset # of moves");
		btn_image = new JButton();
		
		btn_solve.addActionListener(this);
		btn_shuffle.addActionListener(this);
		btn_reset.addActionListener(this);
		btn_image.setIcon(image_min);
		btn_image.setContentAreaFilled(false);
		btn_image.setBorderPainted(false);
	
		txtarea = new TextArea("", 6, 40, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		txtarea.setBackground(Color.GRAY);
		txtarea.setEditable(false);
	
		center_panel.add(btn_solve);
		center_panel.add(btn_shuffle);
		center_panel.add(btn_reset);
		
		bottom_panel.add(btn_image);
		bottom_panel.add(txtarea);
		
		int tile_ctr = 0;
		Image temp_img;
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 2 && i == 2) {
                    puzzle_panel.add(blank);
                } else {
                    temp_img = createImage(new FilteredImageSource(source.getSource(),
                            new CropImageFilter(j * width / 3, i * height / 3,
                            (width / 3) + 1, height / 3)));
					board.tiles[tile_ctr] = new Tile(new ImageIcon(temp_img), tile_ctr);
					board.tiles[tile_ctr].addActionListener(this);
					board.tiles[tile_ctr].setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
					board.tiles[tile_ctr].setContentAreaFilled(false);
					board.tiles[tile_ctr].setBorderPainted(false);
					puzzle_panel.add(board.tiles[tile_ctr++]);
                }
            }
		}
		
		setLayout(new BorderLayout());

		add(puzzle_panel, BorderLayout.NORTH);
		add(center_panel, BorderLayout.CENTER);
		add(bottom_panel, BorderLayout.SOUTH);
		
		setTitle("Eight Puzzle");
		setSize(545, 550);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btn_solve)
		{
			no_of_moves = 0;
			
			if(board.isSolved())
			{
				JOptionPane.showMessageDialog(null, "Puzzle is in goal state. No need to solve");
				return;
			}
			
			txtarea.setText("Algorithm: A*\nHeuristic: Manhattan Distance\nAI Solving...");
			
			btn_solve.setEnabled(false);
			btn_shuffle.setEnabled(false);
			btn_reset.setEnabled(false);
			
			AStar solver = new AStar(new Board(board));
			txtarea.setText(solver.getAIMoves());
			
			for(int move : solver.solution)
			{
				JOptionPane.showMessageDialog(null, "Move tile at index " + move);
				move(move, Mode.AI);
			}
			
			txtarea.append("Puzzle Solved in " + no_of_moves + " move(s)!");
			
			btn_solve.setEnabled(true);
			btn_shuffle.setEnabled(true);
			btn_reset.setEnabled(true);
			
			no_of_moves = 0;
			return;
		}
		if(e.getSource() == btn_shuffle)
		{
			shuffle();
			txtarea.setText("");
			no_of_moves = 0;
			return;
		}
		if(e.getSource() == btn_reset)
		{
			JOptionPane.showMessageDialog(null, "Number of moves set to 0");
			txtarea.setText("");
			no_of_moves = 0;
			return;
		}
	
		JButton btn = (JButton) e.getSource();
		Dimension size = btn.getSize();
		
		int btnX = btn.getX();
		int btnY = btn.getY();
		
		int btnPos = pos[btnY/size.height][btnX/size.width];
		
		move(btnPos, Mode.None);
	}
	
	void move(int pos, Mode mode)
	{
		int tile_index = getIndexOfTileAt(pos);
		
		if((board.blank_pos == pos + 3) || (board.blank_pos == pos + 1))
		{
			puzzle_panel.remove(pos);
			puzzle_panel.add(blank, pos);
			puzzle_panel.add(board.tiles[tile_index], board.blank_pos);
		}
		else if((board.blank_pos == pos - 3) || (board.blank_pos == pos - 1))
		{
			puzzle_panel.remove(board.blank_pos);
			puzzle_panel.add(board.tiles[tile_index], board.blank_pos);
			puzzle_panel.add(blank, pos);
		}
		else
		{
			return;
		}
		
		int temp = board.tiles[tile_index].curr_pos;
		board.tiles[tile_index].curr_pos = board.blank_pos;
		board.blank_pos = temp;
		
		puzzle_panel.validate();
		
		no_of_moves++;
		
		if(mode == Mode.Shuffle)	return;
		
		if(board.isSolved())
		{
			JOptionPane.showMessageDialog(null, "Puzzle Solved in " + no_of_moves + " move(s)");
			if(!(mode == Mode.AI))	no_of_moves = 0;
		}
	}
	
	int getIndexOfTileAt(int pos)
	{
		int index = 0;
		for(int i=0; i < 8; i++)
		{
			if(board.tiles[i].curr_pos == pos)
			{
				index = i;
				break;
			}
		}
		return index;
	}

	void shuffle()
	{
		Random rand = new Random();
		int rand_num;
		
		for(int i=1; i <= 100; i++)
		{
			rand_num = rand.nextInt(9);
			if(rand_num != board.blank_pos)
				move(rand_num, Mode.Shuffle);
		}
		
		if(board.isSolved())	shuffle();
		
		no_of_moves = 0;
	}
	
	public static void main(String[] args)
	{
		final String[] images = {"Numbers", "Gon", "Killua", "Gon and Killua", "Gon, Killua, and Biscuit"};
		
		String url = new String();
		String url_min = new String();
		
		String choice = (String) JOptionPane.showInputDialog(null, "Choose an image: ", 
						"Image", JOptionPane.QUESTION_MESSAGE, null, images, images[0]);
		
		switch(choice)
		{
			case "Numbers"	:	url = "Images/numbers.jpg";
								url_min = "Images/numbers_min.jpg";
								break;
			case "Gon"		:	url = "Images/gon.jpg";
								url_min = "Images/gon_min.jpg";
								break;
			case "Killua"	:	url = "Images/killua.jpg";
								url_min = "Images/killua_min.jpg";
								break;
			case "Gon and Killua":	url = "Images/gon_killua.jpg";
									url_min = "Images/gon_killua_min.jpg";
									break;
			case "Gon, Killua, and Biscuit"	:	url = "Images/gon_killua_biscuit.jpg";
											url_min = "Images/gon_killua_biscuit_min.jpg";
											break;
			default			:	url = "Images/numbers.jpg";
								url_min = "Images/numbers_min.jpg";
								break;
		}
		
		new EightPuzzle(url, url_min);
	}
	
}