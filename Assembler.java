import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class Assembler extends JFrame implements ActionListener,MouseMotionListener {
	private JPanel panel[] = new JPanel[24];
	private JButton button[] = new JButton[8];
	private JTextArea textArea[] = new JTextArea[6];
	private JTextField textfield[] = new JTextField[2];
	private String toolname[][] = {	{"開啟","說明","Pass 1","Pass 2","儲存 pass1","儲存 pass2","清除","離開"},
									{"開啟檔案","處理顯示"}};
	private String toolexplain[][] = {	{"開啟一個SIC組合語言檔案","說明講解此編譯器功能","進行Pass 1的編譯","進行Pass 2的編譯","將Pass 1的結果儲存","將Pass 2的結果儲存","清除所有的檔案","離開此編譯器"},
			{"","","","SIC 組譯器","說明 : ","SIC組譯器相關說明\n==============================================\n功能簡單分為 :\n    1. 開啟 SIC程式碼檔案。\n    2. 經 Pass 1、2 編譯 SIC程式碼。\n    3. 儲存 Pass 1、2 編譯結果。\n    4. 清除及離開。\n\n===============開啟 SIC程式碼檔案================\n選擇一個 SIC程式碼 以供編譯。\n開起檔案類型主要分為 :\n    1. txt文字檔。\n    2. sic程式檔。\n\n=================Pass 1、2 編譯==================\nPass 1編譯內容 :\n    編排出記憶體位置以供Pass 2編碼。\nPass 2編譯內容 :\n    編譯出 object code。\n\n=================儲存 Pass 1、2==================\n可隨使用者之喜好儲存想要的工作項目。\n\n==================清除及離開===================\n清除 :\n    清除三個工作區的內容。\n離開 :\n    關閉此組譯器並離開。"},							
			{"SIC組合語言程式碼","經過Pass 1編譯的SIC組合語言程式碼","經過Pass 2編譯的SIC組合語言程式碼","說明區塊","",""}};
	private JMenuBar bar ; 
	private JMenu menu[];
	private JMenuItem item[][];
	private String menuname[][] = { {"檔案 (F)","編輯 (E)","說明 (H)","儲存檔案 (S)"}, {"檔案相關處理","顯示相關處理","功能相關處理"}};
	private String itemname[][]= { {"開啟檔案 (O)|79","離開 (X)|88"},
								   {"Pass 1 (P)|80","Pass 2 (B)|66","清除 (C)|67"},
								   {"關於 (Q)|81","說明 (H)|72",},
								   {"儲存Pass1 (F)|70","儲存Pass2 (S)|83"}};
	private String load_url,url_p;
	private String A[][],B[][];
	private FileFormat fileformat ;
	
	public Assembler(String name) {
		super(name);
		
		setGUI();
		
		load_url=null;
		url_p=null;
		A = new String[0][0];
		B =new String[0][0];
		fileformat = new FileFormat();
	}
	public void setGUI(){
		for (int i = 0; i < panel.length; i++){
			panel[i] = new JPanel();
			if(i==1 || i==9 || i==10 || i==14 || i==16 || i==22 || i==23) panel[i].setLayout(new FlowLayout());
			else if((i>=0 && i<=6 && i%2==0) || i==3 ||  i==7 || i==11 || (i>=15 && i<=21 && i%2==1))panel[i].setLayout(new BorderLayout(i==0||i==2 ? 1:0, i==2||i==11 ? 10:0));
			else panel[i].setLayout(i==8?new GridLayout( 5, 2, 5, 10):i==12||i==13?new GridLayout(1, 2, 10, 10):new GridLayout(1, 1, 0, 0));
			
			if(i==6)panel[i].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(172, 168, 153)));
			else if((i>=14 && i%2==0) || i==23)panel[i].setBorder(BorderFactory.createMatteBorder(1, 0, 0, i==20?0:1, new Color(172, 168, 153)));
			
			if(i>=3 && i%2==1){
				int c = 0;
				if(i==15) c=10;
				else if(i==11) c=2;
				else if(i==9||i==7||i==5||i==3) c=1;
				else c=0;
				panel[i-2-c].add(panel[i], i==5||i==3?BorderLayout.SOUTH:BorderLayout.CENTER);
				panel[i-2-c].add(panel[i-1], i==13||i==9?BorderLayout.NORTH:i==5||i==3?BorderLayout.CENTER:BorderLayout.EAST);
			}
		}

		for (int i = 0; i < textArea.length; i++) {
			textArea[i] = new JTextArea(toolexplain[1][i],i==2?8:0,i==3?25:0);
			if(i<=2){
				panel[i==2?5:13].add(new JScrollPane(textArea[i]));
				textArea[i].setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, new Color(172, 168, 153)));
				textArea[i].setFont(new Font("Serif", Font.PLAIN, 13));
			}
			else if(i<5){
				textArea[i].setBackground(new Color(240,240,240));
				textArea[i].setFont(new Font("Serif", Font.PLAIN, 17));
				panel[i==3?18:20].add(textArea[i]);
			}
			textArea[i].setEditable(false);
			textArea[i].setToolTipText(toolexplain[2][i]);
			textArea[i].addMouseMotionListener(this);
		}
		for (int i = 0; i < button.length; i++) {
			button[i] = new JButton(toolname[0][i]);
			button[i].setFont(new Font("Serif", Font.PLAIN, 20));
			button[i].setToolTipText(toolexplain[0][i]);
			panel[8].add(button[i]);
			button[i].addActionListener( this );
			button[i].addMouseMotionListener(this);
		}
		for (int i = 0; i < textfield.length; i++) {
			textfield[i] = new JTextField(toolname[1][i]);
			textfield[i].setFont(new Font("Serif", Font.PLAIN, 20));
			textfield[i].setEditable(false);
			panel[12].add(textfield[i]);
		}
		
		bar = new JMenuBar();
		menu = new JMenu [menuname[0].length];
		item = new JMenuItem[4][3];
		
		for(int i=0 ; i < menu.length-1 ; i++ ){
			menu[i] = new JMenu (menuname[0][i]);
			menu[i].setMnemonic( menuname[0][i].split("\\(")[1].charAt(0));
			menu[i].setToolTipText(menuname[1][i]);
			menu[i].addMouseMotionListener(this);
			for(int j = 0 ; j < itemname[i].length ; j++){
				item[i][j] = new JMenuItem(itemname[i][j].split("\\|")[0]);
				item[i][j].setMnemonic( itemname[i][j].split("\\(")[1].charAt(0));
				item[i][j].setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(itemname[i][j].split("\\|")[1]), ActionEvent.CTRL_MASK));
				item[i][j].addActionListener(this);
				if(j==2) menu[i].addSeparator();
				menu[i].add(item[i][j]);
				if(i==0&j==0){
					menu[3] = new JMenu (menuname[0][3]);
					menu[3].setMnemonic( menuname[0][3].split("\\(")[1].charAt(0));
					for(int k = 0 ; k<itemname[3].length ; k++){
						item[3][k] = new JMenuItem(itemname[3][k].split("\\|")[0]);
						item[3][k].setMnemonic( itemname[3][k].split("\\(")[1].charAt(0));
						item[3][k].setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(itemname[3][k].split("\\|")[1]), ActionEvent.CTRL_MASK));
						item[3][k].addActionListener(this);
						menu[3].add(item[3][k]);
					}
					menu[i].add(menu[3]);
					menu[i].addSeparator();
				}
			}
			bar.add( menu[i]);
		}
		this.setJMenuBar( bar ); 
		this.add(panel[0]);
	}
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == button[0] || arg0.getSource() == item[0][0]){
			url_p = getURL(0);
			load_url = url_p != null ? url_p : load_url;
			
			if(load_url!=null){
				fileformat = new FileFormat();
				A = fileformat.format_A(load_url,fileformat.num_line(load_url));

				String A_string = "";
				for(int i = 0; i<A.length ; i++){
					for(int j=0 ; j<A[i].length ; j++)
						if(A[i][j]!=null) A_string = A_string+A[i][j] + "\t";
						else A_string = A_string + "\t";
					A_string = A_string+"\n";
				}
				
				textArea[0].setText(A_string);
				textArea[1].setText("");
				textArea[2].setText("");
			}else JOptionPane.showMessageDialog(null, "尚未開啟檔案!!","錯誤!", JOptionPane.ERROR_MESSAGE);
			
		}
		if(arg0.getSource() == button[1] || arg0.getSource() == item[2][1]){
			JFrame A = new JFrame("SIC組譯器說明");
	   		A.add( new JScrollPane( textArea[5] ) );
	        A.setSize( 500, 500 );
	        A.setResizable(false);
	        A.setVisible( true );
		}
		if(arg0.getSource() == button[2] || arg0.getSource() == item[1][0]){
			if(load_url != null){
				if(fileformat.getcheck()){
					B = fileformat.format_B(A);
					String B_string = "";
					for(int i = 0; i<B.length ; i++){
						for(int j=0 ; j<B[i].length ; j++)
							if(B[i][j]!=null) 
								B_string = B_string+B[i][j] + "\t";
							else 
								B_string = B_string + "\t";
						B_string = B_string+"\n";
					}
					textArea[1].setText(B_string);
				}
				else{
					JOptionPane.showMessageDialog(null, "原始檔案有誤!","錯誤!", JOptionPane.ERROR_MESSAGE);
				}
			}
			else JOptionPane.showMessageDialog(null, "尚未開啟檔案!!","錯誤!", JOptionPane.ERROR_MESSAGE);
			
		}
		if(arg0.getSource() == button[3] || arg0.getSource() == item[1][1]){
			if(load_url != null)
				if(fileformat.getcheck())
					textArea[2].setText(fileformat.getheadrecord(B)+"\n"+fileformat.gettextrecord(B)+"\n"+fileformat.getmodifyrecord(B)+(fileformat.getendrecord(B).equalsIgnoreCase("")? "": "\n"+fileformat.getendrecord(B)));
				else{
					JOptionPane.showMessageDialog(null, "尚未完成\""+button[2].getText()+"\"","錯誤!", JOptionPane.ERROR_MESSAGE);
				}
			else JOptionPane.showMessageDialog(null, "尚未開啟檔案!!","錯誤!", JOptionPane.ERROR_MESSAGE);
		}
		if(arg0.getSource() == button[4] || arg0.getSource() == item[3][0])
			fileformat.save_A2(getURL(1),textArea[1].getText());
		if(arg0.getSource() == button[5] || arg0.getSource() == item[3][1])
			fileformat.save_A2(getURL(1),textArea[2].getText());
		if(arg0.getSource() == button[6] || arg0.getSource() == item[1][2]){
			textArea[0].setText("");
			textArea[1].setText("");
			textArea[2].setText("");
			url_p = null;
			load_url = null;
		}
		if(arg0.getSource() == button[7] || arg0.getSource() == item[0][1])
			System.exit(0);
		if(arg0.getSource() == item[2][0])
			JOptionPane.showMessageDialog(null, introduceOA(), "關於 Assembler", 0, new ImageIcon(getClass().getResource("pic/oa.gif")));
	}
	private JPanel introduceOA(){
		JLabel a = new JLabel("<html>程式名稱：SIC Assembler (2009/5/3)<br>作者：吳政賢<br>系級：淡江大學資訊工程2-A<br>信箱：comdan66@yahoo.com.tw<br>MSN  : com-dan-66@hotmail.om<br></html>");
		
		JLabel b = new JLabel();
		b.setText("<html><a href=\"http://www.wretch.cc/blog/comdan66\">www.wretch.cc/blog/comdan66</a></html>");
		b.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {
				Desktop desktop = Desktop.getDesktop();
				if(desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE))
				    try {desktop.browse(new URI("http://www.wretch.cc/blog/comdan66"));
				    } catch (URISyntaxException e ) {} catch (IOException e) {}
			}
			public void mouseEntered(MouseEvent arg0) {((JComponent)arg0.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));}
			public void mouseExited(MouseEvent arg0) {}public void mousePressed(MouseEvent arg0) {}public void mouseReleased(MouseEvent arg0) {}});
		
		JPanel p = new JPanel(); p.setLayout(new BorderLayout()); p.add(a,BorderLayout.NORTH);
		JPanel p2 = new JPanel(); p2.setLayout(new BorderLayout()); p2.add(b,BorderLayout.CENTER);
		JLabel c = new JLabel("網頁 : "); JLabel d = new JLabel("<html><br>版本：OA_SPSA_04.07.07</html>");
		p2.add(c,BorderLayout.WEST); p.add(p2,BorderLayout.CENTER); p.add(d,BorderLayout.SOUTH);

		return p;
	}
	public void mouseDragged(MouseEvent arg0) {}
	public void mouseMoved(MouseEvent arg0) { textArea[3].setText(((JComponent) arg0.getSource()).getToolTipText());}
	public String getURL(int choice) {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.addChoosableFileFilter(new FileFilter(){
			public boolean accept(File arg0) {
				return arg0.getName().endsWith("sic") ? true : arg0.isDirectory() ? true : false;
			}
			public String getDescription() { return "SIC檔案(*.sic)"; }});
		fileChooser.addChoosableFileFilter(new FileFilter(){
			public boolean accept(File arg0) {
				return arg0.getName().endsWith("txt") ? true : arg0.isDirectory() ? true : false;
			}
			public String getDescription() { return "文字文件(*.txt)"; }});
		
		if(choice==0){
			fileChooser.setSelectedFile(new File("SIC-XE.txt"));// 預設開啟檔案
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				try { new FileReader( fileChooser.getSelectedFile().toURL().toString().substring(6) );
					return fileChooser.getSelectedFile().toURL().toString().substring(6);
				}catch (MalformedURLException a) {}catch (FileNotFoundException a) {}
		}
		else if(choice==1){
			fileChooser.setSelectedFile(new File("新文字文件.txt"));
			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				try {
					String a = fileChooser.getFileFilter().getDescription();
					String b = a.split("\\.")[1].substring(0,a.split("\\.")[1].length()-1);
					String c = fileChooser.getSelectedFile().toURL().toString().substring(6);
					return c.split("\\.")[c.split("\\.").length-1].equalsIgnoreCase(b) ? c:c+"."+b;
				}catch (MalformedURLException a) {}
		}
		return null;
	}
	private String changeDecRadix(int radix,int num,int z){//10->2,8,16
		String bitset = "";
	    final String []BIT={"0","1","2","3", "4","5","6","7", "8","9","A","B", "C","D","E","F"};
        if(radix<2 || radix > BIT.length ){ return "over radix range"; }
        else if(num==0){ return "0"; }
        else if(num<0){
        	String a = for0(changeDecRadix(2,0-num,0),z*4,0,"0"),b="";
        	for(int i=0;i<a.length();i++)
        		if(a.charAt(i)=='1') b =b +"0";
        		else b =b +"1";
        	int c = Integer.valueOf(b, 2);
        	return changeDecRadix(16,++c,0);
        }
        while( num > 0){
            bitset = BIT[ num % radix ] + bitset;
            num/=radix;
        }
        return bitset;
    }
	private String for0(String word,int l , int c,String key){//l補多長   c-0 前補key    c-1後補key     key補什麼
		if(word.length()<l)
			for(int i = word.length() ; i < l ; i++)
				if(c == 0) word = key + word;
				else word = word + key;
		return word;
	}
	public  boolean typecheckint(String a){
		if(a.charAt(0)=='#')a = a.substring(1, a.length());
		for(int i = 0 ; i <a.length() ;i++)
			if(a.codePointAt(i)<48 || a.codePointAt(i)>57)
				return false;
		return true;
	}
	public  String getRegnum(String reg){
		if(reg.equalsIgnoreCase("A"))  return "0";
		else if(reg.equalsIgnoreCase("B"))  return "3";
		else if(reg.equalsIgnoreCase("F"))  return "6";
		else if(reg.equalsIgnoreCase("L"))  return "2";
		else if(reg.equalsIgnoreCase("S"))  return "4";
		else if(reg.equalsIgnoreCase("T"))  return "5";
		else if(reg.equalsIgnoreCase("X"))  return "1";
		else if(reg.equalsIgnoreCase("PC")) return "8";
		else if(reg.equalsIgnoreCase("SW")) return "9";
		
		return null;
	}
	private String blend(String op,String n,String i){
		int intop = Integer.valueOf(op,16);
		if(n.equalsIgnoreCase("1")) intop = intop+2;
		if(i.equalsIgnoreCase("1")) intop = intop+1;
		return for0(changeDecRadix(16,intop,0),2,0,"0");
	}
	
	
	public class FileFormat {
		private OPcode opcode;
		private SMYTab smytab;
		private LITAB littab;
		private boolean error,pass;
		private String BASE;
		
		public FileFormat(){
			opcode = new OPcode();
			smytab = new SMYTab();
			littab = new LITAB();
			error = false;
			pass = false;
			BASE =null;
		}
		public int num_line(String URL){
			int num_line = 0;
			if(URL != null)
				try{BufferedReader stdin = new BufferedReader( new FileReader( URL ) );
			    	num_line = 0; for( ;stdin.ready();stdin.readLine() ) num_line ++;
				}catch (IOException e){ }
			else JOptionPane.showMessageDialog(null, "尚未選擇要開啟檔案!!","提示!", 1);
			return num_line ;
		}
		public String[][] format_A(String URL,int c){//A的array處理
			String A[][] = new String[c][5];
			setcheck(false);
			try {
				BufferedReader stdin = new BufferedReader( new FileReader( URL ) );
				StringTokenizer stoken = null;
				String word = "";
				
				for(int i=0 ; stdin.ready() ; i++ ){
					A[i][0] = String.valueOf((i+1)*5);
					stoken = new StringTokenizer( stdin.readLine() );
					word = stoken.nextToken( );
						
					if(word.equalsIgnoreCase(".")){//該行第一個token為註解
						A[i][1] = word;
						if(!stoken.hasMoreTokens()){}
						else{ A[i][2] = ""; while(stoken.hasMoreTokens()) A[i][2] = A[i][2]+stoken.nextToken( )+" ";}
					}
					else if(word.equalsIgnoreCase("BASE")){
						A[i][1] = null; A[i][2] = word;
						A[i][3] = stoken.nextToken( );
						A[i][4] = "";
							while(stoken.hasMoreTokens()){ A[i][4] = A[i][4]+stoken.nextToken( )+" "; }
					}
					else if(word.equalsIgnoreCase("LTORG")){
						A[i][1] = null; A[i][2] = word;
						A[i][3] = null;
						A[i][4] = "";
							while(stoken.hasMoreTokens()){ A[i][4] = A[i][4]+stoken.nextToken( )+" "; }
					}
					else{
						if(!opcode.contrastOPexist(word,0) && word.charAt(word.length()-1)!='+'){//該行第一個token為Label
							A[i][1] = word;
							word = stoken.nextToken();
						}//以下開始處理OP指令
						if(word.charAt(word.length()-1)=='+'){
							word = word + stoken.nextToken();
						}
						if(opcode.contrastOPexist(word,0)){
							A[i][2] = word;
							if(opcode.contrastOPtype(word)==0){
								A[i][3] = null;
							}
							if(opcode.contrastOPtype(word)==1){
								A[i][3] = stoken.nextToken( );
								if (A[i][3].charAt(A[i][3].length()-1)=='#' || A[i][3].charAt(A[i][3].length()-1)=='@' || A[i][3].charAt(A[i][3].length()-1)=='=')//預防控格
									A[i][3] = A[i][3] + stoken.nextToken();
								
								if (A[i][3].charAt(A[i][3].length()-1)==',' || A[i][3].charAt(A[i][3].length()-1)=='，')//預防控格
									A[i][3] = A[i][3] + stoken.nextToken();
								
							}
							if(opcode.contrastOPtype(word)==2){
								A[i][3] = stoken.nextToken( );
								if (A[i][3].charAt(A[i][3].length()-1)==',' || A[i][3].charAt(A[i][3].length()-1)=='，')//預防控格
									A[i][3] = A[i][3] + stoken.nextToken();
								
							}
							A[i][4] = "";//5存註解
							while(stoken.hasMoreTokens()){
								String a = stoken.nextToken( );
								if(a.charAt(0)==',') A[i][3] = A[i][3] + a;
								A[i][4] = A[i][4]+a+" ";
							}
						}
						else{
							A[i][2] = word;
							A[i][4] = "";//5存註解
							while(stoken.hasMoreTokens())
								A[i][4] = A[i][4]+stoken.nextToken( )+" ";
							JOptionPane.showMessageDialog(null, "語法錯誤!\n錯誤在第"+(i+1)*5+"行!","錯誤!", JOptionPane.ERROR_MESSAGE);
							setcheck(false);
							return A;
						}
					}
				}
			}
			catch (IOException e){}
			setcheck(true);
			return A;
		}
		public boolean getcheck(){ return error; }
		public void setcheck(boolean a){ error = a; }
		public String[][] format_B(String A[][]){
			String B[][] = new String[A.length][6];
			smytab = new SMYTab();
			littab = new LITAB();
			
			for(int i = 0 ; i<A.length ; i++){
				B[i][0] = A[i][0];
				for(int j = 1 ; j<A[i].length-1  ; j++) B[i][j+1] = A[i][j];
			}
			setcheck(false);
			pass = false;
			B = pass1(B);
			
			if(pass && pass2(B))
				setcheck(true);
			
			return B;
		}
		private String[][] pass1(String B[][]){
			int start = -1;
			for(int i= 0 ; i<B.length ; i++){
				if(B[i][2]!=null && B[i][2].equalsIgnoreCase(".")){//該行為註解
					B[i][1] = null;//記憶體先配置0
				}
				else{
					if(B[i][3].equalsIgnoreCase("START")){//讀到指令為START設定記憶體起始點
						start = Integer.parseInt(B[i][4],16);
						B[i][1] = String.valueOf(start);
					}
					else{
						if(start != -1){//如果沒有遇到START不會執行
							if(opcode.contrastOPexist(B[i][3],7)){//判斷是否為OP指令
								B[i][1] = String.valueOf(start);//先存入記憶體位址
								start = start+opcode.getFormat(B[i][3]);//所佔的記憶體格式
							}
							else if(B[i][3].equalsIgnoreCase("WORD")){//如果為WORD
								B[i][1] = String.valueOf(start);
								start = start+3;
							}
							else if(B[i][3].equalsIgnoreCase("RESW")){//如果為RESW
								B[i][1] = String.valueOf(start);
								start = start + (Integer.parseInt(B[i][4])*3);
							}
							else if(B[i][3].equalsIgnoreCase("RESB")){//如果為RESB
								B[i][1] = String.valueOf(start);
								start = start + Integer.parseInt(B[i][4]);
							}
							else if(B[i][3].equalsIgnoreCase("BYTE")){//如果為BYTE
								B[i][1] = String.valueOf(start);
								if(B[i][4].charAt(0)=='C' || B[i][4].charAt(0)=='c') start = start + getbytelong(B[i][4]);
								if(B[i][4].charAt(0)=='X' || B[i][4].charAt(0)=='x') start = start + getbytelong(B[i][4])/2;
							}
							else if(B[i][3].equalsIgnoreCase("BASE")){
								B[i][1] = null; BASE = B[i][4];
							}
							else if(B[i][3].equalsIgnoreCase("END")){
								B[i][1] = String.valueOf(start);
								for(int j = 0 ;littab.getliteralnum()>0 && j < littab.getliteralnum() ; j++){
									B = increasearray(B,i,j,start);
									i++;
									if(B[i][3].charAt(1)=='C' || B[i][3].charAt(1)=='c') start = start + getbytelong(B[i][3]);
									if(B[i][3].charAt(1)=='X' || B[i][3].charAt(1)=='x') start = start + getbytelong(B[i][3])/2;
								}
								littab.clearliteral();
								break;
							}
							else if(B[i][3].equalsIgnoreCase("EQU")){
								B[i][1] = mathematics(B[i][4]);
								B[i][1]= B[i][1]==null ? String.valueOf(start):B[i][1];
							}
							else if(B[i][3].equalsIgnoreCase("LTORG")){
								for(int j = 0 ;littab.getliteralnum()>0 && j < littab.getliteralnum() ; j++){
									B = increasearray(B,i,j,start);
									i++;
									if(B[i][3].charAt(1)=='C' || B[i][3].charAt(1)=='c') start = start + getbytelong(B[i][3]);
									if(B[i][3].charAt(1)=='X' || B[i][3].charAt(1)=='x') start = start + getbytelong(B[i][3])/2;
								}
								littab.clearliteral();
							}
							else{//旗標   非OP指令也非保留字
								B[i][1] = String.valueOf(start);
								JOptionPane.showMessageDialog(null, "第"+(i+1)*5+"行\n疑似語法錯誤!","錯誤!", JOptionPane.ERROR_MESSAGE);
								for(int j = 0 ; j<i+1 ; j++) if(B[j][1]!=null) B[j][1] = for0(changeDecRadix(16,Integer.parseInt(B[j][1]),0),4,0,"0");
								return B;
							}
							if(B[i][4]!=null && B[i][4].charAt(0)=='='){
								littab.addliteral(B[i][4]);
							}
							smytab.addlabel(B[i][2], B[i][1]);
						}
					}
				}
			}
			for(int i = 0 ; i<B.length ; i++) if(B[i][1]!=null)
				B[i][1] = for0(changeDecRadix(16,Integer.parseInt(B[i][1]),0),4,0,"0");
			pass = true;
			return B;
		}
		private String mathematics(String word){
			String a[] = {"+","-","*","/","%","|","&"};
			for(int i = 0 ;!word.equalsIgnoreCase("*") && word.length()!=1 && i < a.length ; i++ )
				if(word.indexOf(a[i])!=-1)
					switch(i){
						case 0: return String.valueOf(Integer.valueOf(smytab.getLoc(word.substring(0, word.indexOf(a[i]))),16) + Integer.valueOf(smytab.getLoc(word.substring(word.indexOf(a[i])+1, word.length())),16));
						case 1: return String.valueOf(Integer.valueOf(smytab.getLoc(word.substring(0, word.indexOf(a[i]))),16) - Integer.valueOf(smytab.getLoc(word.substring(word.indexOf(a[i])+1, word.length())),16));
						case 2: return String.valueOf(Integer.valueOf(smytab.getLoc(word.substring(0, word.indexOf(a[i]))),16) * Integer.valueOf(smytab.getLoc(word.substring(word.indexOf(a[i])+1, word.length())),16));
						case 3: return String.valueOf(Integer.valueOf(smytab.getLoc(word.substring(0, word.indexOf(a[i]))),16) / Integer.valueOf(smytab.getLoc(word.substring(word.indexOf(a[i])+1, word.length())),16));
						case 4: return String.valueOf(Integer.valueOf(smytab.getLoc(word.substring(0, word.indexOf(a[i]))),16) % Integer.valueOf(smytab.getLoc(word.substring(word.indexOf(a[i])+1, word.length())),16));
						case 5: return String.valueOf(Integer.valueOf(smytab.getLoc(word.substring(0, word.indexOf(a[i]))),16) | Integer.valueOf(smytab.getLoc(word.substring(word.indexOf(a[i])+1, word.length())),16));
						case 6: return String.valueOf(Integer.valueOf(smytab.getLoc(word.substring(0, word.indexOf(a[i]))),16) & Integer.valueOf(smytab.getLoc(word.substring(word.indexOf(a[i])+1, word.length())),16));
					}
			return null;
		}
		private String[][] increasearray(String B[][],int i,int j,int start){
			B = Arrays.copyOfRange(B, 0, B.length+1);
			for(int k = B.length-1 ; k > i ; k--){
				B[k] = Arrays.copyOfRange(B[k-1], 0, 6);
			}
			i++;
			String L = littab.getliteral(j);
			B[i][1] = String.valueOf(start);
			B[i][2] = "*"; B[i][3] = L; B[i][4] = null; B[i][5] = "";
			
			try{L = L.split("'")[1];}catch(Exception s){try{ L = L.split("’")[1]; }catch(Exception a){}}
			for(int a = 0 ; a< L.length() ; a++)
				if(B[i][3].charAt(1)=='C' || B[i][3].charAt(1)=='c') B[i][5] = B[i][5] + changeDecRadix(16,L.codePointAt(a),0);
				else if(B[i][3].charAt(1)=='X' || B[i][3].charAt(1)=='x') B[i][5] = B[i][5] + L.charAt(a);
				else{ JOptionPane.showMessageDialog(null, "第"+(i+1)*5+"行\n"+B[i][3]+" 的 '"+B[i][3].charAt(1)+"' 無法解析!","錯誤!", JOptionPane.ERROR_MESSAGE); return B; }
			smytab.addlabel(B[i][3], B[i][1]);
			return B;
		}
		private boolean pass2(String B[][]){
			for(int i = 0 ; i<B.length ; i++){
				if(B[i][3]!=null){
					if(B[i][3].equalsIgnoreCase("BYTE")){
						B[i][5] = ""; String L = B[i][4];
						try{L = L.split("'")[1];}catch(Exception s){try{ L = L.split("’")[1]; }catch(Exception a){}}
						if(L!=B[i][4])
							for(int a = 0 ; a< L.length() ; a++)
								if(B[i][4].charAt(0)=='C' || B[i][4].charAt(0)=='c') B[i][5] = B[i][5] + changeDecRadix(16,L.codePointAt(a),0);
								else if(B[i][4].charAt(0)=='X' || B[i][4].charAt(0)=='x') B[i][5] = B[i][5] + L.charAt(a);
								else{
									JOptionPane.showMessageDialog(null, "第"+(i+1)*5+"行\n"+B[i][4]+" 的 '"+B[i][4].charAt(0)+"' 無法解析!","錯誤!", JOptionPane.ERROR_MESSAGE);
									return false;
								}
						else{
							JOptionPane.showMessageDialog(null, "第"+(i+1)*5+"行\n"+B[i][4]+" 無法解析!","錯誤!", JOptionPane.ERROR_MESSAGE);
							return false;
						}
					}
					else if(B[i][3].equalsIgnoreCase("WORD")){
						B[i][5] = for0(changeDecRadix(16,Integer.parseInt(B[i][4]),0),6,0,"0");
					}
					else if(opcode.contrastOPexist(B[i][3],7)){
						String n="0",I="0",x="0",b="0",p="0",e="0";
						int format = opcode.getFormat(B[i][3]);
						int num = opcode.contrastOPtype(B[i][3]);
						String OPC = opcode.getOPcode(B[i][3]);
						String label = B[i][4];
						String program = null;
						String address = null;
						String disp = null;
						
						switch( format ){
						case 1: B[i][5] = OPC; break;
						case 2:
							switch( num ){
								case 1: B[i][5] = OPC + getRegnum(gettoken(B[i][4], 1, ","))+"0"; break;
								case 2: B[i][5] = OPC + getRegnum(gettoken(B[i][4], 1, ",")) + getRegnum(gettoken(B[i][4], 2, ",")); break;
							} break;
						case 3:
							n = "1" ; I = "1" ; p = "1";
							switch( num ){
							case 0: B[i][5] = for0( blend(OPC,n,I),6,1,"0" ); break;
							case 1:
								if(label.charAt(0)=='#'){ n = "0" ; I = "1"; label = label.substring(1, B[i][4].length()); }
								else if(label.charAt(0)=='@'){ n = "1" ; I = "0"; label = label.substring(1, B[i][4].length()); }
								if(label.length()>2 && label.charAt(label.length()-2)==',' && label.charAt(label.length()-1)=='X'){ x = "1" ; label = label.substring(0, label.length()-2); }
								
								program = Integer.toHexString((Integer.valueOf(B[i][1],16) + opcode.getFormat(B[i][3])));
								
								if(typecheckint(label)){
									address = changeDecRadix(16,Integer.valueOf(label),0);
									program = "0"; p = "0";
								}
								else address = smytab.getLoc(label);
								
								if(address==null){
									JOptionPane.showMessageDialog(null, "找不到"+label+"標籤!!\n在第"+(i+1)*5+"行","錯誤!", JOptionPane.ERROR_MESSAGE); 
									return false;
								}
								int d = Integer.valueOf(address, 16) - Integer.valueOf(program,16);
								
								if(( d>2047 || d< -2048 ) && (!typecheckint(label))){
									p = "0";
									program = smytab.getLoc(BASE);
									
									if(program==null){
										JOptionPane.showMessageDialog(null, "找不到Base!"+(i+1)*5+"行","錯誤!", JOptionPane.ERROR_MESSAGE);
										return false;
									}
									d = Integer.valueOf(address, 16) - Integer.valueOf(program,16);
									b = "1";
								}
								
								disp = for0(changeDecRadix(16,d,format),3,0,"0");
								B[i][5] = blend(OPC,n,I) + changeDecRadix(16,Integer.valueOf(x+b+p+e,2),0) + disp;
								break;
							}
							break;
						case 4:
							n="1";I="1";e="1";p="0";
							
							if(label.charAt(0)=='#'){ n = "0" ; I = "1"; label = label.substring(1, B[i][4].length()); }
							else if(label.charAt(0)=='@'){ n = "1" ; I = "0"; label = label.substring(1, B[i][4].length()); }
							if(label.length()>2 && label.charAt(label.length()-2)==',' && label.charAt(label.length()-1)=='X'){ x = "1" ; label = label.substring(0, label.length()-2); }
							
							if(typecheckint(label)) address = changeDecRadix(16,Integer.valueOf(label),0);
							else address = smytab.getLoc(label);
							
							if(address==null){
								JOptionPane.showMessageDialog(null, "找不到"+label+"\n在第"+(i+1)*5+"行","錯誤!", JOptionPane.ERROR_MESSAGE);
								return false;
							}
							B[i][5] = blend(OPC,n,I) + changeDecRadix(16,Integer.valueOf(x+b+p+e,2),0) +for0(address,5,0,"0");
							break;
						}
					}
				}
			}
			return true;
		}
		public void save_A2(String URL,String file){//要傳"位置"，"內容"
			if(URL != null){
				String setfile = "";
				try{
			      FileWriter fwriter=new FileWriter(new File(URL));
			      for(int i=0 ; i<file.length() ; i++)
			    	  if(file.charAt(i) == '\n') setfile = setfile + "\r\n";
			    	  else setfile = setfile + file.charAt(i);
			      fwriter.write(setfile);
			      fwriter.close();
			    }
			    catch(Exception e){}
			}
		}
		public String getheadrecord(String B[][]){
			String h_start = "0",h_name = "",h_end = "";
			for(int i = 0 ; i < B.length ; i++)
				if(B[i][3]!=null && B[i][3].equalsIgnoreCase("START")){ h_name = B[i][2]; h_start = B[i][1]; }
				else if(B[i][3]!=null && B[i][3].equalsIgnoreCase("END")) h_end = B[i][1];
			
			h_end = for0(changeDecRadix(16,Integer.parseInt(h_end,16) - Integer.parseInt(h_start,16),0),6,0,"0");//for0(h_end,6,0,"0");
			h_name = for0(h_name,6,1," ");
			h_start = for0(h_start,6,0,"0");
			return "H" + h_name + h_start + h_end;
		}
		public String gettextrecord(String B[][]){
			String body = "";
			int L = 0,a = 0,b = 0;
			for(int i = 0 ; i < B.length ; i++)
				if(B[i][1]!=null)
					if(B[i][5]!=null && L+B[i][5].length()<=60){
						body = body +(body.length()<=0?("T"+for0(B[i][1],6,0,"0")+">>"):a!=0 ? ("\nT"+for0(B[i][1],6,0,"0")+">>") : "")+ B[i][5];
						L = L +B[i][5].length();
						a=0;
					}
					else if(B[i][5]==null || L+B[i][5].length()>60){
						a++; L=0;
						i = i - (B[i][5]==null ? 0 : 1);
					}
			for(int i = body.length()-1 ; i>=0 ; i--){ b++;
				if(body.charAt(i)=='>') body = body.substring(0, i-1) + for0(changeDecRadix(16,b/2,0),2,0,"0") + body.substring(i+1, body.length());
				if(body.charAt(i)=='\n') b=0;
			}
			
			return body;
		}
		public String getendrecord(String B[][]){
			String tail = "E";
			for(int i = 0 ; i < B.length ; i++)
				if(B[i][3]!=null && opcode.contrastOPexist(B[i][3],7)){ tail = tail+for0(B[i][1],6,0,"0"); break; }
			return tail;
		}
		public String getmodifyrecord(String B[][]){
			String M = "";int L = 0;
			for(int i = 0 ; i < B.length ; i++)
				if(B[i][5]!=null){
					if(B[i][3].charAt(0)=='+' && !typecheckint(B[i][4]))
						M = M+(M.equalsIgnoreCase("")?"":"\n")+ "M" + for0(Integer.toHexString(L+1),6,0,"0")+"05";
					L = L+B[i][5].length()/2;
				}
			return M;
		}
		private int getbytelong(String word){
			int L = 0;
			for(int w = 0 ; w<word.length() ; w++)
				if(word.charAt(w)=='\'' || word.charAt(w)=='’')
					for(int w2 = w+1 ; w2<word.length() ; w2++)
						if(word.charAt(w2)=='\'' || word.charAt(w2)=='’') break;
						else{ L++; w++;}
			return L;
		}
		private String gettoken(String word,int which,String keyword){
			int a=0,b=0,f=0,n=0;
			for(int i = 0 ; i<word.length() ; i++){
				a = i;
				for(int e = 0 ; e<keyword.length() ; e++)
					if(word.charAt(i+e)==keyword.charAt(e)) f++;
					else break;
				if(f==keyword.length()){
					a = i-1; n++;
					if(n==which) break;
					else b=i+ keyword.length();
				}
				f = 0;
			}
			return word.substring(b, a+1);
		}
	}
	
	public class SMYTab {
		private String Label[];
		private String Loc[];
		public SMYTab(){
			Label = new String[0];
			Loc = new String[0];
		}
		public void addlabel(String label,String loc){
			if(label!=null && !label.equalsIgnoreCase("*"))
				if(!chackhaveduplicatelabel(label)){
					Label = Arrays.copyOfRange(Label, 0, Label.length+1);
					Loc = Arrays.copyOfRange(Loc, 0, Loc.length+1);
					Label[Label.length-1] = label;
					Loc[Loc.length-1] = changeDecRadix(16,Integer.valueOf(loc),0);
				}
				else JOptionPane.showMessageDialog(null, "重複標籤!!","錯誤!", JOptionPane.ERROR_MESSAGE);
		}
		public String getLoc(String label){
			if(label!=null)
				for(int i = 0 ; i <Label.length ; i++)
					if(Label[i].equalsIgnoreCase(label))
						return for0(Loc[i],4,0,"0");
			return null;
		}
		private boolean chackhaveduplicatelabel(String word){
			for(int i = 0 ; i <Label.length  ;i++)
				if(Label[i].equalsIgnoreCase(word))
					return true; 
			return false;
		}
	}
	
	public class OPcode {
		private String OP[][] = {/*指令   後面個數   OP碼   出現個數   格式*/
				{"ADD", "1","18","0","3/4"}, {"ADDF",  "1","58","0","3/4"}, {"ADDR",  "2","90","0","2"  }, {"AND", "1","40","0","3/4"}, {"CLEAR","1","B4","0","2"  },
				{"COMP","1","28","0","3/4"}, {"COMPPF","1","88","0","3/4"}, {"COMPR", "2","A0","0","2"  }, {"DIV", "1","24","0","3/4"}, {"DIVF", "1","64","0","3/4"},
				{"DIVR","2","9C","0","2"  }, {"FIX",   "0","C4","0","1"  }, {"FLOAT", "0","C0","0","1"  }, {"HIO", "0","F4","0","1"  }, {"J",    "1","3C","0","3/4"},
				{"JEQ", "1","30","0","3/4"}, {"JGT",   "1","34","0","3/4"}, {"JLT",   "1","38","0","3/4"}, {"JSUB","1","48","0","3/4"}, {"LDA",  "1","00","0","3/4"},
				{"LDB", "1","68","0","3/4"}, {"LDCH",  "1","50","0","3/4"}, {"LDF",   "1","70","0","3/4"}, {"LDL", "1","08","0","3/4"}, {"LDS",  "1","6C","0","3/4"},
				{"LDT", "1","74","0","3/4"}, {"LDX",   "1","04","0","3/4"}, {"LPS",   "1","D0","0","3/4"}, {"MUL", "1","20","0","3/4"}, {"MULF", "1","60","0","3/4"},
				{"MULR","2","98","0","2"  }, {"NORM",  "0","C8","0","1"  }, {"OR",    "1","44","0","3/4"}, {"RD",  "1","D8","0","3/4"}, {"RMO",  "2","AC","0","2"  },
				{"RSUB","0","4C","0","3/4"}, {"SHIFTL","2","A4","0","2"  }, {"SHIFTR","2","A8","0","2"  }, {"SIO", "0","F0","0","1"  }, {"SSK",  "1","EC","0","3/4"},
				{"STA", "1","0C","0","3/4"}, {"STB",   "1","78","0","3/4"}, {"STCH",  "1","54","0","3/4"}, {"STF", "1","80","0","3/4"}, {"STI",  "1","D4","0","3/4"},
				{"STL", "1","14","0","3/4"}, {"STS",   "1","7C","0","3/4"}, {"STSW",  "1","E8","0","3/4"}, {"STT", "1","84","0","3/4"}, {"STX",  "1","10","0","3/4"},
				{"SUB", "1","1C","0","3/4"}, {"SUBF",  "1","5C","0","3/4"}, {"SUBR",  "2","94","0","2"  }, {"SVC", "1","B0","0","2"  }, {"TD",   "1","E0","0","3/4"},
				{"TIO", "0","F8","0","1"  }, {"TIX",   "1","2C","0","3/4"}, {"TIXR",  "1","B8","0","2"  }, {"WD",  "1","DC","0","3/4"}, 
				{"BYTE","1","xx","0"},{"WORD","1","xx","0"},{"START","1","xx","0"},{"END","1","xx","0"},{"RESB","1","xx","0"},{"RESW","1","xx","0"},{"EQU","1","xx","0"}};
		
		public boolean contrastOPexist(String word,int c){//判斷word是否為OP  c決定是否連保留字一起判斷   6只判斷OP
			if(word.charAt(0)=='+') word = word.substring(1, word.length());
			for(int i = 0 ; i<(OP.length-c) ; i++)
				if(word.equalsIgnoreCase(OP[i][0])) return true;
			return false;
		}
		public int contrastOPtype(String word){//回傳該word後面運算元有多少個
			if(word.charAt(0)=='+') word = word.substring(1, word.length());
			for(int i = 0 ; i<OP.length ; i++)
				if(word.equalsIgnoreCase(OP[i][0])) return Integer.parseInt(OP[i][1]);
			return -1;
		}
		public String getOPcode(String word){//回傳該word的OPcode
			if(word.charAt(0)=='+') word = word.substring(1, word.length());
			for(int i = 0 ; i<OP.length-6 ; i++)
				if(word.equalsIgnoreCase(OP[i][0])) return OP[i][2];
			return null;
		}
		public int getFormat(String word){//回傳word的OP格式
			int c=0;
			if(word.charAt(0)=='+'){ word = word.substring(1, word.length()); c++; }
			for(int i = 0 ; i<OP.length-6 ; i++)
				if(word.equalsIgnoreCase(OP[i][0]))
					if(OP[i][4].equalsIgnoreCase("3/4")) return c==1 ? 4 : 3;
					else return Integer.valueOf(OP[i][4]);
			return -1;
		}
	}
	public class LITAB{
		private String Literal[];
		public LITAB(){
			Literal = new String[0];
		}
		public void addliteral(String word){
			if(!chackhaveduplicateliteral(word)){
				Literal = Arrays.copyOfRange(Literal, 0, Literal.length+1);
				Literal[Literal.length-1] = word;
			}
		}
		public int getliteralnum(){ return Literal.length; }
		public String getliteral(int i){ return Literal[i]; }
		public void clearliteral(){ Literal = new String[0]; }
		private boolean chackhaveduplicateliteral(String word){
			for(int i = 0 ; i <Literal.length  ;i++)
				if(Literal[i].equalsIgnoreCase(word)) return true; 
			return false;
		}
	}
	
	public static void main(String[] args) {
		try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception e){e.printStackTrace();}
		
		Assembler myframe = new Assembler("Assembler");
		
		myframe.setVisible(true);
		myframe.setSize(1200, 750);
		myframe.setExtendedState(Frame.MAXIMIZED_BOTH);
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}