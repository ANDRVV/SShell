*Current Version*: `v0.0.2`

Required: 

1. **Python3** 
2. **JDK + Java**
3. **Npcap**/**Winpcap** (On windows there is auto-install from run.py).

Compatible with:

1. **Windows 8.1+**
2. **Linux** (Recommended **Kali** distro)  -  Run as **Root** only.
3. **MacOS**                                -  Run as **Root** only.
4. **Termux**                               -  Run as **Root** only. (If the setup doesn't work enter `pkg install python-pip`)

# What is SShell?
**SShell** is the acronym of **Simple Shell**, it's a terminal for an easy and instant use where there are default commands and those that can be added externally (version 0.0.3).

**SShell** was created for educational use and easy to use, precisely because everyone can use it.

![SShell](https://raw.githubusercontent.com/ANDRVV/SShell/main/MDimages/maxSShell.jpg)

## How to install:

Start cloning this repository:

`git clone https://github.com/ANDRVV/SShell`

Now change path:

`cd SShell`

Now you can run:

`python run.py`

## How to bypass setup:

`python run.py --bypass`

# Help commands:

    exit/Control+C:         Exit from SShell.         exit                                                 (Exit from SShell)
    
    clr:                    Clear the screen.         clr                                                  (Clear the screen)
    
    user:                   Change password user.     user -r/--reset                                      (Change user password)
    
    cd:                     Change directory.         cd                                                   (Return current directory)
                                                      cd {dir}                                             (Set directory)
                                                      
    ls:                     Explore folders.          ls                                                   (Get all file/dirs of directory)
                                                      ls [-s/--sub-directories]                            (Get all file/dirs of directory and all sub file/dirs)
                                                      
    mk:                     Create a file/directory.  mk [-d/--directory] {dir}                            (Make a directory)
                                                      mk [-f/--file] {file}                                (Make a file)
                                                      
    write:                  Write a file.             write {file} {text}                                  (Write a file)
                                                      write {file} [-b/--bytes] {hex}                      (Write a file in bytes, example: write hello.txt -b a34d53)
                                                      
    del:                    Del a file/directory.     del {file/dir}                                       (Delete a file/directory)
    
    ren:                    Rename a file/directory.  ren {file/dir}                                       (Rename a file/directory)  
    
    copy:                   Copy a file/directory.    copy {file/dir(src)} {file/dir(dst)}                 (Copy a file/dir to another directory)
    
    mem:                    Set, get consts.          mem [-a/--all]                                       (Get list of stored object)
                                                      mem [-r/--remove] {object}                           (Remove stored object)
                                                      mem {object}                                         (Get value of stored object)
                                                      mem {object} {value}                                 (Set a const with a value)
    
    extract:                Read a file.              extract {file}                                       (Read a file)
    
    arp:                    Send ARP requests.        arp                                                  (Get devices connected to router)
                                                      arp {range}                                          (Get devices connected to router)
    
    portscanner:            Scan TCP ports.           portscanner {host}                                   (Scan port from 1 to 1024) 
                                                      portscanner {host} [-r/--range] [{port}-{port}]      (Scan port with range)
    
    ping:                   Ping a server/device.     ping {host}                                          (Ping a server/device with 8 count)
                                                      ping {host} [-c/--count] {count}                     (Ping a server/device with range count)
    
    sniffer:                Capture WiFi traffic.     sniffer {timeout}                                    (Capture WiFi traffic)
                                                      sniffer {timeout} [-s/--show-raw]                    (Capture WiFi traffic and show raw)
    
    hexanalysis:            Hexdump a file/text.      hexanalysis {hex}                                    (Hexdump a text)
                                                      hexanalysis [-f/--file] {file}                       (Hexdump a file)
                                                      hexanalysis [-f/--file] {file} [-c/--copy-clipboard] (Hexdump a file and copy to clipboard)

It is forbidden to use '', for spaces use '**?**'.

Example:

`mk -f Hello?World.txt`

All the commands are made with a "timer", just because you can't use **Control-C**.

For safety and to immediately cancel an action press **Control-C**.

−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−

Copyright 2023 Andrea Vaccaro

Apache License 2.0
