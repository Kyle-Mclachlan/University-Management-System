import com.cs308.cs308usms2023.Module;
import com.cs308.cs308usms2023.User;
import com.cs308.cs308usms2023.UserSignup;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.TestCase.*;

public class ManagerTest extends SetUpTest{

    @Test
    public void approveUserTest() {
        Map<String, UserSignup> userMap = new HashMap<>();
        Map<String, UserSignup> userSignupMap = new HashMap<>();
        UserSignup testUser1 = new UserSignup("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "1");
        userMap.put("1", testUser1);

        // test for when the userid is in the signup map and not in the user map
        assertFalse(manager.approveUser("1", userSignupMap, userMap));

        UserSignup testUser2 = new UserSignup("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "1");
        userMap.put("1", testUser2);

        // test for when the userid is in the signup map an is also in the user map
        assertFalse(manager.approveUser("2", userSignupMap, userMap));

        // test for when the userid is not in the signup map
        assertFalse(manager.approveUser("3", userSignupMap, userMap));
    }

    @Test
    public void deactivateAccountTest() {
        Map<String, User> userMap = new HashMap<>();
        User testUser = new User("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "5", true);
        userMap.put("101", testUser);


        // test for when the userid is not in the user map
        assertFalse(manager.deactivateAccount("notInMap", userMap));

        // test for when the userid is in the user map and can be deactivated
        assertTrue (manager.deactivateAccount("101", userMap));
    }

    @Test
    public void activateAccountTest() {
        Map<String, User> userMap = new HashMap<>();
        User testUser = new User("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "5", true);
        userMap.put("101", testUser);
        manager.deactivateAccount("101", userMap);

        // test for when the userid is not in the user map
        assertFalse (manager.activateAccount("no", userMap));

        // test for when the userid is in the user map and is deactivated, so it can be reactivated
        assertTrue (manager.activateAccount("101", userMap));
    }

    @Test
    public void passwordResetTest() {
        Map<String, User> userMap = new HashMap<>();
        User testUser = new User("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "5", true);
        userMap.put("101", testUser);

        // test for when the userId of the locked out user is not in the userMap
        assertFalse (manager.passwordReset("no", userMap, "newPassword"));

        // test for when the userid is in the user map, their password will be reset
        assertTrue (manager.passwordReset("101", userMap, "newPassword"));
    }

    @Test
    public void addCourseTest() {
        Map<String,Course> courseMap = new HashMap<>();
        Course course = new Course("compSci", "CIS", "compSci", "3", "computers",
                2, 3);

        // test for adding a new course to the course map
        assertTrue (manager.addCourse(course, courseMap));

        // test for adding a course that is already present to the course map
        assertFalse (manager.addCourse(course, courseMap));
    }

    @Test
    public void addModuleTest() {
        Map<String, Module> moduleMap = new HashMap<>();
        Module module = new Module("cs308", "CIS", "Software Systems", 20, 2, "java");

        // test for adding a new module to the module map
        assertTrue (manager.addModule(module, moduleMap));

        // test for adding a module that is already present to the module map
        assertFalse (manager.addModule(module, moduleMap));
    }

    @Test
    public void updateCourseTest() {
        Map<String,Course> courseMap = new HashMap<>();
        Course course = new Course("compSci", "CIS", "compSci", "3", "computers",
                2, 3);
        Course newCourse = new Course("compSci", "CIS", "compSci2", "3", "computers2",
                3, 3);
        Course badCourse = new Course("law", "Business", "law", "3", "gavels and podiums",
                3, 3);
        courseMap.put("compSci", course);

        // test for when the course doesn't exist to update
        assertFalse (manager.updateCourse(badCourse, courseMap));

        // test for when the correct course is updated
        assertTrue (manager.updateCourse(newCourse, courseMap));
    }

    @Test
    public void addBusinessRuleTest() {
        Map<String, Module> moduleMap = new HashMap<>();
        Module module = new Module("cs308", "CIS", "Software Systems", 20, 2, "java");
        moduleMap.put("cs308", module);

        // test for when the module is not in the module map
        assertFalse (manager.addBusinessRule("no", 3, moduleMap));

        // test for when the module is in map, this updates business rule
        assertTrue (manager.addBusinessRule("cs308", 3, moduleMap));
    }

    @Test
    public void assignModuleToCourseTest() {
        Map<String, Module> moduleMap = new HashMap<>();
        Map<String,Course> courseMap = new HashMap<>();
        Module module = new Module("cs308", "CIS", "Software Systems", 20, 2, "java");
        Course course = new Course("compSci", "CIS", "compSci", "3", "computers",
                2, 3);

        // test for when module is not present in the module map
        assertFalse(manager.assignModuleToCourse("cs308", "compSci", moduleMap, courseMap));
        moduleMap.put("cs308", module);
        // test for when course is not in the course map
        assertFalse(manager.assignModuleToCourse("cs308", "compSci", moduleMap, courseMap));

        courseMap.put("compSci", course);
        // test for when both module and course are in their respective maps
        assertTrue(manager.assignModuleToCourse("cs308", "compSci", moduleMap, courseMap));
    }

    @Test
    public void assignLecturerToModuleTest() {
        Map<String, Module> moduleMap = new HashMap<>();
        Map<String, User> userMap = new HashMap<>();
        Module module = new Module("cs308", "CIS", "Software Systems", 20, 2, "java");
        Lecturer lecturer = new Lecturer("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "5", "5", true);
        User user = new User("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "5", true);

        // test for when module is not present in the module map
        assertFalse(manager.assignLecturerToModule(lecturer, "cs308", moduleMap, userMap));

        moduleMap.put("cs308", module);
        // test for when lecturer is not in the user map
        assertFalse(manager.assignLecturerToModule(lecturer, "cs308", moduleMap, userMap));

        userMap.put("101", user);
        // test for when both module and lecturer are in their respective maps
        assertTrue(manager.assignLecturerToModule(lecturer, "cs308", moduleMap, userMap));
    }

    @Test
    public void enrollStudentTest() {
        Map<String,Course> courseMap = new HashMap<>();
        Map<String, User> userMap = new HashMap<>();
        Course course = new Course("compSci", "CIS", "compSci", "3", "computers",
                2, 3);
        Student student = new Student("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "5", true);
        User user = new User("101", "Lewis", "Brindley", "l.b@mail.com", "Student",
                "Male", "2003/02/12", "123", "5", true);

        // test for when course is not present in the course map
        assertFalse(manager.enrollStudent("compSci", student, courseMap, userMap));

        courseMap.put("compSci", course);
        // test for when the student is not in the user map
        assertFalse(manager.enrollStudent("compSci", student, courseMap, userMap));

        userMap.put("101", user);
        // test for when both course and student are in their respective maps
        assertTrue(manager.enrollStudent("compSci", student, courseMap, userMap));
    }
}
