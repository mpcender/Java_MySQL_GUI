package MySQL.Viewer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;

public class MySQLViewer extends JFrame {

    public static final String SQL_ALL_ROWS = "SELECT * FROM %s;";

    private Connection connection;
    private static String url;

    String database = "auction";
    String host = "localhost";
    String port = "3306";
    String USER = "root";
    String PASS = "";

    JPanel mainModPanel;
    JPanel currentForm;
    JComboBox<String> tablesComboBox;
    JTextArea queryTextArea;
    JButton executeButton;
    JTable table;

    public MySQLViewer() {
        // Basic window settings
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set minimized window size.
        this.setSize(600,800);
        setMinimumSize(new Dimension(600, 800));

        this.setLayout(new BorderLayout());
        //this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("MySQL Viewer");
        // Initialize main window components
        this.initComponents();
        // Render window content
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
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

    JButton login;
    JButton addFac;
    JButton delFac;

    private void viewButtonScheme(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        
        c.gridx = 1;
        c.gridy = 0;
        login = new JButton("Connect to Database");
        login.addActionListener(actionEvent -> {
            loginView();
        });
        panel.add(login,c);

        c.gridx = 2;
        c.gridy = 0;
        delFac = new JButton("Delete Facility");
        delFac.setEnabled(false);
        delFac.addActionListener(actionEvent -> {
            setDeleteFacilityView();
        });
        panel.add(delFac,c); 

        c.gridx = 3;
        c.gridy = 0;
        addFac = new JButton("Add Facility");
        addFac.setEnabled(false);
        addFac.addActionListener(actionEvent -> {
            setAddFacilityView();
        });
        panel.add(addFac,c);

        /*
        c.gridx = 4;
        c.gridy = 0;
        delFac = new JButton("Add Facility");
        addFac.setEnabled(false);
        addFac.addActionListener(actionEvent -> {
            setAddFacilityView();
        });
        panel.add(addFac,c);
        */

    }
    JPanel selectTablePanel;
    
    /**
     * Dropdown table selector
     * Has to be re-applied to main panel each time new view is selected
     */
    private void addSelectTable() {
        // Only build table panel once, re-apply if exists
        if (selectTablePanel != null) { 
            mainModPanel.add(selectTablePanel);
            return;
        }
        // SELECT TABLE
        tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");

        selectTablePanel = new JPanel(new GridLayout(2, 1));
        selectTablePanel.add(new JLabel("Please select a table from the database:"));
        selectTablePanel.add(tablesComboBox);
        selectTablePanel.setBorder(BorderFactory.createTitledBorder("Select table"));
        selectTablePanel.setSize(400, 70);

        mainModPanel.add(selectTablePanel);
    }

    private void loginView() {
        if (currentForm != null) {
            mainModPanel.removeAll();
        }
        
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

        selectFilePanel.setBorder(BorderFactory.createTitledBorder("Connect to Database"));

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

        currentForm = selectFilePanel;
        mainModPanel.add(currentForm);
        addSelectTable();
        mainModPanel.updateUI();
    }

    private void setDeleteFacilityView() {
        mainModPanel.removeAll();

        JPanel selectFilePanel = new JPanel();
        selectFilePanel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);

        // NAME
        JTextField fID = new JTextField("");
        buildInput(selectFilePanel, fID, c, 
            "Facility ID:", "fID", 0);


        JButton openFileButton = new JButton("Delete");
        openFileButton.setName("DeleteButton");
        c.gridx = 3;
        c.gridx = 5;
        selectFilePanel.add(openFileButton, c);

        selectFilePanel.setBorder(BorderFactory.createTitledBorder("Delete Facility"));

        openFileButton.addActionListener(actionEvent -> {

            String id = fID.getText();
            try {
                removeFacility(Integer.parseInt(id));
            } catch (Exception e) {
                //TODO: handle exception
            }

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

        currentForm = selectFilePanel;
        mainModPanel.add(currentForm);
        addSelectTable();
        mainModPanel.updateUI();
    }

    private void setAddFacilityView() {
        if (currentForm != null) {
            mainModPanel.removeAll();
        }
        
        JPanel selectFilePanel = new JPanel();
        selectFilePanel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);

        // NAME
        JTextField jId = new JTextField("0");
        buildInput(selectFilePanel, jId, c, 
            "id", "id", 0);

        // Host
        JTextField jUser = new JTextField("username");
        buildInput(selectFilePanel, jUser, c, 
            "Username:", "usernameField", 1);

        // Port
        JTextField jContact = new JTextField("First Last");
        buildInput(selectFilePanel, jContact, c, 
            "Contact Name:", "contactField", 2);
 
        // User
        JTextField jCname = new JTextField("company");
        buildInput(selectFilePanel, jCname, c, 
            "Company Name:", "companyField", 3);
    

        JButton openFileButton = new JButton("Add");
        openFileButton.setName("AddButton");
        c.gridx = 3;
        c.gridx = 5;
        selectFilePanel.add(openFileButton, c);

        selectFilePanel.setBorder(BorderFactory.createTitledBorder("Add New Facility"));

        openFileButton.addActionListener(actionEvent -> {

            int id = Integer.parseInt(jId.getText());
            String user = jUser.getText();
            String contact = jContact.getText();
            String cName = jCname.getText();
            
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = dateObj.format(formatter);
    
            // Date is today & last_activity always null for new user.
            addFacility(id, user, contact, date , null, cName);

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

        currentForm = selectFilePanel;
        mainModPanel.add(currentForm);
        addSelectTable();
        mainModPanel.updateUI();
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
        

        JPanel panel = new JPanel();
        mainModPanel = selectionPanel;

        // Sets main button view for toggling different SQL forms
        viewButtonScheme(panel);
        topPanel.add(panel);
        // start with database login form
        loginView();

        //----------------------------------------------------------------------

        // RUN QUERY
        // Text Box
        queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setRows(8);
        queryTextArea.setColumns(35);
        queryTextArea.setEnabled(false);
        JScrollPane queryTextScroll = new JScrollPane(queryTextArea);

        // Button
        executeButton = new JButton("Execute");
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

        table = new JTable();
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

        tablesComboBox.addItemListener(actionEvent ->
                queryTextArea.setText(String.format(SQL_ALL_ROWS, actionEvent.getItem().toString())));
        executeButton.addActionListener(actionEvent -> {
            executeQuery(queryTextArea.getText());
        });
    }


    /**
     * Execute a string query
     * @param qry
     */
    private void executeQuery(String qry) {
        try (Statement statement = connection.createStatement()) {
            System.out.println(queryTextArea.getText());
            ResultSet resultSet = statement.executeQuery(qry);

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
            resizeColumnWidth(table);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Execute a prepared statement
     * @param stmt
     */
    private void executeStatement(PreparedStatement stmt) {
        try  {
            if (!stmt.execute()) {  return; }
            ResultSet resultSet = stmt.getResultSet();

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
            resizeColumnWidth(table);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Resizes columns to fit data
     * @param table
     */
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    /**
     * Connect to the database
     */
    private void connect(){
        // Database name is same as "Schema" from MySQL workbench
        url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        System.out.println(url);

		try {
			connection = DriverManager.getConnection(url, USER, PASS);
            if (!connection.isClosed()) {
                addFac.setEnabled(true);
                delFac.setEnabled(true);
            }
		} catch (SQLException e) {
			e.printStackTrace();
        }
        
    }

    /**
     * Gets table view for database
     * @return
     * @throws SQLException
     */
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
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM FACILITY WHERE Facility_Id = ?;");
            stmt.setInt(1, id);
            executeStatement(stmt);
            executeQuery("SELECT * FROM facility;");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            
            executeStatement(stmt);
            executeQuery("SELECT * FROM facility;");
        }
        catch(Exception e){

        }
    }

}
