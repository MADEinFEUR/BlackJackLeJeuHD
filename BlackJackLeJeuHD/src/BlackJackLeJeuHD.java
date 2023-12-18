import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;


import netscape.javascript.JSException;

public class BlackJackLeJeuHD {  

    ArrayList<Carte> deck;
    Random random = new Random(); // mélanger le deck

    //_______________curseur_____________
    
    Image cursorImage = new ImageIcon(getClass().getResource("./asset/curseur.png")).getImage();
    Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "custom_cursor");

    
    //___________Constante_______________
    private int numero_dos_carte = 0;
    private int largCarte = 210;
    private int hautCarte = 300;
    private int fenlarg = 1920;
    private int fenHaut = 1080;


//_____________Variable globale__________
    private int debutPartie = 0;
    private int valeurMise =  0;
    private int argentJoueur = 1000;

    private String dosCarteInit = "base";

    private String[] listedoscarte = {"base","foret","turbulax","furaxxx",
        "grignotage","rouleboule","trifouille","gribouille","gerbotron",
        "optimus_dos_de_carte","watermark","blitzkrieg","doucement_sur_la_zaza",
        "le_mousseux_de_guise","minecraft","hotwheels","malifood","la_jar_de_kader","speed_jack","power_rangers","metal_fusion"};
    private int dlc_anime = 1;

    private Image carteImg = new ImageIcon(getClass().getResource("/asset/doscartes/"+ dosCarteInit  +".png")).getImage();
    private Image carteImgjoueur = new ImageIcon(getClass().getResource("/asset/doscartes/"+ dosCarteInit  +".png")).getImage();



    //dealer
    Carte hiddenCarte;
    ArrayList<Carte> dealerhand;
    int dealersomme;
    int dealerNbAs;

    //Joueur
    ArrayList<Carte> mainjoueur;
    int joueurSomme;
    int joueurNbAs;


        public void gamewindow(){
            
            //fenêtre
            JFrame fene = new JFrame("BlackJack Le Jeu HD Casino édition ");
            fene.setVisible(true);
            fene.setSize(fenlarg, fenHaut);
            fene.setLocationRelativeTo(null);
            fene.setResizable(false);
            fene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fene.setCursor(customCursor);
            
            
            //Image & icon
            Icon optIcon = new ImageIcon(getClass().getResource("./asset/option_petit.png"), "Option");
            Icon menuIcon = new ImageIcon(getClass().getResource("./asset/quitter_petit.png"), "Menu principal");
            Icon deco1 = new ImageIcon(getClass().getResource("./asset/deco1.png"));
            Icon jetonIcon = new ImageIcon(getClass().getResource("./asset/jeton_petit.png"));
            Icon argentIcon = new ImageIcon(getClass().getResource("./asset/argent_petit.png"));
            

            //bouton & panel
            JPanel bouttonPanel = new JPanel();

            JButton hitBoutton = new JButton("Pioche");
            JButton stayboutton = new JButton("Check");
            JButton replayboutton = new JButton("Rejouer");
            JButton Playboutton = new JButton("LANCER LA PARTIE!");

            JPanel panelgauche1 = new JPanel();
            BoxLayout box = new BoxLayout(panelgauche1, 1);         

            JButton deco1Button = new JButton(deco1);
            JButton miser = new JButton("Miser 10 sous");
            JButton retourmenu = new JButton(menuIcon);
            JButton optioButton = new JButton(optIcon);
            

            //Jlabel
            JLabel miseJoueur = new JLabel("" + valeurMise, jetonIcon, 0);
            JLabel ArgentJoueur = new JLabel("" + argentJoueur, argentIcon, 0);

            deco1Button.setEnabled(false);
            miser.setFocusable(false);
            hitBoutton.setFocusable(false);
            bouttonPanel.add(hitBoutton);
            stayboutton.setFocusable(false);
            bouttonPanel.add(stayboutton);
            replayboutton.setFocusable(false);
            replayboutton.setEnabled(false);
            bouttonPanel.add(replayboutton); 
            
            Playboutton.setEnabled(true);
            
        
            //gamepanel
            JPanel gamPanel = new JPanel(){
                @Override
                
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    try {                
                        Image BG_DZ = new ImageIcon(getClass().getResource("./asset/fond_avant_partie.png")).getImage();
                        g.drawImage(BG_DZ, 0, 0, null);
                        if(!Playboutton.isEnabled()){
                            BG_DZ = new ImageIcon(getClass().getResource("./asset/fond.png")).getImage();
                            g.drawImage(BG_DZ, 0, 0, null);

                        }

                        
                        
                        
                        
                        //carte face cachée
                        Image carteFaceCacheImg = new ImageIcon(getClass().getResource("/asset/doscartes/"+ dosCarteInit  +".png")).getImage();
                        if (!stayboutton.isEnabled()){
                            carteFaceCacheImg = new ImageIcon(getClass().getResource(hiddenCarte.getImageChem())).getImage();
                        }
                        g.drawImage(carteFaceCacheImg, largCarte + 140, 20, largCarte, hautCarte, null);
 
                                              
    
                        //carte du dealer
                        for (int i = 0; i < dealerhand.size();i++){
                            Carte carte = dealerhand.get(i);
                            carteImg = new ImageIcon(getClass().getResource("/asset/doscartes/"+ dosCarteInit  +".png")).getImage();
                            if(!Playboutton.isEnabled()){
                                carteImg = new ImageIcon(getClass().getResource(carte.getImageChem())).getImage();
                            }
                            g.drawImage(carteImg, largCarte + 360 + (largCarte + 5)*i, 20, largCarte, hautCarte, null);
                        };
                        
                        //carte joueur
                        for (int i = 0; i < mainjoueur.size();i++){
                            Carte carte = mainjoueur.get(i);
                            carteImgjoueur = new ImageIcon(getClass().getResource("/asset/doscartes/"+ dosCarteInit  +".png")).getImage();
                            if(!Playboutton.isEnabled()){
                                carteImgjoueur = new ImageIcon(getClass().getResource(carte.getImageChem())).getImage();
                            }                            
                            g.drawImage(carteImgjoueur, 350 + (largCarte + 5)*i, 625, largCarte, hautCarte, null);
                        };

                        if (!stayboutton.isEnabled()){
                            replayboutton.setEnabled(true);
                            dealersomme = reductionAsDealer();
                            joueurSomme = reductionAsJoueur();
                            System.out.println("Stay");
                            System.out.println(dealersomme);
                            System.out.println(joueurSomme);
                        if(replayboutton.isEnabled()){
                            stayboutton.setEnabled(false);
                        }

                        if(!Playboutton.isEnabled()){
                            replayboutton.setEnabled(true);
                            hitBoutton.setEnabled(true);
                            stayboutton.setEnabled(true);
                            miser.setEnabled(false);  
                        }

                            String message = "";
                            if (joueurSomme > 21 ) {
                                message = " PERDU LOOSER";
                                CompteurVictoire_defaite(1);
                                g.setColor(Color.RED);
                                argentJoueur = argentJoueur - Integer.parseInt(miseJoueur.getText());
                                miseJoueur.setText("0");

                            }
                            else if (dealersomme > 21) {
                                message = "GAGNE GAGNANT";
                                CompteurVictoire_defaite(0);
                                g.setColor(Color.ORANGE);
                                argentJoueur = argentJoueur + 2*Integer.parseInt(miseJoueur.getText());
                                ArgentJoueur.setText("" + (Integer.parseInt(ArgentJoueur.getText()) + 2* Integer.parseInt(miseJoueur.getText())));
                                miseJoueur.setText("0");

                            }
                            else if (joueurSomme == dealersomme){
                                message = " EGALITE DE LOOSER";
                                CompteurVictoire_defaite(1);
                                g.setColor(Color.WHITE);
                                argentJoueur = argentJoueur + Integer.parseInt(miseJoueur.getText());
                                ArgentJoueur.setText("" + (int)(Integer.parseInt(ArgentJoueur.getText()) + Integer.parseInt(miseJoueur.getText())));
                                miseJoueur.setText("0");
                                
                            }
                            else if (joueurSomme > dealersomme){
                                message = "GAGNE GAGNANT";
                                CompteurVictoire_defaite(0);
                                g.setColor(Color.ORANGE);
                                argentJoueur = argentJoueur + 2*Integer.parseInt(miseJoueur.getText());
                                ArgentJoueur.setText("" + (int)(Integer.parseInt(ArgentJoueur.getText()) + 2* Integer.parseInt(miseJoueur.getText())));
                                miseJoueur.setText("0");
                            }
                            else if ( joueurSomme < dealersomme) {
                                message = "PERDU LOOSER";
                                CompteurVictoire_defaite(1);
                                g.setColor(Color.RED);
                                argentJoueur = argentJoueur - Integer.parseInt(miseJoueur.getText());
                                miseJoueur.setText("0");
                            }
                            

                            g.setFont(new Font("Arial", Font.PLAIN, 50));
                            g.drawString(message, 500, 500);
                            
                            



                        }
    
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
    
            };
            
            
            
    
    
    
    
            //affichage
            
            panelgauche1.setLayout(box);
            panelgauche1.add(retourmenu);
            panelgauche1.add(optioButton);
            panelgauche1.add(deco1Button);
            panelgauche1.add(miser);
            panelgauche1.add(miseJoueur);
            panelgauche1.add(ArgentJoueur);
            panelgauche1.add(Playboutton); 

            fene.add(panelgauche1, BorderLayout.WEST);
            fene.add(gamPanel);
            fene.add(bouttonPanel, BorderLayout.SOUTH);

            

            hitBoutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if (!Playboutton.isEnabled()){
                        Carte carte = deck.remove(deck.size()-1);
                        joueurSomme += carte.getValue();
                        joueurNbAs += carte.estunAs()? 1 : 0;
                        mainjoueur.add(carte);
                        if (reductionAsJoueur() > 21) {
                            hitBoutton.setEnabled(false);;
                        }
                        gamPanel.repaint();
                    }
            }});

            stayboutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!Playboutton.isEnabled()){
                        hitBoutton.setEnabled(false);
                        stayboutton.setEnabled(false);

                        while (dealersomme < 17) {
                            Carte carte = deck.remove(deck.size()-1);
                            dealersomme += carte.getValue();
                            dealerNbAs += carte.estunAs()? 1 : 0;
                            dealerhand.add(carte);
                        }
                        gamPanel.repaint();
                    }
                
                }
                
            });


            Playboutton.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                
                Playboutton.setEnabled(false);
                gamPanel.repaint();
                miser.setEnabled(false);
                
            }});
            
            

            replayboutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if(!Playboutton.isEnabled()){
                        startGame();
                        stayboutton.setEnabled(true);
                        hitBoutton.setEnabled(true);
                        replayboutton.setEnabled(false);
                        gamPanel.repaint();
                        carteImg = new ImageIcon(getClass().getResource("/asset/doscartes/"+ dosCarteInit  +".png")).getImage();

                    }
                    Playboutton.setEnabled(true);
                    miser.setEnabled(true);
                    gamPanel.repaint();
                    
                }
            });

            retourmenu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    MenuValid();
                    fene.dispose();

                    
                }
            });

            optioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    MenuOption();
                    System.out.println(dosCarteInit);
                    System.out.println(JFrameManager.getCount());
                    
                    
                    
                }
            });

            miser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if(Integer.parseInt(ArgentJoueur.getText()) == 0){
                        miser.setEnabled(false);
                    }
                    if(hitBoutton.isEnabled() != true){
                        miser.setEnabled(false);
                    }
                    if(hitBoutton.isEnabled()== true){
                        miser.setEnabled(true);
                    }
                    if (Integer.parseInt(ArgentJoueur.getText()) != 0){
                        miser.setEnabled(true);
                        miseJoueur.setText("" + (Integer.parseInt(miseJoueur.getText()) + 10));
                        ArgentJoueur.setText("" + (Integer.parseInt(ArgentJoueur.getText()) - 10));
                    }                
                     
                }
            });

            gamPanel.repaint();

        }
            
            
            
            
        
    BlackJackLeJeuHD() {
       MenuValid();
       

       
        
        
        
        
        
    System.out.println("execution...");
    }

    int victoire = 0;
    int defaite = 0;
    int nbPartieJoue = 0;
    

    public int GestionMise(int sousTotal, int mise, int mise_gain) {
        int sousfinal = 0;
        if (mise_gain != 1){
            sousfinal = sousTotal - mise ;
        } 
        if(mise_gain == 1){
            sousfinal = sousTotal + mise;
        }
        return sousfinal ;
    }



    public void CompteurVictoire_defaite(int e){
        if(e == 0){
            victoire += 1;
            nbPartieJoue+=1;

            System.out.println("nombre de victoire :");
            System.out.println(victoire);  
        }

        if(e == 1){
            defaite += 1;
            nbPartieJoue += 1;
            System.out.println("nombre de défaite :");
            System.out.println(defaite); 
        }
    }

