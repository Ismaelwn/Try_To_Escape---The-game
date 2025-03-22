package bonus;

public class Pair {
    public String nom;
    public long score;

    public Pair(String n, long x) {
        nom = n;
        score = x;
    }

    public String toString() {
        return (nom + " " + score + "\n");
    }
}
