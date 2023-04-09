package app;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.mozilla.universalchardet.*;
import org.apache.commons.codec.binary.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

@SuppressWarnings("deprecation")

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
        String _serviceList = readFile(System.getProperty("user.dir") + SLASH_TYPE + "bin" + SLASH_TYPE + "app" + SLASH_TYPE + "portServices", readtype.STRING);
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
        String _vendorList = readFile(System.getProperty("user.dir") + SLASH_TYPE + "bin" + SLASH_TYPE + "app" + SLASH_TYPE + "macVendors", readtype.STRING);;
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