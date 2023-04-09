import sys, pynetsys, socket, time
import concurrent.futures
import binascii
from functools import partial
import logging
logging.getLogger("scapy.runtime").setLevel(logging.ERROR)
from scapy.layers.inet import IP, ICMP, sr1
from scapy.all import sniff, Raw
from cryptography.fernet import Fernet


if sys.argv[1] == "ARP":
    if len(sys.argv) > 2:
        result = pynetsys.tool.arp(sys.argv[2])
        if pynetsys.isOnline("google.com"):
            if str(result) == "{}":
                if sr1(IP(dst = socket.gethostbyname("8.8.8.8"))/ICMP(id = 1), timeout = 1, verbose = 0) is None:
                    print("error.syntax")
                else:
                    print("error.general")
            else:
                print(result)
        else:
            print("error.general")    
    else:
        result = pynetsys.tool.arp()
        if pynetsys.isOnline("google.com"):
            if str(result) == "{}":
                if sr1(IP(dst = socket.gethostbyname("8.8.8.8"))/ICMP(id = 1), timeout = 1, verbose = 0) is None:
                    print("error.syntax")
                else:
                    print("error.general")
            else:
                print(result)
        else:
            print("error.general") 

elif sys.argv[1] == "isonline":
    print(pynetsys.isOnline(sys.argv[2]))

elif sys.argv[1] == "gethost":
    print(socket.getfqdn(sys.argv[2]))

elif sys.argv[1] == "scanport":
    result = []
    def tcpportscan(dst_port):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        if sock.connect_ex((socket.gethostbyname(sys.argv[2]), dst_port)) == 0:
            result.append(str(dst_port) + ".OPEN")
        sock.close()
    with concurrent.futures.ThreadPoolExecutor(max_workers = 500) as executor:
        for dst_port in range(int(sys.argv[3]), int(sys.argv[4]) + 1):
            executor.submit(tcpportscan, dst_port)   
    print(result) 

elif sys.argv[1] == "ping":
    host = sys.argv[2]
    seq = sys.argv[3]
    PACKET = IP(dst = host)/ICMP(id = 1, seq = int(seq))
    clockbefore = time.time()
    response = sr1(PACKET, timeout = 2, verbose = 0)
    clockafter = time.time()
    if response is None:
        print(f"ERROR,PACKET-LOSS")
    elif int(response[ICMP].type) == 3:
        print(f"ERROR,PACKET-LOSS")
    else:
        delay = str(clockafter - clockbefore).split(".")[0] + "." + str(clockafter - clockbefore).split(".")[1][0:3]
        print(f"{response[IP].ttl},{delay} ms")

