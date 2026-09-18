# Les concepts du TP

Feuille de référence pour les notions de Git qui reviennent pendant la séance.
Les commandes à taper sont dans [commandes.md](commandes.md), pas ici.

---

## Les trois zones, et le dépôt distant

```
  répertoire de travail        index              dépôt local          dépôt distant
  (vos fichiers)               (git add)          (git commit)         (origin)

        |------- git add ------->|
                                 |--- git commit -->|
                                                    |---- git push --->|
        |<------------ git switch / git restore ----|
                                                    |<--- git fetch ---|
```

`git status` vous dit à tout moment dans quelle zone se trouve chaque fichier.

## Identifier un commit : le SHA

Chaque commit a un identifiant unique, le SHA, calculé à partir de son contenu.
`git log --oneline` en affiche les sept premiers caractères devant chaque
commit. Partout où le TP attend le nom d'une branche ou d'un tag, vous pouvez
aussi bien donner un SHA : `git rebase 3f9a2c1` fonctionne exactement comme
`git rebase main`.

Contrairement à un nom de branche, un SHA ne bouge jamais : c'est la façon la
plus directe de désigner un commit précis, sans avoir à créer une étiquette
(`git tag`) rien que pour s'y référer une fois.

## Fast-forward

Quand la branche d'arrivée n'a rien de nouveau, Git peut se contenter d'avancer
son pointeur sans créer de commit de fusion : c'est un *fast-forward*. Ce dépôt
est réglé pour ne jamais le faire, afin que chaque fusion reste visible dans
l'historique.

## Les branches distantes (`origin/...`)

Dans l'historique, les branches d'exercice portent le préfixe `origin/` :
`origin/feat/entrees` désigne la branche **telle qu'elle existe sur le dépôt
distant**. C'est sous cette forme que le sujet référence les branches
d'exercice. Vous n'avez aucune branche à créer : elles existent déjà sur
`origin`.

## Résoudre un conflit

Git ne choisit pas à votre place : il écrit les deux versions dans le fichier,
séparées par des marqueurs. Les lignes ci-dessous sont décalées de deux espaces
pour l'exemple ; dans vos fichiers elles commencent en début de ligne.

```
  <<<<<<< HEAD
  la version de la branche sur laquelle vous êtes
  =======
  la version de la branche que vous intégrez
  >>>>>>> origin/feat/desserts-merge
```

Vous ouvrez le fichier, vous écrivez la bonne version à la place du bloc entier,
**marqueurs compris**, puis `git add <fichier>`.

Attention : ni « accepter le courant » ni « accepter l'entrant » ne donnent
forcément la bonne réponse. Parfois aucun des deux côtés n'est correct.

## Lire un résultat du vérificateur

```
  ECHEC  total du menu                    1240 (attendu 1335)
  |      |                                |     |
  |      |                                |     la valeur attendue
  |      |                                la valeur trouvée dans vos fichiers
  |      le contrôle concerné
  OK ou ECHEC
```

## Trois réflexes pour la suite

1. `git status` avant toute autre commande, toujours.
2. Des commits petits, avec des messages qui disent ce qui change et pourquoi.
3. Jamais de `push --force` sur une branche partagée avec quelqu'un d'autre.
