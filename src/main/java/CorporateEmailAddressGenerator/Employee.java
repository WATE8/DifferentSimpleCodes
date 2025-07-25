package CorporateEmailAddressGenerator;

public class Employee {
    private String firstName;
    private String lastName;
    private String department;
    private String email;
    private String password;

    public Employee(String firstName, String lastName, String department, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public String getDepartment() {
        return department;
    }
}
