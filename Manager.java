package com.cs308.cs308usms2023;
import java.util.*;

public class Manager extends User {
    public ArrayList<Module> moduleList;
    public Manager(String userID, String firstName, String surname, String emailAddress,
                   String userType,String gender, String dateOfBirth, String password, String managerID,Boolean active) {
        super(userID, firstName, surname, emailAddress, userType,gender,dateOfBirth, password, managerID,active);
        this.moduleList = new ArrayList<>();
    }

//    As a manager I want to be able to view sign up workflow | manager-approve-user
//    As a manager I want to be able to approve students who signed up to create their | manager-approve-user
//    As a manager I want to be able to approve lecturers who signed up to create their accounts | manager-approve-user
//    As a manager I want to be able to manager others accounts for example activate, deactivate,password reset | manager-manager-account
//    As a manager I want to be able to display course details (semesters and modules) | manager-view-course
//    As a manager I want to be able to display module details | manager-view-module
//    As a manager I want to be able to add a new course | manager-new-course
//    As a manager I want to be able to add a new module | manager-new-module
//    As a manager I want to be able to update course information | manager-update-course
//    As a manager I want to be able to issue a decision for a student, i.e (award, resit, withdraw) | manager-student-decision
//    As a manager I want to be able to add a business rule such as (maximum number of student attempts, number of modules that are allowed to be compensated) | manager-add-business-rule
//    As a manager I want to be able to assign module to a course so that this course will include this module | manager-assign-module-course
//    As a manager I want to be able to assign a module to a given lecturer | manager-assign-module
//    As a manager I want to be able to enrol a student in a given course | manager-enroll-student

    // methods will be available if user has signed in as a manager

    // print all users waiting to be accepted or declined
    public Boolean viewWorkflow(Map<String,UserSignup> userSignupMap) {
        for (Map.Entry<String, UserSignup> entry : userSignupMap.entrySet()) {
            UserSignup userId = entry.getValue();
            userId.printUserDetails();
            return true;
        }
        return false;
    }

    // could make a return so that an array is given and printed
    public Boolean approveUser(String selectedUser, Map<String,UserSignup> userSignupMap, Map<String,UserSignup> userMap) {
        if (userSignupMap.containsKey(selectedUser)) {
            if (userMap.containsKey(selectedUser)) {
                return false;
            }
            else {
                UserSignup approvedUser = userSignupMap.get(selectedUser);
                userMap.put(selectedUser, approvedUser);
                return true;
            }
        }
        else {
            return false;
        }
    }

    // deactivate account takes in user id then sets the active flag to false
    public boolean deactivateAccount(String removeUser, Map<String, User> userMap) {
        if (userMap.containsKey(removeUser)) {
            User user = userMap.get(removeUser);
            user.setActive(false);
            return true;
        } else {
            return false;
        }
    }

    // activate account takes in user id and sets the active flag to true
    public boolean activateAccount(String addUser, Map<String, User> userMap) {
        if (userMap.containsKey(addUser)) {
            User user = userMap.get(addUser);
            user.setActive(true);
            return true;
        } else {
            return false;
        }
    }

    // method to set password of a user who has been locked out of their account to a default password
    public Boolean passwordReset(String lockedOutUserId, Map<String,User> userMap, String password) {
        if (userMap.containsKey(lockedOutUserId)) {
            User userPasswordReset = userMap.get(lockedOutUserId);
            userPasswordReset.setPassword(password);
            return true;
        }
        else {
            return false;
        }
    }

    // add a course to the courseMap
    public Boolean addCourse(Course course, Map<String,Course> courseMap) {
        if (courseMap.containsKey(course.getCourseID())) {
            return false;
        }
        else {
            courseMap.put(course.getCourseID(), course);
            return true;
        }
    }

    // add a module to the moduleMap
    public Boolean addModule(Module module, Map<String,Module> moduleMap) {
        if (moduleMap.containsKey(module.getModuleID())) {
            return false;
        }
        else {
            moduleMap.put(module.getModuleID(), module);
            return true;
        }
    }

    // method that gets old course and updates it with data from the new course
    public Boolean updateCourse(Course course, Map<String,Course> courseMap) {
        if (courseMap.containsKey(course.getCourseID())) {
            Course badCourse = courseMap.get(course.getCourseID());
            badCourse.setDepartmentID(course.getDepartmentID());
            badCourse.setCourseName(course.getCourseName());
            badCourse.setLevel(course.getLevel());
            return true;
        }
        else {
            return false;
        }
    }

    // method to update the amount of attempts the user has at module
    public Boolean addBusinessRule(String moduleID, int newAttempts, Map<String,Module> moduleMap) {
        if (moduleMap.containsKey(moduleID)) {
            Module moduleToUpdate = moduleMap.get(moduleID);
            moduleToUpdate.setMaxAttempts(newAttempts);
            return true;
        }
        else {
            return false;
        }
    }

    private Boolean checkAllPass(ArrayList<String> arr){
        for(String value : arr){
            if(!value.equals("Pass")){
                return false;
            }
        }
        return true;
    }

    // set decision for a student's course record
    public Integer issueDecision(Map<String, Course> courseMap, String moduleID,String studentID, StudentCourseRecord courseRecord,StudentModuleRecord moduleRecord) {
        ArrayList<String> passList = new ArrayList<>();
        for (int i = 0; i < courseMap.size(); i++) {
            int maxAttempts = courseMap.get(courseRecord.getCourseID()).getMaxAttemptsOfModules();
            if(courseMap.containsKey(courseRecord.courseID) &&
               Objects.equals(courseMap.get(courseRecord.courseID).studentsOnCourse.get(i).getUserID(), studentID)){
                passList.add(moduleRecord.getPass());
            }
        }

        if (checkAllPass(passList)){
            courseRecord.setDecision("Pass");
            return 1;
        } else if (moduleRecord.getStudentAttempt() < courseMap.get(courseRecord.getCourseID()).modules.get(moduleID).getMaxAttempts()){
            courseRecord.setDecision("Resit");
            return 2;
        } else {
            courseRecord.setDecision("Withdraw");
            return 3;
        }
    }

    // method that takes module and course id's then assign module to course
    public Boolean assignModuleToCourse(String moduleId, String courseId, Map<String,Module> moduleMap, Map<String,Course> courseMap) {
        if (moduleMap.containsKey(moduleId) && courseMap.containsKey(courseId)) {
            Module module = moduleMap.get(moduleId);
            Course course = courseMap.get(courseId);
            course.addModule(module);
            return true;
        } else {
            return false;
        }
    }

    // method to assign a lecturer to a module
    public Boolean assignLecturerToModule(Lecturer lecturer , String moduleId, Map<String,Module> moduleMap, Map<String,User> userMap) {
        if (moduleMap.containsKey(moduleId)) {
            if (userMap.containsKey(lecturer.getUserID())) {
                Module module = moduleMap.get(moduleId);
                module.addLecturer(lecturer);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    // method to add pre-existing student to a course
    public Boolean enrollStudent(String courseId, Student student, Map<String,Course> courseMap, Map<String,User> userMap) {
        if (courseMap.containsKey(courseId)) {
            if (userMap.containsKey(student.getUserID())) {
                Course course = courseMap.get(courseId);
                course.addStudent(student);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}