elif sys.argv[1] == "sniffer":
    result = []
    
    def qnflag(_flag):
        flag = ""
        for fl in _flag:
            if len(fl.replace("S", "SYN")) > 1:
                flag += "SYN+"
            elif len(fl.replace("A", "ACK")) > 1:
                flag += "ACK+"
            elif len(fl.replace("F", "FIN")) > 1:
                flag += "FIN+"
            elif len(fl.replace("U", "URG")) > 1:
                flag += "URG+"
            elif len(fl.replace("P", "PSH")) > 1:
                flag += "PSH+"
            elif len(fl.replace("R", "RST")) > 1:
                flag += "RST+"
            elif len(fl.replace("E", "ECE")) > 1:
                flag += "ECE+"
            elif len(fl.replace("C", "CWR")) > 1:
                flag += "CWR+"
            elif len(fl.replace("N", "NS")) > 1:
                flag += "NS+"
        return flag.strip("+")

    def _getLayers(PACKET):
        counter = 0
        while True:
            layer = PACKET.getlayer(counter)
            if layer is None:
                break
            yield layer
            counter += 1

    def getLayers(PACKET):
        layers = []
        for x in _getLayers(PACKET):
            layers.append(x.name)
        layers = list(dict.fromkeys(layers))
        try:
            layers.remove("Ethernet")
        except:
            pass
        return layers

    def assemblrpkt(result, PACKET):
        try:
            layer = getLayers(PACKET)
            try:   
                SOURCEIP = PACKET[layer[0]].src
            except:
                try:   
                    SOURCEIP = PACKET[layer[0]].psrc
                except:
                    try:   
                        SOURCEIP = PACKET[layer[0]].hwsrc
                    except:
                        pass
            try:   
                DESTIP = PACKET[layer[0]].dst
            except:
                try:   
                    DESTIP = PACKET[layer[0]].pdst
                except:
                    try:   
                        DESTIP = PACKET[layer[0]].hwdst
                    except:
                        pass
            try:   
                SOURCEPORT = str(PACKET[layer[1]].sport).upper()
            except:
                pass
            try:   
                DESTPORT = str(PACKET[layer[1]].dport).upper()
            except:
                pass
            try:
                if layer[0] == "ARP":
                    if str(PACKET[layer[0]].op) == "1":
                        op = "QUESTION"
                    else:
                        op = "REPLY"
                    ARGS = "<ARP>$$$[" + op + "]"
                elif layer[1] == "TCP":
                    ARGS = "<TCP>$$$[" + str(qnflag(PACKET[layer[1]].flags)) + "] SEQ: " + str(PACKET[layer[1]].seq) + ", ACK: " + str(PACKET[layer[1]].ack) + ", WIN: " + str(PACKET[layer[1]].window) + ", LEN: " + str(PACKET[layer[0]].len)
                elif layer[1] == "ICMP":
                    if str(PACKET[layer[1]].type) == "0":
                        icmptype = "ECHO-REPLY"
                    elif str(PACKET[layer[1]].type) == "8":
                        icmptype = "ECHO-REQUEST"
                    else:
                        icmptype = str(PACKET[layer[1]].type)
                    ARGS = "<ICMP>$$$[" + icmptype + "] ID: " + str(PACKET[layer[1]].id) + ", TTL: " + str(PACKET[layer[0]].ttl)
                elif layer[1] == "UDP":
                    layer2 = layer
                    try:
                        layer2.remove("IP")
                    except:
                        pass
                    try:
                        layer2.remove("IPv6")
                    except:
                        pass
                    try:
                        layer2.remove("UDP")
                    except:
                        pass
                
                    if len(layer2) > 1:
                        del layer2[0]
                    try:
                        layer2.remove("Raw")
                    except:
                        pass
                    udplayers = ""
                    for udplayer in layer2:
                        udplayers += udplayer.replace(" ", "-") + ","
                    udplayers = udplayers.strip(",").upper()
                    try:
                        ARGS = "<UDP>$$$[" + udplayers + "] LEN: " + str(PACKET["IP"].len)
                    except:
                        ARGS = "<UDP>$$$[" + udplayers + "] LEN: " + str(PACKET["IPv6"].len)
            except:
                pass

            try:
                PREARGS = "[" + SOURCEIP + ";" + SOURCEPORT + "->" + DESTIP + ";" + DESTPORT + "]"   
            except:
                PREARGS = "[" + SOURCEIP + "->" + DESTIP + "]" 

            rawshowable = str(sys.argv[3])
            if rawshowable == "true":
                rawshowable = True
            else:
                rawshowable = False
            haveraw = False
            try:
                rawtext = binascii.hexlify(PACKET[Raw].load).decode()
                haveraw = True
            except:
                pass

            try:
                if haveraw:
                    if rawshowable:
                        result.append(PREARGS + "$$$" + ARGS + " [" + rawtext + "]")
                    else:
                        result.append(PREARGS + "$$$" + ARGS + " [RAW]")
                else:
                    result.append(PREARGS + "$$$" + ARGS + "")
            except:
                unalllayer = ""
                try:
                    layer.remove("Raw")
                except:
                    pass
                for inclayer in layer:
                    unalllayer += inclayer.replace(" ", ".").strip(".") + "-"
                unalllayer = unalllayer.strip("-")
                if haveraw:
                    if rawshowable:
                        result.append(" $$$<UNEXPECTED>$$$[" + unalllayer + "] [" + rawtext + "]")
                    else:
                        result.append(" $$$<UNEXPECTED>$$$[" + unalllayer + "] [RAW]")
                else:
                    result.append(" $$$<UNEXPECTED>$$$[" + unalllayer + "]")
        except:
            pass
    
    sniff(timeout = int(sys.argv[2]), prn = partial(assemblrpkt, result))
    print("|".join(result))

elif sys.argv[1] == "crypt":
    key = Fernet(b"vN6u_6WX7wgZozKNwtPb7Ihu5-XNvHNvbPH_tadLf5o=")
    
    if sys.argv[2] == "encrypt":
        print(key.encrypt(sys.argv[3].encode()).decode())
    else:
        print(key.decrypt(sys.argv[3].encode()).decode())

        
    
    
    


    
