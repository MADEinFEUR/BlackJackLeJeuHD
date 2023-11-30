public class Carte {
    String valeur;
    String type;
        

        Carte(String valeur, String type){
            this.valeur = valeur;
            this.type = type;
            
        }

        public String toString() {              // pour convertir le nom de la carte par sa valeur - type comme enregistré dans le dossier carte.
            return valeur + "-" + type;

        }

        public int getValue(){
            if ("AJQK".contains(valeur)){ // A J Q K
                if (valeur == "A") {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(valeur); //2 -> 10
        }
        public boolean estunAs(){
            return valeur == "A";
        }

        public String getImageChem(){
            return "./asset/cartes/" + toString() + ".png";
        }
    
}
