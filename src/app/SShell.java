package app;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.mozilla.universalchardet.*;
import org.apache.commons.codec.binary.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

public class SShell
{
    public static Map<String, String> MEM_SPACE = new HashMap<String, String>();
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String SLASH_TYPE = "?";
    public static String FAKE_SLASH_TYPE = "?";
    public static boolean isLinux = false;
    public static final String LINUX_USERDIRECTORY = System.getenv("HOME");
    public static String CURRENT_DIRECTORY = System.getenv("USERPROFILE");
    public static final String USERDIRECTORY = System.getenv("USERPROFILE");
    public static final String SYSTEM = System.getProperty("os.name");
    public static final String[] ILLEGAL_SYMBOL = "!#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ ".split("");
    public static List<String> portAccepted = new ArrayList<String>();
    public static List<File> subDir = new ArrayList<File>();

    public class readtype
    {
        public static final String STRING = "default";
        public static final String BYTE_TO_HEX = "byteToHex";
    }
    
    public class protosock
    {
        public static final String TCP = ".tcp_proto";
        public static final String UDP = ".udp_proto";
    }

    public class error
    {
        public static final String INSERT_PARAMETER = ansi.RED_BOLD + "[!]" + ansi.WHITE_BOLD + " Insert parameter."; 
        public static final String INVALID_PARAMETER = ansi.RED_BOLD + "[!]" + ansi.WHITE_BOLD + " Invalid parameter.";
    }

    public class log
    {
        public static final String INFOREGULAR = ansi.BLUE_BOLD + "[!] " + ansi.WHITE_BOLD;
        public static final String INFORMATION = ansi.GREEN_BOLD + "[!] " + ansi.WHITE_BOLD;
        public static final String ERROR = ansi.RED_BOLD + "[!] " + ansi.WHITE_BOLD;
    }

