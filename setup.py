import os, re, subprocess, platform
sver = open("setupVerification", "r")
code = sver.read()
sver.close()
if code == "0":
    os.system("pip install pynetsys --user")
    javaVersion = re.search(r'"[0-9\._]*"', subprocess.check_output("java -version", shell = True, stderr = subprocess.STDOUT).decode().split("\r")[0]).group().replace('"', '')
    if "19" in javaVersion:
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
        print("[ERROR] Java SE 19 required.")
else:
    if "windows" in platform.platform().lower():
        os.system("@echo off & cls & java --enable-preview -jar jar\SShell.jar")
    else:
        os.system("clear && java --enable-preview -jar jar/SShell.jar &> /dev/null")
