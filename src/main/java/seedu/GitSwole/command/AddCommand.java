package seedu.GitSwole.command;

import seedu.GitSwole.assets.Exercise;
import seedu.GitSwole.assets.Workout;
import seedu.GitSwole.assets.WorkoutList;
import seedu.GitSwole.exceptions.GitSwoleException;
import seedu.GitSwole.parser.Parser;
import seedu.GitSwole.ui.Ui;

/**
 * Represents a command that adds a new workout or exercise to the workout list.
 * <p>
 * Supported formats:
 * <ul>
 *   <li>{@code add w/WORKOUT} — creates a new workout session</li>
 *   <li>{@code add e/EXERCISE w/WORKOUT [wt/WEIGHT] [s/SET] [r/REPETITION]} — adds an exercise to a workout</li>
 * </ul>
 */
public class AddCommand extends Command {
	private String response;

	/**
	 * Constructs an AddCommand with the raw user input string.
	 *
	 * @param response The full command string entered by the user.
	 */
	public AddCommand(String response) {
		this.response = response;
	}

	/**
	 * Executes the add command by parsing the user input and either creating a new
	 * workout or adding an exercise to an existing one.
	 *
	 * @param workouts The current list of workouts.
	 * @param ui The user interface for displaying results.
	 * @throws GitSwoleException If required fields such as workout name or exercise details are missing or malformed.
	 */
	@Override
	public void execute(WorkoutList workouts, Ui ui) throws GitSwoleException {
		if (response.contains("e/")) {
			handleAddExercise(workouts, ui);
		} else {
			handleAddWorkout(workouts,ui);
		}
	}

	/**
	 * Processes the addition of a new workout session to the application.
	 * <p>
	 * This method corresponds to Feature 2. It extracts the workout name from the
	 * command string and adds a new {@link Workout} object to the list.
	 *
	 * @param workouts The {@link WorkoutList} to which the new session will be added.
	 * @param ui The {@link Ui} instance used to display success or error messages.
	 * @throws GitSwoleException If the workout name (w/) is missing or the value is empty.
	 */
	private void handleAddWorkout(WorkoutList workouts, Ui ui) throws GitSwoleException {
		String workoutName = Parser.parseValue(response, "w/");
		if (workoutName == null || workoutName.isEmpty()) {
			throw new GitSwoleException(GitSwoleException.ErrorType.INCOMPLETE_COMMAND, "Missing name of workout.");
		}

		workouts.addWorkout(new Workout(workoutName));
		ui.showMessage("Successfully added a " + workoutName + " session! Remember to add your exercises :)");
		ui.showLine();
	}

	/**
	 * Processes the addition of a specific exercise to an existing workout session.
	 * <p>
	 * This method corresponds to Feature 3. It parses the exercise name, target workout name,
	 * and optional flags for weight (wt/), sets (s/), and repetitions (r/). If optional flags
	 * are missing, they default to 0.
	 *
	 * @param workouts The {@link WorkoutList} used to locate the target workout session.
	 * @param ui The {@link Ui} instance used to display success or error messages.
	 * @throws GitSwoleException If the exercise name (e/) or workout name (w/) is missing,
	 *                           or if the specified workout cannot be found in the list.
	 */
	private void handleAddExercise(WorkoutList workouts, Ui ui) throws GitSwoleException {
		String exerciseName = Parser.parseValue(response, "e/");
		String workoutName = Parser.parseValue(response, "w/");

		if (exerciseName == null || workoutName == null) {
			throw new GitSwoleException(GitSwoleException.ErrorType.INCOMPLETE_COMMAND, "Missing name of workout / exercise");
		}

		Workout targetWorkout = workouts.getWorkoutByName(workoutName);
		if (targetWorkout == null) {
			throw new GitSwoleException(GitSwoleException.ErrorType.IDX_OUTOFBOUNDS, workoutName);
		}

		int weight = Parser.parseOptionalInt(response, "wt/", 0);
		int sets = Parser.parseOptionalInt(response, "s/", 0);
		int reps = Parser.parseOptionalInt(response, "r/", 0);

		targetWorkout.addExercise(new Exercise(exerciseName, weight, sets, reps));
		ui.showMessage("Your exercise has been successfully added! Looking swole g");
		ui.showLine();
	}
}
