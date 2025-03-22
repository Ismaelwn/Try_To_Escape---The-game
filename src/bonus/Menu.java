package bonus;

import projetsimple.CompteARebours;
import projetsimple.FenetreJeu;
import projetsimple.Furfeux;
import projetsimple.Son;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;


public class Menu extends JFrame implements ActionListener {
    JButton boutonStart;
    JButton boutonFerme;

    EnregistrementUtilisateur UX;
    public static Son z = new Son("src/projetsimple/son/menu_theme.wav"); // je prefere la considerer comme une variable global comme ca je n'ai pas a a creer d'instances

    public static int tailleX = 760;
    public static int tailleY = 760;

    Menu() {
        // IMAGE DE FOND
        JLabel imageDeFond = new JLabel();
        ImageIcon png = new ImageIcon("src/projetsimple/manoir2.jpg");
        imageDeFond.setIcon(new ImageIcon(png.getImage().getScaledInstance(tailleX, tailleY, Image.SCALE_SMOOTH)));
        imageDeFond.setBounds(0, 0, tailleX, tailleY);

        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon(new ImageIcon("src/projetsimple/logo.png").getImage().getScaledInstance(1400, 1400, Image.SCALE_SMOOTH)));
        logo.setBounds(-220, -380, 1400, 1400);
        logo.setOpaque(false);

        // FIN IMAGE DE FONd


        boutonStart = new JButton("start game");
        boutonFerme = new JButton();

        boutonStart.addActionListener(this);
        boutonFerme.addActionListener(this);

        boutonStart.setBounds(tailleX / 2 - 120, tailleY / 2 + 200, 250, 40);
        boutonFerme.setBounds(tailleX - 50, 0, 40, 40);

        boutonFerme.setContentAreaFilled(false);
        boutonFerme.setBorderPainted(false);

        boutonStart.setBorderPainted(false);

        ImageIcon xr = new ImageIcon("src/projetsimple/po.png");

        boutonFerme.setIcon(new ImageIcon(xr.getImage().getScaledInstance(boutonFerme.getWidth(), boutonFerme.getHeight(), Image.SCALE_SMOOTH)));


        //PANNEAU OU IL Y AURA LIMAGE
        JPanel ImageFond = new JPanel();
        ImageFond.setBackground(Color.BLUE);
        ImageFond.setBounds(0, 0, tailleX, tailleY);
        ImageFond.setLayout(null);
        ImageFond.add(boutonStart);
        ImageFond.add(boutonFerme);
        ImageFond.add(logo);
        ImageFond.add(imageDeFond);


        //FIN PANNEAU OU IL Y AURA LIMAGE


        // LA FRAME
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(tailleX, tailleY);
        getContentPane().add(ImageFond);
        setLayout(null);
        setResizable(false);


        setIconImage((new ImageIcon("src/projetsimple/logoIconAppli.jpg")).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));// logo de mon menu et du jeu
        setTitle("ESSAYE DE T'ECHAPPER DU MANOIR !");
        // FIN DE LA FRAME

        z.jouer(true);

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonStart) {
            UX = new EnregistrementUtilisateur();
            dispose();

        }
        if (e.getSource() == boutonFerme) {
                dispose();
        }

        }
    }





