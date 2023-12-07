import java.util.ArrayList;

public class Deck {
    
    public static ArrayList<Carte> buildDeck(){
        ArrayList<Carte> deck;
        
        deck = new ArrayList<Carte>();
        
        String[] valeurs = { "A", "2", "3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types = {"T","CA","CO","P"};
        int k = 0;
        while(k < 6 ){
            for (int i = 0; i < types.length; i++) {
                for(int j = 0; j< valeurs.length; j++) {
                    Carte carte = new Carte(valeurs[j], types[i]);
                    deck.add(carte);
                }
            }
            k +=1;
        }

        System.out.println("Préparation du deck :");
        System.out.println(deck);

        return deck;

    }
    
}
