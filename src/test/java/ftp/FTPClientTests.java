package ftp;

import ftp.exceptions.FTPDataTransferException;
import ftp.exceptions.FTPException;
import ftp.exceptions.FTPIllegalReplyException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class FTPClientTests {
    private final String serverIp;
    private final String username;
    private final String password;
    private FTPClient ftpClient;

    @Parameters({"serverIp", "username", "password"})
    public FTPClientTests(String serverIp, String username, String password) {
        this.serverIp = serverIp;
        this.username = username;
        this.password = password;
    }

    @BeforeMethod
    public void setUp() {
        ftpClient = new FTPClient();
    }

    @AfterMethod
    public void tearDown() {
        try {
            ftpClient.disconnect();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void Connect_to_existing_server_successful() throws FTPIllegalReplyException, IOException, FTPException {
        ftpClient.connect(serverIp);
        Assert.assertTrue(ftpClient.isConnected());
    }

    @Test
    public void Disconnect_after_connection_successful() throws FTPIllegalReplyException, IOException, FTPException {
        ftpClient.connect(serverIp);
        ftpClient.disconnect();
        Assert.assertFalse(ftpClient.isConnected());
    }

    @Test
    public void Login_with_correct_credentials_successful() throws FTPIllegalReplyException, IOException, FTPException {
        ftpClient.connect(serverIp);
        ftpClient.login(username, password);
        Assert.assertTrue(ftpClient.isAuthenticated());
    }

    @Test(expectedExceptions = FTPException.class)
    public void Login_with_incorrect_credentials_fails_with_exception() throws FTPIllegalReplyException, IOException, FTPException {
        ftpClient.connect(serverIp);
        ftpClient.login(username, "wrong_password");
    }

    @Test
    public void Upload_textual_data_successful() throws FTPIllegalReplyException, IOException, FTPException, FTPDataTransferException {
        ftpClient.connect(serverIp);
        ftpClient.login(username, password);
        String data = "Sample text data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        ftpClient.uploadTextualData("test.txt", inputStream);
        // at this point, the test should pass if no exceptions were thrown
    }

    @Test
    public void Download_textual_data_successful() throws FTPIllegalReplyException, IOException, FTPException, FTPDataTransferException {
        ftpClient.connect(serverIp);
        ftpClient.login(username, password);
        String data = "Sample text data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        ftpClient.uploadTextualData("test.txt", inputStream);
        OutputStream outputStream = new ByteArrayOutputStream();
        ftpClient.downloadTextualData("test.txt", outputStream);
        String downloadedData = outputStream.toString();
        Assert.assertEquals(downloadedData, data);
    }

    @Test(expectedExceptions = FTPException.class)
    public void Download_nonexistent_textual_data_fails_with_exception() throws FTPIllegalReplyException, IOException, FTPException, FTPDataTransferException {
        ftpClient.connect(serverIp);
        ftpClient.login(username, password);
        OutputStream outputStream = new ByteArrayOutputStream();
        ftpClient.downloadTextualData("nonexistent.txt", outputStream);
    }
}