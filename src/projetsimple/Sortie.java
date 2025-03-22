package projetsimple;
public class Sortie extends CaseTraversable{
    public Sortie(int x, int y, int z){
        super(x,y);
        this.chaleur = 0;
    }

    public boolean estTraversable() {
        return true;
    }
}
