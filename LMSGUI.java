import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class LMSGUI extends JPanel {
    
	//Declaring a list of sections which is the individual shelf and checkout books
    private static final String[] sections = {"Biology", "Chemistry", "Physics", "Checked Out"};
    
    
    private Map<String, String> chem = new HashMap<>();
    private Map<String, String> phys = new HashMap<>();
    private Map<String, String> bio = new HashMap<>();
    private Map<String, String> borrow = new HashMap<>();
    
    private Map<Integer, Map<String, String>> library = new HashMap<>();
    private Map<Integer, Map<String, String>> borrowed = new HashMap<>();
    
    
	private ArrayList<Color> colors = new ArrayList<Color>();
	
	//Declaring the shelves	
    private DefaultListModel<String> dlm1 = new DefaultListModel<>();
    private DefaultListModel<String> dlm2 = new DefaultListModel<>();
    private DefaultListModel<String> dlm3 = new DefaultListModel<>();
    private DefaultListModel<String> dlm4 = new DefaultListModel<>();
  
    // Declaring list of items in each shelf
    private JList<String> l1 = new JList<>(dlm1);
    private JList<String> l2 = new JList<>(dlm2);
    private JList<String> l3 = new JList<>(dlm3);
    private JList<String> l4 = new JList<>(dlm4);
    
    public Map<String, String> sort(Map<String, String> map) {
        List<Map.Entry<String, String>> list =  new LinkedList<Map.Entry<String,String>>(map.entrySet()); 
        Collections.sort(list, new Comparator<Map.Entry<String, String> >()
        { 
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        HashMap<String, String> tmp = new LinkedHashMap<String, String>(); 
        for (Entry<String, String> l : list) { 
            tmp.put(l.getKey(), l.getValue()); 
        } 
        return tmp; 
    }
    
    public void sort2(DefaultListModel<String> dlm) {
    	ArrayList<String> lst = Collections.list(dlm.elements());
        Collections.sort(lst);
        dlm.clear();
        for(Object o:lst){ dlm.addElement((String) o); }
    }
    	
    public void initialize() {
    	
    	//adding colors to each shelf
    	colors.add(new Color(255,204,51));
        colors.add(new Color(51,204,255));
        colors.add(new Color(102,255,102));
        //Checked out pane is red
        colors.add(Color.red);
    		
     	BookList b = new BookList();
     	ArrayList<Book> arr = b.getBooks();
     	
     	for(int i = 0; i < 3; i++)
     		chem.put(arr.get(i).getAuthor(), arr.get(i).getTitle());
     	
     	for(int i = 3; i < 6; i++)
     		bio.put(arr.get(i).getAuthor(), arr.get(i).getTitle());
     	
     	for(int i = 6; i < 9; i++)
     		phys.put(arr.get(i).getAuthor(), arr.get(i).getTitle());
    	
     	//Sorting books in alphabetical order in the shelfs
    	bio = sort(bio);
    	chem = sort(chem);
    	phys = sort(phys);
    	borrow = sort(borrow);
    	
    	library.put(1, bio);
    	library.put(2, chem);
    	library.put(3, phys);
    }
    	
    	public void set(DefaultListModel<String> lm, JList<String> list, int i) {
            for (String e : library.get(i).values())
            	lm.addElement(e);
            
            DefaultListCellRenderer renderer = (DefaultListCellRenderer)list.getCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            list.setVisibleRowCount(10);
            list.setBackground(colors.get(i-1));
            list.setFont(new Font("Calibri", Font.BOLD, 18));
            if(i == 4)
            	list.setForeground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            
            JPanel container = new JPanel(new BorderLayout());
            container.setPreferredSize(new Dimension(300,400));
            container.add(scrollPane);
            String s = "";
            if(i != 4)
            	s = "Book shelf #" + i + ": " + sections[i-1] + " Books";
            else
            	s = sections[i-1] + " Books";
            container.setBorder(BorderFactory.createTitledBorder(null, s, TitledBorder.CENTER, TitledBorder.TOP, new Font("Calibri", Font.BOLD, 20))); 
            add(container);
    	}
    	
    	public void setBorrowed(DefaultListModel<String> lm, JList<String> list) { 
            for(Entry<Integer, Map<String, String>> innerMap : borrowed.entrySet())
            	for (Map.Entry<String, String> m : innerMap.getValue().entrySet())
            		lm.addElement(m.getValue());
            
            DefaultListCellRenderer renderer = (DefaultListCellRenderer)list.getCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            list.setVisibleRowCount(10);
            list.setBackground(colors.get(3));
            list.setFont(new Font("Calibri", Font.BOLD, 18));
            list.setForeground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            
            JPanel container = new JPanel(new BorderLayout());
            container.setPreferredSize(new Dimension(300,400));
            container.add(scrollPane);
            String s = "";
            s = sections[3] + " Books";
            container.setBorder(BorderFactory.createTitledBorder(null, s, TitledBorder.CENTER, TitledBorder.TOP, new Font("Calibri", Font.BOLD, 20))); 
            add(container);
    	}
    	
    	
	public LMSGUI() {
		
    	initialize();
    	
        setLayout(new GridLayout(1,0));     
        
        set(dlm1, l1, 1);
        set(dlm2, l2, 2);
        set(dlm3, l3, 3);
        setBorrowed(dlm4, l4);
        
        // list of books in shelf 1 (Biology) 
        //When you double click on each book its moved to the checked out pane
        l1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    l2.clearSelection();
                    l3.clearSelection();
                    l4.clearSelection();
                    int index = list.locationToIndex(evt.getPoint());
                    dlm4.addElement(dlm1.get(index));
                    
                    sort2(dlm4);
                    
                    Map<String, String> tmpInner = new HashMap<>();
                    int k = 0;
                    
                    for(Entry<Integer, Map<String, String>> innerMap : library.entrySet())
                    {
                    	  for (Map.Entry<String, String> m : innerMap.getValue().entrySet())
                    		  if(m.getValue().equals(dlm1.get(index))) {
                            	  k = innerMap.getKey();
                            	  tmpInner.put(m.getKey(), m.getValue());
                    		  }
                    }
                    	  borrowed.put(k,tmpInner);                    	  
                    	  
                    dlm1.remove(index);

                }
            }
            }
        );
        
        // list of books in shelf 2 (chemistry) 
        //When you double click on each book its moved to the checked out pane
        l2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    l1.clearSelection();
                    l3.clearSelection();
                    l4.clearSelection();
                    int index = list.locationToIndex(evt.getPoint());
                    dlm4.addElement(dlm2.get(index));
                    
                    sort2(dlm4);
                    
                    Map<String, String> tmpInner = new HashMap<>();
                    int k = 0;
                    
                    for(Entry<Integer, Map<String, String>> innerMap : library.entrySet())
                    {
                    	  k = innerMap.getKey();
                    	  for (Map.Entry<String, String> m : innerMap.getValue().entrySet())
                    		  if(m.getValue().equals(dlm2.get(index))) {
                            	  k = innerMap.getKey();
                            	  tmpInner.put(m.getKey(), m.getValue());
                    		  } 
                    }
                    borrowed.put(k,tmpInner);
                    
                    dlm2.remove(index);
                    	  
                }
            }
        });
        
        // list of books in shelf 3 (Physics) 
        //When you double click on each book its moved to the checked out pane
        l3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    l1.clearSelection();
                    l2.clearSelection();
                    l4.clearSelection();
                    int index = list.locationToIndex(evt.getPoint());
                    dlm4.addElement(dlm3.get(index));
                    
                    sort2(dlm4);
                    
                    Map<String, String> tmpInner = new HashMap<>();
                    int k = 0;
                    
                    for(Entry<Integer, Map<String, String>> innerMap : library.entrySet())
                    {
                    	  for (Map.Entry<String, String> m : innerMap.getValue().entrySet())
                    		  if(m.getValue().equals(dlm3.get(index))) {
                            	  k = innerMap.getKey();
                            	  tmpInner.put(m.getKey(), m.getValue());
                    		  }

                    }
                    	  borrowed.put(k,tmpInner);
                    	  dlm3.remove(index);
                }
            }
        });
        
        // list of books in checked out pane
        //When you double click on each book its moved back to the respective shelf it was 
        //checked out from
        l4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    l1.clearSelection();
                    l2.clearSelection();
                    l3.clearSelection();
                    int index = list.locationToIndex(evt.getPoint());
                    Map<String, String> tmpInner = new HashMap<>();
                    int k = 0;
                    
                    for(Entry<Integer, Map<String, String>> innerMap : library.entrySet())
                    {
                  	  	for (Map.Entry<String, String> m : innerMap.getValue().entrySet()) {
                    		  if(m.getValue().equals(dlm4.get(index))) {
                    			  tmpInner.put(m.getKey(), m.getValue());
                            	  k = innerMap.getKey();
                    		  }
                  	  	}
                    }
                    
                    	  if(k == 1) {
                    		  dlm1.addElement(dlm4.get(index));
                    		  sort2(dlm1);
                    	  }
                    	  if(k == 2) {
                    		  dlm2.addElement(dlm4.get(index));
                    		  sort2(dlm2);
                    	  }
                    	  if(k == 3) {
                    		  dlm3.addElement(dlm4.get(index));
                    		  sort2(dlm3);
                    	  }
                    
                    dlm4.remove(index);

                }
            }
        }); 
	}   		  
                    		  
    private static void createAndShowGui() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new LMS());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
}
