package MySQL.Viewer;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MySQLViewer extends JFrame {

    public static final String SQL_ALL_ROWS = "SELECT * FROM %s;";

    private Connection connection;
    private static String url;

    String database = "auction";
    String host = "localhost";
    String port = "3306";
    String USER = "root";
    String PASS = "";

    public MySQLViewer() {
        // Basic window settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        setSize(width, height);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("MySQL Viewer");
        // Initialize main window components
        initComponents();
        // Render window content
        setVisible(true);
    }

    private void buildInput(JPanel selectFilePanel, 
    JTextField textField, GridBagConstraints c, 
    String lbl, String fldName, int y){
        c.gridx = 0;
        c.gridy = y;
        c.gridwidth = 1;
        selectFilePanel.add(new JLabel(lbl), c);

        textField.setName(fldName);
        textField.setColumns(35);
        c.gridx = 1;
        c.gridy = y;
        c.gridwidth = GridBagConstraints.RELATIVE;
        selectFilePanel.add(textField, c);
    }

    private void initComponents() {

        //----------------------------------------------------------------------
        //                         TOP MENU
        //----------------------------------------------------------------------
    
        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F); // Only works on win32

        /*
        JMenuItem loadMenuItem = new JMenuItem("Load file...");
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        */

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        quitMenuItem.addActionListener(actionEvent -> System.exit(0));
        quitMenuItem.setName("MenuExit");

        // Add file menu components
        //fileMenu.add(loadMenuItem);
        //fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);

        // SQL selection menu
        JMenu sqlMenu = new JMenu("Select");

        JCheckBoxMenuItem mySQLMenuItem = new JCheckBoxMenuItem("MySQL");
        mySQLMenuItem.setState(true);

        sqlMenu.add(mySQLMenuItem);

        // Add to menu bar
        menuBar.add(fileMenu);
        menuBar.add(sqlMenu);

        //----------------------------------------------------------------------
        //                          COMPONENTS
        //----------------------------------------------------------------------
        // Top Panel For selectionPanel and queryPanel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
        add(topPanel, BorderLayout.PAGE_START);

        // Contains SELECT FILE and SELECT TABLE
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.PAGE_AXIS));

        
        //----------------------------------------------------------------------
        JPanel selectFilePanel = new JPanel();
        selectFilePanel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);

        // NAME
        JTextField databaseTextField = new JTextField("auction");
        buildInput(selectFilePanel, databaseTextField, c, 
            "Database Name:", "databaseTextField", 0);

        // Host
        JTextField hostTextField = new JTextField("localhost");
        buildInput(selectFilePanel, hostTextField, c, 
            "Host Name:", "hostTextField", 1);

        // Port
        JTextField portTextField = new JTextField("3306");
        buildInput(selectFilePanel, portTextField, c, 
            "Port Name:", "portTextField", 2);
 
        // User
        JTextField userTextField = new JTextField("root");
        buildInput(selectFilePanel, userTextField, c, 
            "Username:", "userTextField", 3);
        
        // Password
        JPasswordField passwordTextField = new JPasswordField("");
        buildInput(selectFilePanel, passwordTextField, c, 
            "Password:", "passwordTextField", 4);


        JButton openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        c.gridx = 3;
        c.gridx = 5;
        selectFilePanel.add(openFileButton, c);

        selectFilePanel.setBorder(BorderFactory.createTitledBorder("Select file"));


        // SELECT TABLE
        JComboBox<String> tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");

        JPanel selectTablePanel = new JPanel(new GridLayout(2, 1));
        selectTablePanel.add(new JLabel("Please select a table from the database:"));
        selectTablePanel.add(tablesComboBox);
        selectTablePanel.setBorder(BorderFactory.createTitledBorder("Select table"));
        selectTablePanel.setSize(400, 70);

        selectionPanel.add(selectFilePanel);
        selectionPanel.add(selectTablePanel);

        //----------------------------------------------------------------------

        // RUN QUERY
        // Text Box
        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setRows(8);
        queryTextArea.setColumns(35);
        queryTextArea.setEnabled(false);
        JScrollPane queryTextScroll = new JScrollPane(queryTextArea);

        // Button
        JButton executeButton = new JButton("Execute");
        executeButton.setName("ExecuteQueryButton");
        executeButton.setEnabled(false);

        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        queryPanel.add(queryTextScroll);
        queryPanel.add(executeButton);
        queryPanel.setBorder(BorderFactory.createTitledBorder("Run query"));

        //----------------------------------------------------------------------

        // RESULTS 
        // table Displays query output
        JPanel centerPanel = new JPanel(new GridLayout(1, 0));
        add(centerPanel, BorderLayout.CENTER);

        JTable table = new JTable();
        table.setName("Table");
        table.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new GridLayout(1, 0));
        tablePanel.add(tableScrollPane);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Results"));

        // Add panels to main window
        topPanel.add(selectionPanel);
        topPanel.add(queryPanel);
        centerPanel.add(tablePanel);

        //----------------------------------------------------------------------
        //                          ACTION LISTENERS
        //----------------------------------------------------------------------

        // Action Listeners
        /*
        loadMenuItem.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileNameTextField.setText(""); // Clear text
                fileName = fileChooser.getSelectedFile().toString();
                fileNameTextField.setText(fileChooser.getSelectedFile().toString());
            }
        });
        */

        openFileButton.addActionListener(actionEvent -> {
            //Path filePath = Paths.get(databaseTextField.getText());
            //System.out.println(databaseTextField.getText());
            /*
            
            */
            database = databaseTextField.getText();
            host = hostTextField.getText();
            port = portTextField.getText();
            USER = userTextField.getText();
            PASS = String.valueOf(passwordTextField.getPassword());
            

            tablesComboBox.removeAllItems();
            connect();
            try {
                getTables().forEach(tablesComboBox::addItem);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //driver.getAllTables().forEach(tablesComboBox::addItem);
            queryTextArea.setText(String.format(SQL_ALL_ROWS, tablesComboBox.getSelectedItem()));
            // Enable buttons
            queryTextArea.setEnabled(true);
            executeButton.setEnabled(true);
        });

        tablesComboBox.addItemListener(actionEvent ->
                queryTextArea.setText(String.format(SQL_ALL_ROWS, actionEvent.getItem().toString())));
        executeButton.addActionListener(actionEvent -> {
            
            try (Statement statement = connection.createStatement()) {
                System.out.println(queryTextArea.getText());
                ResultSet resultSet = statement.executeQuery(queryTextArea.getText());

                ResultSetMetaData metaData = resultSet.getMetaData();
                // Retrieve columns
                int columnCount = metaData.getColumnCount();
                String[] columns = new String[columnCount];

                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    columns[i] = metaData.getColumnName(i + 1);
                    System.out.println(columns[i]);
                }
                // Retrieve row
                Map<Integer, Object[]> data = new HashMap<>();
                int i = 0;

                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int j = 0; j < columnCount; j++) {
                        row[j] = resultSet.getObject(j + 1);
                        System.out.println(row[j]);
                    }
                    data.put(i++, row);
                }

                table.setModel(new DataTableModel(columns, data));
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }


    private void connect(){
        // Database name is same as "Schema" from MySQL workbench
        url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        System.out.println(url);

		try {
			connection = DriverManager.getConnection(url, USER, PASS);
		} catch (SQLException e) {
			e.printStackTrace();
        }
        
    }

    private ArrayList<String> getTables() throws SQLException {
        java.sql.DatabaseMetaData metaData = connection.getMetaData();
        
        String[] types = {"TABLE"};
        String set = database.isEmpty() ? null : database;
        ResultSet tables = metaData.getTables(set, null, "%", types);
        ArrayList<String> tableNames = new ArrayList<>();
        while (tables.next()) {
            tableNames.add(tables.getString("TABLE_NAME"));
        }
        return tableNames;
    }

    //removeFacility(3);
    private void removeFacility(int id){
        try{
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM FACILITY WHERE Facility_Id = ?;");
            stmt.setInt(1, id);
            if(stmt.executeUpdate() > 0){
                System.out.println("SUCCESS");
                // Maybe create a pop up idk (kyle)
            }

        }
        catch(Exception e){

        }
    }

    //addFacility(3, "KK", "Kyle Kryza", "2022-04-20", "2022-04-20", "Dream Team");
    private void addFacility(int id, String username, String contact, String created, String last_activity, String name){
        try{
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO FACILITY VALUES (?, ?, ?, ?, ?, ?);");
            stmt.setInt(1, id);
            stmt.setString(2, username);
            stmt.setString(3, contact);
            stmt.setString(4, created);
            stmt.setString(5, last_activity);
            stmt.setString(6, name);
            
            if(stmt.executeUpdate() > 0){
                System.out.println("SUCCESS");
                // Maybe create a pop up idk (kyle)
            }

        }
        catch(Exception e){

        }
    }
}
