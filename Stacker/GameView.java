import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;

import java.util.HashMap;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;

/**
 * Responsible for setting up all visuals, including user options.
 *
 */
public class GameView extends JFrame implements java.io.Serializable {

	private JLayeredPane contentPane;
	private JLabel currency;
	private JLabel storeMoney;
	private Terminator end;
	private ArrayList<JLabel> rect;
	private StackerGame gamePanel;
	private String currentScreen;
	private int money;
	private boolean started;
	private HashMap<String, Integer> saveState;
	String fileName;
	ImageIcon[] optionImages;
	String[] optionNames;
	String[] optionNamesStore;
	int[] optionPrices;
	boolean[] optionOwned;
	int currentStoreOption;
	int currentOptionSelected;

	/**
	 * Create the frame.
	 */
	public GameView(Terminator end1) {
		//create game window
		setPreferredSize(new Dimension(1000, 700));
		setTitle("Stacker");
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,1000,700);
		setResizable(false);
		contentPane = new JLayeredPane();
		contentPane.setPreferredSize(new Dimension(1000, 700));
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		//initialize options based on save state
		currentScreen = "Menu";
		started = false;
		rect = new ArrayList<JLabel>();
		rect.add(new JLabel(""));
		rect.remove(0);
		end = end1;
		saveState = new HashMap<String, Integer>();
		fileName = "save_state/saveState.txt";
		load();
		money = saveState.get("Money");
		
		//options for store
		optionImages = new ImageIcon[2];
		optionImages[0] = new ImageIcon(this.getClass().getResource("images/whiteRectangle.png"));
		optionImages[1] = new ImageIcon(this.getClass().getResource("images/StackerChocolateBar4.png"));
		
		optionNames = new String[4];
		optionNames[0] = "images/whiteRectangle.png";
		optionNames[1] = "images/whiteRectangleFaded.png";
		optionNames[2] = "images/StackerChocolateBar4.png";
		optionNames[3] = "images/fadedChocBar3.png";
		
		optionNamesStore = new String[2];
		optionNamesStore[0] = "White Rectangle";
		optionNamesStore[1] = "Chocolate Bar";
		
		optionPrices = new int[2];
		optionPrices[0] = 0;
		optionPrices[1] = 100;
		
		optionOwned = new boolean[2];
		optionOwned[0] = true;
		optionOwned[1] = saveState.get("Option2Owned") == 1;
		
		
		currentStoreOption = 0;
		//map key OptionSelected
		currentOptionSelected = saveState.get("OptionSelected");
		
		
		//create and initialize "Instructions" option in main menu
		JPanel instPanel = new JPanel();
		instPanel.setBounds(0, 0, 1000, 700);
		instPanel.setPreferredSize(new Dimension(1000, 700));
		instPanel.setVisible(false);
		contentPane.setLayout(null);
		instPanel.setForeground(Color.WHITE);
		contentPane.setLayer(instPanel, 1);
		instPanel.setBackground(Color.BLACK);
		contentPane.add(instPanel);
		instPanel.setLayout(null);
		
		//sets up instructions window
		JLabel lblNewLabel_1 = new JLabel("Instructions");
		lblNewLabel_1.setFont(new Font("Sylfaen", Font.BOLD, 30));
		lblNewLabel_1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel_1.setBackground(Color.BLACK);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setBounds(389, 67, 171, 50);
		instPanel.add(lblNewLabel_1);
		
