package projetsimple;
public class Hall extends CaseTraversable{
    protected boolean clef;
    public Hall(int x, int y){
        super(x, y);
        this.clef = false;
        this.chaleur = 0;
    }
    public Hall(int x, int y, boolean xd){
        super(x, y);
        this.clef = xd;
        this.chaleur = 0;
    }
    public Hall(int x, int y, int hp){
        super(x, y);
        this.clef = false;
        this.chaleur = hp;
    }
    public Hall(int x, int y, boolean xd, int chaleur){
        super(x, y);
        this.clef = xd;
        this.chaleur = chaleur;
    }
    public boolean estTraversable(){
        return true;
    }

    public void entre(Joueur j){j.setC(this);
    }
}
