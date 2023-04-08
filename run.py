import os, re, subprocess, platform

os.chdir(os.path.dirname(__file__))
sver = open("setupVerification", "r")
code = sver.read()
sver.close()
if "0" in code:
    os.system("pip install pynetsys --user")
    os.system("pip install cryptography")
    os.system("pip install paramiko")
    os.system("pip install requests")
    javaVersion = re.search(r'"[0-9\._]*"', subprocess.check_output("java -version", shell = True, stderr = subprocess.STDOUT).decode().split("\r")[0]).group().replace('"', '')
    if "19" in javaVersion:
        pcapver = input("Manually install winpcap/npcap. [N/ok] ")
        if "ok" in pcapver.lower():
            wver = open("setupVerification", "w")
            wver.write("1")
            wver.close()
            print("[OK] SETUP COMPLETED")
            response = input("Run SShell? [N/y] ")
            if response.lower() == "y":
                if "windows" in platform.platform().lower():
                    os.system("@echo off & cls & java --enable-preview -jar jar\SShell.jar")
                else:
                    os.system("clear && java --enable-preview -jar jar/SShell.jar &> /dev/null")
        else:
            pass
    else:
        print("[ERROR] Java SE 19 required.")
else:
    if "windows" in platform.platform().lower():
        os.system("@echo off & cls & java --enable-preview -jar jar\SShell.jar")
    else:
        os.system("clear && java --enable-preview -jar jar/SShell.jar &> /dev/null")
