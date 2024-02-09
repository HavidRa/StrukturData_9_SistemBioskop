import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class Film {
    String judul; 
    int jumlahTiket;
    int jumlahTempatDuduk;
    String jadwalPenayangan;

    public Film(String judul, int jumlahTempatDuduk, String jadwalPenayangan) {
        this.judul = judul;
        this.jumlahTempatDuduk = jumlahTempatDuduk;
        this.jadwalPenayangan = jadwalPenayangan;
        this.jumlahTiket = 0;
    }
}

class Transaksi {
    String judulFilm;
    int jumlahTiket;
    int totalHarga;
    String[] metodePembayaranPerTiket;
    ArrayList<String> namaPemesan;

    public Transaksi(String judulFilm, int jumlahTiket, int totalHarga, String[] metodePembayaranPerTiket, ArrayList<String> namaPemesan) {
        this.judulFilm = judulFilm;
        this.jumlahTiket = jumlahTiket;
        this.totalHarga = totalHarga;
        this.metodePembayaranPerTiket = metodePembayaranPerTiket;
        this.namaPemesan = namaPemesan;
    }
}

class Histori {
    ArrayList<Transaksi> historiTransaksi = new ArrayList<>();
    ArrayList<String> informasiTempatDuduk = new ArrayList<>();
    ArrayList<String> jadwalPenayangan = new ArrayList<>();
}

public class SistemBioskop {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int hargaTiket = 10;

        ArrayList<Film> daftarFilm = new ArrayList<>();
        Queue<Film> antrianPembelian = new LinkedList<>();
        Stack<Film> stackPembelian = new Stack<>();
        Histori histori = new Histori();

        // Memasukkan data film ke dalam list
        daftarFilm.add(new Film("Pemburuan Rantai makanan", 100, "Senin 10:00, Selasa 13:00"));
        daftarFilm.add(new Film("Inoen & Zuleha", 80, "Rabu 11:00, Kamis 14:00"));
        daftarFilm.add(new Film("Sejarah Soeharto Sama ", 120, "Jumat 12:00, Sabtu 15:00"));

        boolean beliLagi = true;

        while (beliLagi) {
            handleMovieBooking(input, daftarFilm, antrianPembelian, stackPembelian, histori, hargaTiket);

            System.out.print("Ingin melakukan transaksi lagi? (ya/tidak): ");
            String pilihan = input.nextLine().toLowerCase();

            if (!pilihan.equals("ya")) {
                break;
            }
        }

        System.out.println("Terima kasih telah menggunakan sistem bioskop. Sampai jumpa!");
    }

    private static void handleMovieBooking(Scanner input, ArrayList<Film> daftarFilm, Queue<Film> antrianPembelian,
            Stack<Film> stackPembelian, Histori histori, int hargaTiket) {

        // Menampilkan daftar film dan jadwal penayangan
        System.out.println("Daftar Film dan Jadwal Penayangan:");
        for (int i = 0; i < daftarFilm.size(); i++) {
            Film film = daftarFilm.get(i);
            System.out.println((i + 1) + ". " + film.judul + " - " + film.jadwalPenayangan);
        }

        // Memilih film untuk pemesanan
        System.out.print("Pilih nomor film untuk pemesanan: ");
        int nomorPilihan = input.nextInt();
        input.nextLine(); // Membersihkan newline

        if (nomorPilihan > 0 && nomorPilihan <= daftarFilm.size()) {
            Film filmPilihan = daftarFilm.get(nomorPilihan - 1);

            // Memasukkan jumlah tiket untuk film pilihan ke dalam antrian pembelian
            System.out.print("Masukkan jumlah tiket untuk film " + filmPilihan.judul + ": ");
            int jumlahTiket = input.nextInt();
            input.nextLine(); // Membersihkan newline

            // Batasi jumlah tiket agar tidak melebihi tempat duduk
            jumlahTiket = Math.min(jumlahTiket, filmPilihan.jumlahTempatDuduk);

            filmPilihan.jumlahTiket = jumlahTiket;
            antrianPembelian.add(filmPilihan);
            stackPembelian.push(filmPilihan);

            // Memilih metode pembayaran per tiket dan mencatat nama pemesan
            String[] metodePembayaranPerTiket = new String[jumlahTiket];
            ArrayList<String> namaPemesan = new ArrayList<>();
            for (int i = 0; i < jumlahTiket; i++) {
                System.out.print("Masukkan nama pemesan tiket ke-" + (i + 1) + ": ");
                namaPemesan.add(input.nextLine());

                System.out.print("Pilih metode pembayaran untuk tiket ke-" + (i + 1) + " (Contoh: Kartu Kredit, Transfer, Tunai): ");
                metodePembayaranPerTiket[i] = input.nextLine();
            }

            // Menangani pembelian tiket dari antrian pembelian dan menyimpan histori
            while (!antrianPembelian.isEmpty()) {
                Film film = antrianPembelian.poll();
                int totalHarga = film.jumlahTiket * hargaTiket;

              
                histori.historiTransaksi.add(new Transaksi(film.judul, film.jumlahTiket, totalHarga, metodePembayaranPerTiket, namaPemesan));
                histori.informasiTempatDuduk.add(film.judul + ": Sisa Tempat Duduk " + (film.jumlahTempatDuduk - film.jumlahTiket));
                histori.jadwalPenayangan.add(film.judul + ": " + film.jadwalPenayangan);
            }

            // Menampilkan histori transaksi, informasi tempat duduk, dan jadwal penayangan
            System.out.println("Histori Transaksi:");
            for (Transaksi transaksi : histori.historiTransaksi) {
                System.out.println("Judul Film: " + transaksi.judulFilm +
                        ", Jumlah Tiket: " + transaksi.jumlahTiket +
                        ", Total Harga: $" + transaksi.totalHarga +
                        ", Metode Pembayaran per Tiket: " + arrayToString(transaksi.metodePembayaranPerTiket) +
                        ", Nama Pemesan: " + arrayToString(transaksi.namaPemesan));
            }

            System.out.println("\nInformasi Tempat Duduk:");
            for (String tempatDuduk : histori.informasiTempatDuduk) {
                System.out.println(tempatDuduk);
            }

            System.out.println("\nJadwal Penayangan:");
            for (String jadwal : histori.jadwalPenayangan) {
                System.out.println(jadwal);
            }
        }
    }

    private static String arrayToString(String[] array) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    private static String arrayToString(ArrayList<String> list) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            if (i < list.size() - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}