# What is SShell?
**SShell** is the acronym of **Simple Shell**, it is a terminal for an easy and instant use where there are default commands and those that can be added externally (version 0.0.2).

**SShell** was created for educational use and easy to use, precisely because everyone can use it.

![SShell](https://raw.githubusercontent.com/ANDRVV/SShell/main/MDimages/maxSShell.jpg)

## How to install

Start cloning this repository:

`git clone https://github.com/ANDRVV/SShell`

Now change path:

`cd SShell`

Now you can run:

`Windows: run.bat`

`Linux/MacOS: bash run.sh`

# Help commands

    exit/Control+C:         Exit from SShell.         exit          
    
    clr:                    Clear the screen.         clr
    
    user -r/--reset:        Change password user.     user -r/--reset
    
    cd:                     Change directory.         cd                                            (Return current directory)
                                                      cd {dir}                                      (Set directory)
                                                      
    ls:                     Explore folders.          ls                                            (Get all file/dirs of directory)
                                                      ls [-s/--sub-directories]                     (Get all file/dirs of directory and all sub file/dirs)
                                                      
    mk:                     Create a file/directory.  mk [-d/--directory] {dir}                     (Make a directory)
                                                      mk [-f/--file] {file}                         (Make a file)
                                                      
    write:                  Write a file.             write {file} {text}                           (Write a file)
                                                      write {file} [-b] {hex}                       (Write a file in bytes, example: write hello.txt -b a34d536b)
                                                      
    del:                    Del a file/directory.
    ren:                    Rename a file/directory.
    copy:                   Copy a file/directory.
    mem:                    Set, get consts.
    extract:                Read a file.
    arp:                    Send ARP requests.
    portscanner:            Scan TCP ports.
    ping:                   Ping a server/device.
    sniffer:                Capture WiFi traffic.
    hexanalysis:            Hexdump a file/text.

It is forbidden to use '', for spaces use '?'.

Example:

`mk -f Hello?World.txt`

All the commands are made with a "timer", just because you can't use Control-C.

For safety and to immediately cancel an action press Control-C.
