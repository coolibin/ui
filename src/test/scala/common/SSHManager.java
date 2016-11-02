package common;

import com.jcraft.jsch.*;
import org.testng.annotations.Test;

import java.io.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by akalinichev on 7/7/16.
 */
public class SSHManager implements Closeable { //TODO: convert to Scala

    private static final org.slf4j.Logger log = LogManager.getLogger(SSHManager.class.getName());
    private JSch jschSSHChannel;
    private String strUserName;
    private String strConnectionIP;
    private int intConnectionPort;
    private String strPassword;
    private Session sesConnection;
    private int intTimeOut;

    public SSHManager(
            String userName,
            String password,
            String connectionIP,
            String knownHostsFileName) {
        doCommonConstructorActions(userName, password,
                connectionIP, knownHostsFileName);
        intConnectionPort = 22;
        intTimeOut = 60000;
    }

    public SSHManager(
            String userName,
            String password,
            String connectionIP,
            String knownHostsFileName,
            int connectionPort) {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = connectionPort;
        intTimeOut = 60000;
    }

    public SSHManager(
            String userName,
            String password,
            String connectionIP,
            String knownHostsFileName,
            int connectionPort,
            int timeOutMilliseconds) {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = connectionPort;
        intTimeOut = timeOutMilliseconds;
    }

    private void doCommonConstructorActions(
            String userName,
            String password,
            String connectionIP,
            String knownHostsFileName) {

        jschSSHChannel = new JSch();

        if (!knownHostsFileName.isEmpty()) {
            try {
                jschSSHChannel.setKnownHosts(knownHostsFileName);

            } catch (JSchException jschX) {
                logError(jschX.getMessage());
            }
        }

        strUserName = userName;
        strPassword = password;
        strConnectionIP = connectionIP;
    }

    public String connect() {
        String errorMessage = null;

        try {
            sesConnection = jschSSHChannel.getSession(
                    strUserName, strConnectionIP, intConnectionPort);
            sesConnection.setPassword(strPassword);
            sesConnection.setConfig("StrictHostKeyChecking", "no");
            sesConnection.connect(intTimeOut);
        } catch (JSchException jschX) {
            errorMessage = jschX.getMessage();
        }

        return errorMessage;
    }

    public void close() {
        sesConnection.disconnect();
    }

    private String logError(String errorMessage) {
        if (errorMessage != null) {
            log.error(String.format("%s:%s - %s",
                    strConnectionIP, intConnectionPort, errorMessage));
        }

        return errorMessage;
    }

    private String logWarning(String warnMessage) {
        if (warnMessage != null) {
            log.warn(String.format("%s:%s - %s",
                    strConnectionIP, intConnectionPort, warnMessage));
        }

        return warnMessage;
    }

    public String sendCommand(String command) {
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = sesConnection.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        } catch (IOException ioX) {
            logWarning(ioX.getMessage());
            return null;
        } catch (JSchException jschX) {
            logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    /**
     * Method copies a file from local machine to a remote
     * It returns error > 0 if something went wrong (like permission denied)
     *
     * @param fileFrom
     * @param fileTo
     * @return
     */
    public int sshUploadFileToServer(String fileFrom, String fileTo) {
        int result = 0;
        ChannelSftp sftpChannel = null;
        OutputStream fos = null;
        InputStream fis = null;

        try {
            log.debug("Creating SFTP Channel...");
            sftpChannel = (ChannelSftp) sesConnection.openChannel("sftp");
            sftpChannel.connect();
            log.debug("SFTP Channel created.");

            fos = sftpChannel.put(fileTo);
            fis = new FileInputStream(fileFrom);

            byte[] buffer = new byte[1024 * 4];
            int noOfBytes;

            log.debug("Copying file...");

            while ((noOfBytes = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, noOfBytes);
            }

            fos.flush();

        } catch (IOException ioX) {
            logWarning(ioX.getMessage());
            return 1;
        } catch (SftpException | JSchException jschX) {
            logWarning(jschX.getMessage());
            return 2;
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            result = 4;
        } finally {

            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                log.error("Error while closing stream: " + ioe);
            }

            sftpChannel.disconnect();
        }
        return result;
    }


