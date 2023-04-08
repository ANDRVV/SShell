import os, re, subprocess, platform
os.system("pip install pynetsys --user")
javaVersion = re.search(r'"[0-9\._]*"', subprocess.check_output("java -version", shell = True, stderr = subprocess.STDOUT).decode().split("\r")[0]).group().replace('"', '')
if "19" in javaVersion:
    print("[OK] SETUP COMPLETED")
    response = input("Run SShell? [N/y] ")
    if response.lower() == "y":
        if "windows" in platform.platform().lower():
            os.system("run.bat")
        else:
            os.system("bash run.sh")
else:
    print("[ERROR] Java SE 19 required.")
