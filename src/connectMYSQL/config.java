package connectMYSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DataModel.order;
import DataModel.order.OrderStatusType;
import DataModel.order.PaymentMethodType;
import DataModel.room;
import DataModel.room.RoomType;
import DataModel.user.IdentificationType;
import DataModel.user.StatusType;
import DataModel.user.StatusRole;

import java.sql.Date;

public class config {
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/hotel";
    private final String USER = "root";
    private final String PASSWORD = "";

    private Connection connection;
    private int users_id;// saya buat global agar bisa mengambil nilai dari tamu id
    private String type;// saya buat global agar bisa mengambil nilai dari room type
    private Integer admin_id;

    // method untuk mengambil nilai tamu id
    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public Integer getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(Integer admin_id) {
        this.admin_id = admin_id;
    }

    // method untuk koneksi ke database
    public void connectToSQL() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkLoginGuest(String name, String password) {
        boolean LoginGuest = false;
        connectToSQL();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT users_id, password FROM users WHERE name = ? AND status = 'active' AND role = 'Guest'")) {
            statement.setString(1, name);
            ResultSet resultData = statement.executeQuery();

            if (resultData.next()) {
                String storedPassword = resultData.getString("password");
                if (password.equals(storedPassword)) {
                    LoginGuest = true;
                    users_id = resultData.getInt("users_id");// mengambil nilai dari users_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return LoginGuest;
    }

    public boolean checkLoginOperator(String name, String password) {
        boolean LoginOperator = false;
        connectToSQL();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT users_id, password FROM users WHERE name = ? AND status = 'active' AND role = 'Operator'")) {
            statement.setString(1, name);
            ResultSet resultData = statement.executeQuery();

            if (resultData.next()) {
                String storedPassword = resultData.getString("password");
                if (password.equals(storedPassword)) {
                    LoginOperator = true;
                    users_id = resultData.getInt("users_id");// mengambil nilai dari users_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return LoginOperator;
    }

    public int register(String name, String password, IdentificationType identification_type,
            String phone, String email, StatusType status, StatusRole role) {
        int insertedId = -1;
        try {
            connectToSQL();

            String query = "INSERT INTO users (name, password, identification_type, phone, email, status, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, identification_type.toString());
            statement.setString(4, phone);
            statement.setString(5, email);
            statement.setString(6, status.toString());
            statement.setString(7, role.toString());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    insertedId = generatedKeys.getInt(1);
                    setUsers_id(insertedId);

                    System.out.println("\n-- Akun Berhasil Terdaftar  --");
                    System.out.println("User ID         : " + insertedId);
                    System.out.println("Nama            : " + name);
                    System.out.println("Password        : " + password);
                    System.out.println("Tipe Identitas  : " + identification_type);
                    System.out.println("No.Telpon       : " + phone);
                    System.out.println("Email           : " + email);
                    System.out.println("-----------------------------------------");
                }
            } else {
                System.out.println("Insert failed.");
            }
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertedId;
    }

    public int insertOrder(Date check_in_date, Date check_out_date, Date booking_date,
            int total_price, PaymentMethodType payment_method, OrderStatusType orders_status,
            int room_id, int users_id) {
        int insertOrderId = -1;

        try {
            connectToSQL();

            String query = "INSERT INTO orders (check_in_date, check_out_date, booking_date, total_price, payment_method, orders_status, room_id, users_id, admin_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, check_in_date);
            statement.setDate(2, check_out_date);
            statement.setDate(3, booking_date);
            statement.setInt(4, total_price);
            statement.setString(5, payment_method.toString());
            statement.setString(6, orders_status.toString());
            statement.setInt(7, room_id);
            statement.setInt(8, users_id);
            statement.setNull(9, java.sql.Types.INTEGER);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    insertOrderId = generatedKeys.getInt(1);
                    System.out.println("Order berhasil diinsert dengan ID: " + insertOrderId);
                }
            } else {
                System.out.println("Insert failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertOrderId;
    }

    public void updateOrder(int orders_id, OrderStatusType orders_status) {
        connectToSQL();

        try {
            String query = "UPDATE orders SET orders_status = ? WHERE orders_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, orders_status.toString());
            statement.setInt(2, orders_id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\n-- Order Dengan ID " + orders_id + " Berhasil --");
            } else {
                System.out.println("\n-- Order Dengan ID " + orders_id + " Gagal --");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrder(int orders_id, int admin_id, OrderStatusType orders_status) {
        connectToSQL();

        try {
            String query = "UPDATE orders SET admin_id = ?, orders_status = ? WHERE orders_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, admin_id);
            statement.setString(2, orders_status.toString());
            statement.setInt(3, orders_id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\n-- Order Dengan ID " + orders_id + " Berhasil --");
            } else {
                System.out.println("\n-- Order Dengan ID " + orders_id + " Gagal --");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(int users_id, String name, String password, IdentificationType identificationType,
            String phone, String email) {
        connectToSQL();

        try {
            // Check if the user exists and is a Guest
            String checkQuery = "SELECT * FROM users WHERE users_id = ? AND role = 'Guest'";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, users_id);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                // Update the user information
                String updateQuery = "UPDATE users SET name = ?, password = ?, identification_type = ?, phone = ?, email = ? WHERE users_id = ?";
                PreparedStatement statement = connection.prepareStatement(updateQuery);
                statement.setString(1, name);
                statement.setString(2, password);
                statement.setString(3, identificationType.toString());
                statement.setString(4, phone);
                statement.setString(5, email);
                statement.setInt(6, users_id);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("\n-- User dengan ID " + users_id + " berhasil diupdate --");
                } else {
                    System.out.println("\n-- Gagal mengupdate user dengan ID " + users_id
                            + ". Periksa kembali data yang dimasukkan --");
                }
            } else {
                System.out.println("\n-- User dengan ID " + users_id + " tidak ditemukan atau bukan Guest --");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(int users_id, StatusType status) {
        connectToSQL();

        try {
            // Check if the user exists and is a Guest
            String checkQuery = "SELECT * FROM users WHERE users_id = ? AND role = 'Guest'";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, users_id);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                // Update the user's status
                String updateQuery = "UPDATE users SET status = ? WHERE users_id = ?";
                PreparedStatement statement = connection.prepareStatement(updateQuery);
                statement.setString(1, status.toString());
                statement.setInt(2, users_id);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("\n-- Status user dengan ID " + users_id + " berhasil diupdate --");
                } else {
                    System.out.println("\n-- Gagal mengupdate status user dengan ID " + users_id
                            + ". Periksa kembali data yang dimasukkan --");
                }
            } else {
                System.out.println("\n-- User dengan ID " + users_id + " tidak ditemukan atau bukan Guest --");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRoom(int room_id, RoomType type, int price, String description) {
        connectToSQL();

        try {
            String updateQuery = "UPDATE room SET type = ?, price = ?, Description = ? WHERE room_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, type.toString());
            statement.setInt(2, price);
            statement.setString(3, description);
            statement.setInt(4, room_id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\n-- Room dengan ID " + room_id + " berhasil diupdate --");
            } else {
                System.out.println("\n-- Gagal mengupdate Room dengan ID " + room_id
                        + ". Periksa kembali data yang dimasukkan --");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalPrice(int orders_id, int totalWithJarakHari) {
        try {
            connectToSQL();

            String query = "UPDATE orders SET total_price = ? WHERE orders_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, totalWithJarakHari);
            statement.setInt(2, orders_id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\n");
            } else {
                System.out.println("\nGagal memperbarui nilai kolom Total_price.");
            }

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void showRoom() {
        connectToSQL();

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT room_id, type, price, Description FROM room";
            ResultSet resultData = statement.executeQuery(query);

            while (resultData.next()) {
                String type = resultData.getString("type");
                int price = resultData.getInt("price");
                String Description = resultData.getString("Description");
                int room_id = resultData.getInt("room_id");

                System.out.println("Nomor Ruangan   : " + room_id);
                System.out.println("Room Type       : " + type);
                System.out.println("Price           : " + price);
                System.out.println("Description     : " + Description);
                System.out.println("-----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showOrder() {
        connectToSQL();

        try {
            String query = "SELECT o.orders_id, u.name, r.type, o.check_in_date, o.check_out_date, o.booking_date, o.total_price, o.payment_method, o.orders_status "
                    +
                    "FROM orders o " +
                    "JOIN room r ON r.room_id = o.room_id " +
                    "JOIN users u ON u.users_id = o.users_id " +
                    "WHERE o.orders_status = 'In_Progress' " +
                    "ORDER BY o.orders_id"; // Add ORDER BY clause for sorting by orders_id

            Statement statement = connection.createStatement();
            ResultSet resultData = statement.executeQuery(query);

            while (resultData.next()) {
                int orders_id = resultData.getInt("orders_id");
                String name = resultData.getString("name");
                String type = resultData.getString("type");
                Date check_in_date = resultData.getDate("check_in_date");
                Date check_out_date = resultData.getDate("check_out_date");
                Date booking_date = resultData.getDate("booking_date");
                int total_price = resultData.getInt("total_price");
                String payment_method = resultData.getString("payment_method");
                String orders_status = resultData.getString("orders_status");

                System.out.println("Order ID        : " + orders_id);
                System.out.println("Nama Pengguna   : " + name);
                System.out.println("Room Type       : " + type);
                System.out.println("Check-in Date   : " + check_in_date);
                System.out.println("Check-out Date  : " + check_out_date);
                System.out.println("Booking Date    : " + booking_date);
                System.out.println("Total Price     : " + total_price);
                System.out.println("Payment Method  : " + payment_method);
                System.out.println("Order Status    : " + orders_status);
                System.out.println("-----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showUserStatus() {
        connectToSQL();

        try {
            String query = "SELECT users_id, name, password, identification_type, phone, email, status FROM users WHERE role = 'Guest'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultData = statement.executeQuery();

            while (resultData.next()) {
                int users_id = resultData.getInt("users_id");
                String name = resultData.getString("name");
                String password = resultData.getString("password");
                String identificationType = resultData.getString("identification_type");
                String phone = resultData.getString("phone");
                String email = resultData.getString("email");
                String status = resultData.getString("status");

                // Replace password with asterisks
                String maskedPassword = maskPassword(password);

                System.out.println("\nUser ID        : " + users_id);
                System.out.println("Nama           : " + name);
                System.out.println("Password       : " + maskedPassword);
                System.out.println("Tipe Identitas : " + identificationType);
                System.out.println("Nomor Telepon  : " + phone);
                System.out.println("Email          : " + email);
                System.out.println("Status         : " + status);
                System.out.println("-----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showUser() {
        connectToSQL();

        try {
            String query = "SELECT users_id, name, password, identification_type, phone, email FROM users WHERE role = 'Guest'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultData = statement.executeQuery();

            while (resultData.next()) {
                int users_id = resultData.getInt("users_id");
                String name = resultData.getString("name");
                String password = resultData.getString("password");
                String identificationType = resultData.getString("identification_type");
                String phone = resultData.getString("phone");
                String email = resultData.getString("email");

                // Replace password with asterisks
                String maskedPassword = maskPassword(password);

                System.out.println("\nUser ID        : " + users_id);
                System.out.println("Nama           : " + name);
                System.out.println("Password       : " + maskedPassword);
                System.out.println("Tipe Identitas : " + identificationType);
                System.out.println("Nomor Telepon  : " + phone);
                System.out.println("Email          : " + email);
                System.out.println("-----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showUser(int users_id) {
        connectToSQL();

        try {
            String query = "SELECT name, password, identification_type, phone, email FROM users WHERE users_id = ? AND role = 'Guest'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, users_id);
            ResultSet resultData = statement.executeQuery();

            while (resultData.next()) {
                String name = resultData.getString("name");
                String password = resultData.getString("password");
                String identificationType = resultData.getString("identification_type");
                String phone = resultData.getString("phone");
                String email = resultData.getString("email");

                // Replace password with asterisks
                String maskedPassword = maskPassword(password);

                System.out.println("\nNama           : " + name);
                System.out.println("Password       : " + maskedPassword);
                System.out.println("Tipe Identitas : " + identificationType);
                System.out.println("Nomor Telepon  : " + phone);
                System.out.println("Email          : " + email);
                System.out.println("-----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showOrder(int orders_id) {
        connectToSQL();

        try {
            String query = "SELECT o.orders_id, u.name AS user_name, u2.name AS admin_name, r.type, o.check_in_date, o.check_out_date, o.booking_date, o.total_price, o.payment_method, o.orders_status "
                    +
                    "FROM orders o " +
                    "JOIN room r ON r.room_id = o.room_id " +
                    "JOIN users u ON u.users_id = o.users_id " +
                    "LEFT JOIN users u2 ON u2.users_id = o.admin_id " +
                    "WHERE o.orders_id = ? " +
                    "ORDER BY o.orders_id"; // Add ORDER BY clause for sorting by orders_id

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orders_id);
            ResultSet resultData = statement.executeQuery();

            while (resultData.next()) {
                int order_id = resultData.getInt("orders_id");
                String user_name = resultData.getString("user_name");
                String admin_name = resultData.getString("admin_name");
                String type = resultData.getString("type");
                Date check_in_date = resultData.getDate("check_in_date");
                Date check_out_date = resultData.getDate("check_out_date");
                Date booking_date = resultData.getDate("booking_date");
                int total_price = resultData.getInt("total_price");
                String payment_method = resultData.getString("payment_method");
                String orders_status = resultData.getString("orders_status");

                System.out.println("Order ID        : " + order_id);
                System.out.println("Nama Pengguna   : " + user_name);
                System.out.println("Admin Name      : " + admin_name);
                System.out.println("Room Type       : " + type);
                System.out.println("Check-in Date   : " + check_in_date);
                System.out.println("Check-out Date  : " + check_out_date);
                System.out.println("Booking Date    : " + booking_date);
                System.out.println("Total Price     : " + total_price);
                System.out.println("Payment Method  : " + payment_method);
                System.out.println("Order Status    : " + orders_status);
                System.out.println("-----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showOrderHistory(int users_id) {
        connectToSQL();

        try {
            String query = "SELECT o.orders_id, u.name AS user_name, u2.name AS admin_name, r.type, o.check_in_date, o.check_out_date, o.booking_date, o.total_price, o.payment_method, o.orders_status "
                    +
                    "FROM orders o " +
                    "JOIN room r ON r.room_id = o.room_id " +
                    "JOIN users u ON u.users_id = o.users_id " +
                    "LEFT JOIN users u2 ON u2.users_id = o.admin_id " +
                    "WHERE o.users_id = ? AND (o.orders_status = 'Successful' OR o.orders_status = 'Cancelled') " +
                    "ORDER BY o.orders_id"; // Add ORDER BY clause for sorting by orders_id

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, users_id);
            ResultSet resultData = statement.executeQuery();

            while (resultData.next()) {
                int order_id = resultData.getInt("orders_id");
                String user_name = resultData.getString("user_name");
                String admin_name = resultData.getString("admin_name");
                String type = resultData.getString("type");
                Date check_in_date = resultData.getDate("check_in_date");
                Date check_out_date = resultData.getDate("check_out_date");
                Date booking_date = resultData.getDate("booking_date");
                int total_price = resultData.getInt("total_price");
                String payment_method = resultData.getString("payment_method");
                String orders_status = resultData.getString("orders_status");

                System.out.println("Order ID        : " + order_id);
                System.out.println("Nama Pengguna   : " + user_name);
                System.out.println("Admin Name      : " + admin_name);
                System.out.println("Room Type       : " + type);
                System.out.println("Check-in Date   : " + check_in_date);
                System.out.println("Check-out Date  : " + check_out_date);
                System.out.println("Booking Date    : " + booking_date);
                System.out.println("Total Price     : " + total_price);
                System.out.println("Payment Method  : " + payment_method);
                System.out.println("Order Status    : " + orders_status);
                System.out.println("-----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showAvailableRooms(Date check_in_date, Date check_out_date, String type) {
        connectToSQL();

        try {
            String query = "SELECT r.type, r.price, r.Description, r.room_id " +
                    "FROM room r " +
                    "WHERE NOT EXISTS (" +
                    "   SELECT 1 FROM orders o " +
                    "   WHERE r.room_id = o.room_id " +
                    "   AND o.check_in_date <= ? AND o.check_out_date >= ? " +
                    "   AND o.orders_status = 'Successful'" +
                    ")" +
                    " AND r.type = ? "; // Add the condition for orders_status

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, check_out_date);
            statement.setDate(2, check_in_date);
            statement.setString(3, type); // Set nilai parameter type

            ResultSet resultData = statement.executeQuery();

            boolean foundAvailableRooms = false;
            while (resultData.next()) {
                foundAvailableRooms = true;
                int room_id = resultData.getInt("room_id");
                String roomType = resultData.getString("type");
                int price = resultData.getInt("price");
                String Description = resultData.getString("Description");

                System.out.println("Nomor Ruangan   : " + room_id);
                System.out.println("Room Type       : " + roomType);
                System.out.println("Price           : " + price);
                System.out.println("Description     : " + Description);
                System.out.println("-----------------------------------------");
            }

            if (!foundAvailableRooms) {
                System.out.println("Tidak ada kamar yang tersedia pada periode tersebut.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPriceRoom(int room_number) {
        connectToSQL();
        int Price = 0;

        try {
            connectToSQL();

            String query = "SELECT price FROM room WHERE room_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, room_number);
            ResultSet resultData = statement.executeQuery();

            if (resultData.next()) {
                Price = resultData.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Price;
    }

    public int getDateDifference(int orders_id) {
        int selisihHari = 0;

        try {
            connectToSQL();

            String query = "SELECT DATEDIFF(Check_out_date, Check_in_date) AS selisih_hari FROM orders WHERE orders_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orders_id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                selisihHari = resultSet.getInt("selisih_hari");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return selisihHari;
    }

    public String getRoomTypeByRoomNumber(int room_id) {
        connectToSQL();
        String roomType = "";

        try {
            String query = "SELECT type FROM room WHERE room_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, room_id);

            ResultSet resultData = statement.executeQuery();

            if (resultData.next()) {
                roomType = resultData.getString("type");
            } else {
                System.out.println("Kamar dengan nomor " + room_id + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomType;
    }

    public int getTotalPrice(int orders_id) {
        int totalprice = 0;
        try {
            connectToSQL();

            String query = "SELECT total_price FROM orders WHERE orders_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orders_id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalprice = resultSet.getInt("Total_price");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return totalprice;
    }

    public boolean cekRoomNumber(int room_id) {
        connectToSQL();

        try {
            String query = "SELECT room_id FROM room WHERE room_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, room_id);
            ResultSet resultData = statement.executeQuery();

            return resultData.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean cekRoomNumber(int room_id, String type) {
        connectToSQL();

        try {
            String query = "SELECT COUNT(*) AS count FROM room WHERE room_id = ? AND type = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, room_id);
            statement.setString(2, type);
            ResultSet resultData = statement.executeQuery();

            if (resultData.next()) {
                int count = resultData.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isRoomReserved(Date check_in_date, Date check_out_date, String type) {
        boolean isReserved = false;
        connectToSQL();

        try {
            String query = "SELECT COUNT(*) AS count FROM orders o JOIN room r ON r.room_id = o.room_id WHERE o.check_in_date <= ? AND o.check_out_date >= ? AND r.type = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, check_out_date);
            statement.setDate(2, check_in_date);
            statement.setString(3, type);

            ResultSet resultData = statement.executeQuery();

            if (resultData.next()) {
                int count = resultData.getInt("count");
                isReserved = count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isReserved;
    }

    public boolean areRoomsAvailable(Date check_in_date, Date check_out_date, String type) {
        connectToSQL();
        boolean foundAvailableRooms = false;

        try {
            String query = "SELECT r.room_id " +
                    "FROM room r " +
                    "WHERE NOT EXISTS (" +
                    "   SELECT 1 FROM orders o " +
                    "   WHERE r.room_id = o.room_id " +
                    "   AND o.check_in_date <= ? AND o.check_out_date >= ? " +
                    "   AND o.orders_status = 'In_Progress'" +
                    ")" +
                    " AND r.type = ?"; // Tambahkan kondisi WHERE type = ?

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, check_out_date);
            statement.setDate(2, check_in_date);
            statement.setString(3, type); // Set nilai parameter type

            ResultSet resultData = statement.executeQuery();

            foundAvailableRooms = resultData.next(); // Cek apakah ada hasil dari query
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foundAvailableRooms;
    }

    public boolean cekOrder() {
        connectToSQL();

        try {
            String query = "SELECT * FROM orders o " +
                    "JOIN users u ON o.users_id = u.users_id " +
                    "WHERE u.role = 'Guest'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultData = statement.executeQuery();

            return resultData.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Mengembalikan false secara default jika terjadi exception atau error
    }

    public boolean cekOrder(int orders_id) {
        connectToSQL();

        try {
            String query = "SELECT users_id FROM orders WHERE orders_id = ? AND orders_status = 'In_Progress'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orders_id);
            ResultSet resultData = statement.executeQuery();

            return resultData.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Mengembalikan false secara default jika terjadi exception atau error
    }

    public boolean cekOrderUser(int users_id) {
        connectToSQL();

        try {
            String query = "SELECT users_id FROM orders WHERE users_id = ? AND orders_status = 'Successful'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, users_id);
            ResultSet resultData = statement.executeQuery();

            return resultData.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Mengembalikan false secara default jika terjadi exception atau error
    }

    public boolean cekUser(int users_id) {
        connectToSQL();

        try {
            String query = "SELECT users_id FROM users WHERE users_id = ? AND role = 'Guest'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, users_id);
            ResultSet resultData = statement.executeQuery();

            return resultData.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Mengembalikan false secara default jika terjadi exception atau error
    }

    private String maskPassword(String password) {
        int passwordLength = password.length();
        StringBuilder maskedPassword = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            maskedPassword.append("*");
        }

        return maskedPassword.toString();
    }
}
