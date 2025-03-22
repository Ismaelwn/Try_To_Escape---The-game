package projetsimple;
public abstract class CaseTraversable extends Case{
    protected int chaleur;
    public CaseTraversable(int x, int y) {
        super(x, y);
        this.chaleur = 0;
    }
    public CaseTraversable(int x, int y, boolean hp){
        super(x, y);
        this.chaleur = 0;
    }
}