		//return to main menu from instructions
		JLabel lblReturnToMain = new JLabel("Return to Main Menu");
		lblReturnToMain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblReturnToMain.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblReturnToMain.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(currentScreen.equals("Instructions"))
				{
					instPanel.setVisible(false);
					currentScreen = "Menu";
				}
			}
		});
		lblReturnToMain.setVerticalAlignment(SwingConstants.BOTTOM);
		lblReturnToMain.setForeground(Color.WHITE);
		lblReturnToMain.setFont(new Font("Sylfaen", Font.BOLD, 30));
		lblReturnToMain.setBackground(Color.BLACK);
		lblReturnToMain.setBounds(330, 552, 303, 50);
		instPanel.add(lblReturnToMain);
		
		//displays info
		JTextPane txtpnInTheGame = new JTextPane();
		txtpnInTheGame.setText("In Stacker, the objective is to stack blocks "
				+ "on top of each other until you reach the top. Press the [enter] "
				+ "key to stop the moving rectangle. If any part of the rectangle is "
				+ "hanging off the edge, it will be cut off. The game is over once the "
				+ "rectangle misses the stack. Good Luck!");
		txtpnInTheGame.setFont(new Font("Sylfaen", Font.ITALIC, 24));
		txtpnInTheGame.setForeground(Color.WHITE);
		txtpnInTheGame.setBackground(Color.BLACK);
		txtpnInTheGame.setBounds(200, 200, 600, 300);
		instPanel.add(txtpnInTheGame);
		
		//creates and initializes "Store" option in main menu
		JPanel storePanel = new JPanel();
		storePanel.setBounds(0, 0, 1000, 700);
		storePanel.setPreferredSize(new Dimension(1000, 700));
		storePanel.setVisible(false);
		storePanel.setForeground(Color.WHITE);
		contentPane.setLayer(storePanel, 4);
		storePanel.setBackground(Color.BLACK);
		contentPane.add(storePanel);
		storePanel.setLayout(null);
		
		//sets up store window
		JLabel lblNewLabel_2 = new JLabel("Store");
		lblNewLabel_2.setFont(new Font("Sylfaen", Font.BOLD, 30));
		lblNewLabel_2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel_2.setBackground(Color.BLACK);
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBounds(450, 67, 100, 50);
		storePanel.add(lblNewLabel_2);
		
		//return to main menu from store
		JLabel lblReturnToMain2 = new JLabel("Return to Main Menu");
		lblReturnToMain2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblReturnToMain2.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblReturnToMain2.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(currentScreen.equals("Store"))
				{
					storePanel.setVisible(false);
					currentScreen = "Menu";
				}
			}
		});
		lblReturnToMain2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblReturnToMain2.setForeground(Color.WHITE);
		lblReturnToMain2.setFont(new Font("Sylfaen", Font.BOLD, 30));
		lblReturnToMain2.setBackground(Color.BLACK);
		lblReturnToMain2.setBounds(330, 552, 303, 50);
		storePanel.add(lblReturnToMain2);
		
		//sets up display for different store options
		JLabel lblOptionImage = new JLabel(optionImages[0]);
		lblOptionImage.setFont(new Font("Sylfaen", Font.BOLD, 20));
		lblOptionImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptionImage.setBackground(Color.BLACK);
		lblOptionImage.setForeground(Color.WHITE);
		lblOptionImage.setBounds(381, 292, 197, 100);
		storePanel.add(lblOptionImage);
		
		JLabel lblOptionName = new JLabel(optionNamesStore[0]);
		lblOptionName.setFont(new Font("Sylfaen", Font.ITALIC, 15));
		lblOptionName.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptionName.setForeground(Color.WHITE);
		lblOptionName.setBackground(Color.BLACK);
		lblOptionName.setBounds(394, 214, 172, 40);
		storePanel.add(lblOptionName);
		
		JLabel lblOptionPrice = new JLabel("Selected");
		if(currentOptionSelected != 0)
		{
			lblOptionPrice.setText("Select");
		}
		
		lblOptionPrice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblOptionPrice.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblOptionPrice.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(lblOptionPrice.getText().equals("Select"))
				{
					gamePanel.setImages(optionNames[currentStoreOption*2], optionNames[currentStoreOption*2+1]);
					currentOptionSelected = currentStoreOption;
					lblOptionPrice.setText("Selected");
					saveState.replace("OptionSelected", currentStoreOption);
					save();
				}
				else if(!lblOptionPrice.getText().equals("Selected"))
				{
					if(money >= optionPrices[currentStoreOption])
					{
						money -= optionPrices[currentStoreOption];
						addMoney(0);
						optionOwned[currentStoreOption]= true;
						gamePanel.setImages(optionNames[currentStoreOption*2], optionNames[currentStoreOption*2+1]);
						currentOptionSelected = currentStoreOption;
						lblOptionPrice.setText("Selected");
						saveState.replace("OptionSelected", currentStoreOption);
						save();
						if(currentStoreOption == 1)
						{
							saveState.replace("Option2Owned", 1);
							save();
						}
						
					}
				}
			}
		});
		lblOptionPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptionPrice.setBackground(Color.BLACK);
		lblOptionPrice.setFont(new Font("Sylfaen", Font.ITALIC, 18));
		lblOptionPrice.setForeground(Color.WHITE);
		lblOptionPrice.setBounds(394, 419, 172, 40);
		storePanel.add(lblOptionPrice);
		
		//sets up navigation for store interface
		JButton btnNewButton = new JButton(">");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentStoreOption+1 != optionPrices.length)
				{
					currentStoreOption+=1;
					lblOptionImage.setIcon(optionImages[currentStoreOption]);
					lblOptionName.setText(optionNamesStore[currentStoreOption]);
					if(optionOwned[currentStoreOption])
					{
						if(currentOptionSelected == currentStoreOption)
						{
							lblOptionPrice.setText("Selected");
						}
						else
						{
							lblOptionPrice.setText("Select");
						}
						
					}
					else
					{
						lblOptionPrice.setText("Price: " + optionPrices[currentStoreOption]);
					}
					
				}
					
			}
		});
		btnNewButton.setFont(new Font("Sylfaen", Font.BOLD, 20));
		btnNewButton.setBackground(Color.BLACK);
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBounds(668, 292, 50, 100);
		storePanel.add(btnNewButton);
		
		JButton button = new JButton("<");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentStoreOption-1 != -1)
				{
					currentStoreOption-=1;
					lblOptionImage.setIcon(optionImages[currentStoreOption]);
					lblOptionName.setText(optionNamesStore[currentStoreOption]);
					if(optionOwned[currentStoreOption])
					{
						if(currentOptionSelected == currentStoreOption)
						{
							lblOptionPrice.setText("Selected");
						}
						else
						{
							lblOptionPrice.setText("Select");
						}
					}
					else
					{
						lblOptionPrice.setText("Price: " + optionPrices[currentStoreOption]);
					}
				}
			}
		});
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Sylfaen", Font.BOLD, 20));
		button.setBackground(Color.BLACK);
		button.setBounds(251, 292, 50, 100);
		storePanel.add(button);
		
		//sets up display for coins
		storeMoney = new JLabel("Coins: " + money);
		storeMoney.setForeground(Color.WHITE);
		storeMoney.setFont(new Font("Sylfaen", Font.BOLD, 20));
		storeMoney.setBackground(Color.BLACK);
		storeMoney.setBounds(50, 50, 100, 50);
		storePanel.add(storeMoney);
		
		//sets up game display
		gamePanel = new StackerGame(this, end);
		gamePanel.setBounds(0, 0, 1000, 700);
		gamePanel.setPreferredSize(new Dimension(1000, 700));
		gamePanel.setVisible(false);
		gamePanel.setForeground(Color.WHITE);
		contentPane.setLayer(gamePanel, 3);
		gamePanel.setBackground(Color.BLACK);
		contentPane.add(gamePanel);
		gamePanel.setLayout(null);
		gamePanel.setImages(optionNames[currentOptionSelected*2], optionNames[currentOptionSelected*2+1]);
		
		//sets up game options menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(Color.WHITE);
		menuBar.setBackground(Color.BLACK);
		menuBar.setBounds(0, 0, 101, 22);
		gamePanel.add(menuBar);
		
		JMenu mnGameOptions = new JMenu("Game Options");
		mnGameOptions.setOpaque(true);
		mnGameOptions.setForeground(Color.WHITE);
		mnGameOptions.setBackground(Color.BLACK);
		menuBar.add(mnGameOptions);
		
		JMenuItem mntmReturnToMain = new JMenuItem("Return to Main Menu");
		mntmReturnToMain.setOpaque(true);
		mntmReturnToMain.setForeground(Color.WHITE);
		mntmReturnToMain.setBackground(Color.BLACK);
		mntmReturnToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				end.setEndGame(true);
				gamePanel.setVisible(false);
				setStart(false);
				addMoney((gamePanel.layer-1)*-2);
				currentScreen = "Menu";
			}
		});
		mnGameOptions.add(mntmReturnToMain);
		
		JMenuItem mntmResartCurrentGame = new JMenuItem("Resart Current Game");
		mntmResartCurrentGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				end.setEndGame(true);
				addMoney((gamePanel.layer-1)*-2);
				setStart(true);
			}
		});
		mntmResartCurrentGame.setOpaque(true);
		mntmResartCurrentGame.setForeground(Color.WHITE);
		mntmResartCurrentGame.setBackground(Color.BLACK);
		mnGameOptions.add(mntmResartCurrentGame);
		
		//sets up title for home page
		JLabel lblNewLabel = new JLabel("Main Menu");
		lblNewLabel.setBounds(400, 100, 200, 50);
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Sylfaen", Font.BOLD, 30));
		contentPane.add(lblNewLabel);
		
		//sets up display for play game option in main menu
		JLabel lblPlayGame = new JLabel("Play Game");
		lblPlayGame.setBounds(150, 300, 120, 50);
		lblPlayGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblPlayGame.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblPlayGame.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(currentScreen.equals("Menu"))
				{
					end.setEndGame(false);
					setStart(true);
					gamePanel.setVisible(true);
					currentScreen = "Game";
				}
			}
		});
		lblPlayGame.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPlayGame.setForeground(Color.WHITE);
		lblPlayGame.setBackground(Color.BLACK);
		lblPlayGame.setFont(new Font("Sylfaen", Font.ITALIC, 24));
		contentPane.add(lblPlayGame);
		
		//sets up display for quit option in main menu
		JLabel lblQuit = new JLabel("Quit");
		lblQuit.setBounds(700, 500, 50, 50);
		lblQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblQuit.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblQuit.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(currentScreen.equals("Menu"))
				{
					System.exit(0);
				}
				
			}
		});
		lblQuit.setVerticalAlignment(SwingConstants.BOTTOM);
		lblQuit.setForeground(Color.WHITE);
		lblQuit.setFont(new Font("Sylfaen", Font.ITALIC, 24));
		lblQuit.setBackground(Color.BLACK);
		contentPane.add(lblQuit);
		
		//sets up display for instructions option in main menu
		JLabel lblInstructions = new JLabel("Instructions");
		lblInstructions.setBounds(635, 300, 130, 50);
		lblInstructions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblInstructions.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblInstructions.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(currentScreen.equals("Menu"))
				{
					instPanel.setVisible(true);
					currentScreen = "Instructions";
				}
				
			}
		});
		lblInstructions.setVerticalAlignment(SwingConstants.BOTTOM);
		lblInstructions.setForeground(Color.WHITE);
		lblInstructions.setFont(new Font("Sylfaen", Font.ITALIC, 24));
		lblInstructions.setBackground(Color.BLACK);
		contentPane.add(lblInstructions);
		
		//sets up display for store option in main menu
		JLabel lblStore = new JLabel("Store");
		lblStore.setBounds(150, 500, 60, 50);
		lblStore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblStore.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblStore.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(currentScreen.equals("Menu"))
				{
					storePanel.setVisible(true);
					currentScreen = "Store";
				}
				
			}
		});
		lblStore.setVerticalAlignment(SwingConstants.BOTTOM);
		lblStore.setForeground(Color.WHITE);
		lblStore.setFont(new Font("Sylfaen", Font.ITALIC, 24));
		lblStore.setBackground(Color.BLACK);
		contentPane.add(lblStore);
		
		currency = new JLabel("Coins: " + money);
		currency.setBounds(50, 50, 100, 50);
		currency.setBackground(Color.BLACK);
		currency.setForeground(Color.WHITE);
		currency.setFont(new Font("Sylfaen", Font.BOLD, 20));
		contentPane.add(currency);

		setContentPane(contentPane);
		
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setForeground(Color.WHITE);
		menuBar_1.setBackground(Color.BLACK);
		menuBar_1.setBounds(0, 0, 120, 22);
		contentPane.add(menuBar_1);
		
		//sets up save state options in main menu
		JMenu mnSaveStateOptions = new JMenu("Save state options");
		mnSaveStateOptions.setBackground(Color.BLACK);
		mnSaveStateOptions.setForeground(Color.WHITE);
		menuBar_1.add(mnSaveStateOptions);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Delete current save state");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveState.replace("Money", 0);
				saveState.replace("OptionSelected", 0);
				saveState.replace("Option2Owned", 0);
				optionOwned[1] = false;
				currentOptionSelected = 0;
				currentStoreOption = 0;
				save();
				money = saveState.get("Money");
				lblOptionImage.setIcon(optionImages[currentStoreOption]);
				lblOptionName.setText(optionNames[currentStoreOption*2]);
				gamePanel.setImages(optionNames[currentStoreOption*2], optionNames[currentStoreOption*2+1]);
				lblOptionPrice.setText("Selected");
				addMoney(0);
			}
		});
		mntmNewMenuItem.setForeground(Color.WHITE);
		mntmNewMenuItem.setBackground(Color.BLACK);
		mnSaveStateOptions.add(mntmNewMenuItem);
		
		//this option is used for testing purposes.
		
		/*JMenuItem mntmAddCoins = new JMenuItem("Add 100 Coins");
		mntmAddCoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMoney(100);
			}
		});
		mnSaveStateOptions.add(mntmAddCoins);*/
		
		pack();
		setVisible(true);
	}
	/**
	 * Returns information about whether game has started or not.
	 * @return
	 * 		Status of game.
	 */
	public boolean gameStart()
	{
		return started;
	}
	/**
	 * Sets status of game start.
	 * @param b
	 * 		Boolean indicating if game has started or not
	 */
	public void setStart(boolean b)
	{
		started = b;
	}
	/**
	 * Begins new game.
	 */
	public void beginNewGame()
	{
		gamePanel.newGame();
	}
	/**
	 * Returns to main menu screen.
	 */
	public void returnToMainMenu()
	{
		end.setEndGame(true);
		gamePanel.setVisible(false);
		setStart(false);
		currentScreen = "Menu";
	}
	/**
	 * Adds money to coin total and refreshes display.
	 * @param amount
	 * 		Amount of currency to be added
	 */
	public void addMoney(int amount)
	{
		money += amount;
		currency.setText("Coins: " + money);
		storeMoney.setText("Coins: " + money);
		saveState.replace("Money", money);
		save();
	}
	/**
	 * Saves current game start.
	 */
	public void save()
	{
		try
		{
			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(saveState);
			out.close();
			file.close();
			System.out.println("Saved successfully!");
		}
		catch(IOException ex)
		{
			System.out.println("Failure to save.");
		}
	}
	/**
	 * Loads saved game state.
	 */
	public void load()
	{
		try
		{
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);
			saveState.putAll((HashMap<String, Integer>)in.readObject());
			in.close();
			file.close();
			System.out.println("Loaded successfully!");
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("Save state not found. Recreating...");
			CreateSaveState();
			InitializeSaveState();
		}
		catch(IOException ex)
		{
			System.out.println("Failed to load. Reinitializing save state...");
			InitializeSaveState();
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println("Failure to load.");
		}
	}
	/**
	 * Initializes all default key value pairs for the save state.
	 */
	private void InitializeSaveState()
	{
		saveState.put("Money", 0);
		saveState.put("Option2Owned", 0);
		saveState.put("OptionSelected", 0);
		save();
	}
	/**
	 * Creates files necessary for save state.
	 */
	private void CreateSaveState()
	{
		File target = new File(fileName);
		File parent = target.getParentFile();
		parent.mkdirs();
	}
}
