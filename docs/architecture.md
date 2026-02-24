---
# Arquitectura de BabyAi

## 1. MVVM Clean
Se usa MVVM Clean, separando:
- **UI (Fragments/Activities)**: Interfaz visual, gestiona eventos y display.
- **ViewModel**: Ciclo de vida de entrenamiento, lógica de presentación, corrutinas.
- **Repository**: Acceso a datos, IA y persistencia.
- **Model/Data**: Entidades Room y estructura de batches.

## 2. TensorFlow Lite Trainable
- Archivo base_model.tflite en /ai-models.
- Soporte para capas entrenables (via TfLiteTrainableModel.kt), entrenamiento en batches y checkpoints.
- Pesos y checkpoints en Internal Storage (MappedByteBuffer).

## 3. Persistencia
- Room Database para training data (AppRoomDatabase, TrainingDao, DataEntry).
- Pesos y checkpoints como archivos binarios en /ai-models/checkpoints.

## 4. Entrenamiento
- Corrutinas (Dispatchers.Default) para entrenamientos fluidos y no bloqueantes.
- Entrenamiento por batches y guardado de checkpoint cada N epochs.

## 5. Background Training
- WorkManager (TrainingWorker.kt) para ejecutar entrenamiento en background SOLO si el dispositivo está cargando.

## 6. Gestión de Memoria
- Uso de MappedByteBuffer para cargar y guardar archivos .tflite/.bin.
- Cuidado con fugas de memoria: liberación explícita y uso de context seguro.

## 7. UI Multilenguaje
- Fragmentos y recursos strings.xml con soporte de español/inglés.

Consulta README.md para implementación inicial.