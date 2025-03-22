package projetsimple;
public class Porte extends CaseTraversable {
    protected boolean ouverte;

    public Porte(int x, int y) {
        super(x, y);
        this.ouverte = false;
        this.chaleur = 0;
        // this.clef = false;
    }

    public Porte(int x, int y, boolean z) {
        super(x, y);
        this.ouverte = z;
        this.chaleur = 0;
        //this.clef = false;
    }

    public boolean estTraversable() {
        if (ouverte) {
            return true;
        } else {
            return false;
        }
    }
}


