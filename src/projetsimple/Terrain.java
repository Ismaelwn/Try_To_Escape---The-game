package projetsimple;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Terrain {
    private int hauteur, largeur;
    private Case[][] carte;
    private Joueur joueur;

    private int resistanceJoueur;
    private int cles;


    public long dureePartie;

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public Terrain(String file) {
        try {
            Scanner sc = new Scanner(new FileInputStream(file));
            this.hauteur = sc.nextInt();
            this.largeur = sc.nextInt();
            sc.nextLine();
            resistanceJoueur = sc.nextInt();
            cles = sc.nextInt();
            sc.nextLine();
            this.carte = new Case[hauteur][largeur];
            for (int l = 0; l < hauteur; l++) {
                String line = sc.nextLine();
                for (int c = 0; c < largeur; c++) {
                    Case cc;
                    Character ch = line.charAt(c);
                    switch (ch) {
                        case '#':
                            cc = new Mur(l, c);
                            break;
                        case ' ':
                            cc = new Hall(l, c);
                            break;
                        case '+':
                            cc = new Hall(l, c); // J'ai touché
                            break;
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                            cc = new Hall(l, c, 0); // J'ai touché
                            break;

                        case '@':
                            cc = new Porte(l, c, false);
                            break;
                        case '.':
                            cc = new Porte(l, c, true);
                            break;
                        default:
                            cc = null;
                            break;
                    }
                    carte[l][c] = cc;
                }
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.poseJoueur(8, 12, 10, 22); // J'ai choisi de deposer aleatoirement le joueur dans une zone pour eviter le fait que le joeuur spawn et ait comme premier reflexe de de retenir les poses des clefs
        this.poseSortie(); //J'ai choisi de deposer la sortie aleatoirement et une clef en plus au cas ou on se trompe d'endroit pour dynamiser le jeu
        RandomChaleurCase(this.getAllCaseHalls()); // J'ai choisi de retirer le feu proposé par le professeur pour augmenter le dynamisme du jeu et augmenter la part d'alea dans le score
        initialise(); // J'ai initialisé des zones ou dans lesquels les clefs n'ont pas de places fixes et peuvent spawn a des endroits differents

    }



    public void poseJoueur(int x1, int y1, int x2, int y2){ // Je donne une zone en parametre et je pose aleatoirement le joueur parmi une case de la zone
        ArrayList<CaseTraversable>rtrn = new ArrayList<>(); // il va contenir la futur zone
        Random indice = new Random();
        for(int x = 0; x < hauteur; x++){
            for(int y = 0; y < largeur; y++){
                if( x >= x1 && x <= x2 && y >= y1 && y <= y2 ){ // la 1ere condition
                    if(getCarte()[x][y] instanceof Hall){ // la 2em condition
                        rtrn.add((Hall)getCarte()[x][y]);
                    }
                }
            }
        }
        if (this.joueur != null) throw new IllegalArgumentException("carte avec deux joueurs"); // pour etre sur qu'il y a pas deux joueurs
        this.joueur = new Joueur((CaseTraversable) rtrn.get(indice.nextInt(rtrn.size())) , resistanceJoueur, cles);
        ((Hall) rtrn.get(indice.nextInt(rtrn.size()))).entre(joueur);


    }


    public ArrayList<Hall> getAllCaseHalls(){ // Je cherche juste toutes les cases de type hall
        ArrayList<Hall>rtrn = new ArrayList<>();
        for(int x = 0; x < hauteur; x++){
            for(int y = 0; y < largeur; y++){
                if(getCarte()[x][y] instanceof Hall && getCarte()[x][y] != getJoueur().getC() ){
                    rtrn.add((Hall)getCarte()[x][y]);
                }
            }
        }

        return rtrn;
    }

    public void poseSortie() { // PAREIL
        Random x = new Random();
        int n = x.nextInt(2);
        if (n == 0) { // SOIT la sortie est dans la zone 1 et du coup dans la zone 2 je rajoute une clef au cas ou le joueur est dans le zone 2, donc il lui manquera une clef
            carte[1][1] =new Sortie(1, 1, 0);
            //ZONE 4
            // [x] de 9 a 10 [y] de 1 a 5
            ArrayList<Hall> zone4 = getAllCaseHallsBounds(9,1,10,5);
            int IndiceCaseClef4 = x.nextInt(zone4.size());
            zone4.get(IndiceCaseClef4).clef = true;
        } else {
            carte[9][1] =new Sortie(9,1 , 0);
            ArrayList<Hall> zone4 = getAllCaseHallsBounds(1,1,3,5);
            int IndiceCaseClef4 = x.nextInt(zone4.size());
            zone4.get(IndiceCaseClef4).clef = true;
        }
    }

    public void RandomChaleurCase(ArrayList<Hall> param) {
        ArrayList<Hall> tmp = param; // Pendant le jeu on initialise la chaleur que des cases Halls
        Random x = new Random();
        for (Hall c : tmp) { // J'ai choisi ces probabilités de chaleur pour essayer de rendre le jeu dynamique
            int newProb = x.nextInt(169);
            if(newProb >= 36 && newProb < 48){
                c.chaleur = 4;
            }else if(newProb >= 6 && newProb < 18){
                c.chaleur = 3;
            }else if(newProb >= 18  && newProb < 36){
                c.chaleur = 2;
            }else if(newProb >= 48  && newProb < 72){
                c.chaleur = 1;
            }else{
                c.chaleur = 0;
            }
        }
    }

    public ArrayList<Hall> getAllCaseHallsBounds(int x1, int y1, int x2, int y2){ // meme principe que pour poseJoueur, je cherche toutes les cases appartenant a une zone dont les coordonnées sont données en parametre
        ArrayList<Hall>rtrn = new ArrayList<>();
        for(int x = 0; x < hauteur; x++){
            for(int y = 0; y < largeur; y++){
                if( x >= x1 && x <= x2 && y >= y1 && y <= y2 ){
                    if(getCarte()[x][y] instanceof Hall && getCarte()[x][y] != getJoueur().getC() ){
                        rtrn.add((Hall)getCarte()[x][y]);
                    }
                }
            }
        }
        return rtrn;
    }



    public void initialise() { // j'initialise aleatoirement le positionnement des clefs dans des zones determinés
        Random Alea = new Random();
        //ZONE 1
        // [x] de 8 a 10 [y] de 12 a 22 && [x] de 7 a 3 [y] de 20 a 22
        ArrayList<Hall> zone1 = getAllCaseHallsBounds(8, 12, 10, 22);
        //this.RandomChaleurCase(zone1);
        int IndiceCaseClef1 = Alea.nextInt(zone1.size());

        zone1.get(IndiceCaseClef1).clef = true;

        ArrayList<Hall> zone1bis = getAllCaseHallsBounds(3, 20, 7,22);
        //this.RandomChaleurCase(zone1bis);
        int IndiceCaseClef1bis = Alea.nextInt(zone1bis.size());
        zone1bis.get(IndiceCaseClef1bis).clef = true;

        //ZONE 2
        // [x] de 1 a 2 [y] de 15 a 22
        ArrayList<Hall> zone2 = getAllCaseHallsBounds(1, 15,2 ,22 );
        int IndiceCaseClef2 = Alea.nextInt(zone2.size());
        //this.RandomChaleurCase(zone2);
        zone2.get(IndiceCaseClef2).clef = true;

        //ZONE 3
        // [x] de 1 a 10 [y] de 8 a 12
        ArrayList<Hall> zone3 = getAllCaseHallsBounds(1, 8,10,12);
        int IndiceCaseClef3 = Alea.nextInt(zone3.size());
        //this.RandomChaleurCase(zone3);
        zone3.get(IndiceCaseClef3).clef = true;
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    public Case[][] getCarte() {
        return carte;
    }

    public ArrayList<CaseTraversable> getVoisinesTraversables(int x, int y) { // Pour ne pas sortir des bornes ou manier des objets qui s'initialise pas, j'ai fait ca pour eviter de gerer trop de cas
        ArrayList<CaseTraversable> rtrn = new ArrayList<>();
        // vérification des voisins à gauche et à droite
        for (int i = y - 1; i <= y + 1; i++) {
            if (i >= 0 && i < largeur && x - 1 >= 0 && x - 1 < hauteur) { // On evite d'etre en dehors des bornes
                if (this.carte[x - 1][i] != null && this.carte[x - 1][i].estTraversable()) {
                    rtrn.add((CaseTraversable) this.carte[x - 1][i]);
                }
            }
            if (i >= 0 && i < largeur && x + 1 >= 0 && x + 1 < hauteur) { // On evite d'etre en dehors des bornes
                if (this.carte[x + 1][i] != null && this.carte[x + 1][i].estTraversable()) {
                    rtrn.add((CaseTraversable) this.carte[x + 1][i]);
                }
            }
        }

        // vérification des voisins en haut et en bas par rapport a la representation dans Manoir.txt
        if (y - 1 >= 0 && y - 1 < largeur && x >= 0 && x < hauteur) {
            if (this.carte[x][y - 1] != null && this.carte[x][y - 1].estTraversable()) {
                rtrn.add((CaseTraversable) this.carte[x][y - 1]);
            }
        }
        if (y + 1 >= 0 && y + 1 < largeur && x >= 0 && x < hauteur) {
            if (this.carte[x][y + 1] != null && this.carte[x][y + 1].estTraversable()) {
                rtrn.add((CaseTraversable) this.carte[x][y + 1]);
            }
        }

        return rtrn;
    }



    public void incrementeCase(CaseTraversable c) {
        if (c.chaleur < 10) {
            c.chaleur++;
        }
    }


    public void decrementeCase(CaseTraversable c) { // j'aurai pu ne pas creer deux fonctions pour faire que cela, mais j'ai preferé le faire
        if (c.chaleur > 0) {
            c.chaleur--;
        }


    }
    Random rand = new Random(); // Generateur d'aleas pour la fonction d'en bas, j'ai preferé au depart du projet de la definir comme une variable globale

    public void propageFeux(CaseTraversable cible){ // Selon les consignes de l'ennoncé
        ArrayList<CaseTraversable> tmp = getVoisinesTraversables(cible.lig, cible.col);
        int rtrn = cible.chaleur;
        int ind = rand.nextInt(200);
        for(int i = 0; i < tmp.size(); i++){
            rtrn+= tmp.get(i).chaleur;
        }
        if(rtrn > ind ){
            incrementeCase(cible);
        }else if( ind >= 190){
            decrementeCase(cible);
        }else{
            return;
        }
    }

    public void appliqueFeu() { // J'ai preferé faire une autre fonction pour applique la fonction du haut pour gerer plus facilement les erreurs d'interpretations de ma part
        for (int x = 0; x < hauteur; x++) {
            for (int y = 0; y < largeur; y++) {
                if (this.getCarte()[x][y] instanceof CaseTraversable) {
                    propageFeux((CaseTraversable)this.getCarte()[x][y]);
                }
            }
        }
    }

    public boolean visible( Case x, int z, int y){ // Selon l'enoncé du prof
        return(( x.lig - z )*( x.lig - z )+( x.col - y )*( x.col - y ) <= 10 );
    }


}

