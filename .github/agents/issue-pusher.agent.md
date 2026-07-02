---
name: IssuePusher
description: |
  Agent responsable de pousser les issues locales (docs/features/**) vers GitHub via MCP.
  - Crée une issue distante si elle n'existe pas.
  - Ignore ou met à jour les fichiers déjà marqués comme poussés.
  - Enregistre le numéro/URL retourné dans l'en-tête YAML du fichier local.
---

Comportement attendu
- Scanne les fichiers matching `docs/features/**/*.md` (configurable).
- Pour chaque fichier :
  - Lit l'en-tête YAML (si existante) et recherche `mcp_issue_number` ou `issue_number`.
  - Si `mcp_issue_number` est présent et non vide : saute le fichier (option `--force` pour recréer).
  - Sinon : extrait `title` et `body` (titre H1 ou champ YAML `title`), compose `body` complet et appelle `mcp_github_mcp_se_issue_write` avec `owner`, `repo`, `title`, `body` et `labels` optionnels.
  - Récupère la réponse MCP (issue number / URL) et insère/écrit `mcp_issue_number: <n>` et `mcp_issue_url: <url>` dans l'en-tête YAML du fichier local.

Entrées attendues
- `owner` (string) : propriétaire GitHub (obligatoire si non détecté).
- `repo` (string) : nom du repository (obligatoire si non détecté).
- `path_pattern` (string) : glob pour fichiers d'issues (défaut `docs/features/**/*.md`).
- `labels` (array[string]) : labels à attacher aux issues créées.
- `dry_run` (bool) : si vrai, ne fait pas d'appel MCP, affiche uniquement ce qui serait fait.
- `force` (bool) : si vrai, recrée/pousse même si `mcp_issue_number` existe.

Sécurité & Avertissements
- Cet agent utilisera la fonction `mcp_github_mcp_se_issue_write` pour créer/update les issues.
- Si `owner`/`repo` ne sont pas fournis, l'agent demandera ces valeurs à l'utilisateur.

Exemple d'invocation (via runSubagent)
{
  "agentName": "IssuePusher",
  "prompt": "Push local issues to GitHub",
  "description": "Push local docs/features issues to GitHub via MCP",
  "model": "GPT-5 mini",
  "args": {
    "owner": "my-org",
    "repo": "my-repo",
    "path_pattern": "docs/features/**/*.md",
    "labels": ["feature","needs-triage"],
    "dry_run": false,
    "force": false
  }
}

Notes pour l'implémentation
- Les appels MCP doivent être faits par l'agent (mcp_github_mcp_se_issue_write).
- Après création remote, modifier le fichier source pour ajouter/mettre à jour l'en-tête YAML avec `mcp_issue_number` et `mcp_issue_url`.
- Fournir une option `--dry-run` pour revue humaine avant push.
