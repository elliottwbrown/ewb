package com.ewb.OSInteraction;

public class GoodWindowsExec extends Thread {

    public static void main(String args[]) {
        String commands = "java -jar theFlopUtilities.jar ALL";
        //runCommand("", commands);
    }

    public static void runProgram(String command,String logLocation) {
        try {
            String osName = System.getProperty("os.name");
            String[] cmd = new String[3];
            System.out.println(osName);
            if (osName.equals("Windows NT")
                    || osName.equals("Windows XP")
                    || osName.equals("Windows 7")
                    || osName.equals("Windows Server 2008 R2")
                    || osName.equals("Windows Server 2008")) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = command;
            } else if (osName.equals("Windows 95")) {
                cmd[0] = "command.com";
                cmd[1] = "/C";
                cmd[2] = command;
            } else {
                System.out.println("not found: " + osName);
            }
            Runtime rt = Runtime.getRuntime();
            System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
            Process proc = rt.exec(cmd);
            StreamGobbler errorGobbler = new StreamGobbler(logLocation, proc.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(logLocation, proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void runCommand(String command,String logLocation) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            StreamGobbler errorGobbler = new StreamGobbler(logLocation, proc.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(logLocation, proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            //System.out.println("ExitValue: " + proc.waitFor());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}