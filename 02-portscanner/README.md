# Port Scanner
A port scanner is a tool or program used to identify open ports on a networked device, such as a computer or server. This helps assess the device's security and functionality. In networking, ports are logical access points that allow applications and services to communicate over a network, including the Internet. For example, port 80 is typically used for web traffic (HTTP), while port 443 is designated for secure web traffic (HTTPS).

Our port scanner program scans the top 1,000 commonly used ports of a target IP address. For our testing, we used a vulnerable virtual machine, which can be found at [Vulnerable Virtual Machine - 64BASE](https://www.vulnhub.com/entry/64base-101,173/).

A port scanner serves as a reconnaissance tool for assessing a target system or network.

## Expected Output
Output from industry-leading port scanning software

![expected output](expected_output.png)


## Program Output
Output from our program

![code output](code_output.png)


## Next Steps
- Update the program to allow input of multiple IP addresses. This could include either a range of IP addresses or a list of comma-separated IPs.
- Enhance the program to accept a list of ports for scanning. This can include a single port, a range or list of ports, or multiple specific ports.
- Implement parallel scanning to expedite the overall port scanning process.
- Add functionality to detect if firewalls are blocking the port scan requests and adjust the scanning intervals accordingly.
