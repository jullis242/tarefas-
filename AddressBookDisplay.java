import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.util.List;

public class AddressBookDisplay extends JFrame {
    private Person currentEntry;
    private PersonQueries personQueries;
    private List<Person> results;
    private int numberOfEntries = 0;
    private int currentEntryIndex;

    private JTextField emailTextField;
    private JLabel emailLabel;
    private JTextField firstNameTextField;
    private JLabel idLabel;
    private JTextField idTextField;
    private JLabel lastNameLabel;
    private JTextField lastNameTextField;
    private JLabel phoneLabel;
    private JTextField phoneTextField;
    private JTextField queryTextField;
    private JLabel queryLabel;
    private JTextField indexTextField;
    private JTextField maxTextField;

    private JButton previousButton;
    private JButton nextButton;
    private JButton displayButton;
    private JButton insertButton;
    private JButton queryButton;
    private JButton browseButton;

    private JPanel navigatePanel;
    private JPanel displayPanel;
    private JPanel queryPanel;
    private JPanel insertPanel;

    // construtor sem argumento
    public AddressBookDisplay() {
        super("Address Book");

        // estabelece a conexão do banco de dados e configura PreparedStatements
        personQueries = new PersonQueries();

        // configura layout e tamanho
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setSize(400, 300);
        setResizable(false);

        // painel de navegação
        navigatePanel = new JPanel();
        navigatePanel.setLayout(new BoxLayout(navigatePanel, BoxLayout.X_AXIS));

        previousButton = new JButton("Previous");
        previousButton.setEnabled(false);
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // ação do botão Previous
            }
        });

        indexTextField = new JTextField(5);
        indexTextField.setHorizontalAlignment(JTextField.CENTER);
        indexTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // ação do campo de índice
            }
        });

        maxTextField = new JTextField(5);
        maxTextField.setHorizontalAlignment(JTextField.CENTER);
        maxTextField.setEditable(false);
        maxTextField.setText("");
        maxTextField.setMaximumSize(maxTextField.getPreferredSize());

        nextButton = new JButton("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // ação do botão Next
            }
        });

        navigatePanel.add(previousButton);
        navigatePanel.add(Box.createHorizontalStrut(10));
        navigatePanel.add(indexTextField);
        navigatePanel.add(Box.createHorizontalStrut(10));
        navigatePanel.add(maxTextField);
        navigatePanel.add(Box.createHorizontalStrut(10));
        navigatePanel.add(nextButton);
        add(navigatePanel);

        // painel de exibição
        displayPanel = new JPanel();
        displayPanel.setLayout(new GridLayout(5, 2, 4, 4));

        idLabel = new JLabel("Address ID:");
        idTextField = new JTextField(10);
        idTextField.setEditable(false);
        displayPanel.add(idLabel);
        displayPanel.add(idTextField);

        firstNameTextField = new JTextField(10);
        JLabel firstNameLabel = new JLabel("First Name:");
        displayPanel.add(firstNameLabel);
        displayPanel.add(firstNameTextField);

        lastNameLabel = new JLabel("Last Name:");
        lastNameTextField = new JTextField(10);
        displayPanel.add(lastNameLabel);
        displayPanel.add(lastNameTextField);

        emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(10);
        displayPanel.add(emailLabel);
        displayPanel.add(emailTextField);

        phoneLabel = new JLabel("Phone Number:");
        phoneTextField = new JTextField(10);
        displayPanel.add(phoneLabel);
        displayPanel.add(phoneTextField);

        add(displayPanel);

        // painel de consulta
        queryPanel = new JPanel();
        queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.X_AXIS));
        queryPanel.setBorder(BorderFactory.createTitledBorder("Find an entry by last name"));

        queryLabel = new JLabel("Last Name:");
        queryTextField = new JTextField(10);
        queryButton = new JButton("Find");
        queryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                results = personQueries.getPeopleByLastName(queryTextField.getText());
                numberOfEntries = results.size();
                if (numberOfEntries != 0) {
                    currentEntryIndex = 0;
                    currentEntry = results.get(currentEntryIndex);
                    updateDisplay();
                } else {
                    JOptionPane.showMessageDialog(this, "No entries found", "Search", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        queryPanel.add(queryLabel);
        queryPanel.add(Box.createHorizontalStrut(5));
        queryPanel.add(queryTextField);
        queryPanel.add(Box.createHorizontalStrut(10));
        queryPanel.add(queryButton);
        add(queryPanel);

        // botão para navegar por todas as entradas
        browseButton = new JButton("Browse All Entries");
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                results = personQueries.getAllPeople();
                numberOfEntries = results.size();
                if (numberOfEntries != 0) {
                    currentEntryIndex = 0;
                    currentEntry = results.get(currentEntryIndex);
                    updateDisplay();
                }
            }
        });
        add(browseButton);

        // botão para inserir nova entrada
        insertButton = new JButton("Insert New Entry");
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int result = personQueries.addPerson(
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    emailTextField.getText(),
                    phoneTextField.getText());

                if (result == 0) {
                    JOptionPane.showMessageDialog(this, "Person not added", "Add Person", JOptionPane.PLAIN_MESSAGE);
                } else {
                    results = personQueries.getAllPeople();
                    currentEntryIndex = results.size() - 1;
                    currentEntry = results.get(currentEntryIndex);
                    updateDisplay();
                }
            }
        });
        add(insertButton);

        // evento de fechamento da janela
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                personQueries.close();
                System.exit(0);
            }
        });
    }

    // atualiza os campos da interface com os dados da entrada atual
    private void updateDisplay() {
        indexTextField.setText("" + (currentEntryIndex + 1));
        idTextField.setText("" + currentEntry.getAddressID());
        firstNameTextField.setText(currentEntry.getFirstName());
        lastNameTextField.setText(currentEntry.getLastName());
        emailTextField.setText(currentEntry.getEmail());
        phoneTextField.setText(currentEntry.getPhoneNumber());
        maxTextField.setText("" + numberOfEntries);
        previousButton.setEnabled(currentEntryIndex > 0);
        nextButton.setEnabled(currentEntryIndex < numberOfEntries - 1);
    }
}
