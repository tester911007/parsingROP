package connection;

import java.sql.*;
import java.time.LocalDateTime;

public class ConnectionDB {
    private static final String INSERT_INPUT = "INSERT INTO [dbo].[input] " +
            "([a01],[a02],[a03],[a04],[a05],[a06],[a07],[a08],[a09],[a10] " +
            ",[a11],[a12],[a13],[a14],[a15],[a16],[a17],[a18],[a19] " +
            ",[a20],[a21],[a22],[a23],[a24],[a25],[a26],[a27],[a28],[a29] " +
            ",[a30],[a31],[a32],[a33],[a34],[a35],[a36],[a37],[a38],[a39] " +
            ",[a40],[a41],[a42],[a43],[a44],[a45],[a46],[a47],[a48],[a49] " +
            ",[a50],[a51],[a52],[a53],[a54],[a55],[a56],[a57],[action],[dateCreateTime]) " +
            " VALUES " +
            "       (?,?,?,?,?,?,?,?,?,? " +
            "       ,?,?,?,?,?,?,?,?,? " +
            "       ,?,?,?,?,?,?,?,?,?,? " +
            "       ,?,?,?,?,?,?,?,?,?,? " +
            "       ,?,?,?,?,?,?,?,?,?,? " +
            "       ,?,?,?,?,?,?,?,?,?,?);";

    private Connection getConnection() throws SQLException {
        String connectionUrl = Config.getProperty(Config.DB_URL) +
                Config.getProperty(Config.DB_DATABASE) +
                Config.getProperty(Config.DB_LOGIN) +
                Config.getProperty(Config.DB_PASSWORD);
        return DriverManager.getConnection(connectionUrl);
    }

    public void executeSQL(String[] allParams) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INPUT, new String[]{"id"})) {

            for (int i = 0; i < allParams.length; i++) {
                statement.setString(i + 1, allParams[i]);
            }
            statement.setString(58, "action");
            statement.setTimestamp(59, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                System.out.println(resultSet.getLong(1));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
