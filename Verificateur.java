import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Stream;

/**
 * Programme de verification du TP.
 *
 * Ce fichier est ecrit une fois pour toutes au commit initial.
 * AUCUNE branche du TP ne le modifie : il connait deja l'etat attendu
 * a la fin de chacun des cinq exercices.
 *
 *   java Verificateur.java       etat des lieux : quel exercice est atteint
 *   java Verificateur.java 2     verifie la cible de l'exercice 2, controle par controle
 *
 * Il se lance depuis la racine du depot, sans compilation prealable
 * (lanceur mono-fichier, Java 17 et superieur).
 *
 * IMPORTANT : ce programme LIT Menu.java comme du texte, il ne l'execute pas.
 * C'est ce qui lui permet de fonctionner meme quand Menu.java contient encore
 * des marqueurs de conflit, cas ou une compilation echouerait.
 *
 * La sortie est du texte nu, sans couleur : elle reste lisible telle quelle
 * et se redirige vers un fichier sans precaution.
 */
public final class Verificateur {

    /** Cible de chaque exercice : numero, nombre de plats, total en centimes. */
    private static final int[][] CIBLES = {
        {  0,  3,  530 },
        {  1,  6,  880 },
        {  2,  9, 1240 },
        {  3,  9, 1240 },
        {  4,  9, 1335 },
        {  5, 10, 1615 },
    };

    private static final Pattern PLAT = Pattern.compile(
        "new Plat\\(\"([^\"]*)\", Categorie\\.(\\w+), (\\d+), (true|false)\\)");

    private static final Set<String> EXTENSIONS =
        Set.of(".java", ".md", ".txt", ".yml", ".yaml", ".sh");

