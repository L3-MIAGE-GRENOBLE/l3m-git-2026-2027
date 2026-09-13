# TP Git • L3 MIAGE 2026-2027 — Le menu du resto U

*Le Crous de Grenoble vous a missionné pour l'aider à préparer les menus de son restaurant universitaire (le "resto U"). Suite à un problème d'organisation, les menus préparés pour aujourd'hui et demain se sont retrouvés éparpillés dans plusieurs commits sur différentes branches.*

## Objectif du TP

Votre rôle consiste à réassembler ces menus grâce à vos connaissances sur Git au travers de six exercices.

> Vous retrouverez les **commandes** utiles dans [docs/commandes.md](docs/commandes.md) ainsi que les **concepts** Git (zones, fast-forward, branches distantes...) dans [docs/concepts.md](docs/concepts.md).

## Pré-requis

Avant de commencer assurez-vous que les outils suivants soient bien installés :
- **Java** — pour lancer la commande de vérification du TP ; testez que la commande `java --version` fonctionne,
- Sur **VSCode** :
  - L'extension [**Markdown Preview Mermaid Support**](https://marketplace.visualstudio.com/items?itemName=bierner.markdown-mermaid) — pour visualiser les graphes Git au format Mermaid ; vérifiez qu'ils s'affichent bien ci-dessous.
  - *(Optionnel)* L'extension [**Git Graph**](https://marketplace.visualstudio.com/items?itemName=mhutchie.git-graph) — pour visualiser directement sous forme d'arbre l'état de votre dépôt (plutôt qu'avec `git log`).

## Installation

>⚠️ Ici je pense pas que le fork soit pas utile, plutôt il faudrait faire une template (plus simple sur la gestion des repos des étudiants) 