    public class ansi
    {
        public static final String NO_COLOR = "\033[0m";
        public static final String BLACK_BOLD = "\033[1;30m"; 
        public static final String RED_BOLD = "\033[1;31m";    
        public static final String GREEN_BOLD = "\033[1;32m";  
        public static final String YELLOW_BOLD = "\033[1;33m"; 
        public static final String BLUE_BOLD = "\033[1;34m";   
        public static final String PURPLE_BOLD = "\033[1;35m"; 
        public static final String CYAN_BOLD = "\033[1;36m";   
        public static final String WHITE_BOLD = "\033[1;37m";
        public static final String ORANGE_BOLD = "\033[1;38;5;208m";
    }  
    public static void main(String[] args) throws Exception
    {   
        try 
        {
            screenClear();

            if (SYSTEM.contains("Windows"))
            {
                SLASH_TYPE = "\\";
                FAKE_SLASH_TYPE = "/";
            } else if (SYSTEM.contains("Linux")) 
            {
                isLinux = true;
                SLASH_TYPE = "/";
                FAKE_SLASH_TYPE = "\\";
            } else if (SYSTEM.contains("Mac")) 
            {
                SLASH_TYPE = "/";
                FAKE_SLASH_TYPE = "\\";
            } else
            {
                exiter(1);
            }
                
            if (CURRENT_DIRECTORY == null) 
            {
                CURRENT_DIRECTORY = LINUX_USERDIRECTORY;
            }
                
            setCurrentDirectory(CURRENT_DIRECTORY, SLASH_TYPE, FAKE_SLASH_TYPE);   
            
            if (accountExists(CURRENT_DIRECTORY, SLASH_TYPE))
            {
                if (login(CURRENT_DIRECTORY, SLASH_TYPE))
                {   

                } else {
                    exiter(1);
                }
            } else {
                if (register(CURRENT_DIRECTORY, SLASH_TYPE, ""))
                {
                } else {
                    exiter(1);
                }
            }
            screenClear();
            while (true) 
                {    
                    try 
                    {
                        String LOCALUSERDOMAIN = System.getenv("USERDOMAIN");
                        if (isLinux) 
                        {
                          LOCALUSERDOMAIN = new File(LINUX_USERDIRECTORY).getName();  
                        }
                        final File LOCALPATH = new File(CURRENT_DIRECTORY);

                        String _command = "";
                        System.out.println(ansi.BLUE_BOLD + "| [" + ansi.WHITE_BOLD + LOCALUSERDOMAIN + SLASH_TYPE + LOCALPATH.getName() + ansi.BLUE_BOLD + "] - " + ansi.RED_BOLD + "SShell");
                        _command = input(ansi.BLUE_BOLD + "| > " + ansi.WHITE_BOLD);
                        System.out.println();

                        try {
                            _command.trim();
                        } catch (Exception e) {
                            System.out.println();
                            _command = "";
                        }

                        String[] COMMAND = _command.trim().split(" ");

                        switch (COMMAND[0].toLowerCase())
                        {
                            case "":
                                break;
                            case "exit":
                                exiter(0);
                                break;
                                
                            case "clr":
                                screenClear();
                                break;

                            case "user":
                                if (COMMAND.length == 2) 
                                {
                                    if (COMMAND[1].equals("--reset") || COMMAND[1].equals("-r")) 
                                    {
                                        String _registerDirectory = USERDIRECTORY;
                                        if (isLinux) 
                                        {
                                            _registerDirectory = LINUX_USERDIRECTORY;
                                        }
                                        boolean Verify = register(_registerDirectory, SLASH_TYPE, "reset");
                                        if (Verify)
                                        {
                                            System.out.println("\n" + log.INFORMATION + " Password changed successfull.\n");
                                        } else 
                                        {

                                        }
                                    } else {
                                        System.out.println(error.INVALID_PARAMETER + "\n");
                                    }
                                } else if (COMMAND.length > 2) 
                                {
                                    System.out.println(error.INVALID_PARAMETER + "\n");
                                } else {
                                    System.out.println(error.INSERT_PARAMETER + "\n");
                                }
                                break;
                            
                            case "cd":
                                if (COMMAND.length > 1) 
                                {
                                    boolean VerifyCD = setCurrentDirectory(formatQuest(COMMAND[1]), SLASH_TYPE, FAKE_SLASH_TYPE);
                                    if (VerifyCD) 
                                    {
                                        System.out.println(log.INFORMATION + "Directory changed successfull.\n");
                                    } else 
                                    {
                                        System.out.println(log.ERROR + "Directory "+ '"' + formatQuest((CURRENT_DIRECTORY + SLASH_TYPE + COMMAND[1]).replace(SLASH_TYPE + SLASH_TYPE, SLASH_TYPE)) + '"' + " not existing.\n");
                                    }
                                } else if (COMMAND.length > 2){
                                    System.out.println(error.INVALID_PARAMETER + "\n");
                                } else {
                                    System.out.println(log.INFORMATION + "Current directory: " + '"' + formatQuest(CURRENT_DIRECTORY) + '"' + ".\n");
                                }
                                break;
                            
                            case "ls":
                                if (COMMAND.length == 2) 
                                {
                                    if (COMMAND[1].equals("-s") || COMMAND[1].equals("--sub-directories")) 
                                    {
                                        System.out.print(log.INFORMATION + "Loading resources:");
                                        try 
                                        {
                                            getFileList(CURRENT_DIRECTORY, "all");
                                        } catch (Exception e) 
                                        {
                                            System.out.println("\r" + log.ERROR + "Failed to loading resource.\n");                                        
                                        }
                                        System.out.println("\r" + log.INFORMATION + "All " + '"' + CURRENT_DIRECTORY + '"' + " files:\n");
                                        long maxSize = 0;
                                        String nameSize = "";
                                        long totalDir = 0;
                                        long totalFile = 0;
                                        long allTotal = 0;
                                        String FileType;
                                        for (File sub : subDir) 
                                        {   
                                            try 
                                            {
                                                if (Files.size(Paths.get(sub.getAbsolutePath())) >= maxSize) 
                                                {
                                                    maxSize = Files.size(Paths.get(sub.getAbsolutePath()));
                                                    nameSize = sub.getAbsolutePath();
                                                }
                                                if (sub.isDirectory()) {
                                                    totalDir++;
                                                    FileType = ansi.PURPLE_BOLD + "DIRECTORY";
                                                } else {
                                                    totalFile++;
                                                    FileType = ansi.YELLOW_BOLD + "FILE";
                                                }
                                                System.out.println(log.INFOREGULAR + "Name: " + '"' + ansi.GREEN_BOLD + sub.getAbsolutePath() + ansi.WHITE_BOLD + '"' + ", Type: " + FileType + ansi.WHITE_BOLD + ", Size: " + ansi.GREEN_BOLD + Files.size(Paths.get(sub.getAbsolutePath())) + ansi.WHITE_BOLD + " byte.");
                                            } catch (FileSystemException e) {}
                                        }
                                        System.out.println("");
                                        allTotal = totalDir + totalFile;  
                                        System.out.println(log.INFORMATION + Long.toString(allTotal) + " object scanned:\n");
                                        System.out.println(log.INFOREGULAR + totalDir + ansi.PURPLE_BOLD + " DIR" + ansi.WHITE_BOLD + ", " + totalFile + ansi.YELLOW_BOLD + " FILE");
                                        System.out.println(log.INFOREGULAR + "The largest file is " + '"' + ansi.GREEN_BOLD + nameSize + ansi.WHITE_BOLD + '"' + " with " + maxSize + " byte.");
                                        System.out.println("");
                                        subDir.clear();
                                    } else {
                                        System.out.println(error.INVALID_PARAMETER + "\n");
                                    }
                                } else if (COMMAND.length > 2){
                                    System.out.println(error.INVALID_PARAMETER + "\n");
                                } else {
                                    System.out.print(log.INFORMATION + "Loading resources:");
                                    List<File> extractDir = new ArrayList<File>();
                                    try 
                                    {
                                        extractDir = getFileList(CURRENT_DIRECTORY, "");
                                        if (extractDir.isEmpty())
                                        {
                                            System.out.println("\r" + log.ERROR + "The directory is empty.\n");
                                            break;
                                        }
                                    } catch (Exception e) 
                                    {
                                        System.out.println("\r" + log.ERROR + "Failed to loading resource.\n");
                                        break;
                                    }
                                    System.out.println("\r" + log.INFORMATION + "All " + '"' + CURRENT_DIRECTORY + '"' + " files:\n");
                                    long maxSize = 0;
                                    String nameSize = "";
                                    long totalDir = 0;
                                    long totalFile = 0;
                                    long allTotal = 0;
                                    String FileType;
                                    for (File sub : extractDir) 
                                    {
                                        try 
                                        {
                                            if (Files.size(Paths.get(sub.getAbsolutePath())) >= maxSize) 
                                            {
                                                maxSize = Files.size(Paths.get(sub.getAbsolutePath()));
                                                nameSize = sub.getAbsolutePath();
                                            }
                                            if (sub.isDirectory()) {
                                                totalDir++;
                                                FileType = ansi.PURPLE_BOLD + "DIRECTORY";
                                            } else {
                                                totalFile++;
                                                FileType = ansi.YELLOW_BOLD + "FILE";
                                            }
                                            System.out.println(log.INFOREGULAR + "Name: " + '"' + ansi.GREEN_BOLD + sub.getAbsolutePath() + ansi.WHITE_BOLD + '"' + ", Type: " + FileType + ansi.WHITE_BOLD + ", Size: " + ansi.GREEN_BOLD + Files.size(Paths.get(sub.getAbsolutePath())) + ansi.WHITE_BOLD + " byte.");
                                        } catch (FileSystemException e) 
                                        {

                                        }
                                    }
                                    System.out.println("");
                                    allTotal = totalDir + totalFile;  
                                    System.out.println(log.INFORMATION + Long.toString(allTotal) + " object scanned:\n");
                                    System.out.println(log.INFOREGULAR + totalDir + ansi.PURPLE_BOLD + " DIR" + ansi.WHITE_BOLD + ", " + totalFile + ansi.YELLOW_BOLD + " FILE");
                                    System.out.println(log.INFOREGULAR + "The largest file is " + '"' + ansi.GREEN_BOLD + nameSize + ansi.WHITE_BOLD + '"' + " with " + maxSize + " byte.");
                                    System.out.println("");
                                }
                                break;

                            case "mk":
                                if (COMMAND.length == 3)
                                {
                                    try
                                    {
                                        if (COMMAND[1].equals("-f") || COMMAND[1].equals("--file"))
                                        {
                                            File file = new File(fileChoice(formatQuest(COMMAND[2])));
                                            if (file.createNewFile() == false)
                                            {
                                                throw new Exception();
                                            } else 
                                            {
                                                System.out.println(log.INFORMATION + "New file created in " + '"' + fileChoice(formatQuest(COMMAND[2])) + '"' + ".");
                                            }
                                        } else if (COMMAND[1].equals("-d") || COMMAND[1].equals("--dir") || COMMAND[1].equals("--directory"))
                                        {
                                            File file = new File(fileChoice(formatQuest(COMMAND[2])));
                                            if (file.mkdir() == false)
                                            {
                                                throw new Exception();
                                            } else 
                                            {
                                                System.out.println(log.INFORMATION + "New directory created in " + '"' + fileChoice(formatQuest(COMMAND[2])) + '"' + ".");
                                            }
                                        } else 
                                        {
                                            System.out.println(error.INVALID_PARAMETER); 
                                        }
                                    } catch (Exception e)
                                    {
                                        if (COMMAND[1].equals("-f") || COMMAND[1].equals("--file"))
                                        {
                                            System.out.println(log.ERROR + "The file may already exist.");
                                        } else if (COMMAND[1].equals("-d") || COMMAND[1].equals("--dir") || COMMAND[1].equals("--directory"))
                                        {
                                            System.out.println(log.ERROR + "The directory may already exist.");
                                        }
                                    } 
                                } else 
                                {
                                    if (COMMAND.length >= 4)
                                    {
                                        System.out.println(error.INVALID_PARAMETER);
                                    } else 
                                    {
                                        System.out.println(error.INSERT_PARAMETER);
                                    }
                                }
                                System.out.println();
                                break;

                            case "write":
                                if (COMMAND.length == 3 || COMMAND.length == 4)
                                {
                                    try 
                                    {
                                        if (!new File(fileChoice(formatQuest(COMMAND[1]))).exists())
                                        {
                                            System.out.println(log.ERROR + "File not existing.\n");
                                            break;
                                        }
                                        if (COMMAND.length == 3)
                                        {
                                            FileWriter file = new FileWriter(fileChoice(formatQuest(COMMAND[1])));
                                            file.write(formatQuest(COMMAND[2]));
                                            file.close();
                                            System.out.println(log.INFORMATION + "Writing done successfully.");
                                        } else 
                                        {
                                            if (COMMAND[2].equals("-b") || COMMAND[2].equals("--bytes"))
                                            {
                                                Path path = Paths.get(fileChoice(formatQuest(COMMAND[1])));
                                                Files.write(path, Hex.decodeHex(COMMAND[3]));
                                                System.out.println(log.INFORMATION + "Writing done successfully.");
                                            } else 
                                            {
                                                System.out.println(error.INVALID_PARAMETER);
                                            }
                                        }
                                    } catch (Exception e)
                                    {
                                        System.out.println(log.ERROR + "Writing failed.");
                                    }
                                } else 
                                {
                                    if (COMMAND.length <= 2)
                                    {
                                        System.out.println(error.INSERT_PARAMETER);
                                    } else 
                                    {
                                        System.out.println(error.INVALID_PARAMETER);
                                    }
                                }
                                System.out.println();
                                break;
                            case "del":
                                if (COMMAND.length == 2) 
                                {
                                    try 
                                    {
                                        if (!new File(fileChoice(formatQuest(COMMAND[1]))).exists()) 
                                        {
                                            if (new File(fileChoice(formatQuest(COMMAND[1]))).isFile())
                                            {
                                                System.out.println(log.ERROR + "File not existing.");
                                            } else 
                                            {
                                                System.out.println(log.ERROR + "Directory not existing.");
                                            } 
                                        }
                                        if (new File(fileChoice(formatQuest(COMMAND[1]))).delete())
                                        {
                                            if (new File(fileChoice(formatQuest(COMMAND[1]))).isFile())
                                            {
                                                System.out.println(log.INFORMATION + "File deleted.");
                                            } else 
                                            {
                                                System.out.println(log.INFORMATION + "Directory deleted.");
                                            } 
                                        } else 
                                        {
                                            throw new Exception();
                                        }
                                    } catch (Exception e) 
                                    {
                                        System.out.println(log.ERROR + "Deleting failed.");
                                    }
                                } else 
                                {
                                    if (COMMAND.length < 2)
                                    {
                                        System.out.println(error.INSERT_PARAMETER);
                                    } else
                                        System.out.println(error.INVALID_PARAMETER);
                                }
                                System.out.println();
                                break;
                            case "[dev]get_err_code":
                                if (COMMAND.length == 2) 
                                {
                                    try 
                                    {
                                        System.out.println(log.INFORMATION + crypter("decrypt", new String(Hex.decodeHex(COMMAND[1]), guessEncoder(Hex.decodeHex(COMMAND[1])))));
                                    } catch (Exception e) 
                                    {
                                        System.out.println(log.ERROR + "Get error code failed.");
                                    }
                                } else 
                                {
                                    if (COMMAND.length < 2)
                                    {
                                        System.out.println(error.INSERT_PARAMETER);
                                    } else
                                        System.out.println(error.INVALID_PARAMETER);
                                }
                                System.out.println();
                                break;

                            case "ren":
                                if (COMMAND.length == 3) 
                                {
                                    if (!new File(fileChoice(formatQuest(COMMAND[1]))).exists()) 
                                    {
                                        System.out.println(log.ERROR + "File not existing.\n");
                                        break;
                                    }
                                    if (new File(getParent(fileChoice(formatQuest(COMMAND[1]))) + SLASH_TYPE + new File(COMMAND[2]).getName()).exists()) 
                                    {
                                        System.out.println(log.ERROR + "The file may already exist.");
                                        break;
                                    }
                                    File file = new File(fileChoice(formatQuest(COMMAND[1])));
                                    file.renameTo(new File(getParent(fileChoice(formatQuest(COMMAND[1]))) + SLASH_TYPE + new File(COMMAND[2]).getName()));
                                    if (new File(fileChoice(formatQuest(COMMAND[1]))).isFile())
                                    {
                                        System.out.println(log.INFORMATION + "File renamed in " + '"' + getParent(fileChoice(formatQuest(COMMAND[1]))) + SLASH_TYPE + new File(COMMAND[2]).getName() + '"' + ".");
                                    } else 
                                    {
                                        System.out.println(log.INFORMATION + "Directory renamed in " + '"' + getParent(fileChoice(formatQuest(COMMAND[1]))) + SLASH_TYPE + (new File(COMMAND[2])).getName() + '"' + ".");
                                    }
                                } else if (COMMAND.length < 3) 
                                {
                                    System.out.println(error.INSERT_PARAMETER);
                                } else 
                                {
                                    System.out.println(error.INVALID_PARAMETER);
                                }
                                System.out.println();
                                break;

                            case "copy":
                            if (COMMAND.length == 3) 
                            {
                                if (!new File(fileChoice(formatQuest(COMMAND[1]))).exists()) 
                                {
                                    System.out.println(log.ERROR + "File not existing.\n");
                                    break;
                                }
                                if (new File(fileChoice(formatQuest(COMMAND[2]))).exists()) 
                                {
                                    System.out.println(log.ERROR + "The file may already exist.\n");
                                    break;
                                }
                                if (!new File(getParent(fileChoice(formatQuest(COMMAND[2])))).exists()) 
                                {
                                    System.out.println(log.ERROR + "Destination path not existing.\n");
                                    break;
                                }
                                Files.copy(new File(fileChoice(formatQuest(COMMAND[1]))).toPath(), new File(fileChoice(formatQuest(COMMAND[2]))).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                if (new File(fileChoice(formatQuest(COMMAND[1]))).isFile())
                                {
                                    System.out.println(log.INFORMATION + "File copied in " + '"' + fileChoice(formatQuest(COMMAND[2])) + '"' + ".");
                                } else 
                                {
                                    System.out.println(log.INFORMATION + "Directory copied in " + '"' + fileChoice(formatQuest(COMMAND[2])) + '"' + ".");
                                }
                            } else if (COMMAND.length < 3) 
                            {
                                System.out.println(error.INSERT_PARAMETER);
                            } else 
                            {
                                System.out.println(error.INVALID_PARAMETER);
                            }
                            System.out.println();
                            break;

                            case "mem":
                                if (COMMAND.length == 2 || COMMAND.length == 3)
                                {
                                    if (COMMAND[1].contains("-"))
                                    {  
                                        if (COMMAND[1].equals("--all") || COMMAND[1].equals("-a"))
                                        {
                                            try 
                                            {
                                                if (MEM_SPACE.isEmpty())
                                                {
                                                    System.out.println(log.ERROR + "No stored objects.");
                                                } else 
                                                {
                                                    System.out.println(log.INFORMATION + "Stored objects:\n");
                                                    for (String key : MEM_SPACE.keySet())
                                                    {
                                                        System.out.println(log.INFOREGULAR + key);
                                                    }
                                                }
                                            } catch (Exception e)
                                            {
                                                System.out.println(log.ERROR + "No stored objects.");
                                            }
                                        } else if (COMMAND.length == 3)
                                        {
                                            if (COMMAND[1].equals("--remove") || COMMAND[1].equals("-r"))
                                            {
                                                try
                                                {
                                                    if (MEM_SPACE.remove(COMMAND[2]).equals("null"))
                                                    {
                                                        System.out.println(log.ERROR + "Object not found.");
                                                    } else 
                                                    {
                                                        System.out.println(log.INFORMATION + "Object removed.");
                                                    }
                                                } catch (Exception e)
                                                {
                                                    System.out.println(log.ERROR + "Object not found.");
                                                }
                                            }
                                        } else 
                                        {
                                            System.out.println(error.INSERT_PARAMETER);
                                        }
                                    } else 
                                    {
                                        if (COMMAND.length == 2)
                                        {
                                            try 
                                            {
                                                if (MEM_SPACE.get(COMMAND[1]).equals("null"))
                                                {
                                                    System.out.println(log.ERROR + "Object not found.");
                                                } else 
                                                {
                                                    System.out.println(log.INFORMATION + "Object value:\n");
                                                    System.out.println(log.INFOREGULAR + MEM_SPACE.get(COMMAND[1]));  
                                                }
                                            } catch (Exception e) 
                                            {
                                                System.out.println(log.ERROR + "Object not found.");
                                            }
                                        } else 
                                        {
                                            try
                                            {
                                                if (!COMMAND[1].contains("-"))
                                                {
                                                    MEM_SPACE.put(COMMAND[1], formatQuest(COMMAND[2]));
                                                    System.out.println(log.INFORMATION + "Object added.");
                                                } else 
                                                {
                                                    System.out.println(error.INVALID_PARAMETER);
                                                }
                                            } catch (Exception e)
                                            {
                                                System.out.println(log.ERROR + "Failed to add object.");
                                            }
                                        }
                                    }   
                                } else if (COMMAND.length >= 4)
                                {
                                    System.out.println(error.INVALID_PARAMETER);
                                } else 
                                {
                                    System.out.println(error.INSERT_PARAMETER);
                                }
                                System.out.println();
                                break;

                            case "extract":
                                if (COMMAND.length == 2) 
                                {
                                    String fileText = readFile(fileChoice(formatQuest(COMMAND[1])), readtype.STRING);  
                                    if (fileText.equals("error")) 
                                    {
                                        System.out.println(log.ERROR + "File not existing.\n");
                                        break;
                                    }
                                    if (new File(fileChoice(formatQuest(COMMAND[1]))).exists() || new File(fileChoice(formatQuest(CURRENT_DIRECTORY + SLASH_TYPE + COMMAND[1]))).exists()) 
                                    {
                                        System.out.println(log.INFORMATION + "Text:\n");
                                        System.out.println(fileText + "\n");
                                    } 
                                } else 
                                {
                                    if (COMMAND.length == 1) 
                                    {
                                        System.out.println(error.INSERT_PARAMETER + "\n");
                                    } else {
                                        System.out.println(error.INVALID_PARAMETER + "\n");
                                    }  
                                }
                                break; 

                            case "arp":
                                if (COMMAND.length == 2) 
                                {
                                    System.out.print(log.INFORMATION + "Sending packets...");
                                    Map<String, String> arpData = ARP(COMMAND[1].toLowerCase());
                                    if (arpData.containsKey("ERROR"))
                                    {
                                        if (arpData.get("ERROR").contains("syntax"))
                                        {
                                            System.out.println("\r" + error.INVALID_PARAMETER + "\n");
                                        } else {
                                            System.out.println("\r" + log.ERROR + "ARP-Request failed." + "\n");
                                        }
                                        break;
                                    }
                                    System.out.println("\r" + log.INFORMATION + "ARP-Request result:\n");
                                    System.out.print(log.INFOREGULAR);
                                    System.out.printf("%-25s%-17s%-19s%-18s\n", "Hostname", "IPv4", "Mac", "Vendor");
                                    for (String data : arpData.keySet())
                                    {
                                        String hostname = "UNKNOWN";
                                        try 
                                        {
                                            hostname = getfqdn(data);
                                            if (hostname.equals(data)) 
                                            {
                                                hostname = "UNKNOWN";
                                            }
                                        } catch (Exception e) 
                                        {
                                            hostname = "UNKNOWN";
                                        } 
                                        System.out.print(log.INFOREGULAR);
                                        System.out.printf("%-25s%-17s%-19s%-18s\n", hostname.trim(), data.trim(), arpData.get(data).toString().trim().toUpperCase(), getVendor((arpData.get(data).toString().trim()).toUpperCase()));
                                    }
                                } else if (COMMAND.length > 2){
                                    System.out.println(error.INVALID_PARAMETER + "\n");
                                } else {
                                    System.out.print(log.INFORMATION + "Sending packets...");
                                    Map<String, String> arpData = ARP("");
                                    if (arpData.containsKey("ERROR"))
                                    {
                                        if (arpData.get("ERROR").contains("syntax"))
                                        {
                                            System.out.println("\r" + error.INVALID_PARAMETER + "\n");
                                        } else {
                                            System.out.println("\r" + log.ERROR + "ARP-Request failed." + "\n");
                                        }
                                        break;
                                    }
                                    System.out.println("\r" + log.INFORMATION + "ARP-Request result:\n");
                                    System.out.print(log.INFOREGULAR);
                                    System.out.printf("%-25s%-17s%-19s%-18s\n", "Hostname", "IPv4", "Mac", "Vendor");
                                    for (String data : arpData.keySet())
                                    {
                                        String hostname = "UNKNOWN";
                                        try 
                                        {
                                            hostname = getfqdn(data);
                                            if (hostname.equals(data)) 
                                            {
                                                hostname = "UNKNOWN";
                                            }
                                        } catch (Exception e) 
                                        {
                                            hostname = "UNKNOWN";
                                        }    
                                        System.out.print(log.INFOREGULAR);
                                        System.out.printf("%-25s%-17s%-19s%-18s\n", hostname.trim(), data.trim(), arpData.get(data).toString().trim().toUpperCase(), getVendor((arpData.get(data).toString().trim()).toUpperCase()));
                                    }
                                }
                                System.out.println();
                                break;
                            
                            case "portscanner":
                                if (COMMAND.length == 2)
                                {
                                    if (isOnline(COMMAND[1].toLowerCase()))
                                    {
                                        
                                    } else 
                                    {
                                        System.out.println(log.ERROR + "Host not existing or offline.\n");
                                        break; 
                                    }
                                    System.out.print(log.INFORMATION + "Sending packets...");
                                    try 
                                    {
                                        Map<String, String> result = scanPort(COMMAND[1].toLowerCase(), 1, 1024);
                                        System.out.println("\r" + log.INFORMATION + "Port-Scanner result:\n");
                                        System.out.print(log.INFOREGULAR);
                                        System.out.printf("%-16s%-17s\n", "Port", "State");
                                        for (String portkey : result.keySet())
                                        {
                                            System.out.print(log.INFOREGULAR);
                                            System.out.printf("%-16s%-17s\n", portkey + " [" + getService(Integer.valueOf(portkey), true).toUpperCase() + "]", result.get(portkey));
                                        }
                                        System.out.println();
                                    } catch (Exception e) 
                                    {
                                        System.out.println("\r" + log.ERROR + "Scanning failed [No port detected?].\n");
                                    }
                                } else if (COMMAND.length == 4) 
                                {
                                    if (isOnline(COMMAND[1].toLowerCase()))
                                    {
                                        
                                    } else 
                                    {
                                        System.out.println(log.ERROR + "Host not existing or offline.\n");
                                        break; 
                                    }
                                    if (COMMAND[2].equals("-r") || COMMAND[1].equals("--range"))
                                    {
                                        try 
                                        {
                                            int n1 = Integer.valueOf(COMMAND[3].toLowerCase().split("-")[0]);
                                            int n2 = Integer.valueOf(COMMAND[3].toLowerCase().split("-")[1]);
                                            if ((n1 >= 1 && n1 <= 65535) && (n2 >= 1 && n2 <= 65535))
                                            {

                                            } else 
                                            {
                                                throw new Exception();
                                            }
                                        } catch (Exception e)
                                        {
                                            System.out.println(error.INVALID_PARAMETER + "\n");
                                            break;
                                        }
                                        System.out.print(log.INFORMATION + "Sending packets...");
                                        try 
                                        {
                                            Map<String, String> result = scanPort(COMMAND[1].toLowerCase(), Integer.valueOf(COMMAND[3].toLowerCase().split("-")[0]), Integer.valueOf(COMMAND[3].toLowerCase().split("-")[1]));
                                            System.out.println("\r" + log.INFORMATION + "Port-Scanner result:\n");
                                            System.out.print(log.INFOREGULAR);
                                            System.out.printf("%-16s%-17s\n", "Port", "State");
                                            for (String portkey : result.keySet())
                                            {
                                                System.out.print(log.INFOREGULAR);
                                                System.out.printf("%-16s%-17s\n", portkey + " [" + getService(Integer.valueOf(portkey), true).toUpperCase() + "]", result.get(portkey));
                                            }
                                            System.out.println();
                                        } catch (Exception e) 
                                        {
                                            System.out.println("\r" + log.ERROR + "Scanning failed [No port detected?].\n");
                                        }
                                    }
                                } else 
                                {
                                    if (COMMAND.length > 4)
                                    {
                                        System.out.println(error.INVALID_PARAMETER + "\n");
                                    } else if (COMMAND.length == 3) 
                                    {
                                        if (COMMAND[2].equals("-r"))
                                        {
                                            System.out.println(error.INSERT_PARAMETER + "\n");
                                        } else 
                                        {
                                            System.out.println(error.INVALID_PARAMETER + "\n");
                                        }
                                    } else 
                                    {
                                        System.out.println(error.INSERT_PARAMETER + "\n");
                                    }
                                }
                                break;

                            case "ping":
                                if (COMMAND.length == 2)
                                {
                                    if (isOnline(COMMAND[1].toLowerCase()) || isOnline(getIP(COMMAND[1].toLowerCase())))
                                    {
                                        System.out.println(log.INFORMATION + "Ping " + COMMAND[1] + ":");
                                        String address = getIP(COMMAND[1].toLowerCase());
                                        String hostname = getfqdn(address);
                                        System.out.println();
                                        for (int i = 1; i < 9; i++)
                                        {
                                            String pingresult[] = ping(address, String.valueOf(i)).split(",");
                                            System.out.println(log.INFOREGULAR + "Send - Receive packet from " + hostname + " [" + address + "]: SEQ = " + i + ", TTL = " + pingresult[0] + ", DELAY = " + pingresult[1]);
                                        }  
                                    } else 
                                    {
                                        System.out.println(log.ERROR + "Host not existing or offline.\n");
                                        break;  
                                    }
                                }
                                else if (COMMAND.length == 4) 
                                {
                                    if (COMMAND[2].equals("-c") || COMMAND[2].equals("--count"))
                                    {
                                        try 
                                        {
                                            if (COMMAND[3].contains("-")) 
                                            {
                                                throw new Exception();
                                            }
                                            int count = Integer.valueOf(COMMAND[3]); 
                                            if (isOnline(COMMAND[1].toLowerCase()) || isOnline(getIP(COMMAND[1].toLowerCase())))
                                            {
                                                System.out.println(log.INFORMATION + "Ping " + COMMAND[1] + ":");
                                                String address = getIP(COMMAND[1].toLowerCase());
                                                String hostname = getfqdn(address);
                                                System.out.println();
                                                for (int i = 1; i < count + 1; i++)
                                                {
                                                    String pingresult[] = ping(address, String.valueOf(i)).split(",");
                                                    System.out.println(log.INFOREGULAR + "Send - Receive packet from " + hostname + " [" + address + "]: SEQ = " + i + ", TTL = " + pingresult[0] + ", DELAY = " + pingresult[1]);
                                                }
                                            } else 
                                            {
                                                System.out.println(log.ERROR + "Host not existing or offline.\n");
                                                break;  
                                            }
                                        } catch (Exception e)
                                        {
                                            System.out.println(error.INVALID_PARAMETER);
                                        }
                                    } else 
                                    {
                                        System.out.println(error.INVALID_PARAMETER);
                                    }
                                }
                                else if (COMMAND.length == 3 && (COMMAND[2].equals("-c") || COMMAND[2].equals("--count"))) 
                                {
                                    System.out.println(error.INSERT_PARAMETER);
                                }
                                else if (COMMAND.length == 3) 
                                {
                                    System.out.println(error.INVALID_PARAMETER);
                                }
                                else
                                {
                                    System.out.println(error.INSERT_PARAMETER);
                                }
                                System.out.println();
                                break;
                                
                            case "sniffer":
                                if (COMMAND.length >= 2 && COMMAND.length < 4)
                                {
                                    boolean rawReadable = false;
                                    try
                                    {
                                        if (COMMAND[1].contains("-"))
                                        {
                                            throw new Exception();
                                        }
                                        int timeout = Integer.valueOf(COMMAND[1]);
                                        if (COMMAND.length == 3) 
                                        { 
                                            if (COMMAND[2].equals("--show-raw") || COMMAND[2].equals("-s"))
                                            {
                                                rawReadable = true;
                                            } else 
                                            {
                                                System.out.println(error.INVALID_PARAMETER + "\n");
                                                break;
                                            }
                                        }
                                        System.out.print(log.INFORMATION + "Sniffing...");
                                        String sniffResponse[] = sniffer(timeout, rawReadable).split("\\|");
                                        if (sniffResponse[0].equals(""))
                                        {
                                            System.out.println("\r" + log.ERROR + "Impossible to sniff.\n");
                                            break;
                                        }
                                        
                                        System.out.println("\r" + log.INFORMATION + "Sniffing result:\n");
                                        for (String response : sniffResponse)
                                        {
                                            String protocolColor = "";
                                            String responseList[] = response.split("\\$\\$\\$");
                                            if (responseList[1].equals("<ARP>"))
                                            {
                                                protocolColor = ansi.YELLOW_BOLD;
                                            }
                                            else if (responseList[1].equals("<TCP>"))
                                            {
                                                protocolColor = ansi.CYAN_BOLD;
                                            }
                                            else if (responseList[1].equals("<ICMP>"))
                                            {
                                                protocolColor = ansi.PURPLE_BOLD;
                                            }
                                            else if (responseList[1].equals("<UDP>"))
                                            {
                                                protocolColor = ansi.ORANGE_BOLD;
                                            } else 
                                            {
                                                protocolColor = ansi.RED_BOLD;
                                            }
                                            if (protocolColor.equals(ansi.RED_BOLD))
                                            {
                                                System.out.println(log.INFOREGULAR + protocolColor + responseList[1] + ansi.WHITE_BOLD + " " + responseList[2]);
                                            } else 
                                            {
                                                System.out.println(log.INFOREGULAR + responseList[0] + " " + protocolColor + responseList[1] + ansi.WHITE_BOLD + " " + responseList[2]);
                                            }
                                        }
                                    } catch (Exception e) 
                                    {
                                        System.out.println(error.INVALID_PARAMETER);
                                    }
                                } else 
                                {
                                    if (COMMAND.length > 4)
                                    {
                                        System.out.println(error.INVALID_PARAMETER);
                                    } 
                                    else
                                    {
                                        System.out.println(error.INSERT_PARAMETER);
                                    }
                                }
                                System.out.println();
                                break;

                            case "hexanalysis":
                                System.out.print(log.INFORMATION + "Decoding...");
                                List<String> result = new ArrayList<String>();
                                if (COMMAND.length == 1)
                                {
                                    System.out.println("\r" + error.INSERT_PARAMETER + "\n");
                                    break;
                                } else if (COMMAND.length == 5)
                                {
                                    System.out.println("\r" + error.INVALID_PARAMETER + "\n");
                                    break;
                                }
                                if (COMMAND[1].equals("-f") || COMMAND[1].equals("--file"))
                                {
                                    if (COMMAND.length == 2)
                                    {
                                        System.out.println("\r" + error.INSERT_PARAMETER + "\n");
                                        break;
                                    }
                                    try
                                    {
                                        result = hexdump(true, fileChoice(formatQuest(COMMAND[2])));
                                    } catch (FileNotFoundException e) 
                                    {
                                        System.out.println("\r" + log.ERROR + "File not existing.\n");
                                        break;
                                    } catch (Exception e) 
                                    {
                                        System.out.println("\r" + log.ERROR + "Failed to decode.\n");
                                        break;
                                    }
                                } else 
                                {  
                                    if (COMMAND.length == 4)
                                    {
                                        System.out.println("\r" + error.INVALID_PARAMETER + "\n");
                                        break;
                                    }
                                    boolean isHex = COMMAND[1].matches("^[0-9a-fA-F]+$");
                                    if (!isHex) 
                                    {
                                        System.out.println("\r" + error.INVALID_PARAMETER + "\n");
                                        break;
                                    }
                                    try 
                                    { 
                                        result = hexdump(false, COMMAND[1]);
                                    } catch (Exception e) 
                                    {
                                        System.out.println("\r" + log.ERROR + "Failed to decode.\n");
                                        break;
                                    }
                                }
                                boolean copy = false;
                                if (COMMAND.length == 4)
                                {
                                    if (COMMAND[3].equals("--copy-clipboard") || COMMAND[3].equals("-c"))
                                    {
                                        copy = true;
                                    } else 
                                    {
                                        System.out.println("\r" + error.INVALID_PARAMETER + "\n");
                                        break;
                                    }
                                }
                                else if (COMMAND.length == 3 && !COMMAND[1].equals("-f")) 
                                {
                                    if (COMMAND[2].equals("--copy-clipboard") || COMMAND[2].equals("-c"))
                                    {
                                        copy = true;
                                    } else
                                    {
                                        System.out.println("\r" + error.INVALID_PARAMETER + "\n");
                                        break;
                                    } 
                                }
                                
                                String copyappend = ""; 

                                System.out.println("\r" + log.INFORMATION + "Hex analysis result:\n");
                                System.out.println(log.INFOREGULAR + ansi.CYAN_BOLD + "Offset" + ansi.WHITE_BOLD + "     00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F\n");
                                for (int i = 0; i < result.size(); i += 3)
                                {
                                    System.out.printf("%-43s%-49s%-34s\n", log.INFOREGULAR + ansi.CYAN_BOLD + result.get(i) + ansi.WHITE_BOLD, result.get(i + 1), ansi.BLACK_BOLD + "|" + result.get(i + 2).strip() + "|" + ansi.WHITE_BOLD);
                                    copyappend += result.get(i + 1);
                                }
                                if (copy == true)
                                {
                                    copyClip(copyappend.replace(" ", ""));
                                }
                                
                                System.out.println();
                                break;

                            default:
                                System.out.println(log.ERROR + "Object " + '"' + ansi.RED_BOLD + COMMAND[0] + ansi.WHITE_BOLD + '"' + " is an unknown command.\n");
                        }
                    } catch (Exception e) 
                    {
                        System.out.println(log.ERROR + "Unexpected error, CODE [" + bytesToHex(crypter("encrypt", e.toString()).getBytes()) + "]");
                    }
                }        
            } catch (Exception e) {}  
        } 

    public static List<String> hexdump(boolean isFile, String fileOrHex) throws Exception 
    {
        String hexSeq = "";
        if (isFile)
        {
            if ((hexSeq = readFile(fileOrHex, readtype.BYTE_TO_HEX)) == "error")
            {
                throw new FileNotFoundException();
            }
        } else 
        {
            hexSeq = fileOrHex;
        }

        List<String> hexList = Arrays.asList(hexSeq.split("(?<=\\G.{2})"));

        int offset = 0;
        int counter = 0;
        String text = "";
        List<String> finalResponse = new ArrayList<String>();
        for (String hexColumn : hexList)
        {
            if (counter == 16)
            {
                finalResponse.add(text.substring(0, text.length() - 1));
                String decoded = "";
                try 
                {
                    decoded = new String(Hex.decodeHex(text.replace(" ", "")), guessEncoder(Hex.decodeHex(text.replace(" ", ""))));
                } catch (Exception e)
                {
                    decoded = new String(Hex.decodeHex(text.replace(" ", "")), "ANSI");
                }
                String newDecoded = "";
                for (String oneDecoded : decoded.split(""))
                {
                    if ((int)oneDecoded.charAt(0) < 128 && (int)oneDecoded.charAt(0) > 32)
                    {
                        newDecoded += oneDecoded;
                    } else 
                    {
                        newDecoded += ".";
                    }
                }
                finalResponse.add(newDecoded);
                counter = 0;
                offset += 16;
                text = "";
            }
            if (counter == 0)
            {
                finalResponse.add(String.format("0x%08X", offset));
            }
            text += hexColumn + " ";
            counter++;
        }
        finalResponse.add(text.substring(0, text.length() - 1));
        String decoded = "";
        try 
        {
            decoded = new String(Hex.decodeHex(text.replace(" ", "")), guessEncoder(Hex.decodeHex(text.replace(" ", ""))));
        } catch (Exception e)
        {
            decoded = new String(Hex.decodeHex(text.replace(" ", "")), "ANSI");
        }
        String newDecoded = "";
        for (String oneDecoded : decoded.split(""))
        {
            if ((int)oneDecoded.charAt(0) < 128 && (int)oneDecoded.charAt(0) > 32)
            {
                newDecoded += oneDecoded;
            } else 
            {
                newDecoded += ".";
            }
        }
        finalResponse.add(newDecoded);
        return finalResponse;
    }

    public static String guessEncoder(byte[] bytes) 
    {
        String DEFAULT_ENCODING = "UTF-8";
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) 
        {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }

    public static String sniffer(int timeout, boolean rawReadable) throws Exception
    {
        String result = callPack(new String[]{"sniffer", String.valueOf(timeout), String.valueOf(rawReadable)});
        return result;
    }
    
    public static String ping(String host, String seq) throws Exception
    {
        String result = callPack(new String[]{"ping", host, seq});
        return result;
    }

    public static boolean isOnline(String host) throws Exception
    {   
        if (callPack(new String[]{"isonline", host}).equals("True"))
        {
            return true;
        } else 
        {
            return false;
        }
    }

    public static String getIP(String hostname) throws Exception
    {
        try 
        {
            return InetAddress.getByName(hostname).getHostAddress();
        } catch (Exception e)
        {
            return "0.0.0.0";
        }
    }

    public static Map<String, String> scanPort(String IPv4, int initPort, int finalPort) throws Exception
    {
        boolean error = false;
        String _result = callPack(new String[]{"scanport", IPv4, String.valueOf(initPort), String.valueOf(finalPort)});
        Map<String, String> result = new HashMap<String, String>();
        try 
        {
            for (String _port : _result.replace("[", "").replace("]", "").replace("'", "").split(", "))
            { 
                String[] port = _port.split("\\.");
                result.put(port[0], port[1]);
            }
        } catch (Exception e)
        {
            error = true;
        }
        if (error)
        {
            throw new Exception();
        }
        return result;
    }

    public static boolean inList(List<String> list, String key)
    {
        for (String obj : list)
        {
            if (obj.equals(key))
            {
                return true;
            }
        }
        return false;
    }

    public static String getService(int port, boolean symbmode) throws Exception
    {
        String _serviceList = "1,tcpmux,2,compressnet,3,compressnet,4,sfs,5,rje,7,echo,8,trojan - Ping Attack,9,discard,11,systat,13,daytime,15,netstat / trojan[B2],17,quotd,18,msp,19,chargen,20,ftp-data,21,ftp,22,ssh,23,telnet,24,priv-mail / trojan[BO2K],25,smtp,26,rsftp,27,nsw-fe,28,trojan[amanda],29,msg-icp,30,trojan[Agent 40421],40421,trojan[Agent 40421],31,msg-auth,33,dsp[Display Support Protocol],34,remote[Remote File],35,priv-printer,37,time,38,rap[Resource Location Protocol],41,graphics,42,nameserver,43,whois,44,mpm-flags,45,mpm,46,mpm-snd,47,ni-ftp,48,auditd,49,bbn-login,50,re-mail.ck,51,la-maint,52,xns-time,53,domain,54,xns-ch,55,isi-gl,56,xns-auth,57,priv-terminal / MTP,58,xns-mail,59,priv-file / Backdoor.Sdbot.AJ,61,NI MAIL,62,ACA Services,63,whois++,64,covia,65,tacacs-ds,66,Oracle Sql *NET,67,bootps,68,bootpc,69,tftp,70,gopher,71,netrjs-1,72,netrjs-2,73,netrjs-3,74,netrjs-4,75,priv-dial,76,deos,77,priv-RJE,78,vettcp,79,finger,80,http,81,hosts2-ns,82,xfer,83,mit-ml-dev,84,Common Trace Facility,85,mit-ml-dev,86,mfcobol,87,priv-terminal link,88,kerberos,89,su-mit-tg,90,dnsix,91,mit-dov,92,Network printing protocol,93,device control protocol,94,objcall,95,supdup,96,dixie,97,swift-rvf,98,tacnews,99,metagram,100,newacct,101,hostname,102,iso-tsap,103,gppitnp,104,acr-nema,105,csnet-ns,106,3com-tsmux,107,rtelnet,108,SNA gateway,109,pop2,110,pop3,111,sunrpc,112,mcidas,113,auth,114,audionews,115,sftp,116,ansanotify,117,uucp-path,118,sqlserv,119,nntp,120,cfdptkt,121,erpc,122,smakynet,123,ntp,124,ansatrader,125,locus-map,126,unitary,127,locus-con,128,gss-xlicen,129,pwdgen,130,cisco-fna,131,cisco-tna,132,cisco-sys,133,statsrv,134,ingres-net,135,loc-srv,136,profile,137,netbios-ns,138,netbios-dgm,139,netbios-ssn,140,emfis-data,141,emfis-cntl,142,bl-idm,143,imap2,144,news,145,uaac,146,iso-tp0,147,iso-ip,148,cronus,149,aed-512,150,SQL-NET,151,hems,152,bftp,153,sgmp,154,netsc-prod,155,netsc-dev,156,SQL servic,157,knet-cmp,158,pcmail-srv,159,nss-routing,160,sgmp-traps,161,snmp,162,snmptrap,163,cmip-man,164,cmip-agent,165,xns-courier,166,s-net,167,namp,168,rsvd,169,send,171,multiplex,170,print-srv,172,cl/1,173,xyplex-mux,174,mailq,175,vmnet,176,genrad-mux,177,xdmcp,178,nextstep,179,bgp,180,ris,181,unify,182,audit SITP,183,ocbinder,184,ocserver,185,remote-kis,186,KIS protocol,187,ACI,188,mumps,189,qft,190,gacp,191,prospero,192,osu-nms,193,srmp,194,irc,195,dn6-nlm-aud,196,dn6-smm-red,197,dls,198,dls-mon,199,smux,200,src,201,at-rtmp,202,at-nbp,203,at-3,204,at-echo,205,at-5,206,at-zis,207,at-7,208,at-8,209,tam,210,z39.50,211,914c/g,212,anet,213,ipx,214,vmpwscs,215,softpc,216,atls,217,dbase,218,mpp,219,uarps,220,imap3,221,fln-spx,222,rsh-spx,223,cdc,243,sur-meas,245,link,246,dsp3270,344,pdap,345,pawserv,346,zserv,347,fatserv,348,cis-sgwp,371,clearcase,372,ulistserv,373,legent-1,374,legent-2,375,hassle,376,nip,377,tnETOS,378,dsETOS,379,is99c,380,is99s,381,hp-collector,382,hp-managed-node,383,hp-alarm-mgr,384,arns,385,ibm-app,386,asa,387,aurp,388,unidata-ldm,389,ldap,390,uis,391,synotics-relay,392,synotics-broker,393,dis,394,embl-ndt,395,netcp,396,netware-ip,397,mptn,398,kryptolan,399,iso-tsap-c2,400,work-sol,401,ups,402,genie,403,decap,404,nced,405,ncld,406,imsp,407,timbuktu,408,prm-sm,409,prm-nm,410,decladebug,411,rmt,412,synoptics-trap,413,smsp,414,infoseek,415,bnet,416,silverplatter,417,onmux,418,hyper-g,419,ariel1,420,smpte,421,ariel2,422,ariel3,423,opc-job-start,424,opc-job-track,425,icad-el,426,smartsdp,427,svrloc,428,ocs_cmu,429,ocs_amu,430,utmpsd,431,utmpcd,432,iasd,433,nnsp,434,mobileip-agent,435,mobilip-mn,436,dna-cml,437,dna-cml,438,dsfgw,439,dasp,440,sgcp,441,decvms-sysmgt,442,cvc_hostd,443,https,444,snpp,445,microsoft-ds,446,ddm-rdb,447,ddm-dfm,448,ddm-byte,449,as-servermap,450,tserver,451,sfs-smp-net,452,sfs-config,453,creativeserver,454,contentserver,455,creativepartnr,456,macon-tcp,457,scohelp,458,appleqtc,460,skronk,459,ampr-rcmd,461,datasurfsrv,462,datasurfsrvsec,463,alpes,464,kpasswd,465,ssmtp,466,digital-vrc,467,mylex-mapd,468,photuris,469,rcp,470,scx-proxy,471,mondex,472,ljk-login,473,hybrid-pop,474,tn-tl-w1,475,tcpnethaspsrv,512,exec,513,login,514,cmd,515,printer,517,talk,518,ntalk,519,utime,520,efs,525,timed,530,courier,531,conference,532,netnews,533,netwall,539,apertus-ldp,540,uucp,541,uucp-rlogin,543,klogin,544,kshell,545,appleqtcsrvr,546,dhcp-client,547,dhcp-server,550,new-rwho,551,cybercash,552,deviceshare,553,pirp,555,dsf,556,remotefs,557,openvms-sysipc,558,sdnskmp,559,teedtap,560,rmonitor,561,monitor,562,chshell,563,snews,564,9pfs,565,whoami,566,streettalk,567,banyan-rpc,568,ms-shuttle,569,ms-rome,570,meter[demon],571,meter[udemon],572,sonar,573,banyan-vip,600,ipcserver,607,nqs,606,urm,608,shift-uft,609,npmp-trap,610,npmp-local,611,npmp-gui,634,ginad,666,mdqs / doom,704,elcsd,709,entrustmanager,729,netviewdm1,730,netviewdm2,731,netviewdm3,741,netgw,742,netrcs,744,flexlm,747,fujitsu-dev,748,ris-cm,749,kerberos-adm,750,rfile,751,pump,752,qrh,753,rrh,754,tell,758,nlogin,759,con,760,ns,761,rxe,762,quotad,763,cycleserv,764,omserv,765,webster,767,phonebook,769,vid,770,cadlock,771,rtip,772,cycleserv2,773,submit,774,rpasswd,775,entomb,776,wpages,780,wpgs,786,concert,800,mdbs_daemon,801,device,888,accessbuilder,996,vsinet,997,maitrd,998,busboy,999,garcon / puprouter,1000,cadlock,1,tcpmux,1,tcpmux,2,compressnet,3,compressnet,5,rje,7,echo,9,discard,11,systat,13,daytime,15,netstat,17,qotd,18,msp,19,chargen,20,ftp-data,21,ftp,22,ssh,23,telnet,24,priv-mail,25,smtp,27,nsw-fe,29,msg-icp,31,msg-auth,33,dsp,35,priv-print,37,time,38,rap,39,rlp,41,graphics,42,nameserver,43,whois,44,mpm-flags,45,mpm,46,mpm-snd,47,ni-ftp,48,auditd,49,tacacs,50,re-mail-ck,51,la-maint,52,xns-time,53,domain,54,xns-ch,55,isi-gl,56,xns-auth,57,priv-term,58,xns-mail,59,priv-file,61,ni-mail,62,acas,63,via-ftp,64,covia,65,tacacs-ds,66,sql*net,67,dhcps,68,dhcpc,69,tftp,70,gopher,71,netrjs-1,72,netrjs-2,73,netrjs-3,74,netrjs-4,75,priv-dial,76,deos,77,priv-rje,78,vettcp,78,vettcp,79,finger,80,http,81,hosts2-ns,82,xfer,83,mit-ml-dev,84,ctf,85,mit-ml-dev,86,mfcobol,87,priv-term-l,88,kerberos-sec,89,su-mit-tg,90,dnsix,91,mit-dov,92,npp,93,dcp,94,objcall,95,supdup,96,dixie,97,swift-rvf,98,linuxconf,99,metagram,100,newacct,101,hostname,102,iso-tsap,103,gppitnp,104,acr-nema,105,csnet-ns,106,pop3pw,107,rtelnet,108,snagas,109,pop2,110,pop3,111,rpcbind,112,mcidas,113,auth,114,audionews,115,sftp,116,ansanotify,117,uucp-path,118,sqlserv,119,nntp,120,cfdptkt,121,erpc,122,smakynet,123,ntp,124,ansatrader,125,locus-map,126,unitary,127,locus-con,128,gss-xlicen,129,pwdgen,130,cisco-fna,131,cisco-tna,132,cisco-sys,133,statsrv,134,ingres-net,135,msrpc,136,profile,137,netbios-ns,138,netbios-dgm,139,netbios-ssn,140,emfis-data,141,emfis-cntl,142,bl-idm,143,imap,144,news,145,uaac,146,iso-tp0,147,iso-ip,148,cronus,149,aed-512,150,sql-net,151,hems,152,bftp,153,sgmp,154,netsc-prod,155,netsc-dev,156,sqlsrv,157,knet-cmp,158,pcmail-srv,159,nss-routing,160,sgmp-traps,161,snmp,162,snmptrap,163,cmip-man,164,cmip-agent,165,xns-courier,166,s-net,167,namp,168,rsvd,169,send,170,print-srv,171,multiplex,172,cl-1,173,xyplex-mux,174,mailq,175,vmnet,176,genrad-mux,177,xdmcp,178,nextstep,179,bgp,180,ris,181,unify,182,audit,183,ocbinder,184,ocserver,185,remote-kis,186,kis,187,aci,188,mumps,189,qft,190,gacp,191,prospero,192,osu-nms,193,srmp,194,irc,195,dn6-nlm-aud,196,dn6-smm-red,197,dls,198,dls-mon,199,smux,200,src,201,at-rtmp,202,at-nbp,203,at-3,204,at-echo,205,at-5,206,at-zis,207,at-7,208,at-8,209,tam,210,z39.50,211,914c-g,212,anet,213,ipx,214,vmpwscs,215,softpc,216,atls,217,dbase,218,mpp,219,uarps,220,imap3,221,fln-spx,222,rsh-spx,223,cdc,242,direct,243,sur-meas,244,dayna,245,link,246,dsp3270,247,subntbcst_tftp,248,bhfhs,256,FW1-secureremote,257,FW1-mc-fwmodule,258,Fw1-mc-gui,259,esro-gen,260,openport,261,nsiiops,262,arcisdms,263,hdap,264,bgmp,265,maybeFW1,280,http-mgmt,281,personal-link,282,cableport-ax,308,novastorbakcup,309,entrusttime,310,bhmds,311,asip-webadmin,312,vslmp,313,magenta-logic,314,opalis-robot,315,dpsi,316,decauth,317,zannet,321,pip,344,pdap,345,pawserv,346,zserv,347,fatserv,348,csi-sgwp,349,mftp,350,matip-type-a,351,matip-type-b,352,dtag-ste-sb,353,ndsauth,354,bh611,355,datex-asn,356,cloanto-net-1,357,bhevent,358,shrinkwrap,359,tenebris_nts,360,scoi2odialog,361,semantix,362,srssend,363,rsvp_tunnel,364,aurora-cmgr,365,dtk,366,odmr,367,mortgageware,368,qbikgdp,369,rpc2portmap,370,codaauth2,371,clearcase,372,ulistserv,373,legent-1,374,legent-2,375,hassle,376,nip,377,tnETOS,378,dsETOS,379,is99c,380,is99s,381,hp-collector,382,hp-managed-node,383,hp-alarm-mgr,384,arns,385,ibm-app,386,asa,387,aurp,388,unidata-ldm,389,ldap,390,uis,391,synotics-relay,392,synotics-broker,393,dis,394,embl-ndt,395,netcp,395,netcp,396,netware-ip,397,mptn,398,kryptolan,399,iso-tsap-c2,400,work-sol,401,ups,402,genie,403,decap,404,nced,405,ncld,406,imsp,407,timbuktu,408,prm-sm,409,prm-nm,410,decladebug,411,rmt,412,synoptics-trap,413,smsp,414,infoseek,415,bnet,416,silverplatter,417,onmux,418,hyper-g,419,ariel1,420,smpte,421,ariel2,422,ariel3,423,opc-job-start,424,opc-job-track,425,icad-el,426,smartsdp,427,svrloc,428,ocs_cmu,429,ocs_amu,430,utmpsd,431,utmpcd,432,iasd,433,nnsp,434,mobileip-agent,435,mobilip-mn,436,dna-cml,437,comscm,438,dsfgw,439,dasp,440,sgcp,441,decvms-sysmgt,442,cvc_hostd,443,https,444,snpp,445,microsoft-ds,446,ddm-rdb,447,ddm-dfm,448,ddm-ssl,449,as-servermap,450,tserver,451,sfs-smp-net,452,sfs-config,453,creativeserver,454,contentserver,455,creativepartnr,456,macon-tcp,457,scohelp,458,appleqtc,459,ampr-rcmd,460,skronk,461,datasurfsrv,462,datasurfsrvsec,463,alpes,464,kpasswd5,465,smtps,466,digital-vrc,467,mylex-mapd,468,photuris,469,rcp,470,scx-proxy,471,mondex,472,ljk-login,473,hybrid-pop,474,tn-tl-w1,475,tcpnethaspsrv,475,tcpnethaspsrv,476,tn-tl-fd1,477,ss7ns,478,spsc,479,iafserver,480,loadsrv,481,dvs,482,bgs-nsi,483,ulpnet,484,integra-sme,485,powerburst,486,sstats,487,saft,488,gss-http,489,nest-protocol,490,micom-pfs,491,go-login,492,ticf-1,493,ticf-2,494,pov-ray,495,intecourier,496,pim-rp-disc,497,dantz,498,siam,499,iso-ill,500,isakmp,501,stmf,502,asa-appl-proto,503,intrinsa,504,citadel,505,mailbox-lm,506,ohimsrv,507,crs,508,xvttp,509,snare,510,fcp,511,passgo,512,exec,513,login,514,shell,515,printer,516,videotex,517,talk,518,ntalk,519,utime,520,efs,521,ripng,522,ulp,523,ibm-db2,524,ncp,525,timed,526,tempo,527,stx,528,custix,529,irc-serv,530,courier,531,conference,532,netnews,533,netwall,534,mm-admin,535,iiop,536,opalis-rdv,537,nmsp,538,gdomap,539,apertus-ldp,540,uucp,541,uucp-rlogin,542,commerce,543,klogin,544,kshell,545,ekshell,546,dhcpv6-client,547,dhcpv6-server,548,afpovertcp,548,afpovertcp,549,idfp,550,new-rwho,551,cybercash,552,deviceshare,553,pirp,554,rtsp,555,dsf,556,remotefs,557,openvms-sysipc,558,sdnskmp,559,teedtap,560,rmonitor,561,monitor,562,chshell,563,snews,564,9pfs,565,whoami,566,streettalk,567,banyan-rpc,568,ms-shuttle,569,ms-rome,570,meter,571,umeter,572,sonar,573,banyan-vip,574,ftp-agent,575,vemmi,576,ipcd,577,vnas,578,ipdd,579,decbsrv,580,sntp-heartbeat,581,bdp,582,scc-security,583,philips-vc,584,keyserver,585,imap4-ssl,586,password-chg,587,submission,588,cal,589,eyelink,590,tns-cml,591,http-alt,592,eudora-set,593,http-rpc-epmap,594,tpip,595,cab-protocol,596,smsd,597,ptcnameservice,598,sco-websrvrmg3,599,acp,600,ipcserver,603,mnotes,606,urm,607,nqs,608,sift-uft,609,npmp-trap,610,npmp-local,611,npmp-gui,617,sco-dtmgr,628,qmqp,631,ipp,634,ginad,636,ldapssl,637,lanserver,660,mac-srvr-admin,666,doom,674,acap,691,resvc,704,elcsd,706,silc,709,entrustmanager,709,entrustmanager,723,omfs,729,netviewdm1,730,netviewdm2,730,netviewdm2,731,netviewdm3,731,netviewdm3,740,netcp,740,netcp,741,netgw,742,netrcs,744,flexlm,747,fujitsu-dev,748,ris-cm,749,kerberos-adm,750,kerberos,751,kerberos_master,752,qrh,753,rrh,754,krb_prop,758,nlogin,759,con,760,krbupdate,761,kpasswd,762,quotad,763,cycleserv,764,omserv,765,webster,767,phonebook,769,vid,770,cadlock,771,rtip,772,cycleserv2,773,submit,774,rpasswd,775,entomb,776,wpages,780,wpgs,781,hp-collector,782,hp-managed-node,783,spamassassin,786,concert,799,controlit,800,mdbs_daemon,801,device,808,ccproxy-http,871,supfilesrv,873,rsync,888,accessbuilder,898,sun-manageconsole,989,ftps-data,901,samba-swat,902,iss-realsecure-sensor,903,iss-console-mgr,950,oftep-rpc,953,rndc,975,securenetpro-sensor,990,ftps,992,telnets,993,imaps,994,ircs,995,pop3s,996,xtreelic,997,maitrd,998,busboy,999,garcon,1000,cadlock,1002,windows-icfw,1008,ufsd,1023,netvenuechat,1024,kdm,1025,NFS-or-IIS,1026,LSA-or-nterm,1027,IIS,1029,ms-lsa,1030,iad1,1031,iad2,1032,iad3,1033,netinfo,1040,netsaint,1043,boinc-client,1050,java-or-OTGfileshare,1058,nim,1059,nimreg,1067,instl_boots,1068,instl_bootc,1076,sns_credit,1080,socks,1083,ansoft-lm-1,1084,ansoft-lm-2,1103,xaudio,1109,kpop,1110,nfsd-status,1112,msql,1127,supfiledbg,1139,cce3x,1155,nfa,1158,lsnr,1178,skkserv,1212,lupa,1214,fasttrack,1220,quicktime,1222,nerv,1234,hotline,1241,nessus,1248,hermes,1337,waste,1346,alta-ana-lm,1347,bbn-mmc,1348,bbn-mmx,1349,sbook,1350,editbench,1351,equationbuilder,1352,lotusnotes,1353,relief,1354,rightbrain,1355,intuitive-edge,1356,cuillamartin,1357,pegboard,1358,connlcli,1359,ftsrv,1360,mimer,1361,linx,1362,timeflies,1363,ndm-requester,1364,ndm-server,1365,adapt-sna,1366,netware-csp,1367,dcs,1368,screencast,1369,gv-us,1370,us-gv,1371,fc-cli,1372,fc-ser,1373,chromagrafx,1374,molly,1375,bytex,1376,ibm-pps,1377,cichlid,1378,elan,1379,dbreporter,1380,telesis-licman,1381,apple-licman,1383,gwha,1384,os-licman,1385,atex_elmd,1386,checksum,1387,cadsi-lm,1388,objective-dbc,1389,iclpv-dm,1390,iclpv-sc,1391,iclpv-sas,1392,iclpv-pm,1393,iclpv-nls,1394,iclpv-nlc,1395,iclpv-wsm,1396,dvl-activemail,1397,audio-activmail,1398,video-activmail,1399,cadkey-licman,1400,cadkey-tablet,1401,goldleaf-licman,1402,prm-sm-np,1403,prm-nm-np,1404,igi-lm,1405,ibm-res,1406,netlabs-lm,1407,dbsa-lm,1408,sophia-lm,1409,here-lm,1410,hiq,1411,af,1412,innosys,1413,innosys-acl,1414,ibm-mqseries,1415,dbstar,1416,novell-lu6.2,1417,timbuktu-srv1,1418,timbuktu-srv2,1419,timbuktu-srv3,1420,timbuktu-srv4,1421,gandalf-lm,1422,autodesk-lm,1423,essbase,1424,hybrid,1425,zion-lm,1426,sas-1,1427,mloadd,1428,informatik-lm,1429,nms,1430,tpdu,1431,rgtp,1432,blueberry-lm,1433,ms-sql-s,1434,ms-sql-m,1435,ibm-cics,1436,sas-2,1437,tabula,1438,eicon-server,1439,eicon-x25,1440,eicon-slp,1441,cadis-1,1442,cadis-2,1443,ies-lm,1444,marcam-lm,1445,proxima-lm,1446,ora-lm,1447,apri-lm,1448,oc-lm,1449,peport,1450,dwf,1451,infoman,1452,gtegsc-lm,1453,genie-lm,1454,interhdl_elmd,1455,esl-lm,1456,dca,1457,valisys-lm,1458,nrcabq-lm,1459,proshare1,1460,proshare2,1461,ibm_wrless_lan,1462,world-lm,1463,nucleus,1464,msl_lmd,1465,pipes,1466,oceansoft-lm,1467,csdmbase,1468,csdm,1469,aal-lm,1470,uaiact,1471,csdmbase,1472,csdm,1473,openmath,1474,telefinder,1475,taligent-lm,1476,clvm-cfg,1477,ms-sna-server,1478,ms-sna-base,1479,dberegister,1480,pacerforum,1481,airs,1482,miteksys-lm,1483,afs,1484,confluent,1485,lansource,1486,nms_topo_serv,1487,localinfosrvr,1488,docstor,1489,dmdocbroker,1490,insitu-conf,1491,anynetgateway,1492,stone-design-1,1493,netmap_lm,1494,citrix-ica,1495,cvc,1496,liberty-lm,1497,rfx-lm,1498,watcom-sql,1499,fhc,1500,vlsi-lm,1501,sas-3,1502,shivadiscovery,1503,imtc-mcs,1504,evb-elm,1505,funkproxy,1506,utcd,1507,symplex,1508,diagmond,1509,robcad-lm,1510,mvx-lm,1511,3l-l1,1512,wins,1513,fujitsu-dtc,1514,fujitsu-dtcns,1515,ifor-protocol,1516,vpad,1517,vpac,1518,vpvd,1519,vpvc,1520,atm-zip-office,1521,oracle,1522,rna-lm,1523,cichild-lm,1524,ingreslock,1525,orasrv,1526,pdap-np,1527,tlisrv,1528,mciautoreg,1529,support,1530,rap-service,1531,rap-listen,1532,miroconnect,1533,virtual-places,1534,micromuse-lm,1535,ampr-info,1536,ampr-inter,1537,sdsc-lm,1538,3ds-lm,1539,intellistor-lm,1540,rds,1541,rds2,1542,gridgen-elmd,1543,simba-cs,1544,aspeclmd,1545,vistium-share,1546,abbaccuray,1547,laplink,1548,axon-lm,1549,shivahose,1550,3m-image-lm,1551,hecmtl-db,1552,pciarray,1600,issd,1650,nkd,1651,shiva_confsrvr,1652,xnmp,1661,netview-aix-1,1662,netview-aix-2,1663,netview-aix-3,1664,netview-aix-4,1665,netview-aix-5,1666,netview-aix-6,1667,netview-aix-7,1668,netview-aix-8,1669,netview-aix-9,1670,netview-aix-10,1671,netview-aix-11,1672,netview-aix-12,1680,CarbonCopy,1720,H.323/Q.931,1723,pptp,1755,wms,1761,landesk-rc,1762,landesk-rc,1763,landesk-rc,1764,landesk-rc,1827,pcm,1900,UPnP,1935,rtmp,1984,bigbrother,1986,licensedaemon,1987,tr-rsrb-p1,1988,tr-rsrb-p2,1989,tr-rsrb-p3,1990,stun-p1,1991,stun-p2,1992,stun-p3,1993,snmp-tcp-port,1993,snmp-tcp-port,1994,stun-port,1995,perf-port,1996,tr-rsrb-port,1997,gdp-port,1998,x25-svc-port,1999,tcp-id-port,1999,tcp-id-port,2000,callbook,2001,dc,2002,globe,2003,cfingerd,2004,mailbox,2005,deslogin,2006,invokator,2007,dectalk,2008,conf,2009,news,2010,search,2011,raid-cc,2012,ttyinfo,2013,raid-am,2014,troff,2015,cypress,2016,bootserver,2017,cypress-stat,2018,terminaldb,2019,whosockami,2020,xinupageserver,2021,servexec,2022,down,2023,xinuexpansion3,2024,xinuexpansion4,2025,ellpack,2026,scrabble,2027,shadowserver,2028,submitserver,2030,device2,2032,blackboard,2033,glogger,2034,scoremgr,2035,imsldoc,2038,objectmanager,2040,lam,2041,interbase,2042,isis,2043,isis-bcast,2044,rimsl,2045,cdfunc,2046,sdfunc,2047,dls,2048,dls-monitor,2049,nfs,2064,dnet-keyproxy,2053,knetd,2065,dlsrpn,2067,dlswpn,2068,advocentkvm,2105,eklogin,2106,ekshell,2108,rkinit,2111,kx,2112,kip,2120,kauth,2121,ccproxy-ftp,2201,ats,2232,ivs-video,2241,ivsd,2301,compaqdiag,2307,pehelp,2401,cvspserver,2430,venus,2431,venus-se,2432,codasrv,2433,codasrv-se,2500,rtsserv,2501,rtsclient,2564,hp-3000-telnet,2600,zebrasrv,2601,zebra,2602,ripd,2603,ripngd,2604,ospfd,2605,bgpd,2627,webster,2628,dict,2638,sybase,2766,listen,2784,www-dev,2809,corbaloc,2903,extensisportfolio,2998,iss-realsec,3000,ppp,3001,nessusd,3005,deslogin,3006,deslogind,3049,cfs,3052,PowerChute,3064,dnet-tstproxy,3086,sj3,3128,squid-http,3141,vmodem,3264,ccmail,3268,globalcatLDAP,3269,globalcatLDAPssl,3292,meetingmaker,3306,mysql,3333,dec-notes,3372,msdtc,3389,ms-term-serv,3421,bmap,3455,prsvp,3456,vat,3457,vat-control,3462,track,3531,peerenabler,3632,distccd,3689,rendezvous,3900,udt_os,3984,mapper-nodemgr,3985,mapper-mapethd,3986,mapper-ws_ethd,3999,remoteanything,4000,remoteanything,4008,netcheque,4045,lockd,4125,rww,4132,nuts_dem,4133,nuts_bootp,4144,wincim,4224,xtell,4321,rwhois,4333,msql,4343,unicall,4444,krb524,4480,proxy-plus,4500,sae-urn,4557,fax,4559,hylafax,4660,mosmig,4672,rfa,4827,squid-htcp,4899,radmin,4987,maybeveritas,4998,maybeveritas,5000,UPnP,5001,commplex-link,5002,rfe,5003,filemaker,5010,telelpathstart,5011,telelpathattack,5050,mmcc,5100,admd,5101,admdog,5102,admeng,5145,rmonitor_secure,5060,sip,5190,aol,5191,aol-1,5192,aol-2,5193,aol-3,5232,sgi-dgl,5236,padl2sim,5300,hacl-hb,5301,hacl-gs,5302,hacl-cfg,5303,hacl-probe,5304,hacl-local,5305,hacl-test,5308,cfengine,5400,pcduo-old,5405,pcduo,5490,connect-proxy,5432,postgres,5510,secureidprop,5520,sdlog,5530,sdserv,5540,sdreport,5550,sdadmind,5555,freeciv,5560,isqlplus,5631,pcanywheredata,5632,pcanywherestat,5680,canna,5679,activesync,5713,proshareaudio,5714,prosharevideo,5715,prosharedata,5716,prosharerequest,5717,prosharenotify,5800,vnc-http,5801,vnc-http-1,5802,vnc-http-2,5803,vnc-http-3,5900,vnc,5901,vnc-1,5902,vnc-2,5903,vnc-3,5977,ncd-pref-tcp,5978,ncd-diag-tcp,5979,ncd-conf-tcp,5997,ncd-pref,5998,ncd-diag,5999,ncd-conf,6000,X11,6001,X11,1,6002,X11,2,6003,X11,3,6004,X11,4,6005,X11,5,6006,X11,6,6007,X11,7,6008,X11,8,6009,X11,9,6017,xmail-ctrl,6050,arcserve,6101,VeritasBackupExec,6103,RETS-or-BackupExec,6105,isdninfo,6106,isdninfo,6110,softcm,6111,spc,6112,dtspc,6141,meta-corp,6142,aspentec-lm,6143,watershed-lm,6144,statsci1-lm,6145,statsci2-lm,6146,lonewolf-lm,6147,montage-lm,6148,ricardo-lm,6346,gnutella,6400,crystalreports,6401,crystalenterprise,6543,mythtv,6544,mythtv,6547,PowerChutePLUS,6548,PowerChutePLUS,6502,netop-rc,6558,xdsxdm,6588,analogx,6666,irc-serv,6667,irc,6668,irc,6969,acmsoda,6699,napster,7000,afs3-fileserver,7001,afs3-callback,7002,afs3-prserver,7003,afs3-vlserver,7004,afs3-kaserver,7005,afs3-volser,7006,afs3-errors,7007,afs3-bos,7008,afs3-update,7009,afs3-rmtsys,7010,ups-onlinet,7070,realserver,7100,font-service,7200,fodms,7201,dlip,7273,openmanage,7326,icb,7464,pythonds,7597,qaz,7937,nsrexecd,7938,lgtomapper,8000,http-alt,8007,ajp12,8009,ajp13,8021,ftp-proxy,8080,http-proxy,8081,blackice-icecap,8082,blackice-alerts,8443,https-alt,8888,sun-answerbook,8892,seosload,9090,zeus-admin,9100,jetdirect,9111,DragonIDSConsole,9152,ms-sql2000,9535,man,9876,sd,9991,issa,9992,issc,9999,abyss,10000,snet-sensor-mgmt,10005,stel,10082,amandaidx,10083,amidxtape,11371,pksd,12000,cce4x,12345,NetBus,12346,NetBus,13701,VeritasNetbackup,13702,VeritasNetbackup,13705,VeritasNetbackup,13706,VeritasNetbackup,13708,VeritasNetbackup,13709,VeritasNetbackup,13710,VeritasNetbackup,13711,VeritasNetbackup,13712,VeritasNetbackup,13713,VeritasNetbackup,13714,VeritasNetbackup,13715,VeritasNetbackup,13716,VeritasNetbackup,13717,VeritasNetbackup,13718,VeritasNetbackup,13720,VeritasNetbackup,13721,VeritasNetbackup,13722,VeritasNetbackup,13782,VeritasNetbackup,13783,VeritasNetbackup,15126,swgps,16959,subseven,17007,isode-dua,17300,kuang2,18000,biimenu,18181,opsec_cvp,18182,opsec_ufp,18183,opsec_sam,18184,opsec_lea,18185,opsec_omi,18187,opsec_ela,19150,gkrellmd,20005,btx,22273,wnn6,22289,wnn6_Cn,22305,wnn6_Kr,22321,wnn6_Tw,22370,hpnpd,26208,wnn6_DS,27000,flexlm0,27001,flexlm1,27002,flexlm2,27003,flexlm3,27004,flexlm4,27005,flexlm5,27006,flexlm6,27007,flexlm7,27008,flexlm8,27009,flexlm9,27010,flexlm10,27374,subseven,27665,Trinoo_Master,31337,Elite,32770,sometimes-rpc3,32771,sometimes-rpc5,32772,sometimes-rpc7,32773,sometimes-rpc9,32774,sometimes-rpc11,32775,sometimes-rpc13,32776,sometimes-rpc15,32777,sometimes-rpc17,32778,sometimes-rpc19,32779,sometimes-rpc21,32780,sometimes-rpc23,32786,sometimes-rpc25,32787,sometimes-rpc27,44334,tinyfw,44442,coldfusion-auth,44443,coldfusion-auth,47557,dbbrowse,49400,compaqdiag,54320,bo2k";
        String[] serviceList = _serviceList.split(",");
        for (int i = 0; i < serviceList.length; i += 2)
        {
            if (serviceList[i].equals(Integer.toString(port)))
            {
                return serviceList[i + 1];
            }
        }
        if (symbmode)
        {
            return "?";
        } else 
        {
            return "UNKNOWN"; 
        } 
    }

    public static String formatQuest(String text) 
    {   
        String _text = text.replace("?", " ");
        return _text;
    }

    public static String fileChoice(String file) throws Exception
    {
        if (new File(CURRENT_DIRECTORY + SLASH_TYPE + file).exists())
        {
            return CURRENT_DIRECTORY + SLASH_TYPE + file;
        } else if (file.contains("\\") || file.contains("/"))
        {
            return file;
        } else 
        {
            return CURRENT_DIRECTORY + SLASH_TYPE + new File(file).getName();
        }
    }
    
    public static String readFile(String file, String datatype) throws Exception
    {
        try 
        {
            if (datatype.equals(readtype.STRING))
            {
                if (new File(file).exists()) {
                    BufferedReader ffile = new BufferedReader(new FileReader(file));
                    String text  = "";
                    String _text = "";
                    while ((_text = ffile.readLine()) != null)
                    {
                        text += _text;
                    }
                    ffile.close(); 
                    return text;
                } else 
                {
                    return "error";
                }
            } else if (datatype.equals(readtype.BYTE_TO_HEX))
            {
                if (new File(file).exists()) 
                {
                    Path ffile = Paths.get(file);
                    String text = bytesToHex(Files.readAllBytes(ffile));
                    return text;
                } else 
                {
                    return "error";
                }
            } else 
            {
                return "error";
            }
        } catch (FileNotFoundException e)
        {
            return "error";
        }
    }

    public static String bytesToHex(byte[] bytes) throws Exception
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static List<File> getFileList(String Folder, String mode) throws Exception
    {
        if (mode.equals("all")) 
        {   
            subDir = new ArrayList<File>();
            getSub(new File(Folder));
            return subDir;
        } else {
        File folder = new File(Folder);
        List<File> fileList = new ArrayList<File>();
        for (File file : folder.listFiles()) 
        {
            fileList.add(file);
        }
        return fileList;
        
        }  
    }

    public static void getSub(File Folder) throws Exception
    {
        subDir.add(new File(Folder.getAbsolutePath()));
        if (Folder.listFiles() == null) 
        {

        } else 
        {
            List<File> Folders = new ArrayList<File>(Arrays.asList(Folder.listFiles()));
            if (Folders.equals(null)) 
            {

            } else 
            {
                for (File FolderQ : Folders) 
                {
                    if (FolderQ.isFile()) 
                    {
                        subDir.add(new File(FolderQ.getAbsolutePath()));
                    } else {
                    getSub(FolderQ); 
                    } 
                }
            }
        }
    }

    public static void copyClip(String text)
    {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard copyclip = defaultToolkit.getSystemClipboard();
        copyclip.setContents(new StringSelection(text), null);
    }

    public static String getParent(String path) throws Exception 
    {
        String newpath = path;
        if (path.endsWith(SLASH_TYPE) || path.endsWith(FAKE_SLASH_TYPE))
        {
            newpath = path.substring(0, path.length()-1);
        }
        int index = newpath.lastIndexOf(SLASH_TYPE); 
        if (index == -1)
        {
            index = newpath.lastIndexOf(FAKE_SLASH_TYPE);
        }
        if (path.substring(0, index).equals(""))
        {
            return newpath;
        }
        return path.substring(0, index);
    }

    public static boolean setCurrentDirectory(String directoryName, String SLASH_TYPE, String FAKE_SLASH_TYPE) throws Exception
    {
        if (directoryName.replace(".", "").isEmpty() && !directoryName.equals(".."))
        {
            return false;
        }
        if (!(new File(directoryName).isFile() || new File((CURRENT_DIRECTORY + SLASH_TYPE + directoryName).replace(FAKE_SLASH_TYPE, SLASH_TYPE).replace(SLASH_TYPE + SLASH_TYPE, SLASH_TYPE)).isFile()))
        {
            if (new File(directoryName).exists() && (directoryName.contains(SLASH_TYPE) || directoryName.contains(FAKE_SLASH_TYPE)))
            {
                CURRENT_DIRECTORY = directoryName.replace(FAKE_SLASH_TYPE, SLASH_TYPE).replace(SLASH_TYPE + SLASH_TYPE, SLASH_TYPE);
                return true;
            }   
            else if (directoryName.equals("..")) 
            {
                CURRENT_DIRECTORY = getParent(CURRENT_DIRECTORY).replace(FAKE_SLASH_TYPE, SLASH_TYPE).replace(SLASH_TYPE + SLASH_TYPE, SLASH_TYPE);
                return true;
            }  
            else if (new File(CURRENT_DIRECTORY + SLASH_TYPE + directoryName).exists()) 
            {
                CURRENT_DIRECTORY = (CURRENT_DIRECTORY + SLASH_TYPE + directoryName).replace(FAKE_SLASH_TYPE, SLASH_TYPE).replace(SLASH_TYPE + SLASH_TYPE, SLASH_TYPE);
                return true;
            } else {
                return false;
            }
        } else 
        {
            return false;
        }
    }      

    public static boolean login(String initPath, String SLASH_TYPE)
    {
        try 
        {
            while (true)
            {
                try 
                {
                    Console console = System.console();
                    String USERLOG = System.getenv("USERDOMAIN");
                    if (System.getenv("USERDOMAIN") == null) 
                    {
                        USERLOG = System.getenv("HOME").replace("/home/", "").replace("/", "");
                    }
                    System.out.println(log.INFOREGULAR + "Login:\n");
                    System.out.println(log.INFOREGULAR + "Account: " + USERLOG);
                    System.out.print(log.INFOREGULAR + "Password: ");
                    char[] passwordChars = console.readPassword();
                    String passwordString = new String(passwordChars);

                    BufferedReader accountFile = new BufferedReader(new FileReader(initPath + SLASH_TYPE + "sshell_account"));

                    String passwdEncrypted = "";
                    String passwdEncryptedPart = "";
                    while ((passwdEncryptedPart = accountFile.readLine()) != null)
                    {
                        passwdEncrypted += passwdEncryptedPart;
                    }


                    String passwdDecrypted = crypter("decrypt", passwdEncrypted);
                    accountFile.close();
                    if (passwordString.equals(passwdDecrypted))
                    {
                        return true;
                    } else {
                        System.out.println("\n" + log.ERROR + "Incorrect password.");
                        Thread.sleep(3000);
                        return false;
                    }

                } catch (Exception e) 
                {
                    return false;
                }
            }

        } catch (Exception e) 
        {
            return false;
        }
    }

    public static boolean register(String initPath, String SLASH_TYPE, String aReset) throws Exception
    {
        try
        {
            if (aReset.equals("reset")) 
            {
                try 
                {
                    BufferedReader accountFile = new BufferedReader(new FileReader(initPath + SLASH_TYPE + "sshell_account"));
                    
                    Console console = System.console();
                    System.out.print(log.INFOREGULAR + "Current Password: ");
                    try {
                    char[] passwordChars = console.readPassword();
                    String passwordString = new String(passwordChars);
                    String passwdEncrypted = "";
                    String passwdEncryptedPart = "";
                    while ((passwdEncryptedPart = accountFile.readLine()) != null)
                    {
                        passwdEncrypted += passwdEncryptedPart;
                    }
                    String passwdDecrypted = crypter("decrypt", passwdEncrypted);
                    accountFile.close();
                    if (passwdDecrypted.equals(passwordString))
                    {
                        String NO_ERROR = "0";
                        System.out.print(log.INFOREGULAR + "New Password: ");
                        char[] passwordChars2 = console.readPassword();
                        String passwordString2 = new String(passwordChars2);
                        for (String symb: ILLEGAL_SYMBOL)
                        {
                            if (passwordString2.contains(symb)){
                                NO_ERROR = "1";
                                System.out.println("\n" + log.ERROR + "Password invalid: Illegal character.\n");
                                return false;
                            }

                            if (passwordString2.length() < 5 || passwordString2.length() > 16)
                            {
                                NO_ERROR = "1";
                                System.out.println("\n" + log.ERROR + "Password invalid: It must contain from 5 to 16 characters.\n");
                                return false;
                                
                            }
                        }

                        if (NO_ERROR.equals("0"))
                        {
                            try 
                            {
                                FileWriter accountFile2 = new FileWriter(initPath + SLASH_TYPE + "sshell_account");
                                String passwdEncrypted2 = crypter("encrypt", passwordString2);
                                accountFile2.write(passwdEncrypted2);
                                accountFile2.close();
                                return true;
                            } catch (Exception e) 
                            {
                                return false;
                            }
                            
                        } else {
                            return false;
                        }

                    } else {
                        System.out.println("\n" + log.ERROR + "Incorrect password.\n");
                        return false;
                    }
                } catch (Exception e) 
                {
                    System.out.println("\n" + log.ERROR + "Failed to change password.\n");
                    return false;
                }
                } catch (Exception e) 
                {
                    System.out.println(log.ERROR + "Failed to change password.\n");
                    return false;
                }
 
            } else
            {
                while (true)
                {
                    String NO_ERROR = "0";
                    try 
                    {
                        Console console = System.console();
                        String USERLOG = System.getenv("USERDOMAIN");
                        if (System.getenv("USERDOMAIN") == null) 
                        {
                                USERLOG = System.getenv("HOME").replace("/home/", "").replace("/", "");
                        }
                        System.out.println(log.INFOREGULAR + "Account Creation:\n");
                        System.out.println(log.INFOREGULAR + "Account: " + USERLOG);
                        System.out.print(log.INFOREGULAR + "Password: ");
                        char[] passwordChars = console.readPassword();
                        String passwordString = new String(passwordChars);
                        
                        for (String symb: ILLEGAL_SYMBOL)
                        {
                            if (passwordString.contains(symb)){
                                NO_ERROR = "1";
                                System.out.println("\n" + log.ERROR + "Password invalid: Illegal character.");
                                Thread.sleep(2200);
                                screenClear();
                                break;
                            }

                            if (passwordString.length() < 5 || passwordString.length() > 16)
                            {
                                NO_ERROR = "1";
                                System.out.println("\n" + log.ERROR + "Password invalid: It must contain from 5 to 16 characters.");
                                Thread.sleep(2200);
                                screenClear();
                                break;
                            }
                        }

                        if (NO_ERROR.equals("0"))
                        {
                            try 
                            {
                                FileWriter accountFile = new FileWriter(initPath + SLASH_TYPE + "sshell_account");
                                String passwdEncrypted = crypter("encrypt", passwordString);
                                accountFile.write(passwdEncrypted);
                                accountFile.close();
                                return true;
                            } catch (Exception e) 
                            {
                                return false;
                            }
                            
                        }

                    } catch (Exception e) 
                    {
                        return false;
                    }   
                }
            }

        } catch (Exception e) 
        {
            return false;
        }
    }

    public static boolean accountExists(String initPath, String SLASH_TYPE) 
    {
        File accountFile = new File(initPath + SLASH_TYPE + "sshell_account"); 
        return accountFile.exists();
    }

    public static void exiter(int flag)
    {
        System.out.print(ansi.NO_COLOR);
        System.exit(flag);
    }

    public static String crypter(String mode, String text) throws Exception
    {
        return callPack(new String[]{"crypt", mode, text});
    }

    public static String getVendor(String MAC) throws Exception
    {
        String[] vendorList = _vendorList.split("\\#\\$");
        for (int i = 0; i < vendorList.length; i += 2)
        {
            if (vendorList[i].toUpperCase().replace(":", "").equals(MAC.substring(0, 8).replace(":", "")))
            {
                return vendorList[i + 1];
            }
        }
        return "UNKNOWN";
    }

    public static String getResponse(String URL) 
    {
        int responseCode = 400;
        for (int i = 0; i < 3; i++) 
        { 
            try 
            {
                URL url = new URL(URL);
                StringBuilder result = new StringBuilder();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                responseCode = conn.getResponseCode();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                return result.toString();
            } catch (IOException e) 
            { 
                if (responseCode != 200) 
                {
                    continue;
                } else {
                    
                    return "error";
                }
            } catch (Exception e) 
            {
                return "error";
            }
        }
        return "error";
    }

    public static Map<String, String> ARP(String range) throws Exception
    {
        String[] args;
        if (range == null) 
        {
            args = new String[]{"ARP"};  
        } else 
        {
            args = new String[]{"ARP", range};
        }
        String _result = callPack(args);
        if (_result.contains("error")) 
        {
            if (_result.contains("error.syntax"))
            {
                Map<String, String> result = new HashMap<String, String>();
                result.put("ERROR", "syntax");
                return result;
            } else 
            {
                Map<String, String> result = new HashMap<String, String>();
                result.put("ERROR", "general");
                return result;
            }
            
        } else 
        {
            _result = _result.replace("{", "").replace("}", "").replace("':", "!").replace("'", "");
            Map<String, String> result = new HashMap<String, String>();
            for (String res : _result.split(","))
            {
                result.put(res.split("!")[0], res.split("!")[1]);
            }
            return result;
        }
    }

    public static String getfqdn(String IP) throws Exception
    {
        return callPack(new String[]{"gethost", IP});
    }

    public static String callPack(String[] args) throws Exception
    {
        String wdir = System.getProperty("user.dir") + SLASH_TYPE + "bin" + SLASH_TYPE + "app" + SLASH_TYPE + "extension" + SLASH_TYPE;
        Process p = Runtime.getRuntime().exec("python " + wdir + "exPack.py " + String.join(" ", args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        return reader.readLine();
    }

    public static String input(String text) throws Exception
    {
        if (text.equals(null))
        {
        } else {
            System.out.print(text);
        }
        BufferedReader in_put = new BufferedReader(new InputStreamReader(System.in));
        return in_put.readLine();
    }

    public static void screenClear() 
    {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

}