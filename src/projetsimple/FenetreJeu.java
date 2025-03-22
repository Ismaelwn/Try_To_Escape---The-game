package projetsimple;
import bonus.EnregistrementUtilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class FenetreJeu extends JPanel implements KeyListener {
    private Terrain terrain;
    private int tailleCase = 36;
    private int hauteur, largeur;
    private JFrame frame;

    JProgressBar hp = new JProgressBar();

    private long tempsDepart;
    JLabel clefN;
    JLabel timerN;
    //JButton exit;



    public FenetreJeu(Terrain t) {
        this.hauteur = t.getHauteur();
        this.largeur = t.getLargeur();
        this.terrain = t;

        /*exit = new JButton("Exit");
        exit.setBounds(tailleCase*13, 2*tailleCase, 180, 9*tailleCase);
        exit.addActionListener(this);
        exit.setBorderPainted(false);
        exit.setVisible(true);*/


        hp.setMaximum(terrain.getJoueur().getResistance());
        hp.setMinimum(0);
        hp.setForeground(new Color(255, 0, 0));
        hp.setBounds(0, 0, largeur*tailleCase-233,2*tailleCase );
        //hp.setStringPainted(true);

        JLabel coeur = new JLabel();
        coeur.setIcon(new ImageIcon(new ImageIcon("src/projetsimple/hp.png").getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT)));
        coeur.setBounds( largeur*tailleCase/2 - 150 , 7, 60, 60);


        JLabel clef = new JLabel();
        clef.setIcon(new ImageIcon(new ImageIcon("src/projetsimple/key.png").getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT)));
        clef.setBounds( 36 , tailleCase-9, 60, 60);

        clefN = new JLabel();
        clefN.setText(""+terrain.getJoueur().getCles());
        clefN.setBounds( 80 , tailleCase-10, 60, 60);

        JLabel timer = new JLabel();
        timer.setIcon(new ImageIcon(new ImageIcon("src/projetsimple/tictac.png").getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT)));
        timer.setBounds( 22 , 3*(tailleCase-28), 60, 60);

        timerN = new JLabel();
        timerN.setText(""+terrain.dureePartie);
        timerN.setBounds( 62 , 3*(tailleCase-29), 60, 60);

        JLabel classementOverlay = new JLabel();
        classementOverlay.setText(EnregistrementUtilisateur.ClassementAFFICHAGE(3));
        classementOverlay.setBounds(6, 0, 120, 120);

        JPanel fondGreen = new JPanel();
        fondGreen.setBackground(Color.GREEN);
        fondGreen.setBounds(0, 8*tailleCase, 180-tailleCase, 3*tailleCase);
        fondGreen.setLayout(null);

        JPanel fondYellow = new JPanel();
        fondYellow.setBackground(Color.YELLOW);
        fondYellow.setBounds(0, 5*tailleCase, 180-tailleCase, 3*tailleCase);
        fondYellow.setLayout(null);


        JPanel fondBleu = new JPanel(); // contient
        fondBleu.setBackground(Color.BLUE);
        fondBleu.setBounds(0, 2*tailleCase, 180-tailleCase, 3*tailleCase);
        fondBleu.setLayout(null);


        // Ajout de la barre de progression au panneau
        this.setLayout(null);

        this.add(hp);
        hp.add(coeur);
        this.add(fondBleu);
        this.add(fondYellow);
        this.add(fondGreen);
        fondBleu.add(clef);
        fondBleu.add(clefN);
        fondYellow.add(timer);
        fondYellow.add(timerN);
        fondGreen.add(classementOverlay);


        this.frame = new JFrame("Furfeux");
        setBackground(Color.BLACK); // j'ai changé ct light gray avant
        setPreferredSize(new Dimension(largeur*tailleCase-233 , hauteur*tailleCase-45));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.addKeyListener(this);
        frame.setLayout(null);

        frame.setIconImage((new ImageIcon("src/projetsimple/logoIconAppli.jpg")).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));// logo de mon menu et du jeu
        frame.setTitle("ESSAYE DE T'ECHAPPER DU MANOIR !");

        tempsDepart = System.currentTimeMillis()/1000;
        //this.add(exit);



        // FIN DE LA FRAME

    }

    public Terrain getTerrain() {
        return terrain;
    }



    public void actualiseScore(Graphics g){
        long temps_actuel =  System.currentTimeMillis()/1000;
        hp.setValue(terrain.getJoueur().getResistance()); // pour mettre a jour la barre d'hp selon la vie restante du joueur
        clefN.setText(""+terrain.getJoueur().getCles()); // pareil sauf que c'est pour les clefs
        timerN.setText(""+(System.currentTimeMillis()/1000-tempsDepart)/60+"mn"+(System.currentTimeMillis()/1000-tempsDepart)%60+"sec"); // j'ai choisi de mesurer le temps de la partie a l'aide du temps en milliseconde ecoulé depuis le 1er janvier 1970

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintMur(g);
        this.paintHall(g);
        this.paintPorte(g);
        this.paintPersonnage(g);
        this.paintSortie(g);
        this.actualiseScore(g);
    }
    public void paintHall(Graphics g){
        for(int x = 0; x < hauteur; x++){
            for(int y = 0; y < largeur; y++) {
                if (terrain.getCarte()[x][y] instanceof Hall && terrain.visible(terrain.getJoueur().getC(), x, y)) {
                    if(((Hall)terrain.getCarte()[x][y]).clef) {
                        g.setColor(new Color(255, 255 - (23 * ((Hall) terrain.getCarte()[x][y]).chaleur), 255 - (23 * ((Hall) terrain.getCarte()[x][y]).chaleur))); // le minimum de chaleur sera a 2 et le max bien a 255, 2 est comme du blanc
                        g.fillRect(y * tailleCase - translation().get(1)*tailleCase, x * tailleCase  - translation().get(0)*tailleCase, tailleCase, tailleCase);
                        g.setColor(new Color(128, 128, 128));
                        g.drawImage(new ImageIcon("src/projetsimple/key.png").getImage(),y * tailleCase + 10 - translation().get(1)*tailleCase , x * tailleCase + 10 - translation().get(0)*tailleCase, tailleCase/2 , tailleCase/2 , null);
                        //AFFICHER LA CLEF
                    }else{
                        g.setColor(new Color(255, 255 - (23 * ((Hall) terrain.getCarte()[x][y]).chaleur), 255 - (23 * ((Hall) terrain.getCarte()[x][y]).chaleur))); // le minimum de chaleur sera a 2 et le max bien a 255, 2 est comme du blanc
                        g.fillRect(y * tailleCase- translation().get(1)*tailleCase, x * tailleCase- translation().get(0)*tailleCase, tailleCase, tailleCase);
                    }
                }
            }
        }
    }









    public void paintMur(Graphics g){
        //g.setColor(new Color(0, 0, 0));
        Image tmp = new ImageIcon("src/projetsimple/wallBlock.png").getImage();
        for(int x = 0; x < hauteur; x++){
            for(int y = 0; y < largeur; y++) {
                if (terrain.getCarte()[x][y] instanceof Mur && terrain.visible(terrain.getJoueur().getC(), x, y)) {
                    g.drawImage(tmp,y * tailleCase - translation().get(1)*tailleCase, x * tailleCase- translation().get(0)*tailleCase, tailleCase, tailleCase, null);
                }
            }
        }
    }
    public void paintPersonnage(Graphics g) {
        switch (terrain.getJoueur().getDirection()) { // Pour dynamiser la perspective du joueur, l'affichage du joueur dependra de la direction dans la lequel regarde le joueur
            case nord :  g.drawImage(new ImageIcon("src/projetsimple/persoNord.png").getImage(),terrain.getJoueur().getC().col * tailleCase - translation().get(1)*tailleCase, terrain.getJoueur().getC().lig * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase,  null);break;
            case est : g.drawImage(new ImageIcon("src/projetsimple/persoEst.png").getImage(),terrain.getJoueur().getC().col * tailleCase - translation().get(1)*tailleCase, terrain.getJoueur().getC().lig * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase,  null);break;
            case ouest : g.drawImage(new ImageIcon("src/projetsimple/persoOuest.png").getImage(),terrain.getJoueur().getC().col * tailleCase - translation().get(1)*tailleCase, terrain.getJoueur().getC().lig * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase,  null);break;
            case sud : g.drawImage(new ImageIcon("src/projetsimple/persoSud.png").getImage(),terrain.getJoueur().getC().col * tailleCase - translation().get(1)*tailleCase, terrain.getJoueur().getC().lig * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase,  null);break;
            //g.setColor(new Color(26, 72, 3));
            // g.drawImage(new ImageIcon("src/projetsimple/perso.png").getImage(), terrain.getJoueur().getC().col * tailleCase /*- translation().get(1)*tailleCase*/, terrain.getJoueur().getC().lig * tailleCase/* - translation().get(0)*tailleCase*/, tailleCase, tailleCase, null);
            //g.fillRect(terrain.getJoueur().getC().col * tailleCase , terrain.getJoueur().getC().lig * tailleCase, tailleCase, tailleCase);
        }
    }

    public void paintPorte(Graphics g) { // cette fonction suit la meme logique que les autres elle est longue car j'ai decider d'implanter des images selon l'etat de la porte et j'ai choisi de montrer la chaleur de la case
        Image porteOuverteV = new ImageIcon("src/projetsimple/doorOpenedV.png").getImage();
        Image porteFermeV = new ImageIcon("src/projetsimple/doorClosedV.png").getImage();
        Image porteOuverteH = new ImageIcon("src/projetsimple/doorOpenedH.png").getImage();
        Image porteFermeH = new ImageIcon("src/projetsimple/doorClosedH.png").getImage();
        for (int x = 0; x < hauteur; x++) {
            for (int y = 0; y < largeur; y++) {
                if (terrain.getCarte()[x][y] instanceof Porte && terrain.visible(terrain.getJoueur().getC(), x, y)) {
                    if(terrain.getCarte()[x][y-1] instanceof Mur && terrain.getCarte()[x][y+1] instanceof Mur) { // SAVOIR SI C'est une porte pres d'un mur sur la vertical ou sur l'horizental
                        if (!((Porte) terrain.getCarte()[x][y]).ouverte) {
                            g.setColor(new Color(255, 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur), 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur))); // le minimum de chaleur sera a 2 et le max bien a 255, 2 est comme du blanc
                            g.fillRect(y * tailleCase - translation().get(1)*tailleCase, x * tailleCase  - translation().get(0)*tailleCase, tailleCase, tailleCase);
                            g.drawImage(porteFermeH, y * tailleCase - translation().get(1)*tailleCase, x * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase, null);
                        } else {
                            g.setColor(new Color(255, 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur), 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur))); // le minimum de chaleur sera a 2 et le max bien a 255, 2 est comme du blanc
                            g.fillRect(y * tailleCase - translation().get(1)*tailleCase, x * tailleCase  - translation().get(0)*tailleCase, tailleCase, tailleCase);
                            g.drawImage(porteOuverteH, y * tailleCase - translation().get(1)*tailleCase, x * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase, null);
                        }
                    }else{ // PAREIL
                        if (!((Porte) terrain.getCarte()[x][y]).ouverte) {
                            g.setColor(new Color(255, 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur), 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur))); // le minimum de chaleur sera a 2 et le max bien a 255, 2 est comme du blanc
                            g.fillRect(y * tailleCase - translation().get(1)*tailleCase, x * tailleCase  - translation().get(0)*tailleCase, tailleCase, tailleCase);
                            g.drawImage(porteFermeV, y * tailleCase - translation().get(1)*tailleCase, x * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase, null);
                        } else {
                            g.setColor(new Color(255, 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur), 255 - (23 * ((Porte) terrain.getCarte()[x][y]).chaleur))); // le minimum de chaleur sera a 2 et le max bien a 255, 2 est comme du blanc
                            g.fillRect(y * tailleCase - translation().get(1)*tailleCase, x * tailleCase  - translation().get(0)*tailleCase, tailleCase, tailleCase);
                            g.drawImage(porteOuverteV, y * tailleCase - translation().get(1)*tailleCase, x * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase, null);
                        }
                    }
                }
            }
        }
    }



    public void paintSortie(Graphics g) {
        Image tmp = new ImageIcon("src/projetsimple/Sortie.png").getImage();
        for (int x = 0; x < hauteur; x++) {
            for (int y = 0; y < largeur; y++) {
                if (terrain.getCarte()[x][y] instanceof Sortie && terrain.visible(terrain.getJoueur().getC(), x, y)) {
                    g.setColor(new Color(255, 255 - (23 * ((Sortie) terrain.getCarte()[x][y]).chaleur), 255 - (23 * ((Sortie) terrain.getCarte()[x][y]).chaleur))); // le minimum de chaleur sera a 2 et le max bien a 255, 2 est comme du blanc
                    g.fillRect(y * tailleCase - translation().get(1)*tailleCase, x * tailleCase  - translation().get(0)*tailleCase, tailleCase, tailleCase);
                    g.drawImage(tmp,y * tailleCase - translation().get(1)*tailleCase, x * tailleCase - translation().get(0)*tailleCase, tailleCase, tailleCase, null);

                }
            }
        }
    }

    public void keyPressed(KeyEvent e){
        // Gérez l'événement lorsque la touche est enfoncée
        Son z = new Son("src/projetsimple/son/singlestep.wav");
        int keyCode = e.getKeyCode();
        int x = ((Case) terrain.getJoueur().getC()).lig; // pour eviter d'ecrire des lignes trop longues
        int y = ((Case) terrain.getJoueur().getC()).col;
        switch (keyCode) {
            case KeyEvent.VK_UP:
                // Code pour déplacer le joueur vers le haut
                ((Joueur) terrain.getJoueur()).bouge((Case) terrain.getCarte()[x-1][y]);
                ((Joueur) terrain.getJoueur()).setDirection(Direction.nord); // j'ai rajouté un system de direction pour ameliorer l'experience du joueur en voyant son personnage changeait de prof selon la touche directionnelle
                break;

            case KeyEvent.VK_DOWN:
                // Code pour déplacer le joueur vers le bas

                ((Joueur) terrain.getJoueur()).bouge((Case) terrain.getCarte()[x+1][y]);
                ((Joueur) terrain.getJoueur()).setDirection(Direction.sud);
                break;

            case KeyEvent.VK_LEFT:
                // Code pour déplacer le joueur vers la gauche

                ((Joueur) terrain.getJoueur()).bouge((Case) terrain.getCarte()[x][y-1]);
                ((Joueur) terrain.getJoueur()).setDirection(Direction.ouest);
                break;

            case KeyEvent.VK_RIGHT:
                // Code pour déplacer le joueur vers la droite

                ((Joueur) terrain.getJoueur()).bouge((Case) terrain.getCarte()[x][y+1]);
                ((Joueur) terrain.getJoueur()).setDirection(Direction.est);
                break;
        }

        z.jouer(false); //Pour arreter la soundboard du step
        this.repaint ();


    }

    public void keyTyped(KeyEvent e) {};
    public void keyReleased(KeyEvent e){};


    public void ecranFinal(long n, String nom) { // je l'ai legerement amelioré, j'ai changé la maniere de calculer le score, il se fera en fonction du temps, des hp et du nombre de porte ouverte
        long dispose = System.currentTimeMillis() / 1000;
        terrain.dureePartie = System.currentTimeMillis() / 1000 - tempsDepart; // j'enregistre la duree de la partie
        frame.remove(this);
        JLabel label;
        if (n > EnregistrementUtilisateur.max(EnregistrementUtilisateur.lireUtilisateurs()).score) {
            label = new JLabel("<html> Felicitation " + nom + " vous avez battu le <br> précedent record(" + EnregistrementUtilisateur.max(EnregistrementUtilisateur.lireUtilisateurs()).score + "), votre score est de " + n + ", votre partie a duree " + terrain.dureePartie / 60 + "mn" + terrain.dureePartie % 60 + "sec"+ "</html>"); // meme explication que dans EnregistrementUtilisateur

        } else {
            label = new JLabel("<html> " +nom + " votre score est de " +  n + "<br>"  +", votre partie a duree " + terrain.dureePartie / 60 + "mn" + terrain.dureePartie % 60 + "sec"+ "</html>");
        }
            label.setFont(new Font("Verdana", 1, 20));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setSize(this.getSize());
            frame.getContentPane().add(label);
            frame.repaint();


        }



    public ArrayList<Integer> translation(){ // j'ai fait cela pour centrer le jeu sur le joueur dans la zone de la fenetre que je souhaite
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        tmp.add(this.terrain.getJoueur().getC().lig - 6); //3 a faire sauter
        tmp.add(this.terrain.getJoueur().getC().col - 10);
        return tmp;
    }

}