~~1. Récupérez le dépôt en faisant un **fork** de celui-ci (bouton en haut à droite).~~
1. Créer votre repo en utilisant le bouton `useTemplate` depuis le repo [l3m-git-2026-2027](https://github.com/L3-MIAGE-GRENOBLE/l3m-git-2026-2027)
2. Au moment de créer le repo via la template, GitHub ne copie par défaut que la branche principale. **Décochez "Copy the default branch only"** pour obtenir toutes les branches de l'exercice. Sans elles, aucune commande du TP ne fonctionne. Vous devriez ensuite avoir votre propre dépôt à cette adresse : `https://github.com/l3miage-<votre-nom>/l3m-git-2026-2027`
3. **Clonez** votre dépôt sur votre machine en local avec la commande suivante, puis ouvrez-le avec votre éditeur (VSCode ou intellij) :

```sh
git clone https://github.com/l3miage-<votre-nom>/l3m-git-2026-2027
```

Vous êtes maintenant prêt·e à démarrer !

---

## Exercice 0 — Prise en main

### Contexte

Voici un graphe présentant l'état actuel du dépôt :

```mermaid
%%{init: {'gitGraph': {'mainBranchName': 'menu-du-jour'}}}%%
gitGraph
   commit id: "build(init)"
   branch feat/entrees
   commit id: "carottes"
   commit id: "oeuf mayo"
   commit id: "terrine"
   checkout menu-du-jour
   branch feat/desserts-merge
   commit id: "tarte"
   commit id: "mousse"
   commit id: "fruits"
   checkout menu-du-jour
   branch feat/desserts-rebase
   commit id: "tarte "
   commit id: "mousse "
   commit id: "fruits "
   checkout menu-du-jour
   branch menu-de-demain
   commit id: "(entrees + desserts deja intégrés)"
   branch tarifs-base
   commit id: "revalorisation"
   checkout menu-de-demain
   branch feat/tarifs
   commit id: "prix lentilles"
   commit id: "prix steak"
   commit id: "prix yaourt"
   checkout menu-du-jour
   branch feat/vegetarien
   commit id: "tarifs arbitres deja appliques"
   commit id: "wip"
   commit id: "oups"
   commit id: "fix typo"
```

Sur la branche `menu-du-jour`, le dépôt contient un menu de trois plats ainsi qu'une affiche `AFFICHE.md` qui expose le nombre de plats et le prix du menu à des fins de vérification.

Toutes les autres branches constitue des ajouts ou des modifications de plats afin de pouvoir reconstruire le menu complet.


~~La branche `menu-de-demain` contient davantage de plats que la branche `menu-du-jour` et sera le point de départ des exercices 4 et 5.~~
~~Chaque autre branche constitue des ajouts ou des modifications de plats à faire aux branches `menu-du-jour` et `menu-de-demain` via les commandes `git merge` ou `git rebase`.~~

### Pour commencer

1. Pour vous aider à garantir la cohérence du menu, le Crous vous met à disposition un *vérificateur*. **Après chaque fusion**, celui-ci vous permettra de comparer le nombre de plats et le prix attendus à celui que vous avez actuellement, et vous indiquera également votre progression au niveau des exercices :

```sh
java Verificateur.java <step>
```

2. Le Crous vous indique également qu'il souhaite expliciter les fusions dans l'arbre des commits pour mieux suivre votre travail. Il vous faudra donc exécuter la commande suivante :

> ⚠️ à mettre peut être dans l'installation non ?   

```sh
git config merge.ff false
```

> Pour en savoir plus à ce sujet, vous pouvez consultez la section **fast-forward** du document [docs/concepts.md](docs/concepts.md).

3. Vous pouvez maintenant faire un état des lieux en **consultant l'historique** via la commande suivante (vous pouvez également utiliser [**Git Graph**](#pré-requis)):

```sh
git log --graph --oneline --all
```

Vous observerez que les branches liées aux exercices portent le préfixe `origin/` : `origin/feat/entrees` désigne la branche telle qu'elle existe sur le **dépôt distant** (c'est-à-dire ce qu'il y a sur *GitHub*). C'est sous cette forme que les exercices suivants y feront référence.

---

## Exercice 1 — Trois commits et une fusion

### Objectif

Ajouter les entrées au menu.

```mermaid
%%{init: {'gitGraph': {'mainBranchName': 'menu-du-jour'}}}%%
gitGraph
   commit id: "build(init)"
   branch feat/entrees
   commit id: "carottes"
   commit id: "oeuf mayo"
   commit id: "terrine"
   checkout menu-du-jour
   merge feat/entrees
```

### Contexte

La branche `origin/feat/entrees` ajoute trois entrées au menu, un plat par commit. Ces commits sont déjà faits : vous allez les lire, puis les intégrer à `menu-du-jour`.

### À faire

1. Lisez d'abord les trois commits, sans vous déplacer :

```sh
git log origin/feat/entrees --graph
```

2. Placez-vous sur `menu-du-jour` et fusionnez :

```sh
git switch menu-du-jour
git merge origin/feat/entrees
```

3. Lancez le vérificateur :

```sh
java Verificateur.java 1
```


> ⚠️ J'ai peur de ce saut entre le exo1 et 3, je pense que beaucoup vont oublié ... 
4. Puis notez le SHA du commit de fusion que vous venez d'obtenir ; vous en aurez besoin à l'exercice 3.

```sh
git log --oneline -1
```

Les sept premiers caractères affichés sont le SHA de votre commit de fusion. Copiez-les quelque part : c'est ainsi que vous vous y référerez, et il sera différent du SHA d'un autre commit — c'est normal, chaque commit de fusion est unique (voir [docs/concepts.md](docs/concepts.md)).

### À observer

```sh
git log --graph --oneline --all
```

Un **commit de fusion** est apparu au bout de `menu-du-jour`, et le graphe montre une branche qui part et qui revient. C'est la forme que doit avoir toute fusion de ce TP.

---

## Exercice 2 — Une fusion qui entre en conflit

### Objectif

Ajouter les desserts au menu, et découvrir qu'une fusion sans conflit peut quand même être fausse.

```mermaid
%%{init: {'gitGraph': {'mainBranchName': 'menu-du-jour'}}}%%
gitGraph
   commit id: "build(init)"
   branch feat/entrees
   commit id: "carottes"
   commit id: "oeuf mayo"
   commit id: "terrine"
   checkout menu-du-jour
   branch feat/desserts-merge
   commit id: "tarte"
   commit id: "mousse"
   commit id: "fruits"
   checkout menu-du-jour
   merge feat/entrees
   merge feat/desserts-merge
```

### Contexte

La branche `origin/feat/desserts-merge` ajoute trois desserts. Elle est partie du commit initial, en même temps que les entrées : les deux branches ont modifié les mêmes fichiers sans se voir.

### À faire

1. Depuis `menu-du-jour`, fusionnez la branche `origin/feat/desserts-merge`:

```sh
git merge origin/feat/desserts-merge
```

Git s'arrête et signale deux fichiers en conflit. Pour lire les marqueurs de conflit et savoir comment les résoudre, voir [docs/concepts.md](docs/concepts.md).

2. Résolvez les trois points suivants :

- **`CHANGELOG.md` — gardez les deux côtés.** Les trois lignes des entrées s'opposent aux trois lignes des desserts, sous la même rubrique. Les six modifications ont bien eu lieu : la résolution consiste à garder les six lignes.
- **`AFFICHE.md`, ligne du total — aucun des deux côtés n'est la réponse.** Un côté annonce 880, l'autre 890. Ni l'un ni l'autre n'est correct : le menu réunit maintenant les entrées **et** les desserts. Si votre éditeur vous propose « accepter le courant » ou « accepter l'entrant », les deux vous donneront un résultat faux. Calculez la bonne valeur.
- **`AFFICHE.md`, ligne du nombre de plats — Git n'a rien signalé, et la valeur est fausse.** Les deux branches avaient écrit 6 ; comme elles écrivaient la même chose, Git a pris cette valeur sans rien dire. Or le menu compte désormais 9 plats. Corrigez-la vous-même.

3. Une fois les deux fichiers résolus :

```sh
git add AFFICHE.md CHANGELOG.md
git commit
java Verificateur.java 2
```

### À observer

**Une fusion sans conflit ne garantit rien.** Git sait recoller des lignes, il ne sait pas si le résultat a du sens. C'est le vérificateur qui vous le révèle, et c'est pour cela qu'il faut le lancer après chaque fusion.

Remarquez au passage que `Menu.java` s'est fusionné tout seul, et correctement : les entrées se sont insérées en haut du tableau, les desserts en bas.

---

## Exercice 3 — Le même scénario, en rebase

### Objectif

Obtenir le même résultat que l'exercice 2, mais par un rebase plutôt qu'une fusion.

> ⚠️ Ici l'objectif n'est pas correct, il faut 2 graphs ou un gif qui montre le rebase sur la branche master, sinon ils ne vont pas comprendre

```mermaid
%%{init: {'gitGraph': {'mainBranchName': 'menu-du-jour'}}}%%
gitGraph
   commit id: "build(init)"
   branch feat/desserts-merge
   checkout menu-du-jour
   branch feat/entrees
   commit id: "carottes"
   commit id: "oeuf mayo"
   commit id: "terrine"
   checkout menu-du-jour
   merge feat/entrees
   branch feat/desserts-rebase
   checkout feat/desserts-merge
   commit id: "tarte"
   commit id: "mousse"
   commit id: "fruits"
   checkout menu-du-jour
   merge feat/desserts-merge
   checkout feat/desserts-rebase
   commit id: "tarte "
   commit id: "mousse "
   commit id: "fruits "
```

### Contexte

`origin/feat/desserts-rebase` est la jumelle exacte de la branche de l'exercice 2 : mêmes fichiers, mêmes messages, seuls les identifiants de commit diffèrent. Vous allez obtenir le même résultat par un autre chemin, pour comparer les deux historiques. **Aucune fusion dans cet exercice.**

### À faire

1. Placez-vous sur la branche et rebasez-la sur le commit de fusion noté à l'exercice 1 :

```sh
git switch feat/desserts-rebase
git rebase <SHA du commit de fusion noté à l'exercice 1>
```

Remplacez `<SHA du commit de fusion noté à l'exercice 1>` par le SHA que vous avez copié : `git rebase 3f9a2c1` par exemple, avec votre propre valeur.

> Si vous êtes perdu·e à n'importe quel moment :
>
> ```sh
> git rebase --abort
> ```
>
> Vous revenez exactement où vous étiez avant de lancer le rebase, et vous pouvez recommencer.

2. Le rebase ne fusionne pas : il **rejoue** vos commits un par un par-dessus le commit que désigne ce SHA. Il s'arrêtera **deux fois** :

- **au premier commit rejoué**, sur `CHANGELOG.md` : gardez les trois lignes des entrées et celle de la tarte ;
- **au troisième**, sur `AFFICHE.md` : même situation qu'à l'exercice 2, la bonne valeur du total n'est aucune des deux proposées. Et **le nombre de plats est là encore faux sans que Git l'ait signalé** : corrigez-le aussi.

Le deuxième commit se rejoue tout seul. À chaque arrêt, après avoir corrigé les fichiers :

```sh
git add .
git rebase --continue
```

3. Une fois le rebase terminé :

```sh
java Verificateur.java 3
```

### À observer

```sh
git log --graph --oneline menu-du-jour feat/desserts-rebase
```

Un seul graphe, deux formes. À gauche `menu-du-jour`, avec sa fourche et son commit de fusion. À droite la jumelle rebasée : trois commits en ligne droite posés sur le même SHA de départ, sans commit de fusion. Même point de départ, même contenu final, deux historiques différents.

Regardez aussi les identifiants des trois commits rejoués, et comparez-les à ceux de `origin/feat/desserts-rebase`.

---

## Exercice 4 — Un conflit à chaque commit rejoué

### Objectif

Ajuster les tarifs du menu en rejouant vos commits par-dessus le travail déjà poussé par une autre équipe.

```mermaid
%%{init: {'gitGraph': {'mainBranchName': 'menu-de-demain'}}}%%
gitGraph
   commit id: "entrees + desserts deja integres"
   branch tarifs-base
   commit id: "revalorisation"
   branch feat/tarifs
   commit id: "prix lentilles rejoue"
   commit id: "prix steak rejoue"
   commit id: "prix yaourt rejoue"
```

### Contexte

À partir d'ici, on quitte `menu-du-jour` : les deux derniers exercices se déroulent chacun sur sa propre base, déjà construite avec la correction des exercices précédents. Si vous avez buté sur un exercice, vous repartez malgré tout du bon pied.

Une autre équipe a déjà poussé la revalorisation annuelle des tarifs sur `origin/tarifs-base`. Votre branche `feat/tarifs` ajuste les mêmes prix, mais différemment. Vous ne fusionnez pas : vous passez par-dessus leur travail.

### À faire

1. Placez-vous sur `feat/tarifs` et rebasez-la :

```sh
git switch feat/tarifs
git rebase origin/tarifs-base
```

> ```sh
> git rebase --abort
> ```
>
> Toujours disponible. Profitez de cet exercice pour l'essayer au moins une fois : abandonnez volontairement le rebase au premier conflit, vérifiez avec `git log --oneline` que votre branche est intacte, puis relancez le rebase.

**Ne lancez pas le vérificateur avant d'avoir terminé ce rebase.** La branche `feat/tarifs` est volontairement incohérente tant que l'arbitrage n'est pas fait : son affiche annonce déjà le total d'arrivée, que son menu n'atteindra qu'à la fin.

2. Le rebase s'arrête **trois fois, une par commit rejoué**, toujours sur `Menu.java`. À chaque fois, un prix de la revalorisation s'oppose au vôtre. Les prix attendus à l'arrivée sont imposés :

| Plat | Prix attendu | D'où il vient |
|---|---|---|
| Salade de lentilles | 140 | le côté `origin/tarifs-base` |
| Steak haché frites | 390 | le côté de votre branche |
| Yaourt nature | 95 | le côté `origin/tarifs-base` |

Autrement dit : **prendre systématiquement le même côté échoue.** Vous devez lire chaque conflit et composer.

Attention également : un bloc de conflit peut contenir plus d'une ligne. Le steak et le yaourt se suivent dans le tableau, Git les présente donc ensemble, et une seule des deux lignes est réellement en jeu. Recopiez l'autre telle qu'elle doit être.

À chaque arrêt :

```sh
git add Menu.java
git rebase --continue
```

3. Une fois le rebase terminé :

```sh
java Verificateur.java 4
```

### À observer

```sh
git log --graph --oneline origin/tarifs-base feat/tarifs
```

Vos trois commits sont maintenant posés au-dessus de la revalorisation, en ligne droite. Aucun commit de fusion.

---

## Exercice 5 — Réécrire ses commits avant de les montrer

### Objectif

Réunir trois commits maladroits en un seul, correctement nommé, avant de les partager.

```mermaid
%%{init: {'gitGraph': {'mainBranchName': 'menu-du-jour'}}}%%
gitGraph
   commit id: "build(init)"
   branch feat/vegetarien
   commit id: "tarifs arbitres deja appliques"
   commit id: "feat(menu): option vegetarienne"
```

### Contexte

La branche `origin/feat/vegetarien` ajoute une option végétarienne au plat principal. Le travail est correct, mais il a été fait en trois commits aux messages inutilisables : `wip`, `oups`, `fix typo`. Vous allez les réunir en un seul, correctement nommé.

### À faire

1. Vérifiez que Git sait quel éditeur ouvrir :

```sh
git config --get core.editor
```

Si la commande ne renvoie rien, définissez-le maintenant, sans quoi Git ouvrira Vim et vous aurez du mal à en sortir :

```sh
git config --global core.editor "code --wait"
```

2. Placez-vous sur la branche et lancez un rebase interactif :

```sh
git switch feat/vegetarien
git rebase -i HEAD~3
```

Git ouvre la liste des trois commits, chacun précédé de `pick`. Les trois verbes à utiliser (`pick`, `squash`, `reword`) sont détaillés dans [docs/commandes.md](docs/commandes.md).

3. Laissez `pick` sur la première ligne, remplacez `pick` par `squash` sur les deux suivantes, enregistrez et fermez. Git rouvre alors l'éditeur avec les trois anciens messages : effacez-les et écrivez à la place :

```sh
feat(menu): ajoute une option végétarienne au plat principal
```

Enregistrez et fermez.

> ```sh
> git rebase --abort
> ```
>
> Vaut aussi pour le rebase interactif.

4. Puis :

```sh
java Verificateur.java 5
```

### À observer

```sh
git log --oneline origin/feat/vegetarien..feat/vegetarien
git log --oneline feat/vegetarien~1..feat/vegetarien
```

La première commande ne renvoie rien de commun : vos commits ne sont plus les mêmes objets. La seconde en montre un seul, avec le bon message : `feat/vegetarien~1` désigne le commit dont `feat/vegetarien` est partie, avant la réécriture. Les trois commits d'origine ont été remplacés par un unique commit qui porte le même contenu.

---

## Exercice 6 — Ajouter vos propres plats

### Objectif

Créer votre propre branche pour ajouter de nouveaux plats au menu, la pousser sur votre dépôt distant, puis l'intégrer à `menu-du-jour`.

### Contexte

Jusqu'ici, toutes les branches que vous avez fusionnées ou rebasées étaient déjà préparées pour vous par le Crous. Ce dernier exercice vous fait pratiquer le cycle complet d'une contribution : créer une branche, y enregistrer votre propre travail, la pousser sur GitHub, puis la réintégrer.

### À faire

1. Créez votre branche depuis `menu-du-jour` :

```sh
git switch menu-du-jour
git switch -c feat/mes-plats
```

2. Ajoutez un ou plusieurs nouveaux `Plat` dans `Menu.java` (une soupe, un fromage, un café...), puis enregistrez votre travail :

```sh
git add Menu.java
git commit -m "feat(menu): ajoute mes plats"
```

3. Poussez votre branche sur votre fork :

```sh
git push -u origin feat/mes-plats
```

4. Revenez sur `menu-du-jour` et intégrez votre branche :

```sh
git switch menu-du-jour
git merge feat/mes-plats
```

5. Vérifiez votre menu :

```sh
java Verificateur.java
```

### À observer

Une fusion sans le moindre conflit, puisque vous êtes seul·e à avoir modifié cette branche — contrairement aux exercices précédents. C'est la situation la plus fréquente en réalité : la plupart des fusions se passent sans encombre, mais il faut être capable de gérer les autres.

---

## Exercices bonus

À faire seulement si vous avez terminé les six exercices. Ils sont indépendants les uns des autres et le vérificateur ne les connaît pas.

Chacun commence par créer une branche jetable : vous ne risquez donc rien, et vous pouvez les recommencer autant de fois que vous voulez.

### Bonus A — Défaire sans casser

Créez une branche jetable depuis `menu-du-jour`, puis faites-y un commit volontairement mauvais : donnez à un plat le prix de 9999 centimes. Lancez le vérificateur et constatez qu'il passe au rouge.

Annulez maintenant ce commit de deux façons, l'une après l'autre :

```sh
git revert <identifiant du commit>
```

ajoute un nouveau commit qui défait le précédent. L'historique grossit, rien n'est perdu.

```sh
git reset --hard <identifiant du commit d'avant>
```

recule le pointeur de la branche. L'historique rétrécit, le commit disparaît de la branche.

Le contenu final est le même dans les deux cas, l'historique non.

**À consigner :** les deux `git log --oneline`, et une phrase sur le cas où l'un vaut mieux que l'autre. Indice : la branche est-elle partagée ?

### Bonus B — Mettre de côté

Modifiez un fichier sans l'enregistrer, puis :

```sh
git stash
```

range vos modifications et rend le répertoire propre, ce qui vous permet de changer de branche. Pour les remettre :

```sh
git stash pop
```

### Bonus C — Deux répertoires de travail

Comment traiter une correction urgente sans toucher à votre travail en cours.

```sh
git worktree add ../menu-hotfix <une branche existante>
git worktree list
git worktree remove ../menu-hotfix
```

Deux points qui surprennent :

- une même branche ne peut pas être active dans deux répertoires de travail à la fois ;
- chaque répertoire de travail a ses propres fichiers non versionnés.

### Bonus D — Retrouver ce qu'on croit perdu

Fait suite au bonus A, si vous avez choisi `git reset --hard` : ce commit a disparu de la branche.

```sh
git reflog
```

liste tout ce que `HEAD` a visité, y compris ce qui n'est plus atteignable depuis aucune branche. Retrouvez-y le commit effacé au bonus A et récupérez-le sur une nouvelle branche.

Retenez ceci : tant qu'un commit a existé, il reste presque toujours récupérable pendant plusieurs semaines. C'est ce qui rend Git sûr.
