// Login and View swticher controller

import com.cafe.model.CafeSystem;
import com.cafe.model.UserRole;
import com.cafe.javafxviews.View;
import com.cafe.javafxviews.ViewSwitcher;
import com.cafe.javafxviews.HelloView;
import com.cafe.javafxviews.CustomerOrderingView;
import com.cafe.javafxviews.BaristaFulfillmentView;
import com.cafe.javafxviews.ManagerOperationsView;

import javafx.stage.Stage;
import javafx.scene.control.Label;

/**
 * Interface for the Login Controller.
 * Defines the contract for handling login actions.
 */
public interface LoginController {
    void handleLogin(Stage stage, String username, String password, Label errorLabel);
}

/**
 * Implementation of the LoginController.
 * This class mediates between the LoginView and the CafeSystem (Model).
 */
public class LoginControllerImpl implements LoginController {
    private final CafeSystem cafeSystem; // The Model

    /**
     * Constructs a LoginControllerImpl with a reference to the CafeSystem.
     * @param says that it is a parameter
     * @param cafeSystem The application's core system (Model).
     */
    public LoginControllerImpl(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
    }

    /**
     * Handles the login attempt from the LoginView.
     * Delegates authentication to the CafeSystem and orchestrates view switching.
     * @param stage The primary stage of the application.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param errorLabel The UI label to display error messages.
     */
    @Override
    public void handleLogin(Stage stage, String username, String password, Label errorLabel) {
        if (cafeSystem.login(username, password)) {
            UserRole role = cafeSystem.getCurrentUserRole();
            View nextView;
            // Determine which view to show based on the user's role
            switch (role) {
                case CUSTOMER: 
                    nextView = new CustomerOrderingView(cafeSystem);
                    break;
                case BARISTA:
                    nextView = new BaristaFulfillmentView(cafeSystem);
                    break;
                case MANAGER:
                    nextView = new ManagerOperationsView(cafeSystem);
                    break;
                default:
                    // Fallback to HelloView if role is not recognized
                    nextView = new HelloView(cafeSystem);
                    break;
            }
            // Switch to the appropriate view
            ViewSwitcher.switchView(stage, nextView.createView(stage));
        } else {
            // Display login failure message in the UI
            errorLabel.setText("Login failed. Try again.");
        }
    }
}

