package bonus;




import projetsimple.CompteARebours;
import projetsimple.FenetreJeu;
import projetsimple.Furfeux;
import projetsimple.Terrain;

import javax.swing.*;
import java.awt.*;
import java.io.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EnregistrementUtilisateur extends JFrame implements ActionListener{

    static public JTextField textField;

     static public String nom;

    static int TailleX = Menu.tailleX;
    static int TailleY = Menu.tailleY;
    JButton button;





    EnregistrementUtilisateur() {

        setSize(TailleX, TailleY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);


        JPanel panel = new JPanel();
        panel.setBounds(0, 0, TailleX, TailleY);
        panel.setLayout(null);

        JLabel nom = new JLabel("Nom : ");
        nom.setBounds(TailleX / 2 - 160, 0, 60, 60);

        textField = new JTextField(20);
        textField.setBounds(TailleX / 2 - 120, 16, 200, 30);



        button = new JButton("Enregistrer");
        button.addActionListener(this);
        button.setBorderPainted(false);
        button.setBounds(TailleX / 2 - 123, 50, 200, 30);

        JLabel classement = new JLabel("CLASSEMENT");
        classement.setBounds(TailleX / 2 - 60, 90, 200, 30);

        JLabel NmeilleursJoueurs = new JLabel("DES MEILLEURS JOUEURS");
        NmeilleursJoueurs.setBounds(TailleX / 2 - 90, 110, 200, 30);

        JLabel Liste = new JLabel(EnregistrementUtilisateur.ClassementAFFICHAGE(15));
        Liste.setBounds(275, 10, TailleX, TailleY);


        panel.add(textField);
        panel.add(button);
        panel.add(nom);
        panel.add(classement);
        panel.add(NmeilleursJoueurs);
        panel.add(Liste);

        getContentPane().add(panel);
        setSize(TailleX, TailleY);
        setVisible(true);
        setResizable(false);
        setIconImage((new ImageIcon("src/projetsimple/logoIconAppli.jpg")).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));// logo de mon menu et du jeu
        setTitle("ESSAYE DE T'ECHAPPER DU MANOIR !");
        // FIN DE LA FRAME
    }

    ;

    public void actionPerformed(ActionEvent e) {
        nom = textField.getText();
        dispose();
        CompteARebours ez = new CompteARebours();
    }


    public static void enregistrerUtilisateur(String nom, long score) { // Fonction qui rajoute les nouveaux scores et les noms dans le fichier basedonnee
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/bonus/Basededonnee.txt", true))) { //ecriture dans le fichier
            writer.write(nom);
            writer.write(" ");// Je rajoute un espace
            writer.write(String.valueOf(score)); // sans cela mon score etait ecrit dans le fichier en unicode
            writer.newLine();
            writer.flush();

            System.out.println("Nom enregistré : " + nom + " votre score a été bien ajouté :" + score); // Verification dans le terminal

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Pair> getTopN(int n) { // fonction qui cherche les n premiers dans la base de données
        ArrayList<Pair> tmp = lireUtilisateurs(); // on lit le fichier
        ArrayList<Pair> rtrn = new ArrayList<>();
        int tour = 0;
        while (tour < n && tmp.size() != 0) {
            rtrn.add(max(tmp)); //je cherche le maximum puis je le supprime rien de complicado
            tmp.remove(max(tmp)); //je supprime ce maximum pour arriver au second maximum le deuxieme maximum, le seconde plus grand
            //System.out.println(tmp.size());
            tour++;
        }
        return rtrn; // renvoie un tableau avec les N premiers Pairs nom scores en fonction du score
    }

    public static ArrayList<Pair> lireUtilisateurs() { // Lit le fichier basededonnee.txt et enregistre les données dans des pairs stockés dans un tableau
        ArrayList<Pair> rtrn = new ArrayList<Pair>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/bonus/Basededonnee.txt"))) {
            String line = reader.readLine();
            while (line != null) { // tant qu'il y a une ligne dans le fichier
                // Traitement de chaque ligne lue
                String[] parts = line.split(" "); // on lit le fichier ligne par ligne et on le scinde selon le caractere espace " "
                if (parts.length == 2) {
                    String nom = parts[0]; // La 1ere partie de la ligne scindée correspond au nom
                    long score = Long.parseLong(parts[1]); // la seconde partie de la ligne le score
                    rtrn.add(new Pair(nom, score));
                }
                line = reader.readLine(); // on passe a la ligne suivante
            }


        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return rtrn;
    }

    public static String ClassementAFFICHAGE(int n) { // j'ai crée cette fonction pour creer le text que je mettrais dans un JLabel dans mon constructeur
        int tailleLigne = TailleX / 2;
        ArrayList<Pair> tmp = getTopN(n);
        String text = "<html>"; // j'ai du faire ca pour pouvoir compter le saut dans le ligne dans mon futur JLabel
        int position = 1;
        for (Pair x : tmp) {
            text += position + " - " + x.nom + (" ".repeat(tailleLigne - x.nom.length() - 4)) + " | " + x.score + "<br>";
            text += "_".repeat(TailleX / 25) + "<br>";
            position++;
        }
        text += "</html>";
        return text;
    }


    public static Pair max(ArrayList<Pair> test) { // je cherche juste le max dans une liste de pairs en fonction du score
        long max = 0;
        Pair maxPair = new Pair("", 0);
        for (Pair x : test) {
            if (x.score >= max) {
                maxPair = x;
                max = x.score;
            }
        }
        return maxPair;
    }




    public static void execute(){ // reprends globalement le code du Main de furfeux mais j'ai preferé la deplacer ici pour facilement enregistrer le resultat
        int tempo = 100;
        Furfeux jeu = new Furfeux("manoir.txt");
        FenetreJeu graphic = new FenetreJeu(jeu.getTerrain());
        Timer timer = new Timer(tempo, x -> {
            jeu.tour();
            graphic.repaint();
            if (jeu.partieFinie()) {
                long tmp = Math.max(0, jeu.getJoueur().getResistance() - graphic.getTerrain().dureePartie + graphic.getTerrain().getJoueur().getTotalClefRamasse()*5);
                graphic.ecranFinal(tmp, nom);
                ((Timer) x.getSource()).stop();
                enregistrerUtilisateur(nom, tmp); // j'enregistre le nom et le score du joueur
            }

        });
        textField.setText(""); // Efface le champ de texte après l'enregistrement
        timer.start();
    };

    public static void main(String[] args) { // Je l'ai utilisé juste pour les tests
        EnregistrementUtilisateur tmp = new EnregistrementUtilisateur();
    /*ArrayList<Pair> tmp = getTopN(10);
    for(int i = 0; i < tmp.size();i++ ){
        System.out.println(tmp.get(i));
    }*/
        // dansCeCas.lireUtilisateurs();

}}




