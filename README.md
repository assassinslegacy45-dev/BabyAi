# BabyAi

BabyAi es una plataforma Android open-source para entrenar modelos de IA directamente en el dispositivo, usando TensorFlow Lite y Room Database. La arquitectura sigue MVVM Clean para separar la lógica de IA de la interfaz. Incluye entrenamiento en segundo plano usando WorkManager, gestión eficiente de memoria con MappedByteBuffer, y almacenamiento seguro de checkpoints y pesos en /ai-models.

## Instrucciones Iniciales
1. Clona el repositorio:
   ```
   git clone https://github.com/assassinslegacy45-dev/BabyAi.git
   ```
2. Abre el proyecto en Android Studio.
3. Revisa scripts/export_tflite_model.py para exportar tu modelo inicial (base_model.tflite).
4. Configura los archivos string.xml para multilenguaje (es/en).
5. Utiliza los fragmentos entregados en app/src/main/java/com/baby/ai/ para expandir el proyecto.

Más detalles en docs/architecture.md.