# Configuration de OPENAI_API_KEY

## Méthode 1 : Via variable d'environnement Windows

### Option A : Définir la variable d'environnement de façon permanente (recommandé)

Exécutez PowerShell en tant qu'administrateur et lancez :

```powershell
[System.Environment]::SetEnvironmentVariable('OPENAI_API_KEY', 'votre_cle_api_here', 'User')
```

Puis redémarrez votre IDE ou terminal pour que le changement prenne effet.

### Option B : Définir temporairement pour la session courante

```powershell
$env:OPENAI_API_KEY = 'votre_cle_api_here'
```

## Méthode 2 : Via fichier `.env` (développement local)

1. Copiez `.env.example` en `.env` :
```powershell
Copy-Item .env.example .env
```

2. Éditez le fichier `.env` et ajoutez votre clé API OpenAI :
```env
OPENAI_API_KEY=sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

Le fichier `.env` est automatiquement chargé au démarrage de l'application par la classe `EnvLoader`.

**Note** : Le fichier `.env` est ignoré par Git et ne sera jamais commité.

## Méthode 3 : Via application.yaml

Vous pouvez aussi définir directement dans `application.yaml` :
```yaml
openai:
  apiKey: sk-votre_cle_api_here
```

Mais **ne commitez pas votre clé API secrète** dans le code source !

---

**Recommandation** : Utilisez la Méthode 1 (variable d'environnement Windows) ou la Méthode 2 (fichier `.env.local`) pour la sécurité.

