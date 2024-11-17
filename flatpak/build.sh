# Script to debug the Flatpak application.
flatpak-builder --user --install --force-clean build-dir dev.nuculabs.ImageTagger.yaml
flatpak run dev.nuculabs.ImageTagger