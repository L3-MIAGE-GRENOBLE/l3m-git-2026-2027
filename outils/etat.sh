#!/usr/bin/env bash
# Diagnostic de l'environnement du TP.
# A lancer depuis la racine du depot :  bash outils/etat.sh
#
# Ce script ne modifie rien. Pour chaque manque, il affiche la commande de
# correction, que vous n'avez plus qu'a recopier.

manques=0

signaler() {
  printf '  MANQUE %s\n         corrigez avec : %s\n' "$1" "$2"
  manques=$(( manques + 1 ))
}

printf '\nOu suis-je\n'
if [ -f Verificateur.java ] && [ -f Menu.java ]; then
  printf '  OK     vous etes a la racine du depot\n'
else
  printf '  MANQUE vous n etes pas a la racine du depot\n'
  printf '         placez-vous dans le dossier qui contient Verificateur.java\n'
  manques=$(( manques + 1 ))
fi

printf '\nOutils\n'
if command -v git >/dev/null 2>&1; then
  printf '  OK     git %s\n' "$(git --version | awk '{print $3}')"
else
  signaler "git est introuvable" "installez Git depuis https://git-scm.com"
fi

if command -v java >/dev/null 2>&1; then
  brute=$(java -version 2>&1 | head -1 | sed 's/.*version "\([^"]*\)".*/\1/')
  majeur=${brute%%.*}
  [ "$majeur" = "1" ] && majeur=$(printf '%s' "$brute" | cut -d. -f2)
  if [ "${majeur:-0}" -ge 17 ] 2>/dev/null; then
    printf '  OK     java %s\n' "$brute"
  else
    signaler "java $brute est trop ancien (JDK 17 minimum)" \
             "installez un JDK 17 ou superieur"
  fi
else
  signaler "java est introuvable" "installez un JDK 17 ou superieur"
fi

printf '\nConfiguration Git\n'
verifier_config() {   # verifier_config <cle> <commande de correction>
  valeur=$(git config --get "$1" || true)
  if [ -n "$valeur" ]; then
    printf '  OK     %s = %s\n' "$1" "$valeur"
  else
    signaler "$1 n est pas defini" "$2"
  fi
}
verifier_config user.name  'git config --global user.name "Prenom Nom"'
verifier_config user.email 'git config --global user.email "prenom.nom@etu.univ-grenoble-alpes.fr"'

# core.editor sert au rebase interactif de l'exercice 5. Sans lui, vous vous
# retrouverez dans Vim sans savoir en sortir.
valeur=$(git config --get core.editor || true)
if [ -n "$valeur" ]; then
  printf '  OK     core.editor = %s\n' "$valeur"
else
  signaler "core.editor n est pas defini (indispensable a l exercice 5)" \
           'git config --global core.editor "code --wait"'
fi

# merge.ff false : ce depot est regle pour que chaque fusion laisse une trace
# visible dans l historique, comme le font beaucoup d equipes.
valeur=$(git config --get merge.ff || true)
if [ "$valeur" = "false" ]; then
  printf '  OK     merge.ff = false\n'
else
  signaler "merge.ff n est pas a false" "git config merge.ff false"
fi

printf '\nBranches d exercice\n'
absentes=0
for b in feat/entrees feat/desserts-merge feat/desserts-rebase etapes \
         tarifs-base feat/tarifs feat/vegetarien; do
  if ! git rev-parse --verify --quiet "origin/$b" >/dev/null; then
    printf '  MANQUE origin/%s\n' "$b"
    absentes=$(( absentes + 1 ))
  fi
done
if [ "$absentes" -eq 0 ]; then
  printf '  OK     les sept branches d exercice sont presentes\n'
else
  printf '\n         %s branche(s) manquante(s). Votre fork a ete cree sans les\n' "$absentes"
  printf '         branches d exercice, et aucune commande du TP ne fonctionnera.\n'
  printf '         Refaites le fork en DECOCHANT « Copy the default branch only »,\n'
  printf '         puis clonez-le a nouveau.\n'
  manques=$(( manques + absentes ))
fi

printf '\n'
if [ "$manques" -eq 0 ]; then
  printf 'Tout est en place. Bon TP.\n'
else
  printf '%s point(s) a corriger avant de commencer.\n' "$manques"
  exit 1
fi
