import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonQueries {
    private static final String URL = "jdbc:derby:AddressBook";
    private static final String USERNAME = "deitel";
    private static final String PASSWORD = "deitel";

    private Connection connection = null;
    private PreparedStatement selectAllPeople = null;
    private PreparedStatement selectPeopleByLastName = null;
    private PreparedStatement insertNewPerson = null;

    // construtor
    public PersonQueries() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            selectAllPeople = connection.prepareStatement("SELECT * FROM Addresses");

            selectPeopleByLastName = connection.prepareStatement(
                "SELECT * FROM Addresses WHERE LastName = ?");

            insertNewPerson = connection.prepareStatement(
                "INSERT INTO Addresses (FirstName, LastName, Email, PhoneNumber) VALUES (?, ?, ?, ?)");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }

    // seleciona todos os endereços no banco de dados
    public List<Person> getAllPeople() {
        List<Person> results = new ArrayList<Person>();
        ResultSet resultSet = null;

        try {
            resultSet = selectAllPeople.executeQuery();

            while (resultSet.next()) {
                results.add(new Person(
                    resultSet.getInt("addressID"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("Email"),
                    resultSet.getString("PhoneNumber")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        return results;
    }

    // seleciona pessoas por sobrenome
    public List<Person> getPeopleByLastName(String name) {
        List<Person> results = new ArrayList<Person>();
        ResultSet resultSet = null;

        try {
            selectPeopleByLastName.setString(1, name);
            resultSet = selectPeopleByLastName.executeQuery();

            while (resultSet.next()) {
                results.add(new Person(
                    resultSet.getInt("addressID"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("Email"),
                    resultSet.getString("PhoneNumber")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        return results;
    }

    // adiciona uma nova pessoa
    public int addPerson(String fname, String lname, String email, String num) {
        int result = 0;

        try {
            insertNewPerson.setString(1, fname);
            insertNewPerson.setString(2, lname);
            insertNewPerson.setString(3, email);
            insertNewPerson.setString(4, num);

            result = insertNewPerson.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            close();
        }

        return result;
    }

    // fecha a conexão com o banco de dados
    public void close() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
