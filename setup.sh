#!/bin/bash

echo "🔍 Detecting installed Java versions..."

# Get all installed Java paths
JAVA_PATHS=($(update-alternatives --list java))

if [ ${#JAVA_PATHS[@]} -eq 0 ]; then
  echo "❌ No Java installations found"
  exit 1
fi

echo "Available Java versions:"
for i in "${!JAVA_PATHS[@]}"; do
  echo "$((i+1))) ${JAVA_PATHS[$i]}"
done

# Ask user to choose
read -p "Select Java (enter number): " choice
INDEX=$((choice-1))

if [ -z "${JAVA_PATHS[$INDEX]}" ]; then
  echo "❌ Invalid choice"
  exit 1
fi

# Extract JAVA_HOME
SELECTED_JAVA="${JAVA_PATHS[$INDEX]}"
JAVA_HOME=$(dirname $(dirname "$SELECTED_JAVA"))

# Set environment
export JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH

echo "✅ JAVA_HOME set to: $JAVA_HOME"
java -version
echo ""

# -------------------------------
# Create VS Code settings.json
# -------------------------------
mkdir -p .vscode

cat > .vscode/settings.json <<EOF
{
  "java.jdt.ls.java.home": "$JAVA_HOME",
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-$(java -version 2>&1 | awk -F[\".] '/version/ {print \$2}')",
      "path": "$JAVA_HOME",
      "default": true
    }
  ]
}
EOF

echo "✅ .vscode/settings.json updated"

# Run Maven
mvn clean install
