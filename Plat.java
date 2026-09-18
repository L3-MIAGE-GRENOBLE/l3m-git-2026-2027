/**
 * Un plat du menu.
 *
 * @param nom Le nom du plat.
 * @param categorie La catégorie du plat.
 * @param prix Le prix en centimes.
 * @param vegetarien Vrai si le plat est végétarien.
 */
public record Plat(String nom, Categorie categorie, int prix, boolean vegetarien) {}
