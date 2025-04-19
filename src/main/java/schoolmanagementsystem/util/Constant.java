package schoolmanagementsystem.util;

import java.security.PublicKey;

public class Constant {

    private Constant() {
    }

    public static final String CREATED = "Created Successfully";
    public static final String RETRIEVED = "Retrieved Successfully";
    public static final String UPDATED = "Updated Successfully";
    public static final String DELETED = "Deleted Successfully";
    public static final String USER_REGISTERED = "User registered successfully!";
    public static final String ID_DOES_NOT_EXIST = "ID Does not Exist";
    public static final String TUTOR_ID_NOT_FOUND = "Tutor ID not found";
    public static final String SCHOOL_ID_NOT_FOUND = "School ID not fount";
    public static final String STUDENT_ID_NOT_FOUNT = "Student ID not found";
    public static final String COURSE_ID_NOT_FOUND = "course ID not found";
    public static final String ENROLLED = "Enrolled";
    public static final String SALARY_INCREMENT = "Salary increment must be greater than zero";
    public static final String NO = "no";
    public static final String COURSE_NAME_REQUIRED = "Course name is required";
    public static final String USERNAME_EXIT = "Error: Username is already taken!";
    public static final String EMAIL_EXIT = "Error: Email is already in use!";
    public static final String ROLE_NOT_FOUND = "Error: Role is not found.";
    public static final String USERNAME_NOT_FOUND = "User Not Found with username: ";
    public static final String TEACHER_ACCESS = "teacher can access.";
    public static final String STUDENT_ACCESS = "student can access.";
    public static final String ADMIN_ACCESS = "admin can access.";
    public static final String MODERATOR_ACCESS = "moderator can access.";
    public static final String USER_ACCESS = "user can access.";
    public static final String PUBLIC_ACCESS = "public can access.";
    public static final String CANNOT_SET_USER_AUTHENTICATE = "Cannot set user authentication: {}";
    public static final String INVALID_TOKEN = "Invalid JWT token: {}";
    public static final String TOKEN_EXPIRED = "JWT token is expired: {}";
    public static final String TOKEN_UNSUPPORTED = "JWT token is unsupported: {}";
    public static final String JWT_CLAIMS_EMPTY = "JWT claims string is empty: {}";
    public static final String UNAUTHORIZED_ERROR = "Unauthorized error: {}";
    public static final String SCHOOL_SERVICE_STARTED = "School Service Test case execution has been started";
    public static final String SCHOOL_SERVICE_FINISHED = "School Service Test case has been execution finished";
    public static final String SCHOOL_CONTROLLER_STARTED = "School Controller Test case execution has been started";
    public static final String SCHOOL_CONTROLLER_FINISHED = "School Controller Test case execution has been finished";
    public static final String STUDENT_SERVICE_STARTED = "Student Service Test case execution has been started";
    public static final String STUDENT_SERVICE_FINISHED = "Student Service Test case execution has been finished";
    public static final String TUTOR_SERVICE_STARTED = "Tutor Service Test case execution has been started";
    public static final String TUTOR_SERVICE_FINISHED = "Tutor Service Test case execution has been finished";
    public static final String COURSE_SERVICE_STARTED = "Course Service test case execution has been started";
    public static final String COURSE_SERVICE_FINISHED = "Course Service test case execution has been finished";
}
