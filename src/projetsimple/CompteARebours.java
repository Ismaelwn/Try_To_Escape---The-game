package projetsimple;

import bonus.EnregistrementUtilisateur;
import bonus.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompteARebours extends JFrame { // La classe que j'utilise pour concretiser mon compte a rebours

    private JLabel label;
    private int count = 3; // juste pour savoir on compte jusqu'a combien
    private Timer timer;
    private Timer timer2;

    static int TailleX = Menu.tailleX;
    static int TailleY = Menu.tailleY;

    public CompteARebours() {
        setVisible(true);
        setTitle("Compte à Rebours");
        setSize(TailleX, TailleY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label = new JLabel("3", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 60));
        add(label, BorderLayout.CENTER);
        setResizable(false);
        setIconImage((new ImageIcon("src/projetsimple/logoIconAppli.jpg")).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));
        setTitle("ESSAYE DE T'ECHAPPER DU MANOIR!");

        timer = new Timer(1000, new ActionListener() { // on commence par montrer le compteARebours a l'ecran
            public void actionPerformed(ActionEvent e) {
                if (count > 0) {
                    label.setText(Integer.toString(count)); // on ecrit de suite  tous les secondes, on a 3 2 1
                    count--;
                } else {
                    label.setText("ES-TU PRÊT ?"); // une fois fini, un petit message get ready apparait puis on arrete le timer pour passer a la suite
                    ((Timer) e.getSource()).stop();
                    timer2.start(); // c'est a dire l'execution de notre jeu FURFEUX
                }
            }
        });

        timer2 = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (count == 0) { //juste pour etre sur que l'operation d'avant c'est bien passé
                    timer2.stop(); // On stop le timer
                    dispose(); // je ferme la fenetre pour donner un effet d'actualisation de fenetre, puisque je vais en ouvrir une autre juste apres avec la ligne suivante
                    EnregistrementUtilisateur.execute();
                }
            }
        });

        timer.start();

    }
}


