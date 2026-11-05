// "Ecranul" pe care scrie structura de fire.
// In consola: mesajele se afiseaza cu println, iar lista() e cea din Java.
// In interfata grafica: totul ajunge in fereastra.

public interface Jurnal {

    // un mesaj obisnuit (pasii programului, ce afiseaza firele)
    void mesaj(String text);

    // afiseaza continutul unui grup (echivalentul lui grup.list())
    void lista(ThreadGroup grup);
}
