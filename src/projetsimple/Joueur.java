package projetsimple;
import javax.sound.sampled.Port;

public class Joueur {
    private CaseTraversable c;
    private int resistance;
    private int cles;

    private int totalClefRamasse;

    private Direction direction;

    public int getResistance() {
        return resistance;
    }

    public Direction getDirection() {
        return direction;
    }

    public CaseTraversable getC(){
        return c;
    }

    public Joueur(CaseTraversable c, int r, int k) {
        this.c = c;
        this.resistance = r;
        this.cles = k;
        this.direction = Direction.nord;
        this.totalClefRamasse = 0;
    }

    public void setC(CaseTraversable c) {
        this.c = c;
        Son z = new Son("src/projetsimple/son/hit.wav");
        if(c.chaleur > 0) {
            z.jouer(false);
        }
        z.arreter();
        this.resistance -= c.chaleur;

         //j'arreterai le jeu
        this.ramasseClef(); // comme ca on rammasse direct la clef
    }

    public int getTotalClefRamasse() { // juste pour savoir le nombre de portes ouvertes car Nb portes ouvertes = nbTotalClefRamasse - nbclef du joeur - 1 car il commence avec une clef
        return totalClefRamasse;
    }

    public int totalPorteOuverte(){
        return getTotalClefRamasse()-(cles-1); // car on commence le jeu avec une clef
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void bouge(Case c) {
        Son z = new Son("src/projetsimple/son/porteouverte.wav");
        if (c == null || c instanceof Mur){ // juste pour gerer d'eventuels bugs
            return;
        }else {
            if (c instanceof Porte) { // si c'est une porte
                if (((Porte) c).ouverte) { // si elle ouverte on agit comme avec un Hall
                    this.setC((Porte) c);
                } else if (!(((Porte) c).ouverte) && this.cles > 0) { // si le joueur est en etat de l'ouvrir pareil
                    z.jouer(false);
                    ((Porte) c).ouverte = true;
                    this.setC((Porte) c);
                    this.decrementeClef();

                }
            } else if (c instanceof Hall) {
                this.setC((Hall) c);
            } else if (c instanceof Sortie) {
                this.setC((Sortie) c);
            }
        }
    }




    public void ramasseClef(){
        Son z = new Son("src/projetsimple/son/ramasseclef.wav");
        if(this.c instanceof Hall && ((Hall) this.c).clef){
            z.jouer(false);
            ((Hall) this.c).clef = false; // puisqu'on vient de prendre la clef
            this.cles++;
            totalClefRamasse++;
        }
    }

    public void decrementeClef() {
        if (cles > 0) {
            this.cles--;     // PAS DE STOCKAGE DE CLEFS NEGATIFS
        }

    }
    public int getCles(){
        return cles;
    }
}


