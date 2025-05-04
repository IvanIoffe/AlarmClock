# Android приложение "Будильник"
Это приложение-будильник с расширенными функциями и интеграцией со Spotify.
Позволяет создавать и настраивать будильники с выбором звука, вибрации и временем повтора.

### Возможности
- ⏰ Создание и управление несколькими будильниками
- 🔊 Выбор звука будильника из:
  - системных звуков
  - локальных файлов
  - треков Spotify
  - новых релизов любимого исполнителя на Spotify
- 🔁 Возможность "отложить" будильник
- 📶 Работает в оффлайн-режиме (кроме Spotify-функций)
### Требования для Spotify-функций
1) Приложение Spotify должно быть установлено на устройстве
2) Необходимо авторизоваться через установленное приложение Spotify
3) Необходимо подключение к Интернету при выборе треков и при срабатывании будильника
### API
- Spotify Web API
- Spotify Android SDK
- Spotify Auth
### Стек
- Language - Kotlin
- UI - Jetpack Compose, Material 3, Lottie Compose, Coil Compose
- Architecture - Clean Architecture, MVVM
- Navigation - Navigation Compose
- DI - Hilt
- Background Work - Foreground Service, WorkManager, AlarmManager
- Network - Retrofit, OkHttp
- Database - Room
- Serialization - Gson
- SplashScreen - Core SplashScreen API