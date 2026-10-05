# Variables
MAVEN = mvn

# Cible par défaut
all: clean compile check

# Nettoyer le projet
clean:
	$(MAVEN) clean

# Compiler le projet
compile:
	$(MAVEN) compile

# Exécuter checkstyle
check:
	$(MAVEN) checkstyle:check

# Aide
help:
	@echo "Cibles disponibles:"
	@echo "  all        - Nettoie, compile et vérifie le style"
	@echo "  clean      - Nettoie le projet"
	@echo "  compile    - Compile le projet"
	@echo "  check - Vérifie le style du code"

.PHONY: all clean compile check help