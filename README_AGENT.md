# Exécution de l'agent `commit-agent`

Ce document explique comment lancer l'agent d'organisation de commits (`commit-agent`) dans un conteneur Docker (sandbox) et comment fournir la clé API OpenAI nécessaire.

IMPORTANT : ne partagez jamais votre clé API dans le dépôt. Utilisez des variables d'environnement ou un fichier `.env` ignoré par git.


Requirements
- `cagent` CLI installé (https://github.com/docker/cagent) — utile si vous utilisez le wrapper `docker sandbox run cagent`
- Variable d'environnement `OPENAI_API_KEY` définie (voir exemples ci‑dessous)

1) Définir la variable d'environnement `OPENAI_API_KEY`

PowerShell (session courante) :

```powershell
$env:OPENAI_API_KEY = 'sk-proj-Im-votre_cle_ici'
```

PowerShell (définition permanente pour l'utilisateur) :

```powershell
[System.Environment]::SetEnvironmentVariable('OPENAI_API_KEY','sk-proj-Im-votre_cle_ici','User')
```

Bash / Linux / macOS :

```bash
export OPENAI_API_KEY="sk-proj-Im-votre_cle_ici"
```

Remplacez `sk-proj-Im-votre_cle_ici` par votre vraie clé. Si vous préférez, utilisez un fichier `.env` local et chargez-le avant d'exécuter Docker.

2) Exécution depuis un fichier remote (raw GitHub)

Exemple fourni (commande demandée) :

```powershell
docker sandbox run cagent -- --exec https://raw.githubusercontent.com/stoufix/agent-java/refs/heads/master/agents/commit-agent.yaml /organize
```
