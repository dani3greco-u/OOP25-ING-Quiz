# ING-Quiz

ING-Quiz is a desktop quiz application focused on computer science and digital technologies, inspired by the format of *Who Wants to Be a Millionaire?*.

The application was developed in Java using Swing and follows a Model–View–Controller architecture.

## Main features

- 15-question quiz with increasing difficulty
- 4 possible answers for each question
- 3 single-use helps:
  - **50:50**
  - **Double Chance**
  - **Switch**
- Normal mode with score recording
- Training mode without score recording
- Local leaderboard with the best score for each player
- Local question repository used as the primary source
- Remote Open Trivia Database repository used as fallback
- Visual and audio feedback for correct answers, wrong answers, victory and defeat

## Requirements

- Java 21

## Run the application

The repository root contains the executable fat JAR.

```bash
java -jar ING-Quiz.jar
```

The application can be started without any external resources: questions, sounds, icons and runtime dependencies are included in 
the JAR.

## Build from source

On Linux or macOS:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat  build
```

The build process compiles the project, runs the automated tests and generates the executable fat JAR.

## Local leaderboard

The leaderboard is stored locally on the user's computer.

When the first score is recorded, the application automatically creates an external JSON file. The file is updated at the end of 
subsequent games played in normal mode.

Training sessions and voluntarily abandoned games are not recorded.

## Question sources

The application uses two interchangeable question repositories:

1. a large local JSON repository, used as the primary source;
2. Open Trivia Database, used as a fallback source.

For every game, the application selects a balanced set of questions divided among easy, medium and hard difficulty levels.

## Testing

The automated test suite is based on JUnit 5.

Mockito is used to isolate external dependencies, especially the HTTP client used by the remote question repository.

Run the tests with:

```bash
./gradlew test
```

## External libraries

- Jackson
- Mockito
- Apache Commons Text
- JUnit 5

## Documentation

The project report is available in the repository root as:

```text
report.pdf
```