//_________________________________________________MenuPrincipale____________________________________________
    public void MenuValid(){
        JFrame fene = new JFrame("BlackJackLeJeuHD");
            
        //fenêtre
        fene.setVisible(true);
        fene.setSize(fenlarg, fenHaut);
        fene.setLocationRelativeTo(null);
        fene.setResizable(false);
        fene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fene.setCursor(customCursor);

        //Icon bouton
        ImageIcon optionIcon = new ImageIcon(getClass().getResource("./asset/option_menu.png"),"Option");
        ImageIcon quitterIcon = new ImageIcon(getClass().getResource("/asset/quitter_menu.png"), "Quitter");
        ImageIcon jouerIcon = new ImageIcon(getClass().getResource("/asset/jouer_menu.png"),"Jouer");
        Icon LogoIcon = new ImageIcon(getClass().getResource("./asset/logo.png"));
        ImageIcon BoutiqueIcon = new ImageIcon(getClass().getResource("./asset/boutique.png"));
        
        //titre
        JLabel logo = new JLabel(LogoIcon);
        logo.setBounds(0,   0,  1800, 1020);

        //Option
        int xo = (int)(fenlarg - fenlarg*0.75);
        int y = (int)(fenHaut -(100 + fenHaut*0.40));

        
       // OptionBoutton.setLayout(grid);
       JButton OptionBoutton = new JButton(optionIcon);
       OptionBoutton.setFocusable(true);
       OptionBoutton.setBounds(xo, y , 310, 470);

        // Jouer
        int xp = (int)(xo + 200);
        JButton Playboutton = new JButton(jouerIcon);
        Playboutton.setBounds(xp, y, 310, 470);
        Playboutton.setFocusable(false);

        //quitter
        int xq = (int)(xp + 200);
        
        JButton Quitboutton = new JButton(quitterIcon);
        Quitboutton.setFocusable(false);
        Quitboutton.setBounds( xq, y, 310, 470);

      //boutique Bouton
        JButton boutikButton = new JButton(BoutiqueIcon);
        boutikButton.setBounds(xq + 500, y - 150, 200, 200);
        boutikButton.setFocusable(false);
        boutikButton.setEnabled(true);
        boutikButton.setBorderPainted(false);
        boutikButton.setBackground(null);

        //affichage
        fene.setBackground(Color.MAGENTA);
        fene.setLayout(null);
        fene.add(logo);
        //fene.add(titre);
        fene.add(OptionBoutton);
        fene.add(Playboutton);
        fene.add(Quitboutton);
        fene.add(boutikButton);
        
       Playboutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            Playboutton.setEnabled(false);
            Playboutton.setBackground(Color.green);
            fene.dispose();
            startGame();
            gamewindow();
        }});

        OptionBoutton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                MenuOption(); 
            }
        });

       Quitboutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            fene.dispose();
        }});
        
        boutikButton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                BoutiqueOption();
                fene.dispose();
            }
        });
    }

    //_____________________________________________________Option_______________________________________________________
    public void MenuOption() {

        System.out.println("liste dos de carte :");
        System.out.println(listedoscarte.toString());

        JFrame Optionframe = new JFrame("Menu d'options");
        Optionframe.setSize(1000, 1000);
        Optionframe.setResizable(false);
        Optionframe.setLocationRelativeTo(null);
        Optionframe.setCursor(customCursor);

        //icon
        Icon suivanIcon = new ImageIcon(getClass().getResource("./asset/suivant.png"));
        Icon retourIcon = new ImageIcon(getClass().getResource("./asset/retour.png"));
        Icon quitterIcon = new ImageIcon(getClass().getResource("/asset/quitter_petit.png"));
        Icon dosinit = new ImageIcon(getClass().getResource("./asset/doscartes/base.png"));
        Icon BG_DZ = new ImageIcon(getClass().getResource("./asset/fond_option.png"));

        //Jlabel
        JLabel BG_DZ_option = new JLabel(BG_DZ);
        BG_DZ_option.setBounds(0, 0, 1000, 1000);

        
        
        
        JLabel txt = new JLabel("Le dos de carte sera disponible à la prochaine partie !" );
        txt.setFont(new Font("Arial", Font.PLAIN, 36));
        txt.setBounds(50,700, 1000, 500);
        txt.setBackground(Color.YELLOW);
        txt.setForeground(Color.GREEN);
        txt.setVisible(false);
        
        

        //bouton
        int y_doscarte = 550;

        JButton suivantJButton = new JButton(retourIcon);
        suivantJButton.setBounds(35, y_doscarte, 80, 50);
        suivantJButton.setFocusable(false);

        JButton retourJButton = new JButton(suivanIcon);
        retourJButton.setBounds(876, y_doscarte, 80, 50);
        retourJButton.setFocusable(false);
        
        JButton quitterJButton = new JButton(quitterIcon);
        quitterJButton.setBounds(5, 5, 50, 50);
        quitterJButton.setFocusable(false);
        
        JButton dosdecartesButton = new JButton(dosinit);
        dosdecartesButton.setBounds(260, y_doscarte - 370, 480, 700);
        dosdecartesButton.setFocusable(false);

    


    quitterJButton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                JFrameManager.unregister(Optionframe);
                Optionframe.dispose();
                
            }
        });

        suivantJButton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                 if(numero_dos_carte == 0){
                    numero_dos_carte = listedoscarte.length;
                }
                numero_dos_carte -= 1;

                System.out.println("dos de carte choisi :");
                System.out.println(listedoscarte[numero_dos_carte]); 

                dosdecartesButton.setIcon(new ImageIcon(getClass().getResource("./asset/doscartes/"+ listedoscarte[numero_dos_carte]+".png")));               
                dosdecartesButton.setEnabled(true);
                txt.setVisible(false);
                

            }
        });

        retourJButton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                if(numero_dos_carte == listedoscarte.length){
                    numero_dos_carte = 0;
                }
                numero_dos_carte += 1;

                System.out.println("dos de carte choisi :");
                System.out.println(listedoscarte[numero_dos_carte]);

                dosdecartesButton.setIcon(new ImageIcon(getClass().getResource("./asset/doscartes/"+ listedoscarte[numero_dos_carte]+".png")));
                dosdecartesButton.setEnabled(true);
                txt.setVisible(false);

                
            }
        });
        
        dosdecartesButton.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                dosdecartesButton.setEnabled(false);
                dosCarteInit = listedoscarte[numero_dos_carte];
                JFrameManager.register(Optionframe);
                txt.setVisible(true);
            }
        });

       

        //fenêtre
        Optionframe.setLayout(null);
        Optionframe.setVisible(true);
        Optionframe.add(txt);
        Optionframe.add(dosdecartesButton);
        Optionframe.add(quitterJButton);
        Optionframe.add(retourJButton);
        Optionframe.add(suivantJButton);
        Optionframe.add(BG_DZ_option);

    }

    //___________________________________________Boutique___________________________________________________________

    public void BoutiqueOption(){

        //fenêtre
        JFrame boutiqueFrame = new JFrame();
       
        boutiqueFrame.setResizable(false);
        boutiqueFrame.setLocation(0,0);
        boutiqueFrame.setVisible(true);
        boutiqueFrame.setLayout(null);
        boutiqueFrame.setSize(1920,1080 );
        boutiqueFrame.setCursor(customCursor);

        //icon
        Icon achatIcon1 = new ImageIcon(getClass().getResource("./asset/boutique_1.png"));
        Icon achatIcon2 = new ImageIcon(getClass().getResource("./asset/boutique_2.png"));
        Icon achatIcon3 = new ImageIcon(getClass().getResource("./asset/boutique_3.png"));
        Icon achatIcon4 = new ImageIcon(getClass().getResource("./asset/boutique4.png"));
        Icon BG_DZ = new ImageIcon(getClass().getResource("./asset/fond_boutik.png"));
        Icon quitterIcon = new ImageIcon(getClass().getResource("./asset/quitter_petit.png"));




        //Bouton
        int xinit = 250;
        int yinit = 100;
        int largbout = 400;

        JButton Pack_5e99 = new JButton(achatIcon1);
        Pack_5e99.setFocusable(false);
        Pack_5e99.setBounds(xinit,yinit, largbout,largbout);
        Pack_5e99.setEnabled(true);

        JButton Pack_19e99 = new JButton(achatIcon2);
        Pack_19e99.setFocusable(false);
        Pack_19e99.setBounds(xinit+50+largbout,yinit, largbout,largbout);
        Pack_19e99.setEnabled(true);

        JButton Pack_99e99 = new JButton(achatIcon3);
        Pack_99e99.setFocusable(false);
        Pack_99e99.setBounds(xinit+2*largbout+100,yinit, largbout,largbout);
        Pack_99e99.setEnabled(true);

        JButton Pack_anime = new JButton(achatIcon4);
        Pack_anime.setFocusable(false);
        Pack_anime.setBounds(xinit - 75,yinit + largbout + 100, 1500,300);
        if(dlc_anime == 0){
            Pack_anime.setEnabled(false);
        }
        

        JButton quitter = new JButton(quitterIcon);
        quitter.setFocusable(false);
        quitter.setBounds(50,50, 50,50);
        quitter.setEnabled(true);




        //Texte / Jlabel
        JLabel BG_DZ_boutique = new JLabel(BG_DZ);
        BG_DZ_boutique.setBounds(0, 0, 1800, 1080);

        JLabel achat_txt = new JLabel("Sous ajoutés");
        achat_txt.setFont(new Font("Arial", Font.PLAIN, 36));
        achat_txt.setBounds(50,700, 1000, 500);
        achat_txt.setBackground(Color.YELLOW);
        achat_txt.setForeground(Color.GREEN);
        achat_txt.setVisible(false);



        Pack_5e99.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                Pack_5e99.setEnabled(false);
                achat_txt.setText("1.000 Sous ajoutés au compte");
                achat_txt.setBounds(xinit,yinit + 200, 1000, 500);
                achat_txt.setVisible(true);
                argentJoueur = argentJoueur + 1000;
            }});
        
        Pack_19e99.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                Pack_19e99.setEnabled(false);
                achat_txt.setText("10.000 Sous ajoutés au compte");
                achat_txt.setBounds(xinit+50+largbout,yinit+200, 1000, 500);
                achat_txt.setVisible(true);
                argentJoueur = argentJoueur + 10000;
                
            }});
        
        Pack_99e99.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                Pack_99e99.setEnabled(false);
                achat_txt.setText("40.000 Sous ajoutés au compte");
                achat_txt.setBounds(xinit+2*largbout+100,yinit + 200, 1000, 500);
                achat_txt.setVisible(true);
                argentJoueur = argentJoueur + 40000;
                
            }});

        Pack_anime.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                if (dlc_anime == 1){
                    Pack_anime.setEnabled(false);
                    achat_txt.setText("8.000 Sous et 4 Dos ajoutés au compte");
                    achat_txt.setBounds(xinit - 70 + 100,yinit + largbout + 200, 1000, 500);
                    achat_txt.setVisible(true);
                    argentJoueur = argentJoueur + 8000;

                    ArrayList<String> dosList = new ArrayList<>(Arrays.asList(listedoscarte));
                    dosList.add("snk");
                    dosList.add("naruto");
                    dosList.add("jojo");
                    dosList.add("hunter_x_hunter");
                    dosList.add("dragon_ball_z");
                    listedoscarte = dosList.toArray(new String[0]);

                    System.out.println("pack anime acheté, voici la nouvelle liste de dos de cartes :");
                    System.out.println(listedoscarte);

                    dlc_anime = 0;

                }

                
            }});
        
        quitter.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                MenuValid();
                boutiqueFrame.dispose();

                
            }});



        //affichage
        
        boutiqueFrame.add(Pack_5e99);
        boutiqueFrame.add(Pack_19e99);
        boutiqueFrame.add(Pack_99e99);
        boutiqueFrame.add(Pack_anime);
        boutiqueFrame.add(quitter);
        boutiqueFrame.add(achat_txt);
        boutiqueFrame.add(BG_DZ_boutique);


    }

    
    //______________________________________________JEU______________________________________________________________

    public void startGame(){
        //deck
        // Deck.buildDeck();
        deck = melangedeck(Deck.buildDeck());

        //dealer
        dealerhand = new ArrayList<Carte>();
        dealersomme = 0;
        dealerNbAs = 0;

        hiddenCarte = deck.remove(deck.size()-1); // retirer la dernière carte au dernier index
        dealersomme += hiddenCarte.getValue();
        dealerNbAs += hiddenCarte.estunAs() ? 1 : 0;
        

        Carte carte = deck.remove(deck.size()-1);
        dealersomme +=carte.getValue();
        dealerNbAs += carte.estunAs() ? 1 : 0;
        dealerhand.add(carte);


        System.out.println("Dealer");
        System.out.println(hiddenCarte);
        System.out.println(dealerhand);
        System.out.println(dealersomme);
        System.out.println(dealerNbAs);

        //Joueur
        mainjoueur = new ArrayList<Carte>();
        joueurSomme = 0;
        joueurNbAs = 0;

        for (int i = 0; i < 2; i++ ){
            carte = deck.remove(deck.size()-1);
            joueurSomme += carte.getValue();
            joueurNbAs += carte.estunAs() ?1 :0 ;
            mainjoueur.add(carte);
        }



        System.out.println("Joueur :");
        System.out.println(mainjoueur);
        System.out.println(joueurSomme);
        System.out.println(joueurNbAs);


    }
    public ArrayList<Carte> melangedeck(ArrayList<Carte> deckPasMel ){
        
        for (int i = 0; i< deckPasMel.size(); i++){
            int j = random.nextInt(deckPasMel.size());
            Carte currCarte = deckPasMel.get(i);
            Carte randomCarte = deckPasMel.get(j);
            deckPasMel.set(j, randomCarte);
            deckPasMel.set(j, currCarte);
        }

        System.out.println("Après le mélange :");
        System.out.println(deckPasMel);
        return deckPasMel;
    }
    


    public int reductionAsJoueur(){
        while (joueurSomme > 21 && joueurNbAs > 0) {
            joueurSomme -= 10;
            joueurNbAs -= 1;
        }
        return joueurSomme;
    }
    public int reductionAsDealer() {
        while (dealersomme > 21 && dealerNbAs > 0) {
            dealersomme -= 10;
            dealerNbAs -= 1;
        }
        return dealersomme ;
    }

    

}

