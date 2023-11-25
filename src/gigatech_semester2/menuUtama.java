package gigatech_semester2;

import DataModel.method;
import connectMYSQL.config;

public class menuUtama {
    public void menuGuest(String name, int users_id) {
        config db = new config();
        method Method = new method();

        boolean MenuUserLoop = true;

        while (MenuUserLoop) {
            System.out.println("==============================================");
            System.out.println("\tSelamat datang -- " + name);
            System.out.println("\t\tLUXEVA Hotel");
            System.out.println("==============================================");
            System.out.println("Menu:");
            System.out.println("1. Pesan Kamar");
            System.out.println("2. History Pemesanan");
            System.out.println("3. View Data Diri");// sekalian update data diri jika mau
            System.out.println("4. EXIT");
            int menuTamu = InputScanner.readInt("Masukkan pilihan kamu");

            switch (menuTamu) {
                case 1:
                    Method.inputOrder(users_id);
                    break;
                case 2:
                    if (!db.cekOrderUser(users_id)) {
                        System.out.println("\nAnda Belum Pernah Order.");
                        break;
                    }
                    db.showOrderHistory(users_id);

                    break;
                case 3:
                    db.showUser(users_id);
                    Method.MenuEditUser(users_id);
                    break;
                case 4:
                    MenuUserLoop = false;
                    InputScanner.clearConsole();
                    return;
                default:
                    InputScanner.clearConsole();
                    System.out.println("Pilihan tidak valid");
            }
        }
    }

    public void MenuOperator(String name, int users_id) {
        config db = new config();
        method met = new method();

        boolean MenuOperatorLoop = true;

        while (MenuOperatorLoop) {
            System.out.println("==============================================");
            System.out.println("\tSelamat datang karyawan -- " + name + " --");
            System.out.println("==============================================");

            System.out.println("Menu:");
            System.out.println("1. Edit Kamar");// edit status dan sekalian nambah kamar disini
            System.out.println("2. Edit tamu");// edit status aja dan sekalian view data tamu
            System.out.println("3. Daftar pesanan");// konfirmasi
            System.out.println("4. Keluar");
            int MenuAd = InputScanner.readInt("Masukkan pilihan kamu");

            switch (MenuAd) {
                case 1:
                    db.showRoom();
                    met.MenuEditKamar();
                    break;
                case 2:
                    db.showUserStatus();
                    met.EditStatusUser();
                    break;
                case 3:
                    met.daftarPesanan(users_id);
                    break;
                case 4:
                    MenuOperatorLoop = false;
                    InputScanner.clearConsole();
                    return;
                default:
                    InputScanner.clearConsole();
                    System.out.println("Pilihan tidak valid");
            }
        }
    }
}
