package services;

import entities.Student;
import ftp.FTPClient;
import ftp.exceptions.FTPDataTransferException;
import ftp.exceptions.FTPException;
import ftp.exceptions.FTPIllegalReplyException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StudentServiceTests {
    private static final String TEST_STUDENT_DATA_FTP_FILEPATH = "students_test.json";
    private StudentService studentService;
    private FTPClient ftpClient;

    @BeforeMethod
    @Parameters({"serverIp", "username", "password"})
    public void setUp(String serverIp, String username, String password) throws FTPIllegalReplyException, IOException, FTPException, FTPDataTransferException {
        // Connect to the FTP server and login
        ftpClient = new FTPClient();
        ftpClient.connect(serverIp);
        ftpClient.login(username, password);

        // Create an empty student data file
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ftpClient.uploadTextualData(TEST_STUDENT_DATA_FTP_FILEPATH, emptyInputStream);

        studentService = new StudentService(ftpClient);
    }

    @AfterMethod
    public void tearDown() throws FTPIllegalReplyException, FTPDataTransferException, IOException, FTPException {
        // Delete the contents of the student data file
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ftpClient.uploadTextualData(TEST_STUDENT_DATA_FTP_FILEPATH, emptyInputStream);

        // Disconnect from the FTP server
        try {
            ftpClient.disconnect();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void Create_student_successful() throws Exception {
        Student student = studentService.createStudent("Mario");
        studentService.downloadStudentData();
        Assert.assertTrue(studentService.getLocalStudentData().containsKey(student.getId()));
    }

    @Test
    public void Create_multiple_students_successful() throws Exception {
        Student student1 = studentService.createStudent("Mario");
        Student student2 = studentService.createStudent("Luigi");
        studentService.downloadStudentData();
        Assert.assertTrue(studentService.getLocalStudentData().containsKey(student1.getId()));
        Assert.assertTrue(studentService.getLocalStudentData().containsKey(student2.getId()));
    }

    @Test
    public void Remove_student_successful() throws Exception {
        Student student = studentService.createStudent("Mario");
        studentService.removeStudent(student.getId());
        studentService.downloadStudentData();
        Assert.assertFalse(studentService.getLocalStudentData().containsKey(student.getId()));
    }
}
