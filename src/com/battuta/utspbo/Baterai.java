package com.battuta.utspbo;

public class Baterai {
    // 1) Atribut Private
    // serialNumber bersifat read-only: hanya dapat diinisialisasi via konstruktor (final, tanpa setter)
    private final String serialNumber;
    private double kapasitasKwh;
    private int levelPersen;

    // Konstruktor Utama
    public Baterai(String serialNumber, double kapasitasKwh, int levelPersen) {
        this.serialNumber = serialNumber;
        setKapasitasKwh(kapasitasKwh);
        setLevelPersen(levelPersen);
    }

    // Copy Constructor untuk mendukung defensive copying
    public Baterai(Baterai sumber) {
        this.serialNumber = sumber.serialNumber;
        this.kapasitasKwh = sumber.kapasitasKwh;
        this.levelPersen = sumber.levelPersen;
    }

    // 2) Enkapsulasi & Validasi:
    // b) serialNumber bersifat read-only (hanya ada getter, tidak ada setter)
    public String getSerialNumber() {
        return serialNumber;
    }

    public double getKapasitasKwh() {
        return kapasitasKwh;
    }

    public void setKapasitasKwh(double kapasitasKwh) {
        this.kapasitasKwh = Math.max(0.0, kapasitasKwh);
    }

    public int getLevelPersen() {
        return levelPersen;
    }

    // a) levelPersen hanya boleh 0 - 100: negatif -> 0, > 100 -> 100
    public void setLevelPersen(int levelPersen) {
        if (levelPersen < 0) {
            this.levelPersen = 0;
        } else if (levelPersen > 100) {
            this.levelPersen = 100;
        } else {
            this.levelPersen = levelPersen;
        }
    }

    // 3) Perilaku (Method):
    // a) pakaiBaterai(int persen): Mengurangi levelPersen, tidak boleh turun di bawah 0%
    public void pakaiBaterai(int persen) {
        if (persen <= 0) {
            System.out.println("[Peringatan] Persentase pemakaian baterai (" + serialNumber + ") harus lebih dari 0%.");
            return;
        }
        setLevelPersen(this.levelPersen - persen);
    }

    // b) isiPenuh(): Mengembalikan levelPersen ke 100%
    public void isiPenuh() {
        this.levelPersen = 100;
    }

    public String getInfo() {
        return String.format("Baterai[SN=%s, Kapasitas=%.2f kWh, Level=%d%%]",
                serialNumber, kapasitasKwh, levelPersen);
    }
}
