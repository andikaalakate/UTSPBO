package com.battuta.utspbo;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   SISTEM PENUKARAN BATERAI MOTOR LISTRIK OTOMATIS (UTS)    ");
        System.out.println("============================================================");

        // 1. Pengujian Enkapsulasi & Validasi Kelas Baterai
        System.out.println("\n--- 1. Uji Validasi Enkapsulasi Baterai ---");
        Baterai bTestMin = new Baterai("BAT-ERR-MIN", 2.5, -25);  // Otomatis 0%
        Baterai bTestMax = new Baterai("BAT-ERR-MAX", 2.5, 140);  // Otomatis 100%
        System.out.println("Input level -25 -> " + bTestMin.getInfo());
        System.out.println("Input level 140 -> " + bTestMax.getInfo());

        bTestMax.pakaiBaterai(120); // Mencoba pakai 120% -> tidak boleh < 0%
        System.out.println("Setelah pakaiBaterai(120) -> " + bTestMax.getInfo());
        bTestMax.isiPenuh();
        System.out.println("Setelah isiPenuh()         -> " + bTestMax.getInfo());

        // 2. Pembuatan StasiunSwap #1 (Constructor Overloading 1 + pasangBaterai)
        System.out.println("\n--- 2. Inisialisasi Stasiun 1 & Pengisian Slot ---");
        StasiunSwap stasiun1 = new StasiunSwap("ST-MEDAN-01");
        Baterai bStasiunA = new Baterai("BAT-ST1-01", 2.5, 100); // Siap swap (>= 90%)
        Baterai bStasiunB = new Baterai("BAT-ST1-02", 2.5, 75);  // Belum siap (< 90%)
        Baterai bStasiunC = new Baterai("BAT-ST1-03", 2.5, 95);  // Siap swap (>= 90%)

        stasiun1.pasangBaterai(bStasiunA, 0);
        stasiun1.pasangBaterai(bStasiunB, 1);
        stasiun1.pasangBaterai(bStasiunC, 2);
        // Uji validasi pasang ke slot yang sudah terisi
        stasiun1.pasangBaterai(new Baterai("BAT-DUPLIKAT", 2.5, 100), 0);
        stasiun1.tampilkanStatusSlot();

        // 3. Pembuatan StasiunSwap #2 (Constructor Overloading 2 + Uji Defensive Copying)
        System.out.println("\n--- 3. Inisialisasi Stasiun 2 & Uji Defensive Copying ---");
        Baterai bLuar1 = new Baterai("BAT-ST2-01", 2.5, 98);
        Baterai[] paketAwal = { bLuar1, new Baterai("BAT-ST2-02", 2.5, 100) };
        StasiunSwap stasiun2 = new StasiunSwap("ST-MEDAN-02", paketAwal);

        // Upaya pembajakan referensi eksternal setelah konstruktor
        paketAwal[0] = null;
        bLuar1.setLevelPersen(5);
        System.out.println("Setelah array luar[0]=null & bLuar1 diubah jadi 5%, status internal Stasiun 2 tetap aman:");
        stasiun2.tampilkanStatusSlot();

        // 4. Simulasi Skenario Penukaran Baterai
        System.out.println("\n--- 4. Simulasi Skenario Penukaran (Swap) ---");
        Baterai bateraiMotor1 = new Baterai("BAT-USR-01", 2.5, 50); // Masih 50%

        // a) Skenario Gagal 1: Daya baterai konsumen masih > 20%
        System.out.println("\n[Skenario A1] Konsumen mencoba swap saat daya baterai masih 50%:");
        bateraiMotor1 = stasiun1.prosesSwap(bateraiMotor1, 0);

        // Konsumen memakai motornya sehingga daya baterai turun menjadi 15% (<= 20%)
        bateraiMotor1.pakaiBaterai(35);
        System.out.println("\nMotor digunakan -> kondisi baterai konsumen saat ini: " + bateraiMotor1.getInfo());

        // a) Skenario Gagal 2: Daya baterai di slot stasiun belum siap (< 90%, yaitu Slot 1 = 75%)
        System.out.println("\n[Skenario A2] Konsumen (15%) mencoba swap ke Slot 1 yang dayanya baru 75%:");
        bateraiMotor1 = stasiun1.prosesSwap(bateraiMotor1, 1);

        // a) Skenario Gagal 3: Memilih slot kosong (Slot 4)
        System.out.println("\n[Skenario A3] Konsumen mencoba swap ke Slot 4 yang kosong:");
        bateraiMotor1 = stasiun1.prosesSwap(bateraiMotor1, 4);

        System.out.println("Total Swap Sistem setelah percobaan gagal: " + StasiunSwap.getTotalSwapSistem());

        // b) Skenario Berhasil 1: Konsumen (15%) swap dengan Slot 0 Stasiun 1 (100%)
        System.out.println("\n[Skenario B1] Konsumen (15%) swap ke Slot 0 Stasiun 1 (100%):");
        bateraiMotor1 = stasiun1.prosesSwap(bateraiMotor1, 0);
        System.out.println("Baterai yang kini dipakai motor 1: " + bateraiMotor1.getInfo());

        // b) Skenario Berhasil 2: Konsumen ke-2 (10%) swap dengan Slot 2 Stasiun 1 (95%)
        System.out.println("\n[Skenario B2] Konsumen 2 (10%) swap ke Slot 2 Stasiun 1 (95%):");
        Baterai bateraiMotor2 = new Baterai("BAT-USR-02", 2.5, 10);
        bateraiMotor2 = stasiun1.prosesSwap(bateraiMotor2, 2);

        // b) Skenario Berhasil 3: Konsumen ke-3 (20%) swap dengan Slot 0 Stasiun 2 (98%)
        System.out.println("\n[Skenario B3] Konsumen 3 (20%) swap ke Slot 0 Stasiun 2 (98%):");
        Baterai bateraiMotor3 = new Baterai("BAT-USR-03", 2.5, 20);
        bateraiMotor3 = stasiun2.prosesSwap(bateraiMotor3, 0);

        // Tampilkan status akhir slot stasiun
        System.out.println("\n--- Status Akhir Slot Stasiun ---");
        stasiun1.tampilkanStatusSlot();
        stasiun2.tampilkanStatusSlot();

        // 5. Pengujian Perhitungan Pendapatan (Method Overloading) & Konsistensi Total Swap Global
        System.out.println("\n--- 5. Laporan Pendapatan & Konsistensi Global Swap ---");
        System.out.println("Stasiun " + stasiun1.getIdStasiun() + ":");
        System.out.println("  - Jumlah Swap Lokal        : " + stasiun1.getJumlahSwapLokal() + " transaksi");
        System.out.println("  - Pendapatan Normal        : Rp " + String.format("%,.2f", stasiun1.hitungPendapatan()));
        System.out.println("  - Pendapatan Promo (15%)   : Rp " + String.format("%,.2f", stasiun1.hitungPendapatan(15.0)));

        System.out.println("Stasiun " + stasiun2.getIdStasiun() + ":");
        System.out.println("  - Jumlah Swap Lokal        : " + stasiun2.getJumlahSwapLokal() + " transaksi");
        System.out.println("  - Pendapatan Normal        : Rp " + String.format("%,.2f", stasiun2.hitungPendapatan()));
        System.out.println("  - Pendapatan Promo (10%)   : Rp " + String.format("%,.2f", stasiun2.hitungPendapatan(10.0)));

        int akumulasiLokal = stasiun1.getJumlahSwapLokal() + stasiun2.getJumlahSwapLokal();
        int totalGlobal = StasiunSwap.getTotalSwapSistem();
        System.out.println("\nPembuktian Konsistensi Global:");
        System.out.println("  - Akumulasi Lokal (ST1 + ST2) : " + akumulasiLokal + " transaksi");
        System.out.println("  - StasiunSwap.getTotalSwapSistem() : " + totalGlobal + " transaksi");
        System.out.println("  - Status Konsistensi          : " + (akumulasiLokal == totalGlobal ? "KONSISTEN & VALID" : "TIDAK VALID"));
        System.out.println("============================================================");
    }
}
