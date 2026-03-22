package seedu.gitswole.command;

import seedu.gitswole.assets.Exercise;
import seedu.gitswole.assets.Workout;
import seedu.gitswole.assets.WorkoutList;
import seedu.gitswole.exceptions.GitSwoleException;
import seedu.gitswole.parser.Parser;
import seedu.gitswole.storage.HistoryStorage;
import seedu.gitswole.ui.Ui;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Represents a command that logs performance data for a workout session.
 * <p>
 * Supported formats:
 * <ul>
 *   <li>{@code log w/WORKOUT_NAME} — starts a session and lists exercises</li>
 *   <li>{@code log e/EXERCISE_NAME [w/WORKOUT_NAME] [wt/WEIGHT] [s/SETS] [r/REPS]} 
 *   — updates stats for an exercise and appends to history log. 
 *   If {@code w/} is omitted, the most recent active session name is used.</li>
 * </ul>
 */
public class LogCommand extends Command {
    private String response;
    private HistoryStorage historyStorage = new HistoryStorage();

    /**
     * Constructs a LogCommand with the raw user input string.
     *
     * @param response The full command string entered by the user.
     */
    public LogCommand(String response) {
        assert response != null : "Response cannot be null";
        this.response = response;
    }

    /**
     * Executes the log command by either starting a session or updating an exercise's stats.
     *
     * @param workouts The current list of workouts.
     * @param ui       The user interface for displaying results.
     * @throws GitSwoleException If required flags are missing or the workout/exercise is not found.
     */
    @Override
    public void execute(WorkoutList workouts, Ui ui) throws GitSwoleException {
        assert workouts != null : "WorkoutList must be initialized";
        assert ui != null : "Ui must be initialized";

        if (response.contains(" e/")) {
            handleLogExercise(workouts, ui);
        } else {
            handleLogWorkout(workouts, ui);
        }
    }

    /**
     * Starts a logging session for a specific workout and records the header in history.
     *
     * @param workouts The list of available workouts.
     * @param ui       The UI to display the session start message.
     * @throws GitSwoleException If the workout name flag (w/) is missing or the workout is not found.
     */
    private void handleLogWorkout(WorkoutList workouts, Ui ui) throws GitSwoleException {
        String workoutName = Parser.parseValue(response, "w/");
        if (workoutName == null || workoutName.isEmpty()) {
            LOGGER.log(Level.WARNING, "LogWorkout failed: Missing 'w/' flag.");
            throw new GitSwoleException(GitSwoleException.ErrorType.INCOMPLETE_COMMAND, "log w/WORKOUT_NAME");
        }

        Workout workout = workouts.getWorkoutByName(workoutName);
        if (workout == null) {
            LOGGER.log(Level.INFO, "LogWorkout failed: Workout '{0}' not found.", workoutName);
            throw new GitSwoleException(GitSwoleException.ErrorType.NOT_FOUND, workoutName);
        }

        // Record the session start in the history file
        try {
            historyStorage.writeSeparator();
            historyStorage.writeSessionHeader(workout.getWorkoutName());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to write session header to history: " + e.getMessage());
        }

        // Set the "sticky" active session name
        workouts.setActiveWorkoutName(workout.getWorkoutName());

        ui.showMessage("Session started for " + workout.getWorkoutName() + "! Let's get those gains.");
        ui.showLine();
        ui.showMessage(workout.getWorkoutName().toUpperCase() + " Workout Exercises:");
        ui.printExercises(workout.getExerciseList());
        ui.showLine();
        ui.showMessage("Continue to log your workout by: log e/EXERCISE wt/WEIGHT s/SETS r/REPS");
        ui.showLine();
    }

    /**
     * Updates the performance statistics for a specific exercise and records it in history.
     * Uses the active workout session if the {@code w/} flag is omitted.
     *
     * @param workouts The list of available workouts.
     * @param ui       The UI to display the updated stats.
     * @throws GitSwoleException If required flags (e/) are missing or workout context cannot be found.
     */
    private void handleLogExercise(WorkoutList workouts, Ui ui) throws GitSwoleException {
        String exerciseName = Parser.parseValue(response, "e/");
        String workoutName = Parser.parseValue(response, "w/");

        // Use the sticky session if w/ flag is missing
        if (workoutName == null) {
            workoutName = workouts.getActiveWorkoutName();
        }

        if (exerciseName == null || workoutName == null) {
            LOGGER.log(Level.WARNING, "LogExercise failed: Missing e/ flag or workout context.");
            String usage = "log e/EXERCISE [w/WORKOUT] wt/WEIGHT s/SETS r/REPS";
            throw new GitSwoleException(GitSwoleException.ErrorType.INCOMPLETE_COMMAND, usage);
        }

        Workout workout = workouts.getWorkoutByName(workoutName);
        if (workout == null) {
            LOGGER.log(Level.INFO, "LogExercise failed: Workout '{0}' not found.", workoutName);
            throw new GitSwoleException(GitSwoleException.ErrorType.NOT_FOUND, workoutName);
        }

        Exercise exercise = workout.getExerciseByName(exerciseName);
        if (exercise == null) {
            LOGGER.log(Level.INFO, "LogExercise failed: Exercise '{0}' not found in '{1}'.", 
                new Object[]{exerciseName, workoutName});
            throw new GitSwoleException(GitSwoleException.ErrorType.NOT_FOUND, exerciseName);
        }

        // Update the stats: use existing value as default if flag is missing
        int weight = Parser.parseOptionalInt(response, "wt/", exercise.getWeight());
        int sets = Parser.parseOptionalInt(response, "s/", exercise.getSets());
        int reps = Parser.parseOptionalInt(response, "r/", exercise.getReps());

        exercise.setWeight(weight);
        exercise.setSets(sets);
        exercise.setReps(reps);

        // Record the exercise log in the history file
        try {
            historyStorage.writeExerciseLog(exercise);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to write exercise log to history: " + e.getMessage());
        }

        ui.showMessage("Stats updated for " + exerciseName + " in " + workoutName + "!");
        ui.printExercises(workout.getExerciseList());
        ui.showLine();
    }
}
