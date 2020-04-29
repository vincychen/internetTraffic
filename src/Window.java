import java.awt.*;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Window extends JFrame {
	private Font fileFont;
	public ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
	private ArrayList<Double> xList = new ArrayList<Double>();
	private ArrayList<Double> yList = new ArrayList<Double>();
	private ArrayList<String> IPList = new ArrayList<String>();
	private ArrayList<String> DestinationList = new ArrayList<String>();
	private JComboBox<String> ComboBox;
	private boolean IPOrD;
	private double max_x;
	private GridBagConstraints d = new GridBagConstraints();
	private JPanel drawPanel = new JPanel();
	/**
	 * Class constructor call a number of functions and setup the Canvas
	 */
	public Window() {
		super("Flow volume viewer");
		setupMenu();
		setLocation(0, 0);
		setLayout(new GridBagLayout());
		d.gridx = 0;
		d.gridwidth = 1;
		d.gridheight = 1;
		d.weightx = 1.0;
		d.weighty = 1.0;
		d.insets = new Insets(0,0,0,0);
		d.fill = GridBagConstraints.NONE;
		setupRadioButtons();
		coordinate();
		setupComboBox();
		setVisible(true);
	}
	 /** 
     * This method setup the menu bar for the Canvas. It create an  
     * ActionListener to get the chooser to choose a file. And then 
     * get all useful information from the file and initialize the 
     * Source list and Destination list.
     */
	private void setupMenu() {
		fileFont = new Font("SansSerif",Font.BOLD, 16);
    	JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		fileMenu.setFont(fileFont);
		menuBar.add(fileMenu);
		JOptionPane.showInputDialog("Please enter PIN");
		JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
		fileMenuOpen.setFont(fileFont);
		fileMenuOpen.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						System.out.println(this);
					    JFileChooser chooser = new JFileChooser();
					    chooser.setCurrentDirectory(new File("."));
					    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
					      public boolean accept(File f) {
					        return f.getName().toLowerCase().endsWith(".txt")
					            || f.isDirectory();
					      }
					      
					      public String getDescription() {
					        return "text file";
					      }
					    });
					    
					    int r = chooser.showOpenDialog(new JFrame());
					    if (r == JFileChooser.APPROVE_OPTION) {
					    	dataList.clear();
					    	IPList.clear();
					    	DestinationList.clear();
					    	File file = chooser.getSelectedFile();
					    	ArrayList<String> aList = new ArrayList<String>();
					    	try{
					    		Scanner sc = new Scanner(file); 
					    		while (sc.hasNextLine())
					    			aList.add(sc.nextLine());}
					    	catch (Exception a){}
					    	for (String s : aList) {
					    		ArrayList<String> list = new ArrayList<String>() ;
					    		for (String i :s.split("\\t")) {
					    			list.add(i);
					    		}
					    		int size = list.size();
					    		if(size > 7) {
					    			dataList.add(list);
					    			String IP = list.get(2);
						    		if (IPList.contains(IP)== false) {
						    			IPList.add(IP);
					    		}
					    			String n = list.get(4);
						    		if (DestinationList.contains(n)== false) 
						    			DestinationList.add(n);
					    		}
					    	}
					    	Sort sort = new Sort(IPList);
					    	IPList = sort.getSort();
					    	sort = new Sort(DestinationList);
					    	DestinationList = sort.getSort();
					    	IPOrD = true;
					    	max_x = Double.valueOf(dataList.get(dataList.size()-1).get(1));
					    	updateIP(IPList);
					    }
					}
				}
        );
		System.out.println(Window.this);
		fileMenu.add(fileMenuOpen);
		JMenuItem fileMenuQuit = new JMenuItem("Quit");
		fileMenuQuit.setFont(fileFont);
		fileMenu.add(fileMenuQuit);
		fileMenuQuit.addActionListener(
				new ActionListener() {public void actionPerformed(ActionEvent e) {System.exit(0);}});
	}
	/**
	 * This method creates two radioButtons so one of them will be chosen 
	 * each time. It set the radioButton group to the top left corner of
	 * the Canvas. 
	 */
	private void setupRadioButtons() {
		JPanel radioButtonPanel;
		ButtonGroup radioButtons;
		JRadioButton SourceHosts;
		JRadioButton DestinationHosts;
		radioButtonPanel = new JPanel(new GridBagLayout());
		radioButtonPanel.setSize(new Dimension(200, 50));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		radioButtons = new ButtonGroup();
		SourceHosts = new JRadioButton("Source hosts");
		SourceHosts.setFont(fileFont);
		SourceHosts.setSelected(true);
		DestinationHosts = new JRadioButton("Destination hosts");
		DestinationHosts.setFont(fileFont);
		radioButtons.add(SourceHosts);
		radioButtons.add(DestinationHosts);
		radioButtonPanel.add(SourceHosts, c);
		radioButtonPanel.add(DestinationHosts, c);
		ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(SourceHosts.isSelected()) {
            		IPOrD = true;
            		updateIP(IPList);
            	}else if (DestinationHosts.isSelected()) {
            		IPOrD = false;
            		updateIP(DestinationList);
            	}
            }
		};
		SourceHosts.addActionListener(listener);
        DestinationHosts.addActionListener(listener);
		d.gridy = 0;
		d.insets = new Insets(0,0,0,0);
		d.weighty = 0.05;
		d.anchor = GridBagConstraints.WEST;
		add(radioButtonPanel, d);
    }
	/**
	 * Draw the original graph without input data and put it on the bottom
	 * of the Canvas
	 */
	private void coordinate() {
		drawPanel.setSize(new Dimension(1000,450));
		drawPanel.setBackground(Color.WHITE);
        drawPanel.setLayout(new BorderLayout());
        JPanel graphPanel = new DrawGraph(xList,yList);
        drawPanel.add(graphPanel, BorderLayout.CENTER);
        
        d.gridx = 0;
		d.gridy = 1;
		d.insets = new Insets(0,0,0,0);
		d.weighty = 0.95;
		d.fill = GridBagConstraints.BOTH;
		d.anchor = GridBagConstraints.CENTER;
		add(drawPanel, d);
	}
	/**
	 * This method is used to setup the combo box in the starting canvas
	 * and set it to invisible. 
	 */
	private void setupComboBox() {
		JPanel p = new JPanel();
		d.gridy = 0;
		d.weighty = 0;
		d.anchor = GridBagConstraints.NORTH;
		ComboBox = new JComboBox<String>();
		ComboBox.setModel((MutableComboBoxModel<String>) ComboBox.getModel());
		ComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
			}});
		ComboBox.setFont(fileFont);
		ComboBox.setMinimumSize(new Dimension(300,25));
		ComboBox.setVisible(false);
		p.add(ComboBox);
		add(p,d);
	}
	/**
	 * This method is used to update the combo box which contains 
	 * source or destination.
	 * @param IPs		the list of source list or the list of 
	 * 					destination list 
	 */
	private void updateIP(ArrayList<String> IPs) {
		if (ComboBox.getItemListeners().length != 0) {
			ComboBox.removeAllItems();
		}
		for (String name : IPs) {ComboBox.addItem(name);}
		ComboBox.setSelectedIndex(0);
		ComboBox.setVisible(true);
		get_the_list(0);
		drawPanel.repaint();
		ComboBox.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int index = ComboBox.getSelectedIndex();
            	if (index == -1) {index = 0;}
            	get_the_list(index);
            	drawPanel.repaint();
            }
		});
	}
	/**
	 * This method is used to find all String arrayList contains
	 * the selected source or destination.
	 * @param index			this is an integer of selected source or 
	 *  					destination in the corresponding list.
	 */
	private void get_the_list(int index) {
		ArrayList<ArrayList<String>> the_list = new ArrayList<ArrayList<String>>();
		if (IPOrD == true) {
    		String IP = IPList.get(index);
        	for (ArrayList<String> list : dataList) {
        		if (IP.equals(list.get(2))) {
        			the_list.add(list);
        		}
        	}
    	}else {
    		String IP = DestinationList.get(index);
    		for (ArrayList<String> list : dataList) {
    			if (IP.equals(list.get(4))){ 
    				the_list.add(list);
    			}
    		}	
    	}
		get_x_y(the_list);
	}
	/**
	 * This method is used to find all dots' x and y in the graph.
	 * For every 2 seconds interval, we have a total value of y.
	 * @param a_list	all String list has the selected source or 
	 * 					destination
     */
	private void get_x_y(ArrayList<ArrayList<String>> a_list) {
		ArrayList<Point> points = new ArrayList<Point>();
		xList.clear();
		yList.clear();
		int all = 0;
		int max_time;
		for (int i = 0; i<a_list.size();i++) {
			ArrayList<String> list = a_list.get(i);
			if (list.get(3).equals("") == true) {
				points.add(new Point(Double.valueOf(list.get(1)),0.0));
			}
			else {
				points.add(new Point(Double.valueOf(list.get(1)),Double.valueOf(list.get(7))));
			}
			
		}
		if ((int)max_x % 2 == 0) {
			max_time = (int)max_x + 2;
		}
		else {max_time = (int)max_x + 1;}
		int j = 0;
		for (double i = 2.0; i <= max_time; i+=2) {
			double total = 0;
			while ((j<a_list.size())&&(points.get(j).getX()<i)) {
				double y = points.get(j).getY();
				all += y;
				total += y;
				j++;
			}
			xList.add(i);
			yList.add(total);
		}

	}
}
