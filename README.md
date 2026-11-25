# UTSPBO - Sistem Stasiun Penukaran Baterai Motor Listrik Otomatis
**Universitas Battuta - Program Studi Sistem Informasi**  
**Mata Kuliah:** Prak. Pemrograman Berorientasi Objek  
**Dosen Pengampu:** M. Rhifky Wayahdi, S.Kom., M.Kom.  
**Mahasiswa:** Andika Pratama (NIM: 110225013)  

---

## 📌 Deskripsi Proyek
Implementasi Ujian Tengah Semester (UTS) Ganjil T.A. 2026/2027 menggunakan Apache NetBeans dan JDK 21 untuk memodelkan modul inti sistem penukaran baterai motor listrik otomatis berbasis OOP murni.

## 📂 Struktur Paket & Kelas (`com.battuta.utspbo`)
1. **`Baterai.java`**
   - Atribut `private`: `serialNumber` (`final`, *read-only*), `kapasitasKwh`, dan `levelPersen`.
   - Validasi rentang `levelPersen` (`0–100`), method `pakaiBaterai(int persen)` tanpa turun di bawah `0%`, `isiPenuh()`, serta *copy constructor*.
2. **`StasiunSwap.java`**
   - Atribut `private`: `idStasiun`, `slotBaterai` (`Baterai[5]`), dan `jumlahSwapLokal`.
   - Anggota statis: `private static int totalSwapSistem`, `public static final double TARIF_PER_SWAP = 10000.0`, dan `getTotalSwapSistem()`.
   - *Constructor Overloading*: inisialisasi 5 slot kosong (`null`), dan pengisian array awal `Baterai[]` dengan *defensive copying*.
   - Method `pasangBaterai(Baterai b, int noSlot)`, `prosesSwap(Baterai bateraiKonsumen, int noSlot)` (validasi daya konsumen `<= 20%` dan daya slot stasiun `>= 90%`), serta *method overloading* `hitungPendapatan()` dan `hitungPendapatan(double diskonMitraPersen)`.
3. **`MainApp.java`**
   - Simulasi validasi `Baterai`, pengisian slot, pembuktian *defensive copying*, skenario penukaran gagal (`< 90%` / `> 20%` / slot kosong), skenario penukaran berhasil, perhitungan pendapatan, serta verifikasi konsistensi `StasiunSwap.getTotalSwapSistem()`.

## 📄 Dokumen Laporan
- `docs/Andika Pratama - 110225013 - SI3-PAGI - UTS Prak PBO.docx`
