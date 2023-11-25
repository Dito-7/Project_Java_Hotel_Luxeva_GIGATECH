package DataModel;

import DataModel.order.OrderStatusType;
import DataModel.order.PaymentMethodType;
import DataModel.room.RoomType;
import DataModel.user.IdentificationType;
import DataModel.user.StatusRole;
import DataModel.user.StatusType;
import connectMYSQL.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDate;

public class method {
    public String daftarKamar() {
        boolean daftarKamarLoop = true;
        String type = null;

        while (daftarKamarLoop) {
            System.out.println("\n-- Tipe Kamar --");
            System.out.println("1. Standard");
            System.out.println("2. Superior");
            System.out.println("3. Deluxe");
            System.out.println("4. Suite");
            System.out.println("5. Family");

            int menuKamar = InputScanner.readInt("Masukkan Kamar Yang Ingin Dipilih");
            config db = new config();

            if (menuKamar == 1) {
                type = "Standard";
                daftarKamarLoop = false;
            } else if (menuKamar == 2) {
                type = "Superior";
                daftarKamarLoop = false;
            } else if (menuKamar == 3) {
                type = "Deluxe";
                daftarKamarLoop = false;
            } else if (menuKamar == 4) {
                type = "Suite";
                daftarKamarLoop = false;
            } else if (menuKamar == 5) {
                type = "Family";
                daftarKamarLoop = false;
            } else {
                System.out.println("Input Tidak Valid");
                daftarKamarLoop = true;
            }
        }
        return type;
    }

    public IdentificationType chooseTypeOfIdentification() {
        System.out.println("-- Tipe Identitas --");
        System.out.println("1. KTP");
        System.out.println("2. SIM");

        int inputktpSim = InputScanner.readInt("Masukkan Menu KTP/SIM");

        if (inputktpSim == 1) {
            return IdentificationType.KTP;
        } else if (inputktpSim == 2) {
            return IdentificationType.SIM;
        } else {
            System.out.println("KTP/SIM?");
            return chooseTypeOfIdentification();
        }
    }

    public String chooseOrderStatus() {
        System.out.println("-- Order Status --");
        System.out.println("1. In_Progress");
        System.out.println("2. Successful");
        System.out.println("3. Cancelled");

        int inputStatus = InputScanner.readInt("Masukkan Menu Order Status");

        if (inputStatus == 1) {
            return "In_Progress";
        } else if (inputStatus == 2) {
            return "Successful";
        } else if (inputStatus == 3) {
            return "Cancelled";
        } else {
            System.out.println("Pilihan Order Status tidak valid, silakan pilih lagi.");
            return chooseOrderStatus();
        }
    }

    public StatusType chooseStatusType() {
        System.out.println("-- Tipe Identitas --");
        System.out.println("1. active");
        System.out.println("2. non_active");

        int MenuTamu = InputScanner.readInt("Masukkan Menu");

        if (MenuTamu == 1) {
            return StatusType.active;
        } else if (MenuTamu == 2) {
            return StatusType.non_active;
        } else {
            System.out.println("Masukkan dengan benar");
            return chooseStatusType();
        }
    }

    public RoomType chooseRoomType() {
        System.out.println("-- Tipe Kamar --");
        System.out.println("1. Standard");
        System.out.println("2. Superior");
        System.out.println("3. Family");
        System.out.println("4. Deluxe");
        System.out.println("5. Suite");

        int menuKamar = InputScanner.readInt("Masukkan Menu");

        switch (menuKamar) {
            case 1:
                return RoomType.Standard;
            case 2:
                return RoomType.Superior;
            case 3:
                return RoomType.Family;
            case 4:
                return RoomType.Deluxe;
            case 5:
                return RoomType.Suite;
            default:
                System.out.println("Masukkan dengan benar");
                return chooseRoomType();
        }
    }

    public void RegisterGuest() {
        String name = InputScanner.readString("Masukkan USERNAME");
        String password = InputScanner.readPassword("Masukkan PASSWORD");
        IdentificationType identification_type = chooseTypeOfIdentification();

        String phone = InputScanner.readString("Masukkan Nomor Telepon");
        String email = InputScanner.readString("Masukkan Email");
        StatusType status = StatusType.valueOf("active");
        StatusRole role = StatusRole.valueOf("Guest");

        config db = new config();

        int users_id = db.register(name, password, identification_type, phone, email, status, role);
        user Users = new user(users_id, name, password, identification_type, phone, email, status, role);

    }

