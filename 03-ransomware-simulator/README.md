# Ransomware Simulator
A ransomware simulator is a tool or program used to simulate the behavior of a ransomware attack. It is commonly used in cybersecurity training, testing, research, or for creating awareness within organizations. These simulators mimic the actions of real ransomware attacks, but they don't encrypt data or cause harm to systems. A common use case is to set up a simulated ransomware infection where files appear encrypted, and users are "asked" to pay a ransom to decrypt them, simulating the threat without actually causing harm.

This is aligned to the actions on objectives phase of the cyber kill chain as the attacker has potentially gone through all the stages before accomplishing their final goal which could be sensitive data theft(data exfiltration) followed by a ransomware attack.

This tactic ensures the attacker has full control over the affected systems as the systems would be inoperable(the majority of the data is encrypted, followed by secure deletion to prevent data recovery ) and the victim would be forced to negotiate with the attacker to receive decryption keys in exchange of cryptocurrency.

## Plaintext
The original file **"important_document.txt"**

![expected output](plaintext.png)


## Program Output
Output from our program

![code output](code_output.png)

Encrypted File **"important_document.txt.encrypted"**

![encrypted output](encrypted_output.png)

## Next Steps
* Upgrade the program to dynamically receive a list of directeries to be encrypted.
* Upgrade the program to send the encryption key to the attackers command and control(C2)
* Test the execution against common security products to ensure it is evasive in nature.
