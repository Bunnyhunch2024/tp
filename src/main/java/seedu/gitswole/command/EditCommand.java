package seedu.gitswole.command;

import seedu.gitswole.assets.Exercise;
import seedu.gitswole.assets.Workout;
import seedu.gitswole.assets.WorkoutList;
import seedu.gitswole.exceptions.GitSwoleException;
import seedu.gitswole.parser.Parser;
import seedu.gitswole.ui.Ui;

import java.util.Scanner;
import java.util.logging.Level;

public class EditCommand extends Command{
    private String response;
    private Scanner scanner;

    public EditCommand(String response){
        this.response = response;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(WorkoutList workouts, Ui ui) throws GitSwoleException {
        if (response.contains(" e/")) {
            handleEditExercise(workouts, ui);
        } else {
            handleEditWorkout(workouts, ui);
        }
        ui.showLine();
    }

    private void handleEditExercise(WorkoutList workouts, Ui ui) throws GitSwoleException {
        String workoutToEditString = Parser.parseValue(response, "w/");
        String exerciseToEditString = Parser.parseValue(response, "e/");

        boolean validInput = (workoutToEditString == null || workoutToEditString.isEmpty()) ||
            (exerciseToEditString == null || exerciseToEditString.isEmpty());
        if (validInput) {
            LOGGER.log(Level.WARNING, "EditWorkout failed: Missing 'w/' or 'e/' flag.");
            throw new GitSwoleException(
                GitSwoleException.ErrorType.INCOMPLETE_COMMAND,
                "Missing name of workout. Usage: edit w/WORKOUT_NAME or edit w/WORKOUT_NAME e/EXERCISE"
            );
        }

        Workout workoutToEdit = workouts.getWorkoutByName(workoutToEditString);
        if (workoutToEdit == null) {
            LOGGER.log(Level.WARNING, "EditWorkout failed: Workout provided does not exist.");
            throw new GitSwoleException(GitSwoleException.ErrorType.NOT_FOUND , workoutToEditString);
        }
        ui.showMessage(exerciseToEditString);
        Exercise exerciseToEdit = workoutToEdit.getExerciseByName(exerciseToEditString);

        changeWorkoutName(ui, workoutToEdit);
        changeExerciseName(ui, exerciseToEdit);
        changeWeight(ui, exerciseToEdit);
        changeSets(ui, exerciseToEdit);
        changeReps(ui,exerciseToEdit);
    }

    private void changeReps(Ui ui, Exercise exerciseToEdit) {
        int oldExerciseReps = exerciseToEdit.getReps();

        ui.showLine();
        ui.showMessage("Change CURRENT Reps: " + exerciseToEdit.getReps());
        ui.showMessage("Change to (press enter to cancel edit): ");
        int newExerciseReps;
        try {
            newExerciseReps = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            ui.showMessage("No Change recorded!");
            return;
        }
        if (newExerciseReps <= 0) {
            ui.showMessage("No Change recorded!");
            return;
        }
        exerciseToEdit.setReps(newExerciseReps);

        ui.showMessage("Change Completed: " + oldExerciseReps + " --> " + exerciseToEdit.getReps());
        ui.showLine();
    }

    private void changeSets(Ui ui, Exercise exerciseToEdit) {
        int oldExerciseSets = exerciseToEdit.getSets();
        ui.showLine();
        ui.showMessage("Change CURRENT Sets: " + exerciseToEdit.getSets());
        ui.showMessage("Change to (press enter to cancel edit): ");
        int newExerciseSets;
        try {
            newExerciseSets = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            ui.showMessage("No Change recorded!");
            return;
        }
        if (newExerciseSets <= 0) {
            ui.showMessage("No Change recorded!");
            return;
        }
        exerciseToEdit.setSets(newExerciseSets);

        ui.showMessage("Change Completed: " + oldExerciseSets + " --> " + exerciseToEdit.getSets());
    }

    private void changeWeight(Ui ui, Exercise exerciseToEdit) {
        int oldExerciseWeight = exerciseToEdit.getWeight();
        ui.showLine();
        ui.showMessage("Change CURRENT Weight: " + exerciseToEdit.getWeight());
        ui.showMessage("Change to (press enter to cancel edit): ");
        int newExerciseWeight;
        try {
            newExerciseWeight = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            ui.showMessage("No Change recorded!");
            return;
        }
        if (newExerciseWeight <= 0) {
            ui.showMessage("No Change recorded!");
            return;
        }
        exerciseToEdit.setWeight(newExerciseWeight);

        ui.showMessage("Change Completed: " + oldExerciseWeight + " --> " + exerciseToEdit.getWeight());
    }

    private void changeExerciseName(Ui ui, Exercise exerciseToEdit) {
        String oldExerciseName = exerciseToEdit.getExerciseName();
        ui.showLine();
        ui.showMessage("Change CURRENT Exercise Name: " + exerciseToEdit.getExerciseName());
        ui.showMessage("Change to (press enter to cancel edit): ");
        String newExerciseName = scanner.nextLine().trim();
        if (newExerciseName == null || newExerciseName.isEmpty()) {
            ui.showMessage("No Change recorded!");
            return;
        }
        exerciseToEdit.setExerciseName(newExerciseName);

        ui.showMessage("Change Completed: " + oldExerciseName + " --> " + exerciseToEdit.getExerciseName());
    }

    private void changeWorkoutName(Ui ui, Workout workoutToEdit) {
        String oldWorkoutName = workoutToEdit.getWorkoutName();
        ui.showLine();
        ui.showMessage("Change CURRENT Workout Name: " + workoutToEdit.getWorkoutName());
        ui.showMessage("Change to (press enter to cancel edit): ");
        String newWorkoutName = scanner.nextLine().trim();
        if (newWorkoutName == null || newWorkoutName.isEmpty()) {
            ui.showMessage("No Change recorded!");
            return;
        }
        workoutToEdit.setWorkoutName(newWorkoutName);

        ui.showMessage("Change Completed: " + oldWorkoutName + " --> " + workoutToEdit.getWorkoutName());
    }

    private void handleEditWorkout(WorkoutList workouts, Ui ui) throws GitSwoleException {
        String workoutToEditString = Parser.parseValue(response, "w/");
        if (workoutToEditString == null || workoutToEditString.isEmpty()) {
            LOGGER.log(Level.WARNING, "EditWorkout failed: Missing 'w/' or 'e/' flag or value.");
            throw new GitSwoleException(
                GitSwoleException.ErrorType.INCOMPLETE_COMMAND,
                "Missing name of workout. Usage: edit w/WORKOUT_NAME or edit w/WORKOUT_NAME e/EXERCISE"
            );
        }

        Workout workoutToEdit = workouts.getWorkoutByName(workoutToEditString);
        if (workoutToEdit == null) {
            LOGGER.log(Level.WARNING, "EditWorkout failed: Workout provided does not exist.");
            throw new GitSwoleException(GitSwoleException.ErrorType.NOT_FOUND , workoutToEditString);
        }

        changeWorkoutName(ui, workoutToEdit);
    }
}