    public void inputOrder(int users_id) {
        config db = new config();

        boolean validKamar = false;
        int room_id = 0;

        String type = daftarKamar();
        boolean available;

        Date Check_in_date;
        Date Check_out_date;

        do {
            Check_in_date = InputScanner.readDate("Check in date ", "dd-MM-yyyy");
            Check_out_date = InputScanner.readDate("Check out date", "dd-MM-yyyy");
            // db.showRoom();
            db.showAvailableRooms(Check_in_date, Check_out_date, type);

            available = db.areRoomsAvailable(Check_in_date, Check_out_date, type);

            if (available) {
                System.out.println("Kamar " + type + " tersedia pada periode tersebut.");
            } else {
                System.out.println("\nKamar " + type + " tidak tersedia pada periode tersebut.");
            }
        } while (!available);

        while (!validKamar) {
            room_id = InputScanner.readInt("Masukkan Nomor Kamar Yang Ingin Di pesan");
            System.out.println("");
            if (!db.cekRoomNumber(room_id, type)) {
                System.out.println("Nomor kamar " + room_id + " tidak ditemukan.");
            } else {
                validKamar = true;
            }
        }
        Date booking_date = getCurrentDate();
        PaymentMethodType payment_method = PaymentMethodType.valueOf("Cash");
        OrderStatusType orders_status = OrderStatusType.valueOf("In_Progress");

        int total_price = db.getPriceRoom(room_id);
        int admin_id = 0; // Set admin_id to null

        int orders_id = db.insertOrder(Check_in_date, Check_out_date, booking_date, total_price, payment_method,
                orders_status, room_id, users_id);

        order Orders = new order(orders_id, Check_in_date, Check_out_date, booking_date, total_price, payment_method,
                orders_status, room_id, users_id, admin_id);

        int jarakHari = db.getDateDifference(orders_id);
        int totalWithJarakHari = total_price * jarakHari;

        db.updateTotalPrice(orders_id, totalWithJarakHari);
        db.showOrder(orders_id);
        System.out.println("Jumlah Hari : " + jarakHari);
        System.out.println("Total Harga : " + totalWithJarakHari);

        int bayar = InputScanner.readInt("Masukkan Uang Anda");
        int totalPrice = db.getTotalPrice(orders_id);
        int kembalian = bayar - totalPrice;

        if (bayar < totalPrice) {
            System.out.println("Uang Anda kurang. Total Harga dari yang anda pesan adalah : " + totalPrice);
            orders_status = OrderStatusType.valueOf("Cancelled");
            db.updateOrder(orders_id, orders_status);
            return;
        }
    }

    public void daftarPesanan(int users_id) {
        config db = new config();

        if (!db.cekOrder()) {
            System.out.println("\nTidak ada pesanan saat ini.");
            return;
        }

        db.showOrder();

        int orders_id = InputScanner.readInt("Masukkan ID orders dari Pengguna yang ingin di Konfirmasi");

        if (!db.cekOrder(orders_id)) {
            System.out.println("\nPesanan dengan ID tersebut tidak ditemukan.");
            return;
        }

        OrderStatusType orders_status = OrderStatusType.valueOf(chooseOrderStatus());
        int admin_id = users_id;

        db.updateOrder(orders_id, admin_id, orders_status);
        db.showOrder(orders_id);
    }

    public void MenuEditUser(int users_id) {
        System.out.println("1. Lanjut");
        System.out.println("0. Kembali");
        int MenuLanjut = InputScanner.readInt("Lanjut Mengedit Data?");

        if (MenuLanjut == 1) {
            inputEditUser(users_id);
        } else if (MenuLanjut == 0) {
            return;
        } else {
            System.out.println("Mohon Input Dengan Benar");
        }
    }

    public void inputEditUser(int users_id) {
        String name = InputScanner.readString("Masukkan USERNAME");
        String password = InputScanner.readPassword("Masukkan PASSWORD");
        IdentificationType identification_type = chooseTypeOfIdentification();
        String phone = InputScanner.readString("Masukkan Nomor Telepon");
        String email = InputScanner.readString("Masukkan Email");

        config db = new config();

        db.updateUser(users_id, name, password, identification_type, phone, email);
    }

    public void EditStatusUser() {
        config db = new config();

        int users_id = InputScanner.readInt("Masukkan ID User");
        if (!db.cekUser(users_id)) {
            System.out.println("\nUser dengan ID tersebut tidak ditemukan.");
            return;
        }
        StatusType status = chooseStatusType();

        db.updateUser(users_id, status);
    }

    public void MenuEditKamar() {
        System.out.println("-- Edit Kamar --");
        System.out.println("1. Lanjut");
        System.out.println("0. Kembali");
        int MenuLanjut = InputScanner.readInt("Lanjut Mengedit Data Kamar?");

        if (MenuLanjut == 1) {
            EditKamar();
        } else if (MenuLanjut == 0) {
            return;
        } else {
            System.out.println("Mohon Input Dengan Benar");
        }
    }

    public void EditKamar() {
        config db = new config();

        int room_id = InputScanner.readInt("Masukkan ID Kamar");
        if (!db.cekRoomNumber(room_id)) {
            System.out.println("\nRoom dengan ID tersebut tidak ditemukan.");
            return;
        }
        RoomType type = chooseRoomType();
        int price = InputScanner.readInt("Masukkan Price Baru");
        String Description = InputScanner.readString("Masukkan Description Baru");

        db.updateRoom(room_id, type, price, Description);
    }

    public Date getCurrentDate() {
        LocalDate today = LocalDate.now();
        return Date.valueOf(today);
    }
}
