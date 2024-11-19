# Keylogger
A keylogger is a type of software that secretly records keystrokes (key presses and mouse clicks) made on a computer or mobile device. Its primary purpose is to capture everything typed, including passwords, messages, and other sensitive information, without the user's knowledge. Although keyloggers are often used for malicious purposes, they can also serve legitimate functions in certain contexts, such as parental controls or corporate monitoring.

In this project, I developed a Java-based keylogger that I tested on Windows. The keylogger transmits the captured information every second. Additionally, I created a demo server program (keyloggerServer.java) to demonstrate how the attacker receives the captured data.

To simulate typical user behavior, I opened Notepad and entered a username and password, mirroring how people usually input login credentials in a web browser. The screenshots below illustrate how the keylogger captures everything the user types, along with mouse clicks, and how the attacker receives the same information.

This keylogger project aligns with the reconnaissance and weaponization phases of the cyber kill chain, as it uses legitimate operating system hooks to harvest sensitive information.


## Live Capture
Keylogger client output

![keylogger client](keystrokes_client_demo.png)


## Captured Data
Keylogger server captures data

![keylogger server](keystrokes_server_demo.png)


## Next Steps
*We need to conduct further testing of our keylogger program against various security software to ensure it goes undetected. If it is detected, we must enhance its evasive capabilities. 

*Additionally, we should improve the organization of the captured data being sent to the attacker to enhance their user experience throughout this phase of the attack.
