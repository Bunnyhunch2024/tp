package seedu.gitswole.storage;

import seedu.gitswole.assets.Exercise;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages the persistent storage of workout session history.
 * <p>
 * This class handles writing formatted session logs to a history file,
 * including timestamps, workout names, and exercise performance data.
 */
public class HistoryStorage {
    private static final String HISTORY_FILE_PATH = "docs/history.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");

    /**
     * Appends a session header to the history file.
     * <p>
     * Starts a new entry with the current date, time, and workout name.
     *
     * @param workoutName The name of the workout session being logged.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeSessionHeader(String workoutName) throws IOException {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        try (PrintWriter out = new PrintWriter(new FileWriter(HISTORY_FILE_PATH, true))) {
            out.println("[" + timestamp + "] " + workoutName.toUpperCase() + " workout");
        }
    }

    /**
     * Appends an exercise log entry to the current session in the history file.
     * <p>
     * Formats the exercise details including name, weight, sets, and repetitions.
     *
     * @param exercise The {@link Exercise} object containing the performance data.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeExerciseLog(Exercise exercise) throws IOException {
        String logEntry = String.format("%-18s: %4dkg | %2d sets | %2d reps",
                exercise.getExerciseName() + ":",
                exercise.getWeight(),
                exercise.getSets(),
                exercise.getReps());
        
        try (PrintWriter out = new PrintWriter(new FileWriter(HISTORY_FILE_PATH, true))) {
            out.println(logEntry);
        }
    }

    /**
     * Appends a visual separator to the history file to mark the end of a session.
     *
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeSeparator() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(HISTORY_FILE_PATH, true))) {
            out.println("--------------------------------------------");
        }
    }
}
