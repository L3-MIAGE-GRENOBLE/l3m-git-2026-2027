# Les concepts du TP

Feuille de référence pour les notions de Git qui reviennent pendant la séance. Les commandes à taper sont dans [commandes.md](commandes.md), pas ici.

---

## Concepts clés

### Identifier un commit : l'id (ou SHA)

Chaque commit a un identifiant unique — son **id**, que Git appelle aussi *SHA* — calculé à partir de son contenu. `git log --oneline` en affiche les sept premiers caractères devant chaque commit. Partout où le TP attend le nom d'une branche, vous pouvez aussi bien donner un id : `git rebase 3f9a2c1` fonctionne exactement comme `git rebase menu-du-jour`.

Contrairement à un nom de branche, un SHA ne bouge jamais : c'est la façon la plus directe de désigner un commit précis, sans avoir à créer une étiquette (`git tag`) rien que pour s'y référer une fois.

### Nommer ses commits : les Conventional Commits

Tous les commits de ce dépôt suivent la même convention d'écriture, appelée *Conventional Commits*. Un message y prend la forme `type(portée): description` :

```
feat(entrees): ajoute les carottes râpées
```

- `feat` — **le type** : la nature du changement.
- `(entrees)` — **la portée** : la partie du projet touchée. Elle est facultative.
- `ajoute les carottes râpées` — **la description** : ce que fait le commit, en une ligne, sans majuscule initiale ni point final.

Les types que vous croiserez dans ce TP :

| Type | Quand l'utiliser |
|---|---|
| `feat` | Ajoute une fonctionnalité — ici, un plat au menu. |
| `fix` | Corrige quelque chose qui ne marchait pas. |
| `build` | Touche à la mise en place du projet ou à ses outils. |
| `chore` | Tâche d'entretien, sans effet sur le menu lui-même. |

L'intérêt est immédiat : `git log --oneline` se lit d'un coup d'œil, on retrouve en un instant quand un plat est arrivé et pourquoi. Des outils savent même en déduire automatiquement un changelog ou un numéro de version — c'est d'ailleurs à quoi sert le fichier `CHANGELOG.md` que vous allez manipuler.

À l'inverse, des messages comme `wip`, `oups` ou `fix typo` ne disent rien à personne. Vous en croiserez dans ce TP, et vous les remplacerez.

### Les trois zones, et le dépôt distant

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

### Les branches distantes (`origin/...`)

Dans l'historique, les branches d'exercice portent le préfixe `origin/` : `origin/feat/entrees` désigne la branche **telle qu'elle existe sur le dépôt distant**. C'est sous cette forme que le sujet référence les branches d'exercice. Vous n'avez aucune branche à créer : elles existent déjà sur `origin`.

### Résoudre un conflit

Git ne choisit pas à votre place : il écrit les deux versions dans le fichier, séparées par des marqueurs. Les lignes ci-dessous sont décalées de deux espaces pour l'exemple ; dans vos fichiers elles commencent en début de ligne.

```
  <<<<<<< HEAD
  la version de la branche sur laquelle vous êtes
  =======
  la version de la branche que vous intégrez
  >>>>>>> origin/feat/desserts-merge
```

Vous ouvrez le fichier, vous écrivez la bonne version à la place du bloc entier, **marqueurs compris**, puis `git add <fichier>`.

Attention : ni « accepter le courant » ni « accepter l'entrant » ne donnent forcément la bonne réponse. Parfois aucun des deux côtés n'est correct.

## Trois réflexes pour la suite

1. `git status` avant toute autre commande.
2. Des commits petits, avec des messages qui disent ce qui change et pourquoi.
3. Jamais de `push --force` sur une branche partagée avec quelqu'un d'autre.

---

## Pour aller plus loin

### Fast-forward

Quand la branche d'arrivée n'a rien de nouveau, Git peut se contenter d'avancer son pointeur sans créer de commit de fusion : c'est un *fast-forward*. Ce dépôt est réglé pour ne jamais le faire, afin que chaque fusion reste visible dans l'historique.

### `git switch` plutôt que `git checkout`

`git checkout` fait plusieurs choses différentes selon comment on l'appelle : changer de branche, restaurer un fichier depuis l'index, ou se placer en HEAD détachée sur un commit précis. Cette ambiguïté est une source classique d'erreurs — `git checkout -- fichier` écrase silencieusement des modifications non enregistrées, alors que la syntaxe ressemble à un simple changement de contexte.

Depuis Git 2.23, cette commande a été scindée en deux : `git switch`, uniquement pour changer de branche (ou en créer une avec `-c`), et `git restore`, uniquement pour restaurer des fichiers. `git switch` refuse aussi de vous laisser atterrir en HEAD détachée sans que vous l'ayez demandé explicitement (`--detach`).

C'est pour cette clarté que ce TP utilise `git switch` : son nom dit exactement ce qu'elle fait, et elle ne peut pas être confondue avec une commande qui écrase des fichiers. `git checkout` reste disponible et fonctionne à l'identique, mais vous ne la croiserez pas dans ces pages.
