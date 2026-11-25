package com.battuta.utspbo;

public class StasiunSwap {
    // 1) Atribut Private
    private final String idStasiun;
    private final Baterai[] slotBaterai;
    private int jumlahSwapLokal;

    // 2) Anggota Statis (Static Members)
    private static int totalSwapSistem = 0;
    public static final double TARIF_PER_SWAP = 10000.0;

    public static int getTotalSwapSistem() {
        return totalSwapSistem;
    }

    // 3) Konstruktor:
    // a) Constructor Overloading 1: Menerima idStasiun dan menginisialisasi 5 slot kosong (null)
    public StasiunSwap(String idStasiun) {
        this.idStasiun = idStasiun;
        this.slotBaterai = new Baterai[5];
        this.jumlahSwapLokal = 0;
    }

    // b) Constructor Overloading 2: Menerima idStasiun dan array Baterai[] dengan defensive copying
    public StasiunSwap(String idStasiun, Baterai[] daftarBateraiAwal) {
        this(idStasiun);
        if (daftarBateraiAwal != null) {
            if (daftarBateraiAwal.length > this.slotBaterai.length) {
                System.out.println("[Info - " + idStasiun + "] Jumlah baterai awal melebihi 5 slot. Hanya 5 slot pertama yang diisi.");
            }
            int batas = Math.min(daftarBateraiAwal.length, this.slotBaterai.length);
            for (int i = 0; i < batas; i++) {
                if (daftarBateraiAwal[i] != null) {
                    // Defensive copying: membuat array baru + salinan objek Baterai baru
                    // agar referensi internal stasiun tidak dapat dibajak/dimodifikasi dari luar
                    this.slotBaterai[i] = new Baterai(daftarBateraiAwal[i]);
                }
            }
        }
    }

    // 4) Perilaku (Method):
    // a) pasangBaterai(Baterai b, int noSlot): Memasang baterai ke slot 0-4 dengan validasi slot kosong
    public void pasangBaterai(Baterai b, int noSlot) {
        if (b == null) {
            System.out.println("[Gagal Pasang - " + idStasiun + "] Baterai tidak valid (null).");
            return;
        }
        if (noSlot < 0 || noSlot >= slotBaterai.length) {
            System.out.println("[Gagal Pasang - " + idStasiun + "] Nomor slot " + noSlot +
                               " tidak valid! Pilih indeks slot 0 hingga " + (slotBaterai.length - 1) + ".");
            return;
        }
        if (slotBaterai[noSlot] != null) {
            System.out.println("[Gagal Pasang - " + idStasiun + "] Slot " + noSlot +
                               " sudah terisi oleh baterai " + slotBaterai[noSlot].getSerialNumber() + ".");
            return;
        }
        this.slotBaterai[noSlot] = b;
        System.out.println("[Pasang Berhasil - " + idStasiun + "] " + b.getSerialNumber() +
                           " (" + b.getLevelPersen() + "%) dipasang ke Slot " + noSlot + ".");
    }

    // b) prosesSwap(Baterai bateraiKonsumen, int noSlot):
    //    Syarat bisnis: bateraiKonsumen <= 20% dan baterai di noSlot stasiun >= 90%.
    public Baterai prosesSwap(Baterai bateraiKonsumen, int noSlot) {
        if (bateraiKonsumen == null) {
            System.out.println("[Swap Ditolak - " + idStasiun + "] Baterai konsumen tidak valid (null).");
            return null;
        }
        if (noSlot < 0 || noSlot >= slotBaterai.length) {
            System.out.println("[Swap Ditolak - " + idStasiun + "] Nomor slot " + noSlot +
                               " tidak valid! Pilih indeks 0 hingga " + (slotBaterai.length - 1) + ".");
            return bateraiKonsumen;
        }
        if (slotBaterai[noSlot] == null) {
            System.out.println("[Swap Ditolak - " + idStasiun + "] Slot " + noSlot +
                               " dalam keadaan kosong, tidak ada baterai pengganti.");
            return bateraiKonsumen;
        }
        if (bateraiKonsumen.getLevelPersen() > 20) {
            System.out.println("[Swap Ditolak - " + idStasiun + "] Daya baterai konsumen (" +
                               bateraiKonsumen.getSerialNumber() + ") masih " + bateraiKonsumen.getLevelPersen() +
                               "% (> 20%). Penukaran hanya diperbolehkan jika daya <= 20%.");
            return bateraiKonsumen;
        }

        Baterai bateraiStasiun = slotBaterai[noSlot];
        if (bateraiStasiun.getLevelPersen() < 90) {
            System.out.println("[Swap Ditolak - " + idStasiun + "] Baterai pada Slot " + noSlot + " (" +
                               bateraiStasiun.getSerialNumber() + ") belum siap pakai, daya baru " +
                               bateraiStasiun.getLevelPersen() + "% (< 90%).");
            return bateraiKonsumen;
        }

        // Lakukan penukaran baterai
        slotBaterai[noSlot] = bateraiKonsumen;
        this.jumlahSwapLokal++;
        totalSwapSistem++;

        System.out.println("[Swap Berhasil - " + idStasiun + "] Slot " + noSlot + ": Konsumen menukar " +
                           bateraiKonsumen.getSerialNumber() + " (" + bateraiKonsumen.getLevelPersen() + "%) -> menerima " +
                           bateraiStasiun.getSerialNumber() + " (" + bateraiStasiun.getLevelPersen() + "%). Biaya: Rp " +
                           String.format("%,.2f", TARIF_PER_SWAP));
        return bateraiStasiun;
    }

    // c) Method Overloading hitungPendapatan:
    public double hitungPendapatan() {
        return this.jumlahSwapLokal * TARIF_PER_SWAP;
    }

    public double hitungPendapatan(double diskonMitraPersen) {
        double diskonValid = Math.max(0.0, Math.min(100.0, diskonMitraPersen));
        double omzetKotor = hitungPendapatan();
        return omzetKotor - (omzetKotor * (diskonValid / 100.0));
    }

    // Getters & Utilitas
    public String getIdStasiun() {
        return idStasiun;
    }

    public int getJumlahSwapLokal() {
        return jumlahSwapLokal;
    }

    // Defensive copy saat mengembalikan array slot agar referensi internal tetap terlindungi
    public Baterai[] getSlotBaterai() {
        Baterai[] salinan = new Baterai[slotBaterai.length];
        for (int i = 0; i < slotBaterai.length; i++) {
            if (slotBaterai[i] != null) {
                salinan[i] = new Baterai(slotBaterai[i]);
            }
        }
        return salinan;
    }

    public void tampilkanStatusSlot() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Status Slot Stasiun: " + idStasiun + " (Swap Lokal: " + jumlahSwapLokal + ")");
        for (int i = 0; i < slotBaterai.length; i++) {
            if (slotBaterai[i] == null) {
                System.out.println("  Slot [" + i + "] : <KOSONG>");
            } else {
                System.out.println("  Slot [" + i + "] : " + slotBaterai[i].getInfo());
            }
        }
        System.out.println("------------------------------------------------------------");
    }
}
