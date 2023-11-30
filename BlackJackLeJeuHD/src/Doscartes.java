public class Doscartes {
    String nom;
        String prix;

        Doscartes(String nom, String prix){
            this.nom = nom;
            this.prix = prix;
        }

        public String dosCartesChoisi(){
            return nom + "-" + prix;

        }

        public String getDosCartesChem(){
            return "./asset/doscartes/" + dosCartesChoisi() + ".png";
        }
}