    private static int reussis = 0;
    private static int echoues = 0;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            etatDesLieux();
        } else {
            verifierExercice(Integer.parseInt(args[0].replaceAll("[^0-9]", "")));
        }
        if (echoues > 0) System.exit(1);
    }

    private static void titre(String texte) {
        System.out.println(texte);
    }

    // ------------------------------------------------------------- exercices

    private static void verifierExercice(int exercice) throws IOException {
        int[] cible = cibleDe(exercice);
        if (cible == null) {
            System.out.println("Exercice inconnu : " + exercice + " (attendu 0 a 5)");
            System.exit(2);
        }

        titre("Verification de l'exercice " + exercice);
        System.out.println();

        titre("Aucun conflit en suspens");
        List<String> restes = fichiersEnConflit();
        if (restes.isEmpty()) {
            reussis++;
            System.out.printf("  %s %s%n", "OK    ", "aucun marqueur de conflit");
        } else {
            echoues++;
            System.out.printf("  %s %s%n", "ECHEC ", "marqueurs de conflit restants :");
            for (String f : restes) System.out.println("           " + f);
        }

        List<Plat> menu = litMenu();

        System.out.println();
        titre("Le menu");
        controle("nombre de plats", menu.size(), cible[1]);
        controle("total du menu", total(menu), cible[2]);
        controle("total sous le budget", total(menu) <= budgetMax(), true);
        controle("une entree au menu", aUneCategorie(menu, "ENTREE"), true);

        System.out.println();
        titre("L'affiche du jour correspond au menu");
        controle("nombre de plats affiche", afficheNombre(), menu.size());
        controle("total affiche", afficheTotal(), total(menu));

        if (exercice >= 4) {
            System.out.println();
            titre("Tarifs arbitres");
            controle("prix de la salade de lentilles", prixDe(menu, "Salade de lentilles"), 140);
            controle("prix du steak hache frites", prixDe(menu, "Steak hach\u00e9 frites"), 390);
            controle("prix du yaourt nature", prixDe(menu, "Yaourt nature"), 95);
        }

        if (exercice >= 5) {
            System.out.println();
            titre("Option vegetarienne");
            controle("un plat principal vegetarien", aUnVegetarienDans(menu, "PLAT"), true);
        }

        System.out.println();
        if (echoues == 0) {
            System.out.println("TOUT EST VERT : l'exercice " + exercice + " est termine.");
        } else {
            System.out.println("IL RESTE DU TRAVAIL : " + echoues
                               + " controle(s) en echec sur " + (reussis + echoues) + ".");
        }
    }

    private static void etatDesLieux() throws IOException {
        List<Plat> menu = litMenu();
        List<String> restes = fichiersEnConflit();

        titre("Etat des lieux");
        System.out.println();
        System.out.println("  menu    : " + menu.size() + " plats, " + total(menu) + " centimes");
        System.out.println("  affiche : " + afficheNombre() + " plats, "
                           + afficheTotal() + " centimes");
        System.out.println();

        if (!restes.isEmpty()) {
            System.out.println("Des marqueurs de conflit restent dans :");
            for (String f : restes) System.out.println("  " + f);
            System.out.println();
        }

        int atteint = -1;
        for (int[] c : CIBLES) {
            boolean ok = restes.isEmpty()
                      && menu.size() == c[1]
                      && total(menu) == c[2]
                      && afficheNombre() == menu.size()
                      && afficheTotal() == total(menu);
            if (ok && atteint < 0) atteint = c[0];
        }
        if (atteint < 0) {
            System.out.println("Aucune cible d'exercice n'est atteinte.");
            System.out.println("Relancez avec le numero de l'exercice en cours, "
                               + "par exemple : java Verificateur.java 2");
        } else {
            System.out.println("Cible atteinte : exercice " + atteint + ".");
            System.out.println("Pour le detail d'un exercice : java Verificateur.java " + atteint);
        }
    }

    // ---------------------------------------------------------------- lecture

    private record Plat(String nom, String categorie, int prix, boolean vegetarien) {}

    private static List<Plat> litMenu() throws IOException {
        List<Plat> plats = new ArrayList<>();
        Matcher m = PLAT.matcher(lire("Menu.java"));
        while (m.find()) {
            plats.add(new Plat(m.group(1), m.group(2),
                               Integer.parseInt(m.group(3)), Boolean.parseBoolean(m.group(4))));
        }
        return plats;
    }

    private static int total(List<Plat> menu) {
        return menu.stream().mapToInt(Plat::prix).sum();
    }

    private static int prixDe(List<Plat> menu, String nom) {
        return menu.stream().filter(p -> p.nom().equals(nom))
                   .mapToInt(Plat::prix).findFirst().orElse(-1);
    }

    private static boolean aUneCategorie(List<Plat> menu, String c) {
        return menu.stream().anyMatch(p -> p.categorie().equals(c));
    }

    private static boolean aUnVegetarienDans(List<Plat> menu, String c) {
        return menu.stream().anyMatch(p -> p.categorie().equals(c) && p.vegetarien());
    }

    private static int budgetMax() throws IOException {
        return premierEntier(lire("Menu.java"), "BUDGET_MAX\\s*=\\s*(\\d+)");
    }

    private static int afficheNombre() throws IOException {
        return premierEntier(lire("AFFICHE.md"), "Nombre de plats\\s*:\\s*(\\d+)");
    }

    private static int afficheTotal() throws IOException {
        return premierEntier(lire("AFFICHE.md"), "Total du menu\\s*:\\s*(\\d+)");
    }

    /**
     * Premiere valeur seulement : en presence de marqueurs de conflit les deux
     * cotes sont lisibles, et le controle des marqueurs signale deja le probleme.
     */
    private static int premierEntier(String texte, String motif) {
        Matcher m = Pattern.compile(motif).matcher(texte);
        return m.find() ? Integer.parseInt(m.group(1)) : -1;
    }

    private static List<String> fichiersEnConflit() throws IOException {
        List<String> trouves = new ArrayList<>();
        Path racine = Path.of(".");
        try (Stream<Path> flux = Files.walk(racine)) {
            for (Path p : flux.toList()) {
                if (!Files.isRegularFile(p)) continue;
                if (p.normalize().toString().replace('\\', '/').contains(".git/")) continue;
                String nom = p.getFileName().toString();
                int point = nom.lastIndexOf('.');
                if (point < 0 || !EXTENSIONS.contains(nom.substring(point))) continue;
                for (String ligne : lire(p.toString()).split("\n", -1)) {
                    if (ligne.startsWith("<<<<<<< ") || ligne.startsWith(">>>>>>> ")) {
                        trouves.add(racine.relativize(p).toString().replace('\\', '/'));
                        break;
                    }
                }
            }
        }
        Collections.sort(trouves);
        return trouves;
    }

    private static String lire(String chemin) throws IOException {
        Path p = Path.of(chemin);
        if (!Files.exists(p)) return "";
        return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
    }

    // ------------------------------------------------------------ utilitaires

    private static int[] cibleDe(int exercice) {
        for (int[] c : CIBLES) if (c[0] == exercice) return c;
        return null;
    }

    /**
     * Le remplissage est calcule sur le texte nu : les colonnes doivent rester
     * alignees pour qu'une ligne ECHEC se repere d'un coup d'oeil.
     */
    private static void controle(String libelle, Object obtenu, Object attendu) {
        boolean ok = obtenu.equals(attendu);
        if (ok) reussis++; else echoues++;
        String etat = String.format("%-6s", ok ? "OK" : "ECHEC");
        String nom = String.format("%-32s", libelle);
        if (ok) {
            System.out.printf("  %s %s %s%n", etat, nom, obtenu);
        } else {
            System.out.printf("  %s %s %s %s%n", etat, nom, obtenu,
                              "(attendu " + attendu + ")");
        }
    }
}
