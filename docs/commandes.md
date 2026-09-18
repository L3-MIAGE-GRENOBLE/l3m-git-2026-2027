# Les commandes du TP

## Se préparer

| Commande | Ce qu'elle fait |
|---|---|
| `git clone <adresse>` | Récupère votre fork sur votre machine. |
| `git config --global user.name "Prenom Nom"` | Le nom qui signera vos commits. |
| `git config --global user.email "..."` | L'adresse qui signera vos commits. |
| `git config --global core.editor "code --wait"` | L'éditeur que Git ouvrira. Indispensable à l'exercice 5. |
| `git config merge.ff false` | Règle ce dépôt pour que chaque fusion laisse une trace visible dans l'historique. |

## Se repérer

| Commande | Ce qu'elle fait |
|---|---|
| `git status` | Où vous êtes, ce qui est modifié, ce qui reste à faire. |
| `git log --graph --oneline --all` | L'historique complet sous forme de graphe. |
| `git log origin/feat/entrees` | Les commits d'une branche, sans s'y déplacer. |
| `git log --oneline -1` | Le SHA (abrégé) du commit courant, à copier pour vous y référer plus tard. |
| `git diff` | Ce que vous avez modifié et pas encore enregistré. |

## Enregistrer

| Commande | Ce qu'elle fait |
|---|---|
| `git add <fichier>` | Marque un fichier comme prêt à être enregistré. |
| `git add .` | Marque tous les fichiers modifiés. |
| `git commit -m "<message>"` | Enregistre un commit avec les fichiers marqués. |

## Se déplacer

| Commande | Ce qu'elle fait |
|---|---|
| `git switch menu-du-jour` | Se place sur la branche `menu-du-jour`. |
| `git switch feat/tarifs` | Se place sur `feat/tarifs`, en la créant depuis `origin/` si besoin. |
| `git switch -c <nom>` | Crée une nouvelle branche à partir d'où vous êtes, et s'y place. |

## Partager

| Commande | Ce qu'elle fait |
|---|---|
| `git push -u origin <branche>` | Envoie votre branche sur votre dépôt distant (votre fork). |

## Assembler

| Commande | Ce qu'elle fait |
|---|---|
| `git merge origin/feat/entrees` | Fusionne une branche dans celle où vous êtes. Crée un commit de fusion. |
| `git rebase <sha>` | Rejoue les commits de votre branche par-dessus le commit désigné par ce SHA. |
| `git rebase --continue` | Reprend le rebase après avoir résolu un conflit et fait `git add`. |
| `git rebase -i HEAD~3` | Rebase interactif : permet de réécrire les trois derniers commits. |

Une fois les conflits résolus (voir [concepts.md](concepts.md) pour lire les marqueurs), `git add <fichier>` puis, selon le cas, `git commit` ou `git rebase --continue`.

## En cas de problème pendant un rebase

> ```
> git rebase --abort
> ```
>
> Annule le rebase en cours et vous ramène exactement où vous étiez avant de le lancer. Rien n'est perdu. Utilisez-la sans hésiter : vous pourrez toujours relancer le rebase ensuite.

### Les trois verbes du rebase interactif

| Verbe | Effet |
|---|---|
| `pick` | Garde le commit tel quel. |
| `squash` | Fusionne ce commit dans le précédent. |
| `reword` | Garde le commit mais change son message. |

---

## Pour aller plus loin

Rien de ce qui suit n'est nécessaire au fil rouge. Ces commandes servent aux exercices bonus, en fin de sujet.

| Commande | Ce qu'elle fait |
|---|---|
| `git revert <sha>` | Ajoute un commit qui défait un commit précédent. L'historique grossit, rien n'est perdu. |
| `git reset --hard <sha>` | Recule le pointeur de la branche. L'historique rétrécit, le commit disparaît de la branche. |
| `git reflog` | Liste tout ce que `HEAD` a visité, y compris ce qui n'est plus atteignable. |
| `git worktree add ../dossier <branche>` | Ouvre un second répertoire de travail sur une autre branche. |
| `git stash` | Met de côté vos modifications non enregistrées. |
| `git stash pop` | Remet en place les modifications mises de côté. |
