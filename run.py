import os, re, subprocess, platform, sys

os.chdir(os.path.dirname(__file__))
sver = open("setupVerification", "r")
code = sver.read()
sver.close()
if len(sys.argv[1]) < 2:
    args = "none"
else: 
    args = sys.argv[1]
if "0" in code and args == "--bypass":
    if "windows" in platform.platform().lower():
        os.system("@echo off & javac -d bin --class-path bin --add-modules juniversalchardet,org.apache.commons.codec --module-path lib src\\app\\SShell.java")
    else:
        os.system("javac -d bin --class-path bin --add-modules juniversalchardet,org.apache.commons.codec --module-path lib src/app/SShell.java &> /dev/null")
    if "windows" in platform.platform().lower():
        os.system('@echo off & cls & java --enable-preview -cp "bin;lib\\commons-codec-1.15.jar;lib\\juniversalchardet-1.0.3.jar" app.SShell')
    else:
        os.system('clear && java --enable-preview -cp "bin:lib/commons-codec-1.15.jar:lib/juniversalchardet-1.0.3.jar" app.SShell')
if "0" in code and args != "--bypass":
    print("Setup in progress...")
    if "windows" in platform.platform().lower():
        os.system("@pip install pynetsys --user")
        os.system("@pip install cryptography --user")
        os.system("@pip install paramiko --user")
        os.system("@pip install requests --user")
    else:
        os.system("pip install pynetsys --user")
        os.system("pip install cryptography --user")
        os.system("pip install paramiko --user")
        os.system("pip install requests --userl")
    javaVersion = re.search(r'"[0-9\._]*"', subprocess.check_output("java -version", shell = True, stderr = subprocess.STDOUT).decode().split("\r")[0]).group().replace('"', '')
    if javaVersion is not None:
        if "windows" in platform.platform().lower():
            os.system("npcap-1.73.exe") 
        print() 
        pcapver = input("Manually install winpcap/npcap. [N/ok] ")
        if "ok" in pcapver.lower():
            if "windows" in platform.platform().lower():
                os.system("@echo off & javac -d bin --class-path bin --add-modules juniversalchardet,org.apache.commons.codec --module-path lib src\\app\\SShell.java")
            else:
                os.system("javac -d bin --class-path bin --add-modules juniversalchardet,org.apache.commons.codec --module-path lib src/app/SShell.java &> /dev/null")
            wver = open("setupVerification", "w")
            wver.write("1")
            wver.close()
            print()
            print("[OK] SETUP COMPLETED")
            print()
            response = input("Run SShell? [N/y] ")
            if response.lower() == "y":
                if "windows" in platform.platform().lower():
                    os.system('@echo off & cls & java --enable-preview -cp "bin;lib\\commons-codec-1.15.jar;lib\\juniversalchardet-1.0.3.jar" app.SShell')
                else:
                    os.system('clear && java --enable-preview -cp "bin:lib/commons-codec-1.15.jar:lib/juniversalchardet-1.0.3.jar" app.SShell')
        else:
            pass
    else:
        print("[ERROR] Java required.")
else:
    if "windows" in platform.platform().lower():
        os.system('@echo off & cls & java --enable-preview -cp "bin;lib\\commons-codec-1.15.jar;lib\\juniversalchardet-1.0.3.jar" app.SShell')
    else:
        os.system('clear && java --enable-preview -cp "bin:lib/commons-codec-1.15.jar:lib/juniversalchardet-1.0.3.jar" app.SShell')
