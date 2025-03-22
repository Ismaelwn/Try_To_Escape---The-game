package projetsimple;
import bonus.EnregistrementUtilisateur;
import bonus.Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Furfeux {

    Terrain terrain;
    Joueur joueur;



    public Furfeux(String f) {
        this.terrain = new Terrain(f);
        this.joueur = terrain.getJoueur();
    }

    public Joueur getJoueur() { // POUR POUVOIR RECUPERER LE SCORE DU JOUEUR/
        return joueur;
    }

    public void tour() {
        Son z = new Son("src/projetsimple/son/hit.wav");
        if (joueur.getC().chaleur > 0) { // comme ca les sons ne s'accumulent pas a la suite des autres, ils continueront de s'executer au maximum une seconde apres avoir quitté la case en feu
            z.jouer(false);
        }else {
            z.arreter();
        }
        joueur.setResistance(joueur.getResistance() - joueur.getC().chaleur); // d'apres la consigne
        terrain.appliqueFeu();
    }



    public boolean partieFinie() {
        Son perdu = new Son("src/projetsimple/son/gameover.wav");
        Son gagne = new Son("src/projetsimple/son/gamewin.wav");
        if( this.joueur.getResistance() <= 0 ){
            Menu.z.arreter();
            perdu.jouer(false);
            return true;
        }else if(((CaseTraversable)this.joueur.getC()) instanceof Sortie ){
            Menu.z.arreter();
            gagne.jouer(false);
            return true;
        }else{
            return false;
        }
    }

    public Terrain getTerrain() { // AJOUTER POUR LE BOUTON
        return terrain;
    }

    public static void main(String[] args) {
        int tempo = 100; //etait a 100
        Furfeux jeu = new Furfeux("manoir.txt");
        FenetreJeu graphic = new FenetreJeu(jeu.terrain);
        Timer timer = new Timer(tempo, e -> {
            jeu.tour();
            graphic.repaint();
            if (jeu.partieFinie()) {
                graphic.ecranFinal(Math.max(0, jeu.joueur.getResistance()),"karim");
                ((Timer)e.getSource()).stop();


            }

        });
        //timer = new Timer(tempo, e -> ); // A quoi tu sert
        timer.start();

    }
}
