/**
 * Le menu du jour.
 *
 * Un plat par ligne, groupe par categorie : entrees, plats, desserts.
 * Ce formatage est volontaire : c'est lui qui rend les conflits lisibles.
 */
public final class Menu {

    /** Budget maximal du menu, en centimes. */
    public static final int BUDGET_MAX = 1650;

    public static final Plat[] PLATS = {
        new Plat("Salade de lentilles", Categorie.ENTREE, 120, true),
        new Plat("Carottes râpées", Categorie.ENTREE, 90, true),
        new Plat("Œuf mayonnaise", Categorie.ENTREE, 110, true),
        new Plat("Terrine de campagne", Categorie.ENTREE, 150, false),
        new Plat("Steak haché frites", Categorie.PLAT, 330, false),
        new Plat("Yaourt nature", Categorie.DESSERT, 80, true),
        new Plat("Tarte aux pommes", Categorie.DESSERT, 130, true),
        new Plat("Mousse au chocolat", Categorie.DESSERT, 120, true),
        new Plat("Salade de fruits", Categorie.DESSERT, 110, true),
    };

    /** Prix total du menu, en centimes. */
    public static int total() {
        int somme = 0;
        for (Plat p : PLATS) {
            somme += p.prix();
        }
        return somme;
    }

    /** Prix d'un plat, en centimes, ou -1 s'il n'est pas au menu. */
    public static int prixDe(String nom) {
        for (Plat p : PLATS) {
            if (p.nom().equals(nom)) {
                return p.prix();
            }
        }
        return -1;
    }

    /** Vrai si la categorie compte au moins un plat vegetarien. */
    public static boolean aUnVegetarienDans(Categorie categorie) {
        for (Plat p : PLATS) {
            if (p.categorie() == categorie && p.vegetarien()) {
                return true;
            }
        }
        return false;
    }
}