    public int sshDownloadFileFromServer(String fileFrom, String fileTo) {
        int result = 0;
        OutputStream fos = null;
        InputStream fis = null;
        ChannelSftp sftpChannel = null;

        try { //todo refactor to try with resources
            log.debug("Creating SFTP Channel...");
            sftpChannel = (ChannelSftp) sesConnection.openChannel("sftp");
            sftpChannel.connect();
            log.debug("SFTP Channel created.");

            fis = sftpChannel.get(fileFrom);
            fos = new FileOutputStream(fileTo);
            byte[] buffer = new byte[1024 * 4];
            int noOfBytes;

            log.debug("Downloading a file...");

            while ((noOfBytes = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, noOfBytes);
            }

            fos.flush();
        } catch (IOException ioX) {
            logWarning(ioX.getMessage());
            return 1;
        } catch (SftpException | JSchException jschX) {
            logWarning(jschX.getMessage());
            return 2;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

                if (fis != null) {
                    fis.close();
                }

            } catch (IOException ioe) {
                log.error("Error while closing a stream: " + ioe);
            }

            sftpChannel.disconnect();
        }
        return result;
    }


    @Test
    public static void testCopyFileToServer() {
        System.out.println("sshUploadFileToServer");

        String fileTo = "/data/prod/ode/staging/testFromClient.txt";
        String fileFrom = "/Users/akalinichev/test.txt";

        String userName = AppConfig.getString("nateraqa.login.username", null).split("@")[0];
        String password = AppConfig.getString("nateraqa.login.password", null);
        String connectionIP = "10.20.100.64";
        SSHManager instance = new SSHManager(userName, password, connectionIP, "");
        String errorMessage = instance.connect();

        if (errorMessage != null) {
            System.out.println(errorMessage);
            fail();
        }

        int expResult = 0;
        int result = instance.sshUploadFileToServer(fileFrom, fileTo);

        instance.close();
        assertEquals(expResult, result);
    }


    @Test
    public static void testCopyFileFromServer() {
        System.out.println("sshDownloadFileFromServer");

        String fileFrom = "/data/prod/ode/staging/test.txt";
        String fileTo = "/Users/akalinichev/test.txt";

        String userName = AppConfig.getString("nateraqa.login.username", null).split("@")[0];
        String password = AppConfig.getString("nateraqa.login.password", null);
        String connectionIP = "10.20.100.64";
        SSHManager instance = new SSHManager(userName, password, connectionIP, "");
        String errorMessage = instance.connect();

        if (errorMessage != null) {
            System.out.println(errorMessage);
            fail();
        }

        int expResult = 0;
        int result = instance.sshDownloadFileFromServer(fileFrom, fileTo);

        instance.close();
        assertEquals(expResult, result);

    }

    @Test
    public static void testSendCommand() {
        System.out.println("sendCommand");

        /**
         * YOU MUST CHANGE THE FOLLOWING
         * FILE_NAME: A FILE IN THE DIRECTORY
         * USER: LOGIN USER NAME
         * PASSWORD: PASSWORD FOR THAT USER
         * HOST: IP ADDRESS OF THE SSH SERVER
         **/
        String command = "ls /data/prod/ode/staging/test.txt";
        String userName = AppConfig.getString("nateraqa.login.username", null).split("@")[0];
        String password = AppConfig.getString("nateraqa.login.password", null);
        String connectionIP = "10.20.100.64";
        SSHManager instance = new SSHManager(userName, password, connectionIP, "");
        String errorMessage = instance.connect();

        if (errorMessage != null) {
            System.out.println(errorMessage);

            fail();
        }

        String expResult = "/data/prod/ode/staging/test.txt\n";
        // call sendCommand for each command and the output
        //(without prompts) is returned
        String result = instance.sendCommand(command);
        // close only after all commands are sent
        instance.close();
        assertEquals(expResult, result);
    }

}
